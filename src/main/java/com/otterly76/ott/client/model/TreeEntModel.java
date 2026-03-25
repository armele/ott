package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.TreeEnt;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TreeEntModel extends GeoModel<TreeEnt> {
    @Override
    public ResourceLocation getModelResource(TreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/treeent/treeent.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/treeent/treeent.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TreeEnt animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/treeent/treeent.animation.json");
    }
}