package com.beeboee.magnot.server;

import com.beeboee.magnot.Magnot;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.neoforged.fml.ModList;

/** Optional sound bridge. Direct Create references live only in compat/create. */
public interface FerrousSelectionEffects {
    FerrousSelectionEffects INSTANCE = load();

    void playFirstCorner(ServerLevel level, BlockPos pos);

    void playConfirmation(ServerLevel level, BlockPos pos);

    void playRemoval(ServerLevel level, BlockPos pos);

    static FerrousSelectionEffects current() {
        return INSTANCE;
    }

    private static FerrousSelectionEffects load() {
        if (ModList.get().isLoaded("create")) {
            try {
                return (FerrousSelectionEffects) Class.forName("com.beeboee.magnot.compat.create.server.CreateFerrousSelectionEffects")
                        .getDeclaredConstructor()
                        .newInstance();
            } catch (ReflectiveOperationException | LinkageError error) {
                Magnot.LOGGER.error("Could not initialize Create selection effects; using vanilla effects", error);
            }
        }
        return new NativeEffects();
    }

    final class NativeEffects implements FerrousSelectionEffects {
        @Override
        public void playFirstCorner(ServerLevel level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.5F, 0.85F);
        }

        @Override
        public void playConfirmation(ServerLevel level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.SLIME_BLOCK_PLACE, SoundSource.BLOCKS, 0.5F, 0.95F);
        }

        @Override
        public void playRemoval(ServerLevel level, BlockPos pos) {
            level.playSound(null, pos, SoundEvents.SLIME_BLOCK_BREAK, SoundSource.BLOCKS, 0.5F, 0.75F);
        }
    }
}
