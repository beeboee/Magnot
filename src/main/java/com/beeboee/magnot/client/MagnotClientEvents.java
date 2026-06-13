package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.compat.sable.MagnotSableClientCompat;
import com.beeboee.magnot.entity.FerrousRegionEntity;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.RemoveClosestFerrousRegionPayload;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
    private static final double REGION_REVEAL_RADIUS = 25.0D;
    private static final double REGION_REVEAL_RADIUS_SQR = REGION_REVEAL_RADIUS * REGION_REVEAL_RADIUS;
    private static final ResourceLocation ARTIFACTS_MAGNETISM = ResourceLocation.fromNamespaceAndPath("artifacts", "magnetism");
    private static final double ARTIFACTS_MAGNETISM_FALLBACK_RANGE = 12.0D;
    private static final double ARTIFACTS_MAGNETISM_FALLBACK_RANGE_SQR = ARTIFACTS_MAGNETISM_FALLBACK_RANGE * ARTIFACTS_MAGNETISM_FALLBACK_RANGE;
    private static long nextRegionRemovalTick = 0L;

    private MagnotClientEvents() {
    }

    @SubscribeEvent
    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
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

        selectedRegion(player).ifPresent(region -> PacketDistributor.sendToServer(new RemoveClosestFerrousRegionPayload(region.id())));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null) {
            return;
        }

        tickArtifactsMagnetismFallback(minecraft, player);

        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return;
        }

        Optional<FerrousRegion> selectedRegion = selectedRegion(player);
        Set<UUID> entityRenderedRegionIds = new HashSet<>();

        AABB entitySearch = player.getBoundingBox().inflate(REGION_REVEAL_RADIUS);
        for (FerrousRegionEntity entity : minecraft.level.getEntitiesOfClass(FerrousRegionEntity.class, entitySearch)) {
            FerrousRegion entityRegion = entity.asRegion();
            Optional<FerrousRegion> syncedRegion = ClientFerrousRegionStore.byId(entityRegion.id());
            if (syncedRegion.isEmpty() || !syncedRegion.get().equals(entityRegion)) {
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
            return;
        }

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

    private static void tickArtifactsMagnetismFallback(Minecraft minecraft, LocalPlayer player) {
        if (!ModList.get().isLoaded("artifacts") || !hasArtifactsMagnetism(player) || minecraft.level == null) {
            return;
        }

        Vec3 source = player.position().add(0.0D, 0.75D, 0.0D);
        AABB search = player.getBoundingBox().inflate(ARTIFACTS_MAGNETISM_FALLBACK_RANGE);
        for (ItemEntity itemEntity : minecraft.level.getEntitiesOfClass(ItemEntity.class, search)) {
            Vec3 motion = itemEntity.getDeltaMovement();
            if (motion.lengthSqr() < 1.0E-6D) {
                continue;
            }

            Vec3 itemPosition = itemEntity.position();
            Vec3 itemCenter = itemEntity.getBoundingBox().getCenter();
            Vec3 itemToSource = source.subtract(itemCenter);
            if (itemToSource.lengthSqr() > ARTIFACTS_MAGNETISM_FALLBACK_RANGE_SQR || itemToSource.lengthSqr() < 1.0E-6D) {
                continue;
            }

            if (motion.normalize().dot(itemToSource.normalize()) <= 0.2D) {
                continue;
            }

            if (!clientBlocksMagnet(player.level(), source, itemPosition)) {
                continue;
            }

            itemEntity.setDeltaMovement(Vec3.ZERO);
            Vec3 previousPosition = new Vec3(itemEntity.xo, itemEntity.yo, itemEntity.zo);
            if (previousPosition.distanceToSqr(itemPosition) <= 4.0D && clientBlocksMagnet(player.level(), source, previousPosition)) {
                itemEntity.setPos(previousPosition.x, previousPosition.y, previousPosition.z);
            }
        }
    }

    private static boolean clientBlocksMagnet(net.minecraft.world.level.Level level, Vec3 source, Vec3 itemPosition) {
        if (ClientFerrousRegionStore.closestIntersecting(source, itemPosition).isPresent()) {
            return true;
        }

        return ModList.get().isLoaded("sable") && MagnotSableClientCompat.blocksMagnet(level, source, itemPosition);
    }

    private static boolean hasArtifactsMagnetism(LocalPlayer player) {
        return player.getActiveEffects().stream()
                .anyMatch(effect -> ARTIFACTS_MAGNETISM.equals(BuiltInRegistries.MOB_EFFECT.getKey(effect.getEffect().value())));
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
