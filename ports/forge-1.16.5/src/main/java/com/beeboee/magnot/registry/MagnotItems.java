package com.beeboee.magnot.registry;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MagnotItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Magnot.MOD_ID);

    public static final RegistryObject<Item> IRON_DUST = ITEMS.register("iron_dust", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<Item> FERROUS_PASTE = ITEMS.register("ferrous_paste", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MISC)));
    public static final RegistryObject<FerrousTubeItem> FERROUS_TUBE = ITEMS.register("ferrous_tube", () -> new FerrousTubeItem(new Item.Properties().tab(ItemGroup.TAB_TOOLS).stacksTo(1).durability(99)));

    private MagnotItems() {}
    public static void register(IEventBus bus) { ITEMS.register(bus); }
}
