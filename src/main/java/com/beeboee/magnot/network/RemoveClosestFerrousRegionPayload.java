package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.server.FerrousRegionActions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record RemoveClosestFerrousRegionPayload(UUID selectedRegionId) implements CustomPacketPayload {
    public static final Type<RemoveClosestFerrousRegionPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Magnot.MOD_ID, "remove_closest_ferrous_region")
    );

    public static RemoveClosestFerrousRegionPayload decode(FriendlyByteBuf buf) {
        return new RemoveClosestFerrousRegionPayload(buf.readUUID());
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeUUID(selectedRegionId);
    }

    public static void handle(RemoveClosestFerrousRegionPayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            FerrousRegionActions.removeSelectedRegion(serverPlayer, payload.selectedRegionId());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
