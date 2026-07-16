package com.beeboee.magnot.client.selection;

import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public record FerrousSelectionView(AABB displayBounds, Vec3[] corners) {
    public static FerrousSelectionView axisAligned(AABB bounds) {
        return new FerrousSelectionView(bounds, corners(bounds));
    }

    public static Vec3[] corners(AABB bounds) {
        return new Vec3[]{
                new Vec3(bounds.minX, bounds.minY, bounds.minZ),
                new Vec3(bounds.maxX, bounds.minY, bounds.minZ),
                new Vec3(bounds.maxX, bounds.minY, bounds.maxZ),
                new Vec3(bounds.minX, bounds.minY, bounds.maxZ),
                new Vec3(bounds.minX, bounds.maxY, bounds.minZ),
                new Vec3(bounds.maxX, bounds.maxY, bounds.minZ),
                new Vec3(bounds.maxX, bounds.maxY, bounds.maxZ),
                new Vec3(bounds.minX, bounds.maxY, bounds.maxZ)
        };
    }
}
