package com.beeboee.magnot.region;

import com.beeboee.magnot.entity.FerrousRegionEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public final class FerrousRegionEntityLookup {
    private static final double POINT_SEARCH_PADDING = 1.0E-4D;
    private static final double SEGMENT_SEARCH_PADDING = 1.0E-3D;

    private FerrousRegionEntityLookup() {
    }

    public static boolean blocksMagnet(ServerLevel level, Vec3 source, Vec3 targetPosition) {
        if (isInsideFerrousRegion(level, targetPosition) || isInsideFerrousRegion(level, source)) {
            return true;
        }

        for (FerrousRegionEntity region : candidateRegions(level, source, targetPosition)) {
            if (region.blocksMagnet(source, targetPosition)) {
                return true;
            }
        }

        return false;
    }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 targetPosition) {
        if (isInsideFerrousRegion(level, targetPosition)) {
            return true;
        }

        AABB body = player.getBoundingBox();
        Vec3 feet = player.position();
        Vec3 bodyCenter = body.getCenter();
        Vec3 eye = player.getEyePosition();

        if (isInsideFerrousRegion(level, feet)
                || isInsideFerrousRegion(level, bodyCenter)
                || isInsideFerrousRegion(level, eye)) {
            return true;
        }

        AABB searchBounds = segmentSearchBounds(feet, targetPosition)
                .minmax(segmentSearchBounds(bodyCenter, targetPosition))
                .minmax(segmentSearchBounds(eye, targetPosition));

        List<FerrousRegionEntity> candidates = level.getEntitiesOfClass(FerrousRegionEntity.class, searchBounds);
        for (FerrousRegionEntity region : candidates) {
            if (region.blocksMagnet(feet, targetPosition)
                    || region.blocksMagnet(bodyCenter, targetPosition)
                    || region.blocksMagnet(eye, targetPosition)) {
                return true;
            }
        }

        return false;
    }

    public static boolean isInsideFerrousRegion(ServerLevel level, Vec3 position) {
        AABB searchBounds = new AABB(position, position).inflate(POINT_SEARCH_PADDING);
        return !level.getEntitiesOfClass(FerrousRegionEntity.class, searchBounds, region -> region.contains(position)).isEmpty();
    }

    private static List<FerrousRegionEntity> candidateRegions(ServerLevel level, Vec3 source, Vec3 targetPosition) {
        return level.getEntitiesOfClass(FerrousRegionEntity.class, segmentSearchBounds(source, targetPosition));
    }

    private static AABB segmentSearchBounds(Vec3 source, Vec3 targetPosition) {
        return new AABB(source, targetPosition).inflate(SEGMENT_SEARCH_PADDING);
    }
}
