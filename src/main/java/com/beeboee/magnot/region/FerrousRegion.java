package com.beeboee.magnot.region;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;
import java.util.UUID;

public record FerrousRegion(UUID id, BlockPos min, BlockPos max, UUID subLevelId) {
    public FerrousRegion(UUID id, BlockPos min, BlockPos max) {
        this(id, min, max, null);
    }

    public static FerrousRegion fromCorners(BlockPos first, BlockPos second) {
        return fromCorners(UUID.randomUUID(), first, second, null);
    }

    public static FerrousRegion fromCorners(UUID id, BlockPos first, BlockPos second) {
        return fromCorners(id, first, second, null);
    }

    public static FerrousRegion fromCorners(UUID id, BlockPos first, BlockPos second, UUID subLevelId) {
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
        return new FerrousRegion(id, min, max, subLevelId);
    }

    public boolean isWorldRegion() {
        return subLevelId == null;
    }

    public boolean belongsToSubLevel(UUID id) {
        return subLevelId != null && subLevelId.equals(id);
    }

    public FerrousRegion inSubLevel(UUID id) {
        return new FerrousRegion(this.id, this.min, this.max, id);
    }

    public FerrousRegion inWorld() {
        return new FerrousRegion(this.id, this.min, this.max, null);
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

    public boolean intersectsBlock(BlockPos pos) {
        return bounds().intersects(new AABB(
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D
        ));
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
        tag.putInt("MinX", min.getX());
        tag.putInt("MinY", min.getY());
        tag.putInt("MinZ", min.getZ());
        tag.putInt("MaxX", max.getX());
        tag.putInt("MaxY", max.getY());
        tag.putInt("MaxZ", max.getZ());
        if (subLevelId != null) {
            tag.putUUID("SubLevelId", subLevelId);
        }
        return tag;
    }

    public static FerrousRegion load(CompoundTag tag) {
        UUID id = tag.hasUUID("Id") ? tag.getUUID("Id") : UUID.randomUUID();
        BlockPos min = new BlockPos(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ"));
        BlockPos max = new BlockPos(tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ"));
        UUID subLevelId = tag.hasUUID("SubLevelId") ? tag.getUUID("SubLevelId") : null;
        return new FerrousRegion(id, min, max, subLevelId);
    }
}
