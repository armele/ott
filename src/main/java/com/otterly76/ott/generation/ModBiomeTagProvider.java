package com.otterly76.ott.generation;

import com.otterly76.ott.util.ModTags;
import com.otterly76.ott.worldgen.biome.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends BiomeTagsProvider {

    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookuprovider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookuprovider, "ott", existingFileHelper);
    }

    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ModTags.Biomes.SPAWNS_WARM_VARIANT_FARM_ANIMALS).add(Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA, Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.ERODED_BADLANDS);
        this.tag(ModTags.Biomes.SPAWNS_COLD_VARIANT_FARM_ANIMALS).add(Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_SLOPES, Biomes.SNOWY_BEACH, Biomes.FROZEN_RIVER, Biomes.ICE_SPIKES, Biomes.GROVE);
        this.tag(ModTags.Biomes.SPAWNS_CAMELS).add(Biomes.DESERT);
        this.tag(ModTags.Biomes.SPAWNS_BUSHES).add(Biomes.PLAINS, Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.DARK_FOREST, Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE);
        this.tag(ModTags.Biomes.SPAWNS_FIREFLY_BUSHES).add(Biomes.DARK_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        this.tag(ModTags.Biomes.SPAWNS_FIREFLY_BUSHES_SWAMP).add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
        this.tag(ModTags.Biomes.SPAWNS_WILDFLOWERS).add(Biomes.PLAINS, Biomes.FLOWER_FOREST, Biomes.MEADOW, Biomes.CHERRY_GROVE, Biomes.FOREST);
        this.tag(ModTags.Biomes.SPAWNS_NOISE_BASED_WILDFLOWERS).add(Biomes.DARK_FOREST, Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
        this.tag(ModTags.Biomes.SPAWNS_DRY_GRASS).add(Biomes.DESERT, Biomes.SAVANNA, Biomes.SAVANNA_PLATEAU, Biomes.WINDSWEPT_SAVANNA);
        this.tag(ModTags.Biomes.SPAWNS_DRY_GRASS_RARELY).add(Biomes.BADLANDS, Biomes.WOODED_BADLANDS, Biomes.ERODED_BADLANDS);
        this.tag(ModTags.Biomes.SPAWNS_FALLEN_OAK_TREES).add(Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.DARK_FOREST);
        this.tag(ModTags.Biomes.SPAWNS_FALLEN_BIRCH_TREES).add(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST);
        this.tag(ModTags.Biomes.SPAWNS_FALLEN_BIRCH_TREES_RARELY).add(Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.MEADOW);
        this.tag(ModTags.Biomes.SPAWNS_FALLEN_SUPER_BIRCH_TREES).add(Biomes.OLD_GROWTH_BIRCH_FOREST);
        this.tag(ModTags.Biomes.SPAWNS_FALLEN_JUNGLE_TREES).add(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE);
        this.tag(ModTags.Biomes.SPAWNS_FALLEN_SPRUCE_TREES).add(Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.SNOWY_TAIGA);
        this.tag(ModTags.Biomes.SPAWNS_FALLEN_SPRUCE_TREES_RARELY).add(Biomes.SNOWY_PLAINS, Biomes.GROVE, Biomes.SNOWY_SLOPES);
        this.tag(ModTags.Biomes.SPAWNS_LEAF_LITTER).add(Biomes.DARK_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA);
        this.tag(ModTags.Biomes.SPAWNS_LEAF_LITTER_PATCHES).add(Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE);
        this.tag(ModTags.Biomes.HAS_DARK_LEAF_LITTER).add(Biomes.DARK_FOREST, Biomes.SWAMP, Biomes.MANGROVE_SWAMP);
        this.tag(ModTags.Biomes.HAS_PALE_LEAF_LITTER).addOptional(ModBiomes.PALE_GARDEN.location());
        this.tag(ModTags.Biomes.SPAWNS_OAK_NESTED_TREES).add(Biomes.FOREST, Biomes.FLOWER_FOREST);
        this.tag(ModTags.Biomes.IS_DESERT).add(Biomes.DESERT);
        this.tag(ModTags.Biomes.IS_SNOWY).add(Biomes.SNOWY_PLAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_SLOPES, Biomes.SNOWY_BEACH, Biomes.FROZEN_RIVER, Biomes.ICE_SPIKES, Biomes.GROVE);

        this.tag(ModTags.Biomes.IS_HUMID).add(Biomes.SWAMP, Biomes.MANGROVE_SWAMP, Biomes.JUNGLE, Biomes.BAMBOO_JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.LUSH_CAVES);
        this.tag(ModTags.Biomes.IS_HUMID).addOptional(ModBiomes.VERDANT_FOREST.location());

        TagKey<Biome> neoforgeIsHumid = TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("neoforge", "is_humid"));
        this.tag(neoforgeIsHumid).addTag(ModTags.Biomes.IS_HUMID);

        this.addVanillaTags();
    }

    protected void addVanillaTags() {
        TagKey<Biome> spawnsNormalFrogs = TagKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace("spawns_white_variant_frogs"));
        this.tag(spawnsNormalFrogs).addOptional(ModBiomes.VERDANT_FOREST.location());
        this.tag(BiomeTags.IS_FOREST).addOptional(ModBiomes.VERDANT_FOREST.location());
        this.tag(BiomeTags.IS_OVERWORLD).addOptional(ModBiomes.VERDANT_FOREST.location());

        this.tag(ModTags.Biomes.IS_BIRCH_FOREST).add(Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST);

        this.tag(ModTags.Biomes.IS_MEADOW).add(Biomes.MEADOW);
    }
}
