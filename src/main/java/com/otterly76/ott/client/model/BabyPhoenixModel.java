package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.BabyPhoenix;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class BabyPhoenixModel extends GeoModel<BabyPhoenix> {
    @Override
    public ResourceLocation getModelResource(BabyPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/baby_phoenix/baby_phoenix.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BabyPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/baby_phoenix/baby_phoenix.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BabyPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/baby_phoenix/baby_phoenix.animation.json");
    }
}