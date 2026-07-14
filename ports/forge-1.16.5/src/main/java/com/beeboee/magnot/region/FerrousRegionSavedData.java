package com.beeboee.magnot.region;

import com.beeboee.magnot.Magnot;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FerrousRegionSavedData extends WorldSavedData {
    private static final String DATA_NAME = Magnot.MOD_ID + "_ferrous_regions";
    private final List<FerrousRegion> regions = new ArrayList<>();

    public FerrousRegionSavedData() { super(DATA_NAME); }

    public static FerrousRegionSavedData get(ServerWorld level) {
        return level.getDataStorage().computeIfAbsent(FerrousRegionSavedData::new, DATA_NAME);
    }

    @Override
    public void load(CompoundNBT tag) {
        regions.clear();
        ListNBT list = tag.getList("Regions", 10);
        for (int i = 0; i < list.size(); i++) regions.add(FerrousRegion.load(list.getCompound(i)));
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        ListNBT list = new ListNBT();
        for (FerrousRegion region : regions) list.add(region.save());
        tag.put("Regions", list);
        return tag;
    }

    public List<FerrousRegion> regions() { return Collections.unmodifiableList(regions); }
    public FerrousRegion addRegion(BlockPos first, BlockPos second) {
        FerrousRegion region = FerrousRegion.fromCorners(first, second);
        regions.add(region); setDirty(); return region;
    }
    public boolean removeContaining(BlockPos pos) {
        boolean removed = regions.removeIf(region -> region.contains(pos));
        if (removed) setDirty();
        return removed;
    }
    public boolean blocksMagnet(Vector3d source, Vector3d target) {
        for (FerrousRegion region : regions) if (region.intersects(source, target)) return true;
        return false;
    }
}
