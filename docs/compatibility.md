# Magnot compatibility

Magnot blocks supported magnet-style item movement when the source-to-item path crosses a ferrous region.

## Supported in code

| Mod | Status | Notes |
| --- | --- | --- |
| Sophisticated Core / Backpacks / Storage | Implemented, tested in dev | Hooks `MagnetUpgradeWrapper`; backpacks and storage share the same compatibility path. Storage false alarm was caused by copied NBT from a barrel placed inside a region. |
| AE2WTLib | Implemented, tested in dev | Hooks the wireless terminal magnet card pickup path. |
| ProjectE | Implemented, tested in dev | Hooks Black Hole Band-style gravitation from inventory, pedestal, alchemical chest, and alchemical bag contexts. Client-side gravitation checks synced client regions so visual pull behavior matches server-side blocking. |
| Sable / Create: Aeronautics | First-pass implemented, needs dev testing | Ferrous regions move with assembled Sable blocks, magnet checks transform source/item paths into sub-level coordinates, selected/removal raycasts check Sable-transformed regions, and held-tube outlines use transformed display bounds. |

## Sable / Create: Aeronautics test matrix

| Scenario | Status | Notes |
| --- | --- | --- |
| Static world regions before assembly | Needs retest | Regions are moved during Sable assembly when the assembled block set intersects the saved region. |
| Regions created on an assembled contraption | Needs retest | Current code should support checks/removal if the saved region is in the sub-level plot coordinate space. |
| Removing contraptionized regions | Needs retest | Client selects transformed regions; server removes the selected region by transforming the removal ray into sub-level coordinates. |
| Magnets across contraptionized regions | Needs retest | Server checks static world regions first, then checks intersecting Sable sub-level regions by transforming the source-to-item path into sub-level space. |
| Region outline on contraptions | Approximate | Outlines transform with the Sable pose but are still drawn as Create Outliner AABBs, so rotated boxes may appear as a world-axis bounding approximation rather than a true oriented box. |
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

Do not block normal item entity ticks, water movement, hoppers, belts, or mechanical transport.
