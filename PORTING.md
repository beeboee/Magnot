# Forge 1.18.2 parity work

Branch: `version/1.18.2-forge`

Baseline: the published `1.0.0-alpha.2` Forge 1.18.2 port. This branch begins the `1.2.0-alpha.1` parity line.

Create is optional. The first feature-complete milestone is a Magnot-only client and dedicated server using Magnot's native selection backend.

## Existing architecture note

The current 1.18.2 build reuses the `ports/forge-1.19.2` source tree. That shared source must be audited against 1.18.2 mappings and behavior before parity is claimed. Split version-specific adapters where APIs or serialization differ.

## Core parity

- Preserve region creation, limits, ownership, persistence, removal, and pull-path intersection rules.
- Port the native renderer and Magnot ferrous texture without requiring Create.
- Match immediate selection, quick fade, exterior/interior face behavior, and selected-border emphasis.
- Port adaptive recipes using Forge tags and hide inactive fallback dust.
- Keep public API semantics aligned with NeoForge 1.2.0.

## Optional integrations

- Add Create-backed presentation only after the native path passes independently.
- Revalidate JEI and magnet/vacuum adapters against exact 1.18.2 builds.
- Missing optional integrations do not block the core release.

## Release gate

- Clean Magnot-only build.
- Client and dedicated-server smoke tests.
- Native region creation, rendering, persistence, removal, and magnet blocking tested in game.
- Shared-source assumptions audited or replaced with version-specific adapters.
- Every advertised integration labeled confirmed or unverified for this target.
