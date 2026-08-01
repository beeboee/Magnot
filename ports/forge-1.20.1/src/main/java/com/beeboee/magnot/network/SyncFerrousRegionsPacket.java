package com.beeboee.magnot.network;

import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public record SyncFerrousRegionsPacket(List<FerrousRegion> regions) {
    public SyncFerrousRegionsPacket {
        regions = List.copyOf(regions);
    }

    public static void encode(SyncFerrousRegionsPacket packet, FriendlyByteBuf buf) {
        buf.writeVarInt(packet.regions.size());
        for (FerrousRegion region : packet.regions) {
            buf.writeUUID(region.id());
            buf.writeUUID(region.groupId());
            buf.writeBlockPos(region.min());
            buf.writeBlockPos(region.max());
        }
    }

    public static SyncFerrousRegionsPacket decode(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        List<FerrousRegion> regions = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            UUID id = buf.readUUID();
            UUID groupId = buf.readUUID();
            BlockPos min = buf.readBlockPos();
            BlockPos max = buf.readBlockPos();
            regions.add(new FerrousRegion(id, groupId, min, max));
        }
        return new SyncFerrousRegionsPacket(regions);
    }

    public static void handle(SyncFerrousRegionsPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> ClientFerrousRegionStore.setRegions(packet.regions));
        context.get().setPacketHandled(true);
    }
}
