package com.beeboee.magnot.server;

import com.beeboee.magnot.network.MagnotNetwork;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public final class FerrousRegionActions {
    private FerrousRegionActions() {}
    public static boolean removeSelectedRegion(ServerPlayer player, UUID id) {
        if (!(player.level() instanceof ServerLevel level) || !player.getMainHandItem().is(MagnotItems.FERROUS_TUBE)) return false;
        Vec3 from=player.getEyePosition(); Vec3 to=from.add(player.getLookAngle().scale(6.0D));
        Optional<FerrousRegion> removed=FerrousRegionSavedData.get(level).removeIntersectingById(id,from,to); if(removed.isEmpty()) return false;
        FerrousEffects.removal(level,removed.get()); MagnotNetwork.syncToPlayersInDimension(level); player.displayClientMessage(Component.translatable("message.magnot.region_removed"),true); return true;
    }
}
