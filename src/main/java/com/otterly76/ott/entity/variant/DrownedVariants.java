package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DrownedVariants {
    public static final ResourceKey<DrownedVariant> DROWNED = register("drowned", DrownedVariant.ModelType.NORMAL, "drowned", 10, SpawnPrioritySelectors.fallback(0));

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<DrownedVariant> register(String key, DrownedVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/zombie/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.DROWNED_VARIANTS.resource(key, new DrownedVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}