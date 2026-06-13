package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record SyncFerrousRegionsPayload(List<FerrousRegion> regions) implements CustomPacketPayload {
    public static final Type<SyncFerrousRegionsPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Magnot.MOD_ID, "sync_ferrous_regions")
    );

    public SyncFerrousRegionsPayload {
        regions = List.copyOf(regions);
    }

    public static SyncFerrousRegionsPayload decode(FriendlyByteBuf buf) {
        int count = buf.readVarInt();
        List<FerrousRegion> regions = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            UUID id = buf.readUUID();
            BlockPos min = buf.readBlockPos();
            BlockPos max = buf.readBlockPos();
            UUID owner = buf.readUUID();
            regions.add(new FerrousRegion(id, min, max, owner));
        }

        return new SyncFerrousRegionsPayload(regions);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(regions.size());
        for (FerrousRegion region : regions) {
            buf.writeUUID(region.id());
            buf.writeBlockPos(region.min());
            buf.writeBlockPos(region.max());
            buf.writeUUID(region.owner());
        }
    }

    public static void handle(SyncFerrousRegionsPayload payload, IPayloadContext context) {
        ClientFerrousRegionStore.setRegions(payload.regions());
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
