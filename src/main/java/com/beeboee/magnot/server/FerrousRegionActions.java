package com.beeboee.magnot.server;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public final class FerrousRegionActions {
    private FerrousRegionActions() {
    }

    public static boolean removeClosestRegion(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.getLookAngle().scale(range));

        Optional<BlockPos> removedAt = FerrousRegionSavedData.get(serverLevel).removeClosestIntersecting(from, to);
        if (removedAt.isEmpty()) {
            return false;
        }

        AllSoundEvents.SLIME_ADDED.play(serverLevel, null, removedAt.get(), 0.5F, 0.5F);
        MagnotNetwork.syncToPlayersInDimension(serverLevel);
        player.displayClientMessage(Component.translatable("message.magnot.region_removed"), true);
        return true;
    }
}
