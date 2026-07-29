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

    private FerrousMagnetRules() {}

    public static boolean blocksMagnet(ServerLevel level, Vec3 source, Vec3 target) {
        if (sourceBlocked(level, source)) return true;
        MagnetCheckKey key = MagnetCheckKey.source(source, target);
        prepare(level);
        Boolean cached = CHECK_CACHE.get(key);
        if (cached != null) return cached;
        boolean blocked = FerrousRegionSavedData.get(level).blocksMagnet(source, target);
        CHECK_CACHE.put(key, blocked);
        return blocked;
    }

    public static boolean blocksItemPull(ServerLevel level, Vec3 source, ItemEntity item) { return blocksMagnet(level, source, itemPullTarget(item)); }
    public static boolean blocksPlayerItemPull(ServerLevel level, Player player, ItemEntity item) { return blocksPlayerMagnet(level, player, itemPullTarget(item)); }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 target) {
        AABB body = player.getBoundingBox();
        if (sourceBlocked(level, player.position()) || sourceBlocked(level, body.getCenter()) || sourceBlocked(level, player.getEyePosition())) return true;
        MagnetCheckKey key = MagnetCheckKey.player(player, target); prepare(level);
        Boolean cached = CHECK_CACHE.get(key); if (cached != null) return cached;
        boolean blocked = FerrousRegionSavedData.get(level).blocksMagnet(player.position(), target)
                || FerrousRegionSavedData.get(level).blocksMagnet(body.getCenter(), target)
                || FerrousRegionSavedData.get(level).blocksMagnet(player.getEyePosition(), target);
        CHECK_CACHE.put(key, blocked); return blocked;
    }

    public static Vec3 itemPullTarget(ItemEntity item) { return item.position().add(0.0D, item.getBbHeight() * 0.5D, 0.0D); }
    public static MagnetQueryContext sourceContext(ServerLevel level, Vec3 source) { return new MagnetQueryContext(level, new Vec3[]{source}, null); }
    public static MagnetQueryContext playerContext(ServerLevel level, Player player) { AABB body = player.getBoundingBox(); return new MagnetQueryContext(level, new Vec3[]{player.position(), body.getCenter(), player.getEyePosition()}, player); }

    static void invalidateCaches() { CHECK_CACHE.clear(); SOURCE_CACHE.clear(); cacheTick = Long.MIN_VALUE; cacheDimension = ""; }

    private static boolean sourceBlocked(ServerLevel level, Vec3 source) {
        prepare(level); SourceCheckKey key = SourceCheckKey.point(source); Boolean cached = SOURCE_CACHE.get(key);
        if (cached != null) return cached;
        boolean blocked = FerrousRegionSavedData.get(level).containsPoint(source); SOURCE_CACHE.put(key, blocked); return blocked;
    }

    private static void prepare(ServerLevel level) {
        long tick = level.getGameTime(); String dimension = level.dimension().location().toString();
        if (tick != cacheTick || !dimension.equals(cacheDimension)) { CHECK_CACHE.clear(); SOURCE_CACHE.clear(); cacheTick = tick; cacheDimension = dimension; }
    }

    public static final class MagnetQueryContext {
        private final ServerLevel level; private final Vec3[] sources; private final Player player; private final boolean sourceBlocked;
        private final Map<BlockPos, List<FerrousRegion>> candidates = new HashMap<>();
        private MagnetQueryContext(ServerLevel level, Vec3[] sources, Player player) {
            this.level = level; this.sources = sources; this.player = player;
            boolean blocked = false; for (Vec3 source : sources) if (FerrousMagnetRules.sourceBlocked(level, source)) { blocked = true; break; }
            this.sourceBlocked = blocked;
        }
        public boolean blocks(ItemEntity item) {
            if (sourceBlocked) return true; Vec3 target = itemPullTarget(item);
            List<FerrousRegion> possible = candidates.computeIfAbsent(item.blockPosition(), this::collect);
            for (Vec3 source : sources) for (FerrousRegion region : possible) if (region.intersectsSegment(source, target)) return true;
            return false;
        }
        private List<FerrousRegion> collect(BlockPos target) { LinkedHashSet<FerrousRegion> all = new LinkedHashSet<>(); FerrousRegionSavedData data = FerrousRegionSavedData.get(level); for (Vec3 source : sources) all.addAll(data.collectCandidates(source, target)); return List.copyOf(all); }
        public Player player() { return player; }
    }

    private record SourceCheckKey(int x, int y, int z) { static SourceCheckKey point(Vec3 p) { return new SourceCheckKey((int)Math.floor(p.x),(int)Math.floor(p.y),(int)Math.floor(p.z)); } }
    private record MagnetCheckKey(boolean player, long most, long least, int sx, int sy, int sz, int tx, int ty, int tz) {
        static MagnetCheckKey source(Vec3 s, Vec3 t) { return new MagnetCheckKey(false,0,0,b(s.x),b(s.y),b(s.z),b(t.x),b(t.y),b(t.z)); }
        static MagnetCheckKey player(Player p, Vec3 t) { UUID id=p.getUUID(); Vec3 s=p.position(); return new MagnetCheckKey(true,id.getMostSignificantBits(),id.getLeastSignificantBits(),b(s.x),b(s.y),b(s.z),b(t.x),b(t.y),b(t.z)); }
        static int b(double v) { return (int)Math.floor(v*CACHE_SCALE); }
    }
}
