# Magnot — Forge 1.20.1 alpha

This branch ports Magnot's core ferrous-region behavior to Minecraft 1.20.1 Forge.

## Requirements

- Minecraft 1.20.1
- Forge 47.1.33 or newer
- Create 0.5.1 through the 6.0.x line; validated target: Create 6.0.8
- Java 17

## Included

- Ferrous paste regions created with the ferrous tube
- Persistent server-side region data
- Public item-pull compatibility API (`MagnotApi.API_VERSION == 2`)
- Visible region outlines while a player holds the tube

## Alpha 2 integration coverage

The following targets were checked against their published 1.20.1 classes and methods before this alpha was built:

- Simple Magnets `1.1.12-forge-mc1.20.1`
- Item Collectors `1.1.12` 1.20.x builds
- Sophisticated Core / Sophisticated Backpacks `1.20.1-1.3.67.2148`
- Artifacts Universal Attractor `9.5.19`
- Mekanism magnetic attraction module `10.4.16.80`
- Draconic Evolution magnet `3.1.2.621`
- Reliquary Reincarnations fortune coin `1.20.1-2.0.62.1532`
- Modular Routers vacuum module `12.1.1`
- Ender IO electromagnet and vacuum machines `6.2.18-beta`
- AE2 Wireless Terminals magnet card
- Mob Grinding Utils absorption hopper

The integrations remain optional. Alpha 2 replaces stale Artifacts and Ender IO target packages, and remaps Minecraft call sites used inside third-party classes so the hooks can apply in production Forge environments.

## Port status

This is an alpha port, not full parity with the 1.21.1 NeoForge release. Sable, moving sub-levels, filtered-region v2 behavior, and custom region entities are not included.

Use crouch + right-click to clear a pending corner. With no pending corner, crouch + right-click inside a region removes it.
