package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public record FerrousRegion(UUID id, BlockPos min, BlockPos max, UUID owner) {
    public static FerrousRegion fromCorners(BlockPos first, BlockPos second, UUID owner) {
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
        return new FerrousRegion(UUID.randomUUID(), min, max, owner);
    }

    public AABB bounds() {
        return new AABB(
                min.getX(), min.getY(), min.getZ(),
                max.getX() + 1.0D, max.getY() + 1.0D, max.getZ() + 1.0D
        );
    }

    public boolean contains(Vec3 pos) {
        return bounds().contains(pos);
    }

    public boolean contains(BlockPos pos) {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX()
                && pos.getY() >= min.getY() && pos.getY() <= max.getY()
                && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    public Optional<Vec3> clip(Vec3 from, Vec3 to) {
        if (contains(from)) {
            return Optional.of(from);
        }
        return bounds().clip(from, to);
    }

    public Optional<Double> hitDistanceSqr(Vec3 from, Vec3 to) {
        return clip(from, to).map(hit -> hit.distanceToSqr(from));
    }

    public boolean intersectsSegment(Vec3 from, Vec3 to) {
        return clip(from, to).isPresent();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", id);
        tag.putUUID("Owner", owner);
        tag.putInt("MinX", min.getX());
        tag.putInt("MinY", min.getY());
        tag.putInt("MinZ", min.getZ());
        tag.putInt("MaxX", max.getX());
        tag.putInt("MaxY", max.getY());
        tag.putInt("MaxZ", max.getZ());
        return tag;
    }

    public static FerrousRegion load(CompoundTag tag) {
        UUID id = tag.hasUUID("Id") ? tag.getUUID("Id") : UUID.randomUUID();
        UUID owner = tag.hasUUID("Owner") ? tag.getUUID("Owner") : new UUID(0L, 0L);
        BlockPos min = new BlockPos(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ"));
        BlockPos max = new BlockPos(tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ"));
        return new FerrousRegion(id, min, max, owner);
    }
}
