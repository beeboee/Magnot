package com.beeboee.magnot.registry;

import com.beeboee.magnot.Magnot;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

@Mod.EventBusSubscriber(modid = Magnot.MOD_ID)
public final class MagnotRecipes {
    private static final String IRON_DUST = "dustIron";
    private static final String IRON_PLATE = "plateIron";
    private static final String IRON_INGOT = "ingotIron";
    private static final String IRON_NUGGET = "nuggetIron";

    private MagnotRecipes() {
    }

    @SubscribeEvent
    public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
        ResourceLocation group = new ResourceLocation(Magnot.MOD_ID, "materials");

        IRecipe paste;
        if (available(IRON_DUST)) {
            paste = new ShapelessOreRecipe(
                    group,
                    new ItemStack(MagnotItems.FERROUS_PASTE),
                    IRON_DUST,
                    Items.SLIME_BALL
            );
        } else {
            paste = new ShapelessOreRecipe(
                    group,
                    new ItemStack(MagnotItems.FERROUS_PASTE),
                    Items.IRON_NUGGET, Items.IRON_NUGGET, Items.IRON_NUGGET, Items.IRON_NUGGET,
                    Items.IRON_NUGGET, Items.IRON_NUGGET, Items.IRON_NUGGET, Items.IRON_NUGGET,
                    Items.SLIME_BALL
            );
        }
        paste.setRegistryName(new ResourceLocation(Magnot.MOD_ID, "ferrous_paste"));

        IRecipe tube;
        if (available(IRON_PLATE)) {
            tube = new ShapedOreRecipe(
                    group,
                    new ItemStack(MagnotItems.FERROUS_TUBE),
                    "PS",
                    "NP",
                    'P', MagnotItems.FERROUS_PASTE,
                    'S', IRON_PLATE,
                    'N', IRON_NUGGET
            );
        } else {
            tube = new ShapedOreRecipe(
                    group,
                    new ItemStack(MagnotItems.FERROUS_TUBE),
                    "PI",
                    "NP",
                    'P', MagnotItems.FERROUS_PASTE,
                    'I', available(IRON_INGOT) ? IRON_INGOT : Items.IRON_INGOT,
                    'N', available(IRON_NUGGET) ? IRON_NUGGET : Items.IRON_NUGGET
            );
        }
        tube.setRegistryName(new ResourceLocation(Magnot.MOD_ID, "ferrous_tube"));

        event.getRegistry().registerAll(paste, tube);
    }

    private static boolean available(String oreName) {
        return OreDictionary.doesOreNameExist(oreName)
                && !OreDictionary.getOres(oreName, false).isEmpty();
    }
}
