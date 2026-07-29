package com.beeboee.magnot.server;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

public final class FerrousEffects {
    private static final DustParticleOptions FERROUS_DUST =
            new DustParticleOptions(new Vector3f(0.741F, 0.145F, 0.216F), 1.0F);

    private FerrousEffects() {
    }

    public static void firstCorner(ServerLevel level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.5F, 0.85F);
        blockParticles(level, pos);
    }

    public static void confirmation(ServerLevel level, BlockPos pos, FerrousRegion region) {
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.5F, 0.95F);
        regionEdges(level, region);
    }

    public static void removal(ServerLevel level, FerrousRegion region) {
        BlockPos pos = BlockPos.containing(region.bounds().getCenter());
        level.playSound(null, pos, SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS, 0.5F, 0.75F);
        regionEdges(level, region);
    }

    private static void blockParticles(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < 12; i++) {
            double x = pos.getX() + level.random.nextDouble();
            double y = pos.getY() + level.random.nextDouble();
            double z = pos.getZ() + level.random.nextDouble();
            level.sendParticles(FERROUS_DUST, x, y, z, 1, 0.0D, 0.0D, 0.0D, 0.0D);
        }
    }

    private static void regionEdges(ServerLevel level, FerrousRegion region) {
        AABB box = region.bounds();
        line(level, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ);
        line(level, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
        line(level, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ);
        line(level, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        line(level, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ);
        line(level, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
        line(level, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ);
        line(level, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        line(level, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ);
        line(level, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
        line(level, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ);
        line(level, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    private static void line(ServerLevel level, double x1, double y1, double z1,
                             double x2, double y2, double z2) {
        int steps = Math.max(4, (int) Math.ceil(Math.max(
                Math.abs(x2 - x1),
                Math.max(Math.abs(y2 - y1), Math.abs(z2 - z1))
        ) * 2.0D));
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            level.sendParticles(
                    FERROUS_DUST,
                    x1 + (x2 - x1) * t,
                    y1 + (y2 - y1) * t,
                    z1 + (z2 - z1) * t,
                    1, 0.0D, 0.0D, 0.0D, 0.0D
            );
        }
    }
}
