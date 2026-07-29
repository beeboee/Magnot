package com.beeboee.magnot.network;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.server.FerrousRegionActions;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class MagnotNetwork {
    public static final ResourceLocation SYNC=new ResourceLocation(Magnot.MOD_ID,"sync_regions");
    public static final ResourceLocation REMOVE=new ResourceLocation(Magnot.MOD_ID,"remove_region");
    private MagnotNetwork() {}
    public static void registerServer() { ServerPlayNetworking.registerGlobalReceiver(REMOVE,(server,player,handler,buf,responseSender)->{UUID id=buf.readUUID();server.execute(()->FerrousRegionActions.removeSelectedRegion(player,id));}); }
    public static void syncTo(ServerPlayer player) { if(player.level() instanceof ServerLevel level) ServerPlayNetworking.send(player,SYNC,encode(level)); }
    public static void syncToPlayersInDimension(ServerLevel level) { for(ServerPlayer player:PlayerLookup.world(level)) ServerPlayNetworking.send(player,SYNC,encode(level)); }
    public static void remove(UUID id) { FriendlyByteBuf buf=PacketByteBufs.create();buf.writeUUID(id);net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking.send(REMOVE,buf); }
    private static FriendlyByteBuf encode(ServerLevel level) { FriendlyByteBuf buf=PacketByteBufs.create(); java.util.List<FerrousRegion> regions=FerrousRegionSavedData.get(level).regions();buf.writeVarInt(regions.size());for(FerrousRegion region:regions){buf.writeUUID(region.id());buf.writeUUID(region.groupId());buf.writeBlockPos(region.min());buf.writeBlockPos(region.max());}return buf; }
}
