package com.beeboee.magnot.item;

import com.beeboee.magnot.material.AdaptiveMaterials;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public final class AdaptiveIronDustItem extends Item {
    public AdaptiveIronDustItem(Properties properties){super(properties);}
    @Override public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items){if(AdaptiveMaterials.fallbackDustRequired())super.fillItemCategory(tab,items);}
}
