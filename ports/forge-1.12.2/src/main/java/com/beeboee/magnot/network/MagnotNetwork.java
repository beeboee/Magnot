package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.client.ClientRegionStore;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.RegionActions;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class MagnotNetwork {
    private static final SimpleNetworkWrapper CHANNEL =
            NetworkRegistry.INSTANCE.newSimpleChannel(Magnot.MOD_ID);
    private static int discriminator;

    private MagnotNetwork() {
    }

    public static void register() {
        CHANNEL.registerMessage(SyncHandler.class, SyncMessage.class, discriminator++, Side.CLIENT);
        CHANNEL.registerMessage(RemoveHandler.class, RemoveMessage.class, discriminator++, Side.SERVER);
    }

    public static void sync(EntityPlayerMP player) {
        CHANNEL.sendTo(
                new SyncMessage(FerrousRegionSavedData.get(player.getServerWorld()).regions()),
                player
        );
    }

    public static void syncDimension(WorldServer world) {
        CHANNEL.sendToDimension(
                new SyncMessage(FerrousRegionSavedData.get(world).regions()),
                world.provider.getDimension()
        );
    }

    public static void remove(UUID selectedRegionId) {
        CHANNEL.sendToServer(new RemoveMessage(selectedRegionId));
    }

    public static final class SyncMessage implements IMessage {
        private List<FerrousRegion> regions = new ArrayList<>();

        public SyncMessage() {
        }

        SyncMessage(List<FerrousRegion> regions) {
            this.regions = new ArrayList<>(regions);
        }

        @Override
        public void fromBytes(ByteBuf buffer) {
            int count = readVarInt(buffer);
            regions = new ArrayList<>(count);
            for (int index = 0; index < count; index++) {
                UUID id = new UUID(buffer.readLong(), buffer.readLong());
                UUID groupId = new UUID(buffer.readLong(), buffer.readLong());
                BlockPos min = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                BlockPos max = new BlockPos(buffer.readInt(), buffer.readInt(), buffer.readInt());
                regions.add(new FerrousRegion(id, groupId, min, max));
            }
        }

        @Override
        public void toBytes(ByteBuf buffer) {
            writeVarInt(buffer, regions.size());
            for (FerrousRegion region : regions) {
                buffer.writeLong(region.id().getMostSignificantBits());
                buffer.writeLong(region.id().getLeastSignificantBits());
                buffer.writeLong(region.groupId().getMostSignificantBits());
                buffer.writeLong(region.groupId().getLeastSignificantBits());
                buffer.writeInt(region.min().getX());
                buffer.writeInt(region.min().getY());
                buffer.writeInt(region.min().getZ());
                buffer.writeInt(region.max().getX());
                buffer.writeInt(region.max().getY());
                buffer.writeInt(region.max().getZ());
            }
        }
    }

    public static final class SyncHandler implements IMessageHandler<SyncMessage, IMessage> {
        @Override
        public IMessage onMessage(SyncMessage message, MessageContext context) {
            FMLCommonHandler.instance()
                    .getWorldThread(context.netHandler)
                    .addScheduledTask(() -> ClientRegionStore.set(message.regions));
            return null;
        }
    }

    public static final class RemoveMessage implements IMessage {
        private UUID selectedRegionId;

        public RemoveMessage() {
        }

        RemoveMessage(UUID selectedRegionId) {
            this.selectedRegionId = selectedRegionId;
        }

        @Override
        public void fromBytes(ByteBuf buffer) {
            selectedRegionId = new UUID(buffer.readLong(), buffer.readLong());
        }

        @Override
        public void toBytes(ByteBuf buffer) {
            buffer.writeLong(selectedRegionId.getMostSignificantBits());
            buffer.writeLong(selectedRegionId.getLeastSignificantBits());
        }
    }

    public static final class RemoveHandler implements IMessageHandler<RemoveMessage, IMessage> {
        @Override
        public IMessage onMessage(RemoveMessage message, MessageContext context) {
            EntityPlayerMP player = context.getServerHandler().player;
            player.getServerWorld().addScheduledTask(
                    () -> RegionActions.remove(player, message.selectedRegionId)
            );
            return null;
        }
    }

    private static void writeVarInt(ByteBuf buffer, int value) {
        while ((value & -128) != 0) {
            buffer.writeByte(value & 127 | 128);
            value >>>= 7;
        }
        buffer.writeByte(value);
    }

    private static int readVarInt(ByteBuf buffer) {
        int value = 0;
        int shift = 0;
        byte current;
        do {
            current = buffer.readByte();
            value |= (current & 127) << shift++ * 7;
            if (shift > 5) {
                throw new IllegalArgumentException("Magnot region packet VarInt is too large");
            }
        } while ((current & 128) == 128);
        return value;
    }
}
