package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class MagnotClientEvents {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Magnot.MOD_ID, "textures/special/ferrous_region.png");
    private static final Object PREVIEW_SLOT = new Object();
    private static final Map<Object, OutlineState> OUTLINES =
            new LinkedHashMap<Object, OutlineState>();

    private static final int FERROUS_RED = 0xBD2537;
    private static final int LIMIT_YELLOW = 0xFFD43B;
    private static final double REVEAL_RADIUS_SQR = 25.0D * 25.0D;
    private static final double TRACE_RANGE = 6.0D;
    private static final float FADE_OUT_SECONDS = 0.18F;

    private static long lastRenderNanos;
    private static long nextRemovalTick;
    private static boolean showPrompt;
    private static boolean promptOverLimit;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        Iterator<OutlineState> stateIterator = OUTLINES.values().iterator();
        while (stateIterator.hasNext()) {
            OutlineState state = stateIterator.next();
            state.fresh = false;
            state.selected = false;
        }
        showPrompt = false;

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP player = minecraft.thePlayer;
        if (player == null || minecraft.theWorld == null) {
            clearClientState();
            return;
        }

        ItemStack held = player.getHeldItem();
        if (held == null || held.getItem() != Magnot.FERROUS_TUBE) {
            return;
        }

        FerrousRegion selected = selectedRegion(player);
        List<FerrousRegion> regions = ClientRegionStore.all();
        for (int index = 0; index < regions.size(); index++) {
            FerrousRegion region = regions.get(index);
            if (distanceToBoxSqr(player.getPosition(1.0F), region.bounds()) > REVEAL_RADIUS_SQR) {
                continue;
            }
            boolean highlighted = selected != null && selected.getId().equals(region.getId());
            show(region.getId(), region.bounds(), FERROUS_RED, highlighted);
        }

        int[] first = FerrousTubeItem.getFirstCorner(held);
        MovingObjectPosition hit = minecraft.objectMouseOver;
        if (first == null || hit == null || hit.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
            return;
        }

        int[] second = FerrousTubeItem.clampToRegionLimit(first, hit.blockX, hit.blockY, hit.blockZ);
        promptOverLimit = FerrousTubeItem.exceedsRegionLimit(first, hit.blockX, hit.blockY, hit.blockZ);
        showPrompt = true;
        FerrousRegion preview = FerrousRegion.fromCorners(
                UUID.nameUUIDFromBytes("magnot-preview".getBytes()),
                first[0],
                first[1],
                first[2],
                second[0],
                second[1],
                second[2]
        );
        show(
                PREVIEW_SLOT,
                preview.bounds(),
                promptOverLimit ? LIMIT_YELLOW : FERROUS_RED,
                true
        );
    }

    @SubscribeEvent
    public void onMouse(MouseEvent event) {
        if (event.button != 0 || !event.buttonstate) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityClientPlayerMP player = minecraft.thePlayer;
        ItemStack held = player == null ? null : player.getHeldItem();
        if (player == null
                || minecraft.theWorld == null
                || held == null
                || held.getItem() != Magnot.FERROUS_TUBE) {
            return;
        }

        event.setCanceled(true);
        player.swingItem();
        long gameTime = minecraft.theWorld.getTotalWorldTime();
        if (gameTime < nextRemovalTick) {
            return;
        }
        nextRemovalTick = gameTime + 5L;
        FerrousRegion selected = selectedRegion(player);
        if (selected != null) {
            MagnotNetwork.remove(selected.getId());
        }
    }

    @SubscribeEvent
    public void onOverlay(RenderGameOverlayEvent.Text event) {
        if (showPrompt) {
            event.left.add(
                    (promptOverLimit ? EnumChatFormatting.YELLOW : EnumChatFormatting.RED)
                            + StatCollector.translateToLocal("message.magnot.click_to_confirm")
            );
        }
    }

    @SubscribeEvent
    public void onDisconnect(FMLNetworkEvent.ClientDisconnectionFromServerEvent event) {
        clearClientState();
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (OUTLINES.isEmpty()) {
            return;
        }

        long now = System.nanoTime();
        float deltaSeconds = lastRenderNanos == 0L
                ? 0.0F
                : Math.min((now - lastRenderNanos) / 1000000000.0F, 0.1F);
        lastRenderNanos = now;

        Minecraft minecraft = Minecraft.getMinecraft();
        if (minecraft.renderViewEntity == null) {
            return;
        }

        float partialTicks = event.partialTicks;
        double cameraX = minecraft.renderViewEntity.lastTickPosX
                + (minecraft.renderViewEntity.posX - minecraft.renderViewEntity.lastTickPosX) * partialTicks;
        double cameraY = minecraft.renderViewEntity.lastTickPosY
                + (minecraft.renderViewEntity.posY - minecraft.renderViewEntity.lastTickPosY) * partialTicks;
        double cameraZ = minecraft.renderViewEntity.lastTickPosZ
                + (minecraft.renderViewEntity.posZ - minecraft.renderViewEntity.lastTickPosZ) * partialTicks;

        GL11.glPushMatrix();
        GL11.glTranslated(-cameraX, -cameraY, -cameraZ);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_CULL_FACE);

        Iterator<OutlineState> iterator = OUTLINES.values().iterator();
        while (iterator.hasNext()) {
            OutlineState state = iterator.next();
            if (!state.selected && state.selectionStrength > 0.0F) {
                state.selectionStrength = Math.max(
                        0.0F,
                        state.selectionStrength - deltaSeconds / FADE_OUT_SECONDS
                );
            }
            if (!state.fresh && state.selectionStrength <= 0.001F) {
                iterator.remove();
                continue;
            }

            renderLines(state);
            if (state.selectionStrength > 0.001F) {
                renderFaces(minecraft, state);
            }
        }

        GL11.glLineWidth(1.0F);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDepthMask(true);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private static FerrousRegion selectedRegion(EntityClientPlayerMP player) {
        Vec3 source = Vec3.createVectorHelper(
                player.posX,
                player.posY + player.getEyeHeight(),
                player.posZ
        );
        Vec3 look = player.getLook(1.0F);
        Vec3 target = source.addVector(
                look.xCoord * TRACE_RANGE,
                look.yCoord * TRACE_RANGE,
                look.zCoord * TRACE_RANGE
        );
        return ClientRegionStore.closestIntersecting(source, target);
    }

    private static void show(Object slot, AxisAlignedBB bounds, int color, boolean selected) {
        OutlineState state = OUTLINES.get(slot);
        if (state == null) {
            state = new OutlineState();
            OUTLINES.put(slot, state);
        }
        state.bounds = bounds;
        state.color = color;
        state.fresh = true;
        state.selected = selected;
        if (selected) {
            state.selectionStrength = 1.0F;
        }
    }

    private static void renderLines(OutlineState state) {
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glLineWidth(state.selected ? 4.0F : 1.0F);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawing(GL11.GL_LINES);
        tessellator.setColorRGBA(
                state.color >> 16 & 255,
                state.color >> 8 & 255,
                state.color & 255,
                255
        );
        addEdges(tessellator, state.bounds);
        tessellator.draw();
    }

    private static void addEdges(Tessellator tessellator, AxisAlignedBB box) {
        edge(tessellator, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ);
        edge(tessellator, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
        edge(tessellator, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ);
        edge(tessellator, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        edge(tessellator, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ);
        edge(tessellator, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
        edge(tessellator, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ);
        edge(tessellator, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        edge(tessellator, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ);
        edge(tessellator, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
        edge(tessellator, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ);
        edge(tessellator, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    private static void edge(
            Tessellator tessellator,
            double x1,
            double y1,
            double z1,
            double x2,
            double y2,
            double z2
    ) {
        tessellator.addVertex(x1, y1, z1);
        tessellator.addVertex(x2, y2, z2);
    }

    private static void renderFaces(Minecraft minecraft, OutlineState state) {
        minecraft.renderEngine.bindTexture(TEXTURE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        float strength = smoothstep(state.selectionStrength);
        float red = (state.color >> 16 & 255) / 255.0F;
        float green = (state.color >> 8 & 255) / 255.0F;
        float blue = (state.color & 255) / 255.0F;
        float alpha = 72.0F / 255.0F * strength;
        AxisAlignedBB box = state.bounds;
        double offset = 1.0D / 128.0D;

        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setBrightness(15728880);
        tessellator.setColorRGBA_F(red, green, blue, alpha);
        horizontalFace(tessellator, box.minY - offset, box);
        horizontalFace(tessellator, box.maxY + offset, box);
        xFace(tessellator, box.minX - offset, box);
        xFace(tessellator, box.maxX + offset, box);
        zFace(tessellator, box.minZ - offset, box);
        zFace(tessellator, box.maxZ + offset, box);
        tessellator.draw();
    }

    private static void horizontalFace(Tessellator tessellator, double y, AxisAlignedBB box) {
        double u = box.maxX - box.minX;
        double v = box.maxZ - box.minZ;
        tessellator.addVertexWithUV(box.minX, y, box.minZ, 0.0D, 0.0D);
        tessellator.addVertexWithUV(box.minX, y, box.maxZ, 0.0D, v);
        tessellator.addVertexWithUV(box.maxX, y, box.maxZ, u, v);
        tessellator.addVertexWithUV(box.maxX, y, box.minZ, u, 0.0D);
    }

    private static void xFace(Tessellator tessellator, double x, AxisAlignedBB box) {
        double u = box.maxZ - box.minZ;
        double v = box.maxY - box.minY;
        tessellator.addVertexWithUV(x, box.maxY, box.minZ, 0.0D, 0.0D);
        tessellator.addVertexWithUV(x, box.minY, box.minZ, 0.0D, v);
        tessellator.addVertexWithUV(x, box.minY, box.maxZ, u, v);
        tessellator.addVertexWithUV(x, box.maxY, box.maxZ, u, 0.0D);
    }

    private static void zFace(Tessellator tessellator, double z, AxisAlignedBB box) {
        double u = box.maxX - box.minX;
        double v = box.maxY - box.minY;
        tessellator.addVertexWithUV(box.minX, box.maxY, z, 0.0D, 0.0D);
        tessellator.addVertexWithUV(box.minX, box.minY, z, 0.0D, v);
        tessellator.addVertexWithUV(box.maxX, box.minY, z, u, v);
        tessellator.addVertexWithUV(box.maxX, box.maxY, z, u, 0.0D);
    }

    private static double distanceToBoxSqr(Vec3 point, AxisAlignedBB box) {
        double x = Math.max(Math.max(box.minX - point.xCoord, 0.0D), point.xCoord - box.maxX);
        double y = Math.max(Math.max(box.minY - point.yCoord, 0.0D), point.yCoord - box.maxY);
        double z = Math.max(Math.max(box.minZ - point.zCoord, 0.0D), point.zCoord - box.maxZ);
        return x * x + y * y + z * z;
    }

    private static float smoothstep(float value) {
        float clamped = Math.max(0.0F, Math.min(1.0F, value));
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }

    private static void clearClientState() {
        ClientRegionStore.clear();
        OUTLINES.clear();
        showPrompt = false;
        lastRenderNanos = 0L;
    }

    private static final class OutlineState {
        private AxisAlignedBB bounds;
        private int color;
        private boolean selected;
        private boolean fresh;
        private float selectionStrength;
    }
}
