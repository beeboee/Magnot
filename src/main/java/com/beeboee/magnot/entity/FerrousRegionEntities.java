package com.beeboee.magnot.entity;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public final class FerrousRegionEntities {
    private static final double SEARCH_PADDING = 64.0D;

    private FerrousRegionEntities() {
    }

    public static void spawn(ServerLevel level, FerrousRegion region) {
        discard(level, region.id(), region.bounds());
        FerrousRegionEntity.spawn(level, region);
    }

    public static void replace(ServerLevel level, FerrousRegion oldRegion, FerrousRegion newRegion) {
        discard(level, oldRegion.id(), oldRegion.bounds().minmax(newRegion.bounds()));
        FerrousRegionEntity.spawn(level, newRegion);
    }

    public static void discard(ServerLevel level, FerrousRegion region) {
        discard(level, region.id(), region.bounds());
    }

    public static void discard(ServerLevel level, UUID regionId, AABB searchBounds) {
        AABB padded = searchBounds.inflate(SEARCH_PADDING);
        for (FerrousRegionEntity entity : level.getEntitiesOfClass(FerrousRegionEntity.class, padded, entity -> entity.regionId().equals(regionId))) {
            entity.discard();
        }
    }
}
