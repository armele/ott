package com.otterly76.ott.entity.variant;

import com.otterly76.ott.registry.OttRegistryKeys;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class CatDataVariants {
    private static ResourceKey<CatDataVariant> register(String key) {
        return register(key, SpawnPrioritySelectors.fallback(0));
    }

    private static ResourceKey<CatDataVariant> register(String key, SpawnPrioritySelectors selectors) {
        return ResourceKey.create(OttRegistryKeys.CAT_VARIANT, ResourceLocation.withDefaultNamespace(key));
    }

    public static void bootstrap() {}
}
