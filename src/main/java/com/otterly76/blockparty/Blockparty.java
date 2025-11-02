package com.otterly76.blockparty;

import com.otterly76.blockparty.block.ModBlocks;
import com.otterly76.blockparty.generation.GradientBlockProvider;
import com.otterly76.blockparty.item.ModCreativeModeTab;
import com.otterly76.blockparty.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

@Mod(Constants.MOD_ID)
public class Blockparty {

    public Blockparty(IEventBus modEventBus) {
        ModCreativeModeTab.register(modEventBus);

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(this::dataGeneratorSetup);
    }

    private void dataGeneratorSetup(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new GradientBlockProvider(generator.getPackOutput(), event.getExistingFileHelper()));
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == ModCreativeModeTab.NON_BLOCKS_TAB) {
            event.accept(ModItems.OTTER);
        }

        if (event.getTabKey() == ModCreativeModeTab.GRADIENT_BLOCKS_TAB) {
            ModBlocks.getAllGradientBlocks().forEach(event::accept);
        }
    }
}