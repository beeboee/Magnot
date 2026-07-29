# Forge 1.16.5 parity work

Branch: `version/1.16.5-forge`

Baseline: the published `1.0.0-alpha.2` Forge 1.16.5 port. This branch begins the `1.2.0-alpha.1` parity line.

Create is optional. The native renderer and core region rules are the required baseline; Create 0.3.2g support is an enhancement only.

## Core parity

- Preserve region creation, limits, ownership, persistence, removal, and pull-path intersection behavior.
- Port the native selection renderer using APIs available in 1.16.5 while preserving Magnot's ferrous texture and visual timing.
- Match immediate selection, quick fade, exterior/interior face visibility, and selected-border emphasis as closely as the renderer permits.
- Port adaptive recipes through Forge tags and suppress inactive fallback dust.
- Keep the public API behavior equivalent even where event wiring differs.
- Retain Java 8 bytecode compatibility.

## Optional integrations

- Add Create-backed presentation only after the native path passes without Create.
- Revalidate JEI and legacy magnet/vacuum adapters against exact 1.16.5 builds.
- Missing optional integrations do not block the core release.

## Release gate

- Clean build producing Java 8-compatible output.
- Client and dedicated-server smoke tests.
- Native region creation, rendering, persistence, removal, and magnet blocking tested in game.
- Recipe fallback behavior verified with and without common-tag materials.
- Every advertised integration labeled confirmed or unverified for this target.
