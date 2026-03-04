package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ChickenVariants {
    public static final ResourceKey<ChickenVariant> TEMPERATE = register("temperate", ChickenVariant.ModelType.NORMAL, "chicken", 17, SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<ChickenVariant> WARM = register("warm", ChickenVariant.ModelType.WARM, "warm_chicken", ModTags.Biomes.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<ChickenVariant> COLD = register("cold", ChickenVariant.ModelType.COLD, "cold_chicken", ModTags.Biomes.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<ChickenVariant> MUSHROOM = register("mushroom", ChickenVariant.ModelType.NORMAL, "mushroom_chicken", TagKey.create(Registries.BIOME, ResourceLocation.withDefaultNamespace("is_mushroom_island")));

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        return register(key, type, assetId, 1, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<ChickenVariant> register(String key, ChickenVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/chicken/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.CHICKEN_VARIANTS.resource(key, new ChickenVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}