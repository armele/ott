package com.otterly76.ott.generation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.otterly76.ott.wood.ModWoodSets;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Merges src/main/resources/assets/ott/lang/en_us_base.json
 * and auto-adds wood-set translations, then writes:
 * src/generated/resources/assets/ott/lang/en_us.json
 */
public class OttLangMergeProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput output;

    public OttLangMergeProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        JsonObject merged = readBaseLang();

        for (ModWoodSets.WoodSet set : ModWoodSets.ALL) {
            addWoodSetEntries(merged, set.name());
        }

        Path out = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve("ott/lang/en_us.json");

        return DataProvider.saveStable(cachedOutput, merged, out);
    }

    @Override
    public @NotNull String getName() {
        return "OTT Lang (merge base + wood sets)";
    }

    @SuppressWarnings("DuplicatedCode")
    private JsonObject readBaseLang() {
        String basePath = "assets/ott/lang/en_us_base.json";

        try (var in = OttLangMergeProvider.class.getClassLoader().getResourceAsStream(basePath)) {
            if (in == null) throw new IllegalStateException("Missing " + basePath + " on classpath");

            JsonElement el = GSON.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), JsonElement.class);
            if (el == null || !el.isJsonObject()) throw new IllegalStateException(basePath + " is not a JSON object");
            return el.getAsJsonObject();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed reading base lang file: " + basePath, e);
        }
    }

    private void addWoodSetEntries(JsonObject out, String setName) {
        String prettySet = titleCase(setName);

        Map<String, String> blockKeys = Map.ofEntries(
                Map.entry("block.ott." + setName + "_log", prettySet + " Log"),
                Map.entry("block.ott." + setName + "_wood", prettySet + " Wood"),
                Map.entry("block.ott.stripped_" + setName + "_log", "Stripped " + prettySet + " Log"),
                Map.entry("block.ott.stripped_" + setName + "_wood", "Stripped " + prettySet + " Wood"),
                Map.entry("block.ott." + setName + "_planks", prettySet + " Planks"),
                Map.entry("block.ott." + setName + "_stairs", prettySet + " Stairs"),
                Map.entry("block.ott." + setName + "_slab", prettySet + " Slab"),
                Map.entry("block.ott." + setName + "_fence", prettySet + " Fence"),
                Map.entry("block.ott." + setName + "_fence_gate", prettySet + " Fence Gate"),
                Map.entry("block.ott." + setName + "_door", prettySet + " Door"),
                Map.entry("block.ott." + setName + "_trapdoor", prettySet + " Trapdoor"),
                Map.entry("block.ott." + setName + "_button", prettySet + " Button"),
                Map.entry("block.ott." + setName + "_pressure_plate", prettySet + " Pressure Plate"),
                Map.entry("block.ott." + setName + "_leaves", prettySet + " Leaves"),
                Map.entry("block.ott." + setName + "_sapling", prettySet + " Sapling"),
                Map.entry("block.ott.potted_" + setName + "_sapling", "Potted " + prettySet + " Sapling"),

                // Add sign blocks too (SignItem often uses block translation keys)
                Map.entry("block.ott." + setName + "_sign", prettySet + " Sign"),
                Map.entry("block.ott." + setName + "_wall_sign", prettySet + " Wall Sign"),
                Map.entry("block.ott." + setName + "_hanging_sign", prettySet + " Hanging Sign"),
                Map.entry("block.ott." + setName + "_wall_hanging_sign", prettySet + " Wall Hanging Sign")
        );

        Map<String, String> itemKeys = Map.ofEntries(
                Map.entry("item.ott." + setName + "_sign", prettySet + " Sign"),
                Map.entry("item.ott." + setName + "_hanging_sign", prettySet + " Hanging Sign"),
                Map.entry("item.ott." + setName + "_boat", prettySet + " Boat"),
                Map.entry("item.ott." + setName + "_chest_boat", prettySet + " Chest Boat")
        );

        blockKeys.forEach((k, v) -> putIfAbsent(out, k, v));
        itemKeys.forEach((k, v) -> putIfAbsent(out, k, v));
    }

    private void putIfAbsent(JsonObject obj, String key, String value) {
        if (!obj.has(key)) obj.addProperty(key, value);
    }

    private String titleCase(String id) {
        String[] parts = id.toLowerCase(Locale.ROOT).split("_+");
        StringBuilder sb = new StringBuilder();
        for (String p : parts) {
            if (p.isBlank()) continue;
            if (!sb.isEmpty()) sb.append(' ');
            sb.append(Character.toUpperCase(p.charAt(0))).append(p.substring(1));
        }
        return sb.toString();
    }
}