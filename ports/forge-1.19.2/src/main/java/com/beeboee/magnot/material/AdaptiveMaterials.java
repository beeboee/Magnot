package com.beeboee.magnot.material;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.ModList;

public final class AdaptiveMaterials {
    public static final TagKey<Item> IRON_DUSTS=TagKey.create(Registry.ITEM_REGISTRY,new ResourceLocation("forge","dusts/iron"));
    public static final TagKey<Item> IRON_PLATES=TagKey.create(Registry.ITEM_REGISTRY,new ResourceLocation("forge","plates/iron"));
    private AdaptiveMaterials(){}
    public static boolean externalIronDustAvailable(){return Registry.ITEM.getTag(IRON_DUSTS).map(tag->tag.size()>0).orElse(false);}
    public static boolean fallbackDustRequired(){return ModList.get().isLoaded("create")&&!externalIronDustAvailable();}
}
