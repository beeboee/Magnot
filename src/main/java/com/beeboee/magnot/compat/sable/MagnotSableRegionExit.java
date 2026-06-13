package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.debug.MagnotDebug;
import com.beeboee.magnot.entity.FerrousRegionEntities;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class MagnotSableRegionExit {
    private MagnotSableRegionExit() {}

    public static void handle(ServerLevel level, SubLevelAccess subLevel) {
        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        List<FerrousRegion> regions = data.regions().stream()
                .filter(region -> region.belongsToSubLevel(subLevel.getUniqueId()))
                .toList();

        for (FerrousRegion region : regions) {
            FerrousRegion worldRegion = toWorldRegion(region, subLevel);
            MagnotDebug.region("sable-exit-before", region);
            MagnotDebug.region("sable-exit-after", worldRegion);

            if (data.removeRegion(region.id())) {
                data.addRegion(worldRegion);
                FerrousRegionEntities.replace(level, region, worldRegion);
            }
        }

        if (!regions.isEmpty()) {
            MagnotNetwork.syncToPlayersInDimension(level);
        }
    }

    private static FerrousRegion toWorldRegion(FerrousRegion region, SubLevelAccess subLevel) {
        Vec3 center = subLevel.logicalPose().transformPosition(region.bounds().getCenter());
        BlockPos centerBlock = BlockPos.containing(center);

        int sx = region.max().getX() - region.min().getX() + 1;
        int sy = region.max().getY() - region.min().getY() + 1;
        int sz = region.max().getZ() - region.min().getZ() + 1;

        BlockPos min = new BlockPos(
                centerBlock.getX() - (sx - 1) / 2,
                centerBlock.getY() - (sy - 1) / 2,
                centerBlock.getZ() - (sz - 1) / 2
        );
        BlockPos max = new BlockPos(
                min.getX() + sx - 1,
                min.getY() + sy - 1,
                min.getZ() + sz - 1
        );

        return FerrousRegion.fromCorners(region.id(), region.groupId(), min, max, null);
    }
}
