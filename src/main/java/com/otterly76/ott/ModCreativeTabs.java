package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.client.gui.BundledTabs;
import com.otterly76.ott.client.registries.ModBundledTabs;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static com.otterly76.ott.block.ModBlocks.ALL_GRADIENT_BLOCKS;


public final class ModCreativeTabs {
    private static final String ITEM_GROUP_PREFIX = "itemGroup." + Constants.MOD_ID + ".";

    public static final DeferredRegister<CreativeModeTab> OTTER_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Constants.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> VANILLA_BACKPORT = OTTER_TABS.register("vanilla_backport", ModCreativeTabs::createVanillaBackportTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GRADIENTS = OTTER_TABS.register("gradients", ModCreativeTabs::createGradientsTab);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> MISC = OTTER_TABS.register("misc", ModCreativeTabs::createMiscTab);


    private static CreativeModeTab createVanillaBackportTab() {
        return CreativeModeTab.builder().title(Component.literal("Vanilla Backport")).icon(() -> new ItemStack(Items.BUNDLE)).displayItems((parameters, output) -> {
            HolderLookup.Provider provider = parameters.holders();
            List<BundledTabs> tabs = ModBundledTabs.getTabs();
            tabs.forEach((tab) -> tab.populate(provider));
            Stream<ItemStack> var10000 = tabs.stream().flatMap((tab) -> tab.getDisplayItems().stream());
            Objects.requireNonNull(output);
            var10000.forEach(output::accept);
        }).build();
    }

    private static CreativeModeTab createGradientsTab() {
        return CreativeModeTab.builder().title(Component.translatable(createTranslationKey("gradients"))).icon(() -> new ItemStack(ALL_GRADIENT_BLOCKS.getFirst())).displayItems((params, output) -> ModBlocks.getAllGradientBlocks().forEach(output::accept)).build();
    }

    private static CreativeModeTab createMiscTab() {
        return CreativeModeTab.builder().title(Component.translatable(createTranslationKey("misc"))).icon(() -> new ItemStack(ModItems.OTTER.get())).displayItems((config, output) -> {
            output.accept(ModItems.OTTER);

            // output.accept(ModBlocks.GAPPER_PANEL_OAK);

            output.accept(ModBlocks.HEDGE);
            output.accept(ModItems.HEDGE_SPROUTS);

            output.accept(ModBlocks.STARLIGHT_SAPLING);
            output.accept(ModBlocks.MIDNIGHT_SAPLING);
            output.accept(ModBlocks.PALE_OAK_SAPLING);

            // Protective Lanterns
            output.accept(ModBlocks.PROTECTIVE_LANTERN);

            // Fluid Lanterns
            output.accept(ModBlocks.WATER_LANTERN);
            output.accept(ModBlocks.LAVA_LANTERN);

            // Damage Lantern
            output.accept(ModBlocks.SMITE_LANTERN);

            ModBlocks.PARTICLE_HEDGES.values().forEach(output::accept);
            ModBlocks.CREEPING_HEDGES.values().forEach(output::accept);

            ModBlocks.TESTBLOCK.forEach(output::accept);
            ModBlocks.LIMESTONE.forEach(output::accept);
            ModBlocks.SEAGLASS.forEach(output::accept);

        }).build();
    }

    private static String createTranslationKey(String tabName) {
        return ITEM_GROUP_PREFIX + tabName;
    }
}