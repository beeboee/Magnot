# Magnot — Forge 1.7.10 alpha

This branch is the Forge 1.7.10 implementation of Magnot 1.2. It carries Magnot-owned gameplay parity on the legacy Java 7 and Forge APIs without requiring Create or any third-party integration.

## Included core behavior

- persistent ferrous regions with migration-safe IDs
- 25-block two-corner selection with clamped preview
- synchronized multiplayer region state
- native Magnot textured rendering, passive outlines, highlighting, and fade
- ray-selected left-click removal with server validation
- selection sounds, particles, chat feedback, and tube durability
- section-indexed and per-tick cached path blocking
- public API v2 while preserving the original `isPullBlocked` calls
- adaptive Ore Dictionary recipes for `dustIron` and `plateIron`
- iron-ingot fallback recipes for materials vanilla 1.7.10 does not provide

## Compatibility scope

This branch intentionally does not include automatic magnet/vacuum integrations, Sable, moving structures, or Create-backed presentation. External mods can use `MagnotApi` to query the authoritative region rules.
