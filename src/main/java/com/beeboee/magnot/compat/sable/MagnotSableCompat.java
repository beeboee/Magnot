package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class MagnotSableCompat {
    private static final double PATH_BOUNDS_PADDING = 0.5D;

    private MagnotSableCompat() {}

    public static boolean blocksMagnet(ServerLevel level, Vec3 source, Vec3 itemPosition) {
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        Vec3 globalSource = SableCompanion.INSTANCE.projectOutOfSubLevel(level, source);
        Vec3 globalItemPosition = SableCompanion.INSTANCE.projectOutOfSubLevel(level, itemPosition);

        if (data.blocksWorldMagnet(globalSource, globalItemPosition)) {
            return true;
        }

        for (SubLevelAccess subLevel : candidateSubLevels(level, source, itemPosition, globalSource, globalItemPosition)) {
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

        for (SubLevelAccess subLevel : candidateSubLevels(level, from, to, globalFrom, globalTo)) {
            Vec3 localFrom = subLevel.logicalPose().transformPositionInverse(globalFrom);
            Vec3 localTo = subLevel.logicalPose().transformPositionInverse(globalTo);
            Optional<FerrousRegion> removed = FerrousRegionSavedData.get(level).removeSubLevelIntersectingById(selectedRegionId, subLevel.getUniqueId(), localFrom, localTo);
            if (removed.isPresent()) {
                return removed;
            }
        }

        return Optional.empty();
    }

    public static void moveRegionsAfterAssembly(ServerLevel level, SubLevelAssemblyHelper.AssemblyTransform transform, List<BlockPos> blocks) {
        if (blocks.isEmpty()) {
            return;
        }

        SubLevelAccess targetSubLevel = SableCompanion.INSTANCE.getContaining(level, transform.apply(blocks.getFirst()));
        if (targetSubLevel == null) {
            return;
        }

        Set<BlockPos> assembledBlocks = new HashSet<>(blocks);
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        List<FerrousRegion> regionsToMove = data.regions().stream()
                .filter(FerrousRegion::isWorldRegion)
                .filter(region -> intersectsAnyAssembledBlock(region, assembledBlocks))
                .toList();

        if (regionsToMove.isEmpty()) {
            return;
        }

        for (FerrousRegion region : regionsToMove) {
            if (!data.removeRegion(region.id())) {
                continue;
            }

            data.addRegion(transformRegion(region, transform, targetSubLevel.getUniqueId()));
        }

        MagnotNetwork.syncToPlayersInDimension(level);
    }

    private static List<SubLevelAccess> candidateSubLevels(ServerLevel level, Vec3 source, Vec3 itemPosition, Vec3 globalSource, Vec3 globalItemPosition) {
        List<SubLevelAccess> candidates = new ArrayList<>();
        addCandidate(candidates, SableCompanion.INSTANCE.getContaining(level, source));
        addCandidate(candidates, SableCompanion.INSTANCE.getContaining(level, itemPosition));

        AABB pathBounds = new AABB(globalSource, globalItemPosition).inflate(PATH_BOUNDS_PADDING);
        for (SubLevelAccess subLevel : SableCompanion.INSTANCE.getAllIntersecting(level, new BoundingBox3d(pathBounds))) {
            addCandidate(candidates, subLevel);
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

    private static boolean intersectsAnyAssembledBlock(FerrousRegion region, Set<BlockPos> assembledBlocks) {
        for (BlockPos blockPos : BlockPos.betweenClosed(region.min(), region.max())) {
            if (assembledBlocks.contains(blockPos)) {
                return true;
            }
        }

        return false;
    }

    private static FerrousRegion transformRegion(FerrousRegion region, SubLevelAssemblyHelper.AssemblyTransform transform, UUID subLevelId) {
        return FerrousRegion.fromCorners(
                region.id(),
                transform.apply(region.min()),
                transform.apply(region.max()),
                subLevelId
        );
    }
}
