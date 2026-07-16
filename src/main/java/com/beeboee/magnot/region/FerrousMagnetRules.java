package com.beeboee.magnot.region;

import com.beeboee.magnot.compat.sable.MagnotSableCompat;
import com.beeboee.magnot.debug.MagnotDebug;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** Shared authoritative entry point for magnet integrations. */
public final class FerrousMagnetRules {
    private static final double CACHE_SCALE = 64.0D;
    private static final Map<MagnetCheckKey, Boolean> CHECK_CACHE = new HashMap<>();
    private static final Map<SourceCheckKey, Boolean> SOURCE_CACHE = new HashMap<>();
    private static long cacheTick = Long.MIN_VALUE;
    private static String cacheDimension = "";

    private FerrousMagnetRules() {
    }

    public static boolean blocksMagnet(ServerLevel level, Vec3 magnetSource, Vec3 targetPosition) {
        if (sourceBlocked(level, magnetSource)) return true;
        MagnetCheckKey cacheKey = MagnetCheckKey.source(magnetSource, targetPosition);
        Boolean cached = getCached(level, cacheKey);
        if (cached != null) {
            MagnotDebug.recordCacheHit(level, false);
            return cached;
        }
        boolean blocked = test(level, magnetSource, targetPosition);
        putCached(level, cacheKey, blocked);
        return blocked;
    }

    public static boolean blocksItemPull(ServerLevel level, Vec3 magnetSource, ItemEntity item) {
        return blocksMagnet(level, magnetSource, itemPullTarget(item));
    }

    public static Vec3 itemPullTarget(ItemEntity item) {
        return item.position().add(0.0D, item.getBbHeight() / 2.0D, 0.0D);
    }

    public static boolean blocksPlayerItemPull(ServerLevel level, Player player, ItemEntity item) {
        return blocksPlayerMagnet(level, player, itemPullTarget(item));
    }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 targetPosition) {
        if (playerSourceBlocked(level, player)) return true;
        MagnetCheckKey cacheKey = MagnetCheckKey.player(player, targetPosition);
        Boolean cached = getCached(level, cacheKey);
        if (cached != null) {
            MagnotDebug.recordCacheHit(level, true);
            return cached;
        }
        AABB body = player.getBoundingBox();
        boolean blocked = test(level, player.position(), targetPosition)
                || test(level, body.getCenter(), targetPosition)
                || test(level, player.getEyePosition(), targetPosition);
        putCached(level, cacheKey, blocked);
        return blocked;
    }

    /** Reuses source points and broad-phase candidates for one bulk magnet pass. */
    public static MagnetQueryContext sourceContext(ServerLevel level, Vec3 source) {
        return new MagnetQueryContext(level, new Vec3[]{source}, null);
    }

    /** Reuses feet/body/eye points and broad-phase candidates for one player magnet pass. */
    public static MagnetQueryContext playerContext(ServerLevel level, Player player) {
        AABB body = player.getBoundingBox();
        return new MagnetQueryContext(level, new Vec3[]{player.position(), body.getCenter(), player.getEyePosition()}, player);
    }

    private static boolean test(ServerLevel level, Vec3 source, Vec3 target) {
        boolean blocked;
        if (ModList.get().isLoaded("sable")) {
            blocked = MagnotSableCompat.blocksMagnet(level, source, target);
            MagnotDebug.recordFallbackCheck(level, "sable", blocked);
        } else {
            blocked = FerrousRegionSavedData.get(level).blocksMagnet(source, target);
            MagnotDebug.recordFallbackCheck(level, "saved-data", blocked);
        }
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
        if (cached != null) return cached;
        boolean blocked = ModList.get().isLoaded("sable")
                ? MagnotSableCompat.containsPoint(level, source)
                : FerrousRegionSavedData.get(level).containsPoint(source);
        putCached(level, key, blocked);
        return blocked;
    }

    static void invalidateCaches() {
        CHECK_CACHE.clear();
        SOURCE_CACHE.clear();
        cacheTick = Long.MIN_VALUE;
        cacheDimension = "";
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
            invalidateCaches();
            cacheTick = gameTime;
            cacheDimension = dimension;
        }
    }

    public static final class MagnetQueryContext {
        private final ServerLevel level;
        private final Vec3[] sources;
        private final Player player;
        private final boolean sable;
        private final boolean sourceBlocked;
        private final Map<BlockPos, List<FerrousRegion>> candidatesByTargetBlock = new HashMap<>();
        private List<FerrousRegion> preparedCandidates = List.of();
        private boolean prepared;

        private MagnetQueryContext(ServerLevel level, Vec3[] sources, Player player) {
            this.level = level;
            this.sources = sources;
            this.player = player;
            this.sable = ModList.get().isLoaded("sable");
            boolean blocked = false;
            for (Vec3 source : sources) {
                if (FerrousMagnetRules.sourceBlocked(level, source)) {
                    blocked = true;
                    break;
                }
            }
            this.sourceBlocked = blocked;
        }

        /**
         * Prepares one conservative candidate set for every potential item target in the
         * magnet's search bounds. Repeated calls extend the prepared area when a mod performs
         * more than one entity query during the same pass.
         */
        public void prepare(AABB targetBounds) {
            if (sourceBlocked || sable) return;

            List<FerrousRegion> candidates = FerrousRegionSavedData.get(level)
                    .collectAnyCandidates(sources, targetBounds);
            if (!prepared) {
                preparedCandidates = candidates;
                prepared = true;
                candidatesByTargetBlock.clear();
                return;
            }
            if (candidates.isEmpty()) return;

            LinkedHashSet<FerrousRegion> merged = new LinkedHashSet<>(preparedCandidates);
            merged.addAll(candidates);
            preparedCandidates = List.copyOf(merged);
        }

        /** True when a prepared pass cannot possibly cross a Magnot world region. */
        public boolean isUnrestricted() {
            return !sourceBlocked && !sable && prepared && preparedCandidates.isEmpty();
        }

        public boolean blocks(ItemEntity item) {
            if (sourceBlocked) return true;
            if (isUnrestricted()) return false;

            Vec3 target = itemPullTarget(item);
            if (sable) {
                for (Vec3 source : sources) {
                    if (MagnotSableCompat.blocksMagnet(level, source, target)) return true;
                }
                return false;
            }

            List<FerrousRegion> candidates = prepared
                    ? preparedCandidates
                    : candidatesByTargetBlock.computeIfAbsent(item.blockPosition(), this::collectCandidates);
            for (Vec3 source : sources) {
                for (FerrousRegion region : candidates) {
                    if (region.intersectsSegment(source, target)) return true;
                }
            }
            return false;
        }

        private List<FerrousRegion> collectCandidates(BlockPos targetBlock) {
            FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
            LinkedHashSet<FerrousRegion> candidates = new LinkedHashSet<>();
            for (Vec3 source : sources) {
                candidates.addAll(data.collectAnyCandidates(source, targetBlock));
            }
            return new ArrayList<>(candidates);
        }

        public Player player() {
            return player;
        }
    }

    private record SourceCheckKey(int sourceX, int sourceY, int sourceZ) {
        private static SourceCheckKey point(Vec3 source) {
            return new SourceCheckKey(block(source.x), block(source.y), block(source.z));
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
            UUID uuid = player.getUUID();
            Vec3 source = player.position();
            return new MagnetCheckKey(true, uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(),
                    bucket(source.x), bucket(source.y), bucket(source.z),
                    bucket(target.x), bucket(target.y), bucket(target.z));
        }

        private static int bucket(double value) {
            return (int) Math.floor(value * CACHE_SCALE);
        }
    }

    private static int block(double value) {
        return (int) Math.floor(value);
    }
}
