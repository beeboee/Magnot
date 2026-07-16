# Contributing

Use a focused branch and keep optional-mod references isolated in the matching `compat` package or `@Pseudo` mixin. Common Magnot code must not directly reference optional Create, JEI, or EMI classes.

Before opening a pull request:

1. Run `gradle build` for the Magnot-only classpath.
2. Run the relevant optional build, such as `gradle build -Pwith_create=true`.
3. Add or update tests for geometry, recipe conditions, networking, or compatibility behavior.
4. Do not commit downloaded jars, game saves, logs, or generated `run` contents.
5. Document any copied or substantially adapted third-party code and do not copy restricted assets.

For magnet compatibility, hook immediately before an item is moved or inserted and preserve the source mod's behavior when Magnot returns false.
