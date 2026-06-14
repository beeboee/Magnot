package com.beeboee.magnot.region;

import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

final class FerrousRegionIndex {
    private static final int SECTION_SIZE_BITS = 4;
    private static final int SECTION_SIZE = 1 << SECTION_SIZE_BITS;
    private static final long COORD_MASK = 0x3FFFFFFL;
    private static final long Y_MASK = 0xFFFL;

    private final Map<UUID, Map<Long, List<FerrousRegion>>> sectionsByOwner = new HashMap<>();

    private FerrousRegionIndex() {
    }

    static FerrousRegionIndex build(List<FerrousRegion> regions) {
        FerrousRegionIndex index = new FerrousRegionIndex();
        for (FerrousRegion region : regions) {
            index.add(region);
        }
        return index;
    }

    boolean containsAnyPoint(Vec3 point) {
        long key = sectionKey(point);
        for (Map<Long, List<FerrousRegion>> sections : sectionsByOwner.values()) {
            if (containsPoint(sections.get(key), point)) {
                return true;
            }
        }
        return false;
    }

    boolean containsWorldPoint(Vec3 point) {
        Map<Long, List<FerrousRegion>> sections = sectionsByOwner.get(null);
        return sections != null && containsPoint(sections.get(sectionKey(point)), point);
    }

    boolean containsSubLevelPoint(UUID subLevelId, Vec3 point) {
        Map<Long, List<FerrousRegion>> sections = sectionsByOwner.get(subLevelId);
        return sections != null && containsPoint(sections.get(sectionKey(point)), point);
    }

    boolean blocksAnyMagnet(Vec3 source, Vec3 target) {
        return blocksMagnet(null, source, target, true);
    }

    boolean blocksWorldMagnet(Vec3 source, Vec3 target) {
        return blocksMagnet(null, source, target, false);
    }

    boolean blocksSubLevelMagnet(UUID subLevelId, Vec3 source, Vec3 target) {
        return blocksMagnet(subLevelId, source, target, false);
    }

    private void add(FerrousRegion region) {
        Map<Long, List<FerrousRegion>> sections = writableSections(region.subLevelId());
        int minSectionX = section(region.min().getX());
        int minSectionY = section(region.min().getY());
        int minSectionZ = section(region.min().getZ());
        int maxSectionX = section(region.max().getX());
        int maxSectionY = section(region.max().getY());
        int maxSectionZ = section(region.max().getZ());

        for (int sectionX = minSectionX; sectionX <= maxSectionX; sectionX++) {
            for (int sectionY = minSectionY; sectionY <= maxSectionY; sectionY++) {
                for (int sectionZ = minSectionZ; sectionZ <= maxSectionZ; sectionZ++) {
                    sections.computeIfAbsent(sectionKey(sectionX, sectionY, sectionZ), key -> new java.util.ArrayList<>()).add(region);
                }
            }
        }
    }

    private boolean blocksMagnet(UUID owner, Vec3 source, Vec3 target, boolean anyOwner) {
        if (sectionsByOwner.isEmpty()) {
            return false;
        }

        int minSectionX = section(Math.min(source.x, target.x));
        int minSectionY = section(Math.min(source.y, target.y));
        int minSectionZ = section(Math.min(source.z, target.z));
        int maxSectionX = section(Math.max(source.x, target.x));
        int maxSectionY = section(Math.max(source.y, target.y));
        int maxSectionZ = section(Math.max(source.z, target.z));

        if (anyOwner) {
            Set<FerrousRegion> visited = null;
            for (Map<Long, List<FerrousRegion>> sections : sectionsByOwner.values()) {
                TestResult result = testSections(sections, visited, source, target, minSectionX, minSectionY, minSectionZ, maxSectionX, maxSectionY, maxSectionZ);
                if (result.blocked()) {
                    return true;
                }
                visited = result.visited();
            }
            return false;
        }

        Map<Long, List<FerrousRegion>> sections = sectionsByOwner.get(owner);
        return sections != null && testSections(sections, null, source, target, minSectionX, minSectionY, minSectionZ, maxSectionX, maxSectionY, maxSectionZ).blocked();
    }

    private static TestResult testSections(Map<Long, List<FerrousRegion>> sections, Set<FerrousRegion> visited, Vec3 source, Vec3 target, int minSectionX, int minSectionY, int minSectionZ, int maxSectionX, int maxSectionY, int maxSectionZ) {
        for (int sectionX = minSectionX; sectionX <= maxSectionX; sectionX++) {
            for (int sectionY = minSectionY; sectionY <= maxSectionY; sectionY++) {
                for (int sectionZ = minSectionZ; sectionZ <= maxSectionZ; sectionZ++) {
                    List<FerrousRegion> regions = sections.get(sectionKey(sectionX, sectionY, sectionZ));
                    if (regions == null) {
                        continue;
                    }

                    for (FerrousRegion region : regions) {
                        if (visited == null) {
                            visited = new HashSet<>();
                        }
                        if (!visited.add(region)) {
                            continue;
                        }
                        if (region.contains(source) || region.contains(target) || region.intersectsSegment(source, target)) {
                            return new TestResult(true, visited);
                        }
                    }
                }
            }
        }
        return new TestResult(false, visited);
    }

    private static boolean containsPoint(List<FerrousRegion> candidates, Vec3 point) {
        if (candidates == null) {
            return false;
        }

        for (FerrousRegion region : candidates) {
            if (region.contains(point)) {
                return true;
            }
        }
        return false;
    }

    private Map<Long, List<FerrousRegion>> writableSections(UUID owner) {
        return sectionsByOwner.computeIfAbsent(owner, key -> new HashMap<>());
    }

    private static long sectionKey(Vec3 point) {
        return sectionKey(section(point.x), section(point.y), section(point.z));
    }

    private static long sectionKey(int sectionX, int sectionY, int sectionZ) {
        return ((long)sectionX & COORD_MASK) << 38
                | ((long)sectionZ & COORD_MASK) << 12
                | ((long)sectionY & Y_MASK);
    }

    private static int section(double coordinate) {
        return section(floor(coordinate));
    }

    private static int section(int blockCoordinate) {
        return Math.floorDiv(blockCoordinate, SECTION_SIZE);
    }

    private static int floor(double value) {
        int integer = (int)value;
        return value < integer ? integer - 1 : integer;
    }

    private record TestResult(boolean blocked, Set<FerrousRegion> visited) {
    }
}
