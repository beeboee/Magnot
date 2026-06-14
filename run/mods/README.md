# Local compat test mods

Drop Minecraft 1.21.1 / NeoForge 21.1.230-compatible mod jars in this folder when testing optional magnet and vacuum compat.

Gradle loads every `*.jar` in this folder into the dev runtime through `runtimeOnly(fileTree(...))`.

## Automatic Modrinth downloads

The `downloadCompatTestMods` task attempts to download the newest Minecraft 1.21.1 NeoForge Modrinth build for each project listed in `compat_modrinth_projects` in `gradle.properties`.

`runClient` and `runServer` depend on that task, so these downloads are attempted before the dev runtime starts.

Current automatic target list:

- Mob Grinding Utils
- Item Collectors
- Simple Magnets by SuperMartijn642
- Modular Routers
- Industrial Foregoing
- Ender IO

To disable automatic downloads, set this in `gradle.properties`:

```properties
compat_modrinth_auto_download=false
```

## Manual jars

Manual jars still work. Put any extra required dependency jars here if the dev client complains during launch.

Current manual/reference target list:

- Draconic Evolution
- Actually Additions
- Reliquary Reincarnations
- Botania, when a compatible target version exists
- Cyclic, when a compatible target version exists

Do not commit downloaded mod jars to the repository.
