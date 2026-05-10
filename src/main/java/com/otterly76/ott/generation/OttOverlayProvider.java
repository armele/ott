package com.otterly76.ott.generation;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Generates overlay model JSONs and merges entries into the two Fordite config files:
 * <ul>
 *   <li>{@code ott_overlay_modifiers/overlay_config.json} — block → overlay model path(s)
 *   <li>{@code ott_overlay_modifiers/tier_config.json}    — block → tier (z_order)
 * </ul>
 *
 * <p>Blocks generated here:
 * <ul>
 *   <li>{@code {color}_dyed_stone}         — 33 colors, tier 5
 *   <li>{@code {color}_dyed_cobblestone}   — 33 colors, tier 125
 *   <li>{@code {color}_concrete_powder}    — 17 custom OTT colors, tier 400
 *   <li>{@code {c1}_{c2}_concrete_powder}  — 240 vanilla DyeColor pairs, tier 400
 * </ul>
 */
public class OttOverlayProvider implements DataProvider {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final PackOutput packOutput;

    public OttOverlayProvider(PackOutput packOutput) {
        this.packOutput = packOutput;
    }

    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput cache) {
        Path mainPath  = packOutput.getOutputFolder().resolve("../../main/resources/assets/ott").normalize();
        Path modelsDir = mainPath.resolve("models/block/overlays");

        Map<String, List<String>> newOverlays = new LinkedHashMap<>();
        Map<String, Integer>      newTiers    = new LinkedHashMap<>();

        // ── dyed_stone — 33 colors, tier 5 ───────────────────────────────────
        for (ModPatterns.ColorInfo color : ModPatterns.ALL_COLORS) {
            String baseName = color.name() + "_dyed_stone";
            String blockId  = "ott:" + baseName;
            String model    = "ott:block/overlays/" + baseName + "_overlay";
            newOverlays.put(blockId, List.of(model));
            newTiers.put(blockId, 5);
            writeModel(cache, modelsDir.resolve(baseName + "_overlay.json"),
                    blockId, "ott:block/overlays/dyed_stone/" + color.name() + "_overflow");
        }

        // ── dyed_cobblestone — 33 colors, tier 125 ───────────────────────────
        for (ModPatterns.ColorInfo color : ModPatterns.ALL_COLORS) {
            String baseName = color.name() + "_dyed_cobblestone";
            String blockId  = "ott:" + baseName;
            String model    = "ott:block/overlays/" + baseName + "_overlay";
            newOverlays.put(blockId, List.of(model));
            newTiers.put(blockId, 125);
            writeModel(cache, modelsDir.resolve(baseName + "_overlay.json"),
                    blockId, "ott:block/overlays/dyed_cobblestone/" + color.name() + "_overflow");
        }

        // ── custom concrete_powder — 17 OTT colors, tier 400 ─────────────────
        for (ModColorSets.ColorSet colorSet : ModColorSets.ALL) {
            String baseName = colorSet.name() + "_concrete_powder";
            String blockId  = "ott:" + baseName;
            String model    = "ott:block/overlays/" + baseName + "_overlay";
            newOverlays.put(blockId, List.of(model));
            newTiers.put(blockId, 400);
            writeModel(cache, modelsDir.resolve(baseName + "_overlay.json"),
                    blockId, "ott:block/overlays/concrete_powder/" + colorSet.name() + "_overflow");
        }

        // ── gradient concrete_powder — 240 vanilla DyeColor pairs, tier 400 ──
        for (DyeColor c1 : DyeColor.values()) {
            for (DyeColor c2 : DyeColor.values()) {
                if (c1 == c2) continue;
                String baseName = c1.getName() + "_" + c2.getName() + "_concrete_powder";
                String blockId  = "ott:" + baseName;
                String model    = "ott:block/overlays/" + baseName + "_overlay";
                newOverlays.put(blockId, List.of(model));
                newTiers.put(blockId, 400);
                writeModel(cache, modelsDir.resolve(baseName + "_overlay.json"),
                        blockId, "ott:block/overlays/" + c1.getName() + "_concrete_powder_overflow");
            }
        }

        // ── merge into the two config files ───────────────────────────────────
        Path configDir = mainPath.resolve("ott_overlay_modifiers");
        mergeOverlayConfig(cache, configDir.resolve("overlay_config.json"), newOverlays);
        mergeTierConfig(cache, configDir.resolve("tier_config.json"), newTiers);

        return CompletableFuture.completedFuture(null);
    }

    // ── config mergers ────────────────────────────────────────────────────────

    private void mergeOverlayConfig(CachedOutput cache, Path file, Map<String, List<String>> newEntries) {
        JsonObject existing = readJsonObject(file);
        JsonObject overlays = existing.has("overlays") ? existing.getAsJsonObject("overlays") : new JsonObject();

        for (Map.Entry<String, List<String>> entry : newEntries.entrySet()) {
            JsonArray arr = new JsonArray();
            entry.getValue().forEach(arr::add);
            overlays.add(entry.getKey(), arr);
        }

        JsonObject sorted = sortedObject(overlays);
        JsonObject out = new JsonObject();
        out.add("overlays", sorted);
        writeJson(cache, file, out);
    }

    private void mergeTierConfig(CachedOutput cache, Path file, Map<String, Integer> newEntries) {
        JsonObject existing = readJsonObject(file);
        JsonObject tiers    = existing.has("tiers") ? existing.getAsJsonObject("tiers") : new JsonObject();

        for (Map.Entry<String, Integer> entry : newEntries.entrySet()) {
            tiers.addProperty(entry.getKey(), entry.getValue());
        }

        JsonObject sorted = sortedObject(tiers);
        JsonObject out = new JsonObject();
        out.add("tiers", sorted);
        writeJson(cache, file, out);
    }

    // ── overlay model writer ──────────────────────────────────────────────────

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
        json.addProperty("loader",  "ott:overlay");
        json.add("textures",        textures);
        json.add("connections",     connections);
        json.add("elements",        elements);

        writeJson(cache, file, json);
    }

    // ── utilities ─────────────────────────────────────────────────────────────

    private JsonObject readJsonObject(Path file) {
        if (Files.exists(file)) {
            try {
                return JsonParser.parseString(Files.readString(file)).getAsJsonObject();
            } catch (Exception ignored) {}
        }
        return new JsonObject();
    }

    /** Returns a new JsonObject with keys in ascending alphabetical order. */
    private JsonObject sortedObject(JsonObject src) {
        JsonObject out = new JsonObject();
        src.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(e -> out.add(e.getKey(), e.getValue()));
        return out;
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