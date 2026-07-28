package com.beeboee.magnot.server;

import com.beeboee.magnot.compat.sable.MagnotSableCompat;
import com.beeboee.magnot.entity.FerrousRegionEntities;
import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
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

    private static void playRemovalEffects(ServerPlayer player, ServerLevel serverLevel, FerrousRegion removed) {
        FerrousRegionEntities.discard(serverLevel, removed);
        BlockPos soundPos = BlockPos.containing(removed.bounds().getCenter());
        FerrousSelectionEffects.current().playRemoval(serverLevel, soundPos);
        FerrousParticles.spawnRedstoneBlockEdges(serverLevel, removed);
        MagnotNetwork.syncToPlayersInDimension(serverLevel);
        player.displayClientMessage(Component.translatable("message.magnot.region_removed"), true);
    }
}
