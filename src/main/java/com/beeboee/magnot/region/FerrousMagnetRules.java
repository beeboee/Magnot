package com.beeboee.magnot.region;

import com.beeboee.magnot.compat.sable.MagnotSableCompat;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;

/**
 * Shared entry point for magnet integrations.
 *
 * Supported magnet adapters should call this before pulling a target. If this returns true,
 * the adapter should skip that target or abort the magnet pass.
 */
public final class FerrousMagnetRules {
    private FerrousMagnetRules() {
    }

    public static boolean blocksMagnet(ServerLevel level, Vec3 magnetSource, Vec3 targetPosition) {
        if (FerrousRegionEntityLookup.blocksMagnet(level, magnetSource, targetPosition)) {
            return true;
        }

        if (ModList.get().isLoaded("sable")) {
            return MagnotSableCompat.blocksMagnet(level, magnetSource, targetPosition);
        }

        return FerrousRegionSavedData.get(level).blocksMagnet(magnetSource, targetPosition);
    }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 targetPosition) {
        if (FerrousRegionEntityLookup.blocksPlayerMagnet(level, player, targetPosition)) {
            return true;
        }

        AABB body = player.getBoundingBox();
        Vec3 feet = player.position();
        Vec3 bodyCenter = body.getCenter();
        Vec3 eye = player.getEyePosition();

        if (ModList.get().isLoaded("sable")) {
            return MagnotSableCompat.blocksMagnet(level, feet, targetPosition)
                    || MagnotSableCompat.blocksMagnet(level, bodyCenter, targetPosition)
                    || MagnotSableCompat.blocksMagnet(level, eye, targetPosition);
        }

        FerrousRegionSavedData data = FerrousRegionSavedData.get(level);
        return data.blocksMagnet(feet, targetPosition)
                || data.blocksMagnet(bodyCenter, targetPosition)
                || data.blocksMagnet(eye, targetPosition);
    }
}
