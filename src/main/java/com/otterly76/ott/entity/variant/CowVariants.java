package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.util.ModTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class CowVariants {
    public static final ResourceKey<CowVariant> TEMPERATE = register("temperate", CowVariant.ModelType.NORMAL, "cow", 68, SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<CowVariant> WARM = register("warm", CowVariant.ModelType.WARM, "warm_cow", 2, ModTags.Biomes.SPAWNS_WARM_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<CowVariant> COLD = register("cold", CowVariant.ModelType.COLD, "cold_cow", 3, ModTags.Biomes.SPAWNS_COLD_VARIANT_FARM_ANIMALS);

    private static ResourceKey<CowVariant> register(String key, CowVariant.ModelType type, String assetId, int count, TagKey<Biome> biome) {
        return register(key, type, assetId, count, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<CowVariant> register(String key, CowVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/cow/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.COW_VARIANTS.resource(key, new CowVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}
