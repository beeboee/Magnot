package com.beeboee.magnot.registry;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.core.Registry;

public final class MagnotItems {
    public static final Item IRON_DUST = new Item(new Item.Properties());
    public static final Item FERROUS_PASTE = new Item(new Item.Properties());
    public static final FerrousTubeItem FERROUS_TUBE = new FerrousTubeItem(new Item.Properties().stacksTo(1).durability(99));

    private MagnotItems() {
    }

    public static void register() {
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Magnot.MOD_ID, "iron_dust"), IRON_DUST);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Magnot.MOD_ID, "ferrous_paste"), FERROUS_PASTE);
        Registry.register(BuiltInRegistries.ITEM, new ResourceLocation(Magnot.MOD_ID, "ferrous_tube"), FERROUS_TUBE);
    }
}
