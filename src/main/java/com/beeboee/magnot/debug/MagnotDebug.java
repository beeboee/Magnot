package com.beeboee.magnot.debug;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.world.phys.AABB;

import java.util.UUID;

public final class MagnotDebug {
    public static final boolean ENABLED = true;
    private static final String PREFIX = "[Magnot Debug]";

    private MagnotDebug() {
    }

    public static void log(String message, Object... args) {
        if (!ENABLED) {
            return;
        }

        Magnot.LOGGER.info(PREFIX + " " + message, args);
    }

    public static void region(String stage, FerrousRegion region) {
        if (!ENABLED) {
            return;
        }

        AABB bounds = region.bounds();
        log("{} region id={} group={} owner={} min={} max={} size={}x{}x{} bounds=[{}, {}, {} -> {}, {}, {}]",
                stage,
                shortId(region.id()),
                shortId(region.groupId()),
                region.subLevelId() == null ? "world" : "sub:" + shortId(region.subLevelId()),
                region.min(),
                region.max(),
                bounds.getXsize(),
                bounds.getYsize(),
                bounds.getZsize(),
                bounds.minX,
                bounds.minY,
                bounds.minZ,
                bounds.maxX,
                bounds.maxY,
                bounds.maxZ
        );
    }

    public static String shortId(UUID uuid) {
        if (uuid == null) {
            return "null";
        }

        String text = uuid.toString();
        return text.substring(0, Math.min(8, text.length()));
    }
}
