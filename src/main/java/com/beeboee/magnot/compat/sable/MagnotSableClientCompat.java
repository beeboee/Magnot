package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.region.FerrousRegion;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MagnotSableClientCompat {
    private static final double PATH_BOUNDS_PADDING = 0.5D;

    private MagnotSableClientCompat() {}

    public static boolean blocksMagnet(Level level, Vec3 source, Vec3 itemPosition) {
        return closestIntersecting(level, source, itemPosition).isPresent();
    }

    public static Optional<FerrousRegion> closestIntersecting(Level level, Vec3 from, Vec3 to) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;
        Vec3 globalFrom = SableCompanion.INSTANCE.projectOutOfSubLevel(level, from);
        Vec3 globalTo = SableCompanion.INSTANCE.projectOutOfSubLevel(level, to);

        for (SubLevelAccess subLevel : candidateSubLevels(level, from, to, globalFrom, globalTo)) {
            Vec3 localFrom = subLevel.logicalPose().transformPositionInverse(globalFrom);
            Vec3 localTo = subLevel.logicalPose().transformPositionInverse(globalTo);

            Optional<FerrousRegion> region = ClientFerrousRegionStore.closestSubLevelIntersecting(subLevel.getUniqueId(), localFrom, localTo);
            if (region.isEmpty()) {
                continue;
            }

            var localHit = region.get().clip(localFrom, localTo);
            if (localHit.isEmpty()) {
                continue;
            }

            Vec3 worldHit = subLevel.logicalPose().transformPosition(localHit.get());
            double distance = worldHit.distanceToSqr(globalFrom);
            if (distance >= bestDistance) {
                continue;
            }

            closest = region.get();
            bestDistance = distance;
        }

        return Optional.ofNullable(closest);
    }

    public static AABB displayBounds(Level level, FerrousRegion region) {
        if (region.isWorldRegion()) {
            return region.bounds();
        }

        Vec3 center = region.bounds().getCenter();
        SubLevelAccess subLevel = SableCompanion.INSTANCE.getContaining(level, center);
        if (subLevel == null || !region.belongsToSubLevel(subLevel.getUniqueId())) {
            return region.bounds();
        }

        AABB bounds = region.bounds();
        AABB transformed = null;

        for (double x : new double[]{bounds.minX, bounds.maxX}) {
            for (double y : new double[]{bounds.minY, bounds.maxY}) {
                for (double z : new double[]{bounds.minZ, bounds.maxZ}) {
                    Vec3 corner = subLevel.logicalPose().transformPosition(new Vec3(x, y, z));
                    AABB cornerBox = new AABB(corner, corner);
                    transformed = transformed == null ? cornerBox : transformed.minmax(cornerBox);
                }
            }
        }

        return transformed == null ? bounds : transformed;
    }

    private static List<SubLevelAccess> candidateSubLevels(Level level, Vec3 from, Vec3 to, Vec3 globalFrom, Vec3 globalTo) {
        List<SubLevelAccess> candidates = new ArrayList<>();
        addCandidate(candidates, SableCompanion.INSTANCE.getContaining(level, from));
        addCandidate(candidates, SableCompanion.INSTANCE.getContaining(level, to));

        AABB pathBounds = new AABB(globalFrom, globalTo).inflate(PATH_BOUNDS_PADDING);
        for (SubLevelAccess subLevel : SableCompanion.INSTANCE.getAllIntersecting(level, new BoundingBox3d(pathBounds))) {
            addCandidate(candidates, subLevel);
        }

        return candidates;
    }

    private static void addCandidate(List<SubLevelAccess> candidates, SubLevelAccess candidate) {
        if (candidate == null) {
            return;
        }

        for (SubLevelAccess existing : candidates) {
            if (existing.getUniqueId().equals(candidate.getUniqueId())) {
                return;
            }
        }

        candidates.add(candidate);
    }
}
