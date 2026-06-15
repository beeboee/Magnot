package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
            UUID groupId = buf.readUUID();
            BlockPos min = buf.readBlockPos();
            BlockPos max = buf.readBlockPos();
            UUID subLevelId = buf.readBoolean() ? buf.readUUID() : null;
            ItemStack filterStack = readFilterStack(buf);
            boolean whitelistMode = buf.readBoolean();
            regions.add(new FerrousRegion(id, groupId, min, max, subLevelId, filterStack, whitelistMode));
        }

        return new SyncFerrousRegionsPayload(regions);
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeVarInt(regions.size());
        for (FerrousRegion region : regions) {
            buf.writeUUID(region.id());
            buf.writeUUID(region.groupId());
            buf.writeBlockPos(region.min());
            buf.writeBlockPos(region.max());
            buf.writeBoolean(region.subLevelId() != null);
            if (region.subLevelId() != null) {
                buf.writeUUID(region.subLevelId());
            }
            writeFilterStack(buf, region.filterStack());
            buf.writeBoolean(region.whitelistMode());
        }
    }

    public static void handle(SyncFerrousRegionsPayload payload, IPayloadContext context) {
        ClientFerrousRegionStore.setRegions(payload.regions());
    }

    private static void writeFilterStack(FriendlyByteBuf buf, ItemStack stack) {
        buf.writeBoolean(!stack.isEmpty());
        if (!stack.isEmpty()) {
            buf.writeUtf(BuiltInRegistries.ITEM.getKey(stack.getItem()).toString());
        }
    }

    private static ItemStack readFilterStack(FriendlyByteBuf buf) {
        if (!buf.readBoolean()) {
            return ItemStack.EMPTY;
        }

        ResourceLocation itemId = ResourceLocation.tryParse(buf.readUtf());
        if (itemId == null) {
            return ItemStack.EMPTY;
        }

        Item item = BuiltInRegistries.ITEM.get(itemId);
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
