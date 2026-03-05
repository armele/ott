package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class SkeletonVariants {
    public static final ResourceKey<SkeletonVariant> SKELETON = register("skeleton", SkeletonVariant.ModelType.NORMAL, "skeleton", 10, SpawnPrioritySelectors.fallback(0));

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<SkeletonVariant> register(String key, SkeletonVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/skeleton/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.SKELETON_VARIANTS.resource(key, new SkeletonVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}