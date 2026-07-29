# Magnot — Forge 1.7.10 alpha

This branch is the Forge 1.7.10 implementation of Magnot 1.2. It carries Magnot-owned gameplay parity on the legacy Java 7 and Forge APIs without requiring Create.

## Requirements

- Minecraft 1.7.10
- Forge 10.13.4.1614
- Java 8 runtime with Java 7-compatible Magnot classes
- MixinBooterLegacy 1.2.1 or newer

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
- one iron ingot plus one slime ball fallback for one ferrous paste

## Optional compatibility adapters

- Ender IO 2.2.4.309 Item Magnet: filters ordinary item entities before motion or direct pickup; XP attraction and energy use remain native
- Botania r1.8-247 Ring of Magnetization and Greater Ring: filters item entities before Botania applies motion; Botania blacklists, Solegnolia, cooldowns, and effects remain native
- OpenBlocks 1.6 Vacuum Hopper: rejects blocked item entities through its normal entity selector; XP orbs, item projectiles, inventory checks, and output behavior remain native

Sable, moving structures, and Create-backed presentation are separate. Other 1.7.10 magnets and collectors are not claimed until their exact release jars and mutation paths are audited.
