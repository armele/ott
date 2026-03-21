package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.util.ModTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class PigVariants {
    public static final ResourceKey<PigVariant> TEMPERATE = register("temperate", PigVariant.ModelType.NORMAL, "pig", 9, SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<PigVariant> WARM = register("warm", PigVariant.ModelType.NORMAL, "warm_pig", ModTags.Biomes.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<PigVariant> COLD = register("cold", PigVariant.ModelType.COLD, "cold_pig", ModTags.Biomes.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static ResourceKey<PigVariant> register(String key, PigVariant.ModelType type, String assetId, TagKey<Biome> biome) {
        return register(key, type, assetId, 1, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<PigVariant> register(String key, PigVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/pig/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.PIG_VARIANTS.resource(key, new PigVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}
