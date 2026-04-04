package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.entity.custom.Butterfly;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.otterly76.ott.block.ModBlocks.ALL_GRADIENT_BLOCKS;


public final class ModCreativeTabs {
    private static final String ITEM_GROUP_PREFIX = "itemGroup." + Constants.MOD_ID + ".";

    public static final DeferredRegister<CreativeModeTab> OTTER_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GRADIENTS = OTTER_TABS.register("gradients", ModCreativeTabs::createGradientsTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> BLOCKS = OTTER_TABS.register("blocks", ModCreativeTabs::createBlocksTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> COLOR_SETS = OTTER_TABS.register("color_sets", ModCreativeTabs::createColorSetsTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> WOOD_SETS = OTTER_TABS.register("wood_sets", ModCreativeTabs::createWoodSetsTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DYES = OTTER_TABS.register("dyes", ModCreativeTabs::createDyesTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> OTT_EGGS = OTTER_TABS.register("ott_eggs", ModCreativeTabs::createOttEggsTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC = OTTER_TABS.register("misc", ModCreativeTabs::createMiscTab);


    private static CreativeModeTab createGradientsTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("gradients")))
                .withTabsBefore(CreativeModeTabs.INVENTORY)
                .withTabsAfter(BLOCKS.getKey())
                .icon(() -> new ItemStack(ALL_GRADIENT_BLOCKS.getFirst()))
                .displayItems((params, output) -> ModBlocks.getAllGradientBlocks().forEach(output::accept))
                .build();
    }

    private static CreativeModeTab createBlocksTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("blocks")))
                .withTabsBefore(GRADIENTS.getKey())
                .withTabsAfter(COLOR_SETS.getKey())
                .icon(() -> new ItemStack(ModBlocks.LIMESTONE_00.get()))
                .displayItems((config, output) -> {
                    // Limestone
                    ModBlocks.LIMESTONE.forEach(output::accept);
                    // Seaglass
                    ModBlocks.SEAGLASS.forEach(output::accept);
                    // Smooth Glowstone
                    output.accept(ModBlocks.SMOOTH_GLOWSTONE);
                    // Testblocks
                    ModBlocks.TESTBLOCK.forEach(output::accept);
                    // Salt
                    output.accept(ModBlocks.SALT_BLOCK);
                    output.accept(ModBlocks.POLISHED_SALT_BLOCK);

                    // Resin
                    output.accept(ModBlocks.RESIN_BLOCK);
                    output.accept(ModBlocks.RESIN_BRICKS);
                    output.accept(ModBlocks.RESIN_BRICK_STAIRS);
                    output.accept(ModBlocks.RESIN_BRICK_SLAB);
                    output.accept(ModBlocks.RESIN_BRICK_WALL);
                    output.accept(ModBlocks.CHISELED_RESIN_BRICKS);

                    // Mosaic / Fresco
                    output.accept(ModBlocks.WATER_MOSAIC_BORDER);
                    output.accept(ModBlocks.WATER_MOSAIC_GEOMETRIC);
                    output.accept(ModBlocks.WATER_MOSAIC_PATTERN);
                    output.accept(ModBlocks.WATER_MOSAIC_DELICATE);
                    output.accept(ModBlocks.WATER_MOSAIC_TRADITIONAL);
                    output.accept(ModBlocks.WATER_MOSAIC_RECESS);
                    output.accept(ModBlocks.MOSAIC_FLOOR);
                    output.accept(ModBlocks.MOSAIC_FLOOR_DELICATE);
                    output.accept(ModBlocks.MOSAIC_FLOOR_ROSETTE);
                    output.accept(ModBlocks.ROMAN_FRESCO_RED);
                    output.accept(ModBlocks.ROMAN_FRESCO_BLACK);

                    // --- DoTB: General ---
                    output.accept(ModBlocks.STONE_LANTERN);
                    output.accept(ModBlocks.IRON_FANCY_LANTERN);
                    output.accept(ModBlocks.WHEAT_THATCH);
                    output.accept(ModBlocks.WHEAT_THATCH_EDGE);
                    output.accept(ModBlocks.WHEAT_THATCH_PLATE);
                    output.accept(ModBlocks.BAMBOO_THATCH);
                    output.accept(ModBlocks.BAMBOO_THATCH_EDGE);
                    output.accept(ModBlocks.BAMBOO_THATCH_PLATE);
                    output.accept(ModBlocks.FLAT_ROOF_TILES);
                    output.accept(ModBlocks.FLAT_ROOF_TILES_EDGE);
                    output.accept(ModBlocks.FLAT_ROOF_TILES_PLATE);
                    output.accept(ModBlocks.GRAY_ROOF_TILES);
                    output.accept(ModBlocks.GRAY_ROOF_TILES_EDGE);
                    output.accept(ModBlocks.GRAY_ROOF_TILES_PLATE);
                    output.accept(ModBlocks.ROOFING_SLATES);
                    output.accept(ModBlocks.ROOFING_SLATES_EDGE);
                    output.accept(ModBlocks.ROOFING_SLATES_PLATE);
                    // Vanilla wall + lattice sets
                    ModBlocks.VANILLA_WALLS.values().forEach(output::accept);
                    ModBlocks.VANILLA_LATTICES.values().forEach(output::accept);

                    // --- DoTB: French (Limestone) ---
                    output.accept(ModBlocks.LIMESTONE_BRICKS);
                    output.accept(ModBlocks.LIMESTONE_BRICKS_EDGE);
                    output.accept(ModBlocks.LIMESTONE_BRICKS_PLATE);
                    output.accept(ModBlocks.LIMESTONE_BANNISTER);

                    // --- DoTB: Roman (Marble) ---
                    output.accept(ModBlocks.MARBLE);
                    output.accept(ModBlocks.MARBLE_PILLAR);
                    output.accept(ModBlocks.MARBLE_FANCY_FENCE);
                    // Roman Sandstone
                    output.accept(ModBlocks.SANDSTONE_PLATE);
                    output.accept(ModBlocks.SANDSTONE_EDGE);
                    output.accept(ModBlocks.SANDSTONE_CRENELATION);
                    output.accept(ModBlocks.CUT_SANDSTONE_PLATE);
                    output.accept(ModBlocks.CUT_SANDSTONE_EDGE);
                    output.accept(ModBlocks.SMOOTH_SANDSTONE_PLATE);
                    output.accept(ModBlocks.SMOOTH_SANDSTONE_EDGE);
                    // Ochre Roof Tiles
                    output.accept(ModBlocks.OCHRE_ROOF_TILES);
                    output.accept(ModBlocks.OCHRE_ROOF_TILES_EDGE);
                    output.accept(ModBlocks.OCHRE_ROOF_TILES_PLATE);
                    // --- DoTB: German (Waxed Oak) ---
                    output.accept(ModBlocks.WAXED_OAK_PLANKS);
                    output.accept(ModBlocks.WAXED_OAK_LOG_STRIPPED);
                    output.accept(ModBlocks.WAXED_OAK_BEAM);
                    output.accept(ModBlocks.WAXED_OAK_PERGOLA);
                    output.accept(ModBlocks.WAXED_OAK_PLANKS_PLATE);
                    output.accept(ModBlocks.WAXED_OAK_PLANKS_EDGE);
                    output.accept(ModBlocks.WAXED_OAK_SUPPORT_BEAM);
                    output.accept(ModBlocks.WAXED_OAK_SUPPORT_SLAB);
                    output.accept(ModBlocks.WAXED_OAK_BANNISTER);
                    // German stone/glass
                    output.accept(ModBlocks.STONE_BRICKS_MASONRY);
                    output.accept(ModBlocks.STONE_BRICKS_MASONRY_EDGE);
                    output.accept(ModBlocks.STONE_BRICKS_MASONRY_PLATE);
                    output.accept(ModBlocks.CURVED_RAKED_GRAVEL);
                    output.accept(ModBlocks.STRAIGHT_RAKED_GRAVEL);

                    // --- DoTB: Japanese (Charred Spruce) ---
                    output.accept(ModBlocks.CHARRED_SPRUCE_PLANKS);
                    output.accept(ModBlocks.CHARRED_SPRUCE_LOG_STRIPPED);
                    output.accept(ModBlocks.CHARRED_SPRUCE_BOARDS);
                    output.accept(ModBlocks.CHARRED_SPRUCE_FOUNDATION);
                    output.accept(ModBlocks.CHARRED_SPRUCE_BEAM);
                    output.accept(ModBlocks.CHARRED_SPRUCE_PERGOLA);
                    output.accept(ModBlocks.CHARRED_SPRUCE_PLANKS_PLATE);
                    output.accept(ModBlocks.CHARRED_SPRUCE_PLANKS_EDGE);
                    output.accept(ModBlocks.CHARRED_SPRUCE_SUPPORT_BEAM);
                    output.accept(ModBlocks.CHARRED_SPRUCE_SUPPORT_SLAB);
                    output.accept(ModBlocks.LIGHT_GRAY_FUTON);

                    // --- DoTB: Persian ---
                    output.accept(ModBlocks.MORAQ_MOSAIC_BORDER);
                    output.accept(ModBlocks.MORAQ_MOSAIC_DELICATE);
                    output.accept(ModBlocks.MORAQ_MOSAIC_GEOMETRIC);
                    output.accept(ModBlocks.MORAQ_MOSAIC_PATTERN);
                    output.accept(ModBlocks.MORAQ_MOSAIC_RECESS);
                    output.accept(ModBlocks.MORAQ_MOSAIC_TRADITIONAL);
                    output.accept(ModBlocks.PERSIAN_CARPET_RED);
                    output.accept(ModBlocks.PERSIAN_CARPET_DELICATE_RED);
                    output.accept(ModBlocks.SANDSTONE_BRICKS);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_WALL);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_EDGE);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_PLATE);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_WALL);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_EDGE);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_PLATE);
                    output.accept(ModBlocks.GOLD_PLATED_SMOOTH_BLOCK);
                    output.accept(ModBlocks.GOLD_PLATED_SMOOTH_EDGE);
                    output.accept(ModBlocks.GOLD_PLATED_SMOOTH_PLATE);

                    // --- DoTB: Pre-Columbian ---
                    output.accept(ModBlocks.PLASTERED_STONE);
                    output.accept(ModBlocks.PLASTERED_STONE_EDGE);
                    output.accept(ModBlocks.PLASTERED_STONE_PLATE);
                    output.accept(ModBlocks.PLASTERED_STONE_WINDOW);
                    output.accept(ModBlocks.CHISELED_PLASTERED_STONE);
                    output.accept(ModBlocks.CHISELED_PLASTERED_STONE_FRIEZE);
                    output.accept(ModBlocks.ORNAMENTED_CHISELED_PLASTERED_STONE);
                    output.accept(ModBlocks.GOLDEN_STONE_FRIEZE);

                    // --- DoTB: Stone Brick Water Features ---
                    output.accept(ModBlocks.STONE_BRICKS_ARROWSLIT);
                    output.accept(ModBlocks.STONE_BRICKS_MACHICOLATION);
                    output.accept(ModBlocks.STONE_BRICKS_FAUCET);
                    output.accept(ModBlocks.STONE_BRICKS_POOL);
                    output.accept(ModBlocks.STONE_BRICKS_SMALL_POOL);
                    output.accept(ModBlocks.STONE_BRICKS_WATER_JET);
                    output.accept(ModBlocks.WATER_SOURCE_TRICKLE);

                }).build();
    }

    private static CreativeModeTab createColorSetsTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("color_sets")))
                .withTabsBefore(BLOCKS.getKey())
                .withTabsAfter(WOOD_SETS.getKey())
                .icon(() -> new ItemStack(ModBlocks.COLOR_SETS.get("amethyst").wool().get()))
                .displayItems((config, output) -> {
                    // Color Sets
                    ModBlocks.COLOR_SETS.forEach((name, set) -> {
                        output.accept(set.wool());
                        output.accept(set.carpet());
                        output.accept(set.terracotta());
                        output.accept(set.glazedTerracotta());
                        output.accept(set.concrete());
                        output.accept(set.concretePowder());
                        output.accept(set.stainedGlass());
                        output.accept(set.stainedGlassPane());
                        output.accept(set.shulkerBox());
                        output.accept(set.bed());
                        output.accept(set.candle());
                        output.accept(set.banner());
                    });

                    // Patterns (Dyed Cobblestone, Dyed Stone, Painted Planks)
                    ModBlocks.PATTERN_BLOCKS.forEach((pattern, colorMap) -> colorMap.values().forEach(output::accept));

                    // Elevators
                    ModBlocks.ELEVATORS.values().forEach(output::accept);

                }).build();
    }

    private static CreativeModeTab createDyesTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("dyes")))
                .withTabsBefore(WOOD_SETS.getKey())
                .withTabsAfter(OTT_EGGS.getKey())
                .icon(() -> new ItemStack(net.minecraft.world.item.Items.CYAN_DYE))
                .displayItems((params, output) -> {
                    output.accept(net.minecraft.world.item.Items.WHITE_DYE);
                    output.accept(net.minecraft.world.item.Items.LIGHT_GRAY_DYE);
                    output.accept(net.minecraft.world.item.Items.GRAY_DYE);
                    output.accept(net.minecraft.world.item.Items.BLACK_DYE);
                    output.accept(net.minecraft.world.item.Items.BROWN_DYE);
                    output.accept(net.minecraft.world.item.Items.RED_DYE);
                    output.accept(net.minecraft.world.item.Items.ORANGE_DYE);
                    output.accept(net.minecraft.world.item.Items.YELLOW_DYE);
                    output.accept(net.minecraft.world.item.Items.LIME_DYE);
                    output.accept(net.minecraft.world.item.Items.GREEN_DYE);
                    output.accept(net.minecraft.world.item.Items.CYAN_DYE);
                    output.accept(net.minecraft.world.item.Items.LIGHT_BLUE_DYE);
                    output.accept(net.minecraft.world.item.Items.BLUE_DYE);
                    output.accept(net.minecraft.world.item.Items.PURPLE_DYE);
                    output.accept(net.minecraft.world.item.Items.MAGENTA_DYE);
                    output.accept(net.minecraft.world.item.Items.PINK_DYE);

                    ModItems.CUSTOM_DYES.values().forEach(output::accept);
                }).build();
    }

    private static CreativeModeTab createOttEggsTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("ott_eggs")))
                .withTabsBefore(DYES.getKey())
                .withTabsAfter(MISC.getKey())
                .icon(() -> new ItemStack(ModItems.OTTER_SPAWN_EGG.get()))
                .displayItems((params, output) -> {
                    output.accept(ModItems.ALLIGATOR_SPAWN_EGG);
                    output.accept(ModItems.ANGELFISH_SPAWN_EGG);
                    output.accept(ModItems.ARID_YETI_SPAWN_EGG);
                    output.accept(ModItems.BABY_PHOENIX_SPAWN_EGG);
                    output.accept(ModItems.BABY_WIND_PHOENIX_SPAWN_EGG);
                    output.accept(ModItems.BARRELEYE_SPAWN_EGG);
                    output.accept(ModItems.BASS_SPAWN_EGG);
                    output.accept(ModItems.BEAVER_SPAWN_EGG);
                    output.accept(ModItems.BLACK_BEAR_SPAWN_EGG);
                    output.accept(ModItems.BLUEJAY_SPAWN_EGG);
                    output.accept(ModItems.BOGGED_BONE_STALKER_SPAWN_EGG);
                    output.accept(ModItems.BOGGED_SHADOW_SPAWN_EGG);
                    output.accept(ModItems.BONE_STALKER_SPAWN_EGG);
                    output.accept(ModItems.BONNETHEAD_SHARK_SPAWN_EGG);
                    output.accept(ModItems.BROWN_BEAR_SPAWN_EGG);
                    output.accept(ModItems.BURROWING_OWL_SPAWN_EGG);
                    output.accept(ModItems.BUSHDOG_SPAWN_EGG);
                    output.accept(ModItems.BUTTERFLY_SPAWN_EGG);
                    output.accept(ModItems.CANARY_SPAWN_EGG);
                    output.accept(ModItems.CANDYCANE_SNAIL_SPAWN_EGG);
                    output.accept(ModItems.CAPYBARA_SPAWN_EGG);
                    output.accept(ModItems.CARDINAL_SPAWN_EGG);
                    output.accept(ModItems.CATERPILLAR_SPAWN_EGG);
                    output.accept(ModItems.CATFISH_SPAWN_EGG);
                    output.accept(ModItems.CHERRY_TREE_ENT_SPAWN_EGG);
                    output.accept(ModItems.CHUPACABRA_SPAWN_EGG);
                    output.accept(ModItems.CICHLID_SPAWN_EGG);
                    output.accept(ModItems.COCONUT_CRAB_SPAWN_EGG);
                    output.accept(ModItems.CORAL_SEA_VIPER_SPAWN_EGG);
                    output.accept(ModItems.COUGAR_SPAWN_EGG);
                    output.accept(ModItems.COYOTE_SPAWN_EGG);
                    output.accept(ModItems.DEER_SPAWN_EGG);
                    output.accept(ModItems.DRAGONFLY_SPAWN_EGG);
                    output.accept(ModItems.DUCK_SPAWN_EGG);
                    output.accept(ModItems.DUMBO_OCTOPUS_SPAWN_EGG);
                    output.accept(ModItems.ECHIDNA_SPAWN_EGG);
                    output.accept(ModItems.ELEPHANT_SPAWN_EGG);
                    output.accept(ModItems.EMU_SPAWN_EGG);
                    output.accept(ModItems.FENNEC_FOX_SPAWN_EGG);
                    output.accept(ModItems.FERRET_SPAWN_EGG);
                    output.accept(ModItems.FIDDLER_CRAB_SPAWN_EGG);
                    output.accept(ModItems.FINCH_SPAWN_EGG);
                    output.accept(ModItems.FIREFLY_SPAWN_EGG);
                    output.accept(ModItems.FIRE_SALAMANDER_SPAWN_EGG);
                    output.accept(ModItems.FLOUNDER_SPAWN_EGG);
                    output.accept(ModItems.GECKO_SPAWN_EGG);
                    output.accept(ModItems.GEIST_SPAWN_EGG);
                    output.accept(ModItems.GHOST_SPAWN_EGG);
                    output.accept(ModItems.GIANT_SOFTSHELL_TURTLE_SPAWN_EGG);
                    output.accept(ModItems.GILDED_TREE_ENT_SPAWN_EGG);
                    output.accept(ModItems.GIRAFFE_SPAWN_EGG);
                    output.accept(ModItems.GLARE_SPAWN_EGG);
                    output.accept(ModItems.GOBLIN_SHARK_SPAWN_EGG);
                    output.accept(ModItems.GOLDEN_HERMIT_KING_SPAWN_EGG);
                    output.accept(ModItems.GOOSE_SPAWN_EGG);
                    output.accept(ModItems.GUINEA_FOWL_SPAWN_EGG);
                    output.accept(ModItems.GUITARFISH_SPAWN_EGG);
                    output.accept(ModItems.HAUNT_SPAWN_EGG);
                    output.accept(ModItems.HEDGEHOG_SPAWN_EGG);
                    output.accept(ModItems.HERMIT_KING_SPAWN_EGG);
                    output.accept(ModItems.HIPPO_SPAWN_EGG);
                    output.accept(ModItems.HOOPOE_SPAWN_EGG);
                    output.accept(ModItems.HOWLER_SPAWN_EGG);
                    output.accept(ModItems.ICEOLOGER_SPAWN_EGG);
                    output.accept(ModItems.ILLUSIONER_SPAWN_EGG);
                    output.accept(ModItems.IMPALA_SPAWN_EGG);
                    output.accept(ModItems.MEDIUM_JELLYFISH_SPAWN_EGG);
                    output.accept(ModItems.SMALL_JELLYFISH_SPAWN_EGG);
                    output.accept(ModItems.LARGE_JELLYFISH_SPAWN_EGG);
                    output.accept(ModItems.JUMPING_SPIDER_SPAWN_EGG);
                    output.accept(ModItems.KIWI_SPAWN_EGG);
                    output.accept(ModItems.KOI_FISH_SPAWN_EGG);
                    output.accept(ModItems.KRILL_SPAWN_EGG);
                    output.accept(ModItems.LEOPARD_CAT_SPAWN_EGG);
                    output.accept(ModItems.LION_SPAWN_EGG);
                    output.accept(ModItems.LIZARD_SPAWN_EGG);
                    output.accept(ModItems.MAMMOTH_SPAWN_EGG);
                    output.accept(ModItems.MANTA_RAY_SPAWN_EGG);
                    output.accept(ModItems.MAN_O_WAR_SPAWN_EGG);
                    output.accept(ModItems.MARINE_IGUANA_SPAWN_EGG);
                    output.accept(ModItems.MARMOT_SPAWN_EGG);
                    output.accept(ModItems.MAULER_SPAWN_EGG);
                    output.accept(ModItems.MOLE_SPAWN_EGG);
                    output.accept(ModItems.MOOSE_SPAWN_EGG);
                    output.accept(ModItems.MOUSE_SPAWN_EGG);
                    output.accept(ModItems.MYCELIUM_MAMMOTH_SPAWN_EGG);
                    output.accept(ModItems.OTTER_SPAWN_EGG);
                    output.accept(ModItems.PALLAS_CAT_SPAWN_EGG);
                    output.accept(ModItems.PENGUIN_SPAWN_EGG);
                    output.accept(ModItems.PHEASANT_SPAWN_EGG);
                    output.accept(ModItems.PHOENIX_SPAWN_EGG);
                    output.accept(ModItems.PINK_LAND_IGUANA_SPAWN_EGG);
                    output.accept(ModItems.PIT_VIPER_SPAWN_EGG);
                    output.accept(ModItems.PSYCHO_JELLY_SPAWN_EGG);
                    output.accept(ModItems.QUAIL_SPAWN_EGG);
                    output.accept(ModItems.RASCAL_SPAWN_EGG);
                    output.accept(ModItems.RATTLESNAKE_SPAWN_EGG);
                    output.accept(ModItems.RED_PANDA_SPAWN_EGG);
                    output.accept(ModItems.REINDEER_SPAWN_EGG);
                    output.accept(ModItems.RHINO_SPAWN_EGG);
                    output.accept(ModItems.RINGTAIL_SPAWN_EGG);
                    output.accept(ModItems.RIVER_TURTLE_SPAWN_EGG);
                    output.accept(ModItems.ROBIN_SPAWN_EGG);
                    output.accept(ModItems.SAND_CRAB_SPAWN_EGG);
                    output.accept(ModItems.SASQUATCH_SPAWN_EGG);
                    output.accept(ModItems.SEAHORSE_SPAWN_EGG);
                    output.accept(ModItems.SEAL_SPAWN_EGG);
                    output.accept(ModItems.SEA_BUNNY_SPAWN_EGG);
                    output.accept(ModItems.SEA_URCHIN_SPAWN_EGG);
                    output.accept(ModItems.SEA_VIPER_SPAWN_EGG);
                    output.accept(ModItems.SHADOW_SPAWN_EGG);
                    output.accept(ModItems.ETHEREAL_SHRIMP_SPAWN_EGG);
                    output.accept(ModItems.SKINWALKER_SPAWN_EGG);
                    output.accept(ModItems.SMALL_FIREFLY_SPAWN_EGG);
                    output.accept(ModItems.SNAIL_SPAWN_EGG);
                    output.accept(ModItems.SNAKE_SPAWN_EGG);
                    output.accept(ModItems.SPARROW_SPAWN_EGG);
                    output.accept(ModItems.SPECTRE_SPAWN_EGG);
                    output.accept(ModItems.SPOONBILL_SPAWN_EGG);
                    output.accept(ModItems.SQUONK_SPAWN_EGG);
                    output.accept(ModItems.STARFISH_SPAWN_EGG);
                    output.accept(ModItems.STINGRAY_SPAWN_EGG);
                    output.accept(ModItems.STORK_SPAWN_EGG);
                    output.accept(ModItems.SUNFISH_SPAWN_EGG);
                    output.accept(ModItems.TORTOISE_SPAWN_EGG);
                    output.accept(ModItems.TOUCAN_SPAWN_EGG);
                    output.accept(ModItems.TREE_ENT_SPAWN_EGG);
                    output.accept(ModItems.TREE_KANGAROO_SPAWN_EGG);
                    output.accept(ModItems.TUFF_GOLEM_SPAWN_EGG);
                    output.accept(ModItems.TURKEY_SPAWN_EGG);
                    output.accept(ModItems.VILE_GATOR_SPAWN_EGG);
                    output.accept(ModItems.VULTURE_SPAWN_EGG);
                    output.accept(ModItems.WATER_BUFFALO_SPAWN_EGG);
                    output.accept(ModItems.WECHUGE_SPAWN_EGG);
                    output.accept(ModItems.WENDIGO_SPAWN_EGG);
                    output.accept(ModItems.WHITE_DEER_SPAWN_EGG);
                    output.accept(ModItems.WILDFIRE_SPAWN_EGG);
                    output.accept(ModItems.WIND_PHOENIX_SPAWN_EGG);
                    output.accept(ModItems.WOLVERINE_SPAWN_EGG);
                    output.accept(ModItems.YETI_SPAWN_EGG);
                    output.accept(ModItems.ZEBRA_SPAWN_EGG);
                }).build();
    }

    private static CreativeModeTab createWoodSetsTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("wood_sets")))
                .withTabsBefore(COLOR_SETS.getKey())
                .withTabsAfter(DYES.getKey())
                .icon(() -> new ItemStack(ModBlocks.WOOD_SETS.get("starlight").log().get()))
                .displayItems((config, output) -> {
                    // Starlight, Midnight, etc.
                    ModBlocks.WOOD_SETS.forEach((name, set) -> {
                        output.accept(set.log());
                        output.accept(set.wood());
                        output.accept(set.strippedLog());
                        output.accept(set.strippedWood());
                        output.accept(set.planks());
                        output.accept(set.stairs());
                        output.accept(set.slab());
                        output.accept(set.fence());
                        output.accept(set.fenceGate());
                        output.accept(set.door());
                        output.accept(set.trapdoor());
                        output.accept(set.button());
                        output.accept(set.pressurePlate());
                        output.accept(set.leaves());
                        output.accept(set.sapling());
                        output.accept(ModItems.WOOD_SET_SIGNS.get(name));
                        output.accept(ModItems.WOOD_SET_HANGING_SIGNS.get(name));
                        output.accept(ModItems.WOOD_SET_BOATS.get(name));
                        output.accept(ModItems.WOOD_SET_CHEST_BOATS.get(name));
                    });

                    // Pale Oak (Backported)
                    output.accept(ModBlocks.PALE_OAK_LOG);
                    output.accept(ModBlocks.PALE_OAK_WOOD);
                    output.accept(ModBlocks.STRIPPED_PALE_OAK_LOG);
                    output.accept(ModBlocks.STRIPPED_PALE_OAK_WOOD);
                    output.accept(ModBlocks.PALE_OAK_PLANKS);
                    output.accept(ModBlocks.PALE_OAK_STAIRS);
                    output.accept(ModBlocks.PALE_OAK_SLAB);
                    output.accept(ModBlocks.PALE_OAK_FENCE);
                    output.accept(ModBlocks.PALE_OAK_FENCE_GATE);
                    output.accept(ModBlocks.PALE_OAK_DOOR);
                    output.accept(ModBlocks.PALE_OAK_TRAPDOOR);
                    output.accept(ModBlocks.PALE_OAK_PRESSURE_PLATE);
                    output.accept(ModBlocks.PALE_OAK_BUTTON);
                    output.accept(ModItems.PALE_OAK_SIGN);
                    output.accept(ModItems.PALE_OAK_HANGING_SIGN);
                    output.accept(ModBlocks.PALE_OAK_LEAVES);
                    output.accept(ModBlocks.PALE_OAK_SAPLING);
                    output.accept(ModItems.PALE_OAK_BOAT);
                    output.accept(ModItems.PALE_OAK_CHEST_BOAT);
                }).build();
    }

    private static CreativeModeTab createMiscTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("misc")))
                .withTabsBefore(OTT_EGGS.getKey())
                .icon(() -> new ItemStack(ModBlocks.WATER_LANTERN.get()))
                .displayItems((config, output) -> {
                    output.accept(ModItems.CLAM);
                    output.accept(ModItems.KOI_FISH);
                    output.accept(ModItems.PEARL);
                    output.accept(ModItems.SILK);
                    output.accept(ModItems.SNAIL_SHELL);

                    output.accept(ModItems.ANGELFISH_BUCKET);
                    output.accept(ModItems.BARRELEYE_BUCKET);
                    output.accept(ModItems.BASS_BUCKET);
                    output.accept(ModItems.BONNETHEAD_SHARK_BUCKET);
                    output.accept(ModItems.CATFISH_BUCKET);
                    output.accept(ModItems.CICHLID_BUCKET);
                    output.accept(ModItems.DUMBO_OCTOPUS_BUCKET);
                    output.accept(ModItems.FLOUNDER_BUCKET);
                    output.accept(ModItems.GOBLIN_SHARK_BUCKET);
                    output.accept(ModItems.GUITARFISH_BUCKET);
                    output.accept(ModItems.KOI_FISH_BUCKET);
                    output.accept(ModItems.KRILL_BUCKET);
                    output.accept(ModItems.MAN_O_WAR_BUCKET);
                    output.accept(ModItems.MARINE_IGUANA_BUCKET);
                    output.accept(ModItems.PSYCHO_JELLY_BUCKET);
                    output.accept(ModItems.SEA_BUNNY_BUCKET);
                    output.accept(ModItems.SNAIL_BUCKET);
                    output.accept(ModItems.STINGRAY_BUCKET);
                    output.accept(ModItems.SUNFISH_BUCKET);
                    output.accept(ModItems.LARGE_JELLYFISH_BUCKET);
                    output.accept(ModItems.SMALL_JELLYFISH_BUCKET);
                    output.accept(ModItems.MEDIUM_JELLYFISH_BUCKET);
                    output.accept(ModItems.SEAHORSE_BUCKET);
                    output.accept(ModItems.ETHEREAL_SHRIMP_BUCKET);

                    // Salt
                    output.accept(ModItems.SALT);
                    output.accept(ModBlocks.SALT_LAMP);

                    // Glow Goop
                    output.accept(ModBlocks.GLOW_GOOP);

                    // Lanterns
                    output.accept(ModBlocks.WATER_LANTERN);
                    output.accept(ModBlocks.LAVA_LANTERN);
                    output.accept(ModBlocks.PROTECTIVE_LANTERN);
                    output.accept(ModBlocks.SMITE_LANTERN);

                    // Shelves
                    ModBlocks.SHELVES.forEach(output::accept);

                    // Nature/Misc
                    output.accept(ModBlocks.PALE_MOSS_BLOCK);
                    output.accept(ModBlocks.PALE_MOSS_CARPET);
                    output.accept(ModBlocks.PALE_HANGING_MOSS);
                    output.accept(ModBlocks.CREAKING_HEART);
                    output.accept(ModBlocks.OPEN_EYEBLOSSOM);
                    output.accept(ModBlocks.CLOSED_EYEBLOSSOM);
                    output.accept(ModBlocks.BUSH);
                    output.accept(ModBlocks.FIREFLY_BUSH);
                    output.accept(ModBlocks.WILDFLOWERS);
                    output.accept(ModBlocks.LEAF_LITTER);
                    output.accept(ModBlocks.CACTUS_FLOWER);
                    output.accept(ModBlocks.SHORT_DRY_GRASS);
                    output.accept(ModBlocks.TALL_DRY_GRASS);
                    output.accept(ModBlocks.DRIED_GHAST);
                    output.accept(ModBlocks.WEATHERING_STATION);
                    output.accept(ModBlocks.DRAGON_SKULL);
                    output.accept(ModBlocks.SILK_COCOON);
                    output.accept(ModBlocks.CHRYSALIS);
                    output.accept(ModBlocks.THORNY_HEDGE);
                    output.accept(ModItems.THORNY_HEDGE_SPROUTS);
                    ModBlocks.PARTICLE_HEDGES.values().forEach(output::accept);
                    ModBlocks.CREEPING_HEDGES.values().forEach(output::accept);

                    output.accept(ModItems.BIG_LILY_PAD);
                    output.accept(ModItems.OAK_NEST);
                    output.accept(ModBlocks.COCONUT);
                    output.accept(ModBlocks.RESIN_CLUMP);

                    // Jars
                    output.accept(ModBlocks.GLASS_JAR);
                    output.accept(ModBlocks.FIREFLY_IN_A_JAR);
                    output.accept(ModBlocks.FIREFLIES_IN_A_JAR);
                    output.accept(ModBlocks.FIREFLY_JAR);
                    ModBlocks.BUTTERFLY_JARS.values().forEach(output::accept);
                    output.accept(ModBlocks.CATERPILLAR_JAR);

                    // Caught Butterflies
                    for (Butterfly.Variant variant : Butterfly.Variant.values()) {
                        output.accept(ModItems.BUTTERFLIES.get(variant).get());
                    }
                    output.accept(ModItems.CATERPILLAR.get());

                    // Food and Drops
                    output.accept(ModItems.BASS);
                    output.accept(ModItems.COOKED_BASS);
                    output.accept(ModItems.RAW_BONNETHEAD);
                    output.accept(ModItems.COOKED_BONNETHEAD);
                    output.accept(ModItems.CATFISH);
                    output.accept(ModItems.COOKED_CATFISH);
                    output.accept(ModItems.RAW_CICHLID);
                    output.accept(ModItems.COOKED_CICHLID);
                    output.accept(ModItems.RAW_GOBLIN_SHARK);
                    output.accept(ModItems.COOKED_GOBLIN_SHARK);
                    output.accept(ModItems.RAW_GUITARFISH);
                    output.accept(ModItems.COOKED_GUITARFISH);
                    output.accept(ModItems.RAW_SUNFISH_MEAT);
                    output.accept(ModItems.COOKED_SUNFISH_MEAT);
                    output.accept(ModItems.RAW_GOLDEN_SUNFISH_MEAT);
                    output.accept(ModItems.COOKED_GOLDEN_SUNFISH_MEAT);
                    output.accept(ModItems.RAW_CRAB_MEAT);
                    output.accept(ModItems.STEAMED_CRAB_MEAT);
                    output.accept(ModItems.RAW_KRILL);
                    output.accept(ModItems.FRIED_KRILL);
                    output.accept(ModItems.RAW_SHRIMP);
                    output.accept(ModItems.STEAMED_SHRIMP);
                    output.accept(ModItems.RAW_SNAIL);
                    output.accept(ModItems.COOKED_SNAIL);
                    output.accept(ModItems.JELLYFISH_JELLY);
                    output.accept(ModItems.SEA_URCHIN_CAVIAR);
                    output.accept(ModItems.RAW_WILD_GAME_MEAT);
                    output.accept(ModItems.COOKED_WILD_GAME_MEAT);
                    output.accept(ModItems.RAW_WILD_BIRD_MEAT);
                    output.accept(ModItems.COOKED_WILD_BIRD_MEAT);
                    output.accept(ModItems.LIZARD_TAIL);
                    output.accept(ModItems.COOKED_LIZARD_TAIL);
                    output.accept(ModItems.COOKED_EGG);

                    // Eggs
                    output.accept(ModItems.BLUE_EGG);
                    output.accept(ModItems.BROWN_EGG);
                    output.accept(ModItems.DUCK_EGG);
                    output.accept(ModItems.EMU_EGG);
                    output.accept(ModItems.HOOPOE_EGG);
                    output.accept(ModItems.KIWI_EGG);
                    output.accept(ModItems.PENGUIN_EGG);
                    output.accept(ModItems.PHEASANT_EGG);
                    output.accept(ModItems.TOUCAN_EGG);
                    output.accept(ModItems.ALLIGATOR_EGG);
                    output.accept(ModItems.CRAB_CLAW);
                    output.accept(ModItems.CRAB_EGG);
                    output.accept(ModItems.SNAIL_EGG);
                    output.accept(ModItems.TORTOISE_EGG);

                    // Structural
                    ModBlocks.VANILLA_STRUCTURAL_SETS.values().forEach(set -> {
                        output.accept(set.beam());
                        output.accept(set.pergola());
                        output.accept(set.planksPlate());
                        output.accept(set.planksEdge());
                        output.accept(set.bannister());
                        output.accept(set.supportSlab());
                        output.accept(set.supportBeam());
                    });
                    ModBlocks.VANILLA_WALLS.values().forEach(output::accept);
                    ModBlocks.VANILLA_LATTICES.values().forEach(output::accept);
                    ModBlocks.WOOD_SETS.values().forEach(set -> {
                        output.accept(set.beam());
                        output.accept(set.pergola());
                        output.accept(set.planksPlate());
                        output.accept(set.planksEdge());
                        output.accept(set.bannister());
                        output.accept(set.supportSlab());
                        output.accept(set.supportBeam());
                    });

                    // Stone Brick Functional
                    output.accept(ModBlocks.STONE_BRICKS_ARROWSLIT);
                    output.accept(ModBlocks.STONE_BRICKS_MACHICOLATION);

                    // Water Stuffs
                    output.accept(ModBlocks.WATER_SOURCE_TRICKLE);
                    output.accept(ModBlocks.STONE_BRICKS_FAUCET);
                    output.accept(ModBlocks.STONE_BRICKS_WATER_JET);
                    output.accept(ModBlocks.STONE_BRICKS_POOL);
                    output.accept(ModBlocks.STONE_BRICKS_SMALL_POOL);

                    // DoTB Phase 2: Limestone
                    output.accept(ModBlocks.LIMESTONE_BRICKS);
                    output.accept(ModBlocks.LIMESTONE_BRICKS_EDGE);
                    output.accept(ModBlocks.LIMESTONE_BRICKS_PLATE);
                    output.accept(ModBlocks.LIMESTONE_BANNISTER);
                    output.accept(ModBlocks.COBBLED_LIMESTONE);

                    // DoTB Phase 2: Marble
                    output.accept(ModBlocks.MARBLE);
                    output.accept(ModBlocks.MARBLE_PILLAR);
                    output.accept(ModBlocks.MARBLE_FANCY_FENCE);

                    // DoTB Phase 2: Sandstone decorative
                    output.accept(ModBlocks.SANDSTONE_PLATE);
                    output.accept(ModBlocks.SANDSTONE_EDGE);
                    output.accept(ModBlocks.SANDSTONE_CRENELATION);
                    output.accept(ModBlocks.CUT_SANDSTONE_PLATE);
                    output.accept(ModBlocks.CUT_SANDSTONE_EDGE);
                    output.accept(ModBlocks.SMOOTH_SANDSTONE_PLATE);
                    output.accept(ModBlocks.SMOOTH_SANDSTONE_EDGE);

                    // DoTB Phase 2: Ochre Roof Tiles
                    output.accept(ModBlocks.OCHRE_ROOF_TILES);
                    output.accept(ModBlocks.OCHRE_ROOF_TILES_EDGE);
                    output.accept(ModBlocks.OCHRE_ROOF_TILES_PLATE);

                    // DoTB Phase 2: Flat/Gray Roof Tiles
                    output.accept(ModBlocks.FLAT_ROOF_TILES);
                    output.accept(ModBlocks.FLAT_ROOF_TILES_EDGE);
                    output.accept(ModBlocks.FLAT_ROOF_TILES_PLATE);
                    output.accept(ModBlocks.GRAY_ROOF_TILES);
                    output.accept(ModBlocks.GRAY_ROOF_TILES_EDGE);
                    output.accept(ModBlocks.GRAY_ROOF_TILES_PLATE);

                    // DoTB Phase 2: Roofing Slates
                    output.accept(ModBlocks.ROOFING_SLATES);
                    output.accept(ModBlocks.ROOFING_SLATES_EDGE);
                    output.accept(ModBlocks.ROOFING_SLATES_PLATE);

                    // DoTB Phase 2: Thatch
                    output.accept(ModBlocks.WHEAT_THATCH);
                    output.accept(ModBlocks.WHEAT_THATCH_EDGE);
                    output.accept(ModBlocks.WHEAT_THATCH_PLATE);
                    output.accept(ModBlocks.BAMBOO_THATCH);
                    output.accept(ModBlocks.BAMBOO_THATCH_EDGE);
                    output.accept(ModBlocks.BAMBOO_THATCH_PLATE);

                    // DoTB Phase 3: Waxed Oak (German)
                    output.accept(ModBlocks.WAXED_OAK_PLANKS);
                    output.accept(ModBlocks.WAXED_OAK_LOG_STRIPPED);
                    output.accept(ModBlocks.WAXED_OAK_BEAM);
                    output.accept(ModBlocks.WAXED_OAK_PERGOLA);
                    output.accept(ModBlocks.WAXED_OAK_PLANKS_PLATE);
                    output.accept(ModBlocks.WAXED_OAK_PLANKS_EDGE);
                    output.accept(ModBlocks.WAXED_OAK_SUPPORT_BEAM);
                    output.accept(ModBlocks.WAXED_OAK_SUPPORT_SLAB);
                    output.accept(ModBlocks.WAXED_OAK_BANNISTER);

                    // DoTB Phase 3: Stone Bricks Masonry + German misc
                    output.accept(ModBlocks.STONE_BRICKS_MASONRY);
                    output.accept(ModBlocks.STONE_BRICKS_MASONRY_EDGE);
                    output.accept(ModBlocks.STONE_BRICKS_MASONRY_PLATE);
                    output.accept(ModBlocks.CURVED_RAKED_GRAVEL);
                    output.accept(ModBlocks.STRAIGHT_RAKED_GRAVEL);

                    // DoTB Phase 3: Charred Spruce (Japanese)
                    output.accept(ModBlocks.CHARRED_SPRUCE_PLANKS);
                    output.accept(ModBlocks.CHARRED_SPRUCE_LOG_STRIPPED);
                    output.accept(ModBlocks.CHARRED_SPRUCE_BOARDS);
                    output.accept(ModBlocks.CHARRED_SPRUCE_FOUNDATION);
                    output.accept(ModBlocks.CHARRED_SPRUCE_BEAM);
                    output.accept(ModBlocks.CHARRED_SPRUCE_PERGOLA);
                    output.accept(ModBlocks.CHARRED_SPRUCE_PLANKS_PLATE);
                    output.accept(ModBlocks.CHARRED_SPRUCE_PLANKS_EDGE);
                    output.accept(ModBlocks.CHARRED_SPRUCE_SUPPORT_BEAM);
                    output.accept(ModBlocks.CHARRED_SPRUCE_SUPPORT_SLAB);

                    // DoTB Phase 4: Persian Sandstone Bricks
                    output.accept(ModBlocks.SANDSTONE_BRICKS);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_WALL);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_EDGE);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_PLATE);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_WALL);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_EDGE);
                    output.accept(ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_PLATE);

                    // DoTB Phase 4: Persian Carpets
                    output.accept(ModBlocks.PERSIAN_CARPET_RED);
                    output.accept(ModBlocks.PERSIAN_CARPET_DELICATE_RED);

                    // DoTB Phase 4: Gold Plated Smooth (Persian)
                    output.accept(ModBlocks.GOLD_PLATED_SMOOTH_BLOCK);
                    output.accept(ModBlocks.GOLD_PLATED_SMOOTH_EDGE);
                    output.accept(ModBlocks.GOLD_PLATED_SMOOTH_PLATE);

                    // Very Random
                    output.accept(ModItems.COCONUT);
                    output.accept(ModItems.WILDFIRE_CROWN);
                    output.accept(ModItems.WILDFIRE_CROWN_FRAGMENT);

                }).build();
    }

    private static String createTranslationKey(String tabName) {
        return ITEM_GROUP_PREFIX + tabName;
    }
}