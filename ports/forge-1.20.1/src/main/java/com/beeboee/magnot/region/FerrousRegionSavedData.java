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
import java.util.Collections;
import java.util.List;

public final class FerrousRegionSavedData extends SavedData {
    private static final String DATA_NAME = Magnot.MOD_ID + "_ferrous_regions";
    private final List<FerrousRegion> regions = new ArrayList<>();

    public static FerrousRegionSavedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(FerrousRegionSavedData::load, FerrousRegionSavedData::new, DATA_NAME);
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
        return Collections.unmodifiableList(regions);
    }

    public FerrousRegion addRegion(BlockPos first, BlockPos second) {
        FerrousRegion region = FerrousRegion.fromCorners(first, second);
        regions.add(region);
        setDirty();
        return region;
    }

    public boolean removeRegionContaining(BlockPos pos) {
        boolean removed = regions.removeIf(region -> region.contains(pos));
        if (removed) {
            setDirty();
        }
        return removed;
    }

    public boolean blocksMagnet(Vec3 source, Vec3 target) {
        for (FerrousRegion region : regions) {
            if (region.intersectsSegment(source, target)) {
                return true;
            }
        }
        return false;
    }
}
