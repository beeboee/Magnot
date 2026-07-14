package com.beeboee.magnot.region;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class FerrousMagnetRules {
    private FerrousMagnetRules() {}

    public static boolean blocksMagnet(ServerLevel level, Vec3 source, Vec3 target) {
        return FerrousRegionSavedData.get(level).blocksMagnet(source, target);
    }

    public static Vec3 itemPullTarget(ItemEntity item) {
        return item.position().add(0.0D, item.getBbHeight() * 0.5D, 0.0D);
    }

    public static boolean blocksItemPull(ServerLevel level, Vec3 source, ItemEntity item) {
        return blocksMagnet(level, source, itemPullTarget(item));
    }

    public static boolean blocksPlayerItemPull(ServerLevel level, Player player, ItemEntity item) {
        return blocksPlayerMagnet(level, player, itemPullTarget(item));
    }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 target) {
        AABB body = player.getBoundingBox();
        return blocksMagnet(level, player.position(), target)
                || blocksMagnet(level, body.getCenter(), target)
                || blocksMagnet(level, player.getEyePosition(), target);
    }
}
