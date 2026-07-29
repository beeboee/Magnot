# Magnet and vacuum compatibility

This page tracks exact compatibility status. Compatibility does not automatically carry between Minecraft versions or loaders.

Magnot adapters are optional with respect to their target mods: a missing magnet, vacuum, or collector mod must never prevent Magnot from loading. Create does not determine whether the native region system or a compatibility adapter can function.

## Status language

- **Runtime confirmed**: exercised in game with the named target build.
- **Implemented and build green**: the target class and mutation path were source- or jar-audited, the adapter is registered, and the Magnot target builds successfully. This is the status of the new alpha-port adapters until public or local gameplay reports arrive.
- **Not registered**: no exact target distribution or safe interception point has been validated for that target.

## Adapter behavior contract

Magnot filters candidate dropped-item entities before a target system moves, teleports, inserts, or removes them. This means the target mod still owns:

- activation and equipment checks
- ranges and filters
- upgrade levels and blacklists
- energy costs, cooldowns, and durability
- particles and sounds
- inventory fit and insertion rules
- XP-orb behavior
- target-specific output behavior

Player-worn magnets use the player as the pull source. Pedestals, chests, collectors, routers, and vacuum blocks use their block or module position. Unblocked items continue through the original target-mod code unchanged.

## NeoForge 1.21.1 reference line

The stable 1.2.x reference line retains its maintained compatibility set:

- Sophisticated Backpacks / Sophisticated Storage
- AE2 Wireless Terminals through AE2WTLib
- ProjectE Black Hole Band
- Artifacts Universal Attractor / Magnetism behavior
- Mekanism Magnetic Attraction Unit
- Draconic Evolution Magnet / Advanced Magnet
- Reliquary Fortune Coin, including pedestal pickup
- Mob Grinding Utils Absorption Hopper on the pinned verified build
- Item Collectors
- Simple Magnets
- Modular Routers Vacuum Module
- Ender IO Electromagnet and Vacuum Chest

Actually Additions and Industrial Foregoing retain source/bytecode-verified status on the reference line until their exact release combinations receive another focused gameplay pass.

## Minecraft 1.20.1 Forge — alpha 4

Implemented and build green:

- Simple Magnets
- Item Collectors
- Sophisticated Backpacks / Sophisticated Storage
- AE2 Wireless Terminals
- ProjectE Black Hole Band:
  - inventory magnet
  - alchemical bag
  - pedestal
  - alchemical chest
- Artifacts Universal Attractor
- Mekanism Magnetic Attraction Unit
- Draconic Evolution magnets
- Reliquary Fortune Coin, including pedestal pickup
- Modular Routers Vacuum Module
- Ender IO Electromagnet and Vacuum Chest
- Industrial Foregoing Infinity Backpack

Mob Grinding Utils is not registered because an exact 1.20.1 distribution and stable target hook have not been validated.

## Minecraft 1.20.1 NeoForge — alpha 4

Uses the same reviewed 1.20.1 compatibility source as Forge, built and packaged against the final NeoForge 1.20.1 line:

- Simple Magnets
- Item Collectors
- Sophisticated Backpacks / Sophisticated Storage
- AE2 Wireless Terminals
- ProjectE Black Hole Band in inventory, bag, pedestal, and chest modes
- Artifacts Universal Attractor
- Mekanism Magnetic Attraction Unit
- Draconic Evolution magnets
- Reliquary Fortune Coin
- Modular Routers Vacuum Module
- Ender IO Electromagnet and Vacuum Chest
- Industrial Foregoing Infinity Backpack

## Minecraft 1.20.1 Fabric / Quilt — alpha 4

The Fabric artifact, also used for Quilt validation, implements the target mods available on that loader family:

- Simple Magnets
- Item Collectors
- Sophisticated Backpacks / Sophisticated Storage through the maintained Fabric Sophisticated Core port
- AE2 Wireless Terminals
- Artifacts Universal Attractor

Forge-only systems are not advertised on this artifact merely because similarly named projects exist.

## Minecraft 1.19.2 Forge — alpha 4

Implemented and build green:

- Simple Magnets
- Item Collectors
- AE2 Wireless Terminals
- ProjectE Black Hole Band in inventory, bag, pedestal, and chest modes
- Artifacts Universal Attractor
- Mekanism Magnetic Attraction Unit
- Reliquary Fortune Coin, including pedestal pickup
- Modular Routers Vacuum Module
- Sophisticated Backpacks / Sophisticated Storage
- Industrial Foregoing Infinity Backpack

## Minecraft 1.18.2 Forge — alpha 4

Implemented and build green:

- Simple Magnets
- Item Collectors
- AE2 Wireless Terminals
- ProjectE Black Hole Band in inventory, bag, pedestal, and chest modes
- Mekanism Magnetic Attraction Unit
- Draconic Evolution magnets
- Reliquary Fortune Coin, including pedestal pickup
- Modular Routers Vacuum Module
- Sophisticated Backpacks / Sophisticated Storage
- Industrial Foregoing Infinity Backpack

The selected Artifacts 1.18.2 line does not expose the same verified Universal Attractor target used by later versions.

## Minecraft 1.16.5 Forge — alpha 4

Implemented and build green:

- Simple Magnets
- Item Collectors
- ProjectE Black Hole Band in inventory, bag, pedestal, and chest modes
- Modular Routers Vacuum Module
- Mekanism Magnetic Attraction Unit
- Draconic Evolution magnets
- Reliquary Fortune Coin
- Industrial Foregoing Infinity Backpack

The selected ecosystem does not provide the copied Sophisticated Core or Mob Grinding Utils targets, so those adapters are not registered.

## Minecraft 1.12.2 Forge — alpha 3

Requires MixinBooter 10.7 or newer for optional target-mod interception.

Implemented and build green:

- Simple Magnets 1.1.12:
  - filters the item list before teleportation
  - also protects the ItemPhysic-assisted direct-pickup path
- Item Collectors 1.1.12:
  - filters before insertion and entity removal
- Draconic Evolution 2.3.28.354:
  - filters ordinary dropped items before motion reset or teleportation
  - Draconic loot cores retain their original special behavior

Other 1.12.2 systems are not claimed from modern class names. Ender IO, ProjectE, Reliquary, Actually Additions, and other legacy candidates require their own exact release-jar audits before registration.

## Minecraft 1.7.10 Forge — alpha 4

Requires MixinBooterLegacy 1.2.1 or newer for optional target-mod interception.

Implemented and build green:

- Ender IO 2.2.4.309 Item Magnet:
  - filters ordinary item entities before movement or direct pickup
  - XP attraction and normal energy drain remain native
- Botania r1.8-247 Ring of Magnetization and Greater Ring:
  - filters before Botania applies item motion
  - Solegnolia checks, item blacklists, cooldowns, and effects remain native
- OpenBlocks 1.6 Vacuum Hopper:
  - rejects blocked item entities through the hopper's own entity selector
  - XP orbs, item projectiles, inventory fit, and output behavior remain native

Other 1.7.10 magnets and collectors are not claimed until their exact jars and mutation paths are audited.

## Direct API support

Mods can avoid mixins entirely by calling the public compatibility API before moving an item. See [API.md](API.md). The API uses the same authoritative region data and path rules as all built-in adapters.

## Reporting compatibility problems

Include:

- Minecraft version
- loader and loader version
- exact Magnot filename
- exact target-mod filename
- item, block, module, or pedestal used
- whether the item was moved, teleported, inserted, or deleted
- the source and item positions relative to the ferrous region
- latest client and server logs
