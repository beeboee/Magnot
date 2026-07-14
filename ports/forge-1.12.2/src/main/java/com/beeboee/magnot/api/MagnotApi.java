package com.beeboee.magnot.api;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public final class MagnotApi {
    public static final int API_VERSION = 2;

    private MagnotApi() {
    }

    public static boolean blocksItemPull(World world, Vec3d source, EntityItem item) {
        return world instanceof WorldServer && FerrousMagnetRules.blocksItemPull((WorldServer) world, source, item);
    }

    public static boolean blocksPlayerItemPull(EntityPlayer player, EntityItem item) {
        return player.world instanceof WorldServer
                && FerrousMagnetRules.blocksPlayerItemPull((WorldServer) player.world, player, item);
    }

    public static boolean blocksPull(World world, Vec3d source, Vec3d target) {
        return world instanceof WorldServer && FerrousMagnetRules.blocksMagnet((WorldServer) world, source, target);
    }
}
