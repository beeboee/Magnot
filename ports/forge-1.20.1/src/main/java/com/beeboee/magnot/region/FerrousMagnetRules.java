package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FerrousMagnetRules {
    private static final double CACHE_SCALE = 64.0D;
    private static final Map<MagnetCheckKey, Boolean> CHECK_CACHE = new HashMap<>();
    private static final Map<SourceCheckKey, Boolean> SOURCE_CACHE = new HashMap<>();
    private static long cacheTick = Long.MIN_VALUE;
    private static String cacheDimension = "";

    private FerrousMagnetRules() {
    }

    public static boolean blocksMagnet(ServerLevel level, Vec3 source, Vec3 target) {
        if (sourceBlocked(level, source)) return true;
        MagnetCheckKey key = MagnetCheckKey.source(source, target);
        Boolean cached = getCached(level, key);
        if (cached != null) return cached;
        boolean blocked = FerrousRegionSavedData.get(level).blocksMagnet(source, target);
        putCached(level, key, blocked);
        return blocked;
    }

    public static boolean blocksItemPull(ServerLevel level, Vec3 source, ItemEntity item) {
        return blocksMagnet(level, source, itemPullTarget(item));
    }

    public static boolean blocksPlayerItemPull(ServerLevel level, Player player, ItemEntity item) {
        return blocksPlayerMagnet(level, player, itemPullTarget(item));
    }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 target) {
        if (playerSourceBlocked(level, player)) return true;
        MagnetCheckKey key = MagnetCheckKey.player(player, target);
        Boolean cached = getCached(level, key);
        if (cached != null) return cached;
        AABB body = player.getBoundingBox();
        boolean blocked = FerrousRegionSavedData.get(level).blocksMagnet(player.position(), target)
                || FerrousRegionSavedData.get(level).blocksMagnet(body.getCenter(), target)
                || FerrousRegionSavedData.get(level).blocksMagnet(player.getEyePosition(), target);
        putCached(level, key, blocked);
        return blocked;
    }

    public static Vec3 itemPullTarget(ItemEntity item) {
        return item.position().add(0.0D, item.getBbHeight() * 0.5D, 0.0D);
    }

    public static MagnetQueryContext sourceContext(ServerLevel level, Vec3 source) {
        return new MagnetQueryContext(level, new Vec3[]{source}, null);
    }

    public static MagnetQueryContext playerContext(ServerLevel level, Player player) {
        AABB body = player.getBoundingBox();
        return new MagnetQueryContext(level, new Vec3[]{
                player.position(), body.getCenter(), player.getEyePosition()
        }, player);
    }

    static void invalidateCaches() {
        CHECK_CACHE.clear();
        SOURCE_CACHE.clear();
        cacheTick = Long.MIN_VALUE;
        cacheDimension = "";
    }

    private static boolean playerSourceBlocked(ServerLevel level, Player player) {
        AABB body = player.getBoundingBox();
        return sourceBlocked(level, player.position())
                || sourceBlocked(level, body.getCenter())
                || sourceBlocked(level, player.getEyePosition());
    }

    private static boolean sourceBlocked(ServerLevel level, Vec3 source) {
        SourceCheckKey key = SourceCheckKey.point(source);
        prepareCache(level);
        Boolean cached = SOURCE_CACHE.get(key);
        if (cached != null) return cached;
        boolean blocked = FerrousRegionSavedData.get(level).containsPoint(source);
        SOURCE_CACHE.put(key, blocked);
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

    public static final class MagnetQueryContext {
        private final ServerLevel level;
        private final Vec3[] sources;
        private final Player player;
        private final boolean sourceBlocked;
        private final Map<BlockPos, List<FerrousRegion>> candidates = new HashMap<>();

        private MagnetQueryContext(ServerLevel level, Vec3[] sources, Player player) {
            this.level = level;
            this.sources = sources;
            this.player = player;
            boolean blocked = false;
            for (Vec3 source : sources) {
                if (FerrousMagnetRules.sourceBlocked(level, source)) {
                    blocked = true;
                    break;
                }
            }
            this.sourceBlocked = blocked;
        }

        public boolean blocks(ItemEntity item) {
            if (sourceBlocked) return true;
            Vec3 target = itemPullTarget(item);
            List<FerrousRegion> possible = candidates.computeIfAbsent(
                    item.blockPosition(), this::collectCandidates
            );
            for (Vec3 source : sources) {
                for (FerrousRegion region : possible) {
                    if (region.intersectsSegment(source, target)) return true;
                }
            }
            return false;
        }

        private List<FerrousRegion> collectCandidates(BlockPos targetBlock) {
            LinkedHashSet<FerrousRegion> all = new LinkedHashSet<>();
            FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
            for (Vec3 source : sources) all.addAll(data.collectCandidates(source, targetBlock));
            return List.copyOf(all);
        }

        public Player player() {
            return player;
        }
    }

    private record SourceCheckKey(int x, int y, int z) {
        private static SourceCheckKey point(Vec3 point) {
            return new SourceCheckKey(
                    (int) Math.floor(point.x),
                    (int) Math.floor(point.y),
                    (int) Math.floor(point.z)
            );
        }
    }

    private record MagnetCheckKey(boolean player, long playerMost, long playerLeast,
                                  int sourceX, int sourceY, int sourceZ,
                                  int targetX, int targetY, int targetZ) {
        private static MagnetCheckKey source(Vec3 source, Vec3 target) {
            return new MagnetCheckKey(false, 0L, 0L,
                    bucket(source.x), bucket(source.y), bucket(source.z),
                    bucket(target.x), bucket(target.y), bucket(target.z));
        }

        private static MagnetCheckKey player(Player player, Vec3 target) {
            UUID id = player.getUUID();
            Vec3 source = player.position();
            return new MagnetCheckKey(true, id.getMostSignificantBits(), id.getLeastSignificantBits(),
                    bucket(source.x), bucket(source.y), bucket(source.z),
                    bucket(target.x), bucket(target.y), bucket(target.z));
        }

        private static int bucket(double value) {
            return (int) Math.floor(value * CACHE_SCALE);
        }
    }
}
