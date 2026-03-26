package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SquonkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SquonkModel extends GeoModel<SquonkEntity> {
    @Override
    public ResourceLocation getModelResource(SquonkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/squonk/squonk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SquonkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/squonk/squonk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SquonkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/squonk/squonk.animation.json");
    }
}