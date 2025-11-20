package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static com.otterly76.ott.block.ModBlocks.ALL_GRADIENT_BLOCKS;


public final class ModCreativeTabs {
    private static final CreativeModeTab.Row DEFAULT_ROW = CreativeModeTab.Row.TOP;
    private static final int DEFAULT_COLUMN = 1;
    private static final String ITEM_GROUP_PREFIX = "itemGroup." + Constants.MOD_ID + ".";

    public static final DeferredRegister<CreativeModeTab> OTTER_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GRADIENTS = OTTER_TABS.register("gradients", ModCreativeTabs::createGradientsTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC = OTTER_TABS.register("misc", ModCreativeTabs::createMiscTab);

    private static final List<DeferredHolder<Item, BlockItem>> TEST_BLOCKS = List.of(
            ModItems.TESTBLOCK_00, ModItems.TESTBLOCK_01, ModItems.TESTBLOCK_02, ModItems.TESTBLOCK_03, ModItems.TESTBLOCK_10,
            ModItems.TESTBLOCK_11, ModItems.TESTBLOCK_12, ModItems.TESTBLOCK_13, ModItems.TESTBLOCK_20, ModItems.TESTBLOCK_21,
            ModItems.TESTBLOCK_22, ModItems.TESTBLOCK_23, ModItems.TESTBLOCK_30, ModItems.TESTBLOCK_31, ModItems.TESTBLOCK_32,
            ModItems.TESTBLOCK_33);

    private static final List<DeferredHolder<Item, BlockItem>> LIMESTONE_BLOCKS = List.of(
            ModItems.LIMESTONE_00, ModItems.LIMESTONE_01, ModItems.LIMESTONE_02, ModItems.LIMESTONE_03,
            ModItems.LIMESTONE_10, ModItems.LIMESTONE_11, ModItems.LIMESTONE_12, ModItems.LIMESTONE_13,
            ModItems.LIMESTONE_20, ModItems.LIMESTONE_21, ModItems.LIMESTONE_22, ModItems.LIMESTONE_23,
            ModItems.LIMESTONE_30, ModItems.LIMESTONE_31, ModItems.LIMESTONE_32, ModItems.LIMESTONE_33);

    private static final List<DeferredHolder<Item, BlockItem>> SEAGLASS_BLOCKS = List.of(
            ModItems.BLACK_BUBBLES_SEAGLASS, ModItems.BLUE_BUBBLES_SEAGLASS, ModItems.BROWN_BUBBLES_SEAGLASS, ModItems.CYAN_BUBBLES_SEAGLASS,
            ModItems.ETHEREAL1_BUBBLES_SEAGLASS, ModItems.ETHEREAL2_BUBBLES_SEAGLASS, ModItems.ETHEREAL3_BUBBLES_SEAGLASS, ModItems.ETHEREAL4_BUBBLES_SEAGLASS,
            ModItems.GRAY_BUBBLES_SEAGLASS, ModItems.GREEN_BUBBLES_SEAGLASS, ModItems.LIGHT_BLUE_BUBBLES_SEAGLASS, ModItems.LIGHT_GRAY_BUBBLES_SEAGLASS,
            ModItems.LIME_BUBBLES_SEAGLASS, ModItems.MAGENTA_BUBBLES_SEAGLASS, ModItems.ORANGE_BUBBLES_SEAGLASS, ModItems.PINK_BUBBLES_SEAGLASS,
            ModItems.PURPLE_BUBBLES_SEAGLASS, ModItems.RED_BUBBLES_SEAGLASS, ModItems.WHITE_BUBBLES_SEAGLASS, ModItems.YELLOW_BUBBLES_SEAGLASS,

            ModItems.BLACK_WAVES_SEAGLASS, ModItems.BLUE_WAVES_SEAGLASS, ModItems.BROWN_WAVES_SEAGLASS, ModItems.CYAN_WAVES_SEAGLASS,
            ModItems.ETHEREAL1_WAVES_SEAGLASS, ModItems.ETHEREAL2_WAVES_SEAGLASS, ModItems.ETHEREAL3_WAVES_SEAGLASS, ModItems.ETHEREAL4_WAVES_SEAGLASS,
            ModItems.GRAY_WAVES_SEAGLASS, ModItems.GREEN_WAVES_SEAGLASS, ModItems.LIGHT_BLUE_WAVES_SEAGLASS, ModItems.LIGHT_GRAY_WAVES_SEAGLASS,
            ModItems.LIME_WAVES_SEAGLASS, ModItems.MAGENTA_WAVES_SEAGLASS, ModItems.ORANGE_WAVES_SEAGLASS, ModItems.PINK_WAVES_SEAGLASS,
            ModItems.PURPLE_WAVES_SEAGLASS, ModItems.RED_WAVES_SEAGLASS, ModItems.WHITE_WAVES_SEAGLASS, ModItems.YELLOW_WAVES_SEAGLASS,

            ModItems.BLACK_SMOOTH_SEAGLASS, ModItems.BLUE_SMOOTH_SEAGLASS, ModItems.BROWN_SMOOTH_SEAGLASS, ModItems.CYAN_SMOOTH_SEAGLASS,
            ModItems.ETHEREAL1_SMOOTH_SEAGLASS, ModItems.ETHEREAL2_SMOOTH_SEAGLASS, ModItems.ETHEREAL3_SMOOTH_SEAGLASS, ModItems.ETHEREAL4_SMOOTH_SEAGLASS,
            ModItems.GRAY_SMOOTH_SEAGLASS, ModItems.GREEN_SMOOTH_SEAGLASS, ModItems.LIGHT_BLUE_SMOOTH_SEAGLASS, ModItems.LIGHT_GRAY_SMOOTH_SEAGLASS,
            ModItems.LIME_SMOOTH_SEAGLASS, ModItems.MAGENTA_SMOOTH_SEAGLASS, ModItems.ORANGE_SMOOTH_SEAGLASS, ModItems.PINK_SMOOTH_SEAGLASS,
            ModItems.PURPLE_SMOOTH_SEAGLASS, ModItems.RED_SMOOTH_SEAGLASS, ModItems.WHITE_SMOOTH_SEAGLASS, ModItems.YELLOW_SMOOTH_SEAGLASS,

            ModItems.BLACK_SEAGLASS, ModItems.BLUE_SEAGLASS, ModItems.BROWN_SEAGLASS, ModItems.CYAN_SEAGLASS,
            ModItems.ETHEREAL1_SEAGLASS, ModItems.ETHEREAL2_SEAGLASS, ModItems.ETHEREAL3_SEAGLASS, ModItems.ETHEREAL4_SEAGLASS,
            ModItems.GRAY_SEAGLASS, ModItems.GREEN_SEAGLASS, ModItems.LIGHT_BLUE_SEAGLASS, ModItems.LIGHT_GRAY_SEAGLASS,
            ModItems.LIME_SEAGLASS, ModItems.MAGENTA_SEAGLASS, ModItems.ORANGE_SEAGLASS, ModItems.PINK_SEAGLASS,
            ModItems.PURPLE_SEAGLASS, ModItems.RED_SEAGLASS, ModItems.WHITE_SEAGLASS, ModItems.YELLOW_SEAGLASS);

    private static CreativeModeTab createGradientsTab() {
        return new CreativeModeTab.Builder(DEFAULT_ROW, DEFAULT_COLUMN).title(Component.translatable(createTranslationKey("gradients"))).icon(() -> new ItemStack(ALL_GRADIENT_BLOCKS.getFirst())).displayItems((params, output) -> ModBlocks.getAllGradientBlocks().forEach(output::accept)).build();
    }

    private static CreativeModeTab createMiscTab() {
        return new CreativeModeTab.Builder(DEFAULT_ROW, DEFAULT_COLUMN).title(Component.translatable(createTranslationKey("misc"))).icon(() -> new ItemStack(ModItems.OTTER.get())).displayItems((config, output) -> {
            output.accept(ModItems.OTTER);
            addTestBlocks(output);
            addLimestoneBlocks(output);
            addSeaGlassBlocks(output);
            output.accept(ModBlocks.GAPPER_PANEL_OAK);
        }).build();
    }

    private static String createTranslationKey(String tabName) {
        return ITEM_GROUP_PREFIX + tabName;
    }

    private static void addTestBlocks(CreativeModeTab.Output output) {
        TEST_BLOCKS.forEach(holder -> output.accept(holder.get()));
    }

    private static void addLimestoneBlocks(CreativeModeTab.Output output) {
        LIMESTONE_BLOCKS.forEach(holder -> output.accept(holder.get()));
    }

    private static void addSeaGlassBlocks(CreativeModeTab.Output output) {
        SEAGLASS_BLOCKS.forEach(holder -> output.accept(holder.get()));
    }
}