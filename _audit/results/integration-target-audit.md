# Magnot integration target audit

Generated: 2026-07-15T01:59:15.614631+00:00

This report resolves the newest published file for each exact Minecraft/loader pair and inspects the actual jar bytecode for Magnot's declared mixin target classes.

## Create version matrix

| Target | Latest resolved Create | Source | Published | Current declarations |
|---|---|---|---|---|
| `main-1.21.1-neoforge` | `6.0.10+mc1.21.1` | modrinth | 2026-04-21T22:20:03.579201Z | build.gradle: def createCoordinates = "com.simibubi.create:create-${minecraft_version}:${create_version}"<br>build.gradle: implementation("net.createmod.ponder:ponder-neoforge:${ponder_version}+mc${minecraft_version}")<br>build.gradle: runtimeOnly("maven.modrinth:oWaK0Q19:${create_aeronautics_modrinth_version}")<br>build.gradle: create_version_range    : create_version_range,<br>gradle.properties: create_version=6.0.10-280 |
| `forge-1.20.1` | `mc1.20.1-6.0.8` | modrinth | 2025-11-02T16:10:25.364809Z | ports/forge-1.20.1/src/main/resources/META-INF/mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: versionRange="${create_version_range}" |
| `fabric-1.20.1` | `6.0.8.1+build.1744-mc1.20.1` | modrinth | 2025-12-02T17:58:03.504148Z | src/main/resources/META-INF/neoforge.mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: versionRange="${create_version_range}" |
| `forge-1.19.2` | `1.19.2-0.5.1.i` | modrinth | 2024-10-09T19:41:38.289272Z | ports/forge-1.19.2/src/main/resources/META-INF/mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: versionRange="${create_version_range}" |
| `forge-1.18.2` | `1.18.2-0.5.1.i` | modrinth | 2024-10-09T19:40:39.712712Z | ports/forge-1.19.2/src/main/resources/META-INF/mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: versionRange="${create_version_range}" |
| `forge-1.16.5` | `unresolved` | modrinth | — | ports/forge-1.16.5/src/main/resources/META-INF/mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: versionRange="${create_version_range}" |
| `forge-1.12.2` | `unresolved` | modrinth | — | src/main/resources/META-INF/neoforge.mods.toml: modId="create"<br>src/main/resources/META-INF/neoforge.mods.toml: versionRange="${create_version_range}" |

## main-1.21.1-neoforge

| Integration | Latest resolved version | Target classes | Bytecode result |
|---|---|---:|---|
| AE2 Wireless Terminals | `19.5.0` | 1 | 1/1 target classes found |
| Artifacts | `13.2.1` | 1 | 1/1 target classes found |
| Draconic Evolution | `3.1.4.632` | 1 | 1/1 target classes found |
| Ender IO | `v8.2.11-beta` | 2 | 2/2 target classes found |
| Industrial Foregoing | `1.21-3.6.39` | 1 | 1/1 target classes found |
| Item Collectors | `1.1.10-neoforge-mc1.21` | 1 | 1/1 target classes found |
| Mekanism | `10.7.19.85` | 1 | 1/1 target classes found |
| Mob Grinding Utils | `unresolved` | 1 | unresolved: mob-grinding-utils: HTTP 404 |
| Modular Routers | `13.2.6` | 1 | 1/1 target classes found |
| ProjectE | `1.21.1-1.0.6` | 1 | 0/1 target classes found; missing: `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand` |
| Reliquary Reincarnations | `1.21.1-2.0.77.1537` | 1 | 1/1 target classes found |
| Simple Magnets | `1.1.12c-neoforge-mc1.21` | 1 | 1/1 target classes found |
| Sophisticated Core | `1.21.1-1.4.73.2151` | 1 | 1/1 target classes found |

## forge-1.20.1

| Integration | Latest resolved version | Target classes | Bytecode result |
|---|---|---:|---|
| AE2 Wireless Terminals | `15.3.3-forge` | 1 | 1/1 target classes found |
| Artifacts | `9.5.19` | 1 | 0/1 target classes found; missing: `artifacts.effect.MagnetismMobEffect` |
| Draconic Evolution | `3.1.2.621` | 1 | 1/1 target classes found |
| Ender IO | `6.2.18-beta` | 2 | 0/2 target classes found; missing: `com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity`, `com.enderio.enderio.content.tools.ElectromagnetItem` |
| Industrial Foregoing | `1.20.1-3.5.22` | 1 | 1/1 target classes found |
| Item Collectors | `1.1.12-forge-mc1.20.2` | 1 | 1/1 target classes found |
| Mekanism | `10.4.16.80` | 1 | 1/1 target classes found |
| Mob Grinding Utils | `unresolved` | 1 | unresolved: mob-grinding-utils: HTTP 404 |
| Modular Routers | `12.1.1` | 1 | 1/1 target classes found |
| ProjectE | `1.20.1-1.1.3` | 1 | 0/1 target classes found; missing: `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand` |
| Reliquary Reincarnations | `1.20.1-2.0.62.1532` | 1 | 1/1 target classes found |
| Simple Magnets | `1.1.12-forge-mc1.20.1` | 1 | 1/1 target classes found |
| Sophisticated Core | `1.20.1-1.3.67.2148` | 1 | 1/1 target classes found |

## fabric-1.20.1

| Integration | Latest resolved version | Target classes | Bytecode result |
|---|---|---:|---|
| AE2 Wireless Terminals | `15.2.1-fabric` | 1 | 1/1 target classes found |
| Artifacts | `9.5.17` | 1 | 0/1 target classes found; missing: `artifacts.effect.MagnetismMobEffect` |
| Draconic Evolution | `1.0.1` | 1 | unresolved: HTTP Error 403: Forbidden |
| Ender IO | `1.20.1-1.1` | 2 | 0/2 target classes found; missing: `com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity`, `com.enderio.enderio.content.tools.ElectromagnetItem` |
| Industrial Foregoing | `1.20.1-1.1` | 1 | 0/1 target classes found; missing: `com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack` |
| Item Collectors | `1.1.12-fabric-mc1.20.2` | 1 | 1/1 target classes found |
| Mekanism | `unresolved` | 1 | unresolved: no matching version |
| Mob Grinding Utils | `unresolved` | 1 | unresolved: mob-grinding-utils: HTTP 404 |
| Modular Routers | `unresolved` | 1 | unresolved: no matching version |
| ProjectE | `1.1.0` | 1 | unresolved: HTTP Error 403: Forbidden |
| Reliquary Reincarnations | `unresolved` | 1 | unresolved: no matching version |
| Simple Magnets | `1.1.12-fabric-mc1.20.1` | 1 | 1/1 target classes found |
| Sophisticated Core | `1.20.1-1.2.7.15.166` | 1 | 1/1 target classes found |

## forge-1.19.2

| Integration | Latest resolved version | Target classes | Bytecode result |
|---|---|---:|---|
| AE2 Wireless Terminals | `12.9.7-forge` | 1 | 1/1 target classes found |
| Artifacts | `5.0.6+forge` | 1 | 0/1 target classes found; missing: `artifacts.effect.MagnetismMobEffect` |
| Draconic Evolution | `unresolved` | 1 | unresolved: no matching version |
| Ender IO | `unresolved` | 2 | unresolved: no matching version |
| Industrial Foregoing | `3.3.2.3` | 1 | 1/1 target classes found |
| Item Collectors | `1.1.12-forge-mc1.19.2` | 1 | 1/1 target classes found |
| Mekanism | `10.3.9.13` | 1 | 1/1 target classes found |
| Mob Grinding Utils | `unresolved` | 1 | unresolved: mob-grinding-utils: HTTP 404 |
| Modular Routers | `1.19.2-10.2.1` | 1 | 1/1 target classes found |
| ProjectE | `1.19.2-1.1.1` | 1 | 0/1 target classes found; missing: `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand` |
| Reliquary Reincarnations | `1.19.2-2.0.40.1198` | 1 | 0/1 target classes found; missing: `reliquary.item.FortuneCoinItem` |
| Simple Magnets | `1.1.12-forge-mc1.19.2` | 1 | 1/1 target classes found |
| Sophisticated Core | `1.19.2-0.6.4.730` | 1 | 1/1 target classes found |

## forge-1.18.2

| Integration | Latest resolved version | Target classes | Bytecode result |
|---|---|---:|---|
| AE2 Wireless Terminals | `11.6.3-forge` | 1 | 1/1 target classes found |
| Artifacts | `1.0.0` | 1 | 0/1 target classes found; missing: `artifacts.effect.MagnetismMobEffect` |
| Draconic Evolution | `3.0.31.531` | 1 | 1/1 target classes found |
| Ender IO | `unresolved` | 2 | unresolved: no matching version |
| Industrial Foregoing | `3.3.1.6` | 1 | 1/1 target classes found |
| Item Collectors | `1.1.12-forge-mc1.18` | 1 | 1/1 target classes found |
| Mekanism | `10.2.5.465` | 1 | 1/1 target classes found |
| Mob Grinding Utils | `unresolved` | 1 | unresolved: mob-grinding-utils: HTTP 404 |
| Modular Routers | `9.1.2` | 1 | 1/1 target classes found |
| ProjectE | `1.18.2-1.1.1` | 1 | 0/1 target classes found; missing: `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand` |
| Reliquary Reincarnations | `1.18.2-2.0.19.1161` | 1 | 0/1 target classes found; missing: `reliquary.item.FortuneCoinItem` |
| Simple Magnets | `1.1.12-forge-mc1.18` | 1 | 1/1 target classes found |
| Sophisticated Core | `1.18.2-0.6.4.604` | 1 | 1/1 target classes found |

## forge-1.16.5

| Integration | Latest resolved version | Target classes | Bytecode result |
|---|---|---:|---|
| AE2 Wireless Terminals | `unresolved` | 1 | unresolved: ae2wtlib: HTTP 404 |
| Artifacts | `1.0.0` | 1 | 0/1 target classes found; missing: `artifacts.effect.MagnetismMobEffect` |
| Draconic Evolution | `3.0.29.518` | 1 | 1/1 target classes found |
| Ender IO | `unresolved` | 2 | unresolved: no matching version |
| Industrial Foregoing | `3.2.14.7` | 1 | 1/1 target classes found |
| Item Collectors | `1.1.12-forge-mc1.16` | 1 | 1/1 target classes found |
| Mekanism | `10.1.2.457` | 1 | 1/1 target classes found |
| Mob Grinding Utils | `unresolved` | 1 | unresolved: mob-grinding-utils: HTTP 404 |
| Modular Routers | `1.16.5-7.5.4` | 1 | 1/1 target classes found |
| ProjectE | `1.16.5-1.0.41` | 1 | 0/1 target classes found; missing: `moze_intel.projecte.gameObjs.items.rings.BlackHoleBand` |
| Reliquary Reincarnations | `1.16.5-1.3.5.1124` | 1 | 0/1 target classes found; missing: `reliquary.item.FortuneCoinItem` |
| Simple Magnets | `1.1.12-forge-mc1.16` | 1 | 1/1 target classes found |
| Sophisticated Core | `unresolved` | 1 | unresolved: no matching version |

## forge-1.12.2

| Integration | Latest resolved version | Target classes | Bytecode result |
|---|---|---:|---|
| AE2 Wireless Terminals | `unresolved` | 1 | unresolved: ae2wtlib: HTTP 404 |
| Artifacts | `unresolved` | 1 | unresolved: no matching version |
| Draconic Evolution | `2.3.28.354` | 1 | 1/1 target classes found |
| Ender IO | `5.3.72` | 2 | 0/2 target classes found; missing: `com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity`, `com.enderio.enderio.content.tools.ElectromagnetItem` |
| Industrial Foregoing | `1.12.13-237` | 1 | 0/1 target classes found; missing: `com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack` |
| Item Collectors | `1.1.12-forge-mc1.12` | 1 | 1/1 target classes found |
| Mekanism | `9.8.3.390` | 1 | 0/1 target classes found; missing: `mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit` |
| Mob Grinding Utils | `unresolved` | 1 | unresolved: mob-grinding-utils: HTTP 404 |
| Modular Routers | `unresolved` | 1 | unresolved: no matching version |
| ProjectE | `7.0.3` | 1 | unresolved: HTTP Error 403: Forbidden |
| Reliquary Reincarnations | `1.12.2-1.3.4.796` | 1 | 0/1 target classes found; missing: `reliquary.item.FortuneCoinItem` |
| Simple Magnets | `1.1.12-forge-mc1.12` | 1 | 1/1 target classes found |
| Sophisticated Core | `unresolved` | 1 | unresolved: no matching version |
