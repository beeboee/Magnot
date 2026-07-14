package com.beeboee.magnot;

import com.beeboee.magnot.registry.MagnotItems;
import com.beeboee.magnot.server.MagnotForgeEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Magnot.MOD_ID)
public final class Magnot {
    public static final String MOD_ID = "magnot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Magnot() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MagnotItems.register(modBus);
        modBus.addListener(this::addCreativeTabContents);
        MinecraftForge.EVENT_BUS.register(MagnotForgeEvents.class);
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(MagnotItems.IRON_DUST.get());
            event.accept(MagnotItems.FERROUS_PASTE.get());
        }
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(MagnotItems.FERROUS_TUBE.get());
        }
    }
}
