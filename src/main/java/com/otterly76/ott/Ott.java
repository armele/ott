package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.generation.GradientBlockProvider;
import com.otterly76.ott.item.ModItems;
import net.minecraft.data.DataGenerator;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@Mod(Constants.MOD_ID)
public class Ott {

    public Ott(IEventBus modEventBus) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.OTTER_TABS.register(modEventBus);

        modEventBus.addListener(this::dataGeneratorSetup);
    }

    private void dataGeneratorSetup(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new GradientBlockProvider(generator.getPackOutput(), event.getExistingFileHelper()));
    }
}