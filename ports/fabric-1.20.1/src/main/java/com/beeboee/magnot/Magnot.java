package com.beeboee.magnot;

import com.beeboee.magnot.material.AdaptiveMaterials;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.registry.MagnotItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.world.item.CreativeModeTabs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Magnot implements ModInitializer {
    public static final String MOD_ID = "magnot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        MagnotItems.register();
        MagnotNetwork.registerServer();
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.INGREDIENTS).register(entries -> {
            if (AdaptiveMaterials.fallbackDustRequired()) entries.accept(MagnotItems.IRON_DUST);
            entries.accept(MagnotItems.FERROUS_PASTE);
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES)
                .register(entries -> entries.accept(MagnotItems.FERROUS_TUBE));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> MagnotNetwork.syncTo(handler.player));
        ServerEntityWorldChangeEvents.AFTER_PLAYER_CHANGE_WORLD.register(
                (player, origin, destination) -> MagnotNetwork.syncTo(player)
        );
    }
}
