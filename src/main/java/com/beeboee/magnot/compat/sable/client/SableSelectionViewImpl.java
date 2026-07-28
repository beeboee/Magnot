package com.beeboee.magnot.compat.sable.client;

import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.client.selection.FerrousSelectionView;
import com.beeboee.magnot.client.selection.SableSelectionView;
import com.beeboee.magnot.region.FerrousRegion;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class SableSelectionViewImpl implements SableSelectionView {
    @Override
    public Optional<SelectionHit> closestIntersecting(Level level, Vec3 from, Vec3 to) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;
        Vec3 globalFrom = SableCompanion.INSTANCE.projectOutOfSubLevel(level, from);
        Vec3 globalTo = SableCompanion.INSTANCE.projectOutOfSubLevel(level, to);

        for (SubLevelAccess subLevel : candidateSubLevels(level, from, to)) {
            Vec3 localFrom = subLevel.logicalPose().transformPositionInverse(globalFrom);
            Vec3 localTo = subLevel.logicalPose().transformPositionInverse(globalTo);
            Optional<FerrousRegion> region = ClientFerrousRegionStore.closestSubLevelIntersecting(
                    subLevel.getUniqueId(), localFrom, localTo
            );
            if (region.isEmpty()) {
                continue;
            }

            Optional<Vec3> localHit = region.get().clip(localFrom, localTo);
            if (localHit.isEmpty()) {
                continue;
            }

            double distance = subLevel.logicalPose().transformPosition(localHit.get()).distanceToSqr(globalFrom);
            if (distance < bestDistance) {
                closest = region.get();
                bestDistance = distance;
            }
        }

        return closest == null ? Optional.empty() : Optional.of(new SelectionHit(closest, bestDistance));
    }

    @Override
    public FerrousSelectionView view(Level level, FerrousRegion region) {
        if (region.isWorldRegion()) {
            return FerrousSelectionView.axisAligned(region.bounds());
        }

        SubLevelAccess subLevel = findSubLevel(level, region.subLevelId());
        if (subLevel == null) {
            return FerrousSelectionView.axisAligned(region.bounds());
        }

        Vec3[] localCorners = FerrousSelectionView.corners(region.bounds());
        Vec3[] worldCorners = new Vec3[localCorners.length];
        AABB displayBounds = null;
        for (int i = 0; i < localCorners.length; i++) {
            Vec3 transformed = subLevel.logicalPose().transformPosition(localCorners[i]);
            worldCorners[i] = transformed;
            AABB point = new AABB(transformed, transformed);
            displayBounds = displayBounds == null ? point : displayBounds.minmax(point);
        }
        return new FerrousSelectionView(displayBounds == null ? region.bounds() : displayBounds, worldCorners);
    }

    private static SubLevelAccess findSubLevel(Level level, UUID id) {
        if (id == null) {
            return null;
        }
        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container == null) {
            return null;
        }
        for (SubLevelAccess subLevel : container.getAllSubLevels()) {
            if (id.equals(subLevel.getUniqueId())) {
                return subLevel;
            }
        }
        return null;
    }

    private static List<SubLevelAccess> candidateSubLevels(Level level, Vec3 from, Vec3 to) {
        LinkedHashMap<UUID, SubLevelAccess> candidates = new LinkedHashMap<>();
        add(candidates, SableCompanion.INSTANCE.getContaining(level, from));
        add(candidates, SableCompanion.INSTANCE.getContaining(level, to));
        SubLevelContainer container = SubLevelContainer.getContainer(level);
        if (container != null) {
            for (SubLevelAccess subLevel : container.getAllSubLevels()) {
                add(candidates, subLevel);
            }
        }
        return new ArrayList<>(candidates.values());
    }

    private static void add(LinkedHashMap<UUID, SubLevelAccess> candidates, SubLevelAccess subLevel) {
        if (subLevel != null) {
            candidates.putIfAbsent(subLevel.getUniqueId(), subLevel);
        }
    }
}
