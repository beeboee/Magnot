package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.compat.sable.MagnotSableClientCompat;
import com.beeboee.magnot.entity.FerrousRegionEntity;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.ConfigureFerrousRegionFilterPayload;
import com.beeboee.magnot.network.RemoveClosestFerrousRegionPayload;
import com.beeboee.magnot.network.ToggleFerrousTubeFilterModePayload;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@EventBusSubscriber(modid = Magnot.MOD_ID, value = Dist.CLIENT)
public final class MagnotClientEvents {
    private static final Object SELECTION_OUTLINE_SLOT = new Object();
    private static final int FERROUS_RED = 0xBD2537;
    private static final int LIMIT_YELLOW = 0xFFD43B;
    private static final int FILTER_PREVIEW_TEXT_ENTITY_ID = -20_250_101;
    private static final int FILTER_PREVIEW_ITEM_ENTITY_ID = -20_250_102;
    private static final double REGION_REVEAL_RADIUS = 25.0D;
    private static final double REGION_REVEAL_RADIUS_SQR = REGION_REVEAL_RADIUS * REGION_REVEAL_RADIUS;
    private static long nextRegionRemovalTick = 0L;
    private static long nextRegionFilterTick = 0L;
    private static ArmorStand filterPreviewText;
    private static ItemEntity filterPreviewItem;

    private MagnotClientEvents() {
    }

    @SubscribeEvent
    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (!holdingFerrousTube()) {
            return;
        }

        if (event.isAttack()) {
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

            selectedRegion(player).ifPresent(region -> PacketDistributor.sendToServer(new RemoveClosestFerrousRegionPayload(region.id())));
            return;
        }

        if (!event.isUseItem()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return;
        }

        ItemStack held = player.getMainHandItem();
        if (FerrousTubeItem.getFirstCorner(held).isPresent()) {
            return;
        }

        ItemStack filterStack = player.getOffhandItem();
        long gameTime = player.level().getGameTime();
        if (player.isShiftKeyDown() && !filterStack.isEmpty()) {
            event.setCanceled(true);
            event.setSwingHand(true);
            if (gameTime >= nextRegionFilterTick) {
                nextRegionFilterTick = gameTime + 5L;
                PacketDistributor.sendToServer(new ToggleFerrousTubeFilterModePayload());
            }
            return;
        }

        Optional<FerrousRegion> selectedRegion = selectedRegion(player);
        if (selectedRegion.isEmpty()) {
            return;
        }

        event.setCanceled(true);
        event.setSwingHand(true);

        if (gameTime < nextRegionFilterTick) {
            return;
        }
        nextRegionFilterTick = gameTime + 5L;

        boolean clear = filterStack.isEmpty();
        PacketDistributor.sendToServer(new ConfigureFerrousRegionFilterPayload(selectedRegion.get().id(), filterStack, clear, false));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return;
        }

        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            hideFilterPreview(minecraft.level);
            return;
        }

        Optional<FerrousRegion> selectedRegion = selectedRegion(player);
        Set<UUID> entityRenderedRegionIds = new HashSet<>();

        AABB entitySearch = player.getBoundingBox().inflate(REGION_REVEAL_RADIUS);
        for (FerrousRegionEntity entity : minecraft.level.getEntitiesOfClass(FerrousRegionEntity.class, entitySearch)) {
            FerrousRegion entityRegion = entity.asRegion();
            Optional<FerrousRegion> syncedRegion = ClientFerrousRegionStore.byId(entityRegion.id());
            if (syncedRegion.isEmpty() || !syncedRegion.get().sameShapeAndSpace(entityRegion)) {
                continue;
            }

            if (renderRegion(player, minecraft.level, syncedRegion.get(), selectedRegion, entity.getUUID())) {
                entityRenderedRegionIds.add(entityRegion.id());
            }
        }

        for (FerrousRegion region : ClientFerrousRegionStore.regions()) {
            if (entityRenderedRegionIds.contains(region.id())) {
                continue;
            }

            renderRegion(player, minecraft.level, region, selectedRegion, region.id());
        }

        var firstCorner = FerrousTubeItem.getFirstCorner(held);
        if (firstCorner.isEmpty()) {
            if (selectedRegion.filter(FerrousRegion::hasFilter).isPresent()) {
                handleSelectedFilteredRegion(player, minecraft.level, selectedRegion.get());
            } else {
                hideFilterPreview(minecraft.level);
            }
            return;
        }

        hideFilterPreview(minecraft.level);
        HitResult hitResult = minecraft.hitResult;
        if (!(hitResult instanceof BlockHitResult blockHitResult) || hitResult.getType() != HitResult.Type.BLOCK) {
            return;
        }

        BlockPos clicked = blockHitResult.getBlockPos();
        BlockPos clampedCorner = FerrousTubeItem.clampToRegionLimit(firstCorner.get(), clicked);
        boolean overLimit = FerrousTubeItem.exceedsRegionLimit(firstCorner.get(), clicked);
        int color = overLimit ? LIMIT_YELLOW : FERROUS_RED;

        player.displayClientMessage(
                Component.translatable("message.magnot.click_to_confirm").withStyle(style -> style.withColor(color)),
                true
        );

        AABB selectionBox = boxBetween(firstCorner.get(), clampedCorner);
        Outliner.getInstance()
                .showAABB(SELECTION_OUTLINE_SLOT, selectionBox)
                .colored(color)
                .withFaceTextures(MagnotSpecialTextures.FERROUS_REGION, MagnotSpecialTextures.FERROUS_REGION)
                .disableLineNormals()
                .lineWidth(1.0F / 16.0F);
    }

    private static void handleSelectedFilteredRegion(LocalPlayer player, ClientLevel level, FerrousRegion region) {
        showFilterPreview(level, region);
        player.displayClientMessage(filterMessage(region), true);
    }

    private static void showFilterPreview(ClientLevel level, FerrousRegion region) {
        AABB displayBounds = ModList.get().isLoaded("sable")
                ? MagnotSableClientCompat.displayBounds(level, region)
                : region.bounds();
        Vec3 basePosition = displayBounds.getCenter().add(0.0D, displayBounds.getYsize() * 0.5D + 0.75D, 0.0D);
        Vec3 itemPosition = basePosition.add(0.0D, 0.35D, 0.0D);
        Vec3 textPosition = basePosition.add(0.0D, -0.25D, 0.0D);

        if (filterPreviewItem == null || filterPreviewItem.level() != level) {
            hideFilterPreview(level);
            filterPreviewItem = new ItemEntity(level, itemPosition.x, itemPosition.y, itemPosition.z, region.filterStack().copy());
            filterPreviewItem.setId(FILTER_PREVIEW_ITEM_ENTITY_ID);
            filterPreviewItem.setNoGravity(true);
            filterPreviewItem.setPickUpDelay(32767);
            level.addFreshEntity(filterPreviewItem);
        }

        if (filterPreviewText == null || filterPreviewText.level() != level) {
            filterPreviewText = new ArmorStand(level, textPosition.x, textPosition.y, textPosition.z);
            filterPreviewText.setId(FILTER_PREVIEW_TEXT_ENTITY_ID);
            filterPreviewText.setInvisible(true);
            filterPreviewText.setNoGravity(true);
            filterPreviewText.setCustomNameVisible(true);
            level.addFreshEntity(filterPreviewText);
        }

        filterPreviewItem.setPos(itemPosition.x, itemPosition.y, itemPosition.z);
        filterPreviewItem.setItem(region.filterStack().copy());
        filterPreviewText.setPos(textPosition.x, textPosition.y, textPosition.z);
        filterPreviewText.setCustomName(filterMessage(region));
    }

    private static void hideFilterPreview(ClientLevel level) {
        if (filterPreviewItem != null) {
            level.removeEntity(filterPreviewItem.getId(), Entity.RemovalReason.DISCARDED);
            filterPreviewItem = null;
        }
        if (filterPreviewText != null) {
            level.removeEntity(filterPreviewText.getId(), Entity.RemovalReason.DISCARDED);
            filterPreviewText = null;
        }
    }

    private static Component filterMessage(FerrousRegion region) {
        return Component.translatable(region.whitelistMode() ? "message.magnot.filter_mode_whitelist" : "message.magnot.filter_mode_blacklist");
    }

    private static boolean renderRegion(LocalPlayer player, net.minecraft.world.level.Level level, FerrousRegion region, Optional<FerrousRegion> selectedRegion, Object renderSlot) {
        AABB displayBounds = ModList.get().isLoaded("sable")
                ? MagnotSableClientCompat.displayBounds(level, region)
                : region.bounds();

        if (!isNearPlayer(player, displayBounds)) {
            return false;
        }

        boolean selected = selectedRegion.map(FerrousRegion::id).filter(region.id()::equals).isPresent();
        MagnotSpecialTextures faceTexture = selected ? MagnotSpecialTextures.FERROUS_REGION : null;
        float lineWidth = selected ? 1.0F / 16.0F : 1.0F / 64.0F;

        if (ModList.get().isLoaded("sable")
                && MagnotSableClientCompat.showRegionOutline(level, renderSlot, region, FERROUS_RED, faceTexture, lineWidth)) {
            return true;
        }

        Outliner.getInstance()
                .showAABB(renderSlot, displayBounds)
                .colored(FERROUS_RED)
                .withFaceTextures(faceTexture, faceTexture)
                .disableLineNormals()
                .lineWidth(lineWidth);

        if (ModList.get().isLoaded("sable")) {
            MagnotSableClientCompat.disableOutlineTransform(renderSlot);
        }

        return true;
    }

    private static boolean holdingFerrousTube() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        return player != null && player.getMainHandItem().is(MagnotItems.FERROUS_TUBE.get());
    }

    private static Optional<FerrousRegion> selectedRegion() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return Optional.empty();
        }

        return selectedRegion(player);
    }

    private static Optional<FerrousRegion> selectedRegion(LocalPlayer player) {
        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return Optional.empty();
        }

        Vec3 from = player.getEyePosition();
        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 to = from.add(player.getLookAngle().scale(range));

        Optional<FerrousRegion> selected = ClientFerrousRegionStore.closestIntersecting(from, to);
        if (selected.isEmpty() && ModList.get().isLoaded("sable")) {
            selected = MagnotSableClientCompat.closestIntersecting(player.level(), from, to);
        }

        return selected;
    }

    private static boolean isNearPlayer(LocalPlayer player, AABB bounds) {
        Vec3 playerPosition = player.position();
        double dx = Math.max(Math.max(bounds.minX - playerPosition.x, 0.0D), playerPosition.x - bounds.maxX);
        double dy = Math.max(Math.max(bounds.minY - playerPosition.y, 0.0D), playerPosition.y - bounds.maxY);
        double dz = Math.max(Math.max(bounds.minZ - playerPosition.z, 0.0D), playerPosition.z - bounds.maxZ);
        return dx * dx + dy * dy + dz * dz <= REGION_REVEAL_RADIUS_SQR;
    }

    private static AABB boxBetween(BlockPos first, BlockPos second) {
        int minX = Math.min(first.getX(), second.getX());
        int minY = Math.min(first.getY(), second.getY());
        int minZ = Math.min(first.getZ(), second.getZ());
        int maxX = Math.max(first.getX(), second.getX()) + 1;
        int maxY = Math.max(first.getY(), second.getY()) + 1;
        int maxZ = Math.max(first.getZ(), second.getZ()) + 1;
        return new AABB(new Vec3(minX, minY, minZ), new Vec3(maxX, maxY, maxZ));
    }
}
