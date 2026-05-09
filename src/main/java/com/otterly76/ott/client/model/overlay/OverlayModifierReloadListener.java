package com.otterly76.ott.client.model.overlay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Loads terrain overlay modifier descriptors from
 * {@code assets/<namespace>/ott_overlay_modifiers/blocks/}.
 *
 * <p>Scanning is done eagerly inside registerModels() using the current
 * ResourceManager so the data is always fresh when model events fire,
 * regardless of NeoForge reload-listener ordering.
 *
 * <p>Each modifier file may include an optional {@code "z_order"} integer
 * (default 0).  Overlays are sorted by ascending z_order before being applied,
 * so higher values render on top at shared corners.  Files with equal z_order
 * maintain alphabetical order as a stable tiebreaker.
 */
public class OverlayModifierReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson   GSON   = new GsonBuilder().setLenient().create();
    private static final String PATH   = "ott_overlay_modifiers/blocks";

    public static final OverlayModifierReloadListener INSTANCE = new OverlayModifierReloadListener();

    /**
     * Maps each target block-state model location to the list of overlay model
     * locations that should be appended to it.  Populated in registerModels().
     */
    private final Map<ModelResourceLocation, List<ResourceLocation>> modifiers = new HashMap<>();

    private OverlayModifierReloadListener() {}

    /** Associates an overlay model location with a z-ordering priority. */
    private record ZOrderedOverlay(int zOrder, ResourceLocation loc) {}

    // ---- Model events -------------------------------------------------------

    /**
     * Scans modifier JSONs from the current resource manager, populates the
     * modifiers map, then registers all referenced overlay models as standalone
     * models.  Call from ModelEvent.RegisterAdditional.
     */
    public void registerModels(@NotNull ModelEvent.RegisterAdditional event) {
        ResourceManager rm = Minecraft.getInstance().getResourceManager();

        // Parse all modifier JSON files.
        // TreeMap sorted by string key ensures alphabetical processing order regardless of the
        // order scanDirectory returns files (NTFS / resource-manager ordering is not guaranteed).
        // Within equal z_order values, alphabetical file order is the stable tiebreaker.
        Map<ResourceLocation, JsonElement> resources = new TreeMap<>(Comparator.comparing(ResourceLocation::toString));
        SimpleJsonResourceReloadListener.scanDirectory(rm, PATH, GSON, resources);

        Map<ResourceLocation, List<ZOrderedOverlay>> plainParsed = new LinkedHashMap<>();
        Map<TagKey<Block>, List<ZOrderedOverlay>> tagParsed = new LinkedHashMap<>();
        for (Map.Entry<ResourceLocation, JsonElement> entry : resources.entrySet()) {
            try {
                parseEntry(entry.getValue(), plainParsed, tagParsed);
            } catch (JsonParseException e) {
                LOGGER.warn("[OTT] Failed to parse overlay modifier '{}': {}",
                        entry.getKey(), e.getMessage());
            }
        }

        // Expand plain block IDs and tag keys into block-state model locations
        modifiers.clear();
        for (Map.Entry<ResourceLocation, List<ZOrderedOverlay>> entry : plainParsed.entrySet()) {
            expandBlock(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<TagKey<Block>, List<ZOrderedOverlay>> entry : tagParsed.entrySet()) {
            var optTag = BuiltInRegistries.BLOCK.getTag(entry.getKey());
            if (optTag.isEmpty()) {
                LOGGER.warn("[OTT] Overlay modifier tag '#{}' not found — tags may not be loaded yet",
                        entry.getKey().location());
                continue;
            }
            for (var holder : optTag.get()) {
                ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(holder.value());
                expandBlock(blockId, entry.getValue());
            }
        }
        LOGGER.debug("[OTT] Overlay modifiers loaded for {} block-state entries", modifiers.size());

        // Register every distinct overlay model location so the baking system picks it up
        Set<ResourceLocation> models = new HashSet<>();
        for (List<ResourceLocation> overlays : modifiers.values()) {
            models.addAll(overlays);
        }
        for (ResourceLocation loc : models) {
            event.register(ModelResourceLocation.standalone(loc));
        }
    }

    /**
     * Wraps each target block-state model with an OverlayModifierBakedModel.
     * Call from ModelEvent.ModifyBakingResult.
     */
    public void applyModifiers(@NotNull ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> models = event.getModels();

        for (Map.Entry<ModelResourceLocation, List<ResourceLocation>> entry : modifiers.entrySet()) {
            ModelResourceLocation target = entry.getKey();
            BakedModel original = models.get(target);
            if (original == null) continue;

            List<BakedModel> overlayModels = new ArrayList<>();
            for (ResourceLocation overlayLoc : entry.getValue()) {
                BakedModel overlay = models.get(ModelResourceLocation.standalone(overlayLoc));
                if (overlay != null) {
                    overlayModels.add(overlay);
                } else {
                    LOGGER.warn("[OTT] Overlay model '{}' was not baked", overlayLoc);
                }
            }
            if (overlayModels.isEmpty()) continue;

            models.put(target, new OverlayModifierBakedModel(original, overlayModels));
        }
    }

    // ---- Helpers ------------------------------------------------------------

    private void expandBlock(ResourceLocation blockId, List<ZOrderedOverlay> overlays) {
        if (!BuiltInRegistries.BLOCK.containsKey(blockId)) {
            LOGGER.warn("[OTT] Overlay modifier target '{}' is not a registered block", blockId);
            return;
        }
        Block block = BuiltInRegistries.BLOCK.get(blockId);
        if (block == Blocks.AIR) return;

        // Sort by z_order (ascending); alphabetical insertion order is the stable tiebreaker.
        List<ResourceLocation> sorted = overlays.stream()
                .sorted(Comparator.comparingInt(ZOrderedOverlay::zOrder))
                .map(ZOrderedOverlay::loc)
                .toList();

        block.getStateDefinition().getPossibleStates().stream()
                .map(BlockModelShaper::stateToModelLocation)
                .forEach(mrl -> modifiers.computeIfAbsent(mrl, k -> new ArrayList<>()).addAll(sorted));
    }

    // ---- JSON parsing -------------------------------------------------------

    private static void parseEntry(JsonElement element,
                                   Map<ResourceLocation, List<ZOrderedOverlay>> blockResult,
                                   Map<TagKey<Block>, List<ZOrderedOverlay>> tagResult) {
        if (!element.isJsonObject())
            throw new JsonParseException("Overlay modifier entry must be a JSON object");
        JsonObject json = element.getAsJsonObject();

        // Optional z_order — determines rendering priority at shared corners.
        // Higher value = rendered last = appears on top.  Default 0.
        int zOrder = json.has("z_order") ? json.get("z_order").getAsInt() : 0;

        // Append
        if (!json.has("append") || !json.get("append").isJsonArray())
            throw new JsonParseException("Must have an 'append' array");
        List<ZOrderedOverlay> appendItems = new ArrayList<>();
        JsonArray appendArr = json.getAsJsonArray("append");
        for (JsonElement a : appendArr) {
            if (!a.isJsonPrimitive() || !a.getAsJsonPrimitive().isString())
                throw new JsonParseException("Each append entry must be a string");
            appendItems.add(new ZOrderedOverlay(zOrder, ResourceLocation.parse(a.getAsString())));
        }
        if (appendItems.isEmpty())
            throw new JsonParseException("'append' must not be empty");

        // Targets — plain block IDs or #tag references
        if (!json.has("targets") || !json.get("targets").isJsonArray())
            throw new JsonParseException("Must have a 'targets' array");
        JsonArray targetsArr = json.getAsJsonArray("targets");
        for (JsonElement t : targetsArr) {
            if (!t.isJsonPrimitive() || !t.getAsJsonPrimitive().isString())
                throw new JsonParseException("Each target must be a string");
            String id = t.getAsString();
            if (id.startsWith("#")) {
                String tagId = id.substring(1);
                if (!tagId.contains(":")) tagId = "minecraft:" + tagId;
                TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, ResourceLocation.parse(tagId));
                tagResult.computeIfAbsent(tagKey, k -> new ArrayList<>()).addAll(appendItems);
            } else {
                if (!id.contains(":")) id = "minecraft:" + id;
                blockResult.computeIfAbsent(ResourceLocation.parse(id), k -> new ArrayList<>()).addAll(appendItems);
            }
        }
    }
}
