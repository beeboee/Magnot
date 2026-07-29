# Magnot — Forge 1.20.1 alpha

This branch is the Forge 1.20.1 port of Magnot 1.2's loader-independent feature set.

## Requirements

- Minecraft 1.20.1
- Forge 47.1.33 or newer
- Java 17
- Create is optional

## Included Magnot features

- Create-free startup and a complete native selection backend
- Immediate two-corner placement preview with the 25-block axis limit
- Textured ferrous faces for the active selection
- Passive stored-region outlines, ray-selected highlighting, and quick deselection fade
- Attack-to-remove with server-side reach and region-ID validation
- Client synchronization on login, dimension change, placement, and removal
- Persistent region IDs and group IDs with migration of older alpha saves
- Indexed path intersection, source-inside blocking, and per-tick query caching
- Public item-pull API v2
- Adaptive recipes:
  - external iron dust tags when available
  - Magnot fallback dust through Create crushing only when Create is installed and no external dust exists
  - eight iron nuggets when neither external dust nor Create is available
  - tagged iron plates when available, otherwise an iron ingot
- Dormant fallback iron dust omitted from the creative tab

## Optional compatibility adapters

The alpha filters candidate item entities before compatible mods move, teleport, insert, or remove them. Active adapters target:

- Simple Magnets
- Item Collectors
- Sophisticated Backpacks / Sophisticated Storage
- AE2 Wireless Terminals
- ProjectE Black Hole Band in inventory, alchemical bag, pedestal, and alchemical chest modes
- Artifacts Universal Attractor
- Mekanism Magnetic Attraction Unit
- Draconic Evolution magnets
- Reliquary Fortune Coin, including player and pedestal collection
- Modular Routers Vacuum Module
- Ender IO Electromagnet and Vacuum Chest
- Industrial Foregoing Infinity Backpack

Mob Grinding Utils is not registered on this target until an exact 1.20.1 distribution and interception point can be validated. Sable remains separate from this compatibility pass.
