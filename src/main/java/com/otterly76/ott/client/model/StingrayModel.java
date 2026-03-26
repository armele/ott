package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.StingrayEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class StingrayModel extends GeoModel<StingrayEntity> {
    @Override
    public ResourceLocation getModelResource(StingrayEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/stingray/stingray.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(StingrayEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/stingray/stingray.png");
    }

    @Override
    public ResourceLocation getAnimationResource(StingrayEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/stingray/stingray.animation.json");
    }
}
