package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.TreeKangarooEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class TreeKangarooModel extends GeoModel<TreeKangarooEntity> {
    @Override
    public ResourceLocation getModelResource(TreeKangarooEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/tree_kangaroo/tree_kangaroo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(TreeKangarooEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/tree_kangaroo/tree_kangaroo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(TreeKangarooEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/tree_kangaroo/tree_kangaroo.animation.json");
    }
}
