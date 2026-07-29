# Forge 1.19.2 parity work

Branch: `version/1.19.2-forge`

Baseline: the published `1.0.0-alpha.2` Forge 1.19.2 port. This branch begins the `1.2.0-alpha.1` parity line.

Create is optional. The first feature-complete milestone is a Magnot-only client and dedicated server using Magnot's native selection backend.

## Core parity

- Preserve region creation, limits, ownership, persistence, removal, and pull-path intersection rules.
- Port the native renderer with immediate selection, quick deselection fade, one-way exterior faces, full interior visibility, selected-border emphasis, and Magnot's ferrous texture.
- Port adaptive iron dust and plate recipes using Forge tags.
- Hide or omit fallback iron dust whenever it is inactive.
- Keep the public API behavior aligned with NeoForge 1.2.0.

## Optional integrations

- Add Create-backed presentation only after the native path passes independently.
- Revalidate JEI and every magnet/vacuum adapter against exact 1.19.2 builds.
- Missing optional integrations do not block the core release.

## Release gate

- Clean Magnot-only build.
- Client and dedicated-server smoke tests.
- Native region creation, rendering, persistence, removal, and magnet blocking tested in game.
- Adaptive recipe truth table verified.
- Every advertised integration labeled confirmed or unverified for this exact target.
