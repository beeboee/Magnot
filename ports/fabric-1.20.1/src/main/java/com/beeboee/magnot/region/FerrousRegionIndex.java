package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class FerrousRegionIndex {
    private static final int SECTION_SIZE = 16;
    private static final long COORD_MASK = 0x3FFFFFFL;
    private static final long Y_MASK = 0xFFFL;
    private final Map<Long, List<FerrousRegion>> sections = new HashMap<>();

    static FerrousRegionIndex build(List<FerrousRegion> regions) {
        FerrousRegionIndex index = new FerrousRegionIndex();
        for (FerrousRegion region : regions) index.add(region);
        return index;
    }

    boolean containsPoint(Vec3 point) {
        List<FerrousRegion> candidates = sections.get(key(point));
        if (candidates == null) return false;
        for (FerrousRegion region : candidates) if (region.contains(point)) return true;
        return false;
    }

    boolean blocksMagnet(Vec3 source, Vec3 target) {
        Set<FerrousRegion> visited = new HashSet<>();
        int minX = section(Math.min(source.x, target.x)); int maxX = section(Math.max(source.x, target.x));
        int minY = section(Math.min(source.y, target.y)); int maxY = section(Math.max(source.y, target.y));
        int minZ = section(Math.min(source.z, target.z)); int maxZ = section(Math.max(source.z, target.z));
        for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) for (int z = minZ; z <= maxZ; z++) {
            List<FerrousRegion> candidates = sections.get(key(x, y, z));
            if (candidates == null) continue;
            for (FerrousRegion region : candidates) if (visited.add(region) && region.intersectsSegment(source, target)) return true;
        }
        return false;
    }

    List<FerrousRegion> collectCandidates(Vec3 source, BlockPos target) {
        LinkedHashSet<FerrousRegion> result = new LinkedHashSet<>();
        int minX = section(Math.min(source.x, target.getX())); int maxX = section(Math.max(source.x, target.getX() + 1.0D));
        int minY = section(Math.min(source.y, target.getY())); int maxY = section(Math.max(source.y, target.getY() + 1.0D));
        int minZ = section(Math.min(source.z, target.getZ())); int maxZ = section(Math.max(source.z, target.getZ() + 1.0D));
        for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) for (int z = minZ; z <= maxZ; z++) {
            List<FerrousRegion> candidates = sections.get(key(x, y, z));
            if (candidates != null) result.addAll(candidates);
        }
        return new ArrayList<>(result);
    }

    private void add(FerrousRegion region) {
        for (int x = section(region.min().getX()); x <= section(region.max().getX()); x++)
            for (int y = section(region.min().getY()); y <= section(region.max().getY()); y++)
                for (int z = section(region.min().getZ()); z <= section(region.max().getZ()); z++)
                    sections.computeIfAbsent(key(x, y, z), ignored -> new ArrayList<>()).add(region);
    }

    private static long key(Vec3 point) { return key(section(point.x), section(point.y), section(point.z)); }
    private static long key(int x, int y, int z) { return ((long) x & COORD_MASK) << 38 | ((long) z & COORD_MASK) << 12 | ((long) y & Y_MASK); }
    private static int section(double value) { return Math.floorDiv((int) Math.floor(value), SECTION_SIZE); }
    private static int section(int value) { return Math.floorDiv(value, SECTION_SIZE); }
}
