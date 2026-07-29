package com.beeboee.magnot.material;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class AdaptiveMaterials {
    public static final TagKey<Item> IRON_DUSTS = TagKey.create(
            BuiltInRegistries.ITEM.key(), new ResourceLocation("magnot", "iron_dusts")
    );
    public static final TagKey<Item> IRON_PLATES = TagKey.create(
            BuiltInRegistries.ITEM.key(), new ResourceLocation("magnot", "iron_plates")
    );

    private AdaptiveMaterials() {
    }

    public static boolean externalIronDustAvailable() {
        return BuiltInRegistries.ITEM.getTag(IRON_DUSTS).map(tag -> tag.size() > 0).orElse(false);
    }

    public static boolean ironPlateAvailable() {
        return BuiltInRegistries.ITEM.getTag(IRON_PLATES).map(tag -> tag.size() > 0).orElse(false);
    }

    public static boolean fallbackDustRequired() {
        return FabricLoader.getInstance().isModLoaded("create") && !externalIronDustAvailable();
    }
}
