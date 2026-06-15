package com.beeboee.magnot.server;

import com.beeboee.magnot.compat.sable.MagnotSableCompat;
import com.beeboee.magnot.entity.FerrousRegionEntities;
import com.beeboee.magnot.item.FerrousTubeItem;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.registry.MagnotItems;
import com.simibubi.create.AllSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

import java.util.Optional;
import java.util.UUID;

public final class FerrousRegionActions {
    private FerrousRegionActions() {
    }

    public static boolean removeSelectedRegion(ServerPlayer player, UUID selectedRegionId) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.getLookAngle().scale(range));

        FerrousRegionSavedData data = FerrousRegionSavedData.get(serverLevel);
        Optional<FerrousRegion> removed = ModList.get().isLoaded("sable")
                ? data.removeWorldIntersectingById(selectedRegionId, from, to)
                : data.removeIntersectingById(selectedRegionId, from, to);

        if (removed.isEmpty() && ModList.get().isLoaded("sable")) {
            removed = MagnotSableCompat.removeSelectedRegion(serverLevel, selectedRegionId, from, to);
        }
        if (removed.isEmpty()) {
            return false;
        }

        playRemovalEffects(player, serverLevel, removed.get());
        return true;
    }

    public static boolean configureSelectedRegionFilter(ServerPlayer player, UUID selectedRegionId, ItemStack filterStack, boolean clear, boolean toggleMode) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        FerrousRegionSavedData data = FerrousRegionSavedData.get(serverLevel);
        Optional<FerrousRegion> region = data.findById(selectedRegionId);
        if (region.isEmpty()) {
            return false;
        }

        boolean changed;
        Component feedback = null;
        if (clear || filterStack.isEmpty()) {
            changed = data.clearRegionFilter(selectedRegionId);
            feedback = Component.translatable("message.magnot.filter_mode_blacklist");
        } else if (toggleMode && region.get().hasFilter()) {
            changed = data.toggleRegionFilterMode(selectedRegionId);
            feedback = data.findById(selectedRegionId).map(FerrousRegionActions::filterStateMessage).orElse(null);
        } else {
            boolean whitelistMode = FerrousTubeItem.isFilterWhitelistMode(player.getMainHandItem());
            changed = data.setRegionFilter(selectedRegionId, filterStack, whitelistMode);
            feedback = Component.translatable("message.magnot.filter_applied");
        }

        if (!changed) {
            return false;
        }

        if (feedback != null) {
            player.displayClientMessage(feedback, true);
        }
        FerrousParticles.spawnRedstoneBlockEdges(serverLevel, region.get());
        MagnotNetwork.syncToPlayersInDimension(serverLevel);
        return true;
    }

    public static boolean toggleHeldTubeFilterMode(ServerPlayer player) {
        ItemStack stack = player.getMainHandItem();
        if (!stack.is(MagnotItems.FERROUS_TUBE.get())
                || player.getOffhandItem().isEmpty()
                || FerrousTubeItem.getFirstCorner(stack).isPresent()) {
            return false;
        }

        FerrousTubeItem.toggleFilterMode(stack);
        player.displayClientMessage(FerrousTubeItem.filterModeMessage(stack), true);
        return true;
    }

    public static boolean removeClosestRegion(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return false;
        }

        double range = player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE) + 1.0D;
        Vec3 from = player.getEyePosition();
        Vec3 to = from.add(player.getLookAngle().scale(range));

        Optional<FerrousRegion> removed = FerrousRegionSavedData.get(serverLevel).removeClosestIntersecting(from, to);
        if (removed.isEmpty()) {
            return false;
        }

        playRemovalEffects(player, serverLevel, removed.get());
        return true;
    }

    private static Component filterStateMessage(FerrousRegion region) {
        return Component.translatable(region.whitelistMode() ? "message.magnot.filter_mode_whitelist" : "message.magnot.filter_mode_blacklist");
    }

    private static void playRemovalEffects(ServerPlayer player, ServerLevel serverLevel, FerrousRegion removed) {
        FerrousRegionEntities.discard(serverLevel, removed);
        BlockPos soundPos = BlockPos.containing(removed.bounds().getCenter());
        AllSoundEvents.SLIME_ADDED.play(serverLevel, null, soundPos, 0.5F, 0.5F);
        FerrousParticles.spawnRedstoneBlockEdges(serverLevel, removed);
        MagnotNetwork.syncToPlayersInDimension(serverLevel);
        player.displayClientMessage(Component.translatable("message.magnot.region_removed"), true);
    }
}
