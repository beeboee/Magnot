package com.beeboee.magnot.api;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

public final class MagnotApi {
    public static final int API_VERSION = 2;
    private MagnotApi() {}
    public static boolean blocksItemPull(World level, Vector3d source, ItemEntity item) { return level instanceof ServerWorld && FerrousMagnetRules.blocksItemPull((ServerWorld) level, source, item); }
    public static boolean blocksPlayerItemPull(PlayerEntity player, ItemEntity item) { return player.level instanceof ServerWorld && FerrousMagnetRules.blocksPlayerItemPull((ServerWorld) player.level, player, item); }
    public static boolean blocksPull(World level, Vector3d source, Vector3d target) { return level instanceof ServerWorld && FerrousMagnetRules.blocksMagnet((ServerWorld) level, source, target); }
    public static boolean blocksItemPull(ServerWorld level, Vector3d source, ItemEntity item) { return FerrousMagnetRules.blocksItemPull(level, source, item); }
    public static boolean blocksPlayerItemPull(ServerWorld level, PlayerEntity player, ItemEntity item) { return FerrousMagnetRules.blocksPlayerItemPull(level, player, item); }
    public static boolean blocksPull(ServerWorld level, Vector3d source, Vector3d target) { return FerrousMagnetRules.blocksMagnet(level, source, target); }
}
