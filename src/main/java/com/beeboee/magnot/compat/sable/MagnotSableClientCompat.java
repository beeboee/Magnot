package com.beeboee.magnot.compat.sable;

import com.beeboee.magnot.client.ClientFerrousRegionStore;
import com.beeboee.magnot.region.FerrousMagnetRules;
import com.beeboee.magnot.region.FerrousRegion;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.SableCompanion;
import dev.ryanhcode.sable.companion.SubLevelAccess;
import dev.ryanhcode.sable.neoforge.mixinhelper.compatibility.create.renderers.AABBOutlineRenderingOptions;
import net.createmod.catnip.outliner.Outliner;
import net.createmod.catnip.render.BindableTexture;
import net.minecraft.world.entity.item.ItemEntity;
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

    public static boolean blocksItemPull(Level level, Vec3 source, ItemEntity item) {
        Vec3 itemTarget = FerrousMagnetRules.itemPullTarget(item);
        Vec3 globalFrom = SableCompanion.INSTANCE.projectOutOfSubLevel(level, source);
        Vec3 globalTo = SableCompanion.INSTANCE.projectOutOfSubLevel(level, itemTarget);

        for (SubLevelAccess subLevel : candidateSubLevels(level, source, itemTarget)) {
            Vec3 localFrom = subLevel.logicalPose().transformPositionInverse(globalFrom);
            Vec3 localTo = subLevel.logicalPose().transformPositionInverse(globalTo);
            for (FerrousRegion region : ClientFerrousRegionStore.regions()) {
                if (region.belongsToSubLevel(subLevel.getUniqueId()) && region.blocksItemPull(level, localFrom, localTo, item.getItem())) {
                    return true;
                }
            }
        }

        return false;
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

    public static boolean showRegionOutline(Level level, Object slot, FerrousRegion region, int color, BindableTexture faceTexture, float lineWidth) {
        if (region.isWorldRegion() || findSubLevel(level, region) == null) {
            disableOutlineTransform(slot);
            return false;
        }

        Outliner outliner = Outliner.getInstance();
        var params = outliner.showAABB(slot, region.bounds())
                .colored(color)
                .withFaceTextures(faceTexture, faceTexture)
                .disableLineNormals()
                .lineWidth(lineWidth);

        var entry = outliner.getOutlines().get(slot);
        if (entry != null && entry.getOutline() instanceof AABBOutlineRenderingOptions options) {
            options.sable$shouldTransform(true);
        }

        return true;
    }

    public static void disableOutlineTransform(Object slot) {
        var entry = Outliner.getInstance().getOutlines().get(slot);
        if (entry != null && entry.getOutline() instanceof AABBOutlineRenderingOptions options) {
            options.sable$shouldTransform(false);
        }
    }

    public static AABB displayBounds(Level level, FerrousRegion region) {
        if (region.isWorldRegion()) {
            return region.bounds();
        }

        SubLevelAccess subLevel = findSubLevel(level, region);
        if (subLevel == null) {
            return region.bounds();
        }

        return transformBounds(subLevel, region.bounds());
    }

    private static AABB transformBounds(SubLevelAccess subLevel, AABB bounds) {
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
