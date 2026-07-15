# Compatibility source review summary

This pass reviewed Magnot's optional integration entry points against upstream source and/or published class bytecode before changing support claims.

## Main 1.21.1

- Added and registered the missing Actually Additions `ItemMagnetRing.inventoryTick` hook.
- Rechecked ProjectE `BlackHoleBand` attraction paths against the real ProjectE repository.
- Rechecked Mob Grinding Utils `TileEntityAbsorptionHopper.getCaptureItems`.
- Moved the default development target to Create 6.0.11 while keeping the supported range at Create 6.0.10–6.0.x.
- Built successfully against both the pinned Create 6.0.11 artifact and the latest available Create 6.0.x artifact.

## Ports

- 1.20.1 Forge/Fabric: replaced the nonexistent old Artifacts effect target with `UniversalAttractorItem`; Forge also uses the actual Ender IO 6.2 package names.
- 1.19.2 Forge: uses the Curios `UniversalAttractorItem` and the plural `reliquary.items` package.
- 1.18.2 Forge: removed the unverified Artifacts claim and corrected Reliquary.
- 1.16.5 Forge: removed unverified Sophisticated Core and Industrial Foregoing hooks; retained exact legacy targets.
- 1.12.2 Forge: remains API-only until a tested mixin/coremod bootstrap is designed.
- All six port branches completed their target Gradle builds after these corrections.

For Forge/Fabric pseudo-mixins, Minecraft invocation targets now opt into remapping even though the third-party target classes themselves remain unremapped.
