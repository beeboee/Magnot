# Fabric 1.20.1 parity work

Branch: `version/1.20.1-fabric`

Baseline: the existing Fabric 1.20.1 alpha.2 port, compared against Magnot 1.2.0 for NeoForge 1.21.1.

Create is not a prerequisite. The first parity milestone is a Magnot-only Fabric 1.20.1 client and server using the native selection backend.

## Phase 1 — Recover the port baseline

- Keep the existing Loom, Java 17, Fabric API, storage, item, and verified integration scaffolding.
- Establish a green Magnot-only build and dedicated-server smoke test.
- Separate loader-neutral region rules from Fabric event, networking, persistence, recipe, and rendering adapters.

## Phase 2 — Core 1.2 parity

- Preserve region serialization, ownership, limits, creation, removal, and path-intersection semantics.
- Port the native renderer: immediate selection, quick fade-out, one-way outside faces, full inside visibility, thick selected borders, and safe face offsets.
- Implement adaptive recipes with Fabric/Common tags and hide dormant fallback dust.
- Preserve the public compatibility API semantics.

## Phase 3 — Optional integrations

- Add Fabric Create-backed presentation only if the exact ecosystem exposes stable equivalent hooks.
- Revalidate EMI/REI/JEI availability rather than copying the NeoForge viewer path.
- Audit Fabric magnet and vacuum mods independently; Forge or NeoForge targets do not count as confirmation.

## Release gate

- Magnot-only client and dedicated server pass.
- Core gameplay matches `docs/VERSION_SUPPORT.md` from the 1.2.0 release.
- Optional integrations are labeled confirmed or unverified individually.
- Existing alpha users receive an explicit migration note.
