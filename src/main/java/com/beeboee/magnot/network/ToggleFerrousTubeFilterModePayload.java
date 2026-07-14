package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.server.FerrousRegionActions;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * Temporary beta packet name retained so the existing filter branch remains wire-compatible.
 */
public record ToggleFerrousTubeFilterModePayload() implements CustomPacketPayload {
    public static final Type<ToggleFerrousTubeFilterModePayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Magnot.MOD_ID, "toggle_ferrous_tube_filter_mode")
    );

    public static ToggleFerrousTubeFilterModePayload decode(FriendlyByteBuf buf) {
        return new ToggleFerrousTubeFilterModePayload();
    }

    public void write(FriendlyByteBuf buf) {
    }

    public static void handle(ToggleFerrousTubeFilterModePayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            FerrousRegionActions.toggleHeldFieldAugmenterMode(serverPlayer);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
