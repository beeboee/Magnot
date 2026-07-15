# Magnot — Fabric 1.20.1 alpha

This branch ports Magnot's core ferrous-region behavior to Minecraft 1.20.1 Fabric.

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.17.2 or newer
- Fabric API 0.92.6 or newer for 1.20.1
- Create Fabric port 0.5.1 or newer
- Java 17

## Included

- Ferrous paste regions created with the ferrous tube
- Persistent server-side region data
- Public item-pull compatibility API (`MagnotApi.API_VERSION == 2`)
- Visible region outlines while a player holds the tube

## Alpha integration coverage

- Simple Magnets `1.1.12-fabric-mc1.20.1`
- Item Collectors `1.1.12` Fabric 1.20.x builds
- Artifacts `9.5.17`
- AE2 Wireless Terminals where the target magnet handler matches the 1.20.1 Fabric build

The integrations are optional. Simple Magnets and Item Collectors have existing port coverage; Artifacts and AE2 Wireless Terminals are experimental until runtime-tested.

## Port status

This is an alpha port, not full parity with the 1.21.1 NeoForge release. Sable, moving sub-levels, filtered-region v2 behavior, custom region entities, and Forge-only compatibility layers are not included.

Use crouch + right-click to clear a pending corner. With no pending corner, crouch + right-click inside a region removes it.
