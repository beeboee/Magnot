# Magnot compatibility

Magnot blocks supported magnet-style item movement when the source-to-item path crosses a ferrous region.

## Supported in code

| Mod | Status | Notes |
| --- | --- | --- |
| Sophisticated Core / Backpacks | Implemented, tested in dev | Hooks `MagnetUpgradeWrapper`; backpack magnet behavior is confirmed working. |
| Sophisticated Storage | Implemented, needs investigation | Uses the same Sophisticated Core wrapper path, but current dev testing shows storage magnets not pulling at all. Confirm whether this is a Magnot hook issue, a test-version mismatch, or Sophisticated Storage behavior before calling it supported. |
| AE2WTLib | Implemented, tested in dev | Hooks the wireless terminal magnet card pickup path. |
| ProjectE | Implemented, tested in dev | Hooks Black Hole Band-style gravitation from inventory, pedestal, alchemical chest, and alchemical bag contexts. Client-side gravitation is suppressed so blocked pulls do not rubberband against the server. |

## Sable / Create: Aeronautics findings

| Scenario | Status | Notes |
| --- | --- | --- |
| Static world regions before assembly | Not supported yet | Regions stay in world coordinates when their blocks are assembled into a Sable contraption. They need to be converted into contraption-local data during assembly, similar to glue-like block data. |
| Regions created on an assembled contraption | Not supported yet | Sable/internal contraption coordinates do not line up with normal item/magnet world coordinates, so current source-to-item checks cannot reliably match the region. |
| Removing contraptionized regions | Not supported yet | Region removal raycasts currently work against normal world-space boxes only. Contraption-local/funky coordinates need a Sable-aware removal path. |
| Magnets across contraptionized regions | Not supported yet | Backpacks and AE2WTLib work against static world regions, but not against regions that have been contraptionized. Needs coordinate transform or contraption-attached region storage. |
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
