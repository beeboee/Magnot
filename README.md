# Magnot — Forge 1.12.2 legacy alpha

This is a standalone legacy core/API port for Minecraft 1.12.2 and Forge 14.23.5.2860.

## Important limitation

Create did not exist for Minecraft 1.12.2. This branch therefore uses vanilla crafting recipes and does not claim feature parity with modern Magnot. It provides region creation, persistence, removal, and an item-pull API for 1.12.2 compatibility integrations to call.

## Researched integration ecosystem

Matching 1.12.2 releases exist for Mekanism `9.8.3.390`, Draconic Evolution `2.3.28.354`, Reliquary `1.12.2-1.3.4.796`, Actually Additions `1.12.2-r152`, Item Collectors `1.1.12-forge-mc1.12`, Simple Magnets `1.1.12-forge-mc1.12`, Industrial Foregoing `1.12.13-237`, and Ender IO `5.3.72`.

Those versions are documented for future integration work, but this alpha does **not** claim built-in support for them. The legacy branch has no Mixin bootstrap, and adding untested reflection or coremod hooks would create more crash risk than useful compatibility. A 1.12.2 magnet or vacuum mod must call `MagnotApi`, or receive a dedicated tested integration in a later alpha.
