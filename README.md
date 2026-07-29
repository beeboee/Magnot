# Magnot — Fabric / Quilt 1.20.1 alpha

This branch carries Magnot 1.2's complete loader-independent feature set on Fabric, with Quilt compatibility validated through the Fabric-compatible artifact.

## Requirements

- Minecraft 1.20.1
- Fabric Loader 0.16.10 or newer
- Fabric API 0.92.6 or newer
- Java 17
- Create is optional

## Included Magnot features

- Create-free startup
- Immediate placement preview and 25-block axis limit
- Native textured active faces, passive region outlines, ray highlighting, and quick fade
- Attack-to-remove with server-side ID and reach validation
- Region synchronization on login, dimension change, placement, and removal
- Stable persistent IDs/group IDs with migration of old alpha saves
- Indexed/cached path blocking and source-inside behavior
- Public API v2
- Adaptive common-tag dust/plate recipes, Create fallback dust, and vanilla fallback recipes
- Dormant fallback dust omitted from the creative tab

## Optional compatibility adapters

The Fabric/Quilt artifact filters item candidates before compatible systems move or insert them. Active adapters target:

- Simple Magnets
- Item Collectors
- Sophisticated Backpacks / Sophisticated Storage through the Fabric Sophisticated Core port
- AE2 Wireless Terminals
- Artifacts Universal Attractor

Mods without a maintained Fabric/Quilt 1.20.1 implementation are not advertised on this target. Sable remains separate from this integration pass.
