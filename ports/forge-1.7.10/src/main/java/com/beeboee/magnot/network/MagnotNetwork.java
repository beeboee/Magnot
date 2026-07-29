package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.MagnotEvents;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

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
                new SyncMessage(FerrousRegionSavedData.get((WorldServer) player.worldObj).regions()),
                player
        );
    }

    public static void syncDimension(WorldServer world) {
        CHANNEL.sendToDimension(
                new SyncMessage(FerrousRegionSavedData.get(world).regions()),
                world.provider.dimensionId
        );
    }

    public static void remove(UUID selectedRegionId) {
        CHANNEL.sendToServer(new RemoveMessage(selectedRegionId));
    }

    public static final class SyncMessage implements IMessage {
        private List<FerrousRegion> regions = new ArrayList<FerrousRegion>();

        public SyncMessage() {
        }

        private SyncMessage(List<FerrousRegion> regions) {
            this.regions = new ArrayList<FerrousRegion>(regions);
        }

        @Override
        public void fromBytes(ByteBuf buffer) {
            int count = readVarInt(buffer);
            regions = new ArrayList<FerrousRegion>(count);
            for (int index = 0; index < count; index++) {
                UUID id = new UUID(buffer.readLong(), buffer.readLong());
                UUID groupId = new UUID(buffer.readLong(), buffer.readLong());
                regions.add(new FerrousRegion(
                        id,
                        groupId,
                        buffer.readInt(),
                        buffer.readInt(),
                        buffer.readInt(),
                        buffer.readInt(),
                        buffer.readInt(),
                        buffer.readInt()
                ));
            }
        }

        @Override
        public void toBytes(ByteBuf buffer) {
            writeVarInt(buffer, regions.size());
            for (FerrousRegion region : regions) {
                buffer.writeLong(region.getId().getMostSignificantBits());
                buffer.writeLong(region.getId().getLeastSignificantBits());
                buffer.writeLong(region.getGroupId().getMostSignificantBits());
                buffer.writeLong(region.getGroupId().getLeastSignificantBits());
                buffer.writeInt(region.getMinX());
                buffer.writeInt(region.getMinY());
                buffer.writeInt(region.getMinZ());
                buffer.writeInt(region.getMaxX());
                buffer.writeInt(region.getMaxY());
                buffer.writeInt(region.getMaxZ());
            }
        }
    }

    public static final class SyncHandler implements IMessageHandler<SyncMessage, IMessage> {
        @Override
        public IMessage onMessage(SyncMessage message, MessageContext context) {
            Magnot.PROXY.acceptRegionSync(message.regions);
            return null;
        }
    }

    public static final class RemoveMessage implements IMessage {
        private UUID selectedRegionId;

        public RemoveMessage() {
        }

        private RemoveMessage(UUID selectedRegionId) {
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
            EntityPlayerMP player = context.getServerHandler().playerEntity;
            if (player != null) {
                MagnotEvents.enqueueRemoval(player, message.selectedRegionId);
            }
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
        int bytes = 0;
        byte current;
        do {
            current = buffer.readByte();
            value |= (current & 127) << bytes++ * 7;
            if (bytes > 5) {
                throw new IllegalArgumentException("Magnot region packet VarInt is too large");
            }
        } while ((current & 128) == 128);
        return value;
    }
}
