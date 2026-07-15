package com.beeboee.magnot.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdaptiveRecipeResourcesTest {
    private static final Path RECIPES = Path.of("src/main/resources/data/magnot/recipe");

    @Test
    void expectedAdaptiveRecipeIdsArePresentAndLegacyIdsAreGone() throws IOException {
        Set<String> names;
        try (var stream = Files.list(RECIPES)) {
            names = stream.map(path -> path.getFileName().toString()).collect(java.util.stream.Collectors.toSet());
        }

        assertTrue(names.containsAll(Set.of(
                "ferrous_paste_external_dust.json",
                "ferrous_paste_create_dust.json",
                "ferrous_paste_nugget_fallback.json",
                "ferrous_tube_plate.json",
                "ferrous_tube_ingot_fallback.json",
                "create_crushing_iron_dust.json"
        )));
        assertFalse(names.contains("ferrous_paste.json"));
        assertFalse(names.contains("ferrous_tube.json"));
        assertFalse(names.contains("iron_dust_from_crushing.json"));
    }

    @Test
    void externalDustRecipeSubtractsMagnotFallbackDust() throws IOException {
        JsonObject recipe = read("ferrous_paste_external_dust.json");
        JsonObject ingredient = recipe.getAsJsonArray("ingredients").get(0).getAsJsonObject();

        assertEquals("neoforge:difference", ingredient.get("type").getAsString());
        assertEquals("c:dusts/iron", ingredient.getAsJsonObject("base").get("tag").getAsString());
        assertEquals("magnot:iron_dust", ingredient.getAsJsonObject("subtracted").get("item").getAsString());
    }

    @Test
    void nuggetFallbackIsEightNuggetsAroundSlime() throws IOException {
        JsonObject recipe = read("ferrous_paste_nugget_fallback.json");
        JsonArray pattern = recipe.getAsJsonArray("pattern");

        assertEquals("NNN", pattern.get(0).getAsString());
        assertEquals("NSN", pattern.get(1).getAsString());
        assertEquals("NNN", pattern.get(2).getAsString());
        assertEquals("c:nuggets/iron", recipe.getAsJsonObject("key").getAsJsonObject("N").get("tag").getAsString());
    }

    private static JsonObject read(String name) throws IOException {
        return JsonParser.parseString(Files.readString(RECIPES.resolve(name))).getAsJsonObject();
    }
}
