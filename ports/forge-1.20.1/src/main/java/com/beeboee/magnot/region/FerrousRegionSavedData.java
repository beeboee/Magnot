package com.beeboee.magnot.region;

import com.beeboee.magnot.Magnot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public final class FerrousRegionSavedData extends SavedData {
    private static final String DATA_NAME = Magnot.MOD_ID + "_ferrous_regions";
    private final List<FerrousRegion> regions = new ArrayList<>();
    private transient FerrousRegionIndex index;

    public static FerrousRegionSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                FerrousRegionSavedData::load,
                FerrousRegionSavedData::new,
                DATA_NAME
        );
    }

    public static FerrousRegionSavedData load(CompoundTag tag) {
        FerrousRegionSavedData data = new FerrousRegionSavedData();
        ListTag list = tag.getList("Regions", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            data.regions.add(FerrousRegion.load(list.getCompound(i)));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (FerrousRegion region : regions) {
            list.add(region.save());
        }
        tag.put("Regions", list);
        return tag;
    }

    public List<FerrousRegion> regions() {
        return List.copyOf(regions);
    }

    public Optional<FerrousRegion> findById(UUID id) {
        return regions.stream().filter(region -> region.id().equals(id)).findFirst();
    }

    public void addRegion(FerrousRegion region) {
        removeRegion(region.id());
        regions.add(region);
        changed();
    }

    public FerrousRegion addRegion(BlockPos first, BlockPos second) {
        FerrousRegion region = FerrousRegion.fromCorners(first, second);
        addRegion(region);
        return region;
    }

    public boolean removeRegion(UUID id) {
        boolean removed = regions.removeIf(region -> region.id().equals(id));
        if (removed) changed();
        return removed;
    }

    public boolean removeGroup(UUID groupId) {
        boolean removed = regions.removeIf(region -> region.groupId().equals(groupId));
        if (removed) changed();
        return removed;
    }

    public Optional<FerrousRegion> closestIntersecting(Vec3 from, Vec3 to) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;
        for (int i = regions.size() - 1; i >= 0; i--) {
            FerrousRegion region = regions.get(i);
            Optional<Double> hit = region.hitDistanceSqr(from, to);
            if (hit.isPresent() && hit.get() < bestDistance) {
                closest = region;
                bestDistance = hit.get();
            }
        }
        return Optional.ofNullable(closest);
    }

    public Optional<FerrousRegion> removeIntersectingById(UUID id, Vec3 from, Vec3 to) {
        for (int i = 0; i < regions.size(); i++) {
            FerrousRegion region = regions.get(i);
            if (!region.id().equals(id) || !region.intersectsSegment(from, to)) continue;
            regions.remove(i);
            changed();
            return Optional.of(region);
        }
        return Optional.empty();
    }

    public boolean containsPoint(Vec3 point) {
        return index().containsPoint(point);
    }

    public boolean blocksMagnet(Vec3 source, Vec3 target) {
        return index().blocksMagnet(source, target);
    }

    List<FerrousRegion> collectCandidates(Vec3 source, BlockPos targetBlock) {
        return index().collectCandidates(source, targetBlock);
    }

    private FerrousRegionIndex index() {
        if (index == null) index = FerrousRegionIndex.build(regions);
        return index;
    }

    private void changed() {
        index = null;
        FerrousMagnetRules.invalidateCaches();
        setDirty();
    }
}
