# Magnot

Magnot lets ferrous paste act as a boundary for item magnets, vacuum blocks, and other remote item collectors.

Items can sit behind a protected wall without being pulled through it by backpacks, rings, modules, hoppers, or similar effects. Magnets still work. They just have to mind the walls.

## Current release

**Magnot 1.2.0** targets:

- Minecraft 1.21.1
- NeoForge 21.1.230 or newer

Install Magnot on both the server and connecting clients.

Create is optional. With Create installed, Magnot reuses Create/Catnip selection rendering, raycasting, fade behavior, and glue-style sounds while remaining authoritative for region creation and removal. Without Create, Magnot uses its native selection backend with matching Magnot visuals and gameplay behavior.

Sable, JEI, EMI, and all magnet/vacuum integrations are optional.

## What it does

Magnot blocks supported remote item pulls when the pull path crosses a protected ferrous region.

It does not delete items, disable magnets globally, or change normal vanilla item pickup. If a pull is not crossing a protected region, it should behave normally.

## Basic use

1. Craft ferrous paste and a ferrous tube. Recipes adapt to the common iron dust and plate tags available in the pack.
2. Right-click the first and second corners with the tube.
3. Hold the tube to inspect nearby regions, or attack a highlighted region to remove it.
4. Use magnets or vacuum blocks nearby as normal.

Selection appears immediately. When the player looks away, the textured faces and emphasized border fade quickly back to the passive outline.

## Adaptive materials

- External `c:dusts/iron` present: Magnot consumes external dust and hides its fallback dust.
- No external dust + Create present: Create crushing produces `magnot:iron_dust`, which becomes visible and is used by ferrous paste.
- No external dust + no Create: the dust path disappears and an eight-iron-nugget plus slime fallback recipe is enabled.
- `c:plates/iron` present: the ferrous tube uses a plate or sheet from that tag.
- `c:plates/iron` empty: the tube uses an iron ingot instead.

Inactive recipes never enter the recipe manager. Dormant Magnot iron dust is omitted from the creative tab and hidden from optional JEI and EMI integrations.

## Compatibility

Magnot supports a growing set of magnet and vacuum mods. Compatibility depends on the exact mod and version, so the maintained list lives in [magnet and vacuum compatibility](docs/COMPATIBILITY.md).

Mods with magnets, vacuums, remote item collectors, absorption hoppers, item teleporters, or similar item-moving behavior can support Magnot directly through the [public compatibility API](docs/API.md).

If a pull still crosses a protected region, report:

- the mod name and version
- the item or block used
- whether the pull came from a player or a block
- what was between the pull source and the item

## Version support

The native backend is the baseline for every port. A Minecraft version does **not** need a matching Create release before Magnot can support it.

Paramount compatibility targets are:

1. Minecraft 1.20.1 on Forge, NeoForge, Fabric, and Quilt
2. Minecraft 1.12.2 on Forge
3. Minecraft 1.7.10 on Forge

NeoForge 1.21.1 remains the reference implementation. Forge 1.19.2, 1.18.2, and 1.16.5 remain active secondary public-alpha lines.

The current 1.2.0 alpha ports implement the shared Magnot-owned core: persistence, synchronization, native rendering, placement preview, ray removal, adaptive recipes, path blocking, and public API semantics. Third-party magnet/vacuum adapters, Sable, moving structures, and Create-backed presentation are tracked separately and do not determine native core parity.

Each loader line has a long-lived branch and branch-specific build gate. Quilt validation uses the Fabric 1.20.1 artifact unless loader-specific testing proves a separate package is necessary. A successful build confirms the target toolchain and mappings, but runtime placement, rendering, save/reload, reconnect, recipe, and path-blocking tests are still required before stable status.

See [version support and parity](docs/VERSION_SUPPORT.md) for the exact target matrix, alpha naming scheme, core-parity definition, and branch policy. Exact optional-mod compatibility is stated per file rather than assumed across versions.

## Disabling integration adapters

Individual Magnot adapter mixins can be disabled for a Gradle dev launch without removing the target mod.

```powershell
.\gradlew.bat runClient -Pwith_compat_test_mods=true -Pdisable_artifacts=true
```

A list form is also supported:

```powershell
.\gradlew.bat runClient -Pwith_compat_test_mods=true -Pdisable_integrations=artifacts,simplemagnets
```

Names are case-insensitive and punctuation is ignored, so `mob_grinding_utils` and `mobgrindingutils` are equivalent. Use `-Pdisable_integrations=all` to disable every Magnot integration mixin. The target mods remain installed and functioning; only Magnot's adapters for them are skipped.

The lower-level JVM property `-Dmagnot.disableIntegrations=...` and environment variable `MAGNOT_DISABLE_INTEGRATIONS=...` remain available for launchers that do not invoke Gradle.

## Development

Development launches use separate game directories so jars from one test cannot contaminate another:

- `runClient` and `runServer` use `run/clean/client` and `run/clean/server`.
- `-Pwith_compat_test_mods=true` switches to `run/compat/client` or `run/compat/server`.
- Manually supplied compatibility jars belong in `run/compat/mods`.
- The legacy `run/mods` directory is intentionally ignored.

A genuinely Magnot-only client launch is:

```powershell
.\gradlew.bat runClient
```

Optional dev runtimes are explicit Gradle properties:

- `-Pwith_create=true`
- `-Pwith_sable=true`
- `-Pwith_jei=true`
- `-Pwith_emi=true`
- `-Pwith_compat_test_mods=true`

Release and architecture details are in [Magnot 1.2.0](docs/V1.2.0.md). The public-facing CurseForge/wiki copy is maintained in [docs/PUBLIC_DESCRIPTION.md](docs/PUBLIC_DESCRIPTION.md).

## License

MIT. Third-party notices are in [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md).
