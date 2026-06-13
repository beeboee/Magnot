package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.region.FerrousRegion;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class MagnotSableClientCompat {
    private MagnotSableClientCompat() {}

    public static boolean blocksMagnet(Level level, Vec3 source, Vec3 itemPosition) {
        return closestIntersecting(level, source, itemPosition).isPresent();
    }

    public static Optional<FerrousRegion> closestIntersecting(Level level, Vec3 from, Vec3 to) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;
        Vec3 globalFrom = SableCompanion.INSTANCE.projectOutOfSubLevel(level, from);
        Vec3 globalTo = SableCompanion.INSTANCE.projectOutOfSubLevel(level, to);

        for (SubLevelAccess subLevel : candidateSubLevels(level, from, to)) {
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

    public static List<AABB> displayBoxes(Level level, FerrousRegion region) {
        if (region.isWorldRegion()) {
            return List.of(region.bounds());
        }

        SubLevelAccess subLevel = findSubLevel(level, region);
        if (subLevel == null) {
            return List.of(region.bounds());
        }

        List<AABB> boxes = new ArrayList<>();
        for (BlockPos localPos : BlockPos.betweenClosed(region.min(), region.max())) {
            if (!isSurfaceBlock(region, localPos)) {
                continue;
            }

            Vec3 worldCenter = subLevel.logicalPose().transformPosition(Vec3.atCenterOf(localPos));
            boxes.add(new AABB(
                    worldCenter.x - 0.5D,
                    worldCenter.y - 0.5D,
                    worldCenter.z - 0.5D,
                    worldCenter.x + 0.5D,
                    worldCenter.y + 0.5D,
                    worldCenter.z + 0.5D
            ));
        }

        return boxes.isEmpty() ? List.of(region.bounds()) : boxes;
    }

    public static AABB displayBounds(Level level, FerrousRegion region) {
        AABB combined = null;
        for (AABB box : displayBoxes(level, region)) {
            combined = combined == null ? box : combined.minmax(box);
        }
        return combined == null ? region.bounds() : combined;
    }

    private static boolean isSurfaceBlock(FerrousRegion region, BlockPos pos) {
        return pos.getX() == region.min().getX()
                || pos.getX() == region.max().getX()
                || pos.getY() == region.min().getY()
                || pos.getY() == region.max().getY()
                || pos.getZ() == region.min().getZ()
                || pos.getZ() == region.max().getZ();
    }

    private static SubLevelAccess findSubLevel(Level level, FerrousRegion region) {
        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return null;
        }

        for (SubLevelAccess subLevel : container.getAllSubLevels()) {
            if (region.belongsToSubLevel(subLevel.getUniqueId())) {
                return subLevel;
            }
        }

        return null;
    }

    private static List<SubLevelAccess> candidateSubLevels(Level level, Vec3 from, Vec3 to) {
        List<SubLevelAccess> candidates = new ArrayList<>();
        addCandidate(candidates, SableCompanion.INSTANCE.getContaining(level, from));
        addCandidate(candidates, SableCompanion.INSTANCE.getContaining(level, to));

        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container != null) {
            for (SubLevelAccess subLevel : container.getAllSubLevels()) {
                addCandidate(candidates, subLevel);
            }
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
