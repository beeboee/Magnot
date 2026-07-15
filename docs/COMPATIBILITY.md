# Magnet and vacuum compatibility

This page tracks exact compatibility status. The README should stay broad; only claim specific mod support publicly after it has been tested against the current Magnot target.

Magnot support is optional. Compat code should be safe when a target mod is missing and should not add hard runtime dependencies to the published mod.

Mods that move dropped items can also support Magnot directly through the public compatibility API in [API.md](API.md).

## Current target

- Minecraft 1.21.1
- NeoForge 21.1.230+
- Default development target: Create 6.0.11
- Supported and CI-tested Create range: 6.0.10 through the 6.0.x line

## Confirmed in current testing

These have active compatibility hooks whose current upstream target classes and methods were verified against the latest matching 1.21.1 NeoForge builds available during the compatibility audit:

- Sophisticated Backpacks / Sophisticated Storage: magnet upgrades.
- Applied Energistics 2 Wireless Terminals: wireless magnet behavior through AE2WTLib.
- ProjectE: Black Hole Band, using the pinned tested ProjectE build until the latest official file is revalidated.
- Artifacts: Magnetism effect.
- Mekanism: Magnetic Attraction Unit.
- Draconic Evolution: Magnet / Advanced Magnet.
- Reliquary Reincarnations: Fortune Coin, including normal use, long-range vacuum use, and pedestal item pickup.
- Mob Grinding Utils: Absorption Hopper item pickup, using the pinned tested build until its current distribution can be revalidated.
- Item Collectors by SuperMartijn642: Basic Item Collector and Advanced Item Collector item pickup.
- Simple Magnets by SuperMartijn642: Basic Magnet and Advanced Magnet item movement.
- Modular Routers: Vacuum Module item pickup.
- Ender IO: Electromagnet item movement and Vacuum Chest item pickup.

## Source and bytecode verified

These hooks match the selected published classes and methods but still need a focused in-game regression test before being described as fully confirmed in release notes:

- Actually Additions: Ring of Magnetizing candidate-item query in `ItemMagnetRing.inventoryTick`.
- Industrial Foregoing: Infinity Backpack loose-item collection.
- Sable / Create Aeronautics moving-level integration remains version-sensitive and should be tested with the exact Create and Sable builds used by a pack.

The Actually Additions hook is registered in Magnot 1.0.2. Earlier releases included the mod in the development runtime without actually registering a compatibility mixin.

## Dev test downloads

The `downloadCompatTestMods` Gradle task attempts to download the newest Minecraft 1.21.1 NeoForge Modrinth build for these projects into `run/mods`:

- Mob Grinding Utils
- Item Collectors by SuperMartijn642
- Simple Magnets by SuperMartijn642
- Modular Routers
- Industrial Foregoing
- Ender IO

This task is only for local testing. Downloaded jars should not be committed.

## Planned or future work

- Simple Magnets / Magnets by LPSMods: magnet items and magnet blocks.
- Botania: revisit when Magnot targets a compatible version with Ring of Magnetization available.
- Cyclic: revisit when Magnot targets a compatible version with magnet or vacuum behavior available.
- Other current NeoForge mods that directly move, teleport, or collect dropped items from a distance.

## Compatibility rule

Prefer filtering candidate item entities before a target mod mutates them. Use head-cancel hooks for one-shot movement paths. Avoid intercepting final movement calls unless no earlier stable hook is available.
