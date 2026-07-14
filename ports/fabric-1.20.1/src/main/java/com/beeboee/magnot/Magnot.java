package com.beeboee.magnot;

import com.beeboee.magnot.registry.MagnotItems;
import com.beeboee.magnot.server.MagnotFabricEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Magnot implements ModInitializer {
    public static final String MOD_ID = "magnot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MagnotItems.register();
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            entries.accept(MagnotItems.IRON_DUST);
            entries.accept(MagnotItems.FERROUS_PASTE);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(MagnotItems.FERROUS_TUBE));
        ServerTickEvents.END_SERVER_TICK.register(MagnotFabricEvents::endServerTick);
    }
}
