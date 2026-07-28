package com.beeboee.magnot.server;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.region.FerrousRegion;
import com.beeboee.magnot.region.FerrousRegionSavedData;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.world.WorldServer;

public final class MagnotEvents {
    @SubscribeEvent
    public void playerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.player instanceof EntityPlayerMP)) {
            return;
        }
        EntityPlayerMP player = (EntityPlayerMP) event.player;
        if (player.ticksExisted % 10 != 0) {
            return;
        }
        ItemStack held = player.getHeldItem();
        if (held == null || held.getItem() != Magnot.FERROUS_TUBE) {
            return;
        }

        WorldServer world = (WorldServer) player.worldObj;
        for (FerrousRegion region : FerrousRegionSavedData.get(world).regions()) {
            double centerX = (region.getMinX() + region.getMaxX() + 1.0D) * 0.5D;
            double centerY = (region.getMinY() + region.getMaxY() + 1.0D) * 0.5D;
            double centerZ = (region.getMinZ() + region.getMaxZ() + 1.0D) * 0.5D;
            if (player.getDistanceSq(centerX, centerY, centerZ) <= 96.0D * 96.0D) {
                showRegion(world, region);
            }
        }
    }

    public static void showRegion(WorldServer world, FerrousRegion region) {
        double minX = region.getMinX();
        double minY = region.getMinY();
        double minZ = region.getMinZ();
        double maxX = region.getMaxX() + 1.0D;
        double maxY = region.getMaxY() + 1.0D;
        double maxZ = region.getMaxZ() + 1.0D;

        line(world, minX, minY, minZ, maxX, minY, minZ);
        line(world, minX, maxY, minZ, maxX, maxY, minZ);
        line(world, minX, minY, maxZ, maxX, minY, maxZ);
        line(world, minX, maxY, maxZ, maxX, maxY, maxZ);
        line(world, minX, minY, minZ, minX, maxY, minZ);
        line(world, maxX, minY, minZ, maxX, maxY, minZ);
        line(world, minX, minY, maxZ, minX, maxY, maxZ);
        line(world, maxX, minY, maxZ, maxX, maxY, maxZ);
        line(world, minX, minY, minZ, minX, minY, maxZ);
        line(world, maxX, minY, minZ, maxX, minY, maxZ);
        line(world, minX, maxY, minZ, minX, maxY, maxZ);
        line(world, maxX, maxY, minZ, maxX, maxY, maxZ);
    }

    private static void line(WorldServer world, double x1, double y1, double z1,
                             double x2, double y2, double z2) {
        int steps = Math.max(4, (int) Math.ceil(Math.max(
                Math.abs(x2 - x1),
                Math.max(Math.abs(y2 - y1), Math.abs(z2 - z1))
        ) * 2.0D));
        for (int i = 0; i <= steps; i++) {
            double t = i / (double) steps;
            world.func_147487_a(
                    "reddust",
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
