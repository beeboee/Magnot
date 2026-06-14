package com.beeboee.magnot.api;

import com.beeboee.magnot.region.FerrousMagnetRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

/**
 * Public compatibility API for mods that move dropped items from a distance.
 *
 * <p>External mods can use this from a small optional compat class with a
 * compileOnly dependency on Magnot. Call it immediately before pulling,
 * teleporting, inserting, or otherwise remotely collecting an item entity.</p>
 *
 * <p>All client-side calls return false. Magnot's region data is authoritative
 * on the server.</p>
 */
public final class MagnotApi {
    /**
     * Current Magnot compatibility API version.
     *
     * <p>This is intended for simple feature gates if the API grows later.</p>
     */
    public static final int API_VERSION = 1;

    private MagnotApi() {
    }

    /**
     * Returns true when Magnot should block a remote item pull from {@code source}
     * to the given {@code item}.
     */
    public static boolean blocksItemPull(Level level, Vec3 source, ItemEntity item) {
        return level instanceof ServerLevel serverLevel && blocksItemPull(serverLevel, source, item);
    }

    /**
     * Returns true when Magnot should block a remote item pull from {@code source}
     * to the given {@code item}.
     */
    public static boolean blocksItemPull(ServerLevel level, Vec3 source, ItemEntity item) {
        return FerrousMagnetRules.blocksItemPull(level, source, item);
    }

    /**
     * Returns true when Magnot should block a remote pull from {@code source} to
     * {@code target}. Prefer {@link #blocksItemPull(Level, Vec3, ItemEntity)} for
     * dropped items so the item's center point is used consistently.
     */
    public static boolean blocksPull(Level level, Vec3 source, Vec3 target) {
        return level instanceof ServerLevel serverLevel && blocksPull(serverLevel, source, target);
    }

    /**
     * Returns true when Magnot should block a remote pull from {@code source} to
     * {@code target}. Prefer {@link #blocksItemPull(ServerLevel, Vec3, ItemEntity)}
     * for dropped items so the item's center point is used consistently.
     */
    public static boolean blocksPull(ServerLevel level, Vec3 source, Vec3 target) {
        return FerrousMagnetRules.blocksMagnet(level, source, target);
    }

    /**
     * Returns true when Magnot should block a player magnet/vacuum effect from
     * pulling an item-like target to {@code player}.
     */
    public static boolean blocksPlayerPull(Player player, Vec3 target) {
        return player.level() instanceof ServerLevel serverLevel && blocksPlayerPull(serverLevel, player, target);
    }

    /**
     * Returns true when Magnot should block a player magnet/vacuum effect from
     * pulling an item-like target to {@code player}.
     */
    public static boolean blocksPlayerPull(ServerLevel level, Player player, Vec3 target) {
        return FerrousMagnetRules.blocksPlayerMagnet(level, player, target);
    }

    /**
     * The target point Magnot uses for dropped item entities.
     */
    public static Vec3 itemPullTarget(ItemEntity item) {
        return FerrousMagnetRules.itemPullTarget(item);
    }
}
