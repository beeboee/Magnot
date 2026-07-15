# Magnot — Forge 1.12.2 legacy alpha

This is a standalone legacy core/API port for Minecraft 1.12.2 and Forge 14.23.5.2860.

## Important limitation

Create did not exist for Minecraft 1.12.2. This branch therefore uses vanilla crafting recipes and does not claim feature parity with modern Magnot. It provides region creation, persistence, removal, and an item-pull API for 1.12.2 integrations to call.

## Researched integration ecosystem

Matching releases exist for Mekanism `9.8.3.390`, Draconic Evolution `2.3.28.354`, Reliquary `1.12.2-1.3.4.796`, Actually Additions `1.12.2-r152`, Item Collectors `1.1.12-forge-mc1.12`, Simple Magnets `1.1.12-forge-mc1.12`, Industrial Foregoing `1.12.13-237`, and Ender IO `5.3.72`.

Source and bytecode review identified useful future interception points around the Reliquary fortune coin, Actually Additions magnet ring, Ender IO electromagnet/vacuum logic, and the candidate-entity queries used by several collector mods. They are not registered in this alpha.

The branch has no Mixin bootstrap. A magnet or vacuum mod must currently call `MagnotApi`, or receive a dedicated tested coremod/mixin-bootstrap integration later. Magnot will not add broad reflection or an untested ASM transformer merely to claim a longer compatibility list.
