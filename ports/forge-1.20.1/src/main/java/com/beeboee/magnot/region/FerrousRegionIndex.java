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
    private static final int SECTION_BITS = 4;
    private static final int SECTION_SIZE = 1 << SECTION_BITS;
    private static final long COORD_MASK = 0x3FFFFFFL;
    private static final long Y_MASK = 0xFFFL;

    private final Map<Long, List<FerrousRegion>> sections = new HashMap<>();

    private FerrousRegionIndex() {
    }

    static FerrousRegionIndex build(List<FerrousRegion> regions) {
        FerrousRegionIndex index = new FerrousRegionIndex();
        for (FerrousRegion region : regions) {
            index.add(region);
        }
        return index;
    }

    boolean containsPoint(Vec3 point) {
        List<FerrousRegion> candidates = sections.get(sectionKey(point));
        if (candidates == null) return false;
        for (FerrousRegion region : candidates) {
            if (region.contains(point)) return true;
        }
        return false;
    }

    boolean blocksMagnet(Vec3 source, Vec3 target) {
        Set<FerrousRegion> visited = new HashSet<>();
        int minX = section(Math.min(source.x, target.x));
        int minY = section(Math.min(source.y, target.y));
        int minZ = section(Math.min(source.z, target.z));
        int maxX = section(Math.max(source.x, target.x));
        int maxY = section(Math.max(source.y, target.y));
        int maxZ = section(Math.max(source.z, target.z));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    List<FerrousRegion> candidates = sections.get(sectionKey(x, y, z));
                    if (candidates == null) continue;
                    for (FerrousRegion region : candidates) {
                        if (visited.add(region) && region.intersectsSegment(source, target)) return true;
                    }
                }
            }
        }
        return false;
    }

    List<FerrousRegion> collectCandidates(Vec3 source, BlockPos targetBlock) {
        LinkedHashSet<FerrousRegion> found = new LinkedHashSet<>();
        int minX = section(Math.min(source.x, targetBlock.getX()));
        int minY = section(Math.min(source.y, targetBlock.getY()));
        int minZ = section(Math.min(source.z, targetBlock.getZ()));
        int maxX = section(Math.max(source.x, targetBlock.getX() + 1.0D));
        int maxY = section(Math.max(source.y, targetBlock.getY() + 1.0D));
        int maxZ = section(Math.max(source.z, targetBlock.getZ() + 1.0D));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    List<FerrousRegion> candidates = sections.get(sectionKey(x, y, z));
                    if (candidates != null) found.addAll(candidates);
                }
            }
        }
        return new ArrayList<>(found);
    }

    private void add(FerrousRegion region) {
        int minX = section(region.min().getX());
        int minY = section(region.min().getY());
        int minZ = section(region.min().getZ());
        int maxX = section(region.max().getX());
        int maxY = section(region.max().getY());
        int maxZ = section(region.max().getZ());

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    sections.computeIfAbsent(sectionKey(x, y, z), ignored -> new ArrayList<>()).add(region);
                }
            }
        }
    }

    private static long sectionKey(Vec3 point) {
        return sectionKey(section(point.x), section(point.y), section(point.z));
    }

    private static long sectionKey(int x, int y, int z) {
        return ((long) x & COORD_MASK) << 38 | ((long) z & COORD_MASK) << 12 | ((long) y & Y_MASK);
    }

    private static int section(double value) {
        return Math.floorDiv((int) Math.floor(value), SECTION_SIZE);
    }

    private static int section(int value) {
        return Math.floorDiv(value, SECTION_SIZE);
    }
}
