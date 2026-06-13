package com.beeboee.magnot.server;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public final class FerrousRegionActions {
    private FerrousRegionActions() {
    }

    public static boolean removeSelectedRegion(ServerPlayer player, UUID selectedRegionId) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.getLookAngle().scale(range));

        Optional<FerrousRegion> removed = FerrousRegionSavedData.get(serverLevel).removeIntersectingById(selectedRegionId, from, to);
        if (removed.isEmpty()) {
            return false;
        }

        playRemovalEffects(player, serverLevel, removed.get());
        return true;
    }

    public static boolean removeClosestRegion(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.getLookAngle().scale(range));

        Optional<FerrousRegion> removed = FerrousRegionSavedData.get(serverLevel).removeClosestIntersecting(from, to);
        if (removed.isEmpty()) {
            return false;
        }

        playRemovalEffects(player, serverLevel, removed.get());
        return true;
    }

    private static void playRemovalEffects(ServerPlayer player, ServerLevel serverLevel, FerrousRegion removed) {
        BlockPos soundPos = BlockPos.containing(removed.bounds().getCenter());
        AllSoundEvents.SLIME_ADDED.play(serverLevel, null, soundPos, 0.5F, 0.5F);
        FerrousParticles.spawnRedstoneBlockEdges(serverLevel, removed);
        MagnotNetwork.syncToPlayersInDimension(serverLevel);
        player.displayClientMessage(Component.translatable("message.magnot.region_removed"), true);
    }
}
