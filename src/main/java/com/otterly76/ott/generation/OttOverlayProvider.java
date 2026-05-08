package com.otterly76.ott.generation;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.otterly76.ott.color.ModColorSets;
import com.otterly76.ott.color.ModPatterns;
import net.minecraft.data.CachedOutput;
import net.minecraft.world.item.DyeColor;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Generates overlay model JSONs and overlay modifier JSONs for pattern blocks:
 * <ul>
 *   <li>{@code {color}_dyed_stone} — all 33 colors, inherits stone targets
 *   <li>{@code {color}_dyed_cobblestone} — all 33 colors, inherits cobblestone targets
 *   <li>{@code {color}_concrete_powder} — 17 custom colors, inherits white_concrete_powder targets
 * </ul>
 *
 * <p>Textures are generated separately by {@link ColorSetTextureProvider}.
 */
public class OttOverlayProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Patterns generated for ALL 33 colors (16 vanilla DyeColors + 17 custom ColorSets).
     * {@code baseModifier} is the name of the existing modifier JSON whose target list to inherit.
     * {@code texSubdir}   is the subdirectory under {@code overlays/} where tinted textures live.
     */
    private record PatternConfig(String blockSuffix, String baseModifier, String texSubdir) {}

    private static final List<PatternConfig> ALL_COLOR_PATTERNS = List.of(
            new PatternConfig("dyed_cobblestone", "cobblestone_overflow", "dyed_cobblestone")
    );

    private final PackOutput packOutput;

    public OttOverlayProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        Path mainPath    = packOutput.getOutputFolder().resolve("../../main/resources/assets/ott").normalize();
        Path modifiersDir = mainPath.resolve("ott_overlay_modifiers/blocks");
        Path modelsDir    = mainPath.resolve("models/block/overlays");

        // dyed_stone + dyed_cobblestone — all 33 colors
        for (PatternConfig cfg : ALL_COLOR_PATTERNS) {
            List<String> targets = readTargets(modifiersDir.resolve(cfg.baseModifier() + ".json"));
            for (ModPatterns.ColorInfo color : ModPatterns.ALL_COLORS) {
                String baseName = color.name() + "_" + cfg.blockSuffix();
                writeModifier(cache, modifiersDir.resolve(baseName + "_overflow.json"),
                        targets, "ott:block/overlays/" + baseName + "_overlay");
                writeModel(cache, modelsDir.resolve(baseName + "_overlay.json"),
                        "ott:" + baseName,
                        "ott:block/overlays/" + cfg.texSubdir() + "/" + color.name() + "_overflow");
            }
        }

        // concrete_powder — 17 custom colors only
        List<String> cpTargets = readTargets(modifiersDir.resolve("white_concrete_powder_overflow.json"));
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String baseName = colorSet.name() + "_concrete_powder";
            writeModifier(cache, modifiersDir.resolve(baseName + "_overflow.json"),
                    cpTargets, "ott:block/overlays/" + baseName + "_overlay");
            writeModel(cache, modelsDir.resolve(baseName + "_overlay.json"),
                    "ott:" + baseName,
                    "ott:block/overlays/concrete_powder/" + colorSet.name() + "_overflow");
        }

        // gradient concrete_powder — all 240 DyeColor pairs
        // Targets are the same explicit list used by the OTT custom-color concrete powder overflows.
        // Texture: reuses the first-color vanilla overlay atlas (96×48 6×3 tile format).
        // TODO: replace with generated per-gradient overlay atlases for accurate gradient blending.
        List<String> gradCpTargets = readTargets(modifiersDir.resolve("amethyst_concrete_powder_overflow.json"));
        for (DyeColor c1 : DyeColor.values()) {
            for (DyeColor c2 : DyeColor.values()) {
                if (c1 == c2) continue;
                String baseName = c1.getName() + "_" + c2.getName() + "_concrete_powder";
                writeModifier(cache, modifiersDir.resolve(baseName + "_overflow.json"),
                        gradCpTargets, "ott:block/overlays/" + baseName + "_overlay");
                writeModel(cache, modelsDir.resolve(baseName + "_overlay.json"),
                        "ott:" + baseName,
                        "ott:block/overlays/" + c1.getName() + "_concrete_powder_overflow");
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    // ---- helpers ------------------------------------------------------------

    /** Reads the {@code "targets"} array from an existing modifier JSON on disk. */
    private static List<String> readTargets(Path file) {
        try {
            JsonObject json = JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            List<String> targets = new ArrayList<>();
            for (JsonElement e : json.getAsJsonArray("targets")) {
                targets.add(e.getAsString());
            }
            return targets;
        } catch (IOException e) {
            throw new RuntimeException("Failed to read targets from " + file, e);
        }
    }

    private void writeModifier(CachedOutput cache, Path file, List<String> targets, String appendModel) {
        JsonArray targetsArr = new JsonArray();
        targets.forEach(targetsArr::add);
        JsonArray appendArr = new JsonArray();
        appendArr.add(appendModel);

        JsonObject json = new JsonObject();
        json.add("targets", targetsArr);
        json.add("append",  appendArr);

        writeJson(cache, file, json);
    }

    private void writeModel(CachedOutput cache, Path file, String blockId, String textureRef) {
        JsonObject textures = new JsonObject();
        textures.addProperty("all",      textureRef);
        textures.addProperty("particle", "#all");

        JsonObject matchBlock = new JsonObject();
        matchBlock.addProperty("type",  "match_block");
        matchBlock.addProperty("block", blockId);

        JsonObject isFaceVisible = new JsonObject();
        isFaceVisible.addProperty("type", "is_face_visible");

        JsonArray predicates = new JsonArray();
        predicates.add(matchBlock);
        predicates.add(isFaceVisible);

        JsonObject connections = new JsonObject();
        connections.addProperty("type", "and");
        connections.add("predicates", predicates);

        JsonObject faces = new JsonObject();
        for (String face : new String[]{"up", "down", "north", "east", "south", "west"}) {
            JsonObject f = new JsonObject();
            f.addProperty("texture",  "#all");
            f.addProperty("cullface", face);
            faces.add(face, f);
        }

        JsonArray from = new JsonArray(); from.add(0);  from.add(0);  from.add(0);
        JsonArray to   = new JsonArray(); to.add(16);   to.add(16);   to.add(16);

        JsonObject element = new JsonObject();
        element.add("from",  from);
        element.add("to",    to);
        element.add("faces", faces);

        JsonArray elements = new JsonArray();
        elements.add(element);

        JsonObject json = new JsonObject();
        json.addProperty("loader",      "ott:overlay");
        json.add("textures",            textures);
        json.add("connections",         connections);
        json.add("elements",            elements);

        writeJson(cache, file, json);
    }

    private void writeJson(CachedOutput cache, Path file, JsonObject json) {
        byte[] bytes = GSON.toJson(json).getBytes(StandardCharsets.UTF_8);
        try {
            cache.writeIfNeeded(file, bytes, Hashing.sha256().hashBytes(bytes));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write " + file, e);
        }
    }

    @Override
    @NotNull
    public String getName() {
        return "OTT Overlay Provider";
    }
}
