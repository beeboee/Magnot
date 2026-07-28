# Version support and parity

Magnot's version ports are not gated by Create.

The native selection backend, Magnot region storage, networking, recipes, and public API are the required baseline for every supported Minecraft version. Create support is an optional enhancement added only where a compatible Create build exists.

## Active release lines

| Minecraft | Loader | Status | Magnot line |
|---|---|---|---|
| 1.21.1 | NeoForge | Current release | 1.2.x |
| 1.20.1 | Forge | First parity target | planned from existing alpha track |
| 1.20.1 | Fabric | First parity target | planned from existing alpha track |
| 1.19.2 | Forge | Second parity target | planned |
| 1.18.2 | Forge | Legacy parity target | queued |
| 1.16.5 | Forge | Legacy parity target | queued |

Additional targets can be added when there is a maintained loader toolchain and enough users to justify testing them.

## Definition of parity

A port is not called feature-complete until it has:

- persistent ferrous regions with equivalent creation, limits, ownership, and removal
- a native renderer that works without Create
- immediate selection and quick deselection fade
- equivalent magnet-path blocking rules
- adaptive recipes using the target loader's common tags where available
- hidden or absent fallback materials when they are inactive
- dedicated-server startup coverage
- a documented compatibility matrix for magnet and vacuum integrations available on that target
- matching public API semantics, even if loader-specific event wiring differs

Create-backed rendering, JEI/EMI equivalents, Sable-style moving structures, and third-party magnet integrations are added per target when those mods exist and expose a stable hook. Missing optional mods do not block the core port.

## Port order

1. Stabilize and publish NeoForge 1.21.1 as Magnot 1.2.0.
2. Port the native/core path to Forge 1.20.1.
3. Port the same behavior to Fabric 1.20.1 with loader-specific adapters.
4. Reintroduce optional integrations target by target.
5. Move backward through the existing Forge release tracks.

## Branch policy

Long-lived version branches use:

- `version/1.20.1-forge`
- `version/1.20.1-fabric`
- `version/1.19.2-forge`

Shared behavior changes should be implemented in the newest maintained branch first, then backported deliberately. Loader-specific code should remain behind adapters so the region model and gameplay rules stay comparable.

## Compatibility claims

A mod integration is listed as confirmed only after its exact target build has been source-verified and tested in game. Compile-only or bytecode-verified hooks are documented separately until manual regression testing is complete.
