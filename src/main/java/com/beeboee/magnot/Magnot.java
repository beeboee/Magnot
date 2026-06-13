package com.beeboee.magnot;

import com.beeboee.magnot.registry.MagnotItems;
import com.beeboee.magnot.server.MagnotServerEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Magnot.MOD_ID)
public final class Magnot {
    public static final String MOD_ID = "magnot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Magnot(IEventBus modEventBus) {
        MagnotItems.register(modEventBus);
        modEventBus.addListener(this::addCreativeTabContents);
        NeoForge.EVENT_BUS.register(MagnotServerEvents.class);
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
