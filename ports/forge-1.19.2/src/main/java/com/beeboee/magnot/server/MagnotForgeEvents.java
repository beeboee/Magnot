package com.beeboee.magnot.server;

import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public final class MagnotForgeEvents {
    private MagnotForgeEvents() {}

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof ServerPlayer) || event.player.tickCount % 20 != 0) return;
        ServerPlayer player = (ServerPlayer) event.player;
        if (!player.getMainHandItem().is(MagnotItems.FERROUS_TUBE.get()) && !player.getOffhandItem().is(MagnotItems.FERROUS_TUBE.get())) return;
        ServerLevel level = player.serverLevel();
        Vec3 playerPos = player.position();
        for (FerrousRegion region : FerrousRegionSavedData.get(level).regions()) {
            if (region.bounds().getCenter().distanceToSqr(playerPos) <= 9216.0D) spawnOutline(level, region);
        }
    }

    public static void spawnOutline(ServerLevel level, FerrousRegion region) {
        AABB b = region.bounds();
        line(level,b.minX,b.minY,b.minZ,b.maxX,b.minY,b.minZ); line(level,b.minX,b.maxY,b.minZ,b.maxX,b.maxY,b.minZ);
        line(level,b.minX,b.minY,b.maxZ,b.maxX,b.minY,b.maxZ); line(level,b.minX,b.maxY,b.maxZ,b.maxX,b.maxY,b.maxZ);
        line(level,b.minX,b.minY,b.minZ,b.minX,b.maxY,b.minZ); line(level,b.maxX,b.minY,b.minZ,b.maxX,b.maxY,b.minZ);
        line(level,b.minX,b.minY,b.maxZ,b.minX,b.maxY,b.maxZ); line(level,b.maxX,b.minY,b.maxZ,b.maxX,b.maxY,b.maxZ);
        line(level,b.minX,b.minY,b.minZ,b.minX,b.minY,b.maxZ); line(level,b.maxX,b.minY,b.minZ,b.maxX,b.minY,b.maxZ);
        line(level,b.minX,b.maxY,b.minZ,b.minX,b.maxY,b.maxZ); line(level,b.maxX,b.maxY,b.minZ,b.maxX,b.maxY,b.maxZ);
    }

    private static void line(ServerLevel level,double x1,double y1,double z1,double x2,double y2,double z2) {
        for (int i=0;i<=4;i++) {
            double t=i/4.0D;
            level.sendParticles(ParticleTypes.CRIT,x1+(x2-x1)*t,y1+(y2-y1)*t,z1+(z2-z1)*t,1,0,0,0,0);
        }
    }
}
