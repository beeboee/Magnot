package com.beeboee.magnot.registry;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Magnot.MOD_ID)
public final class MagnotItems {
    public static final Item IRON_DUST = new Item()
            .setRegistryName(Magnot.MOD_ID, "iron_dust")
            .setTranslationKey(Magnot.MOD_ID + ".iron_dust")
            .setCreativeTab(CreativeTabs.MATERIALS);

    public static final Item FERROUS_PASTE = new Item()
            .setRegistryName(Magnot.MOD_ID, "ferrous_paste")
            .setTranslationKey(Magnot.MOD_ID + ".ferrous_paste")
            .setCreativeTab(CreativeTabs.MATERIALS);

    public static final FerrousTubeItem FERROUS_TUBE = (FerrousTubeItem) new FerrousTubeItem()
            .setRegistryName(Magnot.MOD_ID, "ferrous_tube")
            .setTranslationKey(Magnot.MOD_ID + ".ferrous_tube")
            .setCreativeTab(CreativeTabs.TOOLS);

    private MagnotItems() {
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(IRON_DUST, FERROUS_PASTE, FERROUS_TUBE);
    }
}
