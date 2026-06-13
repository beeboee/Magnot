package com.beeboee.magnot.client;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public final class ClientFerrousRegionStore {
    private static List<FerrousRegion> regions = List.of();

    private ClientFerrousRegionStore() {
    }

    public static void setRegions(List<FerrousRegion> syncedRegions) {
        regions = List.copyOf(syncedRegions);
    }

    public static List<FerrousRegion> regions() {
        return regions;
    }

    public static Optional<FerrousRegion> closestIntersecting(Vec3 from, Vec3 to) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;

        for (int i = regions.size() - 1; i >= 0; i--) {
            FerrousRegion region = regions.get(i);
            var hitDistance = region.hitDistanceSqr(from, to);
            if (hitDistance.isEmpty()) {
                continue;
            }

            double distance = hitDistance.get();
            if (distance > bestDistance) {
                continue;
            }

            closest = region;
            bestDistance = distance;
        }

        return Optional.ofNullable(closest);
    }
}
