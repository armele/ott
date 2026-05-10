package com.otterly76.ott.client.model.overlay;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.client.event.ModelEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;

/**
 * Loads terrain overlay modifier descriptors from two config files:
 * <ul>
 *   <li>{@code assets/ott/ott_overlay_modifiers/tier_config.json} —
 *       maps every block ID to an integer tier.  Higher tier = renders on top.</li>
 *   <li>{@code assets/ott/ott_overlay_modifiers/overlay_config.json} —
 *       maps block IDs that <em>have</em> an overlay to their overlay model location(s).</li>
 * </ul>
 *
 * <p>A block at tier T has its overlay applied to every block whose tier is
 * strictly less than T.  Blocks at the same tier never overlay each other.
 * Rendering order at shared corners is determined by ascending tier (lower tier
 * renders first = bottom layer; higher tier renders last = top layer).
 */
public class OverlayModifierReloadListener {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson   GSON   = new GsonBuilder().setLenient().create();

    private static final ResourceLocation TIER_CONFIG    =
            ResourceLocation.fromNamespaceAndPath("ott", "ott_overlay_modifiers/tier_config.json");
    private static final ResourceLocation OVERLAY_CONFIG =
            ResourceLocation.fromNamespaceAndPath("ott", "ott_overlay_modifiers/overlay_config.json");

    public static final OverlayModifierReloadListener INSTANCE = new OverlayModifierReloadListener();

    /**
     * Maps each target block-state model location to the ordered list of overlay
     * model locations that should be appended to it.  Populated in registerModels().
     */
    private final Map<ModelResourceLocation, List<ResourceLocation>> modifiers = new HashMap<>();

    private OverlayModifierReloadListener() {}

    // ── Model events ──────────────────────────────────────────────────────────

    /**
     * Loads the two config files, derives target lists from tier comparisons,
     * then registers all referenced overlay models as standalone models.
     * Call from ModelEvent.RegisterAdditional.
     */
    public void registerModels(@NotNull ModelEvent.RegisterAdditional event) {
        ResourceManager rm = Minecraft.getInstance().getResourceManager();

        // 1. Load tier_config.json  →  blockId → tier
        Map<ResourceLocation, Integer> blockTiers = loadTierConfig(rm);
        if (blockTiers.isEmpty()) return;

        // 2. Group blocks by tier (sorted ascending)
        Map<Integer, List<ResourceLocation>> tierToBlocks = new TreeMap<>();
        for (Map.Entry<ResourceLocation, Integer> e : blockTiers.entrySet()) {
            tierToBlocks.computeIfAbsent(e.getValue(), k -> new ArrayList<>()).add(e.getKey());
        }

        // 3. Load overlay_config.json  →  blockId → overlay model locations
        Map<ResourceLocation, List<ResourceLocation>> overlayConfig = loadOverlayConfig(rm);
        if (overlayConfig.isEmpty()) return;

        // 4. Sort overlay entries by ascending tier so lower-tier overlays render first (bottom layer)
        List<Map.Entry<ResourceLocation, List<ResourceLocation>>> sortedOverlays =
                new ArrayList<>(overlayConfig.entrySet());
        sortedOverlays.sort(Comparator.comparingInt(e -> blockTiers.getOrDefault(e.getKey(), 0)));

        // 5. Build modifiers map
        modifiers.clear();
        for (Map.Entry<ResourceLocation, List<ResourceLocation>> entry : sortedOverlays) {
            ResourceLocation blockId     = entry.getKey();
            List<ResourceLocation> models = entry.getValue();
            int tier = blockTiers.getOrDefault(blockId, 0);

            // Targets = all blocks with tier strictly less than this block's tier
            for (Map.Entry<Integer, List<ResourceLocation>> tierEntry : tierToBlocks.entrySet()) {
                if (tierEntry.getKey() < tier) {
                    for (ResourceLocation targetId : tierEntry.getValue()) {
                        expandBlock(targetId, models, tier);
                    }
                }
            }
        }
        LOGGER.debug("[OTT] Overlay modifiers loaded for {} block-state entries", modifiers.size());

        // 6. Register every distinct overlay model so the baking system picks it up
        Set<ResourceLocation> allModels = new HashSet<>();
        for (List<ResourceLocation> overlays : modifiers.values()) {
            allModels.addAll(overlays);
        }
        for (ResourceLocation loc : allModels) {
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
            ModelResourceLocation target   = entry.getKey();
            BakedModel            original = models.get(target);
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

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Expands all block states for {@code blockId} and appends the overlay models. */
    private void expandBlock(ResourceLocation blockId,
                             List<ResourceLocation> overlayModels,
                             int zOrder) {
        if (!BuiltInRegistries.BLOCK.containsKey(blockId)) {
            LOGGER.warn("[OTT] Overlay target '{}' is not a registered block — add it to tier_config.json?", blockId);
            return;
        }
        Block block = BuiltInRegistries.BLOCK.get(blockId);
        if (block == Blocks.AIR) return;

        block.getStateDefinition().getPossibleStates().stream()
                .map(BlockModelShaper::stateToModelLocation)
                .forEach(mrl -> modifiers.computeIfAbsent(mrl, k -> new ArrayList<>())
                        .addAll(overlayModels));
    }

    // ── Config file loaders ───────────────────────────────────────────────────

    private Map<ResourceLocation, Integer> loadTierConfig(ResourceManager rm) {
        Optional<Resource> opt = rm.getResource(TIER_CONFIG);
        if (opt.isEmpty()) {
            LOGGER.error("[OTT] tier_config.json not found at {}", TIER_CONFIG);
            return Map.of();
        }
        try (Reader reader = opt.get().openAsReader()) {
            JsonObject json   = GSON.fromJson(reader, JsonObject.class);
            JsonObject tiers  = json.getAsJsonObject("tiers");
            Map<ResourceLocation, Integer> result = new HashMap<>();
            for (Map.Entry<String, JsonElement> e : tiers.entrySet()) {
                result.put(ResourceLocation.parse(e.getKey()), e.getValue().getAsInt());
            }
            LOGGER.debug("[OTT] tier_config.json loaded: {} blocks", result.size());
            return result;
        } catch (Exception e) {
            LOGGER.error("[OTT] Failed to load tier_config.json: {}", e.getMessage());
            return Map.of();
        }
    }

    private Map<ResourceLocation, List<ResourceLocation>> loadOverlayConfig(ResourceManager rm) {
        Optional<Resource> opt = rm.getResource(OVERLAY_CONFIG);
        if (opt.isEmpty()) {
            LOGGER.error("[OTT] overlay_config.json not found at {}", OVERLAY_CONFIG);
            return Map.of();
        }
        try (Reader reader = opt.get().openAsReader()) {
            JsonObject json     = GSON.fromJson(reader, JsonObject.class);
            JsonObject overlays = json.getAsJsonObject("overlays");
            Map<ResourceLocation, List<ResourceLocation>> result = new HashMap<>();
            for (Map.Entry<String, JsonElement> e : overlays.entrySet()) {
                List<ResourceLocation> models = new ArrayList<>();
                if (e.getValue().isJsonArray()) {
                    for (JsonElement el : e.getValue().getAsJsonArray()) {
                        if (el.isJsonPrimitive()) {
                            models.add(ResourceLocation.parse(el.getAsString()));
                        }
                    }
                }
                if (!models.isEmpty()) {
                    result.put(ResourceLocation.parse(e.getKey()), models);
                }
            }
            LOGGER.debug("[OTT] overlay_config.json loaded: {} overlay entries", result.size());
            return result;
        } catch (Exception e) {
            LOGGER.error("[OTT] Failed to load overlay_config.json: {}", e.getMessage());
            return Map.of();
        }
    }
}