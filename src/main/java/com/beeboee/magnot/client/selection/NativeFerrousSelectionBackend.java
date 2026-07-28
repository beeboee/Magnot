package com.beeboee.magnot.client.selection;

import com.beeboee.magnot.region.FerrousRegion;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
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
    private static final double FACE_OFFSET = 1.0D / 128.0D;
    private static final double INSIDE_EPSILON = 1.0E-5D;
    private static final float LINE_WIDTH_SCALE = 64.0F;
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
                renderFaces(poseStack, buffers, outline, camera);
            }
            renderLines(poseStack, buffers, outline);
        }
        poseStack.popPose();
        RenderSystem.lineWidth(1.0F);
    }

    private static void renderFaces(PoseStack poseStack, MultiBufferSource.BufferSource buffers, Outline outline, Vec3 camera) {
        RenderType renderType = RenderType.entityTranslucent(FerrousSelectionTexture.LOCATION);
        VertexConsumer consumer = buffers.getBuffer(renderType);
        Matrix4f matrix = poseStack.last().pose();
        int red = outline.red();
        int green = outline.green();
        int blue = outline.blue();
        Vec3 center = center(outline.corners());
        boolean cameraInside = contains(outline.corners(), camera);

        for (int[] face : FACES) {
            renderTiledFace(consumer, matrix, outline.corners(), center, camera, cameraInside, face, red, green, blue);
        }
        buffers.endBatch(renderType);
    }

    private static void renderTiledFace(VertexConsumer consumer, Matrix4f matrix, Vec3[] corners, Vec3 center,
                                        Vec3 camera, boolean cameraInside, int[] face,
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
        Vec3 normal = vEdge.cross(diagonal);
        if (normal.lengthSqr() < MIN_FACE_EDGE * MIN_FACE_EDGE) {
            return;
        }
        normal = normal.normalize();

        Vec3 faceCenter = corners[face[0]]
                .add(corners[face[1]])
                .add(corners[face[2]])
                .add(corners[face[3]])
                .scale(0.25D);
        if (normal.dot(faceCenter.subtract(center)) < 0.0D) {
            normal = normal.scale(-1.0D);
        }

        // Create's outline behaves like a one-way shell from outside, while remaining
        // fully visible from inside. The native path reproduces that explicitly because
        // its translucent render type is intentionally double-sided.
        if (!cameraInside && normal.dot(camera.subtract(faceCenter)) <= 0.0D) {
            return;
        }

        Vec3 faceOffset = normal.scale(FACE_OFFSET);
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

                Vec3 a = pointOnFace(origin, uDirection, vDirection, uStart, vStart).add(faceOffset);
                Vec3 b = pointOnFace(origin, uDirection, vDirection, uStart, vEnd).add(faceOffset);
                Vec3 c = pointOnFace(origin, uDirection, vDirection, uEnd, vEnd).add(faceOffset);
                Vec3 d = pointOnFace(origin, uDirection, vDirection, uEnd, vStart).add(faceOffset);

                addFaceVertex(consumer, matrix, a, 0.0F, 0.0F, red, green, blue, normal);
                addFaceVertex(consumer, matrix, b, 0.0F, tileV, red, green, blue, normal);
                addFaceVertex(consumer, matrix, c, tileU, tileV, red, green, blue, normal);
                addFaceVertex(consumer, matrix, d, tileU, 0.0F, red, green, blue, normal);
            }
        }
    }

    /** Supports axis-aligned and transformed Sable parallelepipeds. */
    private static boolean contains(Vec3[] corners, Vec3 point) {
        Vec3 origin = corners[0];
        Vec3 xEdge = corners[1].subtract(origin);
        Vec3 zEdge = corners[3].subtract(origin);
        Vec3 yEdge = corners[4].subtract(origin);
        double determinant = xEdge.dot(zEdge.cross(yEdge));
        if (Math.abs(determinant) < MIN_FACE_EDGE) {
            return false;
        }

        Vec3 relative = point.subtract(origin);
        double x = relative.dot(zEdge.cross(yEdge)) / determinant;
        double z = xEdge.dot(relative.cross(yEdge)) / determinant;
        double y = xEdge.dot(zEdge.cross(relative)) / determinant;
        return unitInterval(x) && unitInterval(y) && unitInterval(z);
    }

    private static boolean unitInterval(double value) {
        return value >= -INSIDE_EPSILON && value <= 1.0D + INSIDE_EPSILON;
    }

    private static Vec3 center(Vec3[] corners) {
        Vec3 center = Vec3.ZERO;
        for (Vec3 corner : corners) {
            center = center.add(corner);
        }
        return center.scale(1.0D / corners.length);
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
        RenderSystem.lineWidth(Math.max(1.0F, outline.lineWidth() * LINE_WIDTH_SCALE));
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
