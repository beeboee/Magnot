package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.debug.MagnotDebug;
import com.beeboee.magnot.entity.FerrousRegionEntities;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
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

    public static boolean containsPoint(ServerLevel level, Vec3 point) {
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        Vec3 globalPoint = SableCompanion.INSTANCE.projectOutOfSubLevel(level, point);

        if (data.containsWorldPoint(globalPoint)) {
            return true;
        }

        for (SubLevelAccess subLevel : candidateSubLevels(level, point, point)) {
            Vec3 localPoint = subLevel.logicalPose().transformPositionInverse(globalPoint);
            if (data.containsSubLevelPoint(subLevel.getUniqueId(), localPoint)) {
                return true;
            }
        }

        return false;
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

    public static boolean blocksItemPull(ServerLevel level, Vec3 source, ItemEntity item) {
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        Vec3 itemPosition = item.position().add(0.0D, item.getBbHeight() / 2.0D, 0.0D);
        ItemStack itemStack = item.getItem();
        Vec3 globalSource = SableCompanion.INSTANCE.projectOutOfSubLevel(level, source);
        Vec3 globalItemPosition = SableCompanion.INSTANCE.projectOutOfSubLevel(level, itemPosition);

        if (data.blocksWorldItemPull(globalSource, globalItemPosition, itemStack)) {
            return true;
        }

        for (SubLevelAccess subLevel : candidateSubLevels(level, source, itemPosition)) {
            Vec3 localSource = subLevel.logicalPose().transformPositionInverse(globalSource);
            Vec3 localItemPosition = subLevel.logicalPose().transformPositionInverse(globalItemPosition);

            if (data.blocksSubLevelItemPull(subLevel.getUniqueId(), localSource, localItemPosition, itemStack)) {
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
                MagnotDebug.region("remove-sable", removed.get());
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

        MagnotDebug.log("sable-move blocks={} sourceSub={} targetSub={} matchedRegions={}",
                blocks.size(),
                MagnotDebug.shortId(sourceSubLevelId),
                MagnotDebug.shortId(targetSubLevelId),
                regionsToMove.size()
        );

        if (regionsToMove.isEmpty()) {
            return;
        }

        for (FerrousRegion region : regionsToMove) {
            FerrousRegion transformed = transformRegion(region, transform, targetSubLevelId);
            MagnotDebug.region("sable-move-before", region);
            MagnotDebug.region("sable-move-after", transformed);

            if (!data.removeRegion(region.id())) {
                continue;
            }

            data.addRegion(transformed);
            FerrousRegionEntities.replace(level, region, transformed);
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
        AABB bounds = transformBounds(region.bounds(), transform);
        BlockPos min = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos max = BlockPos.containing(bounds.maxX - 1.0E-6D, bounds.maxY - 1.0E-6D, bounds.maxZ - 1.0E-6D);
        return FerrousRegion.fromCorners(region.id(), region.groupId(), min, max, targetSubLevelId, region.filterStack(), region.whitelistMode());
    }

    private static AABB transformBounds(AABB bounds, SubLevelAssemblyHelper.AssemblyTransform transform) {
        AABB transformed = null;
        for (double x : new double[]{bounds.minX, bounds.maxX}) {
            for (double y : new double[]{bounds.minY, bounds.maxY}) {
                for (double z : new double[]{bounds.minZ, bounds.maxZ}) {
                    Vec3 corner = transform.apply(new Vec3(x, y, z));
                    AABB point = new AABB(corner, corner);
                    transformed = transformed == null ? point : transformed.minmax(point);
                }
            }
        }
        return transformed == null ? bounds : transformed;
    }
}
