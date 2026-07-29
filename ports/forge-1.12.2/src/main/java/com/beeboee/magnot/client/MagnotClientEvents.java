package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = Magnot.MOD_ID, value = Side.CLIENT)
public final class MagnotClientEvents {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Magnot.MOD_ID, "textures/special/ferrous_region.png");
    private static final Object PREVIEW_SLOT = new Object();
    private static final Map<Object, OutlineState> OUTLINES = new LinkedHashMap<>();

    private static final int FERROUS_RED = 0xBD2537;
    private static final int LIMIT_YELLOW = 0xFFD43B;
    private static final double REVEAL_RADIUS_SQR = 25.0D * 25.0D;
    private static final double TRACE_RANGE = 6.0D;
    private static final float FADE_OUT_SECONDS = 0.18F;

    private static long lastRenderNanos;
    private static long nextRemovalTick;

    private MagnotClientEvents() {
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        registerModel(MagnotItems.IRON_DUST);
        registerModel(MagnotItems.FERROUS_PASTE);
        registerModel(MagnotItems.FERROUS_TUBE);
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        for (OutlineState state : OUTLINES.values()) {
            state.fresh = false;
            state.selected = false;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player == null || minecraft.world == null) {
            ClientRegionStore.clear();
            OUTLINES.clear();
            return;
        }
        if (player.getHeldItemMainhand().getItem() != MagnotItems.FERROUS_TUBE) {
            return;
        }

        Optional<FerrousRegion> selected = selectedRegion(player);
        for (FerrousRegion region : ClientRegionStore.all()) {
            if (distanceToBoxSqr(player.getPositionVector(), region.bounds()) > REVEAL_RADIUS_SQR) {
                continue;
            }
            boolean highlighted = selected.isPresent() && selected.get().id().equals(region.id());
            show(region.id(), region.bounds(), FERROUS_RED, highlighted);
        }

        Optional<BlockPos> first = FerrousTubeItem.getFirstCorner(player.getHeldItemMainhand());
        if (!first.isPresent()
                || minecraft.objectMouseOver == null
                || minecraft.objectMouseOver.typeOfHit != RayTraceResult.Type.BLOCK) {
            return;
        }

        BlockPos raw = minecraft.objectMouseOver.getBlockPos();
        BlockPos clamped = FerrousTubeItem.clampToRegionLimit(first.get(), raw);
        boolean overLimit = FerrousTubeItem.exceedsRegionLimit(first.get(), raw);
        int color = overLimit ? LIMIT_YELLOW : FERROUS_RED;

        ITextComponent prompt = new TextComponentTranslation("message.magnot.click_to_confirm");
        prompt.getStyle().setColor(overLimit ? TextFormatting.YELLOW : TextFormatting.RED);
        player.sendStatusMessage(prompt, true);

        FerrousRegion preview = FerrousRegion.fromCorners(
                UUID.nameUUIDFromBytes("magnot-preview".getBytes()),
                first.get(),
                clamped
        );
        show(PREVIEW_SLOT, preview.bounds(), color, true);
    }

    @SubscribeEvent
    public static void onMouse(MouseEvent event) {
        if (event.getButton() != 0 || !event.isButtonstate()) {
            return;
        }

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player == null
                || minecraft.world == null
                || player.getHeldItemMainhand().getItem() != MagnotItems.FERROUS_TUBE) {
            return;
        }

        event.setCanceled(true);
        player.swingArm(EnumHand.MAIN_HAND);
        long gameTime = minecraft.world.getTotalWorldTime();
        if (gameTime < nextRemovalTick) {
            return;
        }
        nextRemovalTick = gameTime + 5L;
        selectedRegion(player).ifPresent(region -> MagnotNetwork.remove(region.id()));
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderWorldLastEvent event) {
        if (OUTLINES.isEmpty()) {
            return;
        }

        long now = System.nanoTime();
        float deltaSeconds = lastRenderNanos == 0L
                ? 0.0F
                : Math.min((now - lastRenderNanos) / 1_000_000_000.0F, 0.1F);
        lastRenderNanos = now;

        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayerSP player = minecraft.player;
        if (player == null) {
            return;
        }

        float partialTicks = event.getPartialTicks();
        double cameraX = player.lastTickPosX + (player.posX - player.lastTickPosX) * partialTicks;
        double cameraY = player.lastTickPosY + (player.posY - player.lastTickPosY) * partialTicks;
        double cameraZ = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * partialTicks;

        GlStateManager.pushMatrix();
        GlStateManager.translate(-cameraX, -cameraY, -cameraZ);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO
        );
        GlStateManager.depthMask(false);
        GlStateManager.disableCull();

        Iterator<OutlineState> iterator = OUTLINES.values().iterator();
        while (iterator.hasNext()) {
            OutlineState state = iterator.next();
            if (!state.selected && state.selectionStrength > 0.0F) {
                state.selectionStrength =
                        Math.max(0.0F, state.selectionStrength - deltaSeconds / FADE_OUT_SECONDS);
            }
            if (!state.fresh && state.selectionStrength <= 0.001F) {
                iterator.remove();
                continue;
            }

            renderLine(state);
            if (state.selectionStrength > 0.001F) {
                renderFaces(minecraft, state);
            }
        }

        GL11.glLineWidth(1.0F);
        GlStateManager.enableCull();
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.popMatrix();
    }

    private static void registerModel(Item item) {
        ModelLoader.setCustomModelResourceLocation(
                item,
                0,
                new ModelResourceLocation(item.getRegistryName(), "inventory")
        );
    }

    private static Optional<FerrousRegion> selectedRegion(EntityPlayerSP player) {
        Vec3d source = player.getPositionEyes(1.0F);
        Vec3d target = source.add(player.getLookVec().scale(TRACE_RANGE));
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

    private static void renderLine(OutlineState state) {
        GlStateManager.disableTexture2D();
        GL11.glLineWidth(state.selected ? 4.0F : 1.0F);
        RenderGlobal.drawSelectionBoundingBox(
                state.bounds.grow(0.002D),
                red(state.color),
                green(state.color),
                blue(state.color),
                1.0F
        );
    }

    private static void renderFaces(Minecraft minecraft, OutlineState state) {
        minecraft.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.enableTexture2D();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        AxisAlignedBB box = state.bounds;
        float alpha = 72.0F / 255.0F * smoothstep(state.selectionStrength);
        float red = red(state.color);
        float green = green(state.color);
        float blue = blue(state.color);

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);

        horizontalFace(buffer, box.minY - 1.0D / 128.0D, box, red, green, blue, alpha);
        horizontalFace(buffer, box.maxY + 1.0D / 128.0D, box, red, green, blue, alpha);
        xFace(buffer, box.minX - 1.0D / 128.0D, box, red, green, blue, alpha);
        xFace(buffer, box.maxX + 1.0D / 128.0D, box, red, green, blue, alpha);
        zFace(buffer, box.minZ - 1.0D / 128.0D, box, red, green, blue, alpha);
        zFace(buffer, box.maxZ + 1.0D / 128.0D, box, red, green, blue, alpha);

        tessellator.draw();
    }

    private static void horizontalFace(
            BufferBuilder buffer,
            double y,
            AxisAlignedBB box,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        double u = box.maxX - box.minX;
        double v = box.maxZ - box.minZ;
        vertex(buffer, box.minX, y, box.minZ, 0.0D, 0.0D, red, green, blue, alpha);
        vertex(buffer, box.minX, y, box.maxZ, 0.0D, v, red, green, blue, alpha);
        vertex(buffer, box.maxX, y, box.maxZ, u, v, red, green, blue, alpha);
        vertex(buffer, box.maxX, y, box.minZ, u, 0.0D, red, green, blue, alpha);
    }

    private static void xFace(
            BufferBuilder buffer,
            double x,
            AxisAlignedBB box,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        double u = box.maxZ - box.minZ;
        double v = box.maxY - box.minY;
        vertex(buffer, x, box.maxY, box.minZ, 0.0D, 0.0D, red, green, blue, alpha);
        vertex(buffer, x, box.minY, box.minZ, 0.0D, v, red, green, blue, alpha);
        vertex(buffer, x, box.minY, box.maxZ, u, v, red, green, blue, alpha);
        vertex(buffer, x, box.maxY, box.maxZ, u, 0.0D, red, green, blue, alpha);
    }

    private static void zFace(
            BufferBuilder buffer,
            double z,
            AxisAlignedBB box,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        double u = box.maxX - box.minX;
        double v = box.maxY - box.minY;
        vertex(buffer, box.minX, box.maxY, z, 0.0D, 0.0D, red, green, blue, alpha);
        vertex(buffer, box.minX, box.minY, z, 0.0D, v, red, green, blue, alpha);
        vertex(buffer, box.maxX, box.minY, z, u, v, red, green, blue, alpha);
        vertex(buffer, box.maxX, box.maxY, z, u, 0.0D, red, green, blue, alpha);
    }

    private static void vertex(
            BufferBuilder buffer,
            double x,
            double y,
            double z,
            double u,
            double v,
            float red,
            float green,
            float blue,
            float alpha
    ) {
        buffer.pos(x, y, z).tex(u, v).color(red, green, blue, alpha).endVertex();
    }

    private static double distanceToBoxSqr(Vec3d point, AxisAlignedBB box) {
        double x = Math.max(Math.max(box.minX - point.x, 0.0D), point.x - box.maxX);
        double y = Math.max(Math.max(box.minY - point.y, 0.0D), point.y - box.maxY);
        double z = Math.max(Math.max(box.minZ - point.z, 0.0D), point.z - box.maxZ);
        return x * x + y * y + z * z;
    }

    private static float smoothstep(float value) {
        float clamped = Math.max(0.0F, Math.min(1.0F, value));
        return clamped * clamped * (3.0F - 2.0F * clamped);
    }

    private static float red(int color) {
        return (color >> 16 & 255) / 255.0F;
    }

    private static float green(int color) {
        return (color >> 8 & 255) / 255.0F;
    }

    private static float blue(int color) {
        return (color & 255) / 255.0F;
    }

    private static final class OutlineState {
        private AxisAlignedBB bounds;
        private int color;
        private boolean selected;
        private boolean fresh;
        private float selectionStrength;
    }
}
