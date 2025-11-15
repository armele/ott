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
            ModItems.TESTBLOCK_00, ModItems.TESTBLOCK_01, ModItems.TESTBLOCK_02, ModItems.TESTBLOCK_03,
            ModItems.TESTBLOCK_10, ModItems.TESTBLOCK_11, ModItems.TESTBLOCK_12, ModItems.TESTBLOCK_13,
            ModItems.TESTBLOCK_20, ModItems.TESTBLOCK_21, ModItems.TESTBLOCK_22, ModItems.TESTBLOCK_23,
            ModItems.TESTBLOCK_30, ModItems.TESTBLOCK_31, ModItems.TESTBLOCK_32, ModItems.TESTBLOCK_33
    );

    private static CreativeModeTab createGradientsTab() {
        return new CreativeModeTab.Builder(DEFAULT_ROW, DEFAULT_COLUMN)
                .title(Component.translatable(createTranslationKey("gradients")))
                .icon(() -> new ItemStack(ALL_GRADIENT_BLOCKS.getFirst()))
                .displayItems((params, output) -> ModBlocks.getAllGradientBlocks().forEach(output::accept))
                .build();
    }

    private static CreativeModeTab createMiscTab() {
        return new CreativeModeTab.Builder(DEFAULT_ROW, DEFAULT_COLUMN)
                .title(Component.translatable(createTranslationKey("misc")))
                .icon(() -> new ItemStack(ModItems.OTTER.get()))
                .displayItems((config, output) -> {
                    output.accept(ModItems.OTTER);
                    addTestBlocks(output);
                })
                .build();
    }

    private static String createTranslationKey(String tabName) {
        return ITEM_GROUP_PREFIX + tabName;
    }

    private static void addTestBlocks(CreativeModeTab.Output output) {
        TEST_BLOCKS.forEach(holder -> output.accept(holder.get()));
    }
}