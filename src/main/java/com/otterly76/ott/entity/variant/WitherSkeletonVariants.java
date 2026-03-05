package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class WitherSkeletonVariants {
    public static final ResourceKey<WitherSkeletonVariant> WITHER_SKELETON = register("wither_skeleton", WitherSkeletonVariant.ModelType.NORMAL, "wither_skeleton", 1, SpawnPrioritySelectors.fallback(0));

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<WitherSkeletonVariant> register(String key, WitherSkeletonVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/skeleton/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.WITHER_SKELETON_VARIANTS.resource(key, new WitherSkeletonVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}