package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class MooshroomVariants {
    public static final ResourceKey<MooshroomVariant> RED = register("red", MooshroomVariant.ModelType.RED, "red_mooshroom", 15, SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<MooshroomVariant> BROWN = register("brown", MooshroomVariant.ModelType.BROWN, "brown_mooshroom", 33, SpawnPrioritySelectors.fallback(0));

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<MooshroomVariant> register(String key, MooshroomVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/mooshroom/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.MOOSHROOM_VARIANTS.resource(key, new MooshroomVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}
