package com.beeboee.magnot.registry;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class MagnotItems {
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Magnot.MOD_ID);

    public static final DeferredItem<Item> IRON_DUST = ITEMS.registerSimpleItem("iron_dust");
    public static final DeferredItem<Item> FERROUS_PASTE = ITEMS.registerSimpleItem("ferrous_paste");
    public static final DeferredItem<FerrousTubeItem> FERROUS_TUBE = ITEMS.registerItem(
            "ferrous_tube",
            FerrousTubeItem::new,
            new Item.Properties().stacksTo(1).durability(99)
    );

    private MagnotItems() {
    }

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
