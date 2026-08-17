package com.whammich.invasion.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * P3 / D-41..D-45: Flux conversion recipes match 1.7 Invasion.java GameRegistry shapes.
 */
class FluxConversionRecipesTest {

    @Test
    void diamondUsesFourFluxCross() {
        JsonObject r = load("data/invasion/recipes/flux_to_diamond.json");
        assertEquals("minecraft:crafting_shaped", r.get("type").getAsString());
        assertPattern(r, " X ", "X X", " X ");
        assertResult(r, "minecraft:diamond", 1);
        assertFluxKey(r);
    }

    @Test
    void ironUsesOneFluxCenter() {
        JsonObject r = load("data/invasion/recipes/flux_to_iron_ingot.json");
        assertPattern(r, "   ", " X ", "   ");
        assertResult(r, "minecraft:iron_ingot", 4);
        assertFluxKey(r);
    }

    @Test
    void redstoneUsesTwoFluxMiddleRow() {
        JsonObject r = load("data/invasion/recipes/flux_to_redstone.json");
        assertPattern(r, "   ", "X X", "   ");
        assertResult(r, "minecraft:redstone", 24);
        assertFluxKey(r);
    }

    @Test
    void lapisUsesTwoFluxVertical() {
        JsonObject r = load("data/invasion/recipes/flux_to_lapis.json");
        assertPattern(r, " X ", "   ", " X ");
        assertResult(r, "minecraft:lapis_lazuli", 12);
        assertFluxKey(r);
    }

    private static void assertFluxKey(JsonObject r) {
        JsonObject key = r.getAsJsonObject("key").getAsJsonObject("X");
        assertEquals("invasion:rift_flux", key.get("item").getAsString());
    }

    private static void assertPattern(JsonObject r, String... rows) {
        JsonArray pattern = r.getAsJsonArray("pattern");
        assertEquals(rows.length, pattern.size());
        for (int i = 0; i < rows.length; i++) {
            assertEquals(rows[i], pattern.get(i).getAsString());
        }
    }

    private static void assertResult(JsonObject r, String item, int count) {
        JsonObject result = r.getAsJsonObject("result");
        assertEquals(item, result.get("item").getAsString());
        assertEquals(count, result.get("count").getAsInt());
    }

    private static JsonObject load(String classpath) {
        InputStream in = FluxConversionRecipesTest.class.getClassLoader().getResourceAsStream(classpath);
        assertNotNull(in, "missing resource " + classpath);
        return JsonParser.parseReader(new InputStreamReader(in, StandardCharsets.UTF_8)).getAsJsonObject();
    }
}
