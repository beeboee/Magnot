# Version support and parity

Magnot's ports are not gated by Create.

The native selection backend, region storage, networking, recipes, magnet-path rules, and public API are the required Magnot-owned baseline for every supported target. Third-party adapters, Sable, moving structures, and Create-backed presentation are separate compatibility layers and do not alter the native core.

## Priority targets

| Priority | Minecraft | Loader | Delivery model | Current 1.2.0 alpha |
|---|---|---|---|---|
| Reference | 1.21.1 | NeoForge | Dedicated build | Stable 1.2.x reference |
| Paramount | 1.20.1 | Forge | Dedicated build | `alpha.4`; core and compatibility build green |
| Paramount | 1.20.1 | NeoForge | Dedicated build | `alpha.4`; shared 1.20.1 compatibility layer build green |
| Paramount | 1.20.1 | Fabric | Dedicated build | `alpha.4`; core and Fabric compatibility build green |
| Paramount | 1.20.1 | Quilt | Fabric-compatible artifact plus Quilt validation | `alpha.4`; shared Fabric artifact |
| Paramount | 1.12.2 | Forge | Dedicated legacy build | `alpha.3`; core and legacy compatibility build green |
| Paramount | 1.7.10 | Forge | Dedicated legacy build | `alpha.4`; core and legacy compatibility build green |
| Secondary | 1.19.2 | Forge | Dedicated build | `alpha.4`; core and compatibility build green |
| Secondary | 1.18.2 | Forge | Shared core plus version-specific shims | `alpha.4`; core and compatibility build green |
| Secondary | 1.16.5 | Forge | Dedicated Java 8 build | `alpha.4`; core and compatibility build green |

## Alpha naming

Prerelease counters are maintained per target. A target advances only when its own packaged behavior changes; one loader reaching `alpha.4` does not force unrelated targets to use the same counter.

Canonical forms are:

- project version: `1.2.0-alpha.<revision>+mc<minecraft>-<loader>`
- filename: `magnot-1.2.0-alpha.<revision>-mc<minecraft>-<loader>.jar`
- display name: `Magnot 1.2.0 alpha <revision> - <Loader> <Minecraft>`

Release notes carry the exact alpha number. Long-lived branch documentation describes current capabilities rather than tying capability headings to one prerelease number.

## What “all loaders” means

Magnot targets every practical loader maintained for a priority Minecraft version:

- Minecraft 1.20.1: Forge, NeoForge, Fabric, and Quilt compatibility.
- Minecraft 1.12.2: Forge. MixinBooter supplies the optional-mod interception layer.
- Minecraft 1.7.10: Forge. MixinBooterLegacy supplies the optional-mod interception layer.

Quilt Loader uses the Fabric 1.20.1 artifact unless testing proves a dedicated Quilt package is required. This avoids duplicate jars while retaining Quilt-specific launch and gameplay validation.

## Definition of core parity

A target has Magnot-owned core parity when it contains:

- persistent ferrous regions with stable IDs and migration-safe loading
- equivalent creation, 25-block axis limits, cancellation, durability, and ray-selected removal
- synchronized client state on login, reconnect, and dimension changes
- a native Magnot renderer that works without Create
- nearby passive outlines, selected highlighting, placement preview, limit warning, and quick deselection fade
- equivalent point containment, segment intersection, and magnet-path blocking rules
- indexed lookup and per-tick caching appropriate to the target APIs
- adaptive common-tag, Ore Dictionary, or loader-native iron-dust and iron-plate recipes
- a usable vanilla-material fallback path without optional mods
- hidden or dormant fallback materials when inactive
- matching public API semantics, even when loader-specific event wiring differs
- per-loader packaging metadata and a green target build gate

Minecraft 1.7.10 has no vanilla iron nuggets, so its fallback recipe uses one iron ingot and one slime ball for one ferrous paste.

## Compatibility-adapter standard

A compatibility adapter must preserve the target mod's ordinary behavior while excluding blocked item entities before mutation. The preferred order is:

1. filter the target mod's candidate entity list before movement, teleportation, insertion, or removal;
2. cancel a one-item helper at method entry when no stable candidate-list hook exists;
3. avoid final movement-call interception unless no earlier stable hook is available.

Player magnets use the player as the pull source. Block collectors, routers, pedestals, chests, and vacuum blocks use the block or module position. Target-mod blacklists, filters, upgrades, range limits, energy costs, cooldowns, XP handling, particles, fit checks, and output behavior remain native.

Exact per-target adapters are listed in [magnet and vacuum compatibility](COMPATIBILITY.md).

## Runtime certification

A green compile proves that the target toolchain, mappings, resources, mixin configuration, and packaging agree. Stable status additionally requires target-specific checks for:

- client and dedicated-server startup
- placement, preview, removal, and durability behavior
- save/reload, reconnect, and dimension changes
- multiplayer synchronization
- recipe truth-table behavior with and without available materials
- rendering from inside and outside regions
- path blocking under representative item-pull patterns
- each advertised target mod moving unblocked items normally while leaving blocked items untouched

## Optional compatibility layers

The following remain independently versioned:

- third-party magnet, vacuum, collector, and remote-item adapters
- Sable and moving-structure behavior
- Create-backed rendering, sounds, particles, or selection hooks
- JEI, EMI, and version-equivalent recipe-viewer handling

Their absence cannot break the native region system. Support is stated per target rather than inherited across Minecraft versions.

## Current port order

1. Maintain NeoForge 1.21.1 as the reference implementation.
2. Runtime-test the new build-green adapter sets against their exact target-mod files.
3. Add further target mods only after their item-selection and mutation paths are source- or jar-audited.
4. Promote targets individually when loader-, version-, and compatibility-specific runtime checks pass.

## Branch policy

Long-lived priority branches use:

- `version/1.20.1-forge`
- `version/1.20.1-neoforge`
- `version/1.20.1-fabric`
- Quilt validation runs against the 1.20.1 Fabric artifact
- `version/1.12.2-forge`
- `version/1.7.10-forge`

Secondary branches use:

- `version/1.19.2-forge`
- `version/1.18.2-forge`
- `version/1.16.5-forge`

Shared behavior changes should be implemented in the reference branch first and then backported deliberately. Loader-specific code stays behind version adapters or shims so gameplay behavior remains comparable.

## Compatibility claims

A third-party integration is listed as implemented after its exact Minecraft version, loader, target class, and mutation path have been reviewed and its Magnot target builds successfully. Runtime-confirmed status is recorded separately after the matching mod file is exercised in game.
