package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SeaUrchinEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class SeaUrchinModel extends GeoModel<SeaUrchinEntity> {
    @Override
    public ResourceLocation getModelResource(SeaUrchinEntity animatable, GeoRenderer<SeaUrchinEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/sea_urchin/sea_urchin.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SeaUrchinEntity animatable, GeoRenderer<SeaUrchinEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/sea_urchin/sea_urchin.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SeaUrchinEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/sea_urchin/sea_urchin.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(SeaUrchinEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(SeaUrchinEntity animatable) {
        return getTextureResource(animatable, null);
    }
}