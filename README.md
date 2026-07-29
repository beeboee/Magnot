# Magnot — NeoForge 1.20.1 alpha

This branch is the final-line NeoForge 1.20.1 port of Magnot 1.2's loader-independent feature set. It deliberately shares the behaviorally compatible 1.20.1 core with Forge while retaining separate packaging and validation.

## Requirements

- Minecraft 1.20.1
- NeoForge 47.1.106
- Java 17
- Create is optional

## Included Magnot features

- Create-free startup and the complete native selection backend
- Immediate two-corner preview, textured active faces, passive outlines, ray highlighting, and quick fade
- Attack-to-remove with server-side reach and stable region-ID validation
- Login, dimension-change, placement, and removal synchronization
- Persistent IDs/group IDs with old-alpha save migration
- Indexed and cached path blocking, including source-inside behavior
- Public item-pull API v2
- Adaptive dust, nugget, plate, and ingot recipe selection
- Fallback iron dust visible only when Create is installed and no external iron dust is available

## Optional compatibility adapters

The shared 1.20.1 adapter layer filters candidate item entities before compatible systems move, teleport, insert, or remove them. Active adapters target:

- Simple Magnets
- Item Collectors
- Sophisticated Backpacks / Sophisticated Storage
- AE2 Wireless Terminals
- Artifacts Universal Attractor
- Mekanism Magnetic Attraction Unit
- Draconic Evolution magnets
- Reliquary Fortune Coin, including player and pedestal collection
- Modular Routers Vacuum Module
- Ender IO Electromagnet and Vacuum Chest
- Industrial Foregoing Infinity Backpack

Mob Grinding Utils is not registered until an exact compatible distribution and target hook are validated. Sable remains separate. NeoForge-specific runtime testing remains appropriate for this alpha.
