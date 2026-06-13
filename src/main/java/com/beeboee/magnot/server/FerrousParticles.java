package com.beeboee.magnot.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;

public final class FerrousParticles {
    private FerrousParticles() {
    }

    public static void spawnRedstoneBlockBreak(ServerLevel level, BlockPos pos) {
        level.sendParticles(
                new BlockParticleOption(ParticleTypes.BLOCK, Blocks.REDSTONE_BLOCK.defaultBlockState()),
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
}
