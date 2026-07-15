# Magnot — Fabric 1.20.1 alpha

This branch ports Magnot's core ferrous-region behavior to Minecraft 1.20.1 Fabric.

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.17.2 or newer
- Fabric API 0.92.6 or newer for 1.20.1
- Create Fabric 6.0.8 or newer within the 6.0 line
- Java 17

The build resolves the current Create Fabric artifact `6.0.8.1+build.1744-mc1.20.1` as part of verification.

## Included

- Ferrous paste regions created with the ferrous tube
- Persistent server-side region data
- Public item-pull compatibility API (`MagnotApi.API_VERSION == 2`)
- Visible region outlines while a player holds the tube

## Alpha integration coverage

Active optional hooks whose classes and methods were found in current matching Fabric jars:

- Simple Magnets `1.1.12-fabric-mc1.20.1`
- Item Collectors Fabric 1.20.x builds
- AE2 Wireless Terminals `15.2.1-fabric`

The previously copied Artifacts hook was removed from the active mixin configuration because the 1.20.1 Fabric jar does not contain the main-branch `MagnetismMobEffect` target. Leaving it enabled would have advertised support while silently doing nothing.

These integrations remain optional. The target audit and exact Create dependency resolution improve confidence, but full in-game interaction tests are still needed before promoting this port from alpha.

## Port status

This is an alpha port, not full parity with the 1.21.1 NeoForge release. Sable, moving sub-levels, filtered-region v2 behavior, custom region entities, and Forge-only compatibility layers are not included.

Use crouch + right-click to clear a pending corner. With no pending corner, crouch + right-click inside a region removes it.
