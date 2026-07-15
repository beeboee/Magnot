# Magnot — Forge 1.20.1 alpha

This branch ports Magnot's core ferrous-region behavior to Minecraft 1.20.1 Forge.

## Requirements

- Minecraft 1.20.1
- Forge 47.1.33 or newer
- Create 0.5.1 or newer
- Java 17

## Included

- Ferrous paste regions created with the ferrous tube
- Persistent server-side region data
- Public item-pull compatibility API (`MagnotApi.API_VERSION == 2`)
- Visible region outlines while a player holds the tube

## Alpha integration coverage

The alpha contains optional mixin hooks for:

- Simple Magnets `1.1.12-forge-mc1.20.1`
- Item Collectors `1.1.12` 1.20.x builds
- Sophisticated Core / Sophisticated Backpacks `1.20.1-1.3.67.2148`
- Artifacts `9.5.19`
- Mekanism `10.4.16.80`
- Draconic Evolution `3.1.2.621`
- Reliquary Reincarnations `1.20.1-2.0.62.1532`
- Modular Routers `12.1.1`
- Ender IO `6.2.18-beta`
- AE2 Wireless Terminals and Mob Grinding Utils where their target methods match the 1.20.1 build

These hooks are deliberately optional and do not make the listed mods hard dependencies. The established Simple Magnets, Item Collectors, and Sophisticated Core hooks have prior port coverage. The newly added hooks are **experimental** until tested in a complete modded runtime.

## Port status

This is an alpha port, not a claim of full parity with the 1.21.1 NeoForge release. Sable, moving sub-levels, filtered-region v2 behavior, and custom region entities are not included.

Use crouch + right-click to clear a pending corner. With no pending corner, crouch + right-click inside a region removes it.
