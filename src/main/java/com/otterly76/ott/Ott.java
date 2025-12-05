package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.generation.GradientBlockProvider;
import com.otterly76.ott.generation.GradientBlockRecipeProvider;
import com.otterly76.ott.generation.OttLeafBlockStateProvider;
import com.otterly76.ott.generation.OttLootTableProvider;
import com.otterly76.ott.item.ModItems;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackSelectionConfig;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.AddPackFindersEvent;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.nio.file.Files;

import static com.otterly76.ott.Constants.MOD_ID;

@Mod(MOD_ID)
public class Ott {

    public Ott(IEventBus modEventBus, ModContainer modContainer) {

        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModCreativeTabs.OTTER_TABS.register(modEventBus);

        modEventBus.addListener(this::dataGeneratorSetup);

        modEventBus.addListener(this::addPackFinders);
    }

    private void dataGeneratorSetup(final GatherDataEvent event) {
        final DataGenerator generator = event.getGenerator();
        generator.addProvider(event.includeClient(), new GradientBlockProvider(generator.getPackOutput(), event.getExistingFileHelper()));

        generator.addProvider(event.includeServer(), new GradientBlockRecipeProvider(generator.getPackOutput(), event.getLookupProvider()));

        generator.addProvider(event.includeServer(), new LootTableProvider(
                generator.getPackOutput(),
                Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(OttLootTableProvider::new, LootContextParamSets.BLOCK)),
                event.getLookupProvider()));

        // Turning this off as to not overwrite the edited files while I color test
        // generator.addProvider(event.includeClient(), new OttLeafBlockStateProvider(generator.getPackOutput(), event.getExistingFileHelper()));
    }

    private void addPackFinders(AddPackFindersEvent event) {
        if (event.getPackType() == PackType.CLIENT_RESOURCES) {
            var resourcePath = ModList.get().getModFileById(MOD_ID).getFile().findResource("resourcepacks/ott_core");

            // Verify the path exists before trying to create a pack
            if (!Files.isDirectory(resourcePath)) {
                return;
            }

            var packLocationInfo = new PackLocationInfo(
                    MOD_ID + ":ott_core", // Use namespaced ID to avoid collisions
                    Component.translatable("pack." + MOD_ID + ".ott_core"),
                    PackSource.BUILT_IN, // Use imported class
                    Optional.empty()
            );
            var packSelectionConfig = new PackSelectionConfig(
                    true, // fixedPosition: Set to false so users can disable it if needed
                    Pack.Position.TOP,
                    true
            );
            var pack = Pack.readMetaAndCreate(
                    packLocationInfo,
                    new PathPackResources.PathResourcesSupplier(resourcePath),
                    PackType.CLIENT_RESOURCES,
                    packSelectionConfig
            );

            if (pack != null) {
                event.addRepositorySource((packConsumer) -> packConsumer.accept(pack));
            }
        }
    }
}