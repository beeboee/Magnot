package com.beeboee.magnot.region;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

import java.util.HashMap;
import java.util.Map;

public final class FerrousMagnetRules {
    private static final double CACHE_SCALE = 64.0D;
    private static final Map<String, Boolean> CACHE = new HashMap<>();
    private static long cacheTick = Long.MIN_VALUE;
    private static int cacheDimension = Integer.MIN_VALUE;

    private FerrousMagnetRules() {
    }

    public static boolean blocksMagnet(WorldServer world, Vec3d source, Vec3d target) {
        prepareCache(world);
        String key = key(source, target);
        Boolean cached = CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        FerrousRegionSavedData data = FerrousRegionSavedData.get(world);
        boolean blocked = data.containsPoint(source) || data.blocksMagnet(source, target);
        CACHE.put(key, blocked);
        return blocked;
    }

    public static boolean blocksItemPull(WorldServer world, Vec3d source, EntityItem item) {
        return blocksMagnet(world, source, itemTarget(item));
    }

    public static boolean blocksPlayerItemPull(WorldServer world, EntityPlayer player, EntityItem item) {
        AxisAlignedBB body = player.getEntityBoundingBox();
        Vec3d target = itemTarget(item);
        return blocksMagnet(world, player.getPositionVector(), target)
                || blocksMagnet(world, body.getCenter(), target)
                || blocksMagnet(world, player.getPositionEyes(1.0F), target);
    }

    public static Vec3d itemTarget(EntityItem item) {
        return item.getPositionVector().addVector(0.0D, item.height * 0.5D, 0.0D);
    }

    static void invalidateCaches() {
        CACHE.clear();
        cacheTick = Long.MIN_VALUE;
        cacheDimension = Integer.MIN_VALUE;
    }

    private static void prepareCache(WorldServer world) {
        long tick = world.getTotalWorldTime();
        int dimension = world.provider.getDimension();
        if (tick != cacheTick || dimension != cacheDimension) {
            CACHE.clear();
            cacheTick = tick;
            cacheDimension = dimension;
        }
    }

    private static String key(Vec3d source, Vec3d target) {
        return bucket(source.x) + ":" + bucket(source.y) + ":" + bucket(source.z) + ":"
                + bucket(target.x) + ":" + bucket(target.y) + ":" + bucket(target.z);
    }

    private static int bucket(double value) {
        return (int) Math.floor(value * CACHE_SCALE);
    }
}
