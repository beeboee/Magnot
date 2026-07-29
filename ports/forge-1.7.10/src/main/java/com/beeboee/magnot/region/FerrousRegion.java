package com.beeboee.magnot.region;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

import java.util.UUID;

public final class FerrousRegion {
    private static final double EPSILON = 1.0E-12D;

    private final UUID id;
    private final UUID groupId;
    private final int minX;
    private final int minY;
    private final int minZ;
    private final int maxX;
    private final int maxY;
    private final int maxZ;

    public FerrousRegion(UUID id, UUID groupId, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this.id = id;
        this.groupId = groupId;
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public FerrousRegion(UUID id, int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
        this(id, id, minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static FerrousRegion fromCorners(int firstX, int firstY, int firstZ, int secondX, int secondY, int secondZ) {
        return fromCorners(UUID.randomUUID(), firstX, firstY, firstZ, secondX, secondY, secondZ);
    }

    public static FerrousRegion fromCorners(
            UUID id,
            int firstX,
            int firstY,
            int firstZ,
            int secondX,
            int secondY,
            int secondZ
    ) {
        return new FerrousRegion(
                id,
                id,
                Math.min(firstX, secondX),
                Math.min(firstY, secondY),
                Math.min(firstZ, secondZ),
                Math.max(firstX, secondX),
                Math.max(firstY, secondY),
                Math.max(firstZ, secondZ)
        );
    }

    public UUID getId() {
        return id;
    }

    public UUID getGroupId() {
        return groupId;
    }

    public AxisAlignedBB bounds() {
        return AxisAlignedBB.getBoundingBox(
                minX,
                minY,
                minZ,
                maxX + 1.0D,
                maxY + 1.0D,
                maxZ + 1.0D
        );
    }

    public boolean contains(Vec3 point) {
        return point.xCoord >= minX && point.xCoord <= maxX + 1.0D
                && point.yCoord >= minY && point.yCoord <= maxY + 1.0D
                && point.zCoord >= minZ && point.zCoord <= maxZ + 1.0D;
    }

    public boolean intersects(Vec3 source, Vec3 target) {
        return hitDistanceSqr(source, target) != Double.POSITIVE_INFINITY;
    }

    public double hitDistanceSqr(Vec3 source, Vec3 target) {
        double tMin = 0.0D;
        double tMax = 1.0D;
        double[] start = {source.xCoord, source.yCoord, source.zCoord};
        double[] delta = {
                target.xCoord - source.xCoord,
                target.yCoord - source.yCoord,
                target.zCoord - source.zCoord
        };
        double[] lower = {minX, minY, minZ};
        double[] upper = {maxX + 1.0D, maxY + 1.0D, maxZ + 1.0D};

        for (int axis = 0; axis < 3; axis++) {
            if (Math.abs(delta[axis]) < EPSILON) {
                if (start[axis] < lower[axis] || start[axis] > upper[axis]) {
                    return Double.POSITIVE_INFINITY;
                }
                continue;
            }

            double near = (lower[axis] - start[axis]) / delta[axis];
            double far = (upper[axis] - start[axis]) / delta[axis];
            if (near > far) {
                double swap = near;
                near = far;
                far = swap;
            }
            tMin = Math.max(tMin, near);
            tMax = Math.min(tMax, far);
            if (tMin > tMax) {
                return Double.POSITIVE_INFINITY;
            }
        }

        if (tMax < 0.0D || tMin > 1.0D) {
            return Double.POSITIVE_INFINITY;
        }
        double parameter = Math.max(0.0D, tMin);
        return source.squareDistanceTo(target) * parameter * parameter;
    }

    public NBTTagCompound write() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setLong("IdMost", id.getMostSignificantBits());
        tag.setLong("IdLeast", id.getLeastSignificantBits());
        tag.setLong("GroupMost", groupId.getMostSignificantBits());
        tag.setLong("GroupLeast", groupId.getLeastSignificantBits());
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
        UUID groupId = tag.hasKey("GroupMost") && tag.hasKey("GroupLeast")
                ? new UUID(tag.getLong("GroupMost"), tag.getLong("GroupLeast"))
                : id;
        return new FerrousRegion(
                id,
                groupId,
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
