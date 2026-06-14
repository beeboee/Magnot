# Magnot

Magnot lets ferrous paste act as a boundary for item magnets and vacuum blocks.

Items can sit behind a protected wall without being pulled through it by backpacks, rings, modules, hoppers, or other remote pickup effects. Magnets still work. They just have to mind the walls.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.230 or newer
- Create 6.0.10 or newer, below Create 6.1

## What it does

Magnot blocks supported remote item pulls when the pull path crosses a protected ferrous region.

It does not delete items, disable magnets globally, or change normal vanilla item pickup. If a pull is not crossing a protected region, it should behave normally.

## Basic use

1. Mark the area you want to protect with ferrous paste.
2. Use magnets or vacuum blocks nearby as normal.
3. Supported item pulls that cross the protected region are blocked.

## Compatibility

Magnot supports a growing set of magnet and vacuum mods. Compatibility depends on the exact mod and version, so the detailed list lives in [magnet and vacuum compatibility](docs/COMPATIBILITY.md).

If a magnet or vacuum still pulls through a protected region, please report:

- the mod name and version
- the item or block used
- whether the pull came from a player or a block
- what was between the pull source and the item

## For mod authors

Mods with magnets, vacuums, remote item collectors, absorption hoppers, item teleporters, or similar item-moving behavior can support Magnot directly through the [public compatibility API](docs/API.md).

## License

MIT
