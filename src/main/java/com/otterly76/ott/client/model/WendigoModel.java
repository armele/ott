package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.WendigoEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WendigoModel extends GeoModel<WendigoEntity> {
    @Override
    public ResourceLocation getModelResource(WendigoEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/wendigo/wendigo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WendigoEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/wendigo/wendigo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WendigoEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/wendigo/wendigo.animation.json");
    }
}