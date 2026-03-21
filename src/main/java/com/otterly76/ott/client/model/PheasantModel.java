package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Pheasant;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class PheasantModel extends GeoModel<Pheasant> {
    @Override
    public ResourceLocation getModelResource(Pheasant animatable, @Nullable GeoRenderer<Pheasant> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/pheasant/pheasant.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(Pheasant animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Pheasant animatable, @Nullable GeoRenderer<Pheasant> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/pheasant/pheasant.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Pheasant animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Pheasant animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/pheasant/pheasant.animation.json");
    }
}
