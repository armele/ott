package com.otterly76.ott.worldgen.generation;

import com.otterly76.ott.util.ModTags;
import com.otterly76.ott.worldgen.biome.ModBiomes;
import com.otterly76.ott.worldgen.feature.SpringToLifePlacements;
import com.otterly76.ott.worldgen.modifier.BiomeContext;
import com.otterly76.ott.worldgen.modifier.BiomeWriter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class SpringToLifeFeatureManager extends FeatureManager {
    public SpringToLifeFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
    }

    @Override
    public void bootstrap() {
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_CAMELS).add(() -> writer.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CAMEL, 1, 1, 1)));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_BUSHES).add(() -> this.addVegetation(SpringToLifePlacements.PATCH_BUSH));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_FIREFLY_BUSHES).add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_NEAR_WATER));
        this.getOrCreateBiomeBuilder(context.is(ModTags.Biomes.SPAWNS_FIREFLY_BUSHES_SWAMP) && !context.is(ModTags.Biomes.SPAWNS_FIREFLY_BUSHES)).add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_SWAMP)).add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_NOISE_BASED_WILDFLOWERS).add(() -> this.addVegetation(SpringToLifePlacements.WILDFLOWERS_MEADOW));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_WILDFLOWERS).add(() -> this.addVegetation(SpringToLifePlacements.WILDFLOWERS_BIRCH_FOREST));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_DRY_GRASS).add(() -> this.addVegetation(SpringToLifePlacements.PATCH_DRY_GRASS_DESERT));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_DRY_GRASS_RARELY).add(() -> this.addVegetation(SpringToLifePlacements.PATCH_DRY_GRASS_BADLANDS));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_FALLEN_OAK_TREES).add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_OAK_TREE));
        this.getOrCreateBiomeBuilder((ctx) -> ctx.is(ModTags.Biomes.SPAWNS_FALLEN_BIRCH_TREES_RARELY) && !ctx.is(ModBiomes.PALE_GARDEN)).add(() -> this.addVegetation(SpringToLifePlacements.PLACED_RARE_FALLEN_BIRCH_TREE));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_FALLEN_BIRCH_TREES).add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_BIRCH_TREE));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_FALLEN_SUPER_BIRCH_TREES).add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_SUPER_BIRCH_TREE));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_FALLEN_JUNGLE_TREES).add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_JUNGLE_TREE));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_FALLEN_SPRUCE_TREES).add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_SPRUCE_TREE));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_FALLEN_SPRUCE_TREES_RARELY).add(() -> this.addVegetation(SpringToLifePlacements.PLACED_RARE_FALLEN_SPRUCE_TREE));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_LEAF_LITTER_PATCHES).add(() -> this.addVegetation(SpringToLifePlacements.PATCH_LEAF_LITTER));
        this.getOrCreateBiomeBuilder(ModTags.Biomes.SPAWNS_LEAF_LITTER).add(() -> this.addVegetation(SpringToLifePlacements.LEAF_LITTER));
        // Cactus flowers logic
    }
}
