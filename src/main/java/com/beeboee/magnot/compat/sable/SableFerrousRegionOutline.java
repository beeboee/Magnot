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
        Matrix4d poseD = subLevel.logicalPose().bakeIntoMatrix(new Matrix4d());
        Matrix4f pose = new Matrix4f(
                (float) poseD.m00(), (float) poseD.m01(), (float) poseD.m02(), (float) poseD.m03(),
                (float) poseD.m10(), (float) poseD.m11(), (float) poseD.m12(), (float) poseD.m13(),
                (float) poseD.m20(), (float) poseD.m21(), (float) poseD.m22(), (float) poseD.m23(),
                (float) poseD.m30(), (float) poseD.m31(), (float) poseD.m32(), (float) poseD.m33()
        );

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        poseStack.last().pose().mul(pose);
        super.render(poseStack, buffer, Vec3.ZERO, partialTicks);
        poseStack.popPose();
    }
}
