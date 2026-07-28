package com.beeboee.magnot.region;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.util.UUID;

public final class FerrousRegion {
    private final UUID id;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public FerrousRegion(UUID id, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.id = id;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public static FerrousRegion fromCorners(int firstX, int firstY, int firstZ, int secondX, int secondY, int secondZ) {
        return new FerrousRegion(
                UUID.randomUUID(),
                Math.min(firstX, secondX),
                Math.min(firstY, secondY),
                Math.min(firstZ, secondZ),
                Math.max(firstX, secondX),
                Math.max(firstY, secondY),
                Math.max(firstZ, secondZ)
        );
    }

    public AxisAlignedBB bounds() {
        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX + 1.0D, maxY + 1.0D, maxZ + 1.0D);
    }

    public boolean contains(int x, int y, int z) {
        return x >= minX && x <= maxX
                && y >= minY && y <= maxY
                && z >= minZ && z <= maxZ;
    }

    public boolean intersects(Vec3 source, Vec3 target) {
        AxisAlignedBB box = bounds();
        return box.isVecInside(source) || box.isVecInside(target) || box.calculateIntercept(source, target) != null;
    }

    public NBTTagCompound write() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("IdMost", id.getMostSignificantBits());
        tag.setLong("IdLeast", id.getLeastSignificantBits());
        tag.setInteger("MinX", minX);
        tag.setInteger("MinY", minY);
        tag.setInteger("MinZ", minZ);
        tag.setInteger("MaxX", maxX);
        tag.setInteger("MaxY", maxY);
        tag.setInteger("MaxZ", maxZ);
        return tag;
    }

    public static FerrousRegion read(NBTTagCompound tag) {
        UUID id = tag.hasKey("IdMost") && tag.hasKey("IdLeast")
                ? new UUID(tag.getLong("IdMost"), tag.getLong("IdLeast"))
                : UUID.randomUUID();
        return new FerrousRegion(
                id,
                tag.getInteger("MinX"),
                tag.getInteger("MinY"),
                tag.getInteger("MinZ"),
                tag.getInteger("MaxX"),
                tag.getInteger("MaxY"),
                tag.getInteger("MaxZ")
        );
    }

    public int getMinX() { return minX; }
    public int getMinY() { return minY; }
    public int getMinZ() { return minZ; }
    public int getMaxX() { return maxX; }
    public int getMaxY() { return maxY; }
    public int getMaxZ() { return maxZ; }
}
