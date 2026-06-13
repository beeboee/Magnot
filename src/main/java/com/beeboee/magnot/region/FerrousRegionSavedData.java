package com.beeboee.magnot.region;

import com.beeboee.magnot.Magnot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
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
import java.util.function.Predicate;

public class FerrousRegionSavedData extends SavedData {
    private static final String DATA_NAME = Magnot.MOD_ID + "_ferrous_regions";
    private static final SavedData.Factory<FerrousRegionSavedData> FACTORY = new SavedData.Factory<>(
            FerrousRegionSavedData::new,
            FerrousRegionSavedData::load
    );

    private final List<FerrousRegion> regions = new ArrayList<>();

    public static FerrousRegionSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FACTORY, DATA_NAME);
    }

    public static FerrousRegionSavedData load(CompoundTag tag, HolderLookup.Provider registries) {
        FerrousRegionSavedData data = new FerrousRegionSavedData();
        ListTag list = tag.getList("Regions", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            data.regions.add(FerrousRegion.load(list.getCompound(i)));
        }
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag, HolderLookup.Provider registries) {
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

    public FerrousRegion addRegion(BlockPos first, BlockPos second) {
        FerrousRegion region = FerrousRegion.fromCorners(first, second);
        addRegion(region);
        return region;
    }

    public void addRegion(FerrousRegion region) {
        removeRegion(region.id());
        regions.add(region);
        setDirty();
    }

    public boolean removeRegion(UUID id) {
        boolean removed = regions.removeIf(region -> region.id().equals(id));
        if (removed) {
            setDirty();
        }
        return removed;
    }

    public boolean removeGroup(UUID groupId) {
        boolean removed = regions.removeIf(region -> region.groupId().equals(groupId));
        if (removed) {
            setDirty();
        }
        return removed;
    }

    public Optional<FerrousRegion> findClosestIntersecting(Vec3 from, Vec3 to) {
        return findClosestIntersecting(from, to, region -> true);
    }

    public Optional<FerrousRegion> findClosestWorldIntersecting(Vec3 from, Vec3 to) {
        return findClosestIntersecting(from, to, FerrousRegion::isWorldRegion);
    }

    public Optional<FerrousRegion> findClosestSubLevelIntersecting(UUID subLevelId, Vec3 from, Vec3 to) {
        return findClosestIntersecting(from, to, region -> region.belongsToSubLevel(subLevelId));
    }

    private Optional<FerrousRegion> findClosestIntersecting(Vec3 from, Vec3 to, Predicate<FerrousRegion> predicate) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;

        for (int i = regions.size() - 1; i >= 0; i--) {
            FerrousRegion region = regions.get(i);
            if (!predicate.test(region)) {
                continue;
            }

            var hit = region.clip(from, to);
            if (hit.isEmpty()) {
                continue;
            }

            double distance = hit.get().distanceToSqr(from);
            if (distance >= bestDistance) {
                continue;
            }

            closest = region;
            bestDistance = distance;
        }

        return Optional.ofNullable(closest);
    }

    public Optional<FerrousRegion> removeIntersectingById(UUID id, Vec3 from, Vec3 to) {
        return removeIntersectingById(id, from, to, region -> true);
    }

    public Optional<FerrousRegion> removeWorldIntersectingById(UUID id, Vec3 from, Vec3 to) {
        return removeIntersectingById(id, from, to, FerrousRegion::isWorldRegion);
    }

    public Optional<FerrousRegion> removeSubLevelIntersectingById(UUID id, UUID subLevelId, Vec3 from, Vec3 to) {
        return removeIntersectingById(id, from, to, region -> region.belongsToSubLevel(subLevelId));
    }

    private Optional<FerrousRegion> removeIntersectingById(UUID id, Vec3 from, Vec3 to, Predicate<FerrousRegion> predicate) {
        for (int i = 0; i < regions.size(); i++) {
            FerrousRegion region = regions.get(i);
            if (!region.id().equals(id) || !predicate.test(region) || region.clip(from, to).isEmpty()) {
                continue;
            }

            FerrousRegion selected = region;
            removeGroup(region.groupId());
            return Optional.of(selected);
        }

        return Optional.empty();
    }

    public Optional<FerrousRegion> removeClosestIntersecting(Vec3 from, Vec3 to) {
        Optional<FerrousRegion> closest = findClosestIntersecting(from, to);
        if (closest.isEmpty()) {
            return Optional.empty();
        }

        FerrousRegion selected = closest.get();
        removeGroup(selected.groupId());
        return Optional.of(selected);
    }

    public boolean blocksMagnet(Vec3 source, Vec3 itemPos) {
        return blocksMagnet(source, itemPos, region -> true);
    }

    public boolean blocksWorldMagnet(Vec3 source, Vec3 itemPos) {
        return blocksMagnet(source, itemPos, FerrousRegion::isWorldRegion);
    }

    public boolean blocksSubLevelMagnet(UUID subLevelId, Vec3 source, Vec3 itemPos) {
        return blocksMagnet(source, itemPos, region -> region.belongsToSubLevel(subLevelId));
    }

    private boolean blocksMagnet(Vec3 source, Vec3 itemPos, Predicate<FerrousRegion> predicate) {
        for (FerrousRegion region : regions) {
            if (!predicate.test(region)) {
                continue;
            }

            if (region.contains(source) || region.contains(itemPos) || region.intersectsSegment(source, itemPos)) {
                return true;
            }
        }
        return false;
    }
}
