# Magnot — Forge 1.12.2 alpha

This branch is the Forge 1.12.2 implementation of Magnot 1.2. It carries Magnot-owned gameplay parity without requiring Create or any third-party integration.

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

## Compatibility scope

This branch intentionally does not include automatic magnet/vacuum integrations, Sable, moving structures, or Create-backed presentation. External mods can use `MagnotApi` to query the authoritative region rules.
