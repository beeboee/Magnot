package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FerrousRegionTest {
    @Test
    void normalizesCornersAndPreservesInclusiveBlocks() {
        UUID id = UUID.randomUUID();
        FerrousRegion region = FerrousRegion.fromCorners(id, new BlockPos(4, 5, 6), new BlockPos(1, 2, 3));

        assertEquals(new BlockPos(1, 2, 3), region.min());
        assertEquals(new BlockPos(4, 5, 6), region.max());
        assertTrue(region.contains(new Vec3(1.0, 2.0, 3.0)));
        assertTrue(region.contains(new Vec3(5.0, 6.0, 7.0)));
        assertFalse(region.contains(new Vec3(5.0001, 6.0, 7.0)));
    }

    @Test
    void segmentIntersectionHandlesFacesEdgesCornersAndMisses() {
        FerrousRegion region = FerrousRegion.fromCorners(new BlockPos(0, 0, 0), new BlockPos(1, 1, 1));

        assertTrue(region.intersectsSegment(new Vec3(-1, 1, 1), new Vec3(3, 1, 1)), "face crossing");
        assertTrue(region.intersectsSegment(new Vec3(-1, -1, 0), new Vec3(0, 0, 0)), "corner touch");
        assertTrue(region.intersectsSegment(new Vec3(0.5, 0.5, 0.5), new Vec3(20, 20, 20)), "source inside");
        assertFalse(region.intersectsSegment(new Vec3(-1, 3, -1), new Vec3(3, 3, 3)), "parallel miss");
    }

    @Test
    void hitDistanceUsesFirstIntersection() {
        FerrousRegion region = FerrousRegion.fromCorners(new BlockPos(2, 0, 0), new BlockPos(2, 0, 0));
        double distance = region.hitDistanceSqr(new Vec3(0, 0.5, 0.5), new Vec3(10, 0.5, 0.5)).orElseThrow();
        assertEquals(4.0D, distance, 1.0E-9D);
    }

    @Test
    void batchCandidateQueryKeepsRelevantRegionsAndDropsDistantOnes() {
        FerrousRegion relevant = FerrousRegion.fromCorners(new BlockPos(4, 0, 0), new BlockPos(4, 2, 2));
        FerrousRegion distant = FerrousRegion.fromCorners(new BlockPos(100, 0, 100), new BlockPos(101, 2, 101));
        FerrousRegionIndex index = FerrousRegionIndex.build(List.of(relevant, distant));

        List<FerrousRegion> candidates = index.collectAnyCandidates(
                new Vec3[]{new Vec3(0.5D, 1.0D, 1.0D)},
                new AABB(8.0D, 0.0D, 0.0D, 12.0D, 3.0D, 3.0D)
        );

        assertTrue(candidates.contains(relevant));
        assertFalse(candidates.contains(distant));
    }

    @Test
    void saveLoadPreservesIdentityGroupingAndSublevel() {
        UUID id = UUID.randomUUID();
        UUID group = UUID.randomUUID();
        UUID sublevel = UUID.randomUUID();
        FerrousRegion original = FerrousRegion.fromCorners(id, group, new BlockPos(-2, 3, 4), new BlockPos(5, 9, 11), sublevel);

        assertEquals(original, FerrousRegion.load(original.save()));
    }
}
