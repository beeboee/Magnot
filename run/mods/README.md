# Local compat test mods

Drop Minecraft 1.21.1 / NeoForge 21.1.230-compatible mod jars in this folder when testing optional magnet compat.

Gradle loads every `*.jar` in this folder into the dev runtime through `runtimeOnly(fileTree(...))`.

Current target test list:

- Botania
- Draconic Evolution
- Actually Additions
- Reliquary Reincarnations
- Cyclic

Also include any required dependency jars for those mods here if the dev client complains during launch. Common examples may include library/API mods required by the target mod.

Do not commit downloaded mod jars to the repository.
