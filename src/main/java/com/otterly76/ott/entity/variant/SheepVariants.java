package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import com.otterly76.ott.util.ModTags;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class SheepVariants {
    public static final ResourceKey<SheepVariant> TEMPERATE = register("temperate", "sheep", 20, SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<SheepVariant> COLD = register("cold", "cold_sheep_1", ModTags.Biomes.SPAWNS_COLD_VARIANT_FARM_ANIMALS);
    public static final ResourceKey<SheepVariant> WARM = register("warm", "warm_sheep_1", ModTags.Biomes.SPAWNS_WARM_VARIANT_FARM_ANIMALS);

    private static ResourceKey<SheepVariant> register(String key, String assetId, TagKey<Biome> biome) {
        return register(key, assetId, 1, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<SheepVariant> register(String key, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/sheep/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.SHEEP_VARIANTS.resource(key, new SheepVariant(new ModelAndTexture<>(SheepVariant.ModelType.NORMAL, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}