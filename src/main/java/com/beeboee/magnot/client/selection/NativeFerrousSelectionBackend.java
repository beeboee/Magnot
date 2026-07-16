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
    private static final double MIN_FACE_EDGE = 1.0E-6D;
    private static final int[][] EDGES = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };
    private static final int[][] FACES = {
            {0, 3, 2, 1},
            {4, 5, 6, 7},
            // Vertical faces start at the top, then travel downward along texture V.
            {5, 1, 0, 4},
            {7, 3, 2, 6},
            {4, 0, 3, 7},
            {6, 2, 1, 5}
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
            renderTiledFace(consumer, matrix, outline.corners(), face, red, green, blue);
        }
        buffers.endBatch(renderType);
    }

    private static void renderTiledFace(VertexConsumer consumer, Matrix4f matrix, Vec3[] corners, int[] face,
                                        int red, int green, int blue) {
        Vec3 origin = corners[face[0]];
        Vec3 vEdge = corners[face[1]].subtract(origin);
        Vec3 diagonal = corners[face[2]].subtract(origin);
        Vec3 uEdge = corners[face[3]].subtract(origin);
        double uLength = uEdge.length();
        double vLength = vEdge.length();
        if (uLength < MIN_FACE_EDGE || vLength < MIN_FACE_EDGE) {
            return;
        }

        Vec3 uDirection = uEdge.scale(1.0D / uLength);
        Vec3 vDirection = vEdge.scale(1.0D / vLength);
        Vec3 normal = vEdge.cross(diagonal).normalize();
        int uTiles = Math.max(1, (int) Math.ceil(uLength - MIN_FACE_EDGE));
        int vTiles = Math.max(1, (int) Math.ceil(vLength - MIN_FACE_EDGE));

        for (int uTile = 0; uTile < uTiles; uTile++) {
            double uStart = uTile;
            double uEnd = Math.min(uTile + 1.0D, uLength);
            float tileU = (float) (uEnd - uStart);

            for (int vTile = 0; vTile < vTiles; vTile++) {
                double vStart = vTile;
                double vEnd = Math.min(vTile + 1.0D, vLength);
                float tileV = (float) (vEnd - vStart);

                Vec3 a = pointOnFace(origin, uDirection, vDirection, uStart, vStart);
                Vec3 b = pointOnFace(origin, uDirection, vDirection, uStart, vEnd);
                Vec3 c = pointOnFace(origin, uDirection, vDirection, uEnd, vEnd);
                Vec3 d = pointOnFace(origin, uDirection, vDirection, uEnd, vStart);

                addFaceVertex(consumer, matrix, a, 0.0F, 0.0F, red, green, blue, normal);
                addFaceVertex(consumer, matrix, b, 0.0F, tileV, red, green, blue, normal);
                addFaceVertex(consumer, matrix, c, tileU, tileV, red, green, blue, normal);
                addFaceVertex(consumer, matrix, d, tileU, 0.0F, red, green, blue, normal);
            }
        }
    }

    private static Vec3 pointOnFace(Vec3 origin, Vec3 uDirection, Vec3 vDirection, double u, double v) {
        return origin.add(uDirection.scale(u)).add(vDirection.scale(v));
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
