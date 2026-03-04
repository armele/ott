package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class BoggedVariants {
    public static final ResourceKey<BoggedVariant> BOGGED = register("bogged", BoggedVariant.ModelType.NORMAL, "bogged", 1, SpawnPrioritySelectors.fallback(0));

    private static ResourceKey<BoggedVariant> register(String key, BoggedVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/skeleton/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.BOGGED_VARIANTS.resource(key, new BoggedVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}