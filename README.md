# Magnot — Fabric 1.20.1 alpha

This branch ports Magnot's core ferrous-region behavior to Minecraft 1.20.1 Fabric.

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.17.2 or newer
- Fabric API 0.92.6 or newer for 1.20.1
- Create Fabric 0.5.1 through the 6.0.x line; validated target: 6.0.8.1
- Java 17

## Alpha 2 integration coverage

- Simple Magnets
- Item Collectors
- AE2 Wireless Terminals magnet card
- Artifacts Universal Attractor using its actual Fabric 1.20.1 `wornTick` implementation

Alpha 2 replaces the nonexistent modern Magnetism-effect target used by alpha.1 and remaps Minecraft call sites inside optional mod classes.

Included core features are persistent ferrous regions, tube placement/removal, region outlines, and API v2. Sable, moving sub-level support, filtered-region behavior, and custom region entities are not included.
