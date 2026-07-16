package com.beeboee.magnot.client.selection;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.RemoveClosestFerrousRegionPayload;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;
import java.util.UUID;

/**
 * One client event owner. Exactly one presentation backend is selected and all
 * clicks, target selection, previews, and stored outlines pass through it.
 */
public final class FerrousSelectionRuntime {
    private static final Object PREVIEW_SLOT = new Object();
    private static final int FERROUS_RED = 0xBD2537;
    private static final int LIMIT_YELLOW = 0xFFD43B;
    private static final double REGION_REVEAL_RADIUS = 25.0D;
    private static final double REGION_REVEAL_RADIUS_SQR = REGION_REVEAL_RADIUS * REGION_REVEAL_RADIUS;
    private static boolean registered;

    private final FerrousSelectionBackend backend;
    private final SableSelectionView sable;
    private long nextRegionRemovalTick;

    private FerrousSelectionRuntime(FerrousSelectionBackend backend) {
        this.backend = backend;
        this.sable = SableSelectionView.load();
    }

    public static void registerSelectedBackend() {
        if (registered) {
            return;
        }
        registered = true;
        FerrousSelectionRuntime runtime = new FerrousSelectionRuntime(FerrousSelectionBackendFactory.create());
        NeoForge.EVENT_BUS.register(runtime);
        Magnot.LOGGER.info("Active ferrous selection backend: {}", runtime.backend.name());
    }

    @SubscribeEvent
    public void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isAttack() || !holdingFerrousTube()) {
            return;
        }

        event.setCanceled(true);
        event.setSwingHand(true);
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return;
        }

        long gameTime = player.level().getGameTime();
        if (gameTime < nextRegionRemovalTick) {
            return;
        }
        nextRegionRemovalTick = gameTime + 5L;
        selectedRegion(player).ifPresent(region -> PacketDistributor.sendToServer(
                new RemoveClosestFerrousRegionPayload(region.id())
        ));
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent.Post event) {
        backend.beginFrame();
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return;
        }

        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return;
        }

        Optional<FerrousRegion> selected = selectedRegion(player);
        for (FerrousRegion region : ClientFerrousRegionStore.regions()) {
            FerrousSelectionView view = sable.view(minecraft.level, region);
            if (!isNearPlayer(player, view.displayBounds())) {
                continue;
            }
            boolean highlighted = selected.map(FerrousRegion::id).filter(region.id()::equals).isPresent();
            backend.showOutline(
                    minecraft.level,
                    region.id(),
                    region,
                    view,
                    FERROUS_RED,
                    highlighted,
                    highlighted ? 1.0F / 16.0F : 1.0F / 64.0F
            );
        }

        Optional<BlockPos> firstCorner = FerrousTubeItem.getFirstCorner(held);
        if (firstCorner.isEmpty()) {
            return;
        }

        HitResult hitResult = minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }

        BlockPos clicked = blockHitResult.getBlockPos();
        BlockPos clamped = FerrousTubeItem.clampToRegionLimit(firstCorner.get(), clicked);
        boolean overLimit = FerrousTubeItem.exceedsRegionLimit(firstCorner.get(), clicked);
        int color = overLimit ? LIMIT_YELLOW : FERROUS_RED;
        player.displayClientMessage(
                Component.translatable("message.magnot.click_to_confirm").withStyle(style -> style.withColor(color)),
                true
        );

        UUID subLevelId = FerrousTubeItem.getFirstSubLevelId(held).orElse(null);
        FerrousRegion preview = FerrousRegion.fromCorners(UUID.nameUUIDFromBytes("magnot-preview".getBytes()), firstCorner.get(), clamped, subLevelId);
        FerrousSelectionView view = sable.view(minecraft.level, preview);
        backend.showOutline(minecraft.level, PREVIEW_SLOT, preview, view, color, true, 1.0F / 16.0F);
    }

    @SubscribeEvent
    public void onRenderLevel(RenderLevelStageEvent event) {
        backend.render(event);
    }

    private Optional<FerrousRegion> selectedRegion(LocalPlayer player) {
        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return Optional.empty();
        }

        Vec3 from = player.getEyePosition();
        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 to = backend.traceTarget(player, range, from);
        Optional<FerrousRegion> selected = ClientFerrousRegionStore.closestIntersecting(from, to);
        Optional<SableSelectionView.SelectionHit> subLevel = sable.closestIntersecting(player.level(), from, to);
        if (selected.isEmpty()) {
            return subLevel.map(SableSelectionView.SelectionHit::region);
        }
        if (subLevel.isEmpty()) {
            return selected;
        }
        double worldDistance = selected.get().hitDistanceSqr(from, to).orElse(Double.MAX_VALUE);
        return subLevel.get().distanceSqr() < worldDistance
                ? Optional.of(subLevel.get().region())
                : selected;
    }

    private static boolean holdingFerrousTube() {
        LocalPlayer player = Minecraft.getInstance().player;
        return player != null && player.getMainHandItem().is(MagnotItems.FERROUS_TUBE.get());
    }

    private static boolean isNearPlayer(LocalPlayer player, AABB bounds) {
        Vec3 pos = player.position();
        double dx = Math.max(Math.max(bounds.minX - pos.x, 0.0D), pos.x - bounds.maxX);
        double dy = Math.max(Math.max(bounds.minY - pos.y, 0.0D), pos.y - bounds.maxY);
        double dz = Math.max(Math.max(bounds.minZ - pos.z, 0.0D), pos.z - bounds.maxZ);
        return dx * dx + dy * dy + dz * dz <= REGION_REVEAL_RADIUS_SQR;
    }
}
