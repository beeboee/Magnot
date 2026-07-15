# Magnot — Forge 1.18.2 alpha

Core Magnot port for Minecraft 1.18.2 Forge and the final Create 0.5.1 line.

## Requirements

- Minecraft 1.18.2
- Forge 40.2.4 or newer
- Create `1.18.2-0.5.1.i`
- Java 17

The build resolves that exact current Create artifact as part of verification.

Included: persistent ferrous regions, tube placement/removal, region outlines, and compatibility API v2.

## Alpha integration coverage

Active optional hooks whose current 1.18.2 target classes were verified:

- Simple Magnets `1.1.12-forge-mc1.18`
- Item Collectors `1.1.12-forge-mc1.18`
- AE2 Wireless Terminals matching the 1.18.2 handler layout
- Mekanism `10.2.5.465`
- Draconic Evolution `3.0.31.531`
- Reliquary Reincarnations `1.18.2-2.0.19.1161`, using the correct `reliquary.items` package
- Sophisticated Core `1.18.2-0.6.4.604`
- Modular Routers `9.1.2`
- Industrial Foregoing Infinity Backpack

Artifacts and Mob Grinding Utils are not advertised by this port. Their copied main-branch targets were not present or could not be verified in the matching 1.18.2 jars, so those entries were removed from the active mixin configuration.

All hooks remain optional. The exact target audit and Create dependency resolution improve confidence, but this is still an alpha until complete in-game testing is performed.

This branch derives from the shared 1.18–1.19 Forge core. It does not include Sable, moving sub-level support, filtered-region behavior, custom region entities, Ender IO integration, or complete 1.21.1 parity.
