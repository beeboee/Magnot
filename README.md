# Magnot — Forge 1.16.5 alpha

Core Java 8 port for Minecraft 1.16.5, Forge 36.2.9+, and Create 0.3.2g+.

Included: persistent ferrous regions, tube placement/removal, particle outlines, and the compatibility API.

## Alpha integration coverage

- Simple Magnets `1.1.12-forge-mc1.16`
- Item Collectors `1.1.12-forge-mc1.16`
- Mekanism `10.1.2.457`
- Draconic Evolution `3.0.29.518`
- Reliquary Reincarnations `1.16.5-1.3.5.1124`
- Modular Routers `1.16.5-7.5.4`
- Industrial Foregoing `3.2.14.7`
- Mob Grinding Utils and Sophisticated Core where their target methods match the installed 1.16.5 build

The integrations are optional Java 8 mixins. Simple Magnets has existing port coverage; the other hooks are experimental and are being released as alpha support.

This branch remains intentionally smaller than the 1.21.1 release. It does not include Sable, moving sub-levels, filtered regions, custom region entities, or modern NeoForge-only integrations.
