package com.beeboee.magnot.server;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;

public final class FerrousEffects {
    private FerrousEffects() {
    }

    public static void spawnCornerParticles(WorldServer world, BlockPos pos) {
        world.spawnParticle(
                EnumParticleTypes.REDSTONE,
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D,
                12,
                0.35D,
                0.35D,
                0.35D,
                0.0D
        );
    }

    public static void spawnRegionParticles(WorldServer world, FerrousRegion region) {
        AxisAlignedBB box = region.bounds();
        edge(world, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ);
        edge(world, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
        edge(world, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ);
        edge(world, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        edge(world, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ);
        edge(world, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
        edge(world, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ);
        edge(world, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        edge(world, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ);
        edge(world, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
        edge(world, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ);
        edge(world, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    private static void edge(
            WorldServer world,
            double x1, double y1, double z1,
            double x2, double y2, double z2
    ) {
        int steps = 4;
        for (int index = 0; index <= steps; index++) {
            double progress = index / (double) steps;
            world.spawnParticle(
                    EnumParticleTypes.REDSTONE,
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
