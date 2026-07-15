# Magnot — Forge 1.19.2 alpha

Core Magnot port for Minecraft 1.19.2 Forge and the final Create 0.5.1 line.

## Requirements

- Minecraft 1.19.2
- Forge 43.2.14 or newer
- Create `1.19.2-0.5.1.i`
- Java 17

The build resolves that exact current Create artifact as part of verification.

Included: persistent ferrous regions, tube placement/removal, region outlines, and compatibility API v2.

## Alpha integration coverage

Active optional hooks whose current 1.19.2 target classes were verified:

- Simple Magnets `1.1.12-forge-mc1.19.2`
- Item Collectors `1.1.12-forge-mc1.19.2`
- AE2 Wireless Terminals `12.9.7-forge`
- Mekanism `10.3.9.13`
- Reliquary Reincarnations `1.19.2-2.0.40.1198`, using the correct `reliquary.items` package
- Sophisticated Core `1.19.2-0.6.4.730`
- Modular Routers `1.19.2-10.2.1`
- Industrial Foregoing Infinity Backpack

Artifacts and Mob Grinding Utils are not advertised by this port. Their previously copied main-branch targets were not present or could not be verified in the matching 1.19.2 jars, so those entries were removed from the active mixin configuration.

All hooks remain optional. The exact target audit and Create dependency resolution improve confidence, but this is still an alpha until the combinations receive complete in-game testing.

Not included: Sable, moving sub-level support, filtered-region behavior, custom region entities, Ender IO integration, or full 1.21.1 parity.
