package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
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
                .icon(() -> new ItemStack(ModItems.CAPYBARA_SPAWN_EGG.get()))
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
                }).build();
    }

    private static CreativeModeTab createMiscTab() {
        return CreativeModeTab.builder()
                .title(Component.translatable(createTranslationKey("misc")))
                .withTabsBefore(OTT_EGGS.getKey())
                .icon(() -> new ItemStack(ModItems.OTTER_SPAWN_EGG.get()))
                .displayItems((config, output) -> {
                    output.accept(ModItems.CLAM);
                    output.accept(ModItems.DRAGONFLY_WING);
                    output.accept(ModItems.KOI_FISH);
                    output.accept(ModItems.PEARL);
                    output.accept(ModItems.SILK);
                    output.accept(ModItems.SEA_BUNNY_SLIME_BOTTLE);
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
                    output.accept(ModItems.SILK_COCOON.get());
                    output.accept(ModItems.SEA_BUNNY_SLIME_BLOCK.get());
                    output.accept(ModItems.CHRYSALIS.get());
                    output.accept(ModItems.SNAIL_SHELL);
                    output.accept(ModItems.QUILL);
                    output.accept(ModItems.QUILL_ARROW);
                    output.accept(ModItems.ANTLER);
                    output.accept(ModItems.FUR);

                    // Protective Lanterns
                    output.accept(ModBlocks.PROTECTIVE_LANTERN);

                    // Fluid Lanterns
                    output.accept(ModBlocks.WATER_LANTERN);
                    output.accept(ModBlocks.LAVA_LANTERN);

                    // Damage Lantern
                    output.accept(ModBlocks.SMITE_LANTERN);

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
                    output.accept(ModItems.LIZARD_TAIL);
                    output.accept(ModItems.COOKED_LIZARD_TAIL);
                    output.accept(ModItems.COOKED_EGG);
                    output.accept(ModItems.RAW_CAPYBARA);
                    output.accept(ModItems.COOKED_CAPYBARA);
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
                }).build();
    }

    private static String createTranslationKey(String tabName) {
        return ITEM_GROUP_PREFIX + tabName;
    }
}