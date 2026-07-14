# Magnot — Forge 1.20.1

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
- Optional mixin compatibility for Simple Magnets, Item Collectors, and Sophisticated Core

## Port status

This is a functional core port, not a claim of full parity with the 1.21.1 NeoForge branch. Sable, moving sub-levels, filtered-region v2 behavior, custom region entities, and the full 1.21.1 compatibility matrix are not included yet.

Use crouch + right-click to clear a pending corner. With no pending corner, crouch + right-click inside a region removes it.
