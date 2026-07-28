# Changelog

## 1.2.0 — 2026-07-28

### Added

- A complete native ferrous-selection renderer so Magnot runs without Create.
- Create-backed rendering when Create is installed, while preserving Magnot's texture and authoritative gameplay behavior.
- One-way outside face visibility, full inside visibility, emphasized selected borders, immediate selection, and quick deselection fading.
- Adaptive iron-dust and iron-plate recipes with a no-Create nugget fallback.
- Conditional creative-tab, JEI, and EMI visibility for Magnot's fallback iron dust.
- Optional Sable transformed-region rendering.
- Clean and compatibility-specific development run directories.
- Integration-disable switches for isolating compatibility adapters.
- Expanded automated build, recipe, architecture, and dedicated-server coverage.
- Version-porting policy that does not depend on Create availability.

### Changed

- Create, Ponder, Flywheel, and Registrate are no longer required runtime dependencies.
- Selection input and rendering now have one authoritative Magnot event owner and exactly one active backend.
- Optional API references are isolated behind compatibility packages.
- Public documentation now distinguishes current, confirmed, and planned version support.

### Fixed

- Native face z-fighting and floor occlusion.
- Double-processing risks between Create-backed and native selection paths.
- Inactive fallback recipes and dust appearing when external common-tag materials are available.
- Development test jars leaking into clean Magnot-only launches.
