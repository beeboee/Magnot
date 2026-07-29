package com.beeboee.magnot.client;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.util.math.Vec3d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public final class ClientRegionStore {
    private static List<FerrousRegion> regions = Collections.emptyList();

    private ClientRegionStore() {
    }

    public static void set(List<FerrousRegion> syncedRegions) {
        regions = Collections.unmodifiableList(new ArrayList<>(syncedRegions));
    }

    public static void clear() {
        regions = Collections.emptyList();
    }

    public static List<FerrousRegion> all() {
        return regions;
    }

    public static Optional<FerrousRegion> closestIntersecting(Vec3d source, Vec3d target) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;
        for (int index = regions.size() - 1; index >= 0; index--) {
            FerrousRegion region = regions.get(index);
            Optional<Double> distance = region.hitDistanceSqr(source, target);
            if (distance.isPresent() && distance.get() < bestDistance) {
                closest = region;
                bestDistance = distance.get();
            }
        }
        return Optional.ofNullable(closest);
    }
}
