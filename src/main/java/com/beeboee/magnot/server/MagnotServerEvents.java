package com.beeboee.magnot.server;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.registry.MagnotItems;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class MagnotServerEvents {
    private MagnotServerEvents() {
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack held = player.getItemInHand(hand);
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.getLookAngle().scale(range));

        var removedAt = FerrousRegionSavedData.get(serverLevel).removeClosestIntersecting(from, to);
        if (removedAt.isPresent()) {
            event.setCanceled(true);
            AllSoundEvents.SLIME_ADDED.play(serverLevel, null, removedAt.get(), 0.5F, 0.5F);
            MagnotNetwork.syncToPlayersInDimension(serverLevel);
            player.displayClientMessage(Component.translatable("message.magnot.region_removed"), true);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            MagnotNetwork.syncTo(serverPlayer);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof ServerPlayer serverPlayer) {
            MagnotNetwork.syncTo(serverPlayer);
        }
    }
}
