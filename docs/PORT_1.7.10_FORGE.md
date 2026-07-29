# Forge 1.7.10 parity

This branch is a dedicated Forge 1.7.10 implementation. It does not compile or package the 1.12.2 source tree.

## Implemented baseline

- ForgeGradle 1.2 / Forge 10.13.4.1614 toolchain
- Java 7-compatible source and Java 8 build runtime
- ferrous paste and tube registration
- vanilla/Ore Dictionary crafting path with no optional-mod requirement
- two-corner region placement and removal
- per-world `WorldSavedData` persistence
- server-visible native region inspection while holding the tube
- public path-blocking API using 1.7.10 `Vec3`

## Remaining full-parity work

- replace the public-alpha particle shell with the full textured native face renderer where the 1.7.10 render pipeline permits it
- add ownership and migration semantics matching the reference implementation
- add packet-backed client region state if required for deterministic selection and fade behavior
- dedicated-server startup and reconnect/save-reload tests
- audit and implement integrations for the actual 1.7.10 magnet and vacuum ecosystem
- document every integration by exact target build and confidence level

Create did not exist for Minecraft 1.7.10 and is not part of this port.