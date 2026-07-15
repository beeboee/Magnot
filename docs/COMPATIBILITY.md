# Magnet and vacuum compatibility

This page tracks exact compatibility status. Magnot support is optional: compatibility code must remain safe when a target mod is missing and must not add hard runtime dependencies to the published jar.

Mods that move dropped items can also support Magnot directly through the public compatibility API in [API.md](API.md).

## Current target

- Minecraft 1.21.1
- NeoForge 21.1.230+
- Default development target: Create 6.0.11
- Supported Create range: 6.0.10 through the 6.0.x line

## Runtime-tested integrations

- Sophisticated Backpacks / Sophisticated Storage magnet upgrades
- Applied Energistics 2 Wireless Terminals through AE2WTLib
- ProjectE Black Hole Band
- Artifacts Magnetism effect
- Mekanism Magnetic Attraction Unit
- Draconic Evolution Magnet / Advanced Magnet
- Reliquary Reincarnations Fortune Coin paths
- Mob Grinding Utils Absorption Hopper
- Item Collectors Basic and Advanced Item Collectors
- Simple Magnets Basic and Advanced Magnets
- Modular Routers Vacuum Module
- Ender IO Electromagnet and vacuum-machine paths

## Source and bytecode verified

These targets match the selected published classes and methods, but should still receive a focused in-game regression test before being described as fully confirmed in release notes:

- Actually Additions Ring of Magnetizing: `ItemMagnetRing.inventoryTick` candidate query
- Industrial Foregoing Infinity Backpack loose-item collection

The Actually Additions hook is registered in Magnot 1.0.2; earlier releases documented it without actually registering a mixin.

## Development test downloads

The `downloadCompatTestMods` Gradle task attempts to download current Minecraft 1.21.1 NeoForge builds for:

- Mob Grinding Utils
- Item Collectors
- Simple Magnets
- Modular Routers
- Industrial Foregoing
- Ender IO

Downloaded test jars belong in `run/mods` and must not be committed.

## Planned or future work

- Simple Magnets / Magnets by LPSMods magnet blocks
- Botania Ring of Magnetization when a compatible target is available
- Cyclic magnet or vacuum behavior when a compatible target is available
- Other mods that directly move, teleport, or collect loose item entities

## Compatibility rule

Prefer filtering candidate item entities before a target mod mutates them. Use head-cancel hooks for one-shot movement paths. Avoid intercepting final movement calls unless no earlier stable hook is available.
