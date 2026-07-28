# Forge 1.12.2 legacy parity work

Branch: `version/1.12.2-forge`

Baseline: the published `1.0.0-alpha.2` Forge 1.12.2 core/API-only port. This branch begins the `1.2.0-alpha.1` parity line.

Create did not exist for this target and is not part of the port. The required baseline is Magnot's own region storage, renderer, recipes, and compatibility API.

## Core parity

- Preserve region creation, limits, ownership, persistence, removal, and pull-path intersection behavior within 1.12.2's APIs.
- Implement a native selection renderer with Magnot's ferrous visual identity.
- Match immediate selection and quick deselection as closely as the legacy rendering pipeline permits.
- Adapt recipes to Ore Dictionary materials where available and avoid exposing inactive fallback materials.
- Keep the public compatibility API semantically equivalent where practical.
- Build and run on Java 8.

## Integration policy

This branch remains core/API-only until an automatic integration mechanism is proven safe on 1.12.2. No untested coremod, transformer, or reflection hook will be advertised as supported. Third-party mods may call the public API directly.

## Release gate

- Clean Java 8 / Gradle 4.10.3 build.
- Client and dedicated-server smoke tests.
- Native region creation, rendering, persistence, and removal tested in game.
- API-level magnet blocking tested with a small fixture or cooperating mod.
- Migration notes clearly distinguish core parity from automatic integration coverage.
