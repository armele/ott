package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.HermitKing;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HermitKingModel extends GeoModel<HermitKing> {
    @Override
    public ResourceLocation getModelResource(HermitKing animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/hermit_king/hermit_king.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HermitKing animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/hermit_king/hermit_king.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HermitKing animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/hermit_king/hermit_king.animation.json");
    }
}