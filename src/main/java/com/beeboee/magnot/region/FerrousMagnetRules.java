package com.beeboee.magnot.region;

import com.beeboee.magnot.compat.sable.MagnotSableCompat;
import net.minecraft.server.level.ServerLevel;
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
        if (FerrousRegionSavedData.get(level).blocksMagnet(magnetSource, itemPosition)) {
            return true;
        }

        return ModList.get().isLoaded("sable") && MagnotSableCompat.blocksMagnet(level, magnetSource, itemPosition);
    }
}
