package com.otterly76.ott.client.gui.creative;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.color.ModPatterns;
import com.otterly76.ott.entity.custom.Butterfly;
import com.otterly76.ott.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public enum OttCreativeCategories {

    // ── Sea Creatures ─────────────────────────────────────────────────────────
    AQUATIC("aquatic",
            ModItems.LARGE_JELLYFISH_BUCKET,
            (params, output) -> {
                // Critter buckets
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
                output.accept(ModItems.SMALL_JELLYFISH_BUCKET);
                output.accept(ModItems.MEDIUM_JELLYFISH_BUCKET);
                output.accept(ModItems.LARGE_JELLYFISH_BUCKET);
                output.accept(ModItems.KOI_FISH_BUCKET);
                output.accept(ModItems.KRILL_BUCKET);
                output.accept(ModItems.MAN_O_WAR_BUCKET);
                output.accept(ModItems.MARINE_IGUANA_BUCKET);
                output.accept(ModItems.PSYCHO_JELLY_BUCKET);
                output.accept(ModItems.SEA_BUNNY_BUCKET);
                output.accept(ModItems.SEAHORSE_BUCKET);
                output.accept(ModItems.ETHEREAL_SHRIMP_BUCKET);
                output.accept(ModItems.SNAIL_BUCKET);
                output.accept(ModItems.STINGRAY_BUCKET);
                output.accept(ModItems.SUNFISH_BUCKET);
                // Spawn eggs
                output.accept(ModItems.ANGELFISH_SPAWN_EGG);
                output.accept(ModItems.BARRELEYE_SPAWN_EGG);
                output.accept(ModItems.BASS_SPAWN_EGG);
                output.accept(ModItems.BONNETHEAD_SHARK_SPAWN_EGG);
                output.accept(ModItems.CATFISH_SPAWN_EGG);
                output.accept(ModItems.CICHLID_SPAWN_EGG);
                output.accept(ModItems.COCONUT_CRAB_SPAWN_EGG);
                output.accept(ModItems.CORAL_SEA_VIPER_SPAWN_EGG);
                output.accept(ModItems.DUMBO_OCTOPUS_SPAWN_EGG);
                output.accept(ModItems.FIDDLER_CRAB_SPAWN_EGG);
                output.accept(ModItems.FLOUNDER_SPAWN_EGG);
                output.accept(ModItems.GOBLIN_SHARK_SPAWN_EGG);
                output.accept(ModItems.GUITARFISH_SPAWN_EGG);
                output.accept(ModItems.SMALL_JELLYFISH_SPAWN_EGG);
                output.accept(ModItems.MEDIUM_JELLYFISH_SPAWN_EGG);
                output.accept(ModItems.LARGE_JELLYFISH_SPAWN_EGG);
                output.accept(ModItems.KOI_FISH_SPAWN_EGG);
                output.accept(ModItems.KRILL_SPAWN_EGG);
                output.accept(ModItems.MAN_O_WAR_SPAWN_EGG);
                output.accept(ModItems.MANTA_RAY_SPAWN_EGG);
                output.accept(ModItems.MARINE_IGUANA_SPAWN_EGG);
                output.accept(ModItems.PSYCHO_JELLY_SPAWN_EGG);
                output.accept(ModItems.SAND_CRAB_SPAWN_EGG);
                output.accept(ModItems.SEA_BUNNY_SPAWN_EGG);
                output.accept(ModItems.SEAHORSE_SPAWN_EGG);
                output.accept(ModItems.SEAL_SPAWN_EGG);
                output.accept(ModItems.SEA_URCHIN_SPAWN_EGG);
                output.accept(ModItems.SEA_VIPER_SPAWN_EGG);
                output.accept(ModItems.SNAIL_SPAWN_EGG);
                output.accept(ModItems.STARFISH_SPAWN_EGG);
                output.accept(ModItems.STINGRAY_SPAWN_EGG);
                output.accept(ModItems.SUNFISH_SPAWN_EGG);
                output.accept(ModItems.ETHEREAL_SHRIMP_SPAWN_EGG);
            }),

    // ── Wildlife: real-world land animals, birds, reptiles, insects ───────────
    WILDLIFE("wildlife",
            ModItems.OTTER_SPAWN_EGG,
            (params, output) -> {
                // Birds
                output.accept(ModItems.BLUEJAY_SPAWN_EGG);
                output.accept(ModItems.BURROWING_OWL_SPAWN_EGG);
                output.accept(ModItems.CANARY_SPAWN_EGG);
                output.accept(ModItems.CARDINAL_SPAWN_EGG);
                output.accept(ModItems.DUCK_SPAWN_EGG);
                output.accept(ModItems.EMU_SPAWN_EGG);
                output.accept(ModItems.FINCH_SPAWN_EGG);
                output.accept(ModItems.GOOSE_SPAWN_EGG);
                output.accept(ModItems.GUINEA_FOWL_SPAWN_EGG);
                output.accept(ModItems.HOOPOE_SPAWN_EGG);
                output.accept(ModItems.KIWI_SPAWN_EGG);
                output.accept(ModItems.PENGUIN_SPAWN_EGG);
                output.accept(ModItems.PHEASANT_SPAWN_EGG);
                output.accept(ModItems.QUAIL_SPAWN_EGG);
                output.accept(ModItems.ROBIN_SPAWN_EGG);
                output.accept(ModItems.SPARROW_SPAWN_EGG);
                output.accept(ModItems.SPOONBILL_SPAWN_EGG);
                output.accept(ModItems.STORK_SPAWN_EGG);
                output.accept(ModItems.TOUCAN_SPAWN_EGG);
                output.accept(ModItems.TURKEY_SPAWN_EGG);
                output.accept(ModItems.VULTURE_SPAWN_EGG);
                // Reptiles & Amphibians
                output.accept(ModItems.ALLIGATOR_SPAWN_EGG);
                output.accept(ModItems.FIRE_SALAMANDER_SPAWN_EGG);
                output.accept(ModItems.GECKO_SPAWN_EGG);
                output.accept(ModItems.GIANT_SOFTSHELL_TURTLE_SPAWN_EGG);
                output.accept(ModItems.LIZARD_SPAWN_EGG);
                output.accept(ModItems.PINK_LAND_IGUANA_SPAWN_EGG);
                output.accept(ModItems.PIT_VIPER_SPAWN_EGG);
                output.accept(ModItems.RATTLESNAKE_SPAWN_EGG);
                output.accept(ModItems.RIVER_TURTLE_SPAWN_EGG);
                output.accept(ModItems.SNAKE_SPAWN_EGG);
                output.accept(ModItems.TORTOISE_SPAWN_EGG);
                // Mammals
                output.accept(ModItems.BEAVER_SPAWN_EGG);
                output.accept(ModItems.BLACK_BEAR_SPAWN_EGG);
                output.accept(ModItems.BROWN_BEAR_SPAWN_EGG);
                output.accept(ModItems.BUSHDOG_SPAWN_EGG);
                output.accept(ModItems.CANDYCANE_SNAIL_SPAWN_EGG);
                output.accept(ModItems.CAPYBARA_SPAWN_EGG);
                output.accept(ModItems.COUGAR_SPAWN_EGG);
                output.accept(ModItems.COYOTE_SPAWN_EGG);
                output.accept(ModItems.DEER_SPAWN_EGG);
                output.accept(ModItems.ECHIDNA_SPAWN_EGG);
                output.accept(ModItems.ELEPHANT_SPAWN_EGG);
                output.accept(ModItems.FENNEC_FOX_SPAWN_EGG);
                output.accept(ModItems.FERRET_SPAWN_EGG);
                output.accept(ModItems.GIRAFFE_SPAWN_EGG);
                output.accept(ModItems.HEDGEHOG_SPAWN_EGG);
                output.accept(ModItems.HIPPO_SPAWN_EGG);
                output.accept(ModItems.IMPALA_SPAWN_EGG);
                output.accept(ModItems.LEOPARD_CAT_SPAWN_EGG);
                output.accept(ModItems.LION_SPAWN_EGG);
                output.accept(ModItems.MAMMOTH_SPAWN_EGG);
                output.accept(ModItems.MARMOT_SPAWN_EGG);
                output.accept(ModItems.MOLE_SPAWN_EGG);
                output.accept(ModItems.MOOSE_SPAWN_EGG);
                output.accept(ModItems.MOUSE_SPAWN_EGG);
                output.accept(ModItems.OTTER_SPAWN_EGG);
                output.accept(ModItems.PALLAS_CAT_SPAWN_EGG);
                output.accept(ModItems.RED_PANDA_SPAWN_EGG);
                output.accept(ModItems.REINDEER_SPAWN_EGG);
                output.accept(ModItems.RHINO_SPAWN_EGG);
                output.accept(ModItems.RINGTAIL_SPAWN_EGG);
                output.accept(ModItems.TREE_KANGAROO_SPAWN_EGG);
                output.accept(ModItems.WATER_BUFFALO_SPAWN_EGG);
                output.accept(ModItems.WHITE_DEER_SPAWN_EGG);
                output.accept(ModItems.WOLVERINE_SPAWN_EGG);
                output.accept(ModItems.ZEBRA_SPAWN_EGG);
                // Insects & small critters
                output.accept(ModItems.BUTTERFLY_SPAWN_EGG);
                output.accept(ModItems.CATERPILLAR_SPAWN_EGG);
                output.accept(ModItems.DRAGONFLY_SPAWN_EGG);
                output.accept(ModItems.FIREFLY_SPAWN_EGG);
                output.accept(ModItems.JUMPING_SPIDER_SPAWN_EGG);
                output.accept(ModItems.SMALL_FIREFLY_SPAWN_EGG);
            }),

    // ── Mythical & Hostile ────────────────────────────────────────────────────
    MYTHICAL("mythical",
            ModItems.GLARE_SPAWN_EGG,
            (params, output) -> {
                // Supernatural / undead
                output.accept(ModItems.BOGGED_BONE_STALKER_SPAWN_EGG);
                output.accept(ModItems.BOGGED_SHADOW_SPAWN_EGG);
                output.accept(ModItems.BONE_STALKER_SPAWN_EGG);
                output.accept(ModItems.GEIST_SPAWN_EGG);
                output.accept(ModItems.GHOST_SPAWN_EGG);
                output.accept(ModItems.HAUNT_SPAWN_EGG);
                output.accept(ModItems.SHADOW_SPAWN_EGG);
                output.accept(ModItems.SKINWALKER_SPAWN_EGG);
                output.accept(ModItems.SPECTRE_SPAWN_EGG);
                // Cryptids & monsters
                output.accept(ModItems.ARID_YETI_SPAWN_EGG);
                output.accept(ModItems.CHUPACABRA_SPAWN_EGG);
                output.accept(ModItems.HOWLER_SPAWN_EGG);
                output.accept(ModItems.MYCELIUM_MAMMOTH_SPAWN_EGG);
                output.accept(ModItems.SASQUATCH_SPAWN_EGG);
                output.accept(ModItems.SQUONK_SPAWN_EGG);
                output.accept(ModItems.VILE_GATOR_SPAWN_EGG);
                output.accept(ModItems.WECHUGE_SPAWN_EGG);
                output.accept(ModItems.WENDIGO_SPAWN_EGG);
                output.accept(ModItems.YETI_SPAWN_EGG);
                // Elemental / magical
                output.accept(ModItems.BABY_PHOENIX_SPAWN_EGG);
                output.accept(ModItems.BABY_WIND_PHOENIX_SPAWN_EGG);
                output.accept(ModItems.CHERRY_TREE_ENT_SPAWN_EGG);
                output.accept(ModItems.GILDED_TREE_ENT_SPAWN_EGG);
                output.accept(ModItems.PHOENIX_SPAWN_EGG);
                output.accept(ModItems.TREE_ENT_SPAWN_EGG);
                output.accept(ModItems.WIND_PHOENIX_SPAWN_EGG);
                output.accept(ModItems.GOLDEN_HERMIT_KING_SPAWN_EGG);
                output.accept(ModItems.HERMIT_KING_SPAWN_EGG);
                // Mob Vote
                output.accept(ModItems.GLARE_SPAWN_EGG);
                output.accept(ModItems.ICEOLOGER_SPAWN_EGG);
                output.accept(ModItems.ILLUSIONER_SPAWN_EGG);
                output.accept(ModItems.MAULER_SPAWN_EGG);
                output.accept(ModItems.RASCAL_SPAWN_EGG);
                output.accept(ModItems.TUFF_GOLEM_SPAWN_EGG);
                output.accept(ModItems.WILDFIRE_SPAWN_EGG);
            }),

    DYES("dyes",
            ModItems.CUSTOM_DYES.get("goldenrod"),
            (params, output) -> {
                output.accept(Items.WHITE_DYE);
                output.accept(Items.LIGHT_GRAY_DYE);
                output.accept(Items.GRAY_DYE);
                output.accept(Items.BLACK_DYE);
                output.accept(Items.BROWN_DYE);
                output.accept(Items.RED_DYE);
                output.accept(Items.ORANGE_DYE);
                output.accept(Items.YELLOW_DYE);
                output.accept(Items.LIME_DYE);
                output.accept(Items.GREEN_DYE);
                output.accept(Items.CYAN_DYE);
                output.accept(Items.LIGHT_BLUE_DYE);
                output.accept(Items.BLUE_DYE);
                output.accept(Items.PURPLE_DYE);
                output.accept(Items.MAGENTA_DYE);
                output.accept(Items.PINK_DYE);
                ModItems.CUSTOM_DYES.values().forEach(output::accept);
            }),

    COLORS("colors",
            () -> ModBlocks.COLOR_SETS.get("amethyst").wool().get().asItem(),
            (params, output) -> {
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
                    output.accept(set.plate());
                    output.accept(set.edge());
                    output.accept(set.beam());
                    output.accept(set.pergola());
                    output.accept(set.geometricWindow());
                });
                ModBlocks.SEAGLASS_SETS.values().forEach(set -> {
                    output.accept(set.seaglass());
                    output.accept(set.bubblesSeaglass());
                    output.accept(set.smoothSeaglass());
                    output.accept(set.wavesSeaglass());
                });
                ModBlocks.PATTERN_BLOCKS.values().forEach(colorMap -> colorMap.values().forEach(output::accept));
                ModBlocks.PATTERN_PLATES.values().forEach(colorMap -> colorMap.values().forEach(output::accept));
                ModBlocks.PATTERN_EDGES.values().forEach(colorMap -> colorMap.values().forEach(output::accept));
                ModBlocks.PATTERN_BEAMS.values().forEach(colorMap -> colorMap.values().forEach(output::accept));
                ModBlocks.PATTERN_PERGOLAS.values().forEach(colorMap -> colorMap.values().forEach(output::accept));
                ModBlocks.PATTERN_WINDOWS.values().forEach(colorMap -> colorMap.values().forEach(output::accept));
                ModBlocks.ELEVATORS.values().forEach(output::accept);
                ModPatterns.ALL_COLORS.forEach(color -> output.accept(ModItems.CLAY_TILES.get(color.name()).get()));
                ModBlocks.FUTONS.values().forEach(output::accept);
            }),

    WOOD_SETS("wood_sets",
            () -> ModBlocks.WOOD_SETS.get("starlight").log().get().asItem(),
            (params, output) -> {
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
                ModBlocks.WOOD_SETS.values().forEach(set -> {
                    output.accept(set.beam());
                    output.accept(set.pergola());
                    output.accept(set.planksPlate());
                    output.accept(set.planksEdge());
                    output.accept(set.bannister());
                    output.accept(set.supportSlab());
                    output.accept(set.supportBeam());
                    output.accept(set.geometricWindow());
                });
            }),

    BACKPORT("backport",
            () -> ModBlocks.PALE_OAK_LOG.get().asItem(),
            (params, output) -> {
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
                output.accept(ModBlocks.PALE_MOSS_BLOCK);
                output.accept(ModBlocks.PALE_MOSS_CARPET);
                output.accept(ModBlocks.PALE_HANGING_MOSS);
                output.accept(ModBlocks.CREAKING_HEART);
                output.accept(ModBlocks.OPEN_EYEBLOSSOM);
                output.accept(ModBlocks.CLOSED_EYEBLOSSOM);

                output.accept(ModBlocks.RESIN_BLOCK);
                output.accept(ModBlocks.RESIN_BRICKS);
                output.accept(ModBlocks.RESIN_BRICK_STAIRS);
                output.accept(ModBlocks.RESIN_BRICK_SLAB);
                output.accept(ModBlocks.RESIN_BRICK_WALL);
                output.accept(ModBlocks.CHISELED_RESIN_BRICKS);
                output.accept(ModBlocks.RESIN_CLUMP);

                ModBlocks.SHELVES.forEach(output::accept);

                output.accept(ModBlocks.BUSH);
                output.accept(ModBlocks.FIREFLY_BUSH);
                output.accept(ModBlocks.WILDFLOWERS);
                output.accept(ModBlocks.LEAF_LITTER);
                output.accept(ModBlocks.CACTUS_FLOWER);
                output.accept(ModBlocks.SHORT_DRY_GRASS);
                output.accept(ModBlocks.TALL_DRY_GRASS);

                output.accept(ModBlocks.DRIED_GHAST);

                output.accept(ModItems.WOODEN_SPEAR);
                output.accept(ModItems.STONE_SPEAR);
                output.accept(ModItems.IRON_SPEAR);
                output.accept(ModItems.GOLDEN_SPEAR);
                output.accept(ModItems.DIAMOND_SPEAR);
                output.accept(ModItems.NETHERITE_SPEAR);
                output.accept(ModItems.COPPER_SPEAR);

                output.accept(ModItems.NETHERITE_HORSE_ARMOR);

                output.accept(ModItems.NAUTILUS_SPAWN_EGG);
                output.accept(ModItems.ZOMBIE_NAUTILUS_SPAWN_EGG);
                output.accept(ModItems.CAMEL_HUSK_SPAWN_EGG);

                output.accept(ModItems.COPPER_NAUTILUS_ARMOR);
                output.accept(ModItems.IRON_NAUTILUS_ARMOR);
                output.accept(ModItems.GOLDEN_NAUTILUS_ARMOR);
                output.accept(ModItems.DIAMOND_NAUTILUS_ARMOR);
                output.accept(ModItems.NETHERITE_NAUTILUS_ARMOR);
            }),

    COPPER_CHAOS("copper_chaos",
            () -> ModBlocks.COPPER_CHEST.get().asItem(),
            (params, output) -> {
                // Vanilla copper base blocks
                output.accept(Items.COPPER_BLOCK);
                output.accept(Items.EXPOSED_COPPER);
                output.accept(Items.WEATHERED_COPPER);
                output.accept(Items.OXIDIZED_COPPER);
                output.accept(Items.WAXED_COPPER_BLOCK);
                output.accept(Items.WAXED_EXPOSED_COPPER);
                output.accept(Items.WAXED_WEATHERED_COPPER);
                output.accept(Items.WAXED_OXIDIZED_COPPER);
                output.accept(Items.CUT_COPPER);
                output.accept(Items.EXPOSED_CUT_COPPER);
                output.accept(Items.WEATHERED_CUT_COPPER);
                output.accept(Items.OXIDIZED_CUT_COPPER);
                output.accept(Items.WAXED_CUT_COPPER);
                output.accept(Items.WAXED_EXPOSED_CUT_COPPER);
                output.accept(Items.WAXED_WEATHERED_CUT_COPPER);
                output.accept(Items.WAXED_OXIDIZED_CUT_COPPER);
                output.accept(Items.CUT_COPPER_STAIRS);
                output.accept(Items.EXPOSED_CUT_COPPER_STAIRS);
                output.accept(Items.WEATHERED_CUT_COPPER_STAIRS);
                output.accept(Items.OXIDIZED_CUT_COPPER_STAIRS);
                output.accept(Items.WAXED_CUT_COPPER_STAIRS);
                output.accept(Items.WAXED_EXPOSED_CUT_COPPER_STAIRS);
                output.accept(Items.WAXED_WEATHERED_CUT_COPPER_STAIRS);
                output.accept(Items.WAXED_OXIDIZED_CUT_COPPER_STAIRS);
                output.accept(Items.CUT_COPPER_SLAB);
                output.accept(Items.EXPOSED_CUT_COPPER_SLAB);
                output.accept(Items.WEATHERED_CUT_COPPER_SLAB);
                output.accept(Items.OXIDIZED_CUT_COPPER_SLAB);
                output.accept(Items.WAXED_CUT_COPPER_SLAB);
                output.accept(Items.WAXED_EXPOSED_CUT_COPPER_SLAB);
                output.accept(Items.WAXED_WEATHERED_CUT_COPPER_SLAB);
                output.accept(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB);
                output.accept(Items.CHISELED_COPPER);
                output.accept(Items.EXPOSED_CHISELED_COPPER);
                output.accept(Items.WEATHERED_CHISELED_COPPER);
                output.accept(Items.OXIDIZED_CHISELED_COPPER);
                output.accept(Items.WAXED_CHISELED_COPPER);
                output.accept(Items.WAXED_EXPOSED_CHISELED_COPPER);
                output.accept(Items.WAXED_WEATHERED_CHISELED_COPPER);
                output.accept(Items.WAXED_OXIDIZED_CHISELED_COPPER);
                output.accept(Items.COPPER_GRATE);
                output.accept(Items.EXPOSED_COPPER_GRATE);
                output.accept(Items.WEATHERED_COPPER_GRATE);
                output.accept(Items.OXIDIZED_COPPER_GRATE);
                output.accept(Items.WAXED_COPPER_GRATE);
                output.accept(Items.WAXED_EXPOSED_COPPER_GRATE);
                output.accept(Items.WAXED_WEATHERED_COPPER_GRATE);
                output.accept(Items.WAXED_OXIDIZED_COPPER_GRATE);
                output.accept(Items.COPPER_BULB);
                output.accept(Items.EXPOSED_COPPER_BULB);
                output.accept(Items.WEATHERED_COPPER_BULB);
                output.accept(Items.OXIDIZED_COPPER_BULB);
                output.accept(Items.WAXED_COPPER_BULB);
                output.accept(Items.WAXED_EXPOSED_COPPER_BULB);
                output.accept(Items.WAXED_WEATHERED_COPPER_BULB);
                output.accept(Items.WAXED_OXIDIZED_COPPER_BULB);
                // Chests
                output.accept(ModBlocks.COPPER_CHEST);
                output.accept(ModBlocks.EXPOSED_COPPER_CHEST);
                output.accept(ModBlocks.WEATHERED_COPPER_CHEST);
                output.accept(ModBlocks.OXIDIZED_COPPER_CHEST);
                output.accept(ModBlocks.WAXED_COPPER_CHEST);
                output.accept(ModBlocks.WAXED_EXPOSED_COPPER_CHEST);
                output.accept(ModBlocks.WAXED_WEATHERED_COPPER_CHEST);
                output.accept(ModBlocks.WAXED_OXIDIZED_COPPER_CHEST);
                // Doors & Trapdoors
                ModBlocks.COPPER_DOORS.values().forEach(s -> output.accept(s.get().asItem()));
                ModBlocks.COPPER_TRAPDOORS.values().forEach(s -> output.accept(s.get().asItem()));
                // Buttons & Pressure Plates
                ModBlocks.COPPER_BUTTONS.values().forEach(s -> output.accept(s.get().asItem()));
                ModBlocks.COPPER_PRESSURE_PLATES.values().forEach(s -> output.accept(s.get().asItem()));
                // Torch
                output.accept(ModBlocks.COPPER_TORCH);
                // Lanterns & Soul Lanterns
                ModBlocks.COPPER_LANTERNS.values().forEach(s -> output.accept(s.get().asItem()));
                ModBlocks.COPPER_SOUL_LANTERNS.values().forEach(s -> output.accept(s.get().asItem()));
                // Chains, Bars, Ladders
                ModBlocks.COPPER_CHAINS.values().forEach(s -> output.accept(s.get().asItem()));
                ModBlocks.COPPER_BARS.values().forEach(s -> output.accept(s.get().asItem()));
                ModBlocks.COPPER_LADDERS.values().forEach(s -> output.accept(s.get().asItem()));
                // Hoppers
                ModBlocks.COPPER_HOPPERS.values().forEach(s -> output.accept(s.get().asItem()));
                // Cauldrons
                ModBlocks.COPPER_CAULDRONS.values().forEach(s -> output.accept(s.get().asItem()));
                // Rails
                ModBlocks.COPPER_RAILS.values().forEach(s -> output.accept(s.get().asItem()));
                // Anvils
                ModBlocks.COPPER_ANVILS.values().forEach(s -> output.accept(s.get().asItem()));
                // Lightning Rods
                ModBlocks.LIGHTNING_RODS.values().forEach(s -> output.accept(s.get().asItem()));
                // Golem Statues
                ModBlocks.COPPER_GOLEM_STATUES.values().forEach(s -> output.accept(s.get().asItem()));
            }),

    GRADIENTS("gradients",
            () -> ModBlocks.getAllGradientBlocks().iterator().next().get().asItem(),
            (params, output) -> {
                ModBlocks.getAllGradientBlocks().forEach(output::accept);
            }),

    VANPLUS("vanplus",
            () -> ModBlocks.OAK_BANNISTER.get().asItem(),
            (params, output) -> {
                ModBlocks.VANILLA_WALLS.values().forEach(output::accept);

                ModBlocks.VANILLA_STRUCTURAL_SETS.values().forEach(set -> {
                    output.accept(set.beam());
                    output.accept(set.pergola());
                    output.accept(set.planksPlate());
                    output.accept(set.planksEdge());
                    output.accept(set.bannister());
                    output.accept(set.supportSlab());
                    output.accept(set.supportBeam());
                    output.accept(set.geometricWindow());
                });

                output.accept(ModBlocks.SANDSTONE_PLATE);
                output.accept(ModBlocks.SANDSTONE_EDGE);

                output.accept(ModBlocks.CUT_SANDSTONE_PLATE);
                output.accept(ModBlocks.CUT_SANDSTONE_EDGE);

                output.accept(ModBlocks.SMOOTH_SANDSTONE_PLATE);
                output.accept(ModBlocks.SMOOTH_SANDSTONE_EDGE);
     }),

    MOSAIC("mosaic",
            () -> ModBlocks.WATER_MOSAIC_TRADITIONAL.get().asItem(),
            (params, output) -> {
                output.accept(ModBlocks.WATER_MOSAIC_BORDER);
                output.accept(ModBlocks.WATER_MOSAIC_GEOMETRIC);
                output.accept(ModBlocks.WATER_MOSAIC_PATTERN);
                output.accept(ModBlocks.WATER_MOSAIC_DELICATE);
                output.accept(ModBlocks.EARTH_MOSAIC_BORDER);
                output.accept(ModBlocks.EARTH_MOSAIC_GEOMETRIC);
                output.accept(ModBlocks.EARTH_MOSAIC_PATTERN);
                output.accept(ModBlocks.EARTH_MOSAIC_DELICATE);
                output.accept(ModBlocks.FIRE_MOSAIC_BORDER);
                output.accept(ModBlocks.FIRE_MOSAIC_GEOMETRIC);
                output.accept(ModBlocks.FIRE_MOSAIC_PATTERN);
                output.accept(ModBlocks.FIRE_MOSAIC_DELICATE);
                output.accept(ModBlocks.SPIRIT_MOSAIC_BORDER);
                output.accept(ModBlocks.SPIRIT_MOSAIC_GEOMETRIC);
                output.accept(ModBlocks.SPIRIT_MOSAIC_PATTERN);
                output.accept(ModBlocks.SPIRIT_MOSAIC_DELICATE);
                output.accept(ModBlocks.AIR_MOSAIC_BORDER);
                output.accept(ModBlocks.AIR_MOSAIC_GEOMETRIC);
                output.accept(ModBlocks.AIR_MOSAIC_PATTERN);
                output.accept(ModBlocks.AIR_MOSAIC_DELICATE);

                output.accept(ModBlocks.WATER_MOSAIC_TRADITIONAL);
                output.accept(ModBlocks.EARTH_MOSAIC_TRADITIONAL);
                output.accept(ModBlocks.FIRE_MOSAIC_TRADITIONAL);
                output.accept(ModBlocks.SPIRIT_MOSAIC_TRADITIONAL);
                output.accept(ModBlocks.AIR_MOSAIC_TRADITIONAL);

                output.accept(ModBlocks.MOSAIC_FLOOR);
                output.accept(ModBlocks.MOSAIC_FLOOR_DELICATE);
                output.accept(ModBlocks.MOSAIC_FLOOR_ROSETTE);
                output.accept(ModBlocks.ROMAN_FRESCO_RED);
                output.accept(ModBlocks.ROMAN_FRESCO_BLACK);

                output.accept(ModBlocks.LIMESTONE_MASONRY);
                output.accept(ModBlocks.LIMESTONE_MASONRY_EDGE);
                output.accept(ModBlocks.LIMESTONE_MASONRY_PLATE);

                output.accept(ModBlocks.STONE_BRICKS_MASONRY);
                output.accept(ModBlocks.STONE_BRICKS_MASONRY_EDGE);
                output.accept(ModBlocks.STONE_BRICKS_MASONRY_PLATE);

                output.accept(ModBlocks.ORNAMENTED_RED_WOOL);
                output.accept(ModBlocks.ORNAMENTED_RED_CARPET);
                output.accept(ModBlocks.DELICATE_RED_WOOL);
                output.accept(ModBlocks.DELICATE_RED_CARPET);
                output.accept(ModBlocks.ORNAMENTED_BLUE_WOOL);
                output.accept(ModBlocks.ORNAMENTED_BLUE_CARPET);
                output.accept(ModBlocks.DELICATE_BLUE_WOOL);
                output.accept(ModBlocks.DELICATE_BLUE_CARPET);
                output.accept(ModBlocks.ORNAMENTED_GREEN_WOOL);
                output.accept(ModBlocks.ORNAMENTED_GREEN_CARPET);
                output.accept(ModBlocks.DELICATE_GREEN_WOOL);
                output.accept(ModBlocks.DELICATE_GREEN_CARPET);
                output.accept(ModBlocks.ORNAMENTED_PURPLE_WOOL);
                output.accept(ModBlocks.ORNAMENTED_PURPLE_CARPET);
                output.accept(ModBlocks.DELICATE_PURPLE_WOOL);
                output.accept(ModBlocks.DELICATE_PURPLE_CARPET);
            }),

    BLOCKS("blocks",
            () -> ModBlocks.MIXED_LIMESTONE_BRICKS.get().asItem(),
            (params, output) -> {
                output.accept(ModBlocks.PLAIN_LIMESTONE);
                output.accept(ModBlocks.COBBLED_LIMESTONE);
                output.accept(ModBlocks.MIXED_LIMESTONE_BRICKS);
                ModBlocks.SEAGLASS.forEach(output::accept);  // ethereal seaglass only
                ModBlocks.TESTBLOCK.forEach(output::accept);

                output.accept(ModBlocks.SMOOTH_GLOWSTONE);

                output.accept(ModBlocks.SALT_BLOCK);
                output.accept(ModBlocks.POLISHED_SALT_BLOCK);

                output.accept(ModBlocks.WHEAT_THATCH);
                output.accept(ModBlocks.WHEAT_THATCH_EDGE);
                output.accept(ModBlocks.WHEAT_THATCH_PLATE);

                output.accept(ModBlocks.BAMBOO_THATCH);
                output.accept(ModBlocks.BAMBOO_THATCH_EDGE);
                output.accept(ModBlocks.BAMBOO_THATCH_PLATE);

                output.accept(ModBlocks.ROOFING_SLATES);
                output.accept(ModBlocks.ROOFING_SLATES_EDGE);
                output.accept(ModBlocks.ROOFING_SLATES_PLATE);

                output.accept(ModBlocks.BLACK_MARBLE);
                output.accept(ModBlocks.BLACK_MARBLE_BRICKS);
                output.accept(ModBlocks.BLACK_MARBLE_SMALL_BRICKS);
                output.accept(ModBlocks.BLACK_MARBLE_TILES);
                output.accept(ModBlocks.BLACK_POLISHED_MARBLE);
                output.accept(ModBlocks.BLACK_MARBLE_PILLAR);
                output.accept(ModBlocks.BLACK_MARBLE_PILLAR_CAP);
                output.accept(ModBlocks.WHITE_MARBLE);
                output.accept(ModBlocks.WHITE_MARBLE_BRICKS);
                output.accept(ModBlocks.WHITE_MARBLE_SMALL_BRICKS);
                output.accept(ModBlocks.WHITE_MARBLE_TILES);
                output.accept(ModBlocks.WHITE_POLISHED_MARBLE);
                output.accept(ModBlocks.WHITE_MARBLE_PILLAR);
                output.accept(ModBlocks.WHITE_MARBLE_PILLAR_CAP);
                output.accept(ModBlocks.BLACK_MARBLE_FLOOR_TILE);
                output.accept(ModBlocks.WHITE_MARBLE_FLOOR_TILE);

                output.accept(ModBlocks.SLENDER_SANDSTONE_BRICKS);
                output.accept(ModBlocks.SLENDER_SANDSTONE_BRICKS_WALL);
                output.accept(ModBlocks.SLENDER_SANDSTONE_BRICKS_EDGE);
                output.accept(ModBlocks.SLENDER_SANDSTONE_BRICKS_PLATE);

                output.accept(ModBlocks.SLENDER_TURQUOISE_PATTERN);
                output.accept(ModBlocks.SLENDER_TURQUOISE_PATTERN_WALL);
                output.accept(ModBlocks.SLENDER_TURQUOISE_PATTERN_EDGE);
                output.accept(ModBlocks.SLENDER_TURQUOISE_PATTERN_PLATE);

                output.accept(ModBlocks.GOLD_PLATED_SMOOTH_BLOCK);
                output.accept(ModBlocks.GOLD_PLATED_SMOOTH_EDGE);
                output.accept(ModBlocks.GOLD_PLATED_SMOOTH_PLATE);

                output.accept(ModBlocks.CHISELED_PLASTERED_STONE_PILLAR);
            }),

    // ── Engraved / Connecting Blocks ──────────────────────────────────────────
    ENGRAVED("engraved",
            () -> ModBlocks.ENGRAVED_STONE.get().asItem(),
            (params, output) -> {
                // Stone variants
                output.accept(ModBlocks.ANGRY_STONE);
                output.accept(ModBlocks.BLANK_STONE_CARVING);
                output.accept(ModBlocks.BORDERED_STONE);
                output.accept(ModBlocks.BRICK_BORDERED_STONE);
                output.accept(ModBlocks.CARVED_STONE);
                output.accept(ModBlocks.CHECKERED_STONE_TILES);
                output.accept(ModBlocks.COBBLED_STONE);
                output.accept(ModBlocks.CRACKED_DISORDERED_STONE_BRICKS);
                output.accept(ModBlocks.CRACKED_FLAT_STONE_TILES);
                output.accept(ModBlocks.CREEPER_STONE_CARVING);
                output.accept(ModBlocks.CRYING_STONE);
                output.accept(ModBlocks.CURLY_STONE_PILLAR);
                output.accept(ModBlocks.CUT_BLANK_STONE);
                output.accept(ModBlocks.CUT_STONE_COLUMN);
                output.accept(ModBlocks.DUH_STONE);
                output.accept(ModBlocks.EDGED_STONE_BRICKS);
                output.accept(ModBlocks.ENGRAVED_STONE);
                output.accept(ModBlocks.ETCHED_STONE_BRICKS);
                output.accept(ModBlocks.FINE_STONE_PILLAR);
                output.accept(ModBlocks.FLAT_STONE_TILES);
                output.accept(ModBlocks.GLAD_STONE);
                output.accept(ModBlocks.INLAYED_STONE);
                output.accept(ModBlocks.INSCRIBED_STONE);
                output.accept(ModBlocks.LAYED_STONE_BRICKS);
                output.accept(ModBlocks.LODED_STONE);
                output.accept(ModBlocks.MASSIVE_STONE_BRICKS);
                output.accept(ModBlocks.OFFSET_STONE_BRICKS);
                output.accept(ModBlocks.ORNATE_STONE_PILLAR);
                output.accept(ModBlocks.OVERLAPPING_STONE_TILES);
                output.accept(ModBlocks.PILLAR_STONE_BRICKS);
                output.accept(ModBlocks.POLISHED_STONE);
                output.accept(ModBlocks.PRISMAL_STONE_REMNANTS);
                output.accept(ModBlocks.ROUGH_STONE);
                output.accept(ModBlocks.ROUNDED_STONE_BRICKS);
                output.accept(ModBlocks.RUNIC_CARVED_STONE);
                output.accept(ModBlocks.SAD_STONE);
                output.accept(ModBlocks.SANDED_STONE);
                output.accept(ModBlocks.SIMPLE_STONE_PILLAR);
                output.accept(ModBlocks.SMALL_STONE_BRICKS);
                output.accept(ModBlocks.SMOOTH_INLAYED_STONE);
                output.accept(ModBlocks.SMOOTH_STONE_COLUMN);
                output.accept(ModBlocks.SMOOTHED_DOUBLE_INLAYED_STONE);
                output.accept(ModBlocks.SPIDER_STONE_CARVING);
                output.accept(ModBlocks.SPIRALED_STONE);
                output.accept(ModBlocks.STACKED_STONE_BRICKS);
                output.accept(ModBlocks.STONE_MINI_TILES);
                output.accept(ModBlocks.STONE_PILLAR);
                output.accept(ModBlocks.STONE_SCALES);
                output.accept(ModBlocks.THICK_INLAYED_STONE);
                output.accept(ModBlocks.TILED_BORDERED_STONE);
                output.accept(ModBlocks.TILED_STONE);
                output.accept(ModBlocks.TILED_STONE_COLUMN);
                output.accept(ModBlocks.TINY_BRICK_BORDERED_STONE);
                output.accept(ModBlocks.TINY_LAYERED_STONE_BRICKS);
                output.accept(ModBlocks.TINY_LAYERED_STONE_SLABS);
                output.accept(ModBlocks.TINY_STONE_BRICKS);
                output.accept(ModBlocks.TRODDEN_STONE);
                output.accept(ModBlocks.UNAMUSED_STONE);
                output.accept(ModBlocks.VERTICAL_CUT_STONE);
                output.accept(ModBlocks.VERTICAL_DISORDERED_STONE_BRICKS);
                output.accept(ModBlocks.WEATHERED_STONE);

                // CTM blocks
                output.accept(ModBlocks.PURPUR_PILLAR_CTM);
                output.accept(ModBlocks.SANDSTONE_CTM);
                output.accept(ModBlocks.RED_SANDSTONE_CTM);
                output.accept(ModBlocks.POLISHED_ANDESITE_CTM);
                output.accept(ModBlocks.POLISHED_BLACKSTONE_CTM);
                output.accept(ModBlocks.POLISHED_DIORITE_CTM);
                output.accept(ModBlocks.POLISHED_GRANITE_CTM);
                output.accept(ModBlocks.NETHERITE_BLOCK_CTM);
                output.accept(ModBlocks.SMOOTH_STONE_CTM);
            }),

    JARS("jars",
            () -> ModBlocks.FIREFLY_JAR.get().asItem(),
            (params, output) -> {
                output.accept(ModBlocks.CHRYSALIS);
                output.accept(ModItems.CATERPILLAR.get());
                output.accept(ModBlocks.GLASS_JAR);
                output.accept(ModBlocks.CATERPILLAR_JAR);
                output.accept(ModBlocks.FIREFLY_IN_A_JAR);
                output.accept(ModBlocks.FIREFLIES_IN_A_JAR);
                output.accept(ModBlocks.FIREFLY_JAR);
                output.accept(ModItems.BUG_NET);
                ModBlocks.BUTTERFLY_JARS.values().forEach(output::accept);
                for (Butterfly.Variant variant : Butterfly.Variant.values()) {
                    output.accept(ModItems.BUTTERFLIES.get(variant).get());
                }
            }),

    FLORA("flora",
            ModItems.BIG_LILY_PAD,
            (params, output) -> {
                output.accept(ModBlocks.THORNY_HEDGE);
                output.accept(ModItems.THORNY_HEDGE_SPROUTS);

                ModBlocks.PARTICLE_HEDGES.values().forEach(output::accept);
                ModBlocks.CREEPING_HEDGES.values().forEach(output::accept);

                output.accept(ModItems.BIG_LILY_PAD);

                output.accept(ModBlocks.COCONUT);

                output.accept(ModItems.COCONUT);
            }),

    FAUNA("fauna",
            ModItems.CLAM,
            (params, output) -> {
                output.accept(ModItems.CLAM);
                output.accept(ModItems.KOI_FISH);
                output.accept(ModItems.PEARL);
                output.accept(ModItems.SILK);
                output.accept(ModItems.SNAIL_SHELL);
                output.accept(ModBlocks.GLOW_GOOP);

                output.accept(ModBlocks.DRAGON_SKULL);
                output.accept(ModBlocks.SILK_COCOON);
                output.accept(ModItems.OAK_NEST);
                output.accept(ModItems.ACACIA_BEEHIVE);
                output.accept(ModItems.BAMBOO_BEEHIVE);
                output.accept(ModItems.BIRCH_BEEHIVE);
                output.accept(ModItems.CHERRY_BEEHIVE);
                output.accept(ModItems.CRIMSON_BEEHIVE);
                output.accept(ModItems.DARK_OAK_BEEHIVE);
                output.accept(ModItems.JUNGLE_BEEHIVE);
                output.accept(ModItems.MANGROVE_BEEHIVE);
                output.accept(ModItems.SPRUCE_BEEHIVE);
                output.accept(ModItems.WARPED_BEEHIVE);

                output.accept(ModItems.ALLIGATOR_EGG);
                output.accept(ModItems.CRAB_CLAW);
                output.accept(ModItems.CRAB_EGG);
                output.accept(ModItems.SNAIL_EGG);
                output.accept(ModItems.TORTOISE_EGG);
            }),

    FOOD("food",
            ModItems.COOKED_WILD_GAME_MEAT,
            (params, output) -> {
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
                output.accept(ModItems.BLUE_EGG);
                output.accept(ModItems.BROWN_EGG);
                output.accept(ModItems.DUCK_EGG);
                output.accept(ModItems.EMU_EGG);
                output.accept(ModItems.HOOPOE_EGG);
                output.accept(ModItems.KIWI_EGG);
                output.accept(ModItems.PENGUIN_EGG);
                output.accept(ModItems.PHEASANT_EGG);
                output.accept(ModItems.TOUCAN_EGG);
            }),

    MISC("misc",
            () -> ModBlocks.WATER_LANTERN.get().asItem(),
            (params, output) -> {
                output.accept(ModItems.SALT);
                output.accept(ModBlocks.SALT_LAMP);

                output.accept(ModBlocks.STONE_LANTERN);
                output.accept(ModBlocks.IRON_FANCY_LANTERN);
                output.accept(ModBlocks.STARLIGHT_LAMP);

                output.accept(ModBlocks.WATER_LANTERN);
                output.accept(ModBlocks.LAVA_LANTERN);
                output.accept(ModBlocks.PROTECTIVE_LANTERN);
                output.accept(ModBlocks.SMITE_LANTERN);

                output.accept(ModBlocks.WEATHERING_STATION);
                output.accept(ModBlocks.WOODCUTTER);

                output.accept(ModItems.UNFIRED_CLAY_ROOF_TILE.get());
                output.accept(ModItems.PLASTER_BUCKET.get());

                output.accept(ModBlocks.CURVED_RAKED_GRAVEL);
                output.accept(ModBlocks.STRAIGHT_RAKED_GRAVEL);

                output.accept(ModBlocks.LIMESTONE_BANNISTER);
                output.accept(ModBlocks.WHITE_MARBLE_FANCY_FENCE);
                output.accept(ModBlocks.BLACK_MARBLE_FANCY_FENCE);
                output.accept(ModBlocks.SANDSTONE_CRENELATION);

                output.accept(ModBlocks.WATER_MOSAIC_RECESS);
                output.accept(ModBlocks.EARTH_MOSAIC_RECESS);
                output.accept(ModBlocks.FIRE_MOSAIC_RECESS);
                output.accept(ModBlocks.SPIRIT_MOSAIC_RECESS);
                output.accept(ModBlocks.AIR_MOSAIC_RECESS);

                output.accept(ModBlocks.STONE_BRICKS_ARROWSLIT);
                output.accept(ModBlocks.STONE_BRICKS_MACHICOLATION);
                output.accept(ModBlocks.STONE_BRICKS_FAUCET);
                output.accept(ModBlocks.STONE_BRICKS_POOL);
                output.accept(ModBlocks.STONE_BRICKS_SMALL_POOL);
                output.accept(ModBlocks.STONE_BRICKS_WATER_JET);
                output.accept(ModBlocks.WATER_SOURCE_TRICKLE);

                output.accept(ModItems.WILDFIRE_CROWN);
                output.accept(ModItems.WILDFIRE_CROWN_FRAGMENT);
            });

    // --- Display order (top to bottom in the button list) ---
    public static final java.util.List<OttCreativeCategories> DISPLAY_ORDER =
            java.util.List.of(MISC, VANPLUS, WOOD_SETS, DYES, COLORS, GRADIENTS, BLOCKS, ENGRAVED, MOSAIC, BACKPORT, COPPER_CHAOS, FLORA, FAUNA, FOOD, JARS, AQUATIC, WILDLIFE, MYTHICAL
            );

    // --- State ---
    @Nullable
    private static OttCreativeCategories selected = MISC;

    public static @Nullable OttCreativeCategories getSelected() {
        return selected;
    }

    public static void setSelected(@Nullable OttCreativeCategories cat) {
        selected = cat;
    }

    // --- Enum fields ---
    private final String id;
    private final Supplier<Item> iconItem;
    private final BiConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output> populator;

    OttCreativeCategories(@NotNull String id,
                          @NotNull Supplier<Item> iconItem,
                          @NotNull BiConsumer<CreativeModeTab.ItemDisplayParameters, CreativeModeTab.Output> populator) {
        this.id = id;
        this.iconItem = iconItem;
        this.populator = populator;
    }

    public @NotNull Component getDisplayName() {
        return Component.translatable("ott.creative_category." + id);
    }

    public @NotNull Item getIconItem() {
        return iconItem.get();
    }

    /**
     * Populate items into output. Accepts null params — none of the current populators use them.
     */
    public void populateItems(@Nullable CreativeModeTab.ItemDisplayParameters params,
                              @NotNull CreativeModeTab.Output output) {
        populator.accept(params, output);
    }
}