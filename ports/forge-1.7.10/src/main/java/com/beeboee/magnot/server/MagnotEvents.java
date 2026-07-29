package com.beeboee.magnot.server;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.network.MagnotNetwork;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class MagnotEvents {
    private static final ConcurrentLinkedQueue<RemovalRequest> REMOVALS =
            new ConcurrentLinkedQueue<RemovalRequest>();

    public static void enqueueRemoval(EntityPlayerMP player, UUID selectedRegionId) {
        REMOVALS.add(new RemovalRequest(player, selectedRegionId));
    }

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }
        RemovalRequest request;
        while ((request = REMOVALS.poll()) != null) {
            RegionActions.remove(request.player, request.selectedRegionId);
        }
    }

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            MagnotNetwork.sync((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            MagnotNetwork.sync((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onLeftClickBlock(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) {
            return;
        }
        ItemStack held = event.entityPlayer.getHeldItem();
        if (held != null && held.getItem() == Magnot.FERROUS_TUBE) {
            event.setCanceled(true);
        }
    }

    private static final class RemovalRequest {
        private final EntityPlayerMP player;
        private final UUID selectedRegionId;

        private RemovalRequest(EntityPlayerMP player, UUID selectedRegionId) {
            this.player = player;
            this.selectedRegionId = selectedRegionId;
        }
    }
}
