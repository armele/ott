package com.otterly76.ott.entity.variant;

import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.util.ModTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ChickenVariants {
    public static final ResourceKey<ChickenVariant> TEMPERATE = register("temperate", ChickenVariant.ModelType.NORMAL, ResourceLocation.withDefaultNamespace("entity/chicken"), SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<ChickenVariant> WARM = register("warm", ChickenVariant.ModelType.NORMAL, "warm_chicken", ModTags.Biomes.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<ChickenVariant> COLD = register("cold", ChickenVariant.ModelType.COLD, "cold_chicken", ModTags.Biomes.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        ResourceLocation path = ResourceLocation.withDefaultNamespace("entity/chicken/" + assetId);
        return register(key, type, path, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, ResourceLocation assetId, SpawnPrioritySelectors selectors) {
        return OttBuiltInRegistries.CHICKEN_VARIANTS.resource(key, new ChickenVariant(new ModelAndTexture<>(type, assetId), selectors));
    }

    public static void bootstrap() {}
}
