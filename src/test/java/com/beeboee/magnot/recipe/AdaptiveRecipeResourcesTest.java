package com.beeboee.magnot.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AdaptiveRecipeResourcesTest {
    private static final String RECIPE_ROOT = "data/magnot/recipe/";

    @Test
    void expectedAdaptiveRecipeIdsArePresentAndLegacyIdsAreGone() {
        Set<String> expected = Set.of(
                "ferrous_paste_external_dust.json",
                "ferrous_paste_create_dust.json",
                "ferrous_paste_nugget_fallback.json",
                "ferrous_tube_plate.json",
                "ferrous_tube_ingot_fallback.json",
                "create_crushing_iron_dust.json"
        );

        for (String name : expected) {
            assertNotNull(resource(name), "Missing adaptive recipe resource: " + name);
        }

        assertNull(resource("ferrous_paste.json"));
        assertNull(resource("ferrous_tube.json"));
        assertNull(resource("iron_dust_from_crushing.json"));
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

    private static java.net.URL resource(String name) {
        return AdaptiveRecipeResourcesTest.class.getClassLoader().getResource(RECIPE_ROOT + name);
    }

    private static JsonObject read(String name) throws IOException {
        try (InputStream stream = AdaptiveRecipeResourcesTest.class.getClassLoader()
                .getResourceAsStream(RECIPE_ROOT + name)) {
            if (stream == null) {
                throw new IOException("Missing recipe resource: " + name);
            }
            return JsonParser.parseReader(new InputStreamReader(stream, StandardCharsets.UTF_8)).getAsJsonObject();
        }
    }
}
