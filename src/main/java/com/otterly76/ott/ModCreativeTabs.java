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
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> OTT_BLOCKS = OTTER_TABS.register("ott_blocks", ModCreativeTabs::createOttBlocksTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DYES = OTTER_TABS.register("dyes", ModCreativeTabs::createDyesTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> OTT_EGGS = OTTER_TABS.register("ott_eggs", ModCreativeTabs::createOttEggsTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC = OTTER_TABS.register("misc", ModCreativeTabs::createMiscTab);


    private static CreativeModeTab createGradientsTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("gradients")))
                .withTabsBefore(CreativeModeTabs.INVENTORY)
                .icon(() -> new ItemStack(ALL_GRADIENT_BLOCKS.getFirst()))
                .displayItems((params, output) -> ModBlocks.getAllGradientBlocks().forEach(output::accept))
                .build();
    }

    private static CreativeModeTab createOttBlocksTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("ott_blocks")))
                .withTabsBefore(GRADIENTS.getKey())
                .icon(() -> new ItemStack(ModBlocks.LIMESTONE.getFirst()))
                .displayItems((config, output) -> {
                    ModBlocks.TESTBLOCK.forEach(output::accept);
                    ModBlocks.LIMESTONE.forEach(output::accept);
                    ModBlocks.SEAGLASS.forEach(output::accept);
                }).build();
    }

    private static CreativeModeTab createDyesTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("dyes")))
                .withTabsBefore(OTT_BLOCKS.getKey())
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
                .icon(() -> new ItemStack(ModItems.OTTER_SPAWN_EGG.get()))
                .displayItems((params, output) -> {
                    output.accept(ModItems.DUCK_SPAWN_EGG);
                    output.accept(ModItems.GOOSE_SPAWN_EGG);
                    output.accept(ModItems.MAN_O_WAR_SPAWN_EGG);
                    output.accept(ModItems.STINGRAY_SPAWN_EGG);
                    output.accept(ModItems.SUNFISH_SPAWN_EGG);
                    output.accept(ModItems.KRILL_SPAWN_EGG);
                    output.accept(ModItems.ANGELFISH_SPAWN_EGG);
                    output.accept(ModItems.BARRELEYE_SPAWN_EGG);
                    output.accept(ModItems.FLOUNDER_SPAWN_EGG);
                    output.accept(ModItems.MARINE_IGUANA_SPAWN_EGG);
                    output.accept(ModItems.GECKO_SPAWN_EGG);
                    output.accept(ModItems.EMU_SPAWN_EGG);
                    output.accept(ModItems.HOOPOE_SPAWN_EGG);
                    output.accept(ModItems.PHEASANT_SPAWN_EGG);
                    output.accept(ModItems.TOUCAN_SPAWN_EGG);
                    output.accept(ModItems.CATFISH_SPAWN_EGG);
                    output.accept(ModItems.BASS_SPAWN_EGG);
                    output.accept(ModItems.BLUEJAY_SPAWN_EGG);
                    output.accept(ModItems.CANARY_SPAWN_EGG);
                    output.accept(ModItems.CARDINAL_SPAWN_EGG);
                    output.accept(ModItems.FINCH_SPAWN_EGG);
                    output.accept(ModItems.ROBIN_SPAWN_EGG);
                    output.accept(ModItems.SPARROW_SPAWN_EGG);
                    output.accept(ModItems.BROWN_BEAR_SPAWN_EGG);
                    output.accept(ModItems.BLACK_BEAR_SPAWN_EGG);
                    output.accept(ModItems.DEER_SPAWN_EGG);
                    output.accept(ModItems.REINDEER_SPAWN_EGG);
                    output.accept(ModItems.WHITE_DEER_SPAWN_EGG);
                    output.accept(ModItems.BUTTERFLY_SPAWN_EGG);
                    output.accept(ModItems.CATERPILLAR_SPAWN_EGG);
                    output.accept(ModItems.FIREFLY_SPAWN_EGG);
                    output.accept(ModItems.SMALL_FIREFLY_SPAWN_EGG);
                    output.accept(ModItems.ALLIGATOR_SPAWN_EGG);
                    output.accept(ModItems.ELEPHANT_SPAWN_EGG);
                    output.accept(ModItems.GIRAFFE_SPAWN_EGG);
                    output.accept(ModItems.HIPPO_SPAWN_EGG);
                    output.accept(ModItems.LION_SPAWN_EGG);
                    output.accept(ModItems.RHINO_SPAWN_EGG);
                    output.accept(ModItems.LIZARD_SPAWN_EGG);
                    output.accept(ModItems.SNAIL_SPAWN_EGG);
                    output.accept(ModItems.TORTOISE_SPAWN_EGG);
                    output.accept(ModItems.VULTURE_SPAWN_EGG);
                    output.accept(ModItems.ZEBRA_SPAWN_EGG);
                    output.accept(ModItems.MOOSE_SPAWN_EGG);
                    output.accept(ModItems.MAMMOTH_SPAWN_EGG);
                    output.accept(ModItems.MYCELIUM_MAMMOTH_SPAWN_EGG);
                    output.accept(ModItems.FENNEC_FOX_SPAWN_EGG);
                    output.accept(ModItems.CAPYBARA_SPAWN_EGG);
                    output.accept(ModItems.HEDGEHOG_SPAWN_EGG);
                    output.accept(ModItems.JELLYFISH_SPAWN_EGG);
                    output.accept(ModItems.SEAHORSE_1_SPAWN_EGG);
                    output.accept(ModItems.SHRIMP_1_SPAWN_EGG);
                    output.accept(ModItems.STARFISH_1_SPAWN_EGG);
                    output.accept(ModItems.JELLYFISH_2_SPAWN_EGG);
                    output.accept(ModItems.JELLYFISH_3_SPAWN_EGG);
                    output.accept(ModItems.KIWI_SPAWN_EGG);
                    output.accept(ModItems.PENGUIN_SPAWN_EGG);
                    output.accept(ModItems.SEAL_SPAWN_EGG);
                    output.accept(ModItems.SEA_URCHIN_SPAWN_EGG);
                    output.accept(ModItems.DRAGONFLY_SPAWN_EGG);
                    output.accept(ModItems.DUMBO_OCTOPUS_SPAWN_EGG);
                    output.accept(ModItems.FERRET_SPAWN_EGG);
                    output.accept(ModItems.JUMPING_SPIDER_SPAWN_EGG);
                    output.accept(ModItems.KOI_FISH_SPAWN_EGG);
                    output.accept(ModItems.OTTER_SPAWN_EGG);
                    output.accept(ModItems.RED_PANDA_SPAWN_EGG);
                    output.accept(ModItems.SEA_BUNNY_SPAWN_EGG);
                    output.accept(ModItems.CICHLID_SPAWN_EGG);
                    output.accept(ModItems.WATER_BUFFALO_SPAWN_EGG);
                    output.accept(ModItems.LEOPARD_CAT_SPAWN_EGG);
                    output.accept(ModItems.ECHIDNA_SPAWN_EGG);
                    output.accept(ModItems.GUITARFISH_SPAWN_EGG);
                    output.accept(ModItems.BONNETHEAD_SHARK_SPAWN_EGG);
                    output.accept(ModItems.BURROWING_OWL_SPAWN_EGG);
                    output.accept(ModItems.BUSHDOG_SPAWN_EGG);
                    output.accept(ModItems.QUAIL_SPAWN_EGG);
                    output.accept(ModItems.CANDYCANE_SNAIL_SPAWN_EGG);
                    output.accept(ModItems.FIRE_SALAMANDER_SPAWN_EGG);
                    output.accept(ModItems.RIVER_TURTLE_SPAWN_EGG);
                    output.accept(ModItems.GOBLIN_SHARK_SPAWN_EGG);
                    output.accept(ModItems.GUINEA_FOWL_SPAWN_EGG);
                    output.accept(ModItems.IMPALA_SPAWN_EGG);
                    output.accept(ModItems.MANTA_RAY_SPAWN_EGG);
                    output.accept(ModItems.STORK_SPAWN_EGG);
                    output.accept(ModItems.MOLE_SPAWN_EGG);
                    output.accept(ModItems.TREE_KANGAROO_SPAWN_EGG);
                    output.accept(ModItems.PALLAS_CAT_SPAWN_EGG);
                    output.accept(ModItems.PINK_LAND_IGUANA_SPAWN_EGG);
                    output.accept(ModItems.PSYCHO_JELLY_SPAWN_EGG);
                    output.accept(ModItems.SPOONBILL_SPAWN_EGG);
                    output.accept(ModItems.GIANT_SOFTSHELL_TURTLE_SPAWN_EGG);
                    output.accept(ModItems.GHOST_SPAWN_EGG);
                    output.accept(ModItems.SPECTRE_SPAWN_EGG);
                    output.accept(ModItems.HAUNT_SPAWN_EGG);
                    output.accept(ModItems.GEIST_SPAWN_EGG);
                    output.accept(ModItems.TREE_ENT_SPAWN_EGG);
                    output.accept(ModItems.HERMIT_KING_SPAWN_EGG);
                    output.accept(ModItems.SAND_HERMIT_SPAWN_EGG);
                    output.accept(ModItems.SEA_VIPER_SPAWN_EGG);
                    output.accept(ModItems.YETI_SPAWN_EGG);
                    output.accept(ModItems.VILE_GATOR_SPAWN_EGG);
                    output.accept(ModItems.PHOENIX_SPAWN_EGG);
                    output.accept(ModItems.BABY_PHOENIX_SPAWN_EGG);
                    output.accept(ModItems.BONE_STALKER_SPAWN_EGG);
                    output.accept(ModItems.SHADOW_SPAWN_EGG);
                    output.accept(ModItems.CHERRY_TREE_ENT_SPAWN_EGG);
                    output.accept(ModItems.GOLDEN_HERMIT_KING_SPAWN_EGG);
                    output.accept(ModItems.CORAL_SEA_VIPER_SPAWN_EGG);
                    output.accept(ModItems.ARID_YETI_SPAWN_EGG);
                    output.accept(ModItems.WIND_PHOENIX_SPAWN_EGG);
                    output.accept(ModItems.BABY_WIND_PHOENIX_SPAWN_EGG);
                    output.accept(ModItems.BOGGED_BONE_STALKER_SPAWN_EGG);
                    output.accept(ModItems.BOGGED_SHADOW_SPAWN_EGG);
                    output.accept(ModItems.GILDED_TREE_ENT_SPAWN_EGG);
                    output.accept(ModItems.BEAVER_SPAWN_EGG);
                    output.accept(ModItems.CHUPACABRA_SPAWN_EGG);
                    output.accept(ModItems.COUGAR_SPAWN_EGG);
                    output.accept(ModItems.COYOTE_SPAWN_EGG);
                    output.accept(ModItems.HOWLER_SPAWN_EGG);
                    output.accept(ModItems.MARMOT_SPAWN_EGG);
                    output.accept(ModItems.MOUSE_SPAWN_EGG);
                    output.accept(ModItems.PIT_VIPER_SPAWN_EGG);
                    output.accept(ModItems.RATTLESNAKE_SPAWN_EGG);
                    output.accept(ModItems.RINGTAIL_SPAWN_EGG);
                    output.accept(ModItems.SASQUATCH_SPAWN_EGG);
                    output.accept(ModItems.SKINWALKER_SPAWN_EGG);
                    output.accept(ModItems.SNAKE_SPAWN_EGG);
                    output.accept(ModItems.SQUONK_SPAWN_EGG);
                    output.accept(ModItems.TURKEY_SPAWN_EGG);
                    output.accept(ModItems.WECHUGE_SPAWN_EGG);
                    output.accept(ModItems.WENDIGO_SPAWN_EGG);
                    output.accept(ModItems.WOLVERINE_SPAWN_EGG);
                    // --- Ecologics ---
                    output.accept(ModItems.COCONUT_CRAB_SPAWN_EGG);
                    output.accept(ModItems.SAND_CRAB_SPAWN_EGG);
                    // --- Friends and Foes ---
                    output.accept(ModItems.FIDDLER_CRAB_SPAWN_EGG);
                    output.accept(ModItems.GLARE_SPAWN_EGG);
                    output.accept(ModItems.ICEOLOGER_SPAWN_EGG);
                    output.accept(ModItems.ILLUSIONER_SPAWN_EGG);
                    output.accept(ModItems.MAULER_SPAWN_EGG);
                    output.accept(ModItems.RASCAL_SPAWN_EGG);
                    output.accept(ModItems.TUFF_GOLEM_SPAWN_EGG);
                    output.accept(ModItems.WILDFIRE_SPAWN_EGG);
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
                    output.accept(ModItems.MAN_O_WAR_BUCKET);
                    output.accept(ModItems.STINGRAY_BUCKET);
                    output.accept(ModItems.SUNFISH_BUCKET);
                    output.accept(ModItems.KRILL_BUCKET);
                    output.accept(ModItems.ANGELFISH_BUCKET);
                    output.accept(ModItems.BARRELEYE_BUCKET);
                    output.accept(ModItems.FLOUNDER_BUCKET);
                    output.accept(ModItems.MARINE_IGUANA_BUCKET);
                    output.accept(ModItems.SNAIL_BUCKET);
                    output.accept(ModItems.CATFISH_BUCKET);
                    output.accept(ModItems.BASS_BUCKET);
                    output.accept(ModItems.DUMBO_OCTOPUS_BUCKET);
                    output.accept(ModItems.KOI_FISH_BUCKET);
                    output.accept(ModItems.SEA_BUNNY_BUCKET);
                    output.accept(ModItems.CICHLID_BUCKET);
                    output.accept(ModItems.GUITARFISH_BUCKET);
                    output.accept(ModItems.BONNETHEAD_SHARK_BUCKET);
                    output.accept(ModItems.GOBLIN_SHARK_BUCKET);
                    output.accept(ModItems.PSYCHO_JELLY_BUCKET);
                    output.accept(ModItems.SILK_COCOON.get());
                    output.accept(ModItems.CHRYSALIS.get());
                    output.accept(ModItems.SNAIL_SHELL);

                    // Nature
                    output.accept(ModItems.THORNY_HEDGE.get());
                    output.accept(ModItems.THORNY_HEDGE_SPROUTS);
                    ModBlocks.PARTICLE_HEDGES.values().forEach(output::accept);
                    ModBlocks.CREEPING_HEDGES.values().forEach(output::accept);
                    output.accept(ModItems.BIG_LILY_PAD);
                    output.accept(ModItems.OAK_NEST);
                    output.accept(ModItems.GLOW_GOOP);

                    // Salt
                    output.accept(ModItems.SALT);
                    output.accept(ModItems.SALT_BLOCK);
                    output.accept(ModItems.POLISHED_SALT_BLOCK);
                    output.accept(ModItems.SALT_LAMP);

                    // Decorative
                    output.accept(ModItems.DRAGON_SKULL);

                    // Protective Lanterns
                    output.accept(ModBlocks.PROTECTIVE_LANTERN);

                    // Fluid Lanterns
                    output.accept(ModBlocks.WATER_LANTERN);
                    output.accept(ModBlocks.LAVA_LANTERN);

                    // Damage Lantern
                    output.accept(ModBlocks.SMITE_LANTERN);

                    output.accept(ModBlocks.GLASS_JAR);
                    output.accept(ModBlocks.FIREFLY_IN_A_JAR);
                    output.accept(ModBlocks.FIREFLIES_IN_A_JAR);
                    output.accept(ModBlocks.FIREFLY_JAR);

                    // Butterfly Jars
                    for (Butterfly.Variant variant : Butterfly.Variant.values()) {
                        output.accept(ModItems.BUTTERFLY_JAR_ITEMS.get(variant).get());
                    }
                    output.accept(ModItems.CATERPILLAR_JAR.get());

                    // Caught Butterflies
                    for (Butterfly.Variant variant : Butterfly.Variant.values()) {
                        output.accept(ModItems.BUTTERFLIES.get(variant).get());
                    }
                    output.accept(ModItems.CATERPILLAR.get());


                    output.accept(ModBlocks.WEATHERING_STATION);

                    // Food and Drops
                    output.accept(ModItems.BASS);
                    output.accept(ModItems.COOKED_BASS);
                    output.accept(ModItems.CATFISH);
                    output.accept(ModItems.COOKED_CATFISH);
                    output.accept(ModItems.VENISON);
                    output.accept(ModItems.COOKED_VENISON);
                    output.accept(ModItems.PHEASANT);
                    output.accept(ModItems.COOKED_PHEASANT);
                    output.accept(ModItems.BUSHMEAT);
                    output.accept(ModItems.COOKED_BUSHMEAT);
                    output.accept(ModItems.RAW_GAME);
                    output.accept(ModItems.COOKED_GAME);
                    output.accept(ModItems.RAW_TURKEY);
                    output.accept(ModItems.LIZARD_TAIL);
                    output.accept(ModItems.COOKED_LIZARD_TAIL);
                    output.accept(ModItems.COOKED_EGG);
                    output.accept(ModItems.RAW_CAPYBARA);
                    output.accept(ModItems.RAW_BUFFALO_MEAT);
                    output.accept(ModItems.COOKED_CAPYBARA);
                    output.accept(ModItems.COOKED_BUFFALO_MEAT);
                    output.accept(ModItems.RAW_HEDGEHOG);
                    output.accept(ModItems.COOKED_HEDGEHOG);
                    output.accept(ModItems.RAW_SEAL);
                    output.accept(ModItems.COOKED_SEAL);
                    output.accept(ModItems.RAW_KIWI);
                    output.accept(ModItems.COOKED_KIWI);
                    output.accept(ModItems.RAW_SHRIMP_1);
                    output.accept(ModItems.COOKED_SHRIMP_1);
                    output.accept(ModItems.RAW_SUNFISH_MEAT);
                    output.accept(ModItems.COOKED_SUNFISH_MEAT);
                    output.accept(ModItems.RAW_GOLDEN_SUNFISH_MEAT);
                    output.accept(ModItems.COOKED_GOLDEN_SUNFISH_MEAT);
                    output.accept(ModItems.RAW_KRILL);
                    output.accept(ModItems.FRIED_KRILL);
                    output.accept(ModItems.JELLYFISH_JELLY);
                    output.accept(ModItems.SEA_URCHIN_CAVIAR);
                    output.accept(ModItems.RAW_CICHLID);
                    output.accept(ModItems.COOKED_CICHLID);
                    output.accept(ModItems.RAW_BONNETHEAD);
                    output.accept(ModItems.COOKED_BONNETHEAD);
                    output.accept(ModItems.RAW_GUITARFISH);
                    output.accept(ModItems.COOKED_GUITARFISH);
                    output.accept(ModItems.RAW_GOBLIN_SHARK);
                    output.accept(ModItems.COOKED_GOBLIN_SHARK);
                    output.accept(ModItems.RAW_SNAIL_MEAT);
                    output.accept(ModItems.COOKED_SNAIL_MEAT);
                    output.accept(ModItems.WILD_BIRD_MEAT);
                    output.accept(ModItems.COOKED_WILD_BIRD_MEAT);

                    // Eggs
                    output.accept(ModItems.BLUE_EGG);
                    output.accept(ModItems.BROWN_EGG);
                    output.accept(ModItems.DUCK_EGG);
                    output.accept(ModItems.PHEASANT_EGG);
                    output.accept(ModItems.ALLIGATOR_EGG);
                    output.accept(ModItems.TORTOISE_EGG);
                    output.accept(ModItems.SNAIL_EGG);
                    output.accept(ModItems.KIWI_EGG);
                    output.accept(ModItems.PENGUIN_EGG);
                    output.accept(ModItems.EMU_EGG);
                    output.accept(ModItems.HOOPOE_EGG);
                    output.accept(ModItems.TOUCAN_EGG);
                    // --- Ecologics ---
                    output.accept(ModItems.COCONUT);
                    output.accept(ModItems.CRAB_MEAT);
                    // --- Friends and Foes ---
                    output.accept(ModItems.CRAB_CLAW);
                    output.accept(ModItems.CRAB_EGG);
                    output.accept(ModItems.WILDFIRE_CROWN);
                    output.accept(ModItems.WILDFIRE_CROWN_FRAGMENT);
                }).build();
    }

    private static String createTranslationKey(String tabName) {
        return ITEM_GROUP_PREFIX + tabName;
    }
}