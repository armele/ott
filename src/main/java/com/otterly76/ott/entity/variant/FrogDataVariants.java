package com.otterly76.ott.entity.variant;

import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class FrogDataVariants {
    private static ResourceKey<FrogDataVariant> register(String key, TagKey<Biome> biome) {
        return register(key, SpawnPrioritySelectors.single(new RawBiomeCheck(biome), 1));
    }

    private static ResourceKey<FrogDataVariant> register(String key, SpawnPrioritySelectors selectors) {
        ResourceLocation texture = ResourceLocation.withDefaultNamespace("entity/frog/" + key + "_frog");
        return OttBuiltInRegistries.FROG_VARIANTS.resource(key, new FrogDataVariant(new ClientAsset(texture), selectors));
    }

    public static void bootstrap() {}
}
