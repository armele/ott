package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModProcessorListTagProvider extends TagsProvider<StructureProcessorList> {

    public ModProcessorListTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, Registries.PROCESSOR_LIST, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        TagKey<StructureProcessorList> shipwreckPalettes = TagKey.create(Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "shipwreck_palettes"));
        
        String[] lists = {
                "shipwreck_palette/dark_oak_and_jungle",
                "shipwreck_palette/dark_oak_and_spruce",
                "shipwreck_palette/jungle_and_spruce",
                "shipwreck_palette/oak_and_birch",
                "shipwreck_palette/oak_and_spruce",
                "shipwreck_palette/spruce_and_dark_oak",
                "shipwreck_palette/spruce_and_jungle",
                "shipwreck_palette/spruce_and_oak"
        };

        for (String list : lists) {
            this.tag(shipwreckPalettes).add(ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, list)));
        }
    }
}
