package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.EtherealShrimpEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class EtherealShrimpModel extends GeoModel<EtherealShrimpEntity> {
    @Override
    public ResourceLocation getModelResource(EtherealShrimpEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/shrimp/shrimp_1.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EtherealShrimpEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/shrimp/shrimp_1.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EtherealShrimpEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/shrimp/shrimp_1.animation.json");
    }
}
