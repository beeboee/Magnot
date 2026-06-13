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
        regions.add(region);
        setDirty();
        return region;
    }

    public Optional<FerrousRegion> findClosestIntersecting(Vec3 from, Vec3 to) {
        FerrousRegion closest = null;
        double bestDistance = Double.MAX_VALUE;

        for (int i = regions.size() - 1; i >= 0; i--) {
            FerrousRegion region = regions.get(i);
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
        for (FerrousRegion region : regions) {
            if (!region.id().equals(id) || region.clip(from, to).isEmpty()) {
                continue;
            }

            regions.remove(region);
            setDirty();
            return Optional.of(region);
        }

        return Optional.empty();
    }

    public Optional<FerrousRegion> removeClosestIntersecting(Vec3 from, Vec3 to) {
        Optional<FerrousRegion> closest = findClosestIntersecting(from, to);
        if (closest.isEmpty()) {
            return Optional.empty();
        }

        regions.remove(closest.get());
        setDirty();
        return closest;
    }

    public boolean blocksMagnet(Vec3 source, Vec3 itemPos) {
        for (FerrousRegion region : regions) {
            if (region.contains(source) || region.contains(itemPos) || region.intersectsSegment(source, itemPos)) {
                return true;
            }
        }
        return false;
    }
}
