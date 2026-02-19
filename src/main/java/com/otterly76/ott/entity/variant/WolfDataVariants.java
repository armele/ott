package com.otterly76.ott.entity.variant;

import com.otterly76.ott.entity.variant.check.RawBiomeCheck;
import com.otterly76.ott.registry.OttBuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class WolfDataVariants {
    private static ResourceKey<WolfDataVariant> register(String key, String assetId, TagKey<Biome> tag) {
        return register(key, assetId, SpawnPrioritySelectors.single(new RawBiomeCheck(tag), 1));
    }

    private static ResourceKey<WolfDataVariant> register(String key, String assetId, SpawnPrioritySelectors selectors) {
        ResourceLocation wild = ResourceLocation.withDefaultNamespace("entity/wolf/" + assetId);
        ResourceLocation tame = ResourceLocation.withDefaultNamespace("entity/wolf/" + assetId + "_tame");
        ResourceLocation angry = ResourceLocation.withDefaultNamespace("entity/wolf/" + assetId + "_angry");
        return OttBuiltInRegistries.WOLF_VARIANTS.resource(key, new WolfDataVariant(new WolfDataVariant.AssetInfo(new ClientAsset(wild), new ClientAsset(tame), new ClientAsset(angry)), selectors));
    }

    public static void bootstrap() {}
}
