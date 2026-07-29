# Magnot — Forge 1.20.1 alpha

This branch is the Forge 1.20.1 port of Magnot 1.2's loader-independent feature set.

## Requirements

- Minecraft 1.20.1
- Forge 47.1.33 or newer
- Java 17
- Create is optional

## Included Magnot features

- Create-free startup and a complete native selection backend
- Immediate two-corner placement preview with the 25-block axis limit
- Textured ferrous faces for the active selection
- Passive stored-region outlines, ray-selected highlighting, and quick deselection fade
- Attack-to-remove with server-side reach and region-ID validation
- Client synchronization on login, dimension change, placement, and removal
- Persistent region IDs and group IDs with migration of older alpha saves
- Indexed path intersection, source-inside blocking, and per-tick query caching
- Public item-pull API v2
- Adaptive recipes:
  - external iron dust tags when available
  - Magnot fallback dust through Create crushing only when Create is installed and no external dust exists
  - eight iron nuggets when neither external dust nor Create is available
  - tagged iron plates when available, otherwise an iron ingot
- Dormant fallback iron dust omitted from the creative tab

## Not part of this parity pass

Third-party magnet/vacuum adapters and Sable are maintained separately and are not used to determine core feature parity.
