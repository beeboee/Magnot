# Feature idea: filtered ferrous regions

This is intended future work, not part of the 0.1.0 release.

## Goal

Let a ferrous region keep its current wall-like behavior while optionally filtering which items it blocks or allows.

The feature should stay in character for Magnot: the rule belongs to the protected region, not to every magnet item.

## Region filter slot

Each ferrous tube / protected region can store one filter stack.

Suggested interaction:

- Right-click a tube/region with an item or filter to place a copy into the region's filter slot.
- Right-click with an empty hand to remove the stored filter.
- Sneak + right-click to toggle blacklist / whitelist mode.

Suggested tooltip lines:

- `Right click with item to add filter`
- `Sneak + Right Click to toggle blacklist/whitelist`

The stored filter should appear as a small icon in the bottom-right corner of the tube item icon.

## Filter behavior

Default behavior with no filter stays the same: block all supported remote item pulls through the protected region.

With a filter installed:

- Blacklist mode: matching items are blocked, non-matching items are allowed through.
- Whitelist mode: matching items are allowed through, non-matching items are blocked.

The exact wording can be adjusted, but the UI must make the active mode obvious.

## Supported filter inputs

The filter slot should support:

- Normal item stacks.
- Create Filter.
- Create Attribute Filter / Brass Filter, if practical for the target Create version.
- Item lists from Create filters.
- Tag lists from Create filters.

If a Create filter is unset or blank, treat the filter item itself as the selected filter item. This avoids a blank Create filter meaning "filter nothing" when the player probably expected to filter that filter item.

## Tooltip / inventory preview

When hovering the tube item, show a compact preview of what the region filter resolves to.

For normal item filters, show the item icon.

For Create filters, show the resolved contents, not just the Create filter item:

- Item icons for specific item entries.
- Text rows for tag entries, such as `#c:ingots/iron`.
- A compact `+N more` overflow when the list is too large.

The preview should expand and shrink to fit the number of items/tags, similar in spirit to bundle-style hover previews.

## In-world reveal with Create goggles

When wearing Create goggles and revealing ferrous regions by holding the tube, only show filter details for the specific region currently highlighted.

Specifically: show the filter items/tags only when the player is looking at or otherwise highlighting the region closely enough that the region border face texture is displayed.

Do not show all region filter previews at once. That would create too much visual noise.

The highlighted region preview should show:

- Item icons for resolved item entries.
- Text labels for resolved tag entries.
- A `+N more` overflow if needed.
- A label below the preview: `Blacklist` or `Whitelist`.

This should be client-side rendering only. Avoid spawning real display entities just to show filter previews.

## Implementation notes

Likely data shape:

```java
ItemStack filterStack;
boolean whitelistMode;
```

Filtering should be item-aware. Prefer checks like:

```java
blocksItemPull(ServerLevel level, Vec3 source, ItemEntity item)
blocksPlayerItemPull(ServerLevel level, Player player, ItemEntity item)
```

Avoid reducing the item to a position too early, because item filters need access to the pulled `ItemStack`.

Create one shared resolver so behavior and UI agree:

```java
ResolvedFerrousFilter resolve(ItemStack filterStack)
```

The resolver should produce item entries, tag entries, and any useful display entries for the tooltip / goggles overlay.

## Release notes

This should probably land after the first public release. It touches region storage, interaction behavior, item rendering, tooltips, client sync, Create filter parsing, and in-world overlay rendering.
