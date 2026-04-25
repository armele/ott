package com.otterly76.ott.block.stone;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Defines all stone-type variants for the stone-shape-set system (plate, edge, beam, pergola,
 * geometric window, bannister, support slab, support beam).

 * Each variant declares:
 *   name      – used as the block-name prefix (e.g. "stone" → "stone_plate")
 *   input     – the input block for stonecutter recipes (lazily evaluated)
 *   sideTex   – texture used for side/frieze faces and beam/pergola/bannister/slab
 *   topTex    – optional distinct top texture for plate's #top slot (null = same as sideTex)
 *   cutout    – whether non-beam/pergola shape models need renderType:cutout (glass, grates)
 */
public class ModStoneVariants {

    public record StoneVariant(
            String name,
            Supplier<Block> input,
            Supplier<Block> propsSource,
            String sideTex,
            @Nullable String topTex,
            boolean cutout
    ) {
        /** Returns topTex if set, otherwise sideTex. */
        public String effectiveTopTex() { return topTex != null ? topTex : sideTex; }
    }

    // ── Factory helpers ─────────────────────────────────────────────────────────

    private static StoneVariant of(String name, Supplier<Block> input, String tex) {
        return new StoneVariant(name, input, input, tex, null, false);
    }

    private static StoneVariant ofTop(String name, Supplier<Block> input, String side, String top) {
        return new StoneVariant(name, input, input, side, top, false);
    }

    private static StoneVariant ofCutout(String name, Supplier<Block> input, String tex) {
        return new StoneVariant(name, input, input, tex, null, true);
    }

    /** Use when input is a minecraft-namespace backport block that may not be bound at OTT's RegisterEvent time.
     *  propsSource must be a vanilla block (always available). */
    private static StoneVariant ofVanillaProps(String name, Supplier<Block> input, Supplier<Block> propsSource, String tex) {
        return new StoneVariant(name, input, propsSource, tex, null, false);
    }

    // ── Group 1: Classic Stone ──────────────────────────────────────────────────

    public static final List<StoneVariant> CLASSIC = List.of(
            of("stone",                    () -> Blocks.STONE,                    "minecraft:block/stone"),
            of("cobblestone",              () -> Blocks.COBBLESTONE,              "minecraft:block/cobblestone"),
            of("mossy_cobblestone",        () -> Blocks.MOSSY_COBBLESTONE,        "minecraft:block/mossy_cobblestone"),
            of("smooth_stone",             () -> Blocks.SMOOTH_STONE,             "minecraft:block/smooth_stone"),
            of("stone_bricks",             () -> Blocks.STONE_BRICKS,             "minecraft:block/stone_bricks"),
            of("cracked_stone_bricks",     () -> Blocks.CRACKED_STONE_BRICKS,     "minecraft:block/cracked_stone_bricks"),
            of("chiseled_stone_bricks",    () -> Blocks.CHISELED_STONE_BRICKS,    "minecraft:block/chiseled_stone_bricks"),
            of("mossy_stone_bricks",       () -> Blocks.MOSSY_STONE_BRICKS,       "minecraft:block/mossy_stone_bricks"),
            of("granite",                  () -> Blocks.GRANITE,                  "minecraft:block/granite"),
            of("polished_granite",         () -> Blocks.POLISHED_GRANITE,         "minecraft:block/polished_granite"),
            of("diorite",                  () -> Blocks.DIORITE,                  "minecraft:block/diorite"),
            of("polished_diorite",         () -> Blocks.POLISHED_DIORITE,         "minecraft:block/polished_diorite"),
            of("andesite",                 () -> Blocks.ANDESITE,                 "minecraft:block/andesite"),
            of("polished_andesite",        () -> Blocks.POLISHED_ANDESITE,        "minecraft:block/polished_andesite"),
            of("bricks",                   () -> Blocks.BRICKS,                   "minecraft:block/bricks"),
            of("calcite",                  () -> Blocks.CALCITE,                  "minecraft:block/calcite")
    );

    // ── Group 2: Deepslate & Tuff ───────────────────────────────────────────────

    public static final List<StoneVariant> DEEPSLATE = List.of(
            ofTop("deepslate",                () -> Blocks.DEEPSLATE,                "minecraft:block/deepslate",          "minecraft:block/deepslate_top"),
            of("cobbled_deepslate",           () -> Blocks.COBBLED_DEEPSLATE,        "minecraft:block/cobbled_deepslate"),
            of("chiseled_deepslate",          () -> Blocks.CHISELED_DEEPSLATE,       "minecraft:block/chiseled_deepslate"),
            of("polished_deepslate",          () -> Blocks.POLISHED_DEEPSLATE,       "minecraft:block/polished_deepslate"),
            of("deepslate_bricks",            () -> Blocks.DEEPSLATE_BRICKS,         "minecraft:block/deepslate_bricks"),
            of("cracked_deepslate_bricks",    () -> Blocks.CRACKED_DEEPSLATE_BRICKS, "minecraft:block/cracked_deepslate_bricks"),
            of("deepslate_tiles",             () -> Blocks.DEEPSLATE_TILES,          "minecraft:block/deepslate_tiles"),
            of("cracked_deepslate_tiles",     () -> Blocks.CRACKED_DEEPSLATE_TILES,  "minecraft:block/cracked_deepslate_tiles"),
            of("tuff",                        () -> Blocks.TUFF,                     "minecraft:block/tuff"),
            of("chiseled_tuff",               () -> Blocks.CHISELED_TUFF,            "minecraft:block/chiseled_tuff"),
            of("polished_tuff",               () -> Blocks.POLISHED_TUFF,            "minecraft:block/polished_tuff"),
            of("tuff_bricks",                 () -> Blocks.TUFF_BRICKS,              "minecraft:block/tuff_bricks"),
            of("chiseled_tuff_bricks",        () -> Blocks.CHISELED_TUFF_BRICKS,     "minecraft:block/chiseled_tuff_bricks")
    );

    // ── Group 3: Sandstone, Mud & Prismarine ────────────────────────────────────

    public static final List<StoneVariant> SANDSTONE = List.of(
            of("sandstone",                () -> Blocks.SANDSTONE,              "minecraft:block/sandstone"),
            of("chiseled_sandstone",       () -> Blocks.CHISELED_SANDSTONE,     "minecraft:block/chiseled_sandstone"),
            of("smooth_sandstone",         () -> Blocks.SMOOTH_SANDSTONE,       "minecraft:block/sandstone_top"),
            of("cut_sandstone",            () -> Blocks.CUT_SANDSTONE,          "minecraft:block/cut_sandstone"),
            of("red_sandstone",            () -> Blocks.RED_SANDSTONE,          "minecraft:block/red_sandstone"),
            of("chiseled_red_sandstone",   () -> Blocks.CHISELED_RED_SANDSTONE, "minecraft:block/chiseled_red_sandstone"),
            of("smooth_red_sandstone",     () -> Blocks.SMOOTH_RED_SANDSTONE,   "minecraft:block/red_sandstone_top"),
            of("cut_red_sandstone",        () -> Blocks.CUT_RED_SANDSTONE,      "minecraft:block/cut_red_sandstone"),
            of("packed_mud",               () -> Blocks.PACKED_MUD,             "minecraft:block/packed_mud"),
            of("mud_bricks",               () -> Blocks.MUD_BRICKS,             "minecraft:block/mud_bricks"),
            of("prismarine",               () -> Blocks.PRISMARINE,             "minecraft:block/prismarine"),
            of("prismarine_bricks",        () -> Blocks.PRISMARINE_BRICKS,      "minecraft:block/prismarine_bricks"),
            of("dark_prismarine",          () -> Blocks.DARK_PRISMARINE,        "minecraft:block/dark_prismarine")
    );

    // ── Group 4: Nether & Blackstone ────────────────────────────────────────────

    public static final List<StoneVariant> NETHER = List.of(
            of("netherrack",                          () -> Blocks.NETHERRACK,                       "minecraft:block/netherrack"),
            of("nether_bricks",                       () -> Blocks.NETHER_BRICKS,                    "minecraft:block/nether_bricks"),
            of("cracked_nether_bricks",               () -> Blocks.CRACKED_NETHER_BRICKS,            "minecraft:block/cracked_nether_bricks"),
            of("chiseled_nether_bricks",              () -> Blocks.CHISELED_NETHER_BRICKS,           "minecraft:block/chiseled_nether_bricks"),
            of("red_nether_bricks",                   () -> Blocks.RED_NETHER_BRICKS,                "minecraft:block/red_nether_bricks"),
            of("basalt",                              () -> Blocks.BASALT,                           "minecraft:block/basalt_side"),
            of("smooth_basalt",                       () -> Blocks.SMOOTH_BASALT,                    "minecraft:block/smooth_basalt"),
            ofTop("polished_basalt",                  () -> Blocks.POLISHED_BASALT,                  "minecraft:block/polished_basalt_side",             "minecraft:block/polished_basalt_top"),
            ofTop("blackstone",                       () -> Blocks.BLACKSTONE,                       "minecraft:block/blackstone",                       "minecraft:block/blackstone_top"),
            of("gilded_blackstone",                   () -> Blocks.GILDED_BLACKSTONE,                "minecraft:block/gilded_blackstone"),
            of("chiseled_polished_blackstone",        () -> Blocks.CHISELED_POLISHED_BLACKSTONE,     "minecraft:block/chiseled_polished_blackstone"),
            of("polished_blackstone",                 () -> Blocks.POLISHED_BLACKSTONE,              "minecraft:block/polished_blackstone"),
            of("polished_blackstone_bricks",          () -> Blocks.POLISHED_BLACKSTONE_BRICKS,       "minecraft:block/polished_blackstone_bricks"),
            of("cracked_polished_blackstone_bricks",  () -> Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, "minecraft:block/cracked_polished_blackstone_bricks"),
            of("magma_block",                         () -> Blocks.MAGMA_BLOCK,                      "minecraft:block/magma"),
            of("obsidian",                            () -> Blocks.OBSIDIAN,                         "minecraft:block/obsidian"),
            of("crying_obsidian",                     () -> Blocks.CRYING_OBSIDIAN,                  "minecraft:block/crying_obsidian")
    );

    // ── Group 5: End, Misc & Nature ─────────────────────────────────────────────

    public static final List<StoneVariant> END_MISC = List.of(
            of("end_stone",                () -> Blocks.END_STONE,             "minecraft:block/end_stone"),
            of("end_stone_bricks",         () -> Blocks.END_STONE_BRICKS,      "minecraft:block/end_stone_bricks"),
            of("purpur_block",             () -> Blocks.PURPUR_BLOCK,          "minecraft:block/purpur_block"),
            of("dripstone_block",          () -> Blocks.DRIPSTONE_BLOCK,       "minecraft:block/dripstone_block"),
            of("glowstone",                () -> Blocks.GLOWSTONE,             "minecraft:block/glowstone"),
            ofTop("bone_block",            () -> Blocks.BONE_BLOCK,            "minecraft:block/bone_block_side",           "minecraft:block/bone_block_top"),
            ofTop("ochre_froglight",       () -> Blocks.OCHRE_FROGLIGHT,       "minecraft:block/ochre_froglight_side",      "minecraft:block/ochre_froglight_top"),
            ofTop("verdant_froglight",     () -> Blocks.VERDANT_FROGLIGHT,     "minecraft:block/verdant_froglight_side",    "minecraft:block/verdant_froglight_top"),
            ofTop("pearlescent_froglight", () -> Blocks.PEARLESCENT_FROGLIGHT, "minecraft:block/pearlescent_froglight_side","minecraft:block/pearlescent_froglight_top"),
            of("dead_tube_coral_block",    () -> Blocks.DEAD_TUBE_CORAL_BLOCK,   "minecraft:block/dead_tube_coral_block"),
            of("dead_brain_coral_block",   () -> Blocks.DEAD_BRAIN_CORAL_BLOCK,  "minecraft:block/dead_brain_coral_block"),
            of("dead_bubble_coral_block",  () -> Blocks.DEAD_BUBBLE_CORAL_BLOCK, "minecraft:block/dead_bubble_coral_block"),
            of("dead_fire_coral_block",    () -> Blocks.DEAD_FIRE_CORAL_BLOCK,   "minecraft:block/dead_fire_coral_block"),
            of("dead_horn_coral_block",    () -> Blocks.DEAD_HORN_CORAL_BLOCK,   "minecraft:block/dead_horn_coral_block")
    );

    // ── Group 6: Minerals & OTT ─────────────────────────────────────────────────

    public static final List<StoneVariant> MINERALS = List.of(
            of("coal_block",             () -> Blocks.COAL_BLOCK,           "minecraft:block/coal_block"),
            of("iron_block",             () -> Blocks.IRON_BLOCK,           "minecraft:block/iron_block"),
            of("gold_block",             () -> Blocks.GOLD_BLOCK,           "minecraft:block/gold_block"),
            of("redstone_block",         () -> Blocks.REDSTONE_BLOCK,       "minecraft:block/redstone_block"),
            of("emerald_block",          () -> Blocks.EMERALD_BLOCK,        "minecraft:block/emerald_block"),
            of("lapis_block",            () -> Blocks.LAPIS_BLOCK,          "minecraft:block/lapis_block"),
            of("diamond_block",          () -> Blocks.DIAMOND_BLOCK,        "minecraft:block/diamond_block"),
            of("netherite_block",        () -> Blocks.NETHERITE_BLOCK,      "minecraft:block/netherite_block"),
            of("quartz_block",           () -> Blocks.QUARTZ_BLOCK,         "minecraft:block/quartz_block_side"),
            of("amethyst_block",         () -> Blocks.AMETHYST_BLOCK,       "minecraft:block/amethyst_block"),
            of("raw_iron_block",         () -> Blocks.RAW_IRON_BLOCK,       "minecraft:block/raw_iron_block"),
            of("raw_copper_block",       () -> Blocks.RAW_COPPER_BLOCK,     "minecraft:block/raw_copper_block"),
            of("raw_gold_block",         () -> Blocks.RAW_GOLD_BLOCK,       "minecraft:block/raw_gold_block"),
            ofVanillaProps("resin_block",           ModBlocks.RESIN_BLOCK,          () -> Blocks.CLAY,   "minecraft:block/resin_block"),
            ofVanillaProps("resin_bricks",          ModBlocks.RESIN_BRICKS,         () -> Blocks.BRICKS, "minecraft:block/resin_bricks"),
            ofVanillaProps("chiseled_resin_bricks", ModBlocks.CHISELED_RESIN_BRICKS, () -> Blocks.BRICKS, "minecraft:block/chiseled_resin_bricks"),
            of("salt_block",             ModBlocks.SALT_BLOCK,              "ott:block/salt/salt_block"),
            of("polished_salt_block",    ModBlocks.POLISHED_SALT_BLOCK,     "ott:block/salt/polished_salt_block")
    );

    // ── Group 7: Copper ─────────────────────────────────────────────────────────

    public static final List<StoneVariant> COPPER = List.of(
            of("copper_block",                     () -> Blocks.COPPER_BLOCK,                   "minecraft:block/copper_block"),
            of("chiseled_copper",                  () -> Blocks.CHISELED_COPPER,                "minecraft:block/chiseled_copper"),
            of("cut_copper",                       () -> Blocks.CUT_COPPER,                     "minecraft:block/cut_copper"),
            of("exposed_copper",                   () -> Blocks.EXPOSED_COPPER,                 "minecraft:block/exposed_copper"),
            of("exposed_chiseled_copper",          () -> Blocks.EXPOSED_CHISELED_COPPER,        "minecraft:block/exposed_chiseled_copper"),
            of("exposed_cut_copper",               () -> Blocks.EXPOSED_CUT_COPPER,             "minecraft:block/exposed_cut_copper"),
            of("weathered_copper",                 () -> Blocks.WEATHERED_COPPER,               "minecraft:block/weathered_copper"),
            of("weathered_chiseled_copper",        () -> Blocks.WEATHERED_CHISELED_COPPER,      "minecraft:block/weathered_chiseled_copper"),
            of("weathered_cut_copper",             () -> Blocks.WEATHERED_CUT_COPPER,           "minecraft:block/weathered_cut_copper"),
            of("oxidized_copper",                  () -> Blocks.OXIDIZED_COPPER,                "minecraft:block/oxidized_copper"),
            of("oxidized_chiseled_copper",         () -> Blocks.OXIDIZED_CHISELED_COPPER,       "minecraft:block/oxidized_chiseled_copper"),
            of("oxidized_cut_copper",              () -> Blocks.OXIDIZED_CUT_COPPER,            "minecraft:block/oxidized_cut_copper"),
            of("waxed_copper_block",               () -> Blocks.WAXED_COPPER_BLOCK,             "minecraft:block/copper_block"),
            of("waxed_chiseled_copper",            () -> Blocks.WAXED_CHISELED_COPPER,          "minecraft:block/chiseled_copper"),
            of("waxed_cut_copper",                 () -> Blocks.WAXED_CUT_COPPER,               "minecraft:block/cut_copper"),
            of("waxed_exposed_copper",             () -> Blocks.WAXED_EXPOSED_COPPER,           "minecraft:block/exposed_copper"),
            of("waxed_exposed_chiseled_copper",    () -> Blocks.WAXED_EXPOSED_CHISELED_COPPER,  "minecraft:block/exposed_chiseled_copper"),
            of("waxed_exposed_cut_copper",         () -> Blocks.WAXED_EXPOSED_CUT_COPPER,       "minecraft:block/exposed_cut_copper"),
            of("waxed_weathered_copper",           () -> Blocks.WAXED_WEATHERED_COPPER,         "minecraft:block/weathered_copper"),
            of("waxed_weathered_chiseled_copper",  () -> Blocks.WAXED_WEATHERED_CHISELED_COPPER,"minecraft:block/weathered_chiseled_copper"),
            of("waxed_weathered_cut_copper",       () -> Blocks.WAXED_WEATHERED_CUT_COPPER,     "minecraft:block/weathered_cut_copper"),
            of("waxed_oxidized_copper",            () -> Blocks.WAXED_OXIDIZED_COPPER,          "minecraft:block/oxidized_copper"),
            of("waxed_oxidized_chiseled_copper",   () -> Blocks.WAXED_OXIDIZED_CHISELED_COPPER, "minecraft:block/oxidized_chiseled_copper"),
            of("waxed_oxidized_cut_copper",        () -> Blocks.WAXED_OXIDIZED_CUT_COPPER,      "minecraft:block/oxidized_cut_copper")
    );

    // ── Group 8: OTT Custom Stone ────────────────────────────────────────────────

    public static final List<StoneVariant> CUSTOM = List.of(
            of("limestone",                         ModBlocks.PLAIN_LIMESTONE,                        "ott:block/limestone/limestone"),
            of("cobbled_limestone",                 ModBlocks.COBBLED_LIMESTONE,                      "ott:block/limestone/cobbled_limestone"),
            of("refined_glowstone",                 ModBlocks.REFINED_GLOWSTONE,                      "ott:block/glowstone/refined_glowstone"),
            of("roofing_slates",                    ModBlocks.ROOFING_SLATES,                         "ott:block/misc/roofing_slates"),
            of("black_marble",                      ModBlocks.BLACK_MARBLE,                           "ott:block/black_marble/black_marble"),
            of("black_marble_bricks",               ModBlocks.BLACK_MARBLE_BRICKS,                    "ott:block/black_marble/black_marble_bricks"),
            of("black_marble_small_bricks",         ModBlocks.BLACK_MARBLE_SMALL_BRICKS,              "ott:block/black_marble/black_marble_small_bricks"),
            of("black_marble_tiles",                ModBlocks.BLACK_MARBLE_TILES,                     "ott:block/black_marble/black_marble_tiles"),
            of("black_polished_marble",             ModBlocks.BLACK_POLISHED_MARBLE,                  "ott:block/black_marble/black_polished_marble"),
            of("white_marble",                      ModBlocks.WHITE_MARBLE,                           "ott:block/white_marble/white_marble"),
            of("white_marble_bricks",               ModBlocks.WHITE_MARBLE_BRICKS,                    "ott:block/white_marble/white_marble_bricks"),
            of("white_marble_small_bricks",         ModBlocks.WHITE_MARBLE_SMALL_BRICKS,              "ott:block/white_marble/white_marble_small_bricks"),
            of("white_marble_tiles",                ModBlocks.WHITE_MARBLE_TILES,                     "ott:block/white_marble/white_marble_tiles"),
            of("white_polished_marble",             ModBlocks.WHITE_POLISHED_MARBLE,                  "ott:block/white_marble/white_polished_marble"),
            of("sandstone_slender_bricks",          ModBlocks.SANDSTONE_SLENDER_BRICKS,               "ott:block/sandstone/sandstone_slender_bricks"),
            of("sandstone_slender_turquoise_pattern", ModBlocks.SANDSTONE_SLENDER_TURQUOISE_PATTERN,  "ott:block/sandstone/sandstone_slender_turquoise_pattern")
    );

    // ── Group 9: Mosaic Traditional ──────────────────────────────────────────────

    public static final List<StoneVariant> MOSAIC_TRADITIONAL = List.of(
            of("water_mosaic_traditional",   ModBlocks.WATER_MOSAIC_TRADITIONAL,  "ott:block/water_mosaic/water_mosaic_traditional"),
            of("earth_mosaic_traditional",   ModBlocks.EARTH_MOSAIC_TRADITIONAL,  "ott:block/earth_mosaic/earth_mosaic_traditional"),
            of("fire_mosaic_traditional",    ModBlocks.FIRE_MOSAIC_TRADITIONAL,   "ott:block/fire_mosaic/fire_mosaic_traditional"),
            of("spirit_mosaic_traditional",  ModBlocks.SPIRIT_MOSAIC_TRADITIONAL, "ott:block/spirit_mosaic/spirit_mosaic_traditional"),
            of("air_mosaic_traditional",     ModBlocks.AIR_MOSAIC_TRADITIONAL,    "ott:block/air_mosaic/air_mosaic_traditional")
    );

    // ── Combined list ───────────────────────────────────────────────────────────

    public static final List<StoneVariant> ALL = Stream.of(
            CLASSIC, DEEPSLATE, SANDSTONE, NETHER, END_MISC, MINERALS, COPPER, CUSTOM, MOSAIC_TRADITIONAL
    ).flatMap(List::stream).toList();

    // ── Creative-tab groups ─────────────────────────────────────────────────────

    public record StoneGroup(String tabName, String iconVariant, List<StoneVariant> variants) {}

    public static final List<StoneGroup> GROUPS = List.of(
            new StoneGroup("stone_vanilla",             "stone",                    CLASSIC),
            new StoneGroup("stone_custom",              "limestone",                CUSTOM),
            new StoneGroup("stone_mosaic_traditional",  "water_mosaic_traditional", MOSAIC_TRADITIONAL)
    );

    // ── Lookup helper ───────────────────────────────────────────────────────────

    public static @Nullable StoneVariant byName(String name) {
        for (StoneVariant v : ALL) {
            if (v.name().equals(name)) return v;
        }
        return null;
    }
}