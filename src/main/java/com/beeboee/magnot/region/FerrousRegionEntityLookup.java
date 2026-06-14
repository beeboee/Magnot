package com.beeboee.magnot.region;

import com.beeboee.magnot.debug.MagnotDebug;
import com.beeboee.magnot.entity.FerrousRegionEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class FerrousRegionEntityLookup {
    private static final double SEGMENT_SEARCH_PADDING = 1.0E-3D;
    private static final double POINT_SEARCH_PADDING = 1.0E-3D;
    private static final Map<CandidateKey, List<FerrousRegionEntity>> CANDIDATE_CACHE = new HashMap<>();
    private static long cacheTick = Long.MIN_VALUE;
    private static String cacheDimension = "";

    private FerrousRegionEntityLookup() {
    }

    public static boolean containsPoint(ServerLevel level, Vec3 point) {
        List<FerrousRegionEntity> candidates = level.getEntitiesOfClass(FerrousRegionEntity.class, new AABB(point, point).inflate(POINT_SEARCH_PADDING));
        for (FerrousRegionEntity region : candidates) {
            if (region.contains(point)) {
                return true;
            }
        }
        return false;
    }

    public static boolean blocksMagnet(ServerLevel level, Vec3 source, Vec3 targetPosition) {
        List<FerrousRegionEntity> candidates = candidateRegions(level, CandidateKey.source(source, targetPosition));
        for (FerrousRegionEntity region : candidates) {
            if (region.blocksMagnet(source, targetPosition)) {
                MagnotDebug.recordEntityCheck(level, false, candidates.size(), true);
                return true;
            }
        }

        MagnotDebug.recordEntityCheck(level, false, candidates.size(), false);
        return false;
    }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 targetPosition) {
        AABB body = player.getBoundingBox();
        Vec3 feet = player.position();
        Vec3 bodyCenter = body.getCenter();
        Vec3 eye = player.getEyePosition();

        List<FerrousRegionEntity> candidates = candidateRegions(level, CandidateKey.player(player, targetPosition));
        for (FerrousRegionEntity region : candidates) {
            if (region.blocksMagnet(feet, targetPosition)
                    || region.blocksMagnet(bodyCenter, targetPosition)
                    || region.blocksMagnet(eye, targetPosition)) {
                MagnotDebug.recordEntityCheck(level, true, candidates.size(), true);
                return true;
            }
        }

        MagnotDebug.recordEntityCheck(level, true, candidates.size(), false);
        return false;
    }

    private static List<FerrousRegionEntity> candidateRegions(ServerLevel level, CandidateKey key) {
        prepareCache(level);
        List<FerrousRegionEntity> cached = CANDIDATE_CACHE.get(key);
        if (cached != null) {
            return cached;
        }

        List<FerrousRegionEntity> candidates = level.getEntitiesOfClass(FerrousRegionEntity.class, key.searchBounds());
        CANDIDATE_CACHE.put(key, candidates);
        return candidates;
    }

    private static void prepareCache(ServerLevel level) {
        long gameTime = level.getGameTime();
        String dimension = level.dimension().location().toString();
        if (gameTime != cacheTick || !dimension.equals(cacheDimension)) {
            CANDIDATE_CACHE.clear();
            cacheTick = gameTime;
            cacheDimension = dimension;
        }
    }

    private record CandidateKey(
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
        private static CandidateKey source(Vec3 source, Vec3 target) {
            return new CandidateKey(
                    false,
                    0L,
                    0L,
                    block(source.x),
                    block(source.y),
                    block(source.z),
                    block(target.x),
                    block(target.y),
                    block(target.z)
            );
        }

        private static CandidateKey player(Player player, Vec3 target) {
            UUID uuid = player.getUUID();
            Vec3 source = player.position();
            return new CandidateKey(
                    true,
                    uuid.getMostSignificantBits(),
                    uuid.getLeastSignificantBits(),
                    block(source.x),
                    block(source.y),
                    block(source.z),
                    block(target.x),
                    block(target.y),
                    block(target.z)
            );
        }

        private AABB searchBounds() {
            double minX = Math.min(sourceX, targetX);
            double minY = Math.min(sourceY, targetY);
            double minZ = Math.min(sourceZ, targetZ);
            double maxX = Math.max(sourceX, targetX) + 1.0D;
            double maxY = Math.max(sourceY, targetY) + (player ? 3.0D : 1.0D);
            double maxZ = Math.max(sourceZ, targetZ) + 1.0D;
            return new AABB(minX, minY, minZ, maxX, maxY, maxZ).inflate(SEGMENT_SEARCH_PADDING);
        }

        private static int block(double value) {
            return (int) Math.floor(value);
        }
    }
}
