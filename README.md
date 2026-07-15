# Magnot

Magnot lets ferrous paste act as a boundary for item magnets and vacuum blocks.

Items can sit behind a protected wall without being pulled through it by backpacks, rings, modules, hoppers, or other remote pickup effects. Magnets still work. They just have to mind the walls.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.230 or newer

Create is optional. With Create installed, Magnot uses Create/Catnip selection rendering, raycasting, fade behavior, and glue-style sounds while keeping Magnot authoritative for region creation and removal. Without Create, Magnot uses its native selection backend with the same Magnot texture and gameplay rules.

Sable, JEI, EMI, and all supported magnet integrations are optional.

## What it does

Magnot blocks supported remote item pulls when the pull path crosses a protected ferrous region.

It does not delete items, disable magnets globally, or change normal vanilla item pickup. If a pull is not crossing a protected region, it should behave normally.

## Basic use

1. Craft ferrous paste and a ferrous tube. Recipes adapt to the common iron dust and plate tags available in the pack.
2. Right-click the first and second corners with the tube.
3. Hold the tube to inspect nearby regions, or attack a highlighted region to remove it.
4. Use magnets or vacuum blocks nearby as normal.

## Adaptive materials

- External `c:dusts/iron` present: Magnot consumes external dust and hides its fallback dust.
- No external dust + Create present: Create crushing produces `magnot:iron_dust`, which becomes visible and is used by ferrous paste.
- No external dust + no Create: the dust path disappears and an eight-iron-nugget plus slime fallback recipe is enabled.
- `c:plates/iron` present: the ferrous tube uses a plate or sheet from that tag.
- `c:plates/iron` empty: the tube uses an iron ingot instead.

Inactive recipes never enter the recipe manager. Dormant Magnot iron dust is omitted from the creative tab and hidden from optional JEI and EMI integrations.

## Compatibility

Magnot supports a growing set of magnet and vacuum mods. Compatibility depends on the exact mod and version, so the detailed list lives in [magnet and vacuum compatibility](docs/COMPATIBILITY.md).

If a magnet or vacuum still pulls through a protected region, please report:

- the mod name and version
- the item or block used
- whether the pull came from a player or a block
- what was between the pull source and the item

## For mod authors

Mods with magnets, vacuums, remote item collectors, absorption hoppers, item teleporters, or similar item-moving behavior can support Magnot directly through the [public compatibility API](docs/API.md).

## Development

Optional dev runtimes are explicit Gradle properties:

- `-Pwith_create=true`
- `-Pwith_sable=true`
- `-Pwith_jei=true`
- `-Pwith_emi=true`
- `-Pwith_compat_test_mods=true`

See [the v1.1.0 implementation notes](docs/V1.1.0.md) for architecture, recipe decisions, attribution, test coverage, and known risks.

## License

MIT. Third-party notices are in [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
