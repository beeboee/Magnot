package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.UUID;

public record FerrousRegion(UUID id, BlockPos min, BlockPos max) {
    public static FerrousRegion fromCorners(BlockPos first, BlockPos second) {
        BlockPos min = new BlockPos(Math.min(first.getX(), second.getX()), Math.min(first.getY(), second.getY()), Math.min(first.getZ(), second.getZ()));
        BlockPos max = new BlockPos(Math.max(first.getX(), second.getX()), Math.max(first.getY(), second.getY()), Math.max(first.getZ(), second.getZ()));
        return new FerrousRegion(UUID.randomUUID(), min, max);
    }

    public AABB bounds() {
        return new AABB(min.getX(), min.getY(), min.getZ(), max.getX() + 1.0D, max.getY() + 1.0D, max.getZ() + 1.0D);
    }

    public boolean contains(BlockPos point) {
        return point.getX() >= min.getX() && point.getX() <= max.getX()
                && point.getY() >= min.getY() && point.getY() <= max.getY()
                && point.getZ() >= min.getZ() && point.getZ() <= max.getZ();
    }

    public boolean intersectsSegment(Vec3 from, Vec3 to) {
        return bounds().contains(from) || bounds().contains(to) || bounds().clip(from, to).isPresent();
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("Id", id);
        tag.putInt("MinX", min.getX()); tag.putInt("MinY", min.getY()); tag.putInt("MinZ", min.getZ());
        tag.putInt("MaxX", max.getX()); tag.putInt("MaxY", max.getY()); tag.putInt("MaxZ", max.getZ());
        return tag;
    }

    public static FerrousRegion load(CompoundTag tag) {
        return new FerrousRegion(
                tag.hasUUID("Id") ? tag.getUUID("Id") : UUID.randomUUID(),
                new BlockPos(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ")),
                new BlockPos(tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ"))
        );
    }
}
