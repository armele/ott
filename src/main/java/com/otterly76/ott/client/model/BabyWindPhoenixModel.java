package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BabyWindPhoenix;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BabyWindPhoenixModel extends GeoModel<BabyWindPhoenix> {
    @Override
    public ResourceLocation getModelResource(BabyWindPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/baby_wind_phoenix/baby_wind_phoenix.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BabyWindPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/baby_wind_phoenix/baby_wind_phoenix.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BabyWindPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/baby_wind_phoenix/baby_wind_phoenix.animation.json");
    }
}