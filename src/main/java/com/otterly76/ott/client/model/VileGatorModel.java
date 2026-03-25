package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.VileGator;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class VileGatorModel extends GeoModel<VileGator> {
    @Override
    public ResourceLocation getModelResource(VileGator animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/vile_gator/vile_gator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VileGator animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/vile_gator/vile_gator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VileGator animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/vile_gator/vile_gator.animation.json");
    }
}