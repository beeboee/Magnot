package com.beeboee.magnot.compat.sable;

import com.mojang.blaze3d.vertex.PoseStack;
import com.beeboee.magnot.region.FerrousRegion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.createmod.catnip.outliner.AABBOutline;
import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4d;
import org.joml.Matrix4f;

public class SableFerrousRegionOutline extends AABBOutline {
    private final SubLevelAccess subLevel;

    public SableFerrousRegionOutline(FerrousRegion region, SubLevelAccess subLevel) {
        super(region.bounds());
        this.subLevel = subLevel;
    }

    @Override
    public void render(PoseStack poseStack, SuperRenderTypeBuffer buffer, Vec3 camera, float partialTicks) {
        Matrix4f pose = subLevel.logicalPose()
                .bakeIntoMatrix(new Matrix4d())
                .get(new Matrix4f());

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        poseStack.mulPoseMatrix(pose);
        super.render(poseStack, buffer, Vec3.ZERO, partialTicks);
        poseStack.popPose();
    }
}
