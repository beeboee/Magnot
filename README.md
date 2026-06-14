# Magnot

Magnot adds Create-style ferrous paste regions that block supported magnet and vacuum effects from pulling dropped items through protected areas.

Build a protected region, paste it with ferrous paste, and item magnets should respect it. The goal is simple: magnets can still be useful, but they should not pull items through walls, machines, contraptions, storage rooms, or other areas you deliberately shielded.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.230 or newer
- Create 6.0.10 or newer, below Create 6.1

## What it does

Magnot blocks supported remote item movement when the pull path crosses a ferrous paste region.

That includes player magnets, backpack magnets, armor/module magnets, vacuum blocks, absorption hoppers, item collectors, and similar effects when a compatibility layer exists.

Magnot does **not** delete items, globally disable magnets, or stop normal vanilla item pickup. It only blocks supported remote pulls that would cross a protected region.

## Basic use

1. Build the area you want to protect.
2. Apply ferrous paste to mark the protected region.
3. Use magnets or vacuum blocks nearby as normal.
4. Items inside the protected region should stay inside unless the pull source is also allowed by the region check.

## Supported magnet and vacuum integrations

Currently implemented or actively tested:

- Sophisticated Backpacks / Sophisticated Storage magnet upgrades
- Applied Energistics 2 Wireless Terminals through AE2WTLib
- ProjectE Black Hole Band
- Artifacts Magnetism effect
- Mekanism Magnetic Attraction Unit
- Draconic Evolution Magnet / Advanced Magnet
- Reliquary Reincarnations Fortune Coin
- Actually Additions magnet behavior
- Mob Grinding Utils Absorption Hopper
- Item Collectors by SuperMartijn642
- Simple Magnets by SuperMartijn642
- Modular Routers Vacuum Module
- Ender IO Electromagnet and Vacuum Chest

Compatibility is optional. You do not need all of these mods installed.

See [magnet and vacuum compatibility](docs/COMPATIBILITY.md) for the current support roadmap.

## Modpack notes

Magnot is intended to be pack-friendly. It should be safe to add to packs that already use supported magnet or vacuum mods, but compatibility depends on the exact mod versions in the pack.

If a supported magnet still pulls through a protected region, please report the exact mod name, version, item/block used, and whether the pull was player-based or block-based.

## For mod authors

Mods with magnets, vacuums, remote item collectors, absorption hoppers, item teleporters, or similar item-moving behavior can support Magnot directly through the public compatibility API.

See [the public compatibility API](docs/API.md).

## License

MIT
