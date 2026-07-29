package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

public record FerrousRegion(UUID id, UUID groupId, BlockPos min, BlockPos max) {
    private static final double EPSILON = 1.0E-12D;

    public FerrousRegion(UUID id, BlockPos min, BlockPos max) {
        this(id, id, min, max);
    }

    public static FerrousRegion fromCorners(BlockPos first, BlockPos second) {
        return fromCorners(UUID.randomUUID(), first, second);
    }

    public static FerrousRegion fromCorners(UUID id, BlockPos first, BlockPos second) {
        return fromCorners(id, id, first, second);
    }

    public static FerrousRegion fromCorners(UUID id, UUID groupId, BlockPos first, BlockPos second) {
        return new FerrousRegion(id, groupId,
                new BlockPos(Math.min(first.getX(), second.getX()), Math.min(first.getY(), second.getY()), Math.min(first.getZ(), second.getZ())),
                new BlockPos(Math.max(first.getX(), second.getX()), Math.max(first.getY(), second.getY()), Math.max(first.getZ(), second.getZ())));
    }

    public AABB bounds() {
        return new AABB(min.getX(), min.getY(), min.getZ(), max.getX() + 1.0D, max.getY() + 1.0D, max.getZ() + 1.0D);
    }

    public boolean contains(Vec3 pos) {
        AABB box = bounds();
        return pos.x >= box.minX && pos.x <= box.maxX && pos.y >= box.minY && pos.y <= box.maxY && pos.z >= box.minZ && pos.z <= box.maxZ;
    }

    public boolean intersectsBlock(BlockPos pos) {
        AABB box = bounds();
        return pos.getX() + 1.0D > box.minX && pos.getX() < box.maxX
                && pos.getY() + 1.0D > box.minY && pos.getY() < box.maxY
                && pos.getZ() + 1.0D > box.minZ && pos.getZ() < box.maxZ;
    }

    public Optional<Vec3> clip(Vec3 from, Vec3 to) {
        OptionalDouble parameter = clipParameter(from, to);
        if (parameter.isEmpty()) return Optional.empty();
        double t = parameter.getAsDouble();
        return Optional.of(new Vec3(from.x + (to.x - from.x) * t, from.y + (to.y - from.y) * t, from.z + (to.z - from.z) * t));
    }

    public Optional<Double> hitDistanceSqr(Vec3 from, Vec3 to) {
        OptionalDouble parameter = clipParameter(from, to);
        if (parameter.isEmpty()) return Optional.empty();
        double t = parameter.getAsDouble();
        return Optional.of(from.distanceToSqr(to) * t * t);
    }

    public boolean intersectsSegment(Vec3 from, Vec3 to) {
        return clipParameter(from, to).isPresent();
    }

    private OptionalDouble clipParameter(Vec3 from, Vec3 to) {
        AABB box = bounds();
        double tMin = 0.0D;
        double tMax = 1.0D;
        double[] start = {from.x, from.y, from.z};
        double[] delta = {to.x - from.x, to.y - from.y, to.z - from.z};
        double[] min = {box.minX, box.minY, box.minZ};
        double[] max = {box.maxX, box.maxY, box.maxZ};
        for (int axis = 0; axis < 3; axis++) {
            if (Math.abs(delta[axis]) < EPSILON) {
                if (start[axis] < min[axis] || start[axis] > max[axis]) return OptionalDouble.empty();
                continue;
            }
            double a = (min[axis] - start[axis]) / delta[axis];
            double b = (max[axis] - start[axis]) / delta[axis];
            if (a > b) { double swap = a; a = b; b = swap; }
            tMin = Math.max(tMin, a);
            tMax = Math.min(tMax, b);
            if (tMin > tMax) return OptionalDouble.empty();
        }
        return tMax < 0.0D || tMin > 1.0D ? OptionalDouble.empty() : OptionalDouble.of(Math.max(0.0D, tMin));
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", id);
        tag.putUUID("GroupId", groupId);
        tag.putInt("MinX", min.getX()); tag.putInt("MinY", min.getY()); tag.putInt("MinZ", min.getZ());
        tag.putInt("MaxX", max.getX()); tag.putInt("MaxY", max.getY()); tag.putInt("MaxZ", max.getZ());
        return tag;
    }

    public static FerrousRegion load(CompoundTag tag) {
        UUID id = tag.hasUUID("Id") ? tag.getUUID("Id") : UUID.randomUUID();
        UUID groupId = tag.hasUUID("GroupId") ? tag.getUUID("GroupId") : id;
        return new FerrousRegion(id, groupId,
                new BlockPos(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ")),
                new BlockPos(tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ")));
    }
}
