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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

            if (targetSubLevelId == null) {
                for (FerrousRegion transformedPiece : transformRegionToWorldColumns(region, transform)) {
                    data.addRegion(transformedPiece);
                }
            } else {
                data.addRegion(transformRegion(region, transform, targetSubLevelId));
            }
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

            for (FerrousRegion worldPiece : projectRegionToWorldColumns(region, subLevel)) {
                data.addRegion(worldPiece);
            }
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

    private static List<FerrousRegion> transformRegionToWorldColumns(FerrousRegion region, SubLevelAssemblyHelper.AssemblyTransform transform) {
        Map<ColumnKey, YRange> columns = new HashMap<>();

        for (BlockPos localPos : BlockPos.betweenClosed(region.min(), region.max())) {
            BlockPos worldPos = transform.apply(localPos);
            addColumn(columns, worldPos);
        }

        return columnsToRegions(region.groupId(), columns);
    }

    private static List<FerrousRegion> projectRegionToWorldColumns(FerrousRegion region, SubLevelAccess subLevel) {
        Map<ColumnKey, YRange> columns = new HashMap<>();

        for (BlockPos localPos : BlockPos.betweenClosed(region.min(), region.max())) {
            Vec3 worldCenter = subLevel.logicalPose().transformPosition(Vec3.atCenterOf(localPos));
            BlockPos worldPos = BlockPos.containing(worldCenter);
            addColumn(columns, worldPos);
        }

        return columnsToRegions(region.groupId(), columns);
    }

    private static void addColumn(Map<ColumnKey, YRange> columns, BlockPos pos) {
        ColumnKey key = new ColumnKey(pos.getX(), pos.getZ());
        columns.compute(key, (ignored, range) -> range == null ? new YRange(pos.getY(), pos.getY()) : range.include(pos.getY()));
    }

    private static List<FerrousRegion> columnsToRegions(UUID groupId, Map<ColumnKey, YRange> columns) {
        List<FerrousRegion> pieces = new ArrayList<>(columns.size());
        for (Map.Entry<ColumnKey, YRange> entry : columns.entrySet()) {
            ColumnKey key = entry.getKey();
            YRange range = entry.getValue();
            BlockPos min = new BlockPos(key.x, range.minY, key.z);
            BlockPos max = new BlockPos(key.x, range.maxY, key.z);
            pieces.add(FerrousRegion.fromCorners(UUID.randomUUID(), groupId, min, max, null));
        }
        return pieces;
    }

    private record ColumnKey(int x, int z) {}

    private record YRange(int minY, int maxY) {
        private YRange include(int y) {
            return new YRange(Math.min(minY, y), Math.max(maxY, y));
        }
    }
}
