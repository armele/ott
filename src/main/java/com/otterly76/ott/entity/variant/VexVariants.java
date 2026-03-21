package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class VexVariants {
    public static final ResourceKey<VexVariant> VEX = register("vex", VexVariant.ModelType.NORMAL, "illager/vex", 1, SpawnPrioritySelectors.fallback(0));

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<VexVariant> register(String key, VexVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.VEX_VARIANTS.resource(key, new VexVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}
