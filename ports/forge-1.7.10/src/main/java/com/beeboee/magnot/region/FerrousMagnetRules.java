package com.beeboee.magnot.region;

import net.minecraft.util.Vec3;
import net.minecraft.world.WorldServer;

import java.util.HashMap;
import java.util.Map;

public final class FerrousMagnetRules {
    private static final double CACHE_SCALE = 64.0D;
    private static final Map<String, Boolean> CACHE = new HashMap<String, Boolean>();
    private static long cacheTick = Long.MIN_VALUE;
    private static int cacheDimension = Integer.MIN_VALUE;

    private FerrousMagnetRules() {
    }

    public static boolean blocksMagnet(WorldServer world, Vec3 source, Vec3 target) {
        prepareCache(world);
        String key = key(source, target);
        Boolean cached = CACHE.get(key);
        if (cached != null) {
            return cached.booleanValue();
        }

        FerrousRegionSavedData data = FerrousRegionSavedData.get(world);
        boolean blocked = data.containsPoint(source) || data.blocksMagnet(source, target);
        CACHE.put(key, Boolean.valueOf(blocked));
        return blocked;
    }

    static void invalidateCaches() {
        CACHE.clear();
        cacheTick = Long.MIN_VALUE;
        cacheDimension = Integer.MIN_VALUE;
    }

    private static void prepareCache(WorldServer world) {
        long tick = world.getTotalWorldTime();
        int dimension = world.provider.dimensionId;
        if (tick != cacheTick || dimension != cacheDimension) {
            CACHE.clear();
            cacheTick = tick;
            cacheDimension = dimension;
        }
    }

    private static String key(Vec3 source, Vec3 target) {
        return bucket(source.xCoord) + ":" + bucket(source.yCoord) + ":" + bucket(source.zCoord) + ":"
                + bucket(target.xCoord) + ":" + bucket(target.yCoord) + ":" + bucket(target.zCoord);
    }

    private static int bucket(double value) {
        return (int) Math.floor(value * CACHE_SCALE);
    }
}
