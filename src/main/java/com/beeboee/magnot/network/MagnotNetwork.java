package com.beeboee.magnot.network;

import com.beeboee.magnot.region.FerrousRegionSavedData;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class MagnotNetwork {
    private MagnotNetwork() {
    }

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SyncFerrousRegionsPayload.TYPE,
                StreamCodec.of(
                        (RegistryFriendlyByteBuf buf, SyncFerrousRegionsPayload payload) -> payload.write(buf),
                        SyncFerrousRegionsPayload::decode
                ),
                SyncFerrousRegionsPayload::handle
        );
        registrar.playToServer(
                RemoveClosestFerrousRegionPayload.TYPE,
                StreamCodec.of(
                        (RegistryFriendlyByteBuf buf, RemoveClosestFerrousRegionPayload payload) -> payload.write(buf),
                        RemoveClosestFerrousRegionPayload::decode
                ),
                RemoveClosestFerrousRegionPayload::handle
        );
        registrar.playToServer(
                ConfigureFerrousRegionFilterPayload.TYPE,
                StreamCodec.of(
                        (RegistryFriendlyByteBuf buf, ConfigureFerrousRegionFilterPayload payload) -> payload.write(buf),
                        ConfigureFerrousRegionFilterPayload::decode
                ),
                ConfigureFerrousRegionFilterPayload::handle
        );
    }

    public static void syncTo(ServerPlayer player) {
        if (!(player.level() instanceof ServerLevel serverLevel)) {
            return;
        }

        PacketDistributor.sendToPlayer(player, new SyncFerrousRegionsPayload(FerrousRegionSavedData.get(serverLevel).regions()));
    }

    public static void syncToPlayersInDimension(ServerLevel level) {
        PacketDistributor.sendToPlayersInDimension(level, new SyncFerrousRegionsPayload(FerrousRegionSavedData.get(level).regions()));
    }
}
