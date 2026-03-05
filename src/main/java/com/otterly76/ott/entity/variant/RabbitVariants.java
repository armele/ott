package com.otterly76.ott.entity.variant;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;

public class RabbitVariants {
    public static final ResourceKey<RabbitVariant> BROWN = register("brown", RabbitVariant.ModelType.NORMAL, "brown", 1, SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<RabbitVariant> WHITE = register("white", RabbitVariant.ModelType.NORMAL, "white", 1, SpawnPrioritySelectors.single(new RawBiomeCheck(BiomeTags.SPAWNS_WHITE_RABBITS), 1));
    public static final ResourceKey<RabbitVariant> BLACK = register("black", RabbitVariant.ModelType.NORMAL, "black", 1, SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<RabbitVariant> WHITE_SPLOTCHED = register("white_splotched", RabbitVariant.ModelType.NORMAL, "white_splotched", 1, SpawnPrioritySelectors.fallback(0));
    public static final ResourceKey<RabbitVariant> GOLD = register("gold", RabbitVariant.ModelType.NORMAL, "gold", 1, SpawnPrioritySelectors.single(new RawBiomeCheck(BiomeTags.SPAWNS_GOLD_RABBITS), 1));
    public static final ResourceKey<RabbitVariant> SALT = register("salt", RabbitVariant.ModelType.NORMAL, "salt", 1, SpawnPrioritySelectors.single(new RawBiomeCheck(BiomeTags.SPAWNS_WHITE_RABBITS), 1));
    public static final ResourceKey<RabbitVariant> CAERBANNOG = register("caerbannog", RabbitVariant.ModelType.NORMAL, "caerbannog", 1, SpawnPrioritySelectors.EMPTY);
    public static final ResourceKey<RabbitVariant> TOAST = register("toast", RabbitVariant.ModelType.NORMAL, "toast", 1, SpawnPrioritySelectors.EMPTY);

    @SuppressWarnings("SameParameterValue")
    private static ResourceKey<RabbitVariant> register(String key, RabbitVariant.ModelType type, String assetId, int count, SpawnPrioritySelectors selectors) {
        ResourceLocation id = Ott.resource("entity/rabbit/" + assetId);
        ResourceLocation path = id.withPath((string) -> "textures/" + string + ".png");
        return OttBuiltInRegistries.RABBIT_VARIANTS.resource(key, new RabbitVariant(new ModelAndTexture<>(type, new ClientAsset(id, path, count)), selectors));
    }

    public static void bootstrap() {}
}