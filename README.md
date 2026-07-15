# Magnot — Forge 1.19.2 alpha

Core Magnot port for Minecraft 1.19.2 Forge and Create 0.5.1.

## Requirements

- Minecraft 1.19.2
- Forge 43.2.14 or newer
- Create 0.5.1 through the 0.5.x line; validated target: Create 0.5.1.i
- Java 17

## Alpha 2 integration coverage

Source and published-class targets were checked for:

- Simple Magnets
- Item Collectors
- AE2 Wireless Terminals
- Artifacts Universal Attractor using the 1.19.2 Curios implementation
- Mekanism magnetic attraction
- Reliquary Reincarnations fortune coin
- Mob Grinding Utils absorption hopper
- Modular Routers vacuum module
- Sophisticated Core magnet upgrades

Alpha 2 corrects the older Artifacts and Reliquary packages and explicitly remaps Minecraft call sites used inside third-party classes.

The port includes persistent ferrous regions, tube placement/removal, region outlines, and API v2. It does not yet include Sable, moving sub-level support, filtered-region behavior, custom region entities, or the complete 1.21.1 feature set.
