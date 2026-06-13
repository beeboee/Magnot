package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.region.FerrousRegion;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.Pose3dc;
import net.createmod.catnip.outliner.AABBOutline;
import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniondc;
import org.joml.Quaternionf;
import org.joml.Vector3dc;

public class SableFerrousRegionOutline extends AABBOutline {
    private final SubLevelAccess subLevel;

    public SableFerrousRegionOutline(FerrousRegion region, SubLevelAccess subLevel) {
        super(region.bounds());
        this.subLevel = subLevel;
    }

    @Override
    public void render(PoseStack poseStack, SuperRenderTypeBuffer buffer, Vec3 camera, float partialTicks) {
        Pose3dc pose = subLevel.logicalPose();
        Vector3dc position = pose.position();
        Vector3dc scale = pose.scale();
        Vector3dc rotationPoint = pose.rotationPoint();
        Quaterniondc orientation = pose.orientation();

        poseStack.pushPose();
        poseStack.translate(position.x() - camera.x, position.y() - camera.y, position.z() - camera.z);
        poseStack.mulPose(new Quaternionf(
                (float) orientation.x(),
                (float) orientation.y(),
                (float) orientation.z(),
                (float) orientation.w()
        ));
        poseStack.scale((float) scale.x(), (float) scale.y(), (float) scale.z());
        poseStack.translate(-rotationPoint.x(), -rotationPoint.y(), -rotationPoint.z());
        super.render(poseStack, buffer, Vec3.ZERO, partialTicks);
        poseStack.popPose();
    }
}
