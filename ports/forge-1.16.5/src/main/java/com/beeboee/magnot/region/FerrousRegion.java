package com.beeboee.magnot.region;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import java.util.UUID;

public final class FerrousRegion {
    private final UUID id;
    private final BlockPos min;
    private final BlockPos max;

    public FerrousRegion(UUID id, BlockPos min, BlockPos max) {
        this.id = id;
        this.min = min;
        this.max = max;
    }

    public static FerrousRegion fromCorners(BlockPos first, BlockPos second) {
        BlockPos min = new BlockPos(Math.min(first.getX(), second.getX()), Math.min(first.getY(), second.getY()), Math.min(first.getZ(), second.getZ()));
        BlockPos max = new BlockPos(Math.max(first.getX(), second.getX()), Math.max(first.getY(), second.getY()), Math.max(first.getZ(), second.getZ()));
        return new FerrousRegion(UUID.randomUUID(), min, max);
    }

    public AxisAlignedBB bounds() { return new AxisAlignedBB(min.getX(), min.getY(), min.getZ(), max.getX() + 1.0D, max.getY() + 1.0D, max.getZ() + 1.0D); }
    public boolean contains(BlockPos pos) { return pos.getX() >= min.getX() && pos.getX() <= max.getX() && pos.getY() >= min.getY() && pos.getY() <= max.getY() && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ(); }
    public boolean intersects(Vector3d source, Vector3d target) { return bounds().contains(source) || bounds().contains(target) || bounds().clip(source, target).isPresent(); }

    public CompoundNBT save() {
        CompoundNBT tag = new CompoundNBT();
        tag.putUUID("Id", id);
        tag.putInt("MinX", min.getX()); tag.putInt("MinY", min.getY()); tag.putInt("MinZ", min.getZ());
        tag.putInt("MaxX", max.getX()); tag.putInt("MaxY", max.getY()); tag.putInt("MaxZ", max.getZ());
        return tag;
    }

    public static FerrousRegion load(CompoundNBT tag) {
        UUID id = tag.hasUUID("Id") ? tag.getUUID("Id") : UUID.randomUUID();
        return new FerrousRegion(id,
                new BlockPos(tag.getInt("MinX"), tag.getInt("MinY"), tag.getInt("MinZ")),
                new BlockPos(tag.getInt("MaxX"), tag.getInt("MaxY"), tag.getInt("MaxZ")));
    }
}
