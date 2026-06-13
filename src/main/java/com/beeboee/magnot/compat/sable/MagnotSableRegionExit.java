package com.beeboee.magnot.compat.sable;

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
            if (data.removeRegion(region.id())) {
                data.addRegion(worldRegion);
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

        int[] worldSizes = mapSizes(subLevel, sx, sy, sz);
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

    private static int[] mapSizes(SubLevelAccess subLevel, int sx, int sy, int sz) {
        int[] sizes = new int[]{1, 1, 1};
        boolean[] used = new boolean[]{false, false, false};
        assign(sizes, used, subLevel.logicalPose().transformNormal(new Vec3(1, 0, 0)), sx);
        assign(sizes, used, subLevel.logicalPose().transformNormal(new Vec3(0, 1, 0)), sy);
        assign(sizes, used, subLevel.logicalPose().transformNormal(new Vec3(0, 0, 1)), sz);
        return sizes;
    }

    private static void assign(int[] sizes, boolean[] used, Vec3 axis, int size) {
        int index = strongestUnusedAxis(axis, used);
        sizes[index] = size;
        used[index] = true;
    }

    private static int strongestUnusedAxis(Vec3 axis, boolean[] used) {
        double ax = Math.abs(axis.x);
        double ay = Math.abs(axis.y);
        double az = Math.abs(axis.z);
        int best = -1;
        double strength = -1;
        double[] values = new double[]{ax, ay, az};
        for (int i = 0; i < values.length; i++) {
            if (!used[i] && values[i] > strength) {
                best = i;
                strength = values[i];
            }
        }
        return best >= 0 ? best : 0;
    }
}
