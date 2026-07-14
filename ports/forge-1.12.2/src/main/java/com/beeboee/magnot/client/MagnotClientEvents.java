package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(modid = Magnot.MOD_ID, value = Side.CLIENT)
public final class MagnotClientEvents {
    private MagnotClientEvents() {
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        register(MagnotItems.IRON_DUST);
        register(MagnotItems.FERROUS_PASTE);
        register(MagnotItems.FERROUS_TUBE);
    }

    private static void register(Item item) {
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(item.getRegistryName(), "inventory")
        );
    }
}
