# Version support and parity

Magnot's ports are not gated by Create.

The native selection backend, region storage, networking, recipes, magnet-path rules, and public API are the required baseline for every supported target. Optional integrations are added only when the corresponding mod exists for that Minecraft version and exposes a viable hook.

## Priority targets

| Priority | Minecraft | Loader | Delivery model | Status |
|---|---|---|---|---|
| Reference | 1.21.1 | NeoForge | Dedicated build | Current stable 1.2.x line |
| Paramount | 1.20.1 | Forge | Dedicated build | Public 1.2.0 alpha line |
| Paramount | 1.20.1 | NeoForge | Dedicated build | Active port |
| Paramount | 1.20.1 | Fabric | Dedicated build | Public 1.2.0 alpha line |
| Paramount | 1.20.1 | Quilt | Fabric-compatible artifact plus Quilt validation | Active validation |
| Paramount | 1.12.2 | Forge | Dedicated legacy build | Public 1.2.0 alpha; core/API parity work |
| Paramount | 1.7.10 | Forge | Dedicated legacy build | Active new port |
| Secondary | 1.19.2 | Forge | Dedicated build | Public 1.2.0 alpha line |
| Secondary | 1.18.2 | Forge | Dedicated build | Public 1.2.0 alpha line |
| Secondary | 1.16.5 | Forge | Dedicated build | Public 1.2.0 alpha line |

## What “all loaders” means

Magnot targets every practical loader maintained for a priority Minecraft version:

- Minecraft 1.20.1: Forge, NeoForge, Fabric, and Quilt compatibility.
- Minecraft 1.12.2: Forge. Official Fabric support begins with Minecraft 1.14; the old pre-1.14 compatibility route is unmaintained and is not treated as a production loader target.
- Minecraft 1.7.10: Forge. This is the established general-purpose loader and modding API for the target.

Quilt Loader is designed to load Fabric mods, so the 1.20.1 Quilt target uses the Fabric artifact unless testing proves a dedicated Quilt package is required. This avoids publishing duplicate jars with identical code while still requiring Quilt-specific launch and gameplay validation.

A loader is not added merely because an experimental or abandoned bootstrapping project once existed. New loader targets require a reproducible toolchain, a package format users can install, and enough runtime support to provide Magnot's complete gameplay model safely.

## Definition of full compatibility

A target is not called feature-complete until it has:

- persistent ferrous regions with equivalent creation, limits, ownership, removal, and migration behavior
- a native renderer that works without Create
- immediate selection and quick deselection fade
- equivalent magnet-path intersection and blocking rules
- a usable recipe path without optional mods
- adaptive common-tag, Ore Dictionary, or loader-native materials where available
- hidden or absent fallback materials when inactive
- client and dedicated-server startup coverage
- save/reload and reconnect coverage
- a documented compatibility matrix for magnets, vacuums, and remote collectors available on that target
- matching public API semantics, even when loader-specific event wiring differs
- per-loader packaging metadata and dependency declarations that match the actual artifact

Create-backed rendering, JEI/EMI equivalents, moving-structure integrations, and third-party magnet adapters are target-specific. Their absence cannot break the core region system, and their support is stated on the individual file rather than assumed from the project description.

## Port order

1. Maintain NeoForge 1.21.1 as the reference implementation.
2. Complete Forge, NeoForge, Fabric, and Quilt validation for Minecraft 1.20.1.
3. Complete Forge 1.12.2 core, API, rendering, recipes, persistence, and available integrations.
4. Establish and complete the Forge 1.7.10 port.
5. Continue the secondary Forge 1.19.2, 1.18.2, and 1.16.5 lines.
6. Reintroduce and verify optional integrations target by target.

Public alpha builds broaden testing, but a green compile or public file does not by itself mean full gameplay parity.

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

Shared behavior changes should be implemented in the reference branch first and then backported deliberately. Loader-specific code stays behind adapters so region behavior remains comparable.

## Compatibility claims

A mod integration is listed as confirmed only after its exact Minecraft version, loader, and target-mod build have been verified. Compile-only, source-audited, or bytecode-audited hooks are labeled separately until runtime reports confirm them.