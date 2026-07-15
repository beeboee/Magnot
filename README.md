# Magnot — Forge 1.18.2 alpha

Core Magnot port for Minecraft 1.18.2 Forge and Create 0.5.1.

Included: persistent ferrous regions, tube placement/removal, region outlines, and compatibility API v2.

## Alpha integration coverage

- Simple Magnets `1.1.12-forge-mc1.18`
- Item Collectors `1.1.12-forge-mc1.18`
- Mekanism `10.2.5.465`
- Draconic Evolution `3.0.31.531`
- Reliquary Reincarnations `1.18.2-2.0.19.1161`
- Sophisticated Core `1.18.2-0.6.4.604`
- Modular Routers `9.1.2`
- Artifacts, AE2 Wireless Terminals, and Mob Grinding Utils where their target methods match the installed 1.18.2 build

All integrations remain optional. Simple Magnets and Item Collectors have existing port coverage; the new hooks are experimental and published as alpha support.

This branch derives from the shared 1.18–1.19 Forge core. It does not include Sable, moving sub-level support, filtered-region behavior, custom region entities, Ender IO integration, or complete 1.21.1 parity.
