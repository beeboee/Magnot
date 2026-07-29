package com.beeboee.magnot.region;

import com.beeboee.magnot.Magnot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class FerrousRegionSavedData extends WorldSavedData {
    private static final String DATA_NAME = Magnot.MOD_ID + "_ferrous_regions";
    private static final int SECTION_SIZE = 16;

    private final List<FerrousRegion> regions = new ArrayList<FerrousRegion>();
    private transient Map<Long, List<FerrousRegion>> sectionIndex;

    public FerrousRegionSavedData() {
        super(DATA_NAME);
    }

    public FerrousRegionSavedData(String name) {
        super(name);
    }

    public static FerrousRegionSavedData get(WorldServer world) {
        MapStorage storage = world.perWorldStorage;
        FerrousRegionSavedData data =
                (FerrousRegionSavedData) storage.loadData(FerrousRegionSavedData.class, DATA_NAME);
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
        for (int index = 0; index < list.tagCount(); index++) {
            regions.add(FerrousRegion.read(list.getCompoundTagAt(index)));
        }
        sectionIndex = null;
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

    public void addRegion(FerrousRegion region) {
        removeRegion(region.getId());
        regions.add(region);
        changed();
    }

    public boolean removeRegion(UUID id) {
        boolean removed = false;
        Iterator<FerrousRegion> iterator = regions.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getId().equals(id)) {
                iterator.remove();
                removed = true;
            }
        }
        if (removed) {
            changed();
        }
        return removed;
    }

    public FerrousRegion removeIntersectingById(UUID id, Vec3 source, Vec3 target) {
        Iterator<FerrousRegion> iterator = regions.iterator();
        while (iterator.hasNext()) {
            FerrousRegion region = iterator.next();
            if (region.getId().equals(id) && region.intersects(source, target)) {
                iterator.remove();
                changed();
                return region;
            }
        }
        return null;
    }

    public boolean containsPoint(Vec3 point) {
        for (FerrousRegion region : candidates(point, point)) {
            if (region.contains(point)) {
                return true;
            }
        }
        return false;
    }

    public boolean blocksMagnet(Vec3 source, Vec3 target) {
        for (FerrousRegion region : candidates(source, target)) {
            if (region.intersects(source, target)) {
                return true;
            }
        }
        return false;
    }

    private Collection<FerrousRegion> candidates(Vec3 source, Vec3 target) {
        ensureIndex();
        Set<FerrousRegion> result = new LinkedHashSet<FerrousRegion>();
        int minSectionX = section(Math.min(source.xCoord, target.xCoord));
        int minSectionY = section(Math.min(source.yCoord, target.yCoord));
        int minSectionZ = section(Math.min(source.zCoord, target.zCoord));
        int maxSectionX = section(Math.max(source.xCoord, target.xCoord));
        int maxSectionY = section(Math.max(source.yCoord, target.yCoord));
        int maxSectionZ = section(Math.max(source.zCoord, target.zCoord));

        for (int x = minSectionX; x <= maxSectionX; x++) {
            for (int y = minSectionY; y <= maxSectionY; y++) {
                for (int z = minSectionZ; z <= maxSectionZ; z++) {
                    List<FerrousRegion> candidates = sectionIndex.get(sectionKey(x, y, z));
                    if (candidates != null) {
                        result.addAll(candidates);
                    }
                }
            }
        }
        return result;
    }

    private void ensureIndex() {
        if (sectionIndex != null) {
            return;
        }
        sectionIndex = new HashMap<Long, List<FerrousRegion>>();
        for (FerrousRegion region : regions) {
            for (int x = section(region.getMinX()); x <= section(region.getMaxX()); x++) {
                for (int y = section(region.getMinY()); y <= section(region.getMaxY()); y++) {
                    for (int z = section(region.getMinZ()); z <= section(region.getMaxZ()); z++) {
                        Long key = Long.valueOf(sectionKey(x, y, z));
                        List<FerrousRegion> bucket = sectionIndex.get(key);
                        if (bucket == null) {
                            bucket = new ArrayList<FerrousRegion>();
                            sectionIndex.put(key, bucket);
                        }
                        bucket.add(region);
                    }
                }
            }
        }
    }

    private void changed() {
        sectionIndex = null;
        FerrousMagnetRules.invalidateCaches();
        markDirty();
    }

    private static int section(double coordinate) {
        int block = (int) Math.floor(coordinate);
        int result = block / SECTION_SIZE;
        if (block < 0 && block % SECTION_SIZE != 0) {
            result--;
        }
        return result;
    }

    private static long sectionKey(int x, int y, int z) {
        return ((long) x & 0x3FFFFFFL) << 38
                | ((long) z & 0x3FFFFFFL) << 12
                | ((long) y & 0xFFFL);
    }
}
