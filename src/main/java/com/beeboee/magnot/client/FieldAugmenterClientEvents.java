package com.beeboee.magnot.client;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.compat.sable.MagnotSableClientCompat;
import com.beeboee.magnot.network.ConfigureFerrousRegionFilterPayload;
import com.beeboee.magnot.network.ToggleFerrousTubeFilterModePayload;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.registry.MagnotItems;
import net.createmod.catnip.outliner.Outliner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
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
public final class FieldAugmenterClientEvents {
    private static final Object SELECTED_REGION_SLOT = new Object();
    private static final int FERROUS_RED = 0xBD2537;
    private static long nextActionTick;

    private FieldAugmenterClientEvents() {
    }

    @SubscribeEvent
    public static void onInteractionKeyMappingTriggered(InputEvent.InteractionKeyMappingTriggered event) {
        if (!event.isUseItem()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null || !holdingFieldAugmenter(player)) {
            return;
        }

        long gameTime = player.level().getGameTime();
        if (gameTime < nextActionTick) {
            event.setCanceled(true);
            return;
        }

        if (player.isShiftKeyDown()) {
            event.setCanceled(true);
            event.setSwingHand(true);
            nextActionTick = gameTime + 5L;
            PacketDistributor.sendToServer(new ToggleFerrousTubeFilterModePayload());
            return;
        }

        Optional<FerrousRegion> selected = selectedRegion(player);
        if (selected.isEmpty()) {
            return;
        }

        event.setCanceled(true);
        event.setSwingHand(true);
        nextActionTick = gameTime + 5L;
        PacketDistributor.sendToServer(new ConfigureFerrousRegionFilterPayload(
                selected.get().id(),
                ItemStack.EMPTY,
                false,
                false
        ));
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null || minecraft.level == null || !holdingFieldAugmenter(player)) {
            return;
        }

        selectedRegion(player).ifPresent(region -> {
            AABB displayBounds = ModList.get().isLoaded("sable")
                    ? MagnotSableClientCompat.displayBounds(minecraft.level, region)
                    : region.bounds();

            Outliner.getInstance()
                    .showAABB(SELECTED_REGION_SLOT, displayBounds)
                    .colored(FERROUS_RED)
                    .withFaceTextures(MagnotSpecialTextures.FERROUS_REGION, MagnotSpecialTextures.FERROUS_REGION)
                    .disableLineNormals()
                    .lineWidth(1.0F / 16.0F);

            if (ModList.get().isLoaded("sable")) {
                MagnotSableClientCompat.disableOutlineTransform(SELECTED_REGION_SLOT);
            }
        });
    }

    private static boolean holdingFieldAugmenter(LocalPlayer player) {
        return player.getMainHandItem().is(MagnotItems.FIELD_AUGMENTER.get());
    }

    private static Optional<FerrousRegion> selectedRegion(LocalPlayer player) {
        Vec3 from = player.getEyePosition();
        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 to = from.add(player.getLookAngle().scale(range));

        Optional<FerrousRegion> selected = ClientFerrousRegionStore.closestIntersecting(from, to);
        if (selected.isEmpty() && ModList.get().isLoaded("sable")) {
            selected = MagnotSableClientCompat.closestIntersecting(player.level(), from, to);
        }
        return selected;
    }
}
