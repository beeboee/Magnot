# Magnot

Magnot adds ferrous paste regions that block supported magnets, vacuums, and remote item collectors from pulling dropped items into, out of, or through protected areas.

Magnets still work normally outside those boundaries. Protect storage rooms, machines, contraptions, farms, and item-processing areas without disabling magnet effects globally.

## Installation

Choose the file that matches your exact Minecraft version and mod loader. Install Magnot on the server and on connecting clients unless that file's changelog explicitly says otherwise.

Each file page is authoritative for its loader requirements, supported optional mods, known limitations, and test status. Compatibility can differ between Minecraft versions because their modding APIs and available third-party mods differ substantially.

Create is not a universal requirement. Builds that support Create treat it as an optional enhancement; builds for versions without Create use Magnot's native behavior.

## Using ferrous regions

1. Craft ferrous paste and a ferrous tube.
2. Right-click two corners with the tube to define a region.
3. Hold the tube to reveal nearby regions.
4. Attack a highlighted region with the tube to remove it.

Recipes adapt to the material systems available in each supported environment. A build should always provide a usable route using materials available without optional mods.

## Compatibility

Magnot can block supported player magnets, vacuum blocks, absorption hoppers, remote collectors, and similar item-moving effects when their pull path crosses a ferrous region.

Exact integrations are listed per file and in the repository compatibility matrix. An integration is only called confirmed for a specific Minecraft version and loader after that exact combination has been verified. Missing optional integrations do not prevent Magnot's core region system from working.

Mod authors can integrate directly through Magnot's public compatibility API where available.

## Feedback

Public alpha builds are released specifically to broaden testing. When reporting a problem, include:

- the exact Magnot filename
- Minecraft version and loader
- whether Create was installed
- the magnet, vacuum, or collector involved
- reproduction steps
- the latest log or crash report

Reports are tracked through the Magnot GitHub issue form.