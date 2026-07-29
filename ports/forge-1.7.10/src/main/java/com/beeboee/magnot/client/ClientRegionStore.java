package com.beeboee.magnot.client;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.util.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class ClientRegionStore {
    private static volatile List<FerrousRegion> regions = Collections.emptyList();

    private ClientRegionStore() {
    }

    public static void set(List<FerrousRegion> syncedRegions) {
        regions = Collections.unmodifiableList(new ArrayList<FerrousRegion>(syncedRegions));
    }

    public static void clear() {
        regions = Collections.emptyList();
    }

    public static List<FerrousRegion> all() {
        return regions;
    }

    public static FerrousRegion closestIntersecting(Vec3 source, Vec3 target) {
        List<FerrousRegion> snapshot = regions;
        FerrousRegion closest = null;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = snapshot.size() - 1; index >= 0; index--) {
            FerrousRegion region = snapshot.get(index);
            double distance = region.hitDistanceSqr(source, target);
            if (distance < bestDistance) {
                closest = region;
                bestDistance = distance;
            }
        }
        return closest;
    }
}
