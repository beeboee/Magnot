# Magnot — Forge 1.18.2 alpha

Core Magnot port for Minecraft 1.18.2 Forge and Create 0.5.1.

## Requirements

- Minecraft 1.18.2
- Forge 40.2.4 or newer
- Create 0.5.1 through the 0.5.x line; validated target: Create 0.5.1.i
- Java 17

## Alpha 2 integration coverage

Verified targets retained for:

- Simple Magnets
- Item Collectors
- AE2 Wireless Terminals
- Mekanism magnetic attraction
- Draconic Evolution magnet
- Reliquary Reincarnations fortune coin
- Mob Grinding Utils absorption hopper
- Modular Routers vacuum module
- Sophisticated Core magnet upgrades

Artifacts is not claimed for this branch because the selected 1.18.2 release did not expose the expected attraction implementation. Alpha 2 also corrects the Reliquary package and remaps Minecraft call sites used inside optional mod classes.

The port includes persistent ferrous regions, tube placement/removal, region outlines, and API v2. It does not yet include Sable, moving sub-level support, filtered-region behavior, custom region entities, or the complete 1.21.1 feature set.
