# Compatibility repair scope

This pass validates Magnot against the newest resolved integration and Create versions for each supported Minecraft/loader branch.

## Rules

- Inspect only mixins registered by each branch.
- Match the exact target class, target method, and vanilla invocation descriptor in the resolved integration jar.
- On Forge jars that contain obfuscated vanilla calls, remap the `@At` invocation target while leaving third-party target method names unremapped.
- Remove compatibility claims and mixin registrations when the matching integration version does not contain that magnet implementation.
- Keep target mods optional.
- Validate every registered mixin target against the resolved jars before publishing another alpha.

## Create targets

- NeoForge 1.21.1: Create `6.0.11-295`.
- Forge 1.20.1: Create `6.0.8-291`.
- Fabric 1.20.1: Create Fabric `6.0.8.1+build.1744-mc1.20.1`.
- Forge 1.19.2: Create `0.5.1.i`.
- Forge 1.18.2: Create `0.5.1.i`.
- Forge 1.16.5: verify the final published 1.16.5 Create build before changing the existing `0.3.2g` requirement.
- Forge 1.12.2 legacy: no Create dependency; Create did not support Minecraft 1.12.2.
