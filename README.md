# Magnot — Forge 1.12.2 legacy

This is a standalone legacy core/API port for Minecraft 1.12.2 and Forge 14.23.5.2860.

## Important limitation

Create did not exist for Minecraft 1.12.2. This branch therefore uses vanilla crafting recipes and does not claim feature parity with modern Magnot. It provides region creation, persistence, removal, and an item-pull API for 1.12.2 compatibility integrations to call.

No magnet mod is hooked by default yet. A region will only affect a 1.12.2 magnet or vacuum mod after that mod calls `MagnotApi`, or after a dedicated compatibility hook is added for it.
