package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public final class MagnotNetwork {
    private static final String PROTOCOL = "2";
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(Magnot.MOD_ID, "main"))
            .networkProtocolVersion(() -> PROTOCOL)
            .clientAcceptedVersions(PROTOCOL::equals)
            .serverAcceptedVersions(PROTOCOL::equals)
            .simpleChannel();
    private static int nextId;
    private static boolean registered;

    private MagnotNetwork() {
    }

    public static void register() {
        if (registered) return;
        registered = true;
        CHANNEL.messageBuilder(SyncFerrousRegionsPacket.class, nextId++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(SyncFerrousRegionsPacket::encode)
                .decoder(SyncFerrousRegionsPacket::decode)
                .consumerMainThread(SyncFerrousRegionsPacket::handle)
                .add();
        CHANNEL.messageBuilder(RemoveFerrousRegionPacket.class, nextId++, NetworkDirection.PLAY_TO_SERVER)
                .encoder(RemoveFerrousRegionPacket::encode)
                .decoder(RemoveFerrousRegionPacket::decode)
                .consumerMainThread(RemoveFerrousRegionPacket::handle)
                .add();
    }

    public static void syncTo(ServerPlayer player) {
        if (player.level() instanceof ServerLevel level) {
            CHANNEL.send(
                    PacketDistributor.PLAYER.with(() -> player),
                    new SyncFerrousRegionsPacket(FerrousRegionSavedData.get(level).regions())
            );
        }
    }

    public static void syncToPlayersInDimension(ServerLevel level) {
        CHANNEL.send(
                PacketDistributor.DIMENSION.with(level::dimension),
                new SyncFerrousRegionsPacket(FerrousRegionSavedData.get(level).regions())
        );
    }

    public static void removeRegion(java.util.UUID id) {
        CHANNEL.sendToServer(new RemoveFerrousRegionPacket(id));
    }
}
