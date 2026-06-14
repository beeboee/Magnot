package com.beeboee.magnot.debug;

import com.beeboee.magnot.Magnot;
import com.beeboee.magnot.region.FerrousRegion;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.phys.AABB;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public final class MagnotDebug {
    public static final boolean ENABLED = true;
    private static final String PREFIX = "[Magnot Debug]";
    private static final Path LOG_PATH = Path.of("logs", "magnot.log");
    private static final long SUMMARY_INTERVAL_TICKS = 100L;
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private static boolean writeFailureWarned;
    private static long lastSummaryTick = Long.MIN_VALUE;
    private static String lastSummaryDimension = "unknown";

    private static long cacheHits;
    private static long playerCacheHits;
    private static long checks;
    private static long playerChecks;
    private static long entityBlocked;
    private static long entityMissed;
    private static long fallbackChecked;
    private static long fallbackBlocked;
    private static long sableFallbackChecked;
    private static long savedDataFallbackChecked;
    private static long pointInsideChecks;
    private static long pointInsideHits;
    private static long candidateSearches;
    private static long candidateTotal;
    private static int maxCandidates;

    private MagnotDebug() {
    }

    public static Path logPath() {
        ensureLogDirectory();
        return LOG_PATH.toAbsolutePath().normalize();
    }

    public static void log(String message, Object... args) {
        if (!ENABLED) {
            return;
        }

        append(PREFIX + " " + format(message, args));
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

    public static void recordCacheHit(ServerLevel level, boolean playerCheck) {
        if (!ENABLED) {
            return;
        }

        prepareCounters(level);
        cacheHits++;
        if (playerCheck) {
            playerCacheHits++;
        }
    }

    public static void recordPointInsideCheck(ServerLevel level, boolean hit) {
        if (!ENABLED) {
            return;
        }

        prepareCounters(level);
        pointInsideChecks++;
        if (hit) {
            pointInsideHits++;
        }
    }

    public static void recordEntityCheck(ServerLevel level, boolean playerCheck, int candidateCount, boolean blocked) {
        if (!ENABLED) {
            return;
        }

        prepareCounters(level);
        checks++;
        if (playerCheck) {
            playerChecks++;
        }

        candidateSearches++;
        candidateTotal += candidateCount;
        maxCandidates = Math.max(maxCandidates, candidateCount);

        if (blocked) {
            entityBlocked++;
        } else {
            entityMissed++;
        }
    }

    public static void recordFallbackCheck(ServerLevel level, String kind, boolean blocked) {
        if (!ENABLED) {
            return;
        }

        prepareCounters(level);
        fallbackChecked++;
        if (blocked) {
            fallbackBlocked++;
        }

        if ("sable".equals(kind)) {
            sableFallbackChecked++;
        } else if ("saved-data".equals(kind)) {
            savedDataFallbackChecked++;
        }
    }

    public static String shortId(UUID uuid) {
        if (uuid == null) {
            return "null";
        }

        String text = uuid.toString();
        return text.substring(0, Math.min(8, text.length()));
    }

    private static void prepareCounters(ServerLevel level) {
        long gameTime = level.getGameTime();
        String dimension = level.dimension().location().toString();
        if (lastSummaryTick == Long.MIN_VALUE) {
            lastSummaryTick = gameTime;
            lastSummaryDimension = dimension;
            return;
        }

        if (gameTime - lastSummaryTick >= SUMMARY_INTERVAL_TICKS || !dimension.equals(lastSummaryDimension)) {
            writeSummary(gameTime, dimension);
            resetCounters();
            lastSummaryTick = gameTime;
            lastSummaryDimension = dimension;
        }
    }

    private static void writeSummary(long gameTime, String dimension) {
        if (checks == 0L && fallbackChecked == 0L && pointInsideChecks == 0L && cacheHits == 0L) {
            return;
        }

        double averageCandidates = candidateSearches == 0L ? 0.0D : (double) candidateTotal / candidateSearches;
        log("perf dim={} tick={} cacheHits={} playerCacheHits={} checks={} playerChecks={} entityBlocked={} entityMissed={} fallbackChecked={} fallbackBlocked={} sableFallbackChecked={} savedDataFallbackChecked={} pointInsideChecks={} pointInsideHits={} candidateSearches={} avgCandidates={} maxCandidates={}",
                dimension,
                gameTime,
                cacheHits,
                playerCacheHits,
                checks,
                playerChecks,
                entityBlocked,
                entityMissed,
                fallbackChecked,
                fallbackBlocked,
                sableFallbackChecked,
                savedDataFallbackChecked,
                pointInsideChecks,
                pointInsideHits,
                candidateSearches,
                String.format("%.2f", averageCandidates),
                maxCandidates
        );
    }

    private static void resetCounters() {
        cacheHits = 0L;
        playerCacheHits = 0L;
        checks = 0L;
        playerChecks = 0L;
        entityBlocked = 0L;
        entityMissed = 0L;
        fallbackChecked = 0L;
        fallbackBlocked = 0L;
        sableFallbackChecked = 0L;
        savedDataFallbackChecked = 0L;
        pointInsideChecks = 0L;
        pointInsideHits = 0L;
        candidateSearches = 0L;
        candidateTotal = 0L;
        maxCandidates = 0;
    }

    private static void append(String line) {
        try {
            ensureLogDirectory();
            Files.writeString(
                    LOG_PATH,
                    "[" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + "] " + line + System.lineSeparator(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException exception) {
            if (!writeFailureWarned) {
                writeFailureWarned = true;
                Magnot.LOGGER.warn("Failed to write Magnot debug log {}", LOG_PATH.toAbsolutePath().normalize(), exception);
            }
        }
    }

    private static void ensureLogDirectory() {
        try {
            Path parent = LOG_PATH.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
        } catch (IOException exception) {
            if (!writeFailureWarned) {
                writeFailureWarned = true;
                Magnot.LOGGER.warn("Failed to create Magnot debug log directory {}", LOG_PATH.toAbsolutePath().normalize(), exception);
            }
        }
    }

    private static String format(String message, Object... args) {
        String result = message;
        for (Object arg : args) {
            int placeholder = result.indexOf("{}");
            if (placeholder < 0) {
                break;
            }

            result = result.substring(0, placeholder) + String.valueOf(arg) + result.substring(placeholder + 2);
        }
        return result;
    }
}
