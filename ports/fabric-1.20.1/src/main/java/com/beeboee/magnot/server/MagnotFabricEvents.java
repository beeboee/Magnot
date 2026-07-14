package com.beeboee.magnot.server;

import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import com.beeboee.magnot.registry.MagnotItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class MagnotFabricEvents {
    private MagnotFabricEvents() {
    }

    public static void endServerTick(MinecraftServer server) {
        if (server.getTickCount() % 20 != 0) {
            return;
        }
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            if (!player.getMainHandItem().is(MagnotItems.FERROUS_TUBE)
                    && !player.getOffhandItem().is(MagnotItems.FERROUS_TUBE)) {
                continue;
            }
            ServerLevel level = player.serverLevel();
            Vec3 playerPos = player.position();
            for (FerrousRegion region : FerrousRegionSavedData.get(level).regions()) {
                if (region.bounds().getCenter().distanceToSqr(playerPos) <= 96.0D * 96.0D) {
                    spawnOutline(level, region);
                }
            }
        }
    }

    public static void spawnOutline(ServerLevel level, FerrousRegion region) {
        AABB box = region.bounds();
        line(level, box.minX, box.minY, box.minZ, box.maxX, box.minY, box.minZ);
        line(level, box.minX, box.maxY, box.minZ, box.maxX, box.maxY, box.minZ);
        line(level, box.minX, box.minY, box.maxZ, box.maxX, box.minY, box.maxZ);
        line(level, box.minX, box.maxY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        line(level, box.minX, box.minY, box.minZ, box.minX, box.maxY, box.minZ);
        line(level, box.maxX, box.minY, box.minZ, box.maxX, box.maxY, box.minZ);
        line(level, box.minX, box.minY, box.maxZ, box.minX, box.maxY, box.maxZ);
        line(level, box.maxX, box.minY, box.maxZ, box.maxX, box.maxY, box.maxZ);
        line(level, box.minX, box.minY, box.minZ, box.minX, box.minY, box.maxZ);
        line(level, box.maxX, box.minY, box.minZ, box.maxX, box.minY, box.maxZ);
        line(level, box.minX, box.maxY, box.minZ, box.minX, box.maxY, box.maxZ);
        line(level, box.maxX, box.maxY, box.minZ, box.maxX, box.maxY, box.maxZ);
    }

    private static void line(ServerLevel level, double x1, double y1, double z1, double x2, double y2, double z2) {
        int steps = 4;
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            level.sendParticles(
                    ParticleTypes.CRIT,
                    x1 + (x2 - x1) * t,
                    y1 + (y2 - y1) * t,
                    z1 + (z2 - z1) * t,
                    1,
                    0.0D,
                    0.0D,
                    0.0D,
                    0.0D
            );
        }
    }
}
