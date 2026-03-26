package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.CandycaneSnailEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class CandycaneSnailModel extends GeoModel<CandycaneSnailEntity> {
    @Override
    public ResourceLocation getModelResource(CandycaneSnailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/candycane_snail/candycane_snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CandycaneSnailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/candycane_snail/candycane_snail.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CandycaneSnailEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/candycane_snail/candycane_snail.animation.json");
    }
}
