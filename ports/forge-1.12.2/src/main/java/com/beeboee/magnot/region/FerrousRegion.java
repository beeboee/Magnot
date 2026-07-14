package com.beeboee.magnot.region;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

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
        return new FerrousRegion(UUID.randomUUID(), min, max);
    }

    public AxisAlignedBB bounds() {
        return new AxisAlignedBB(min.getX(), min.getY(), min.getZ(), max.getX() + 1.0D, max.getY() + 1.0D, max.getZ() + 1.0D);
    }

    public boolean contains(BlockPos pos) {
        return pos.getX() >= min.getX() && pos.getX() <= max.getX()
                && pos.getY() >= min.getY() && pos.getY() <= max.getY()
                && pos.getZ() >= min.getZ() && pos.getZ() <= max.getZ();
    }

    public boolean intersects(Vec3d source, Vec3d target) {
        AxisAlignedBB box = bounds();
        return box.contains(source) || box.contains(target) || box.calculateIntercept(source, target) != null;
    }

    public NBTTagCompound write() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setUniqueId("Id", id);
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
        return new FerrousRegion(
                id,
                new BlockPos(tag.getInteger("MinX"), tag.getInteger("MinY"), tag.getInteger("MinZ")),
                new BlockPos(tag.getInteger("MaxX"), tag.getInteger("MaxY"), tag.getInteger("MaxZ"))
        );
    }
}
