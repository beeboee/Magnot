# Magnet and vacuum compatibility

This page tracks exact compatibility status. The README should stay broad; only claim specific mod support publicly after it has been tested against the current Magnot target.

Magnot support is optional. Compat code should be safe when a target mod is missing and should not add hard runtime dependencies to the published mod.

Mods that move dropped items can also support Magnot directly through the public compatibility API in [API.md](API.md).

## Current target

- Minecraft 1.21.1
- NeoForge 21.1.230+
- Create 6.0.10+, below Create 6.1

## Confirmed in current testing

These have been tested against the current Magnot dev target:

- Sophisticated Backpacks / Sophisticated Storage: magnet upgrades.
- Applied Energistics 2 Wireless Terminals: wireless magnet behavior through AE2WTLib.
- ProjectE: Black Hole Band.
- Artifacts: Magnetism effect.
- Mekanism: Magnetic Attraction Unit.
- Draconic Evolution: Magnet / Advanced Magnet.
- Reliquary Reincarnations: Fortune Coin, including normal use, long-range vacuum use, and pedestal item pickup.
- Actually Additions: magnet behavior.
- Mob Grinding Utils: Absorption Hopper item pickup.
- Item Collectors by SuperMartijn642: Basic Item Collector and Advanced Item Collector item pickup.
- Simple Magnets by SuperMartijn642: Basic Magnet and Advanced Magnet item movement.
- Modular Routers: Vacuum Module item pickup.
- Ender IO: Electromagnet item movement and Vacuum Chest item pickup.

## Experimental compat layers

No active experimental compat layers are currently listed as supported. New compat layers should be tested before being listed as confirmed support on a release page.

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
- Industrial Foregoing: inspect for a current loose-item vacuum or remote item collection path.
- Botania: revisit when Magnot targets a compatible version with Ring of Magnetization available.
- Cyclic: revisit when Magnot targets a compatible version with magnet or vacuum behavior available.
- Other current NeoForge mods that directly move, teleport, or collect dropped items from a distance.

## Compatibility rule

Prefer filtering candidate item entities before a target mod mutates them. Use head-cancel hooks for one-shot movement paths. Avoid intercepting final movement calls unless no earlier stable hook is available.
