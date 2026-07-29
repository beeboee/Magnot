# Magnot — Forge 1.12.2 alpha

This branch is the Forge 1.12.2 implementation of Magnot 1.2. It carries Magnot-owned gameplay parity without requiring Create.

## Requirements

- Minecraft 1.12.2
- Forge 14.23.5.2860
- Java 8
- MixinBooter 10.7 or newer

## Included core behavior

- persistent ferrous regions with migration-safe IDs
- 25-block two-corner selection with clamped preview
- synchronized multiplayer region state
- native Magnot textured rendering, passive outlines, highlighting, and fade
- ray-selected left-click removal with server validation
- selection sounds, particles, status messages, and tube durability
- indexed and cached path-blocking API v2
- adaptive Ore Dictionary recipes for iron dust and iron plates
- vanilla nugget/ingot recipes when those materials are unavailable
- hidden dormant Magnot iron dust retained for registry and save compatibility

## Optional compatibility adapters

- Simple Magnets: filters the entity list before teleportation or ItemPhysic-assisted direct pickup
- Item Collectors: filters the entity list before inventory insertion and entity removal
- Draconic Evolution 2.3.28.354: filters ordinary item entities before motion reset or teleportation

Draconic loot cores are not ordinary item entities and retain their native behavior. Ender IO, Industrial Foregoing, Mekanism, Reliquary, and ProjectE use substantially different legacy systems and are not claimed until their exact item-pull paths are intercepted. Sable remains separate.
