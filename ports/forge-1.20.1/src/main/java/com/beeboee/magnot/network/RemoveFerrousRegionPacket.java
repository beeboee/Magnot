package com.beeboee.magnot.network;

import com.beeboee.magnot.server.FerrousRegionActions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public record RemoveFerrousRegionPacket(UUID regionId) {
    public static void encode(RemoveFerrousRegionPacket packet, FriendlyByteBuf buf) {
        buf.writeUUID(packet.regionId);
    }

    public static RemoveFerrousRegionPacket decode(FriendlyByteBuf buf) {
        return new RemoveFerrousRegionPacket(buf.readUUID());
    }

    public static void handle(RemoveFerrousRegionPacket packet, Supplier<NetworkEvent.Context> context) {
        ServerPlayer player = context.get().getSender();
        if (player != null) {
            context.get().enqueueWork(() -> FerrousRegionActions.removeSelectedRegion(player, packet.regionId));
        }
        context.get().setPacketHandled(true);
    }
}
