package com.otterly76.ott.generation;

import com.otterly76.ott.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {

    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookuprovider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookuprovider, "ott", existingFileHelper);
    }

    private static TagKey<Biome> create(String string) {
        return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("minecraft", string));
    }

    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.addVanillaTags();
    }

    protected void addVanillaTags() {
        TagKey<Biome> spawnsNormalFrogs = TagKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace("spawns_white_variant_frogs"));
        this.tag(spawnsNormalFrogs).add(ModBiomes.VERDANT_FOREST);
        this.tag(BiomeTags.IS_FOREST).add(ModBiomes.VERDANT_FOREST);
        this.tag(BiomeTags.IS_OVERWORLD).add(ModBiomes.VERDANT_FOREST);
    }
}