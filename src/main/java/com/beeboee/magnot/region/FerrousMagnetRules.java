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
 * Supported magnet adapters should call this before pulling an item. If this returns true,
 * the adapter should skip that item or abort the magnet pass.
 */
public final class FerrousMagnetRules {
    private FerrousMagnetRules() {
    }

    public static boolean blocksMagnet(ServerLevel level, Vec3 magnetSource, Vec3 itemPosition) {
        if (ModList.get().isLoaded("sable")) {
            return MagnotSableCompat.blocksMagnet(level, magnetSource, itemPosition);
        }

        return FerrousRegionSavedData.get(level).blocksMagnet(magnetSource, itemPosition);
    }

    public static boolean blocksPlayerMagnet(ServerLevel level, Player player, Vec3 itemPosition) {
        AABB body = player.getBoundingBox();
        Vec3 feet = player.position();
        Vec3 bodyCenter = body.getCenter();
        Vec3 eye = player.getEyePosition();

        return blocksMagnet(level, feet, itemPosition)
                || blocksMagnet(level, bodyCenter, itemPosition)
                || blocksMagnet(level, eye, itemPosition);
    }
}
