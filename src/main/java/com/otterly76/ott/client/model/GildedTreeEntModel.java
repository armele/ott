package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.GildedTreeEnt;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GildedTreeEntModel extends GeoModel<GildedTreeEnt> {
    @Override
    public ResourceLocation getModelResource(GildedTreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/gildedtreeent/gildedtreeent.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GildedTreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/gildedtreeent/gildedtreeent.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GildedTreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/gildedtreeent/gildedtreeent.animation.json");
    }
}