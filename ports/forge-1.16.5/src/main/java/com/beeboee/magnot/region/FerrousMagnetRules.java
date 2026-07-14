package com.beeboee.magnot.region;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public final class FerrousMagnetRules {
    private FerrousMagnetRules() {}
    public static boolean blocksMagnet(ServerWorld level, Vector3d source, Vector3d target) { return FerrousRegionSavedData.get(level).blocksMagnet(source, target); }
    public static Vector3d itemPullTarget(ItemEntity item) { return item.position().add(0.0D, item.getBbHeight() * 0.5D, 0.0D); }
    public static boolean blocksItemPull(ServerWorld level, Vector3d source, ItemEntity item) { return blocksMagnet(level, source, itemPullTarget(item)); }
    public static boolean blocksPlayerItemPull(ServerWorld level, PlayerEntity player, ItemEntity item) { return blocksPlayerMagnet(level, player, itemPullTarget(item)); }
    public static boolean blocksPlayerMagnet(ServerWorld level, PlayerEntity player, Vector3d target) {
        AxisAlignedBB body = player.getBoundingBox();
        return blocksMagnet(level, player.position(), target) || blocksMagnet(level, body.getCenter(), target) || blocksMagnet(level, player.getEyePosition(), target);
    }
}
