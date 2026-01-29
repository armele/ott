package com.otterly76.ott.neoforge.impl.data;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
        JsonObject merged = readBaseLang();

        Path out = output.getOutputFolder(PackOutput.Target.RESOURCE_PACK)
                .resolve("minecraft/lang/en_us.json");

        return DataProvider.saveStable(cachedOutput, merged, out);
    }

    @Override
    public @NotNull String getName() {
        return "Minecraft Lang (merge base into en_us.json)";
    }

    @SuppressWarnings("DuplicatedCode")
    private JsonObject readBaseLang() {
        String basePath = "assets/minecraft/lang/en_us_base.json";

        try (var in = MinecraftLangMergeProvider.class.getClassLoader().getResourceAsStream(basePath)) {
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
}




