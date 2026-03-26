package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.ImpalaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ImpalaModel extends GeoModel<ImpalaEntity> {
    @Override
    public ResourceLocation getModelResource(ImpalaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/impala/impala.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ImpalaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/impala/impala.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ImpalaEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/impala/impala.animation.json");
    }
}
