package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.entity.FerrousRegionEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FerrousRegionEntityRenderer extends EntityRenderer<FerrousRegionEntity> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(Magnot.MOD_ID, "textures/misc/empty.png");

    public FerrousRegionEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(FerrousRegionEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
    }

    @Override
    public ResourceLocation getTextureLocation(FerrousRegionEntity entity) {
        return TEXTURE;
    }
}
