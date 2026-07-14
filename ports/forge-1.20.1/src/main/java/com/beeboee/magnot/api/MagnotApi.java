package com.beeboee.magnot.api;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class MagnotApi {
    public static final int API_VERSION = 2;

    private MagnotApi() {
    }

    public static boolean blocksItemPull(Level level, Vec3 source, ItemEntity item) {
        return level instanceof ServerLevel serverLevel && FerrousMagnetRules.blocksItemPull(serverLevel, source, item);
    }

    public static boolean blocksItemPull(ServerLevel level, Vec3 source, ItemEntity item) {
        return FerrousMagnetRules.blocksItemPull(level, source, item);
    }

    public static boolean blocksPlayerItemPull(Player player, ItemEntity item) {
        return player.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerItemPull(serverLevel, player, item);
    }

    public static boolean blocksPlayerItemPull(ServerLevel level, Player player, ItemEntity item) {
        return FerrousMagnetRules.blocksPlayerItemPull(level, player, item);
    }

    public static boolean blocksPull(Level level, Vec3 source, Vec3 target) {
        return level instanceof ServerLevel serverLevel && FerrousMagnetRules.blocksMagnet(serverLevel, source, target);
    }

    public static boolean blocksPull(ServerLevel level, Vec3 source, Vec3 target) {
        return FerrousMagnetRules.blocksMagnet(level, source, target);
    }

    @Deprecated(forRemoval = false)
    public static boolean blocksPlayerPull(Player player, Vec3 target) {
        return player.level() instanceof ServerLevel serverLevel
                && FerrousMagnetRules.blocksPlayerMagnet(serverLevel, player, target);
    }

    @Deprecated(forRemoval = false)
    public static boolean blocksPlayerPull(ServerLevel level, Player player, Vec3 target) {
        return FerrousMagnetRules.blocksPlayerMagnet(level, player, target);
    }

    public static Vec3 itemPullTarget(ItemEntity item) {
        return FerrousMagnetRules.itemPullTarget(item);
    }
}
