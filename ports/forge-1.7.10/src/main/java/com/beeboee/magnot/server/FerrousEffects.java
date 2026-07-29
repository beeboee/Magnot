package com.beeboee.magnot.server;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.world.WorldServer;

public final class FerrousEffects {
    private FerrousEffects() {
    }

    public static void spawnCorner(WorldServer world, int x, int y, int z) {
        world.func_147487_a(
                "reddust",
                x + 0.5D,
                y + 0.5D,
                z + 0.5D,
                12,
                0.35D,
                0.35D,
                0.35D,
                0.0D
        );
    }

    public static void spawnRegion(WorldServer world, FerrousRegion region) {
        double minX = region.getMinX();
        double minY = region.getMinY();
        double minZ = region.getMinZ();
        double maxX = region.getMaxX() + 1.0D;
        double maxY = region.getMaxY() + 1.0D;
        double maxZ = region.getMaxZ() + 1.0D;

        edge(world, minX, minY, minZ, maxX, minY, minZ);
        edge(world, minX, maxY, minZ, maxX, maxY, minZ);
        edge(world, minX, minY, maxZ, maxX, minY, maxZ);
        edge(world, minX, maxY, maxZ, maxX, maxY, maxZ);
        edge(world, minX, minY, minZ, minX, maxY, minZ);
        edge(world, maxX, minY, minZ, maxX, maxY, minZ);
        edge(world, minX, minY, maxZ, minX, maxY, maxZ);
        edge(world, maxX, minY, maxZ, maxX, maxY, maxZ);
        edge(world, minX, minY, minZ, minX, minY, maxZ);
        edge(world, maxX, minY, minZ, maxX, minY, maxZ);
        edge(world, minX, maxY, minZ, minX, maxY, maxZ);
        edge(world, maxX, maxY, minZ, maxX, maxY, maxZ);
    }

    private static void edge(
            WorldServer world,
            double x1,
            double y1,
            double z1,
            double x2,
            double y2,
            double z2
    ) {
        for (int index = 0; index <= 4; index++) {
            double progress = index / 4.0D;
            world.func_147487_a(
                    "reddust",
                    x1 + (x2 - x1) * progress,
                    y1 + (y2 - y1) * progress,
                    z1 + (z2 - z1) * progress,
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }
}
