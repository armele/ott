package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Angelfish;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class AngelfishModel extends GeoModel<Angelfish> {
    @Override
    public ResourceLocation getModelResource(Angelfish animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(Angelfish animatable, @Nullable GeoRenderer<Angelfish> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/angelfish/angelfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Angelfish animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Angelfish animatable, @Nullable GeoRenderer<Angelfish> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/angelfish/angelfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Angelfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/angelfish/angelfish.animation.json");
    }
}