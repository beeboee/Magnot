package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import net.createmod.catnip.render.BindableTexture;
import net.minecraft.resources.ResourceLocation;

public enum MagnotSpecialTextures implements BindableTexture {
    FERROUS_REGION("ferrous_region.png");

    public static final String ASSET_PATH = "textures/special/";

    private final ResourceLocation location;

    MagnotSpecialTextures(String filename) {
        location = ResourceLocation.fromNamespaceAndPath(Magnot.MOD_ID, ASSET_PATH + filename);
    }

    @Override
    public ResourceLocation getLocation() {
        return location;
    }
}
