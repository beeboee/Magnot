# Magnot — Forge 1.20.1 alpha

This branch ports Magnot's core ferrous-region behavior to Minecraft 1.20.1 Forge.

## Requirements

- Minecraft 1.20.1
- Forge 47.1.33 or newer
- Create 6.0.8, below Create 6.1
- Java 17

The build resolves the current Modrinth Create artifact `mc1.20.1-6.0.8` as part of verification.

## Included

- Ferrous paste regions created with the ferrous tube
- Persistent server-side region data
- Public item-pull compatibility API (`MagnotApi.API_VERSION == 2`)
- Visible region outlines while a player holds the tube

## Alpha integration coverage

Active optional hooks whose target classes were found in current matching upstream jars:

- Simple Magnets `1.1.12-forge-mc1.20.1`
- Item Collectors 1.20.x builds
- Sophisticated Core / Sophisticated Backpacks `1.20.1-1.3.67.2148`
- AE2 Wireless Terminals `15.3.3-forge`
- Mekanism `10.4.16.80`
- Draconic Evolution `3.1.2.621`
- Reliquary Reincarnations `1.20.1-2.0.62.1532`
- Modular Routers `12.1.1`
- Ender IO `6.2.18-beta`, using its 1.20.1 package layout
- Industrial Foregoing `1.20.1-3.5.22` Infinity Backpack

Artifacts and Mob Grinding Utils are not advertised by this port. Their previously copied hooks could not be tied to a verified 1.20.1 target and were removed from the active mixin configuration rather than silently doing nothing.

All compatibility mods remain optional. Bytecode target verification and a successful Magnot build are stronger than the earlier compile-only alpha, but complete in-game interaction testing is still required before promoting these hooks to stable support.

## Port status

This is an alpha port, not a claim of full parity with the 1.21.1 NeoForge release. Sable, moving sub-levels, filtered-region v2 behavior, and custom region entities are not included.

Use crouch + right-click to clear a pending corner. With no pending corner, crouch + right-click inside a region removes it.
