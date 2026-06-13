package com.beeboee.magnot.server;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public final class FerrousParticles {
    private static final BlockParticleOption REDSTONE_BLOCK_PARTICLE = new BlockParticleOption(
            ParticleTypes.BLOCK,
            Blocks.REDSTONE_BLOCK.defaultBlockState()
    );

    private FerrousParticles() {
    }

    public static void spawnRedstoneBlockBreak(ServerLevel level, BlockPos pos) {
        level.sendParticles(
                REDSTONE_BLOCK_PARTICLE,
                pos.getX() + 0.5D,
                pos.getY() + 0.5D,
                pos.getZ() + 0.5D,
                18,
                0.35D,
                0.35D,
                0.35D,
                0.035D
        );
    }

    public static void spawnRedstoneBlockEdges(ServerLevel level, FerrousRegion region) {
        AABB bounds = region.bounds();

        spawnEdge(level, bounds.minX, bounds.minY, bounds.minZ, bounds.maxX, bounds.minY, bounds.minZ);
        spawnEdge(level, bounds.minX, bounds.minY, bounds.maxZ, bounds.maxX, bounds.minY, bounds.maxZ);
        spawnEdge(level, bounds.minX, bounds.maxY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.minZ);
        spawnEdge(level, bounds.minX, bounds.maxY, bounds.maxZ, bounds.maxX, bounds.maxY, bounds.maxZ);

        spawnEdge(level, bounds.minX, bounds.minY, bounds.minZ, bounds.minX, bounds.maxY, bounds.minZ);
        spawnEdge(level, bounds.minX, bounds.minY, bounds.maxZ, bounds.minX, bounds.maxY, bounds.maxZ);
        spawnEdge(level, bounds.maxX, bounds.minY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.minZ);
        spawnEdge(level, bounds.maxX, bounds.minY, bounds.maxZ, bounds.maxX, bounds.maxY, bounds.maxZ);

        spawnEdge(level, bounds.minX, bounds.minY, bounds.minZ, bounds.minX, bounds.minY, bounds.maxZ);
        spawnEdge(level, bounds.maxX, bounds.minY, bounds.minZ, bounds.maxX, bounds.minY, bounds.maxZ);
        spawnEdge(level, bounds.minX, bounds.maxY, bounds.minZ, bounds.minX, bounds.maxY, bounds.maxZ);
        spawnEdge(level, bounds.maxX, bounds.maxY, bounds.minZ, bounds.maxX, bounds.maxY, bounds.maxZ);
    }

    private static void spawnEdge(ServerLevel level, double fromX, double fromY, double fromZ, double toX, double toY, double toZ) {
        double length = Math.sqrt(
                square(toX - fromX)
                        + square(toY - fromY)
                        + square(toZ - fromZ)
        );
        int steps = Math.max(1, Math.min(24, Mth.ceil(length * 2.0D)));

        for (int i = 0; i <= steps; i++) {
            double progress = (double) i / (double) steps;
            double x = Mth.lerp(progress, fromX, toX);
            double y = Mth.lerp(progress, fromY, toY);
            double z = Mth.lerp(progress, fromZ, toZ);
            level.sendParticles(REDSTONE_BLOCK_PARTICLE, x, y, z, 1, 0.035D, 0.035D, 0.035D, 0.015D);
        }
    }

    private static double square(double value) {
        return value * value;
    }
}
