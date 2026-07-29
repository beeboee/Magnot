# Version support and parity

Magnot's ports are not gated by Create.

The native selection backend, region storage, networking, recipes, magnet-path rules, and public API are the required Magnot-owned baseline for every supported target. Third-party adapters, Sable, moving structures, and Create-backed presentation are separate compatibility layers and are not part of the core-parity claim.

## Priority targets

| Priority | Minecraft | Loader | Delivery model | Current 1.2.0 alpha |
|---|---|---|---|---|
| Reference | 1.21.1 | NeoForge | Dedicated build | Stable 1.2.x reference |
| Paramount | 1.20.1 | Forge | Dedicated build | `alpha.2`; core-parity build green |
| Paramount | 1.20.1 | NeoForge | Dedicated build | `alpha.2`; core-parity build green |
| Paramount | 1.20.1 | Fabric | Dedicated build | `alpha.3`; core-parity build green |
| Paramount | 1.20.1 | Quilt | Fabric-compatible artifact plus Quilt validation | `alpha.3`; shared Fabric artifact |
| Paramount | 1.12.2 | Forge | Dedicated legacy build | `alpha.2`; core-parity build green |
| Paramount | 1.7.10 | Forge | Dedicated legacy build | `alpha.3`; core-parity build green |
| Secondary | 1.19.2 | Forge | Dedicated build | `alpha.2`; core-parity build green |
| Secondary | 1.18.2 | Forge | Shared core plus version-specific shims | `alpha.2`; core-parity build green |
| Secondary | 1.16.5 | Forge | Dedicated Java 8 build | `alpha.2`; core-parity build green |

## Alpha naming

Prerelease counters are maintained per target. A target advances only when its own packaged behavior changes; one loader reaching `alpha.3` does not force unrelated targets to use the same counter.

Canonical forms are:

- project version: `1.2.0-alpha.<revision>+mc<minecraft>-<loader>`
- filename: `magnot-1.2.0-alpha.<revision>-mc<minecraft>-<loader>.jar`
- display name: `Magnot 1.2.0 alpha <revision> - <Loader> <Minecraft>`

Release notes carry the exact alpha number. Long-lived branch documentation describes current capabilities rather than using headings such as “Alpha 2 integration coverage.”

## What “all loaders” means

Magnot targets every practical loader maintained for a priority Minecraft version:

- Minecraft 1.20.1: Forge, NeoForge, Fabric, and Quilt compatibility.
- Minecraft 1.12.2: Forge. Official Fabric support begins with Minecraft 1.14; the old pre-1.14 compatibility route is unmaintained and is not treated as a production loader target.
- Minecraft 1.7.10: Forge. This is the established general-purpose loader and modding API for the target.

Quilt Loader is designed to load Fabric mods, so the 1.20.1 Quilt target uses the Fabric artifact unless testing proves a dedicated Quilt package is required. This avoids publishing duplicate jars with identical code while still requiring Quilt-specific launch and gameplay validation.

A loader is not added merely because an experimental or abandoned bootstrapping project once existed. New loader targets require a reproducible toolchain, a package format users can install, and enough runtime support to provide Magnot's complete gameplay model safely.

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

Legacy targets may use version-appropriate equivalents where vanilla lacks a modern material. Minecraft 1.7.10 has no vanilla iron nuggets, so its fallback recipe uses one iron ingot and one slime ball for one ferrous paste.

## Runtime certification

A green compile proves that the target toolchain, mappings, resources, and packaging agree. It does not by itself certify gameplay.

Stable status additionally requires target-specific checks for:

- client and dedicated-server startup
- placement, preview, removal, and durability behavior
- save/reload, reconnect, and dimension changes
- multiplayer synchronization
- recipe truth-table behavior with and without available materials
- rendering from inside and outside regions
- path blocking under representative item-pull patterns

## Optional compatibility layers

The following are tracked independently from core parity:

- third-party magnet, vacuum, collector, and remote-item adapters
- Sable and moving-structure behavior
- Create-backed rendering, sounds, particles, or selection hooks
- JEI, EMI, and version-equivalent recipe-viewer handling

Their absence cannot break the native region system. Support is stated per target and per file rather than inherited from the project-wide core-parity status.

## Current port order

1. Maintain NeoForge 1.21.1 as the reference implementation.
2. Runtime-validate placement, rendering, persistence, synchronization, recipes, and path blocking across all build-green alpha targets.
3. Promote targets individually when their loader- and version-specific runtime checks pass.
4. Reintroduce and verify optional integrations target by target without changing the universal native baseline.

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

A third-party integration is listed as confirmed only after its exact Minecraft version, loader, and target-mod build have been verified. Compile-only, source-audited, or bytecode-audited hooks are labeled separately until runtime reports confirm them.
