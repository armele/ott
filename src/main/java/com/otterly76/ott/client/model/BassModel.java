package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Bass;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BassModel extends GeoModel<Bass> {
    @Override
    public ResourceLocation getModelResource(Bass animatable, @Nullable GeoRenderer<Bass> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/bass/bass.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(Bass animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Bass animatable, @Nullable GeoRenderer<Bass> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/bass/bass.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Bass animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Bass animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/bass/bass.animation.json");
    }
}