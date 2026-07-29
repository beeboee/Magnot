package com.beeboee.magnot.client;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class ClientFerrousRegionStore {
    private static List<FerrousRegion> regions = List.of();

    private ClientFerrousRegionStore() {
    }

    public static void setRegions(List<FerrousRegion> synced) {
        regions = List.copyOf(synced);
    }

    public static void clear() {
        regions = List.of();
    }

    public static List<FerrousRegion> regions() {
        return regions;
    }

    public static Optional<FerrousRegion> byId(UUID id) {
        return regions.stream().filter(region -> region.id().equals(id)).findFirst();
    }

    public static Optional<FerrousRegion> closestIntersecting(Vec3 from, Vec3 to) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;
        for (int i = regions.size() - 1; i >= 0; i--) {
            FerrousRegion region = regions.get(i);
            Optional<Double> hit = region.hitDistanceSqr(from, to);
            if (hit.isPresent() && hit.get() < bestDistance) {
                closest = region;
                bestDistance = hit.get();
            }
        }
        return Optional.ofNullable(closest);
    }
}
