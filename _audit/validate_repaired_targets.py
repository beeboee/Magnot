#!/usr/bin/env python3
from __future__ import annotations

import json
import os
import re
import subprocess
import urllib.parse
import urllib.request
import zipfile
from pathlib import Path

ROOT = Path.cwd()
AUDIT = json.loads((ROOT / "_audit/results/integration-target-audit.json").read_text(encoding="utf-8"))
OUT = ROOT / "_audit/final-validation"
JARS = OUT / "jars"
OUT.mkdir(parents=True, exist_ok=True)
JARS.mkdir(parents=True, exist_ok=True)
TOKEN = os.environ.get("CURSEFORGE_TOKEN", "")
UA = "Magnot repaired integration target validator/1.0"

# project, target class, required target methods, required bytecode descriptor fragments
SPECS = {
    "main-1.21.1-neoforge": [
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet"], ["ItemEntity;playerTouch", "Player;)V"]),
        ("artifacts", "artifacts.effect.MagnetismMobEffect", ["applyEffectTick"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("draconic-evolution", "com.brandon3055.draconicevolution.items.tools.Magnet", ["updateMagnet"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("enderio", "com.enderio.enderio.content.tools.ElectromagnetItem", ["onTickWhenActive"], ["Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("enderio", "com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity", ["getEntities"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("industrial-foregoing", "com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack", ["inventoryTick"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["update"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["ItemEntity"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("projecte", "moze_intel.projecte.gameObjs.items.rings.BlackHoleBand", ["inventoryTick", "updateInPedestal", "updateInAlchChest", "updateInAlchBag"], ["gravitateEntityTowards"]),
        ("reliquary", "reliquary.item.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryUpdate"], ["EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["ItemEntity"]),
    ],
    "forge-1.20.1": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryUpdate"], ["EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["lambda$update$0"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["ItemEntity"]),
        ("artifacts", "artifacts.item.wearable.belt.UniversalAttractorItem", ["wornTick"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet"], ["ItemEntity;playerTouch"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["ItemEntity"]),
        ("draconic-evolution", "com.brandon3055.draconicevolution.items.tools.Magnet", ["updateMagnet"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("reliquary", "reliquary.item.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("enderio", "com.enderio.base.common.item.tool.ElectromagnetItem", ["onTickWhenActive"], ["Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("enderio", "com.enderio.machines.common.blockentity.base.VacuumMachineBlockEntity", ["getEntities"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
    ],
    "fabric-1.20.1": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryUpdate"], ["EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["lambda$update$0"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("artifacts", "artifacts.item.wearable.belt.UniversalAttractorItem", ["wornTick"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet"], ["ItemEntity;playerTouch"]),
    ],
    "forge-1.19.2": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryUpdate"], ["EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["lambda$update$0"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("artifacts", "artifacts.common.item.curio.belt.UniversalAttractorItem", ["curioTick"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet"], ["ItemEntity;playerTouch"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["ItemEntity"]),
        ("reliquary", "reliquary.items.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["ItemEntity"]),
    ],
    "forge-1.18.2": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryUpdate"], ["EntityTypeTest;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["lambda$update$0"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet"], ["ItemEntity;playerTouch"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["ItemEntity"]),
        ("draconic-evolution", "com.brandon3055.draconicevolution.items.tools.Magnet", ["updateMagnet"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("reliquary", "reliquary.items.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode"], ["Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;", "Ljava/util/List;"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["ItemEntity"]),
    ],
    "forge-1.16.5": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryUpdate"], ["EntityType;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["lambda$update$0"], ["Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;", "Ljava/util/List;"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems"], ["Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;", "Ljava/util/List;"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode"], ["Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;", "Ljava/util/List;"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["ItemEntity"]),
        ("draconic-evolution", "com.brandon3055.draconicevolution.items.tools.Magnet", ["updateMagnet"], ["Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;", "Ljava/util/List;"]),
        ("reliquary", "xreliquary.items.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer"], ["Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;", "Ljava/util/List;"]),
        ("industrial-foregoing", "com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack", ["inventoryTick"], ["Ljava/lang/Class;Lnet/minecraft/util/math/AxisAlignedBB;", "Ljava/util/List;"]),
    ],
}


def request(url: str, headers: dict[str, str] | None = None) -> bytes:
    merged = {"User-Agent": UA}
    if headers:
        merged.update(headers)
    req = urllib.request.Request(url, headers=merged)
    with urllib.request.urlopen(req, timeout=180) as response:
        return response.read()


def curseforge_latest(target: str, project: str) -> dict | None:
    if not TOKEN:
        return None
    game, loader = {
        "main-1.21.1-neoforge": ("1.21.1", "neoforge"),
        "forge-1.20.1": ("1.20.1", "forge"),
        "fabric-1.20.1": ("1.20.1", "fabric"),
        "forge-1.19.2": ("1.19.2", "forge"),
        "forge-1.18.2": ("1.18.2", "forge"),
        "forge-1.16.5": ("1.16.5", "forge"),
    }[target]
    search_name = {"mob-grinding-utils": "Mob Grinding Utils"}.get(project, project)
    q = urllib.parse.urlencode({"gameId": 432, "classId": 6, "searchFilter": search_name, "pageSize": 20})
    mods = json.loads(request("https://api.curseforge.com/v1/mods/search?" + q, {"x-api-key": TOKEN})).get("data", [])
    if not mods:
        return None
    # Exact normalized name/slug preference.
    needle = re.sub(r"[^a-z0-9]", "", search_name.lower())
    mods.sort(key=lambda m: (re.sub(r"[^a-z0-9]", "", m.get("name", "").lower()) == needle, needle in re.sub(r"[^a-z0-9]", "", m.get("name", "").lower())), reverse=True)
    project_id = mods[0]["id"]
    q = urllib.parse.urlencode({"gameVersion": game, "pageSize": 50})
    files = json.loads(request(f"https://api.curseforge.com/v1/mods/{project_id}/files?" + q, {"x-api-key": TOKEN})).get("data", [])
    candidates = []
    for file in files:
        versions = [str(v).lower() for v in file.get("gameVersions", [])]
        if game not in versions or loader not in versions or not str(file.get("fileName", "")).endswith(".jar"):
            continue
        candidates.append(file)
    candidates.sort(key=lambda f: f.get("fileDate", ""), reverse=True)
    if not candidates:
        return None
    file = candidates[0]
    url = file.get("downloadUrl")
    if not url:
        fid = str(file["id"])
        url = f"https://edge.forgecdn.net/files/{fid[:-3]}/{fid[-3:]}/{urllib.parse.quote(file['fileName'])}"
    return {"download_url": url, "version": file.get("displayName") or file.get("fileName"), "project_id": project_id}


def resolution(target: str, project: str) -> dict | None:
    if target == "main-1.21.1-neoforge" and project == "projecte":
        return {"download_url": "https://www.cursemaven.com/curse/maven/projecte-226410/6611984/projecte-226410-6611984.jar", "version": "pinned CurseForge file 6611984"}
    row = AUDIT["targets"].get(target, {}).get("projects", {}).get(project, {}).get("resolution", {})
    if row.get("download_url"):
        return row
    return curseforge_latest(target, project)


def javap(jar: Path, class_name: str) -> str:
    proc = subprocess.run(["javap", "-classpath", str(jar), "-p", "-s", "-c", class_name], text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=60)
    return proc.stdout


def main() -> int:
    results: list[dict] = []
    failed = False
    cache: dict[tuple[str, str], Path] = {}
    for target, specs in SPECS.items():
        for project, class_name, methods, descriptors in specs:
            row = {"target": target, "project": project, "class": class_name, "methods": methods, "descriptors": descriptors}
            resolved = resolution(target, project)
            row["resolution"] = resolved
            if not resolved:
                row["status"] = "UNRESOLVED"
                failed = True
                results.append(row)
                continue
            key = (target, project)
            jar = cache.get(key)
            if jar is None:
                jar = JARS / f"{target}--{project}.jar"
                try:
                    jar.write_bytes(request(resolved["download_url"]))
                    cache[key] = jar
                except Exception as exc:
                    row["status"] = "DOWNLOAD_FAILED"
                    row["error"] = repr(exc)
                    failed = True
                    results.append(row)
                    continue
            entry = class_name.replace(".", "/") + ".class"
            try:
                with zipfile.ZipFile(jar) as archive:
                    row["class_found"] = entry in archive.namelist()
                if not row["class_found"]:
                    row["status"] = "CLASS_MISSING"
                    failed = True
                    results.append(row)
                    continue
                text = javap(jar, class_name)
                row["method_presence"] = {method: method in text for method in methods}
                row["descriptor_presence"] = {descriptor: descriptor in text for descriptor in descriptors}
                ok = all(row["method_presence"].values()) and all(row["descriptor_presence"].values())
                row["status"] = "PASS" if ok else "SIGNATURE_MISMATCH"
                if not ok:
                    failed = True
            except Exception as exc:
                row["status"] = "INSPECTION_FAILED"
                row["error"] = repr(exc)
                failed = True
            results.append(row)

    (OUT / "repaired-target-validation.json").write_text(json.dumps(results, indent=2) + "\n", encoding="utf-8")
    lines = [
        "# Repaired integration target validation",
        "",
        "This validates the active repaired mixin targets against the newest resolved integration jar for each exact Minecraft/loader pair. PASS requires the target class, every targeted third-party method, and the expected vanilla-call descriptor shape to exist.",
        "",
        "| Target | Integration | Resolved version | Target class | Result |",
        "|---|---|---|---|---|",
    ]
    for row in results:
        version = (row.get("resolution") or {}).get("version", "—")
        lines.append(f"| {row['target']} | {row['project']} | `{version}` | `{row['class']}` | **{row['status']}** |")
    mismatches = [r for r in results if r["status"] != "PASS"]
    if mismatches:
        lines += ["", "## Mismatches", ""]
        for row in mismatches:
            lines += [f"### {row['target']} — {row['project']}", "", "```json", json.dumps(row, indent=2), "```", ""]
    (OUT / "repaired-target-validation.md").write_text("\n".join(lines) + "\n", encoding="utf-8")
    return 1 if failed else 0


if __name__ == "__main__":
    raise SystemExit(main())
