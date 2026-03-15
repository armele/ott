package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static com.otterly76.ott.block.ModBlocks.ALL_GRADIENT_BLOCKS;


public final class ModCreativeTabs {
    private static final String ITEM_GROUP_PREFIX = "itemGroup." + Constants.MOD_ID + ".";

    public static final DeferredRegister<CreativeModeTab> OTTER_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GRADIENTS = OTTER_TABS.register("gradients", ModCreativeTabs::createGradientsTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> DYES = OTTER_TABS.register("dyes", ModCreativeTabs::createDyesTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC = OTTER_TABS.register("misc", ModCreativeTabs::createMiscTab);


    private static CreativeModeTab createGradientsTab() {
        return CreativeModeTab.builder().title(Component.translatable(createTranslationKey("gradients"))).icon(() -> new ItemStack(ALL_GRADIENT_BLOCKS.getFirst())).displayItems((params, output) -> ModBlocks.getAllGradientBlocks().forEach(output::accept)).build();
    }

    private static CreativeModeTab createDyesTab() {
        return CreativeModeTab.builder().title(Component.translatable(createTranslationKey("dyes"))).icon(() -> new ItemStack(net.minecraft.world.item.Items.CYAN_DYE)).displayItems((params, output) -> {
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

    private static CreativeModeTab createMiscTab() {
        return CreativeModeTab.builder().title(Component.translatable(createTranslationKey("misc"))).icon(() -> new ItemStack(ModItems.OTTER.get())).displayItems((config, output) -> {
            output.accept(ModItems.OTTER);

            // Protective Lanterns
            output.accept(ModBlocks.PROTECTIVE_LANTERN);

            // Fluid Lanterns
            output.accept(ModBlocks.WATER_LANTERN);
            output.accept(ModBlocks.LAVA_LANTERN);

            // Damage Lantern
            output.accept(ModBlocks.SMITE_LANTERN);

            output.accept(ModBlocks.WEATHERING_STATION);

            ModBlocks.TESTBLOCK.forEach(output::accept);
            ModBlocks.LIMESTONE.forEach(output::accept);
            ModBlocks.SEAGLASS.forEach(output::accept);

        }).build();
    }

    private static String createTranslationKey(String tabName) {
        return ITEM_GROUP_PREFIX + tabName;
    }
}