package com.beeboee.magnot.client;

import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Optional;

public final class ClientFerrousRegionStore {
    private static List<FerrousRegion> regions=List.of();
    private ClientFerrousRegionStore() {}
    public static void setRegions(List<FerrousRegion> value){regions=List.copyOf(value);} public static void clear(){regions=List.of();} public static List<FerrousRegion> regions(){return regions;}
    public static Optional<FerrousRegion> closestIntersecting(Vec3 from,Vec3 to){FerrousRegion closest=null;double best=Double.MAX_VALUE;for(int i=regions.size()-1;i>=0;i--){FerrousRegion region=regions.get(i);Optional<Double> hit=region.hitDistanceSqr(from,to);if(hit.isPresent()&&hit.get()<best){closest=region;best=hit.get();}}return Optional.ofNullable(closest);}
}
