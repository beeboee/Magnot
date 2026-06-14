# Magnet and vacuum compatibility

Magnot support is kept optional. Compat code should be safe when a target mod is missing and should not add hard runtime dependencies to the published mod.

Mods that move dropped items can also support Magnot directly through the public compatibility API in [API.md](API.md).

## Current 1.21.1 NeoForge target

### Implemented or actively being tested

- Sophisticated Backpacks / Sophisticated Storage: magnet upgrades.
- Applied Energistics 2 Wireless Terminals: wireless magnet behavior through AE2WTLib.
- ProjectE: Black Hole Band.
- Artifacts: Magnetism effect.
- Mekanism: Magnetic Attraction Unit.
- Draconic Evolution: Magnet / Advanced Magnet.
- Reliquary Reincarnations: Fortune Coin, including player use, long-range vacuum use, and pedestal item pickup.
- Actually Additions: pinned in the local test environment; implementation still needs runtime inspection.
- Mob Grinding Utils: Absorption Hopper item pickup.
- Item Collectors by SuperMartijn642: Basic Item Collector and Advanced Item Collector item pickup.
- Simple Magnets by SuperMartijn642: Basic Magnet and Advanced Magnet item movement.
- Modular Routers: Vacuum Module item pickup.
- Ender IO: Electromagnet item movement and Vacuum Chest item pickup.

### Downloaded for local testing through Gradle

The `downloadCompatTestMods` Gradle task attempts to download the newest Minecraft 1.21.1 NeoForge Modrinth build for these projects into `run/mods`:

- Mob Grinding Utils: Absorption Hopper.
- Item Collectors by SuperMartijn642: Basic Item Collector and Advanced Item Collector.
- Simple Magnets by SuperMartijn642: Basic Magnet and Advanced Magnet.
- Modular Routers: dropped-item absorption modules.
- Industrial Foregoing: inspect for loose-item collection behavior.
- Ender IO: Electromagnet and Vacuum Chest.

### Planned for this target

- Simple Magnets / Magnets by LPSMods: magnet items and magnet blocks.
- Other vacuum-style item collectors that are available for Minecraft 1.21.1 on NeoForge and directly move, teleport, or collect item entities across distance.

## Future target versions or backports

These should be revisited when Magnot targets a Minecraft/mod-loader version where the mod exists and can be tested cleanly:

- Botania: Ring of Magnetization.
- Cyclic: magnet and vacuum item/block behavior.
- Any other magnet-source or vacuum-source mod that has a current NeoForge build and directly moves item entities.

## Compatibility rule

Prefer filtering candidate item entities before a target mod mutates them. Only fall back to intercepting final movement calls when candidate filtering is not practical.
