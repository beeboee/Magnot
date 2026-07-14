package com.beeboee.magnot.region;

import com.beeboee.magnot.Magnot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class FerrousRegionSavedData extends WorldSavedData {
    private static final String DATA_NAME = Magnot.MOD_ID + "_ferrous_regions";
    private final List<FerrousRegion> regions = new ArrayList<>();

    public FerrousRegionSavedData() {
        super(DATA_NAME);
    }

    public FerrousRegionSavedData(String name) {
        super(name);
    }

    public static FerrousRegionSavedData get(WorldServer world) {
        MapStorage storage = world.getPerWorldStorage();
        FerrousRegionSavedData data = (FerrousRegionSavedData) storage.getOrLoadData(FerrousRegionSavedData.class, DATA_NAME);
        if (data == null) {
            data = new FerrousRegionSavedData(DATA_NAME);
            storage.setData(DATA_NAME, data);
        }
        return data;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        regions.clear();
        NBTTagList list = tag.getTagList("Regions", 10);
        for (int i = 0; i < list.tagCount(); i++) {
            regions.add(FerrousRegion.read(list.getCompoundTagAt(i)));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for (FerrousRegion region : regions) {
            list.appendTag(region.write());
        }
        tag.setTag("Regions", list);
        return tag;
    }

    public List<FerrousRegion> regions() {
        return Collections.unmodifiableList(regions);
    }

    public void addRegion(BlockPos first, BlockPos second) {
        regions.add(FerrousRegion.fromCorners(first, second));
        markDirty();
    }

    public boolean removeContaining(BlockPos pos) {
        boolean removed = regions.removeIf(region -> region.contains(pos));
        if (removed) {
            markDirty();
        }
        return removed;
    }

    public boolean blocksMagnet(Vec3d source, Vec3d target) {
        for (FerrousRegion region : regions) {
            if (region.intersects(source, target)) {
                return true;
            }
        }
        return false;
    }
}
