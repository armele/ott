package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.GoldenHermitKing;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GoldenHermitKingModel extends GeoModel<GoldenHermitKing> {
    @Override
    public ResourceLocation getModelResource(GoldenHermitKing animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/golden_hermit_king/golden_hermit_king.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GoldenHermitKing animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/golden_hermit_king/golden_hermit_king.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GoldenHermitKing animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/golden_hermit_king/golden_hermit_king.animation.json");
    }
}