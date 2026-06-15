# Magnot compatibility API

Magnot exposes a small server-side API for mods that move dropped items from a distance.

Use this when your mod has a magnet, vacuum, remote collector, absorption hopper, item teleporter, or similar feature. The intended integration point is immediately before your mod moves, teleports, inserts, or deletes an item entity.

## Dependency shape

Depend on Magnot as optional / compile-only. Do not make Magnot required unless your mod's feature truly cannot run without it.

A typical integration should keep all direct Magnot references in a small compat class and only call that class when Magnot is loaded.

```java
if (ModList.get().isLoaded("magnot") && MagnotCompat.blocksItemPull(level, source, item)) {
    return;
}
```

## Item pull check

Use this for block vacuums, item collectors, absorption hoppers, routers, and other non-player item movers.

```java
import com.beeboee.magnot.api.MagnotApi;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class MagnotCompat {
    private MagnotCompat() {
    }

    public static boolean blocksItemPull(Level level, Vec3 source, ItemEntity item) {
        return MagnotApi.blocksItemPull(level, source, item);
    }
}
```

## Player item pull check

Use this for player-held magnets, curio/trinket magnets, armor modules, backpack magnets, or player-centered vacuum effects.

```java
import com.beeboee.magnot.api.MagnotApi;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;

public final class MagnotCompat {
    private MagnotCompat() {
    }

    public static boolean blocksPlayerItemPull(Player player, ItemEntity item) {
        return MagnotApi.blocksPlayerItemPull(player, item);
    }
}
```

## Generic point-to-point check

Use this only when you are not moving an `ItemEntity`, or when you already have a precise target point.

```java
boolean blocked = MagnotApi.blocksPull(level, sourcePosition, targetPosition);
```

For dropped items, prefer `blocksItemPull(...)` or `blocksPlayerItemPull(...)` so Magnot can inspect the item entity. Item-aware calls are required for filtered ferrous regions.

## Client behavior

The API returns `false` on the client. Server logic is authoritative.

If your magnet performs client-side prediction, suppress that prediction or mirror the server-side check carefully. Otherwise blocked items may visually jump and then snap back.

## Stability

`MagnotApi.API_VERSION` is currently `2`.

The API should remain source-compatible within a Minecraft target line when possible. If Magnot needs to replace a public method, the old method should be deprecated before removal.
