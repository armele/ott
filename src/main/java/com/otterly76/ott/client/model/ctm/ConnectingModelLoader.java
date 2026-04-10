package com.otterly76.ott.client.model.ctm;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Geometry loader registered as "ott:connecting".
 * Replaces "fusion:model" + "type":"connecting" with our own CTM implementation.
 */
public class ConnectingModelLoader implements IGeometryLoader<ConnectingUnbakedGeometry> {

    public static final ConnectingModelLoader INSTANCE = new ConnectingModelLoader();

    private ConnectingModelLoader() {}

    @Override
    public @NotNull ConnectingUnbakedGeometry read(@NotNull JsonObject json, @NotNull JsonDeserializationContext ctx) throws JsonParseException {
        // Deep-copy so we can strip custom keys before passing to BlockModel deserializer
        JsonObject cleaned = json.deepCopy();
        cleaned.remove("loader");
        cleaned.remove("type");
        cleaned.remove("connections");
        cleaned.remove("isolated");

        // Parse connections: either array (all textures share one rule)
        // or object (per-texture-variable rules)
        Map<String, ConnectionRule> rulesByTexVar = new HashMap<>();

        if (json.has("connections")) {
            JsonElement connections = json.get("connections");
            if (connections.isJsonArray()) {
                // All textures use this rule
                ConnectionRule rule = parseRuleList(connections.getAsJsonArray());
                rulesByTexVar.put("*", rule);
            } else if (connections.isJsonObject()) {
                // Per-texture-variable rules
                for (Map.Entry<String, JsonElement> entry : connections.getAsJsonObject().entrySet()) {
                    if (entry.getValue().isJsonArray()) {
                        rulesByTexVar.put(entry.getKey(), parseRuleList(entry.getValue().getAsJsonArray()));
                    }
                }
            }
        }

        // Parse optional "isolated" block: maps texture variable name → isolated texture path.
        // Used to provide a properly-sized sprite to Domum Ornamentum so it sees just the
        // isolated tile rather than the full CTM atlas.
        Map<String, ResourceLocation> isolatedTextures = Collections.emptyMap();
        if (json.has("isolated")) {
            JsonObject isolatedObj = json.getAsJsonObject("isolated");
            isolatedTextures = new HashMap<>();
            for (Map.Entry<String, JsonElement> entry : isolatedObj.entrySet()) {
                isolatedTextures.put(entry.getKey(), ResourceLocation.parse(entry.getValue().getAsString()));
            }
        }

        // Deserialize the cleaned JSON as a standard BlockModel
        BlockModel baseModel = ctx.deserialize(cleaned, BlockModel.class);

        return new ConnectingUnbakedGeometry(baseModel, rulesByTexVar, isolatedTextures);
    }

    private static ConnectionRule parseRuleList(JsonArray array) {
        List<ConnectionRule> rules = new ArrayList<>();
        for (JsonElement el : array) {
            rules.add(parseRule(el.getAsJsonObject()));
        }
        if (rules.isEmpty()) {
            throw new JsonParseException("CTM connections array must not be empty");
        }
        return rules.size() == 1 ? rules.getFirst() : new ConnectionRule.AnyOf(rules.toArray(new ConnectionRule[0]));
    }

    private static ConnectionRule parseRule(JsonObject obj) {
        String type = obj.get("type").getAsString();
        return switch (type) {
            case "is_same_block" -> new ConnectionRule.IsSameBlock();
            case "match_block" -> {
                String blockId = obj.get("block").getAsString();
                Block block = net.minecraft.core.registries.BuiltInRegistries.BLOCK
                        .get(ResourceLocation.parse(blockId));
                if (block == net.minecraft.world.level.block.Blocks.AIR) {
                    throw new JsonParseException("Unknown block in CTM match_block rule: " + blockId);
                }
                yield new ConnectionRule.MatchBlock(block);
            }
            default -> throw new JsonParseException("Unknown CTM connection rule type: " + type);
        };
    }
}
