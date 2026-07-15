#!/usr/bin/env python3
"""Static truth-table verification for Magnot's conditional recipe resources."""

from __future__ import annotations

import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RECIPES = ROOT / "src/main/resources/data/magnot/recipe"


def condition_matches(condition: dict, *, create: bool, external_dust: bool, plate: bool) -> bool:
    kind = condition["type"]
    if kind == "magnot:material_available":
        actual = {
            "external_iron_dust": external_dust,
            "iron_plate": plate,
        }[condition["material"]]
        return actual is condition.get("expected", True)
    if kind == "neoforge:mod_loaded":
        return create if condition["modid"] == "create" else False
    if kind == "neoforge:not":
        return not condition_matches(
            condition["value"], create=create, external_dust=external_dust, plate=plate
        )
    raise AssertionError(f"Unknown condition type: {kind}")


def active_recipes(*, create: bool, external_dust: bool, plate: bool) -> set[str]:
    active: set[str] = set()
    for path in RECIPES.glob("*.json"):
        recipe = json.loads(path.read_text(encoding="utf-8"))
        conditions = recipe.get("neoforge:conditions", [])
        if all(
            condition_matches(c, create=create, external_dust=external_dust, plate=plate)
            for c in conditions
        ):
            active.add(path.stem)
    return active


def verify() -> None:
    paste = {
        "ferrous_paste_external_dust",
        "ferrous_paste_create_dust",
        "ferrous_paste_nugget_fallback",
    }
    tube = {"ferrous_tube_plate", "ferrous_tube_ingot_fallback"}
    dust_production = {"create_crushing_iron_dust"}

    for create in (False, True):
        for external in (False, True):
            for plate in (False, True):
                active = active_recipes(create=create, external_dust=external, plate=plate)
                expected_paste = (
                    "ferrous_paste_external_dust"
                    if external
                    else "ferrous_paste_create_dust"
                    if create
                    else "ferrous_paste_nugget_fallback"
                )
                expected_tube = "ferrous_tube_plate" if plate else "ferrous_tube_ingot_fallback"
                expected_dust = {"create_crushing_iron_dust"} if create and not external else set()

                assert active & paste == {expected_paste}, (create, external, plate, active)
                assert active & tube == {expected_tube}, (create, external, plate, active)
                assert active & dust_production == expected_dust, (create, external, plate, active)

    external_recipe = json.loads((RECIPES / "ferrous_paste_external_dust.json").read_text())
    dust = external_recipe["ingredients"][0]
    assert dust == {
        "type": "neoforge:difference",
        "base": {"tag": "c:dusts/iron"},
        "subtracted": {"item": "magnot:iron_dust"},
    }

    print("Adaptive recipe truth table verified for all 8 dust/Create/plate combinations.")


if __name__ == "__main__":
    verify()
