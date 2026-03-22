package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.KoiFishEntity;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import software.bernie.geckolib.model.GeoModel;

@OnlyIn(Dist.CLIENT)
public class KoiFishModel extends GeoModel<KoiFishEntity> {
    @Override
    public ResourceLocation getModelResource(KoiFishEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/koi_fish/koi_fish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(KoiFishEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/koi_fish/koi_fish_" + entity.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(KoiFishEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/koi_fish/koi_fish.animation.json");
    }
}