# NeoForge 1.20.1 parity

This branch targets the final NeoForge 1.20.1 line using `net.neoforged:forge:1.20.1-47.1.106`.

## Implemented loader-independent parity

- no required Create dependency
- native textured selection rendering and quick fade
- persistent stable region IDs with old-alpha migration
- client synchronization on login, dimension changes, placement, and removal
- validated ray-based removal
- adaptive common-tag materials with vanilla and Create-aware fallbacks
- indexed and cached path blocking with public API v2 semantics
- dedicated NeoForge-labelled artifact and metadata

## Remaining release gate

Build success is not sufficient for stable status. NeoForge-specific client launch, dedicated-server startup, save/reload, reconnect, recipe truth-table, selection, and path-blocking tests must pass. Third-party integrations and Sable are maintained separately.
