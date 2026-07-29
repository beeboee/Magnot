package com.beeboee.magnot.api;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public final class MagnotApi {
    public static final int API_VERSION = 2;

    private MagnotApi() {
    }

    public static boolean isPullBlocked(World world, Vec3 source, Vec3 target) {
        return blocksPull(world, source, target);
    }

    public static boolean blocksPull(World world, Vec3 source, Vec3 target) {
        if (!(world instanceof WorldServer) || source == null || target == null) {
            return false;
        }
        return FerrousMagnetRules.blocksMagnet((WorldServer) world, source, target);
    }

    public static boolean isPullBlocked(
            World world,
            double sourceX,
            double sourceY,
            double sourceZ,
            double targetX,
            double targetY,
            double targetZ
    ) {
        return blocksPull(
                world,
                Vec3.createVectorHelper(sourceX, sourceY, sourceZ),
                Vec3.createVectorHelper(targetX, targetY, targetZ)
        );
    }
}
