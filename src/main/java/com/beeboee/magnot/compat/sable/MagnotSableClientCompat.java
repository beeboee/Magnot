package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.region.FerrousRegion;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

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
        AABB pathBounds = new AABB(from, to).inflate(PATH_BOUNDS_PADDING);

        for (SubLevelAccess subLevel : SableCompanion.INSTANCE.getAllIntersecting(level, new BoundingBox3d(pathBounds))) {
            Vec3 localFrom = subLevel.logicalPose().transformPositionInverse(from);
            Vec3 localTo = subLevel.logicalPose().transformPositionInverse(to);

            for (FerrousRegion region : ClientFerrousRegionStore.regions()) {
                var localHit = region.clip(localFrom, localTo);
                if (localHit.isEmpty()) {
                    continue;
                }

                Vec3 worldHit = subLevel.logicalPose().transformPosition(localHit.get());
                double distance = worldHit.distanceToSqr(from);
                if (distance >= bestDistance) {
                    continue;
                }

                closest = region;
                bestDistance = distance;
            }
        }

        return Optional.ofNullable(closest);
    }

    public static AABB displayBounds(Level level, FerrousRegion region) {
        Vec3 center = region.bounds().getCenter();
        SubLevelAccess subLevel = SableCompanion.INSTANCE.getContaining(level, center);
        if (subLevel == null) {
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
}
