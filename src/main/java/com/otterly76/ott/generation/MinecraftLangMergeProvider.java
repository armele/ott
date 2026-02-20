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
import java.util.concurrent.CompletableFuture;

/**
 * Merges src/main/resources/assets/minecraft/lang/en_us_base.json
 * and writes src/generated/resources/assets/minecraft/lang/en_us.json
 */
public class MinecraftLangMergeProvider implements DataProvider {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput output;

    public MinecraftLangMergeProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cachedOutput) {
        JsonObject merged = readBaseLang("assets/minecraft/lang/en_us_base.json");
        JsonObject ottBase = readBaseLang("assets/ott/lang/en_us_base.json");

        // Merge OTT base into Minecraft base
        ottBase.entrySet().forEach(entry -> {
            if (!merged.has(entry.getKey())) {
                merged.add(entry.getKey(), entry.getValue());
            }
        });

        // Add auto-generated wood set entries
        for (ModWoodSets.WoodSet set : ModWoodSets.ALL) {
            addWoodSetEntries(merged, set.name());
        }

        Path out = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve("minecraft/lang/en_us.json");

        return DataProvider.saveStable(cachedOutput, merged, out);
    }

    private void addWoodSetEntries(JsonObject json, String name) {
        String capitalized = name.substring(0, 1).toUpperCase() + name.substring(1);
        json.addProperty("block.ott." + name + "_button", capitalized + " Button");
        json.addProperty("block.ott." + name + "_door", capitalized + " Door");
        json.addProperty("block.ott." + name + "_fence", capitalized + " Fence");
        json.addProperty("block.ott." + name + "_fence_gate", capitalized + " Fence Gate");
        json.addProperty("block.ott." + name + "_hanging_sign", capitalized + " Hanging Sign");
        json.addProperty("block.ott." + name + "_leaves", capitalized + " Leaves");
        json.addProperty("block.ott." + name + "_log", capitalized + " Log");
        json.addProperty("block.ott." + name + "_planks", capitalized + " Planks");
        json.addProperty("block.ott." + name + "_pressure_plate", capitalized + " Pressure Plate");
        json.addProperty("block.ott." + name + "_sapling", capitalized + " Sapling");
        json.addProperty("block.ott." + name + "_sign", capitalized + " Sign");
        json.addProperty("block.ott." + name + "_slab", capitalized + " Slab");
        json.addProperty("block.ott." + name + "_stairs", capitalized + " Stairs");
        json.addProperty("block.ott." + name + "_trapdoor", capitalized + " Trapdoor");
        json.addProperty("block.ott." + name + "_wood", capitalized + " Wood");
        json.addProperty("block.ott.stripped_" + name + "_log", "Stripped " + capitalized + " Log");
        json.addProperty("block.ott.stripped_" + name + "_wood", "Stripped " + capitalized + " Wood");
        json.addProperty("block.ott.potted_" + name + "_sapling", "Potted " + capitalized + " Sapling");
    }

    @Override
    public @NotNull String getName() {
        return "Minecraft Lang (merge base into en_us.json)";
    }

    @SuppressWarnings("DuplicatedCode")
    private JsonObject readBaseLang(String path) {
        try (var in = MinecraftLangMergeProvider.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) throw new IllegalStateException("Missing " + path + " on classpath");

            JsonElement el = GSON.fromJson(new InputStreamReader(in, StandardCharsets.UTF_8), JsonElement.class);
            if (el == null || !el.isJsonObject()) throw new IllegalStateException(path + " is not a JSON object");
            return el.getAsJsonObject();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Failed reading base lang file: " + path, e);
        }
    }
}