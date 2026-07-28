package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.UUID;

public record FerrousRegion(UUID id, UUID groupId, BlockPos min, BlockPos max, UUID subLevelId) {
    private static final double EPSILON = 1.0E-12D;

    public FerrousRegion(UUID id, BlockPos min, BlockPos max) {
        this(id, id, min, max, null);
    }

    public FerrousRegion(UUID id, BlockPos min, BlockPos max, UUID subLevelId) {
        this(id, id, min, max, subLevelId);
    }

    public static FerrousRegion fromCorners(BlockPos first, BlockPos second) {
        return fromCorners(UUID.randomUUID(), first, second, null);
    }

    public static FerrousRegion fromCorners(UUID id, BlockPos first, BlockPos second) {
        return fromCorners(id, first, second, null);
    }

    public static FerrousRegion fromCorners(UUID id, BlockPos first, BlockPos second, UUID subLevelId) {
        return fromCorners(id, id, first, second, subLevelId);
    }

    public static FerrousRegion fromCorners(UUID id, UUID groupId, BlockPos first, BlockPos second, UUID subLevelId) {
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
        return new FerrousRegion(id, groupId, min, max, subLevelId);
    }

    public boolean isWorldRegion() {
        return subLevelId == null;
    }

    public boolean belongsToSubLevel(UUID id) {
        return subLevelId != null && subLevelId.equals(id);
    }

    public boolean belongsToSameGroupAs(FerrousRegion other) {
        return groupId.equals(other.groupId);
    }

    public FerrousRegion inSubLevel(UUID id) {
        return new FerrousRegion(this.id, this.groupId, this.min, this.max, id);
    }

    public FerrousRegion inWorld() {
        return new FerrousRegion(this.id, this.groupId, this.min, this.max, null);
    }

    public AABB bounds() {
        return new AABB(minX(), minY(), minZ(), maxX(), maxY(), maxZ());
    }

    /** Allocation-free containment for the magnet hot path. */
    public boolean contains(Vec3 pos) {
        return pos.x >= minX() && pos.x <= maxX()
                && pos.y >= minY() && pos.y <= maxY()
                && pos.z >= minZ() && pos.z <= maxZ();
    }

    public boolean intersectsBlock(BlockPos pos) {
        return pos.getX() + 1.0D > minX() && pos.getX() < maxX()
                && pos.getY() + 1.0D > minY() && pos.getY() < maxY()
                && pos.getZ() + 1.0D > minZ() && pos.getZ() < maxZ();
    }

    public Optional<Vec3> clip(Vec3 from, Vec3 to) {
        OptionalDouble parameter = clipParameter(from, to);
        if (parameter.isEmpty()) {
            return Optional.empty();
        }
        double t = parameter.getAsDouble();
        return Optional.of(new Vec3(
                from.x + (to.x - from.x) * t,
                from.y + (to.y - from.y) * t,
                from.z + (to.z - from.z) * t
        ));
    }

    public Optional<Double> hitDistanceSqr(Vec3 from, Vec3 to) {
        OptionalDouble parameter = clipParameter(from, to);
        if (parameter.isEmpty()) {
            return Optional.empty();
        }
        double t = parameter.getAsDouble();
        return Optional.of(from.distanceToSqr(to) * t * t);
    }

    /** Allocation-free slab intersection used by FerrousRegionIndex. */
    public boolean intersectsSegment(Vec3 from, Vec3 to) {
        return clipParameter(from, to).isPresent();
    }

    private OptionalDouble clipParameter(Vec3 from, Vec3 to) {
        double tMin = 0.0D;
        double tMax = 1.0D;

        double delta = to.x - from.x;
        if (Math.abs(delta) < EPSILON) {
            if (from.x < minX() || from.x > maxX()) return OptionalDouble.empty();
        } else {
            double a = (minX() - from.x) / delta;
            double b = (maxX() - from.x) / delta;
            if (a > b) { double swap = a; a = b; b = swap; }
            tMin = Math.max(tMin, a);
            tMax = Math.min(tMax, b);
            if (tMin > tMax) return OptionalDouble.empty();
        }

        delta = to.y - from.y;
        if (Math.abs(delta) < EPSILON) {
            if (from.y < minY() || from.y > maxY()) return OptionalDouble.empty();
        } else {
            double a = (minY() - from.y) / delta;
            double b = (maxY() - from.y) / delta;
            if (a > b) { double swap = a; a = b; b = swap; }
            tMin = Math.max(tMin, a);
            tMax = Math.min(tMax, b);
            if (tMin > tMax) return OptionalDouble.empty();
        }

        delta = to.z - from.z;
        if (Math.abs(delta) < EPSILON) {
            if (from.z < minZ() || from.z > maxZ()) return OptionalDouble.empty();
        } else {
            double a = (minZ() - from.z) / delta;
            double b = (maxZ() - from.z) / delta;
            if (a > b) { double swap = a; a = b; b = swap; }
            tMin = Math.max(tMin, a);
            tMax = Math.min(tMax, b);
            if (tMin > tMax) return OptionalDouble.empty();
        }

        return tMax < 0.0D || tMin > 1.0D ? OptionalDouble.empty() : OptionalDouble.of(Math.max(0.0D, tMin));
    }

    private double minX() { return min.getX(); }
    private double minY() { return min.getY(); }
    private double minZ() { return min.getZ(); }
    private double maxX() { return max.getX() + 1.0D; }
    private double maxY() { return max.getY() + 1.0D; }
    private double maxZ() { return max.getZ() + 1.0D; }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", id);
        tag.putUUID("GroupId", groupId);
        tag.putInt("MinX", min.getX());
        tag.putInt("MinY", min.getY());
        tag.putInt("MinZ", min.getZ());
        tag.putInt("MaxX", max.getX());
        tag.putInt("MaxY", max.getY());
        tag.putInt("MaxZ", max.getZ());
        if (subLevelId != null) tag.putUUID("SubLevelId", subLevelId);
        return tag;
    }

    public static FerrousRegion load(CompoundTag tag) {
        UUID id = tag.hasUUID("Id") ? tag.getUUID("Id") : UUID.randomUUID();
        UUID groupId = tag.hasUUID("GroupId") ? tag.getUUID("GroupId") : id;
        BlockPos min = new BlockPos(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ"));
        BlockPos max = new BlockPos(tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ"));
        UUID subLevelId = tag.hasUUID("SubLevelId") ? tag.getUUID("SubLevelId") : null;
        return new FerrousRegion(id, groupId, min, max, subLevelId);
    }
}
