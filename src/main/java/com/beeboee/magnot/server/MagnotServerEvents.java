package com.beeboee.magnot.server;

import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class MagnotServerEvents {
    private MagnotServerEvents() {
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }

        InteractionHand hand = event.getHand();
        ItemStack held = player.getItemInHand(hand);
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return;
        }

        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        boolean removed = FerrousRegionSavedData.get(serverLevel).removeRegionContaining(event.getPos());
        if (removed) {
            event.setCanceled(true);
            player.displayClientMessage(Component.translatable("message.magnot.region_removed"), true);
        }
    }
}
