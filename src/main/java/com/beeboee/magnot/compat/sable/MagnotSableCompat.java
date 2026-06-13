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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class MagnotSableCompat {
    private static final double PATH_BOUNDS_PADDING = 0.5D;

    private MagnotSableCompat() {}

    public static boolean blocksMagnet(ServerLevel level, Vec3 source, Vec3 itemPosition) {
        AABB pathBounds = new AABB(source, itemPosition).inflate(PATH_BOUNDS_PADDING);

        for (SubLevelAccess subLevel : SableCompanion.INSTANCE.getAllIntersecting(level, new BoundingBox3d(pathBounds))) {
            Vec3 localSource = subLevel.logicalPose().transformPositionInverse(source);
            Vec3 localItemPosition = subLevel.logicalPose().transformPositionInverse(itemPosition);

            if (FerrousRegionSavedData.get(level).blocksMagnet(localSource, localItemPosition)) {
                return true;
            }
        }

        return false;
    }

    public static Optional<FerrousRegion> removeSelectedRegion(ServerLevel level, UUID selectedRegionId, Vec3 from, Vec3 to) {
        AABB pathBounds = new AABB(from, to).inflate(PATH_BOUNDS_PADDING);

        for (SubLevelAccess subLevel : SableCompanion.INSTANCE.getAllIntersecting(level, new BoundingBox3d(pathBounds))) {
            Vec3 localFrom = subLevel.logicalPose().transformPositionInverse(from);
            Vec3 localTo = subLevel.logicalPose().transformPositionInverse(to);
            Optional<FerrousRegion> removed = FerrousRegionSavedData.get(level).removeIntersectingById(selectedRegionId, localFrom, localTo);
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

        Set<BlockPos> assembledBlocks = new HashSet<>(blocks);
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        List<FerrousRegion> regionsToMove = data.regions().stream()
                .filter(region -> intersectsAnyAssembledBlock(region, assembledBlocks))
                .toList();

        if (regionsToMove.isEmpty()) {
            return;
        }

        for (FerrousRegion region : regionsToMove) {
            if (!data.removeRegion(region.id())) {
                continue;
            }

            data.addRegion(transformRegion(region, transform));
        }

        MagnotNetwork.syncToPlayersInDimension(level);
    }

    private static boolean intersectsAnyAssembledBlock(FerrousRegion region, Set<BlockPos> assembledBlocks) {
        for (BlockPos blockPos : BlockPos.betweenClosed(region.min(), region.max()).map(BlockPos::immutable).toList()) {
            if (assembledBlocks.contains(blockPos)) {
                return true;
            }
        }

        return false;
    }

    private static FerrousRegion transformRegion(FerrousRegion region, SubLevelAssemblyHelper.AssemblyTransform transform) {
        return FerrousRegion.fromCorners(
                region.id(),
                transform.apply(region.min()),
                transform.apply(region.max())
        );
    }
}
