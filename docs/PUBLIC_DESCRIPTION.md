# Magnot

Magnot lets players define ferrous regions that stop supported magnets, vacuums, and remote item collectors from pulling dropped items across protected boundaries.

A magnet can still work normally on its own side of a wall. Magnot only blocks a supported pull when its path enters, leaves, or passes through a protected region. It does not delete items, disable magnets globally, or change normal vanilla item pickup.

Use it to protect storage rooms, machine areas, farms, contraptions, item-processing lines, or any other space where remote collection should respect physical boundaries.

## Using Magnot

1. Craft ferrous paste and a ferrous tube.
2. Right-click two corners with the tube to define a region.
3. Hold the tube to reveal nearby regions.
4. Attack a highlighted region with the tube to remove it.
5. Use supported magnets and vacuum blocks normally.

Recipes adapt to the material systems available in each supported environment. Every build should provide a usable recipe path without requiring an optional content mod.

## Installation

Choose the file that matches your exact Minecraft version and mod loader. Install Magnot on the server and on connecting clients unless the notes for that version explicitly say otherwise.

Create is not a universal requirement. Versions with a supported Create integration can use it as an optional presentation backend, while versions without Create use Magnot's native selection and rendering system.

The individual file page or changelog is authoritative for:

- required loader and Java versions
- client and server installation requirements
- supported optional mods
- known limitations
- current testing status

## General compatibility

Magnot's region system works independently of optional integrations, but each third-party magnet, vacuum, hopper, collector, or item teleporter needs either a Magnot adapter or direct support through Magnot's public API.

Compatibility claims apply only to the exact Minecraft version, loader, Magnot version, and target-mod build that were tested. Support on one version should not be assumed on another.

Mod authors can add direct support through the [public compatibility API](API.md). The maintained technical matrix is available in [magnet and vacuum compatibility](COMPATIBILITY.md).

## Versions and compatibility

### Minecraft 1.21.1 — NeoForge

**Status:** Current stable 1.2.x line

- NeoForge 21.1.230 or newer
- Java 21
- Install on both the server and connecting clients
- Create is optional
- Supported Create range: 6.0.10 through the 6.0.x line

With Create installed, Magnot uses its Create/Catnip-backed selection presentation while keeping Magnot authoritative for regions, networking, limits, persistence, removal, and gameplay rules. Without Create, Magnot uses its native backend with the same gameplay behavior and Magnot visuals.

Recipes adapt to available common-tag iron dust and plates. When those materials are unavailable, Magnot enables an appropriate fallback recipe and hides inactive fallback materials.

Confirmed integrations in the current 1.21.1 line include:

- Sophisticated Backpacks and Sophisticated Storage magnet upgrades
- Applied Energistics 2 Wireless Terminals through AE2WTLib
- ProjectE Black Hole Band
- Artifacts Magnetism and Universal Attractor behavior
- Mekanism Magnetic Attraction Unit
- Draconic Evolution Magnet and Advanced Magnet
- Reliquary Reincarnations Fortune Coin and pedestal pickup
- Mob Grinding Utils Absorption Hopper
- Item Collectors by SuperMartijn642
- Simple Magnets by SuperMartijn642
- Modular Routers Vacuum Module
- Ender IO Electromagnet and Vacuum Chest

Actually Additions and Industrial Foregoing have source- or bytecode-verified hooks but retain a lower-confidence label until their exact release combinations receive another focused in-game regression test. Sable support is version-sensitive and should be tested with the exact Sable build used by a pack.

### Minecraft 1.20.1

**Status:** Multi-loader alpha and active port work

- Forge: public 1.2.0 alpha line
- Fabric: public 1.2.0 alpha line
- NeoForge: active port
- Quilt: validation uses the Fabric artifact unless testing proves a dedicated package is necessary

Create is not required for core support. Optional integrations and their confidence levels are specific to each loader artifact and are listed on the corresponding file page.

### Minecraft 1.19.2 — Forge

**Status:** Public 1.2.0 alpha line

Core gameplay parity, rendering, persistence, recipes, and available third-party integrations are tracked independently from newer versions. A green build or public alpha file does not by itself mean every 1.21.1 integration is present.

### Minecraft 1.18.2 — Forge

**Status:** Public 1.2.0 alpha line

This line follows the same native-first parity policy. Compatibility is limited to mods and stable hooks available for this exact Minecraft and Forge version.

### Minecraft 1.16.5 — Forge

**Status:** Public 1.2.0 alpha line

This line is maintained separately from modern NeoForge builds. Recipes, rendering, networking, and optional integrations use the systems available on 1.16.5 rather than assuming modern APIs.

### Minecraft 1.12.2 — Forge

**Status:** Public 1.2.0 alpha; core and API parity work

The 1.12.2 line uses Forge-era systems such as the Ore Dictionary where appropriate. Modern Create, common-tag, and integration behavior is not assumed to exist on this target.

### Minecraft 1.7.10 — Forge

**Status:** Active port

This is a dedicated legacy target. It should not be treated as released or feature-complete until its own file and changelog say so.

The full target list, parity definition, and branch policy are maintained in [version support and parity](VERSION_SUPPORT.md).

## Reporting problems

Public alpha builds are released to broaden testing. When reporting a problem, include:

- the exact Magnot filename
- Minecraft version and loader
- Java version when relevant
- whether Create was installed
- the magnet, vacuum, hopper, or collector involved
- clear reproduction steps
- the latest log or crash report

Reports are tracked through the Magnot GitHub issue form.
