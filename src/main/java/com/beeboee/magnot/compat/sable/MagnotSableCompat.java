package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class MagnotSableCompat {
    private MagnotSableCompat() {}

    public static UUID containingSubLevelId(ServerLevel level, BlockPos pos) {
        SubLevelAccess subLevel = SableCompanion.INSTANCE.getContaining(level, pos);
        return subLevel == null ? null : subLevel.getUniqueId();
    }

    public static boolean blocksMagnet(ServerLevel level, Vec3 source, Vec3 itemPosition) {
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        Vec3 globalSource = SableCompanion.INSTANCE.projectOutOfSubLevel(level, source);
        Vec3 globalItemPosition = SableCompanion.INSTANCE.projectOutOfSubLevel(level, itemPosition);

        if (data.blocksWorldMagnet(globalSource, globalItemPosition)) {
            return true;
        }

        for (SubLevelAccess subLevel : candidateSubLevels(level, source, itemPosition)) {
            Vec3 localSource = subLevel.logicalPose().transformPositionInverse(globalSource);
            Vec3 localItemPosition = subLevel.logicalPose().transformPositionInverse(globalItemPosition);

            if (data.blocksSubLevelMagnet(subLevel.getUniqueId(), localSource, localItemPosition)) {
                return true;
            }
        }

        return false;
    }

    public static Optional<FerrousRegion> removeSelectedRegion(ServerLevel level, UUID selectedRegionId, Vec3 from, Vec3 to) {
        Vec3 globalFrom = SableCompanion.INSTANCE.projectOutOfSubLevel(level, from);
        Vec3 globalTo = SableCompanion.INSTANCE.projectOutOfSubLevel(level, to);

        for (SubLevelAccess subLevel : candidateSubLevels(level, from, to)) {
            Vec3 localFrom = subLevel.logicalPose().transformPositionInverse(globalFrom);
            Vec3 localTo = subLevel.logicalPose().transformPositionInverse(globalTo);
            Optional<FerrousRegion> removed = FerrousRegionSavedData.get(level).removeSubLevelIntersectingById(selectedRegionId, subLevel.getUniqueId(), localFrom, localTo);
            if (removed.isPresent()) {
                return removed;
            }
        }

        return Optional.empty();
    }

    public static void moveRegionsAfterSableMove(ServerLevel level, SubLevelAssemblyHelper.AssemblyTransform transform, List<BlockPos> blocks, UUID sourceSubLevelId, UUID targetSubLevelId) {
        if (blocks.isEmpty()) {
            return;
        }

        Set<BlockPos> movedBlocks = new HashSet<>(blocks);
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        List<FerrousRegion> regionsToMove = data.regions().stream()
                .filter(region -> shouldMoveRegion(region, sourceSubLevelId, movedBlocks))
                .toList();

        if (regionsToMove.isEmpty()) {
            return;
        }

        for (FerrousRegion region : regionsToMove) {
            if (!data.removeRegion(region.id())) {
                continue;
            }

            data.addRegion(transformRegion(region, transform, targetSubLevelId));
        }

        MagnotNetwork.syncToPlayersInDimension(level);
    }

    public static void dropRegionsFromSubLevel(ServerLevel level, SubLevelAccess subLevel) {
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        List<FerrousRegion> regionsToDrop = data.regions().stream()
                .filter(region -> region.belongsToSubLevel(subLevel.getUniqueId()))
                .toList();

        if (regionsToDrop.isEmpty()) {
            return;
        }

        for (FerrousRegion region : regionsToDrop) {
            if (!data.removeRegion(region.id())) {
                continue;
            }

            data.addRegion(projectRegionToWorld(region, subLevel));
        }

        MagnotNetwork.syncToPlayersInDimension(level);
    }

    private static boolean shouldMoveRegion(FerrousRegion region, UUID sourceSubLevelId, Set<BlockPos> movedBlocks) {
        if (sourceSubLevelId == null && !region.isWorldRegion()) {
            return false;
        }

        if (sourceSubLevelId != null && !region.belongsToSubLevel(sourceSubLevelId)) {
            return false;
        }

        return intersectsAnyMovedBlock(region, movedBlocks);
    }

    private static List<SubLevelAccess> candidateSubLevels(ServerLevel level, Vec3 source, Vec3 itemPosition) {
        List<SubLevelAccess> candidates = new ArrayList<>();
        addCandidate(candidates, SableCompanion.INSTANCE.getContaining(level, source));
        addCandidate(candidates, SableCompanion.INSTANCE.getContaining(level, itemPosition));

        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container != null) {
            for (SubLevelAccess subLevel : container.getAllSubLevels()) {
                addCandidate(candidates, subLevel);
            }
        }

        return candidates;
    }

    private static void addCandidate(List<SubLevelAccess> candidates, SubLevelAccess candidate) {
        if (candidate == null) {
            return;
        }

        for (SubLevelAccess existing : candidates) {
            if (existing.getUniqueId().equals(candidate.getUniqueId())) {
                return;
            }
        }

        candidates.add(candidate);
    }

    private static boolean intersectsAnyMovedBlock(FerrousRegion region, Set<BlockPos> movedBlocks) {
        for (BlockPos blockPos : BlockPos.betweenClosed(region.min(), region.max())) {
            if (movedBlocks.contains(blockPos)) {
                return true;
            }
        }

        return false;
    }

    private static FerrousRegion transformRegion(FerrousRegion region, SubLevelAssemblyHelper.AssemblyTransform transform, UUID targetSubLevelId) {
        return FerrousRegion.fromCorners(
                region.id(),
                region.groupId(),
                transform.apply(region.min()),
                transform.apply(region.max()),
                targetSubLevelId
        );
    }

    private static FerrousRegion projectRegionToWorld(FerrousRegion region, SubLevelAccess subLevel) {
        Vec3 center = subLevel.logicalPose().transformPosition(region.bounds().getCenter());
        BlockPos centerBlock = BlockPos.containing(center);

        int[] localSizes = new int[]{
                region.max().getX() - region.min().getX() + 1,
                region.max().getY() - region.min().getY() + 1,
                region.max().getZ() - region.min().getZ() + 1
        };
        int[] worldSizes = new int[]{1, 1, 1};
        boolean[] usedWorldAxes = new boolean[]{false, false, false};

        assignProjectedAxis(worldSizes, usedWorldAxes, subLevel.logicalPose().transformNormal(new Vec3(1.0D, 0.0D, 0.0D)), localSizes[0]);
        assignProjectedAxis(worldSizes, usedWorldAxes, subLevel.logicalPose().transformNormal(new Vec3(0.0D, 1.0D, 0.0D)), localSizes[1]);
        assignProjectedAxis(worldSizes, usedWorldAxes, subLevel.logicalPose().transformNormal(new Vec3(0.0D, 0.0D, 1.0D)), localSizes[2]);

        BlockPos min = new BlockPos(
                centerBlock.getX() - (worldSizes[0] - 1) / 2,
                centerBlock.getY() - (worldSizes[1] - 1) / 2,
                centerBlock.getZ() - (worldSizes[2] - 1) / 2
        );
        BlockPos max = new BlockPos(
                min.getX() + worldSizes[0] - 1,
                min.getY() + worldSizes[1] - 1,
                min.getZ() + worldSizes[2] - 1
        );

        return FerrousRegion.fromCorners(region.id(), region.groupId(), min, max, null);
    }

    private static void assignProjectedAxis(int[] worldSizes, boolean[] usedWorldAxes, Vec3 projectedAxis, int size) {
        int worldAxis = strongestUnusedAxis(projectedAxis, usedWorldAxes);
        worldSizes[worldAxis] = size;
        usedWorldAxes[worldAxis] = true;
    }

    private static int strongestUnusedAxis(Vec3 axis, boolean[] usedWorldAxes) {
        double[] strength = new double[]{Math.abs(axis.x), Math.abs(axis.y), Math.abs(axis.z)};
        int bestAxis = -1;
        double bestStrength = -1.0D;

        for (int i = 0; i < strength.length; i++) {
            if (usedWorldAxes[i]) {
                continue;
            }

            if (strength[i] > bestStrength) {
                bestAxis = i;
                bestStrength = strength[i];
            }
        }

        if (bestAxis >= 0) {
            return bestAxis;
        }

        return strength[0] >= strength[1] && strength[0] >= strength[2] ? 0 : strength[1] >= strength[2] ? 1 : 2;
    }
}
