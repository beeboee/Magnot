# Magnot

Magnot adds ferrous paste regions that block supported magnets, vacuums, and remote item collectors from pulling dropped items into, out of, or through protected areas.

Magnets still work normally outside those boundaries. Protect storage rooms, machines, contraptions, farms, and item-processing areas without disabling magnet effects globally.

## Requirements for Magnot 1.2.0

- Minecraft 1.21.1
- NeoForge 21.1.230 or newer
- Magnot installed on the server and connecting clients

**Create is optional.**

When Create is installed, Magnot reuses Create/Catnip selection rendering and glue-style presentation where practical. Without Create, Magnot uses its own native renderer with the same ferrous texture and gameplay result.

## Using ferrous regions

1. Craft ferrous paste and a ferrous tube.
2. Right-click two corners with the tube to define a region.
3. Hold the tube to reveal nearby regions.
4. Attack a highlighted region with the tube to remove it.

Magnot automatically adapts its recipes to common iron dust and plate tags. Packs without Create or iron dust still receive a nugget-and-slime fallback recipe.

## Optional compatibility

Magnot includes optional adapters for supported magnet and vacuum behavior from mods including:

- Sophisticated Backpacks and Sophisticated Storage
- Applied Energistics 2 Wireless Terminals / AE2WTLib
- ProjectE
- Artifacts
- Mekanism
- Draconic Evolution
- Reliquary Reincarnations
- Actually Additions
- Mob Grinding Utils
- Item Collectors
- Simple Magnets
- Modular Routers
- Ender IO
- Industrial Foregoing

Sable moving structures, JEI, EMI, Create, and every listed magnet integration remain optional. Exact tested versions and confidence levels are maintained in the repository compatibility document.

## Version ports

NeoForge 1.21.1 is the current release line. Forge and Fabric 1.20.1 parity work comes next, followed by the older published Forge tracks. Create availability does not gate those ports; the native backend is the baseline.

## Feedback

Magnot is young and compatibility reports are useful. Include the target mod version, magnet or collector used, whether the source was a player or block, and the protected geometry involved.
