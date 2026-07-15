#!/usr/bin/env python3
from __future__ import annotations

import json
import re
import subprocess
import urllib.parse
import urllib.request
import xml.etree.ElementTree as ET
import zipfile
from pathlib import Path

ROOT = Path.cwd()
AUDIT = ROOT / "_audit" / "results"
JARS = ROOT / "_audit" / "deep-jars"
OUT = ROOT / "_audit" / "deep-results"
JARS.mkdir(parents=True, exist_ok=True)
OUT.mkdir(parents=True, exist_ok=True)
UA = "Magnot deep compatibility audit/1.0"

# Only mixins actually registered by each target branch are included.
ACTIVE = {
    "main-1.21.1-neoforge": [
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet", "playerTouch"], ["magnet", "playerTouch"]),
        ("artifacts", "artifacts.effect.MagnetismMobEffect", ["applyEffectTick", "getEntitiesOfClass"], ["magnet", "effect", "itementity"]),
        ("draconic-evolution", "com.brandon3055.draconicevolution.items.tools.Magnet", ["updateMagnet", "getEntitiesOfClass"], ["magnet", "itementity"]),
        ("enderio", "com.enderio.enderio.content.tools.ElectromagnetItem", ["onTickWhenActive", "getEntities"], ["electromagnet", "itementity"]),
        ("enderio", "com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity", ["getEntities", "getEntitiesOfClass"], ["vacuum", "itementity"]),
        ("industrial-foregoing", "com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack", ["inventoryTick", "getEntitiesOfClass"], ["backpack", "magnet", "itementity"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["tick", "getEntitiesOfClass"], ["collector", "itementity"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["magnetic", "pullitem", "itementity"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems", "getEntitiesOfClass"], ["absorption", "captureitems", "itementity"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode", "getEntitiesOfClass"], ["vacuum", "itementity"]),
        ("projecte", "moze_intel.projecte.gameObjs.items.rings.BlackHoleBand", ["inventoryTick", "gravitateEntityTowards"], ["blackhole", "gravitate", "ring"]),
        ("reliquary", "reliquary.item.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["fortune", "coin", "pickup"]),
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryTick", "inventoryUpdate", "getEntities"], ["magnet", "itementity"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["magnet", "pickupitems", "trytoinsert"]),
    ],
    "forge-1.20.1": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryTick", "inventoryUpdate", "getEntities"], ["magnet", "itementity"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["tick", "getEntitiesOfClass"], ["collector", "itementity"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["magnet", "pickupitems", "trytoinsert"]),
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet", "playerTouch"], ["magnet", "playertouch"]),
        ("artifacts", "artifacts.effect.MagnetismMobEffect", ["applyEffectTick", "getEntitiesOfClass"], ["magnet", "effect", "itementity"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["magnetic", "pullitem"]),
        ("draconic-evolution", "com.brandon3055.draconicevolution.items.tools.Magnet", ["updateMagnet", "getEntitiesOfClass"], ["magnet", "itementity"]),
        ("reliquary", "reliquary.item.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["fortune", "coin", "pickup"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems", "getEntitiesOfClass"], ["absorption", "captureitems"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode", "getEntitiesOfClass"], ["vacuum", "itementity"]),
        ("enderio", "com.enderio.enderio.content.tools.ElectromagnetItem", ["onTickWhenActive", "getEntities"], ["electromagnet", "itementity"]),
        ("enderio", "com.enderio.enderio.content.machines.vacuum.VacuumMachineBlockEntity", ["getEntities", "getEntitiesOfClass"], ["vacuum", "itementity"]),
    ],
    "fabric-1.20.1": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryTick", "inventoryUpdate", "getEntities"], ["magnet", "itementity"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["tick", "getEntitiesOfClass"], ["collector", "itementity"]),
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet", "playerTouch"], ["magnet", "playertouch"]),
        ("artifacts", "artifacts.effect.MagnetismMobEffect", ["applyEffectTick", "getEntitiesOfClass"], ["magnet", "effect", "itementity"]),
    ],
    "forge-1.19.2": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryTick", "inventoryUpdate", "getEntities"], ["magnet", "itementity"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["tick", "getEntitiesOfClass"], ["collector", "itementity"]),
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet", "playerTouch"], ["magnet", "playertouch"]),
        ("artifacts", "artifacts.effect.MagnetismMobEffect", ["applyEffectTick", "getEntitiesOfClass"], ["magnet", "effect", "itementity"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["magnetic", "pullitem"]),
        ("reliquary", "reliquary.item.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["fortune", "coin", "pickup"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems", "getEntitiesOfClass"], ["absorption", "captureitems"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode", "getEntitiesOfClass"], ["vacuum", "itementity"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["magnet", "pickupitems"]),
    ],
    "forge-1.18.2": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryTick", "inventoryUpdate", "getEntities"], ["magnet", "itementity"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["tick", "getEntitiesOfClass"], ["collector", "itementity"]),
        ("ae2wtlib", "de.mari_023.ae2wtlib.wct.magnet_card.MagnetHandler", ["handleMagnet", "playerTouch"], ["magnet", "playertouch"]),
        ("artifacts", "artifacts.effect.MagnetismMobEffect", ["applyEffectTick", "getEntitiesOfClass"], ["magnet", "effect", "itementity"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["magnetic", "pullitem"]),
        ("draconic-evolution", "com.brandon3055.draconicevolution.items.tools.Magnet", ["updateMagnet", "getEntitiesOfClass"], ["magnet", "itementity"]),
        ("reliquary", "reliquary.item.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["fortune", "coin", "pickup"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems", "getEntitiesOfClass"], ["absorption", "captureitems"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode", "getEntitiesOfClass"], ["vacuum", "itementity"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["magnet", "pickupitems"]),
    ],
    "forge-1.16.5": [
        ("simple-magnets", "com.supermartijn642.simplemagnets.MagnetItem", ["inventoryTick", "inventoryUpdate", "getEntities"], ["magnet", "itementity"]),
        ("item-collectors", "com.supermartijn642.itemcollectors.CollectorBlockEntity", ["tick", "getEntitiesOfClass"], ["collector", "itementity"]),
        ("mob-grinding-utils", "mob_grinding_utils.tile.TileEntityAbsorptionHopper", ["getCaptureItems", "getEntitiesOfClass"], ["absorption", "captureitems"]),
        ("modular-routers", "me.desht.modularrouters.logic.compiled.CompiledVacuumModule", ["handleItemMode", "getEntitiesOfClass"], ["vacuum", "itementity"]),
        ("mekanism", "mekanism.common.content.gear.mekasuit.ModuleMagneticAttractionUnit", ["pullItem"], ["magnetic", "pullitem"]),
        ("draconic-evolution", "com.brandon3055.draconicevolution.items.tools.Magnet", ["updateMagnet", "getEntitiesOfClass"], ["magnet", "itementity"]),
        ("reliquary", "reliquary.item.FortuneCoinItem", ["scanForEntitiesInRange", "teleportEntityToPlayer", "pickupItems"], ["fortune", "coin", "pickup"]),
        ("sophisticated-core", "net.p3pp3rf1y.sophisticatedcore.upgrades.magnet.MagnetUpgradeWrapper", ["pickupItems", "tryToInsertItem"], ["magnet", "pickupitems"]),
        ("industrial-foregoing", "com.buuz135.industrial.item.infinity.item.ItemInfinityBackpack", ["inventoryTick", "getEntitiesOfClass"], ["backpack", "magnet", "itementity"]),
    ],
}


def request(url: str) -> bytes:
    req = urllib.request.Request(url, headers={"User-Agent": UA})
    with urllib.request.urlopen(req, timeout=180) as response:
        return response.read()


def download(url: str, path: Path):
    if not path.exists() or path.stat().st_size == 0:
        path.write_bytes(request(url))


def printable_strings(data: bytes) -> str:
    return " ".join(match.decode("latin1", "ignore").lower() for match in re.findall(rb"[ -~]{4,}", data))


def rank_candidates(jar: Path, expected_class: str, methods: list[str], keywords: list[str]):
    simple = expected_class.rsplit(".", 1)[-1].lower()
    tokens = [t.lower() for t in re.findall(r"[A-Z]?[a-z]+|[A-Z]+(?=[A-Z]|$)", expected_class.rsplit('.',1)[-1]) if len(t) >= 4]
    wanted = [w.lower() for w in keywords + methods]
    rows = []
    with zipfile.ZipFile(jar) as archive:
        for name in archive.namelist():
            if not name.endswith(".class") or name.startswith(("net/minecraft/", "org/", "com/google/")):
                continue
            lower = name.lower()
            name_score = (30 if simple in lower else 0) + sum(7 for token in tokens if token in lower)
            if name_score == 0 and not any(word in lower for word in keywords):
                # Reading every class is expensive; only inspect names that have some semantic overlap.
                continue
            try:
                strings = printable_strings(archive.read(name))
            except Exception:
                continue
            byte_score = sum(4 for word in wanted if word in strings)
            score = name_score + byte_score
            if score:
                rows.append((score, name, [word for word in wanted if word in strings]))
    rows.sort(key=lambda row: (-row[0], len(row[1]), row[1]))
    return rows[:15]


def javap(jar: Path, class_name: str) -> str:
    proc = subprocess.run(["javap", "-classpath", str(jar), "-p", "-s", "-c", class_name], text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT, timeout=60)
    return proc.stdout


def official_create_versions(game: str):
    url = f"https://maven.createmod.net/com/simibubi/create/create-{game}/maven-metadata.xml"
    try:
        root = ET.fromstring(request(url))
        versions = [node.text for node in root.findall(".//version") if node.text]
        return {"url": url, "latest": versions[-1] if versions else None, "recent": versions[-12:]}
    except Exception as exc:
        return {"url": url, "error": repr(exc)}


def main():
    base = json.loads((AUDIT / "integration-target-audit.json").read_text(encoding="utf-8"))
    report = {"targets": {}, "create": {}}
    for game in ("1.21.1", "1.20.1", "1.19.2", "1.18.2", "1.16.5"):
        report["create"][game] = official_create_versions(game)

    for target, active_rows in ACTIVE.items():
        target_data = base["targets"][target]
        out_rows = []
        for project, expected_class, methods, keywords in active_rows:
            resolution = target_data.get("projects", {}).get(project, {}).get("resolution", {})
            row = {"project": project, "expected_class": expected_class, "methods": methods, "resolution": resolution}
            url = resolution.get("download_url")
            if not url:
                row["status"] = "unresolved"
                out_rows.append(row)
                continue
            jar = JARS / f"{target}--{project}.jar"
            try:
                download(url, jar)
                with zipfile.ZipFile(jar) as archive:
                    entry = expected_class.replace(".", "/") + ".class"
                    found = entry in set(archive.namelist())
                row["class_found"] = found
                classes = [expected_class]
                if not found:
                    ranked = rank_candidates(jar, expected_class, methods, keywords)
                    row["candidates"] = [{"score": score, "class": name[:-6].replace("/", "."), "matched_strings": matches} for score, name, matches in ranked]
                    classes = [candidate["class"] for candidate in row["candidates"][:6]]
                row["javap"] = {}
                for candidate in classes:
                    text = javap(jar, candidate)
                    row["javap"][candidate] = text
                selected_text = "\n".join(row["javap"].values())
                row["method_presence"] = {method: method in selected_text for method in methods}
                row["status"] = "exact" if found else ("candidate-found" if classes else "no-candidate")
            except Exception as exc:
                row["status"] = "failed"
                row["error"] = repr(exc)
            out_rows.append(row)
        report["targets"][target] = out_rows

    (OUT / "deep-audit.json").write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")
    lines = ["# Deep registered-mixin audit", "", "This report checks only mixins registered by each branch and compares their target classes and method names with the newest resolved jar for that exact Minecraft/loader pair.", "", "## Official Create Maven metadata", "", "| Minecraft | Latest Maven artifact | Recent versions |", "|---|---|---|"]
    for game, row in report["create"].items():
        lines.append(f"| {game} | `{row.get('latest','unresolved')}` | {'<br>'.join('`'+v+'`' for v in row.get('recent',[])) or row.get('error','—')} |")
    for target, rows in report["targets"].items():
        lines += ["", f"## {target}", "", "| Integration | Resolved version | Target | Class | Required method strings |", "|---|---|---|---|---|"]
        for row in rows:
            candidate = "—"
            if row.get("candidates"):
                candidate = "<br>".join(f"`{c['class']}`" for c in row["candidates"][:4])
            class_status = "exact" if row.get("class_found") else ("candidate: " + candidate if candidate != "—" else row.get("status","missing"))
            methods = ", ".join(f"{name}: {'yes' if present else 'NO'}" for name, present in row.get("method_presence",{}).items()) or "—"
            lines.append(f"| {row['project']} | `{row.get('resolution',{}).get('version','unresolved')}` | `{row['expected_class']}` | {class_status} | {methods} |")
        for row in rows:
            if row.get("class_found") and all(row.get("method_presence",{}).values()):
                continue
            lines += ["", f"### {target}: {row['project']}", ""]
            if row.get("candidates"):
                lines.append("Candidate classes: " + ", ".join(f"`{c['class']}`" for c in row["candidates"][:8]))
                lines.append("")
            for class_name, text in row.get("javap",{}).items():
                # Keep only declarations and instructions mentioning the relevant method/search terms.
                selected = []
                for line in text.splitlines():
                    if any(token.lower() in line.lower() for token in row["methods"] + ["getEntities", "ItemEntity", "playerTouch", "setDeltaMovement", "teleport", "gravitate"]):
                        selected.append(line)
                lines += [f"#### `{class_name}`", "", "```text", "\n".join(selected[:240]) or "(no relevant signatures)", "```", ""]
    (OUT / "deep-audit.md").write_text("\n".join(lines) + "\n", encoding="utf-8")


if __name__ == "__main__":
    main()
