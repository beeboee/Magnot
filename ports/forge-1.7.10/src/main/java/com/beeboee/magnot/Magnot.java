package com.beeboee.magnot;

import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.server.MagnotEvents;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(modid = Magnot.MOD_ID, name = Magnot.NAME, version = Magnot.VERSION, acceptedMinecraftVersions = "[1.7.10]")
public final class Magnot {
    public static final String MOD_ID = "magnot";
    public static final String NAME = "Magnot";
    public static final String VERSION = "1.2.0-alpha.1";

    public static Item FERROUS_PASTE;
    public static Item FERROUS_TUBE;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        FERROUS_PASTE = new Item()
                .setUnlocalizedName(MOD_ID + ".ferrous_paste")
                .setTextureName(MOD_ID + ":ferrous_paste")
                .setCreativeTab(CreativeTabs.tabMaterials);
        FERROUS_TUBE = new FerrousTubeItem()
                .setUnlocalizedName(MOD_ID + ".ferrous_tube")
                .setTextureName(MOD_ID + ":ferrous_tube")
                .setCreativeTab(CreativeTabs.tabTools);

        GameRegistry.registerItem(FERROUS_PASTE, "ferrous_paste");
        GameRegistry.registerItem(FERROUS_TUBE, "ferrous_tube");

        GameRegistry.addShapelessRecipe(new ItemStack(FERROUS_PASTE, 8), Items.iron_ingot, Items.slime_ball);
        GameRegistry.addRecipe(new ShapedOreRecipe(
                new ItemStack(FERROUS_TUBE),
                "IP",
                " P",
                'I', "ingotIron",
                'P', FERROUS_PASTE
        ));

        MagnotEvents events = new MagnotEvents();
        MinecraftForge.EVENT_BUS.register(events);
        FMLCommonHandler.instance().bus().register(events);
    }
}
