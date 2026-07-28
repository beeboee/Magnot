package com.beeboee.magnot.compat.create.client;

import com.beeboee.magnot.client.selection.FerrousSelectionTexture;
import net.createmod.catnip.render.BindableTexture;
import net.minecraft.resources.ResourceLocation;

public enum MagnotCreateTexture implements BindableTexture {
    FERROUS_REGION;

    @Override
    public ResourceLocation getLocation() {
        return FerrousSelectionTexture.LOCATION;
    }
}
