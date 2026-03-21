package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ZombieVariants {
    public static final ResourceKey<ZombieVariant> ZOMBIE = register("zombie", ZombieVariant.ModelType.NORMAL, "zombie", 19, SpawnPrioritySelectors.fallback(0));

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<ZombieVariant> register(String key, ZombieVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/zombie/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.ZOMBIE_VARIANTS.resource(key, new ZombieVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}
