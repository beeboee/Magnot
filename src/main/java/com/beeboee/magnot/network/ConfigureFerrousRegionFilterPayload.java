package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.server.FerrousRegionActions;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.UUID;

public record ConfigureFerrousRegionFilterPayload(UUID selectedRegionId, ItemStack filterStack, boolean clear, boolean toggleMode) implements CustomPacketPayload {
    public static final Type<ConfigureFerrousRegionFilterPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(Magnot.MOD_ID, "configure_ferrous_region_filter")
    );

    public ConfigureFerrousRegionFilterPayload {
        filterStack = filterStack == null ? ItemStack.EMPTY : filterStack.copyWithCount(1);
    }

    public static ConfigureFerrousRegionFilterPayload decode(RegistryFriendlyByteBuf buf) {
        UUID selectedRegionId = buf.readUUID();
        ItemStack filterStack = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
        boolean clear = buf.readBoolean();
        boolean toggleMode = buf.readBoolean();
        return new ConfigureFerrousRegionFilterPayload(selectedRegionId, filterStack, clear, toggleMode);
    }

    public void write(RegistryFriendlyByteBuf buf) {
        buf.writeUUID(selectedRegionId);
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, filterStack);
        buf.writeBoolean(clear);
        buf.writeBoolean(toggleMode);
    }

    public static void handle(ConfigureFerrousRegionFilterPayload payload, IPayloadContext context) {
        if (context.player() instanceof ServerPlayer serverPlayer) {
            FerrousRegionActions.configureSelectedRegionFilter(
                    serverPlayer,
                    payload.selectedRegionId(),
                    payload.filterStack(),
                    payload.clear(),
                    payload.toggleMode()
            );
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
