package com.beeboee.magnot.region;

import com.beeboee.magnot.Magnot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public final class FerrousRegionSavedData extends WorldSavedData {
    private static final String DATA_NAME = Magnot.MOD_ID + "_ferrous_regions";
    private final List<FerrousRegion> regions = new ArrayList<FerrousRegion>();

    public FerrousRegionSavedData() {
        super(DATA_NAME);
    }

    public FerrousRegionSavedData(String name) {
        super(name);
    }

    public static FerrousRegionSavedData get(WorldServer world) {
        MapStorage storage = world.perWorldStorage;
        FerrousRegionSavedData data = (FerrousRegionSavedData) storage.loadData(FerrousRegionSavedData.class, DATA_NAME);
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
    public void writeToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for (FerrousRegion region : regions) {
            list.appendTag(region.write());
        }
        tag.setTag("Regions", list);
    }

    public List<FerrousRegion> regions() {
        return Collections.unmodifiableList(regions);
    }

    public FerrousRegion addRegion(int firstX, int firstY, int firstZ, int secondX, int secondY, int secondZ) {
        FerrousRegion region = FerrousRegion.fromCorners(firstX, firstY, firstZ, secondX, secondY, secondZ);
        regions.add(region);
        markDirty();
        return region;
    }

    public boolean removeContaining(int x, int y, int z) {
        boolean removed = false;
        Iterator<FerrousRegion> iterator = regions.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().contains(x, y, z)) {
                iterator.remove();
                removed = true;
            }
        }
        if (removed) {
            markDirty();
        }
        return removed;
    }

    public boolean blocksMagnet(Vec3 source, Vec3 target) {
        for (FerrousRegion region : regions) {
            if (region.intersects(source, target)) {
                return true;
            }
        }
        return false;
    }
}
