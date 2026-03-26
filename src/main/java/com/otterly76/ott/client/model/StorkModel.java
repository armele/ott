package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.StorkEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StorkModel extends GeoModel<StorkEntity> {
    @Override
    public ResourceLocation getModelResource(StorkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/stork/stork.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StorkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/stork/stork.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StorkEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/stork/stork.animation.json");
    }
}
