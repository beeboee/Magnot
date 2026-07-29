package com.beeboee.magnot.region;

import com.beeboee.magnot.Magnot;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public final class FerrousRegionSavedData extends WorldSavedData {
    private static final String DATA_NAME = Magnot.MOD_ID + "_ferrous_regions";
    private static final int SECTION_SIZE = 16;

    private final List<FerrousRegion> regions = new ArrayList<>();
    private transient Map<Long, List<FerrousRegion>> sectionIndex;

    public FerrousRegionSavedData() {
        super(DATA_NAME);
    }

    public FerrousRegionSavedData(String name) {
        super(name);
    }

    public static FerrousRegionSavedData get(WorldServer world) {
        MapStorage storage = world.getPerWorldStorage();
        FerrousRegionSavedData data =
                (FerrousRegionSavedData) storage.getOrLoadData(FerrousRegionSavedData.class, DATA_NAME);
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
        sectionIndex = null;
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

    public void addRegion(FerrousRegion region) {
        removeRegion(region.id());
        regions.add(region);
        changed();
    }

    public boolean removeRegion(UUID id) {
        boolean removed = regions.removeIf(region -> region.id().equals(id));
        if (removed) {
            changed();
        }
        return removed;
    }

    public Optional<FerrousRegion> removeIntersectingById(UUID id, Vec3d source, Vec3d target) {
        for (int index = 0; index < regions.size(); index++) {
            FerrousRegion region = regions.get(index);
            if (region.id().equals(id) && region.intersects(source, target)) {
                regions.remove(index);
                changed();
                return Optional.of(region);
            }
        }
        return Optional.empty();
    }

    public boolean containsPoint(Vec3d point) {
        for (FerrousRegion region : candidates(point, point)) {
            if (region.contains(point)) {
                return true;
            }
        }
        return false;
    }

    public boolean blocksMagnet(Vec3d source, Vec3d target) {
        for (FerrousRegion region : candidates(source, target)) {
            if (region.intersects(source, target)) {
                return true;
            }
        }
        return false;
    }

    private Collection<FerrousRegion> candidates(Vec3d source, Vec3d target) {
        ensureIndex();
        Set<FerrousRegion> result = new LinkedHashSet<>();
        int minX = section(Math.min(source.x, target.x));
        int minY = section(Math.min(source.y, target.y));
        int minZ = section(Math.min(source.z, target.z));
        int maxX = section(Math.max(source.x, target.x));
        int maxY = section(Math.max(source.y, target.y));
        int maxZ = section(Math.max(source.z, target.z));

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
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
        sectionIndex = new HashMap<>();
        for (FerrousRegion region : regions) {
            for (int x = section(region.min().getX()); x <= section(region.max().getX()); x++) {
                for (int y = section(region.min().getY()); y <= section(region.max().getY()); y++) {
                    for (int z = section(region.min().getZ()); z <= section(region.max().getZ()); z++) {
                        sectionIndex
                                .computeIfAbsent(sectionKey(x, y, z), ignored -> new ArrayList<>())
                                .add(region);
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
        return Math.floorDiv((int) Math.floor(coordinate), SECTION_SIZE);
    }

    private static long sectionKey(int x, int y, int z) {
        return ((long) x & 0x3FFFFFFL) << 38
                | ((long) z & 0x3FFFFFFL) << 12
                | ((long) y & 0xFFFL);
    }
}
