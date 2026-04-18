package com.otterly76.ott.client.model.ctm;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.geometry.IGeometryLoader;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Geometry loader registered as "ott:ctm".
 * Simpler 4-tile CTM system inspired by Chipped/Athena.
 *
 * <p>Model JSON format:
 * <pre>{@code
 * {
 *   "parent":  "minecraft:block/cube_all",
 *   "textures": { "particle": "ott:block/limestone_bricks/limestone_bricks" },
 *   "loader":  "ott:ctm",
 *   "ctm":     "ott:block/limestone_bricks/ctm",
 *   "connections": [{"type": "is_same_block"}]
 * }
 * }</pre>
 *
 * <p>The "ctm" value is the resource-location prefix.  Tiles 0–3 are loaded as
 * {@code ctm/0}, {@code ctm/1}, {@code ctm/2}, {@code ctm/3}.
 */
public class SimpleCTMLoader implements IGeometryLoader<SimpleCTMUnbakedGeometry> {

    public static final SimpleCTMLoader INSTANCE = new SimpleCTMLoader();

    private SimpleCTMLoader() {}

    @Override
    public @NotNull SimpleCTMUnbakedGeometry read(@NotNull JsonObject json,
                                                   @NotNull JsonDeserializationContext ctx)
            throws JsonParseException {
        if (!json.has("ctm")) {
            throw new JsonParseException("ott:ctm model missing required 'ctm' field");
        }
        String ctmPath = json.get("ctm").getAsString();

        // Parse the catch-all connection rule
        ConnectionRule rule = new ConnectionRule.IsSameBlock(); // default
        if (json.has("connections")) {
            JsonElement conn = json.get("connections");
            if (conn.isJsonArray()) {
                rule = parseRuleList(conn.getAsJsonArray());
            }
        }

        // Deep-copy and strip our custom keys before handing off to BlockModel
        JsonObject cleaned = json.deepCopy();
        cleaned.remove("loader");
        cleaned.remove("ctm");
        cleaned.remove("connections");
        // Note: "render_type" is intentionally NOT removed — BlockModel reads it.

        // Inject the 4 CTM tile paths into the "textures" block so NeoForge stitches them.
        if (!cleaned.has("textures")) {
            cleaned.add("textures", new JsonObject());
        }
        JsonObject textures = cleaned.getAsJsonObject("textures");
        for (int i = 0; i < 4; i++) {
            textures.addProperty("_ctm_tile_" + i, ctmPath + "/" + i);
        }

        BlockModel baseModel = ctx.deserialize(cleaned, BlockModel.class);
        return new SimpleCTMUnbakedGeometry(baseModel, ctmPath, rule);
    }

    // ── Connection rule parsing (mirrors ConnectingModelLoader) ──────────────

    private static ConnectionRule parseRuleList(JsonArray array) {
        List<ConnectionRule> rules = new ArrayList<>();
        for (JsonElement el : array) {
            rules.add(parseRule(el.getAsJsonObject()));
        }
        if (rules.isEmpty()) {
            throw new JsonParseException("ott:ctm connections array must not be empty");
        }
        return rules.size() == 1 ? rules.getFirst()
                : new ConnectionRule.AnyOf(rules.toArray(new ConnectionRule[0]));
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
                    throw new JsonParseException("Unknown block in ott:ctm match_block rule: " + blockId);
                }
                yield new ConnectionRule.MatchBlock(block);
            }
            default -> throw new JsonParseException("Unknown ott:ctm connection rule type: " + type);
        };
    }
}
