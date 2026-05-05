package com.otterly76.ott.client.model.overlay;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Geometry loader registered as {@code "ott:overlay"}.
 *
 * <p>Reads terrain-transition overlay models and produces
 * {@link OverlayUnbakedGeometry}.
 *
 * <p>JSON format: a standard BlockModel JSON with an added {@code "connections"} key.
 * The {@code "connections"} value is either:
 * <ul>
 *   <li>A predicate object (has a {@code "type"} key) — applies to <em>all</em> texture variables.
 *   <li>An object mapping texture variable names ({@code "top"}, {@code "side"}, etc.)
 *       to predicate objects for per-variable rules.
 * </ul>
 *
 * <p>Supported predicate types:
 * {@code match_block}, {@code match_block_in_front}, {@code is_face_visible},
 * {@code and}, {@code or}.
 */
public class OverlayModelLoader implements IGeometryLoader<OverlayUnbakedGeometry> {

    public static final OverlayModelLoader INSTANCE = new OverlayModelLoader();

    private OverlayModelLoader() {}

    @Override
    public @NotNull OverlayUnbakedGeometry read(@NotNull JsonObject json,
                                                @NotNull JsonDeserializationContext ctx)
            throws JsonParseException {
        Map<String, OverlayConnectionRule> rulesByTexVar = parseConnections(json);
        boolean useGrassTint = json.has("tint_type")
                && "grass".equals(json.get("tint_type").getAsString());

        // Strip custom keys so the remainder is a valid vanilla BlockModel JSON
        JsonObject cleaned = json.deepCopy();
        cleaned.remove("loader");
        cleaned.remove("connections");
        cleaned.remove("tint_type");

        BlockModel baseModel = ctx.deserialize(cleaned, BlockModel.class);
        return new OverlayUnbakedGeometry(baseModel, rulesByTexVar, useGrassTint);
    }

    // ---- connection parsing -------------------------------------------------

    private static Map<String, OverlayConnectionRule> parseConnections(JsonObject json) {
        Map<String, OverlayConnectionRule> result = new HashMap<>();
        if (!json.has("connections")) return result;

        JsonElement connElem = json.get("connections");
        if (!connElem.isJsonObject())
            throw new JsonParseException("'connections' must be a JSON object");
        JsonObject connObj = connElem.getAsJsonObject();

        if (connObj.has("type")) {
            // Single predicate for all texture variables — stored under catch-all key "*"
            result.put("*", parsePredicate(connObj));
        } else {
            // Map of texture variable name → predicate
            for (Map.Entry<String, JsonElement> entry : connObj.entrySet()) {
                if (!entry.getValue().isJsonObject())
                    throw new JsonParseException(
                            "Connection entry '" + entry.getKey() + "' must be a JSON object");
                result.put(entry.getKey(), parsePredicate(entry.getValue().getAsJsonObject()));
            }
        }
        return result;
    }

    private static OverlayConnectionRule parsePredicate(JsonObject obj) {
        if (!obj.has("type"))
            throw new JsonParseException("Overlay predicate is missing 'type'");
        String type = obj.get("type").getAsString();
        return switch (type) {
            case "match_block"          -> new OverlayConnectionRule.MatchBlock(requireBlock(obj));
            case "match_block_in_front" -> new OverlayConnectionRule.MatchBlockInFront(requireBlock(obj));
            case "is_face_visible"      -> OverlayConnectionRule.IsFaceVisible.INSTANCE;
            case "and" -> {
                List<OverlayConnectionRule> parts = parsePredicateArray(obj);
                yield new OverlayConnectionRule.And(parts.toArray(OverlayConnectionRule[]::new));
            }
            case "or" -> {
                List<OverlayConnectionRule> parts = parsePredicateArray(obj);
                yield new OverlayConnectionRule.Or(parts.toArray(OverlayConnectionRule[]::new));
            }
            default -> throw new JsonParseException("Unknown overlay predicate type: " + type);
        };
    }

    private static List<OverlayConnectionRule> parsePredicateArray(JsonObject obj) {
        if (!obj.has("predicates") || !obj.get("predicates").isJsonArray())
            throw new JsonParseException("Compound predicate missing 'predicates' array");
        JsonArray arr = obj.getAsJsonArray("predicates");
        List<OverlayConnectionRule> rules = new ArrayList<>(arr.size());
        for (JsonElement el : arr) {
            if (!el.isJsonObject())
                throw new JsonParseException("Each predicate must be a JSON object");
            rules.add(parsePredicate(el.getAsJsonObject()));
        }
        if (rules.isEmpty())
            throw new JsonParseException("'predicates' array must not be empty");
        return rules;
    }

    private static Block requireBlock(JsonObject obj) {
        if (!obj.has("block"))
            throw new JsonParseException("Block predicate missing 'block' field");
        String id = obj.get("block").getAsString();
        // Accept bare names like "sand" as shorthand for "minecraft:sand"
        if (!id.contains(":")) id = "minecraft:" + id;
        ResourceLocation loc = ResourceLocation.parse(id);
        if (!BuiltInRegistries.BLOCK.containsKey(loc))
            throw new JsonParseException("Unknown block in overlay predicate: " + loc);
        return BuiltInRegistries.BLOCK.get(loc);
    }
}
