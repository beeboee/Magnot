#!/usr/bin/env python3
from __future__ import annotations

import datetime as dt
import json
import os
import re
import subprocess
import sys
import urllib.error
import urllib.parse
import urllib.request
import zipfile
from pathlib import Path

ROOT = Path.cwd()
OUT = ROOT / "_audit" / "results"
JARS = OUT / "jars"
JAVAP = OUT / "javap"
OUT.mkdir(parents=True, exist_ok=True)
JARS.mkdir(parents=True, exist_ok=True)
JAVAP.mkdir(parents=True, exist_ok=True)

TARGETS = [
    {"key": "main-1.21.1-neoforge", "branch": "main", "mc": "1.21.1", "loader": "neoforge"},
    {"key": "forge-1.20.1", "branch": "1.20.1-forge", "mc": "1.20.1", "loader": "forge"},
    {"key": "fabric-1.20.1", "branch": "1.20.1-fabric", "mc": "1.20.1", "loader": "fabric"},
    {"key": "forge-1.19.2", "branch": "1.19.2-forge", "mc": "1.19.2", "loader": "forge"},
    {"key": "forge-1.18.2", "branch": "1.18.2-forge", "mc": "1.18.2", "loader": "forge"},
    {"key": "forge-1.16.5", "branch": "1.16.5-forge", "mc": "1.16.5", "loader": "forge"},
    {"key": "forge-1.12.2", "branch": "1.12.2-forge-legacy", "mc": "1.12.2", "loader": "forge"},
]

PROJECTS = {
    "sophisticated-core": {"title": "Sophisticated Core", "modrinth": "sophisticated-core", "curseforge": "sophisticated-core"},
    "ae2wtlib": {"title": "AE2 Wireless Terminals", "modrinth": "ae2wtlib", "curseforge": "ae2wtlib"},
    "projecte": {"title": "ProjectE", "modrinth": "projecte", "curseforge": "projecte"},
    "artifacts": {"title": "Artifacts", "modrinth": "artifacts", "curseforge": "artifacts"},
    "mekanism": {"title": "Mekanism", "modrinth": "mekanism", "curseforge": "mekanism"},
    "draconic-evolution": {"title": "Draconic Evolution", "modrinth": "draconic-evolution", "curseforge": "draconic-evolution"},
    "reliquary": {"title": "Reliquary Reincarnations", "modrinth": "reliquary-reincarnations", "curseforge": "reliquary-reincarnations"},
    "mob-grinding-utils": {"title": "Mob Grinding Utils", "modrinth": "mob-grinding-utils", "curseforge": "mob-grinding-utils"},
    "item-collectors": {"title": "Item Collectors", "modrinth": "item-collectors", "curseforge": "item-collectors"},
    "simple-magnets": {"title": "Simple Magnets", "modrinth": "simple-magnets", "curseforge": "simple-magnets"},
    "modular-routers": {"title": "Modular Routers", "modrinth": "modular-routers", "curseforge": "modular-routers"},
    "enderio": {"title": "Ender IO", "modrinth": "enderio", "curseforge": "ender-io"},
    "industrial-foregoing": {"title": "Industrial Foregoing", "modrinth": "industrial-foregoing", "curseforge": "industrial-foregoing"},
}

PREFIX_PROJECT = [
    ("net.p3pp3rf1y.sophisticatedcore.", "sophisticated-core"),
    ("de.mari_023.ae2wtlib.", "ae2wtlib"),
    ("moze_intel.projecte.", "projecte"),
    ("artifacts.", "artifacts"),
    ("mekanism.", "mekanism"),
    ("com.brandon3055.draconicevolution.", "draconic-evolution"),
    ("reliquary.", "reliquary"),
    ("mob_grinding_utils.", "mob-grinding-utils"),
    ("com.supermartijn642.itemcollectors.", "item-collectors"),
    ("com.supermartijn642.simplemagnets.", "simple-magnets"),
    ("me.desht.modularrouters.", "modular-routers"),
    ("com.enderio.", "enderio"),
    ("com.buuz135.industrial.", "industrial-foregoing"),
]

CREATE_PROJECT = {"fabric": "create-fabric", "forge": "create", "neoforge": "create"}
USER_AGENT = "Magnot integration compatibility audit/1.0 (github.com/beeboee/Magnot)"
CF_TOKEN = os.environ.get("CURSEFORGE_TOKEN", "").strip()


def request_json(url: str, headers: dict[str, str] | None = None):
    merged = {"User-Agent": USER_AGENT, "Accept": "application/json"}
    if headers:
        merged.update(headers)
    request = urllib.request.Request(url, headers=merged)
    with urllib.request.urlopen(request, timeout=45) as response:
        return json.load(response)


def download(url: str, output: Path):
    output.parent.mkdir(parents=True, exist_ok=True)
    request = urllib.request.Request(url, headers={"User-Agent": USER_AGENT})
    with urllib.request.urlopen(request, timeout=180) as response, output.open("wb") as out:
        while True:
            chunk = response.read(1024 * 1024)
            if not chunk:
                break
            out.write(chunk)


def modrinth_versions(slug: str, mc: str, loader: str):
    loaders = urllib.parse.quote(json.dumps([loader], separators=(",", ":")))
    games = urllib.parse.quote(json.dumps([mc], separators=(",", ":")))
    url = f"https://api.modrinth.com/v2/project/{urllib.parse.quote(slug)}/version?loaders={loaders}&game_versions={games}"
    try:
        return request_json(url), None
    except urllib.error.HTTPError as error:
        return [], f"HTTP {error.code}"
    except Exception as error:
        return [], str(error)


def modrinth_search(title: str):
    query = urllib.parse.urlencode({"query": title, "limit": 20})
    try:
        data = request_json(f"https://api.modrinth.com/v2/search?{query}")
        return data.get("hits", []), None
    except Exception as error:
        return [], str(error)


def normalize(value: str) -> str:
    return re.sub(r"[^a-z0-9]+", "", value.lower())


def resolve_modrinth(project: dict, mc: str, loader: str):
    candidates = [project["modrinth"]]
    hits, search_error = modrinth_search(project["title"])
    desired = normalize(project["title"])
    ranked = sorted(
        hits,
        key=lambda hit: (
            normalize(hit.get("title", "")) != desired,
            normalize(hit.get("slug", "")) != normalize(project["modrinth"]),
            -int(hit.get("downloads", 0)),
        ),
    )
    candidates.extend(hit.get("project_id") or hit.get("slug") for hit in ranked if hit.get("project_id") or hit.get("slug"))
    seen = set()
    errors = []
    for candidate in candidates:
        if not candidate or candidate in seen:
            continue
        seen.add(candidate)
        versions, error = modrinth_versions(candidate, mc, loader)
        if versions:
            version = versions[0]
            files = version.get("files", [])
            jar = next((f for f in files if f.get("primary") and str(f.get("filename", "")).endswith(".jar")), None)
            jar = jar or next((f for f in files if str(f.get("filename", "")).endswith(".jar")), None)
            return {
                "source": "modrinth",
                "project": candidate,
                "version": version.get("version_number"),
                "version_id": version.get("id"),
                "published": version.get("date_published"),
                "version_type": version.get("version_type"),
                "download_url": jar.get("url") if jar else None,
                "filename": jar.get("filename") if jar else None,
                "all_matching_versions": len(versions),
            }
        if error:
            errors.append(f"{candidate}: {error}")
    return {"source": "modrinth", "error": "; ".join(errors) or search_error or "no matching version"}


def cf_json(url: str):
    if not CF_TOKEN:
        raise RuntimeError("CURSEFORGE_TOKEN unavailable")
    last = None
    for header in ("x-api-key", "X-Api-Token"):
        try:
            return request_json(url, {header: CF_TOKEN})
        except Exception as error:
            last = error
    raise last or RuntimeError("CurseForge request failed")


def resolve_curseforge(project: dict, mc: str, loader: str):
    if not CF_TOKEN:
        return {"source": "curseforge", "error": "token unavailable"}
    try:
        query = urllib.parse.urlencode({"gameId": 432, "slug": project["curseforge"], "pageSize": 20})
        search = cf_json(f"https://api.curseforge.com/v1/mods/search?{query}")
        mods = search.get("data", [])
        if not mods:
            query = urllib.parse.urlencode({"gameId": 432, "searchFilter": project["title"], "pageSize": 50})
            mods = cf_json(f"https://api.curseforge.com/v1/mods/search?{query}").get("data", [])
        if not mods:
            return {"source": "curseforge", "error": "project not found"}
        desired_slug = normalize(project["curseforge"])
        desired_title = normalize(project["title"])
        mods.sort(key=lambda mod: (normalize(mod.get("slug", "")) != desired_slug, normalize(mod.get("name", "")) != desired_title))
        mod = mods[0]
        files_url = f"https://api.curseforge.com/v1/mods/{mod['id']}/files?{urllib.parse.urlencode({'gameVersion': mc, 'pageSize': 50})}"
        files = cf_json(files_url).get("data", [])
        loader_name = {"forge": "forge", "fabric": "fabric", "neoforge": "neoforge"}[loader]
        matching = []
        for file in files:
            labels = [str(v).lower().replace(" ", "") for v in file.get("gameVersions", [])]
            if mc.lower() not in labels:
                continue
            has_other = any(name in labels for name in ("forge", "fabric", "neoforge"))
            if has_other and loader_name not in labels:
                continue
            matching.append(file)
        matching.sort(key=lambda file: file.get("fileDate", ""), reverse=True)
        if not matching:
            return {"source": "curseforge", "project_id": mod.get("id"), "error": "no matching file"}
        file = matching[0]
        url = file.get("downloadUrl")
        if not url:
            try:
                url = cf_json(f"https://api.curseforge.com/v1/mods/{mod['id']}/files/{file['id']}/download-url").get("data")
            except Exception:
                url = None
        return {
            "source": "curseforge",
            "project": mod.get("slug"),
            "project_id": mod.get("id"),
            "version": file.get("displayName") or file.get("fileName"),
            "version_id": file.get("id"),
            "published": file.get("fileDate"),
            "version_type": file.get("releaseType"),
            "download_url": url,
            "filename": file.get("fileName"),
            "all_matching_versions": len(matching),
        }
    except Exception as error:
        return {"source": "curseforge", "error": str(error)}


def resolve_project(project: dict, mc: str, loader: str):
    result = resolve_modrinth(project, mc, loader)
    if result.get("download_url"):
        return result
    fallback = resolve_curseforge(project, mc, loader)
    if fallback.get("download_url") or not fallback.get("error"):
        fallback["modrinth_error"] = result.get("error")
        return fallback
    result["curseforge_error"] = fallback.get("error")
    return result


def git(*args: str, check: bool = True) -> str:
    completed = subprocess.run(["git", *args], cwd=ROOT, check=check, text=True, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    return completed.stdout


def branch_text(branch: str, path: str) -> str | None:
    ref = "HEAD" if branch == "main" else f"origin/{branch}"
    completed = subprocess.run(["git", "show", f"{ref}:{path}"], cwd=ROOT, text=True, stdout=subprocess.PIPE, stderr=subprocess.DEVNULL)
    return completed.stdout if completed.returncode == 0 else None


def branch_paths(branch: str) -> list[str]:
    ref = "HEAD" if branch == "main" else f"origin/{branch}"
    output = git("ls-tree", "-r", "--name-only", ref)
    return output.splitlines()


def project_for_class(class_name: str) -> str | None:
    for prefix, project in PREFIX_PROJECT:
        if class_name.startswith(prefix):
            return project
    return None


def extract_mixin_targets(branch: str):
    results = []
    for path in branch_paths(branch):
        if not path.endswith(".java") or "mixin" not in path.lower():
            continue
        text = branch_text(branch, path)
        if not text:
            continue
        for match in re.finditer(r"@Mixin\s*\(\s*targets\s*=\s*\{?\s*\"([^\"]+)\"", text, re.S):
            class_name = match.group(1)
            project = project_for_class(class_name)
            if project:
                results.append({"path": path, "class": class_name, "project": project})
    unique = []
    seen = set()
    for row in results:
        key = (row["path"], row["class"])
        if key not in seen:
            seen.add(key)
            unique.append(row)
    return unique


def declared_create(branch: str):
    rows = []
    for path in branch_paths(branch):
        if not (path.endswith(".toml") or path.endswith(".json") or path.endswith(".properties") or path.endswith(".gradle")):
            continue
        text = branch_text(branch, path)
        if not text or "create" not in text.lower():
            continue
        for line in text.splitlines():
            if "create" in line.lower() and any(token in line.lower() for token in ("version", "create(required)", "modid=\"create\"", "depends")):
                rows.append(f"{path}: {line.strip()}")
    return rows[:30]


def inspect_jar(jar_path: Path, classes: list[str], target_key: str, project_key: str):
    result = {"jar": jar_path.name, "classes": {}}
    with zipfile.ZipFile(jar_path) as archive:
        names = archive.namelist()
        name_set = set(names)
        lower_names = [name.lower() for name in names if name.endswith(".class")]
        for class_name in classes:
            entry = class_name.replace(".", "/") + ".class"
            row = {"entry": entry, "found": entry in name_set}
            if row["found"]:
                output_dir = JAVAP / target_key
                output_dir.mkdir(parents=True, exist_ok=True)
                output_file = output_dir / f"{project_key}-{class_name.rsplit('.', 1)[-1]}.txt"
                completed = subprocess.run(
                    ["javap", "-classpath", str(jar_path), "-p", "-s", "-c", class_name],
                    text=True,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.STDOUT,
                )
                output_file.write_text(completed.stdout, encoding="utf-8")
                row["javap"] = str(output_file.relative_to(ROOT))
                row["javap_exit"] = completed.returncode
            else:
                simple = class_name.rsplit(".", 1)[-1].lower()
                words = [word.lower() for word in re.findall(r"[A-Z]?[a-z]+|[A-Z]+(?=[A-Z]|$)", class_name.rsplit('.', 1)[-1]) if len(word) >= 4]
                candidates = [name for name in lower_names if simple in name.rsplit("/", 1)[-1]][:25]
                if not candidates:
                    candidates = [name for name in lower_names if any(word in name.rsplit("/", 1)[-1] for word in words)][0:25]
                row["candidates"] = candidates
            result["classes"][class_name] = row
    return result


def main():
    branches = [target["branch"] for target in TARGETS if target["branch"] != "main"]
    git("fetch", "--no-tags", "origin", *[f"+refs/heads/{branch}:refs/remotes/origin/{branch}" for branch in branches])

    report = {
        "generated_at": dt.datetime.now(dt.timezone.utc).isoformat(),
        "targets": {},
        "create": {},
    }

    for target in TARGETS:
        key = target["key"]
        mixins = extract_mixin_targets(target["branch"])
        report["targets"][key] = {
            **target,
            "declared_create": declared_create(target["branch"]),
            "mixins": mixins,
            "projects": {},
        }

        create_slug = CREATE_PROJECT[target["loader"]]
        create_result = resolve_project({"title": "Create Fabric" if target["loader"] == "fabric" else "Create", "modrinth": create_slug, "curseforge": create_slug}, target["mc"], target["loader"])
        report["create"][key] = create_result

        by_project: dict[str, list[str]] = {}
        for mixin in mixins:
            by_project.setdefault(mixin["project"], []).append(mixin["class"])

        for project_key, classes in sorted(by_project.items()):
            project = PROJECTS[project_key]
            resolution = resolve_project(project, target["mc"], target["loader"])
            project_row = {"resolution": resolution, "targets": sorted(set(classes))}
            if resolution.get("download_url"):
                filename = resolution.get("filename") or f"{project_key}.jar"
                safe = re.sub(r"[^A-Za-z0-9._+-]+", "_", filename)
                jar_path = JARS / key / f"{project_key}--{safe}"
                try:
                    download(resolution["download_url"], jar_path)
                    project_row["inspection"] = inspect_jar(jar_path, sorted(set(classes)), key, project_key)
                except Exception as error:
                    project_row["inspection_error"] = str(error)
            report["targets"][key]["projects"][project_key] = project_row

    json_path = OUT / "integration-target-audit.json"
    json_path.write_text(json.dumps(report, indent=2) + "\n", encoding="utf-8")

    lines = [
        "# Magnot integration target audit",
        "",
        f"Generated: {report['generated_at']}",
        "",
        "This report resolves the newest published file for each exact Minecraft/loader pair and inspects the actual jar bytecode for Magnot's declared mixin target classes.",
        "",
        "## Create version matrix",
        "",
        "| Target | Latest resolved Create | Source | Published | Current declarations |",
        "|---|---|---|---|---|",
    ]
    for target in TARGETS:
        key = target["key"]
        row = report["create"][key]
        declarations = "<br>".join(report["targets"][key]["declared_create"][:5]) or "—"
        lines.append(f"| `{key}` | `{row.get('version', 'unresolved')}` | {row.get('source', '—')} | {row.get('published', '—')} | {declarations.replace('|', '\\|')} |")

    for target in TARGETS:
        key = target["key"]
        data = report["targets"][key]
        lines += ["", f"## {key}", "", "| Integration | Latest resolved version | Target classes | Bytecode result |", "|---|---|---:|---|"]
        for project_key, project_row in sorted(data["projects"].items()):
            resolution = project_row["resolution"]
            inspection = project_row.get("inspection", {})
            class_rows = inspection.get("classes", {})
            found = sum(1 for row in class_rows.values() if row.get("found"))
            total = len(project_row["targets"])
            if resolution.get("download_url"):
                status = f"{found}/{total} target classes found"
                missing = [name for name, row in class_rows.items() if not row.get("found")]
                if missing:
                    status += "; missing: " + ", ".join(f"`{name}`" for name in missing)
            else:
                status = "unresolved: " + str(resolution.get("error") or resolution.get("curseforge_error") or "no downloadable file")
            lines.append(f"| {PROJECTS[project_key]['title']} | `{resolution.get('version', 'unresolved')}` | {total} | {status.replace('|', '\\|')} |")

    md_path = OUT / "integration-target-audit.md"
    md_path.write_text("\n".join(lines) + "\n", encoding="utf-8")
    print(md_path)


if __name__ == "__main__":
    try:
        main()
    except Exception as error:
        print(f"AUDIT FAILED: {error}", file=sys.stderr)
        raise
