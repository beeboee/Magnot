package com.beeboee.magnot.compat.create.server;

import com.beeboee.magnot.server.FerrousSelectionEffects;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public final class CreateFerrousSelectionEffects implements FerrousSelectionEffects {
    @Override
    public void playFirstCorner(ServerLevel level, BlockPos pos) {
        AllSoundEvents.SLIME_ADDED.play(level, null, pos, 0.5F, 0.85F);
    }

    @Override
    public void playConfirmation(ServerLevel level, BlockPos pos) {
        AllSoundEvents.SLIME_ADDED.play(level, null, pos, 0.5F, 0.95F);
    }

    @Override
    public void playRemoval(ServerLevel level, BlockPos pos) {
        AllSoundEvents.SLIME_ADDED.play(level, null, pos, 0.5F, 0.5F);
    }
}
