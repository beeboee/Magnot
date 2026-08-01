# Forge 1.20.1 parity work

Branch: `version/1.20.1-forge`

Baseline: the existing Forge 1.20.1 alpha.2 port, compared against Magnot 1.2.0 for NeoForge 1.21.1.

Create is not a prerequisite. The first parity milestone is a Magnot-only Forge 1.20.1 client and server using the native selection backend.

## Phase 1 — Recover the port baseline

- Keep the existing ForgeGradle 6, Java 17, official mappings, storage, item, and integration scaffolding.
- Fix the known compiler errors and establish a green Magnot-only build.
- Add clean client and dedicated-server smoke tests before optional mods.

## Phase 2 — Core 1.2 parity

- Preserve region serialization, ownership, limits, creation, removal, and path-intersection semantics.
- Port the native renderer: immediate selection, quick fade-out, one-way outside faces, full inside visibility, thick selected borders, and safe face offsets.
- Port adaptive recipes using Forge common tags and hide dormant fallback dust.
- Preserve the public API semantics and integration-disable controls.

## Phase 3 — Optional integrations

- Add Create-backed presentation only after the native path is complete and only when the exact Create 1.20.1 API is stable.
- Revalidate JEI and each magnet/vacuum integration against exact Forge 1.20.1 builds.
- Treat Create, JEI, and every integration as optional.

## Release gate

- Magnot-only client and dedicated server pass.
- Core gameplay matches `docs/VERSION_SUPPORT.md` from the 1.2.0 release.
- Optional integrations are labeled confirmed or unverified individually.
- Existing alpha users receive an explicit migration note.
