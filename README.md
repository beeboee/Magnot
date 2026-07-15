# Magnot — Forge 1.16.5 alpha

Core Java 8 port for Minecraft 1.16.5 and Forge 36.2.9+.

## Requirements

- Minecraft 1.16.5
- Forge 36.2.9 or newer
- Create 0.3.2g, the final version reported by Create's maintained `mc1.16/dev` source branch
- Java 8

The build checks the official Create source version during verification so the port cannot silently drift from the final 1.16.5 release.

Included: persistent ferrous regions, tube placement/removal, particle outlines, and the compatibility API.

## Alpha integration coverage

Active optional hooks whose target classes were found in matching 1.16.5 jars:

- Simple Magnets `1.1.12-forge-mc1.16`, using its real `inventoryUpdate` method
- Item Collectors `1.1.12-forge-mc1.16`
- Mekanism `10.1.2.457`
- Draconic Evolution `3.0.29.518`
- Reliquary Reincarnations `1.16.5-1.3.5.1124`, using the actual `xreliquary.items` package
- Modular Routers `1.16.5-7.5.4`
- Industrial Foregoing `3.2.14.7`

Mob Grinding Utils and Sophisticated Core were removed from the active mixin list because their 1.16.5 targets could not be resolved and verified. Keeping optional no-op hooks would have overstated support.

The remaining integrations are optional Java 8 mixins. This is still an alpha until the combinations receive complete in-game testing.

This branch remains intentionally smaller than the 1.21.1 release. It does not include Sable, moving sub-levels, filtered regions, custom region entities, or modern NeoForge-only integrations.
