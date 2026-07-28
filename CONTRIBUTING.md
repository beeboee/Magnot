# Contributing

Use a focused branch and keep optional-mod references isolated in the matching compatibility package or `@Pseudo` mixin. Common Magnot code must not directly reference optional Create, JEI, EMI, or third-party magnet classes.

Create support is optional on every version. New ports must establish the native renderer and core region behavior before adding Create-backed presentation.

Before opening a pull request:

1. Run `gradle build` for the Magnot-only classpath.
2. Run the relevant optional build, such as `gradle build -Pwith_create=true`.
3. Add or update tests for geometry, recipe conditions, networking, rendering state, or compatibility behavior.
4. Do not commit downloaded jars, game saves, logs, or generated `run` contents.
5. Document any copied or substantially adapted third-party code and do not copy restricted assets.
6. Update the version-specific compatibility document instead of assuming an integration works across loaders or Minecraft versions.

Long-lived ports use the branch format described in [docs/VERSION_SUPPORT.md](docs/VERSION_SUPPORT.md), such as `version/1.20.1-forge`.

For magnet compatibility, hook immediately before an item is moved or inserted and preserve the source mod's behavior when Magnot returns false.
