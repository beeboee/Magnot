package com.beeboee.magnot.region;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

public final class FerrousMagnetRules {
    private FerrousMagnetRules() {
    }

    public static boolean blocksMagnet(WorldServer world, Vec3d source, Vec3d target) {
        return FerrousRegionSavedData.get(world).blocksMagnet(source, target);
    }

    public static Vec3d itemPullTarget(EntityItem item) {
        return item.getPositionVector().addVector(0.0D, item.height * 0.5D, 0.0D);
    }

    public static boolean blocksItemPull(WorldServer world, Vec3d source, EntityItem item) {
        return blocksMagnet(world, source, itemPullTarget(item));
    }

    public static boolean blocksPlayerItemPull(WorldServer world, EntityPlayer player, EntityItem item) {
        Vec3d target = itemPullTarget(item);
        AxisAlignedBB body = player.getEntityBoundingBox();
        return blocksMagnet(world, player.getPositionVector(), target)
                || blocksMagnet(world, new Vec3d((body.minX + body.maxX) * 0.5D, (body.minY + body.maxY) * 0.5D, (body.minZ + body.maxZ) * 0.5D), target)
                || blocksMagnet(world, player.getPositionEyes(1.0F), target);
    }
}
