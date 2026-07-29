package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Magnot.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class MagnotClientEvents {
    private static final Object PREVIEW_SLOT = new Object();
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Magnot.MOD_ID, "textures/special/ferrous_region.png");
    private static final int FERROUS_RED = 0xBD2537;
    private static final int LIMIT_YELLOW = 0xFFD43B;
    private static final double REVEAL_RADIUS_SQR = 25.0D * 25.0D;
    private static final double FACE_OFFSET = 1.0D / 128.0D;
    private static final float FADE_SECONDS = 0.18F;
    private static final float MIN_STRENGTH = 1.0E-3F;
    private static final int[][] EDGES = {
            {0, 1}, {1, 2}, {2, 3}, {3, 0},
            {4, 5}, {5, 6}, {6, 7}, {7, 4},
            {0, 4}, {1, 5}, {2, 6}, {3, 7}
    };
    private static final int[][] FACES = {
            {0, 3, 2, 1}, {4, 5, 6, 7},
            {5, 1, 0, 4}, {7, 3, 2, 6},
            {4, 0, 3, 7}, {6, 2, 1, 5}
    };
    private static final Map<Object, Outline> OUTLINES = new LinkedHashMap<>();
    private static long nextRemovalTick;
    private static long lastRenderNanos;

    private MagnotClientEvents() {
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        for (Outline outline : OUTLINES.values()) outline.beginFrame();

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) return;

        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) return;

        Optional<FerrousRegion> selected = selectedRegion(player);
        for (FerrousRegion region : ClientFerrousRegionStore.regions()) {
            AABB bounds = region.bounds();
            if (!isNearPlayer(player, bounds)) continue;
            boolean highlighted = selected.map(FerrousRegion::id).filter(region.id()::equals).isPresent();
            show(region.id(), bounds, FERROUS_RED, highlighted, highlighted ? 1.0F / 16.0F : 1.0F / 64.0F);
        }

        Optional<BlockPos> first = FerrousTubeItem.getFirstCorner(held);
        if (first.isEmpty()) return;
        HitResult hit = minecraft.hitResult;
        if (!(hit instanceof BlockHitResult blockHit) || hit.getType() != HitResult.Type.BLOCK) return;

        BlockPos clicked = blockHit.getBlockPos();
        BlockPos clamped = FerrousTubeItem.clampToRegionLimit(first.get(), clicked);
        boolean overLimit = FerrousTubeItem.exceedsRegionLimit(first.get(), clicked);
        int color = overLimit ? LIMIT_YELLOW : FERROUS_RED;
        player.displayClientMessage(
                Component.translatable("message.magnot.click_to_confirm")
                        .withStyle(style -> style.withColor(color)),
                true
        );
        FerrousRegion preview = FerrousRegion.fromCorners(
                UUID.nameUUIDFromBytes("magnot-preview".getBytes()),
                first.get(),
                clamped
        );
        show(PREVIEW_SLOT, preview.bounds(), color, true, 1.0F / 16.0F);
    }

    @SubscribeEvent
    public static void onAttack(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isAttack()) return;
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null
                || !player.getMainHandItem().is(MagnotItems.FERROUS_TUBE.get())) {
            return;
        }

        event.setCanceled(true);
        event.setSwingHand(true);
        long gameTime = minecraft.level.getGameTime();
        if (gameTime < nextRemovalTick) return;
        nextRemovalTick = gameTime + 5L;
        selectedRegion(player).ifPresent(region -> MagnotNetwork.removeRegion(region.id()));
    }

    @SubscribeEvent
    public static void onRender(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS || OUTLINES.isEmpty()) {
            return;
        }

        long now = System.nanoTime();
        float deltaSeconds = lastRenderNanos == 0L
                ? 0.0F
                : Math.min((now - lastRenderNanos) / 1_000_000_000.0F, 0.1F);
        lastRenderNanos = now;

        Minecraft minecraft = Minecraft.getInstance();
        Vec3 camera = event.getCamera().getPosition();
        PoseStack poseStack = event.getPoseStack();
        MultiBufferSource.BufferSource buffers = minecraft.renderBuffers().bufferSource();

        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        Iterator<Outline> iterator = OUTLINES.values().iterator();
        while (iterator.hasNext()) {
            Outline outline = iterator.next();
            outline.advance(deltaSeconds);
            if (!outline.visible()) {
                iterator.remove();
                continue;
            }
            float strength = smoothstep(outline.selectionStrength);
            if (strength > MIN_STRENGTH) {
                renderFaces(poseStack, buffers, outline, camera, strength);
            }
            renderLines(poseStack, buffers, outline);
            if (strength > MIN_STRENGTH) {
                renderThickEdges(poseStack, buffers, outline, camera, strength);
            }
        }
        poseStack.popPose();
        RenderSystem.lineWidth(1.0F);
    }

    @SubscribeEvent
    public static void onLogout(ClientPlayerNetworkEvent.LoggingOut event) {
        ClientFerrousRegionStore.clear();
        OUTLINES.clear();
    }

    private static Optional<FerrousRegion> selectedRegion(LocalPlayer player) {
        Vec3 from = player.getEyePosition();
        double range = player.getAttributeValue(ForgeMod.BLOCK_REACH.get()) + 1.0D;
        Vec3 to = from.add(player.getViewVector(1.0F).scale(range));
        return ClientFerrousRegionStore.closestIntersecting(from, to);
    }

    private static void show(Object slot, AABB bounds, int color, boolean selected, float lineWidth) {
        Vec3[] corners = corners(bounds);
        Outline outline = OUTLINES.get(slot);
        if (outline == null) {
            OUTLINES.put(slot, new Outline(corners, color, selected, lineWidth));
        } else {
            outline.update(corners, color, selected, lineWidth);
        }
    }

    private static Vec3[] corners(AABB bounds) {
        return new Vec3[]{
                new Vec3(bounds.minX, bounds.minY, bounds.minZ),
                new Vec3(bounds.maxX, bounds.minY, bounds.minZ),
                new Vec3(bounds.maxX, bounds.minY, bounds.maxZ),
                new Vec3(bounds.minX, bounds.minY, bounds.maxZ),
                new Vec3(bounds.minX, bounds.maxY, bounds.minZ),
                new Vec3(bounds.maxX, bounds.maxY, bounds.minZ),
                new Vec3(bounds.maxX, bounds.maxY, bounds.maxZ),
                new Vec3(bounds.minX, bounds.maxY, bounds.maxZ)
        };
    }

    private static boolean isNearPlayer(LocalPlayer player, AABB bounds) {
        Vec3 pos = player.position();
        double dx = Math.max(Math.max(bounds.minX - pos.x, 0.0D), pos.x - bounds.maxX);
        double dy = Math.max(Math.max(bounds.minY - pos.y, 0.0D), pos.y - bounds.maxY);
        double dz = Math.max(Math.max(bounds.minZ - pos.z, 0.0D), pos.z - bounds.maxZ);
        return dx * dx + dy * dy + dz * dz <= REVEAL_RADIUS_SQR;
    }

    private static void renderFaces(PoseStack poseStack, MultiBufferSource.BufferSource buffers,
                                    Outline outline, Vec3 camera, float strength) {
        RenderType type = RenderType.entityTranslucent(TEXTURE);
        VertexConsumer consumer = buffers.getBuffer(type);
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();
        int alpha = Math.max(1, Math.round(72.0F * strength));
        Vec3 center = outline.center();
        boolean inside = outline.contains(camera);

        for (int[] face : FACES) {
            Vec3 a = outline.corners[face[0]];
            Vec3 b = outline.corners[face[1]];
            Vec3 c = outline.corners[face[2]];
            Vec3 d = outline.corners[face[3]];
            Vec3 normal = b.subtract(a).cross(c.subtract(a)).normalize();
            Vec3 faceCenter = a.add(b).add(c).add(d).scale(0.25D);
            if (normal.dot(faceCenter.subtract(center)) < 0.0D) normal = normal.scale(-1.0D);
            if (!inside && normal.dot(camera.subtract(faceCenter)) <= 0.0D) continue;
            Vec3 offset = normal.scale(inside ? -FACE_OFFSET : FACE_OFFSET);
            vertex(consumer, matrix, normalMatrix, a.add(offset), 0.0F, 0.0F, outline, alpha, normal);
            vertex(consumer, matrix, normalMatrix, b.add(offset), 0.0F, 1.0F, outline, alpha, normal);
            vertex(consumer, matrix, normalMatrix, c.add(offset), 1.0F, 1.0F, outline, alpha, normal);
            vertex(consumer, matrix, normalMatrix, d.add(offset), 1.0F, 0.0F, outline, alpha, normal);
        }
        buffers.endBatch(type);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f matrix, Matrix3f normalMatrix,
                               Vec3 point, float u, float v, Outline outline, int alpha, Vec3 normal) {
        consumer.vertex(matrix, (float) point.x, (float) point.y, (float) point.z)
                .color(outline.red(), outline.green(), outline.blue(), alpha)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(LightTexture.FULL_BRIGHT)
                .normal(normalMatrix, (float) normal.x, (float) normal.y, (float) normal.z)
                .endVertex();
    }

    private static void renderLines(PoseStack poseStack, MultiBufferSource.BufferSource buffers, Outline outline) {
        RenderType type = RenderType.lines();
        VertexConsumer consumer = buffers.getBuffer(type);
        Matrix4f matrix = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();
        for (int[] edge : EDGES) {
            Vec3 from = outline.corners[edge[0]];
            Vec3 to = outline.corners[edge[1]];
            Vec3 normal = to.subtract(from).normalize();
            consumer.vertex(matrix, (float) from.x, (float) from.y, (float) from.z)
                    .color(outline.red(), outline.green(), outline.blue(), 255)
                    .normal(normalMatrix, (float) normal.x, (float) normal.y, (float) normal.z)
                    .endVertex();
            consumer.vertex(matrix, (float) to.x, (float) to.y, (float) to.z)
                    .color(outline.red(), outline.green(), outline.blue(), 255)
                    .normal(normalMatrix, (float) normal.x, (float) normal.y, (float) normal.z)
                    .endVertex();
        }
        buffers.endBatch(type);
    }

    private static void renderThickEdges(PoseStack poseStack, MultiBufferSource.BufferSource buffers,
                                         Outline outline, Vec3 camera, float strength) {
        RenderType type = RenderType.debugQuads();
        VertexConsumer consumer = buffers.getBuffer(type);
        Matrix4f matrix = poseStack.last().pose();
        double halfWidth = outline.selectedLineWidth * 0.5D * strength;
        int alpha = Math.max(1, Math.round(255.0F * strength));

        for (int[] edge : EDGES) {
            Vec3 from = outline.corners[edge[0]];
            Vec3 to = outline.corners[edge[1]];
            Vec3 direction = to.subtract(from);
            double length = direction.length();
            if (length < 1.0E-6D) continue;
            direction = direction.scale(1.0D / length);
            Vec3 midpoint = from.add(to).scale(0.5D);
            Vec3 toCamera = camera.subtract(midpoint);
            Vec3 side = direction.cross(toCamera);
            if (side.lengthSqr() < 1.0E-12D) {
                side = direction.cross(Math.abs(direction.y) < 0.9D
                        ? new Vec3(0.0D, 1.0D, 0.0D)
                        : new Vec3(1.0D, 0.0D, 0.0D));
            }
            side = side.normalize().scale(halfWidth);
            quadVertex(consumer, matrix, from.add(side), outline, alpha);
            quadVertex(consumer, matrix, from.subtract(side), outline, alpha);
            quadVertex(consumer, matrix, to.subtract(side), outline, alpha);
            quadVertex(consumer, matrix, to.add(side), outline, alpha);
        }
        buffers.endBatch(type);
    }

    private static void quadVertex(VertexConsumer consumer, Matrix4f matrix, Vec3 point, Outline outline, int alpha) {
        consumer.vertex(matrix, (float) point.x, (float) point.y, (float) point.z)
                .color(outline.red(), outline.green(), outline.blue(), alpha)
                .endVertex();
    }

    private static float smoothstep(float value) {
        float clamped = Math.max(0.0F, Math.min(1.0F, value));
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }

    private static final class Outline {
        private Vec3[] corners;
        private int color;
        private float selectedLineWidth;
        private boolean selected;
        private boolean refreshed;
        private float selectionStrength;

        private Outline(Vec3[] corners, int color, boolean selected, float lineWidth) {
            update(corners, color, selected, lineWidth);
        }

        private void beginFrame() {
            refreshed = false;
            selected = false;
        }

        private void update(Vec3[] corners, int color, boolean selected, float lineWidth) {
            this.corners = corners;
            this.color = color;
            this.refreshed = true;
            this.selected = selected;
            if (selected) {
                this.selectedLineWidth = lineWidth;
                this.selectionStrength = 1.0F;
            }
        }

        private void advance(float deltaSeconds) {
            if (!selected && selectionStrength > 0.0F) {
                selectionStrength = Math.max(0.0F, selectionStrength - deltaSeconds / FADE_SECONDS);
            }
        }

        private boolean visible() {
            return refreshed || selectionStrength > MIN_STRENGTH;
        }

        private Vec3 center() {
            Vec3 center = Vec3.ZERO;
            for (Vec3 corner : corners) center = center.add(corner);
            return center.scale(1.0D / corners.length);
        }

        private boolean contains(Vec3 point) {
            double minX = corners[0].x;
            double minY = corners[0].y;
            double minZ = corners[0].z;
            double maxX = corners[6].x;
            double maxY = corners[6].y;
            double maxZ = corners[6].z;
            return point.x >= minX && point.x <= maxX
                    && point.y >= minY && point.y <= maxY
                    && point.z >= minZ && point.z <= maxZ;
        }

        private int red() { return color >> 16 & 255; }
        private int green() { return color >> 8 & 255; }
        private int blue() { return color & 255; }
    }
}
