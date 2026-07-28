# NeoForge 1.20.1 parity

This branch targets the final NeoForge 1.20.1 line using `net.neoforged:forge:1.20.1-47.1.106`.

## Delivery requirements

- dedicated NeoForge-labelled artifact
- no required Create dependency
- native region selection and rendering
- persistent region storage and reconnect behavior matching Magnot 1.2
- vanilla-material recipe path with optional tag/material enhancements
- dedicated-server startup
- exact packaging metadata for Minecraft 1.20.1 and NeoForge
- public API behavior matching the Forge, Fabric, and reference NeoForge 1.21.1 implementations

## Compatibility policy

Forge 1.20.1 source may be shared only while its APIs remain binary and behaviorally compatible with NeoForge 1.20.1. Build success alone is not enough: NeoForge-specific launch, save/reload, selection, recipe, and magnet-blocking tests are required before stable status.

Create and third-party magnet integrations remain optional and are reported per file.