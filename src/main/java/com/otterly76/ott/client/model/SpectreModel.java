package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Spectre;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SpectreModel extends GeoModel<Spectre> {
    @Override
    public ResourceLocation getModelResource(Spectre animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/spectre/spectre.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Spectre animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/spectre/spectre.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Spectre animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/spectre/spectre.animation.json");
    }
}