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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Loads terrain overlay modifier descriptors from
 * {@code assets/<namespace>/ott_overlay_modifiers/blocks/}.
 *
 * <p>Scanning is done eagerly inside registerModels() using the current
 * ResourceManager so the data is always fresh when model events fire,
 * regardless of NeoForge reload-listener ordering.
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

    // ---- Model events -------------------------------------------------------

    /**
     * Scans modifier JSONs from the current resource manager, populates the
     * modifiers map, then registers all referenced overlay models as standalone
     * models.  Call from ModelEvent.RegisterAdditional.
     */
    public void registerModels(@NotNull ModelEvent.RegisterAdditional event) {
        ResourceManager rm = Minecraft.getInstance().getResourceManager();

        // Parse all modifier JSON files
        Map<ResourceLocation, JsonElement> resources = new HashMap<>();
        SimpleJsonResourceReloadListener.scanDirectory(rm, PATH, GSON, resources);

        Map<ResourceLocation, List<ResourceLocation>> plainParsed = new HashMap<>();
        Map<TagKey<Block>, List<ResourceLocation>> tagParsed = new HashMap<>();
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
        for (Map.Entry<ResourceLocation, List<ResourceLocation>> entry : plainParsed.entrySet()) {
            expandBlock(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<TagKey<Block>, List<ResourceLocation>> entry : tagParsed.entrySet()) {
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

    private void expandBlock(ResourceLocation blockId, List<ResourceLocation> overlays) {
        if (!BuiltInRegistries.BLOCK.containsKey(blockId)) {
            LOGGER.warn("[OTT] Overlay modifier target '{}' is not a registered block", blockId);
            return;
        }
        Block block = BuiltInRegistries.BLOCK.get(blockId);
        if (block == Blocks.AIR) return;
        block.getStateDefinition().getPossibleStates().stream()
                .map(BlockModelShaper::stateToModelLocation)
                .forEach(mrl -> modifiers.computeIfAbsent(mrl, k -> new ArrayList<>()).addAll(overlays));
    }

    // ---- JSON parsing -------------------------------------------------------

    private static void parseEntry(JsonElement element,
                                   Map<ResourceLocation, List<ResourceLocation>> blockResult,
                                   Map<TagKey<Block>, List<ResourceLocation>> tagResult) {
        if (!element.isJsonObject())
            throw new JsonParseException("Overlay modifier entry must be a JSON object");
        JsonObject json = element.getAsJsonObject();

        // Append
        if (!json.has("append") || !json.get("append").isJsonArray())
            throw new JsonParseException("Must have an 'append' array");
        List<ResourceLocation> appendModels = new ArrayList<>();
        JsonArray appendArr = json.getAsJsonArray("append");
        for (JsonElement a : appendArr) {
            if (!a.isJsonPrimitive() || !a.getAsJsonPrimitive().isString())
                throw new JsonParseException("Each append entry must be a string");
            appendModels.add(ResourceLocation.parse(a.getAsString()));
        }
        if (appendModels.isEmpty())
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
                tagResult.computeIfAbsent(tagKey, k -> new ArrayList<>()).addAll(appendModels);
            } else {
                if (!id.contains(":")) id = "minecraft:" + id;
                blockResult.computeIfAbsent(ResourceLocation.parse(id), k -> new ArrayList<>()).addAll(appendModels);
            }
        }
    }
}