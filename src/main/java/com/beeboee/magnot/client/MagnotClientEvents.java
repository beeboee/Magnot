package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.compat.sable.MagnotSableClientCompat;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.RemoveClosestFerrousRegionPayload;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import net.createmod.catnip.outliner.Outliner;
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
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.Optional;

@EventBusSubscriber(modid = Magnot.MOD_ID, value = Dist.CLIENT)
public final class MagnotClientEvents {
    private static final Object SELECTION_OUTLINE_SLOT = new Object();
    private static final int FERROUS_RED = 0xBD2537;
    private static final int LIMIT_YELLOW = 0xFFD43B;
    private static final double REGION_REVEAL_RADIUS = 25.0D;
    private static final double REGION_REVEAL_RADIUS_SQR = REGION_REVEAL_RADIUS * REGION_REVEAL_RADIUS;
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

        ItemStack held = player.getMainHandItem();
        if (!held.is(MagnotItems.FERROUS_TUBE.get())) {
            return;
        }

        Optional<FerrousRegion> selectedRegion = selectedRegion(player);

        for (FerrousRegion region : ClientFerrousRegionStore.regions()) {
            AABB displayBounds = ModList.get().isLoaded("sable")
                    ? MagnotSableClientCompat.displayBounds(minecraft.level, region)
                    : region.bounds();

            if (!isNearPlayer(player, displayBounds)) {
                continue;
            }

            boolean selected = selectedRegion.map(FerrousRegion::id).filter(region.id()::equals).isPresent();
            MagnotSpecialTextures faceTexture = selected ? MagnotSpecialTextures.FERROUS_REGION : null;
            float lineWidth = selected ? 1.0F / 16.0F : 1.0F / 64.0F;

            Outliner.getInstance()
                    .showAABB(region.id(), displayBounds)
                    .colored(FERROUS_RED)
                    .withFaceTextures(faceTexture, faceTexture)
                    .disableLineNormals()
                    .lineWidth(lineWidth);
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
