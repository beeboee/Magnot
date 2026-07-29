package com.beeboee.magnot.server;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber(modid = Magnot.MOD_ID)
public final class MagnotServerEvents {
    private MagnotServerEvents() {
    }

    @SubscribeEvent
    public static void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            MagnotNetwork.sync((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void onChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.player instanceof EntityPlayerMP) {
            MagnotNetwork.sync((EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getHand() == EnumHand.MAIN_HAND
                && event.getEntityPlayer().getHeldItemMainhand().getItem() == MagnotItems.FERROUS_TUBE) {
            event.setCanceled(true);
        }
    }
}
