package com.beeboee.magnot.region;

import com.beeboee.magnot.compat.sable.MagnotSableCompat;
import com.beeboee.magnot.debug.MagnotDebug;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Shared entry point for magnet integrations.
 *
 * Supported magnet adapters should call this before pulling a target. If this returns true,
 * the adapter should skip that target or abort the magnet pass.
 */
public final class FerrousMagnetRules {
    private static final double CACHE_SCALE = 64.0D;
    private static final Map<MagnetCheckKey, Boolean> CHECK_CACHE = new HashMap<>();
    private static final Map<SourceCheckKey, Boolean> SOURCE_CACHE = new HashMap<>();
    private static long cacheTick = Long.MIN_VALUE;
    private static String cacheDimension = "";

    private FerrousMagnetRules() {
    }

    public static boolean blocksMagnet(ServerLevel level, Vec3 magnetSource, Vec3 targetPosition) {
        if (sourceBlocked(level, magnetSource)) {
            return true;
        }

        MagnetCheckKey cacheKey = MagnetCheckKey.source(magnetSource, targetPosition);
        Boolean cached = getCached(level, cacheKey);
        if (cached != null) {
            MagnotDebug.recordCacheHit(level, false);
            return cached;
        }

        boolean blocked;
        if (ModList.get().isLoaded("sable")) {
            blocked = MagnotSableCompat.blocksMagnet(level, magnetSource, targetPosition);
            MagnotDebug.recordFallbackCheck(level, "sable", blocked);
        } else {
            blocked = FerrousRegionSavedData.get(level).blocksMagnet(magnetSource, targetPosition);
            MagnotDebug.recordFallbackCheck(level, "saved-data", blocked);
        }

        putCached(level, cacheKey, blocked);
        return blocked;
    }

    public static boolean blocksItemPull(ServerLevel level, Vec3 magnetSource, ItemEntity item) {
        return blocksMagnet(level, magnetSource, itemPullTarget(item));
    }

    public static Vec3 itemPullTarget(ItemEntity item) {
        return item.position().add(0.0D, item.getBbHeight() / 2.0D, 0.0D);
    }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 targetPosition) {
        if (playerSourceBlocked(level, player)) {
            return true;
        }

        MagnetCheckKey cacheKey = MagnetCheckKey.player(player, targetPosition);
        Boolean cached = getCached(level, cacheKey);
        if (cached != null) {
            MagnotDebug.recordCacheHit(level, true);
            return cached;
        }

        AABB body = player.getBoundingBox();
        Vec3 feet = player.position();
        Vec3 bodyCenter = body.getCenter();
        Vec3 eye = player.getEyePosition();

        boolean blocked;
        if (ModList.get().isLoaded("sable")) {
            blocked = MagnotSableCompat.blocksMagnet(level, feet, targetPosition)
                    || MagnotSableCompat.blocksMagnet(level, bodyCenter, targetPosition)
                    || MagnotSableCompat.blocksMagnet(level, eye, targetPosition);
            MagnotDebug.recordFallbackCheck(level, "sable", blocked);
        } else {
            FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
            blocked = data.blocksMagnet(feet, targetPosition)
                    || data.blocksMagnet(bodyCenter, targetPosition)
                    || data.blocksMagnet(eye, targetPosition);
            MagnotDebug.recordFallbackCheck(level, "saved-data", blocked);
        }

        putCached(level, cacheKey, blocked);
        return blocked;
    }

    private static boolean playerSourceBlocked(ServerLevel level, Player player) {
        AABB body = player.getBoundingBox();
        return sourceBlocked(level, player.position())
                || sourceBlocked(level, body.getCenter())
                || sourceBlocked(level, player.getEyePosition());
    }

    private static boolean sourceBlocked(ServerLevel level, Vec3 source) {
        SourceCheckKey key = SourceCheckKey.point(source);
        Boolean cached = getCached(level, key);
        if (cached != null) {
            return cached;
        }

        boolean blocked;
        if (ModList.get().isLoaded("sable")) {
            blocked = MagnotSableCompat.containsPoint(level, source);
        } else {
            blocked = FerrousRegionSavedData.get(level).containsPoint(source);
        }

        putCached(level, key, blocked);
        return blocked;
    }

    private static Boolean getCached(ServerLevel level, MagnetCheckKey key) {
        prepareCache(level);
        return CHECK_CACHE.get(key);
    }

    private static void putCached(ServerLevel level, MagnetCheckKey key, boolean blocked) {
        prepareCache(level);
        CHECK_CACHE.put(key, blocked);
    }

    private static Boolean getCached(ServerLevel level, SourceCheckKey key) {
        prepareCache(level);
        return SOURCE_CACHE.get(key);
    }

    private static void putCached(ServerLevel level, SourceCheckKey key, boolean blocked) {
        prepareCache(level);
        SOURCE_CACHE.put(key, blocked);
    }

    private static void prepareCache(ServerLevel level) {
        long gameTime = level.getGameTime();
        String dimension = level.dimension().location().toString();
        if (gameTime != cacheTick || !dimension.equals(cacheDimension)) {
            CHECK_CACHE.clear();
            SOURCE_CACHE.clear();
            cacheTick = gameTime;
            cacheDimension = dimension;
        }
    }

    private record SourceCheckKey(int sourceX, int sourceY, int sourceZ) {
        private static SourceCheckKey point(Vec3 source) {
            return new SourceCheckKey(
                    block(source.x),
                    block(source.y),
                    block(source.z)
            );
        }
    }

    private record MagnetCheckKey(
            boolean player,
            long playerMost,
            long playerLeast,
            int sourceX,
            int sourceY,
            int sourceZ,
            int targetX,
            int targetY,
            int targetZ
    ) {
        private static MagnetCheckKey source(Vec3 source, Vec3 target) {
            return new MagnetCheckKey(
                    false,
                    0L,
                    0L,
                    bucket(source.x),
                    bucket(source.y),
                    bucket(source.z),
                    bucket(target.x),
                    bucket(target.y),
                    bucket(target.z)
            );
        }

        private static MagnetCheckKey player(Player player, Vec3 target) {
            UUID uuid = player.getUUID();
            Vec3 source = player.position();
            return new MagnetCheckKey(
                    true,
                    uuid.getMostSignificantBits(),
                    uuid.getLeastSignificantBits(),
                    bucket(source.x),
                    bucket(source.y),
                    bucket(source.z),
                    bucket(target.x),
                    bucket(target.y),
                    bucket(target.z)
            );
        }

        private static int bucket(double value) {
            return (int) Math.floor(value * CACHE_SCALE);
        }
    }

    private static int block(double value) {
        return (int) Math.floor(value);
    }
}
