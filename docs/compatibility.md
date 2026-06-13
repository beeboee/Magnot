# Magnot compatibility

Magnot blocks supported magnet-style item movement when the source-to-item path crosses a ferrous region.

## Supported in code

| Mod | Status | Notes |
| --- | --- | --- |
| Sophisticated Core / Backpacks / Storage | Implemented, tested in dev | Hooks `MagnetUpgradeWrapper`; backpacks and storage share the same compatibility path. Storage false alarm was caused by copied NBT from a barrel placed inside a region. |
| AE2WTLib | Implemented, tested in dev | Hooks the wireless terminal magnet card pickup path. |
| ProjectE | Implemented, tested in dev | Hooks Black Hole Band-style gravitation from inventory, pedestal, alchemical chest, and alchemical bag contexts. Client-side gravitation checks synced client regions so visual pull behavior matches server-side blocking. |
| Sable / Create: Aeronautics | Third-pass implemented, needs dev testing | Ferrous regions track whether they are world-space or owned by a Sable sub-level. Sable block moves now transform regions from source space to target space and clear sub-level ownership when target is the normal world. |

## Sable / Create: Aeronautics test matrix

| Scenario | Status | Notes |
| --- | --- | --- |
| Static world regions before assembly | Needs retest | Regions move during Sable assembly when the moved block set intersects a world-space saved region, then get tagged with the target sub-level UUID. |
| Regions created on an assembled contraption | Needs retest | Existing placement still depends on the positions returned by Sable/Create hit detection. If those positions are local plot coordinates, checks/removal should work; if they are projected global positions, placement needs a Sable-aware creation path. |
| Removing contraptionized regions | Needs retest | Client selects transformed regions; server removes the selected region by transforming the removal ray into sub-level coordinates and matching the region's sub-level UUID. |
| Magnets across contraptionized regions | Needs retest | Server checks world-space regions against projected global paths and sub-level-owned regions against transformed local paths. This should cover standing on the contraption and standing on normal ground. |
| Disassembly | Needs retest | Sable moves now capture source sub-level ownership before the block move and target sub-level ownership after the block move. If the target is the normal world, the region is transformed back to world coordinates and its sub-level owner is cleared. |
| Region outline on contraptions | Approximate | Sable's own super-glue selection projects bounding boxes outward; Magnot currently does the same AABB-style approximation. True rotated boxes need custom oriented rendering or inward ray transforms. |
| Normal item transport | Intentionally unaffected | Hoppers, water streams, belts, pipes, and normal item physics should not be blocked. Magnot only targets supported magnet pulls. |

## Planned magnet targets

| Mod | Priority | Notes |
| --- | --- | --- |
| Simple Magnets | High | Dedicated magnet mod. Needs source or decompiled runtime inspection for the current NeoForge class/method hook. |
| Actually Additions | High | Check current Ring of Magnetizing behavior before hooking. |
| Mekanism | Medium/high | Needs verification of current item pickup source. Do not mix in blindly. |
| Reliquary | Medium | Check Coin of Fortune or equivalent pickup behavior. |
| Draconic Evolution | Medium | Verify current 1.21.1 utility item behavior before adding. |

## Compatibility rule

For each integration, prefer the narrowest hook around the final magnet action:

1. identify the magnet source position;
2. identify the item entity position;
3. call `FerrousMagnetRules.blocksMagnet(level, source, item.position())`;
4. skip only that magnet pull when blocked.

Do not block normal item entity ticks, water movement, hoppers, belts, pipes, or mechanical transport.
