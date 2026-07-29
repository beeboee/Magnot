package com.beeboee.magnot.region;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;
import java.util.UUID;

public final class FerrousRegion {
    private static final double EPSILON = 1.0E-12D;

    private final UUID id;
    private final UUID groupId;
    private final BlockPos min;
    private final BlockPos max;

    public FerrousRegion(UUID id, UUID groupId, BlockPos min, BlockPos max) {
        this.id = id;
        this.groupId = groupId;
        this.min = min;
        this.max = max;
    }

    public FerrousRegion(UUID id, BlockPos min, BlockPos max) {
        this(id, id, min, max);
    }

    public static FerrousRegion fromCorners(BlockPos first, BlockPos second) {
        UUID id = UUID.randomUUID();
        return fromCorners(id, first, second);
    }

    public static FerrousRegion fromCorners(UUID id, BlockPos first, BlockPos second) {
        BlockPos min = new BlockPos(
                Math.min(first.getX(), second.getX()),
                Math.min(first.getY(), second.getY()),
                Math.min(first.getZ(), second.getZ())
        );
        BlockPos max = new BlockPos(
                Math.max(first.getX(), second.getX()),
                Math.max(first.getY(), second.getY()),
                Math.max(first.getZ(), second.getZ())
        );
        return new FerrousRegion(id, id, min, max);
    }

    public UUID id() {
        return id;
    }

    public UUID groupId() {
        return groupId;
    }

    public BlockPos min() {
        return min;
    }

    public BlockPos max() {
        return max;
    }

    public AxisAlignedBB bounds() {
        return new AxisAlignedBB(
                min.getX(), min.getY(), min.getZ(),
                max.getX() + 1.0D, max.getY() + 1.0D, max.getZ() + 1.0D
        );
    }

    public boolean contains(Vec3d point) {
        return point.x >= min.getX() && point.x <= max.getX() + 1.0D
                && point.y >= min.getY() && point.y <= max.getY() + 1.0D
                && point.z >= min.getZ() && point.z <= max.getZ() + 1.0D;
    }

    public boolean intersects(Vec3d source, Vec3d target) {
        return hitDistanceSqr(source, target).isPresent();
    }

    public Optional<Double> hitDistanceSqr(Vec3d source, Vec3d target) {
        AxisAlignedBB box = bounds();
        double tMin = 0.0D;
        double tMax = 1.0D;

        double[] start = {source.x, source.y, source.z};
        double[] delta = {target.x - source.x, target.y - source.y, target.z - source.z};
        double[] minValues = {box.minX, box.minY, box.minZ};
        double[] maxValues = {box.maxX, box.maxY, box.maxZ};

        for (int axis = 0; axis < 3; axis++) {
            if (Math.abs(delta[axis]) < EPSILON) {
                if (start[axis] < minValues[axis] || start[axis] > maxValues[axis]) {
                    return Optional.empty();
                }
                continue;
            }

            double near = (minValues[axis] - start[axis]) / delta[axis];
            double far = (maxValues[axis] - start[axis]) / delta[axis];
            if (near > far) {
                double swap = near;
                near = far;
                far = swap;
            }
            tMin = Math.max(tMin, near);
            tMax = Math.min(tMax, far);
            if (tMin > tMax) {
                return Optional.empty();
            }
        }

        if (tMax < 0.0D || tMin > 1.0D) {
            return Optional.empty();
        }
        double parameter = Math.max(0.0D, tMin);
        return Optional.of(source.squareDistanceTo(target) * parameter * parameter);
    }

    public NBTTagCompound write() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setUniqueId("Id", id);
        tag.setUniqueId("GroupId", groupId);
        tag.setInteger("MinX", min.getX());
        tag.setInteger("MinY", min.getY());
        tag.setInteger("MinZ", min.getZ());
        tag.setInteger("MaxX", max.getX());
        tag.setInteger("MaxY", max.getY());
        tag.setInteger("MaxZ", max.getZ());
        return tag;
    }

    public static FerrousRegion read(NBTTagCompound tag) {
        UUID id = tag.hasUniqueId("Id") ? tag.getUniqueId("Id") : UUID.randomUUID();
        UUID groupId = tag.hasUniqueId("GroupId") ? tag.getUniqueId("GroupId") : id;
        return new FerrousRegion(
                id,
                groupId,
                new BlockPos(tag.getInteger("MinX"), tag.getInteger("MinY"), tag.getInteger("MinZ")),
                new BlockPos(tag.getInteger("MaxX"), tag.getInteger("MaxY"), tag.getInteger("MaxZ"))
        );
    }
}
