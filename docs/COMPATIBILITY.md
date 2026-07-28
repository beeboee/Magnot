# Magnet and vacuum compatibility

This page tracks exact compatibility status. The README stays broad; specific support is claimed only after the target mod and Magnot version have been reviewed together.

Magnot compatibility adapters are optional. Missing target mods must never prevent Magnot from loading, and Create availability does not determine whether the core mod or a version port is supported.

Mods that move dropped items can also support Magnot directly through the public compatibility API in [API.md](API.md).

## Current release target

- Magnot 1.2.0
- Minecraft 1.21.1
- NeoForge 21.1.230+
- Create optional
- Default Create development target: 6.0.11
- CI-tested Create range: 6.0.10 through the 6.0.x line

The Magnot-only configuration is a first-class tested target, not a reduced compatibility mode.

## Confirmed in the current 1.21.1 line

These have active compatibility hooks whose upstream target classes and methods were verified against matching NeoForge builds during the compatibility audit:

- Sophisticated Backpacks / Sophisticated Storage: magnet upgrades.
- Applied Energistics 2 Wireless Terminals: wireless magnet behavior through AE2WTLib.
- ProjectE: Black Hole Band, using the pinned tested ProjectE build until the latest official file is revalidated.
- Artifacts: Magnetism effect / Universal Attractor behavior.
- Mekanism: Magnetic Attraction Unit.
- Draconic Evolution: Magnet / Advanced Magnet.
- Reliquary Reincarnations: Fortune Coin, including normal use, long-range vacuum use, and pedestal item pickup.
- Mob Grinding Utils: Absorption Hopper item pickup, using the pinned tested build until its current distribution can be revalidated.
- Item Collectors by SuperMartijn642: Basic and Advanced Item Collectors.
- Simple Magnets by SuperMartijn642: Basic and Advanced Magnets.
- Modular Routers: Vacuum Module item pickup.
- Ender IO: Electromagnet item movement and Vacuum Chest item pickup.

## Source and bytecode verified

These hooks match the selected published classes and methods but retain a lower confidence label until their exact release combination receives another focused in-game regression test:

- Actually Additions: Ring of Magnetizing candidate-item query in `ItemMagnetRing.inventoryTick`.
- Industrial Foregoing: Infinity Backpack loose-item collection.
- Sable moving-level integration remains version-sensitive and should be tested with the exact Sable build used by a pack. The native Magnot renderer does not require Create for transformed Sable regions.

The Actually Additions hook was first registered after the early 1.0 releases. Those releases could include the mod in the development runtime without actually enabling the adapter.

## Create behavior

With Create installed, Magnot uses its Create/Catnip-backed presentation path. Without Create, it uses its native backend. Both paths use the same Magnot region data, texture, networking, limits, persistence, and removal behavior.

The native renderer is required for every future Minecraft/loader port. A missing Create version never blocks core parity work.

## Development test downloads

The `downloadCompatTestMods` Gradle task attempts to download current matching 1.21.1 NeoForge Modrinth builds for these projects into the compatibility runtime:

- Mob Grinding Utils
- Item Collectors by SuperMartijn642
- Simple Magnets by SuperMartijn642
- Modular Routers
- Industrial Foregoing
- Ender IO

Downloaded and manually supplied compatibility jars belong in `run/compat/mods`. They must not be committed. Clean launches deliberately ignore the legacy `run/mods` directory.

## Cross-version status

Compatibility claims do not automatically carry between Minecraft or loader versions. Each version branch maintains its own tested list.

The active port order is documented in [VERSION_SUPPORT.md](VERSION_SUPPORT.md): Forge and Fabric 1.20.1 are the first parity targets after the NeoForge 1.21.1 release, followed by the older Forge tracks.

## Planned integrations

- Simple Magnets / Magnets by LPSMods: magnet items and magnet blocks where available.
- Botania: revisit on targets with Ring of Magnetization.
- Cyclic: revisit on targets with magnet or vacuum behavior.
- Other maintained mods that move, teleport, or collect dropped items remotely.

## Compatibility rule

Prefer filtering candidate item entities before a target mod mutates them. Use head-cancel hooks for one-shot movement paths. Avoid intercepting final movement calls unless no earlier stable hook is available.
