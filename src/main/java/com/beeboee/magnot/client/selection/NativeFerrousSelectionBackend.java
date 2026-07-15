package com.beeboee.magnot.client.selection;

import com.beeboee.magnot.region.FerrousRegion;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Self-contained fallback inspired by Create's MIT-licensed glue outline flow.
 * See THIRD_PARTY_NOTICES.md for attribution.
 * It intentionally uses only Magnot artwork and Minecraft rendering APIs.
 */
public final class NativeFerrousSelectionBackend implements FerrousSelectionBackend {
    private static final int[][] EDGES = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };
    private static final int[][] FACES = {
            {0, 3, 2, 1},
            {4, 5, 6, 7},
            {0, 1, 5, 4},
            {3, 7, 6, 2},
            {0, 4, 7, 3},
            {1, 2, 6, 5}
    };
    private final Map<Object, Outline> outlines = new LinkedHashMap<>();

    @Override
    public String name() {
        return "native";
    }

    @Override
    public void beginFrame() {
        outlines.clear();
    }

    @Override
    public void showOutline(Level level, Object slot, FerrousRegion region, FerrousSelectionView view, int color, boolean textured, float lineWidth) {
        outlines.put(slot, new Outline(view.corners(), color, textured, lineWidth));
    }

    @Override
    public void render(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS || outlines.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Vec3 camera = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffers = minecraft.renderBuffers().bufferSource();

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        for (Outline outline : outlines.values()) {
            if (outline.textured()) {
                renderFaces(poseStack, buffers, outline);
            }
            renderLines(poseStack, buffers, outline);
        }
        poseStack.popPose();
        RenderSystem.lineWidth(1.0F);
    }

    private static void renderFaces(PoseStack poseStack, MultiBufferSource.BufferSource buffers, Outline outline) {
        RenderType renderType = RenderType.entityTranslucent(FerrousSelectionTexture.LOCATION);
        VertexConsumer consumer = buffers.getBuffer(renderType);
        Matrix4f matrix = poseStack.last().pose();
        int red = outline.red();
        int green = outline.green();
        int blue = outline.blue();

        for (int[] face : FACES) {
            Vec3 a = outline.corners()[face[0]];
            Vec3 b = outline.corners()[face[1]];
            Vec3 c = outline.corners()[face[2]];
            Vec3 normal = b.subtract(a).cross(c.subtract(a)).normalize();
            addFaceVertex(consumer, matrix, a, 0.0F, 0.0F, red, green, blue, normal);
            addFaceVertex(consumer, matrix, b, 0.0F, 1.0F, red, green, blue, normal);
            addFaceVertex(consumer, matrix, c, 1.0F, 1.0F, red, green, blue, normal);
            addFaceVertex(consumer, matrix, outline.corners()[face[3]], 1.0F, 0.0F, red, green, blue, normal);
        }
        buffers.endBatch(renderType);
    }

    private static void addFaceVertex(VertexConsumer consumer, Matrix4f matrix, Vec3 point, float u, float v, int red, int green, int blue, Vec3 normal) {
        consumer.addVertex(matrix, (float) point.x, (float) point.y, (float) point.z)
                .setColor(red, green, blue, 72)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setUv2(LightTexture.FULL_BRIGHT & 0xFFFF, LightTexture.FULL_BRIGHT >> 16)
                .setNormal((float) normal.x, (float) normal.y, (float) normal.z);
    }

    private static void renderLines(PoseStack poseStack, MultiBufferSource.BufferSource buffers, Outline outline) {
        RenderSystem.lineWidth(outline.lineWidth() >= 1.0F / 32.0F ? 3.0F : 1.0F);
        RenderType renderType = RenderType.lines();
        VertexConsumer consumer = buffers.getBuffer(renderType);
        Matrix4f matrix = poseStack.last().pose();
        float red = outline.red() / 255.0F;
        float green = outline.green() / 255.0F;
        float blue = outline.blue() / 255.0F;

        for (int[] edge : EDGES) {
            Vec3 from = outline.corners()[edge[0]];
            Vec3 to = outline.corners()[edge[1]];
            Vec3 normal = to.subtract(from).normalize();
            consumer.addVertex(matrix, (float) from.x, (float) from.y, (float) from.z)
                    .setColor(red, green, blue, 1.0F)
                    .setNormal(poseStack.last(), (float) normal.x, (float) normal.y, (float) normal.z);
            consumer.addVertex(matrix, (float) to.x, (float) to.y, (float) to.z)
                    .setColor(red, green, blue, 1.0F)
                    .setNormal(poseStack.last(), (float) normal.x, (float) normal.y, (float) normal.z);
        }
        buffers.endBatch(renderType);
    }

    private record Outline(Vec3[] corners, int color, boolean textured, float lineWidth) {
        int red() {
            return color >> 16 & 255;
        }

        int green() {
            return color >> 8 & 255;
        }

        int blue() {
            return color & 255;
        }
    }
}
