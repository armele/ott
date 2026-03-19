package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Sunfish;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class SunfishModel extends GeoModel<Sunfish> {
    @Override
    public ResourceLocation getModelResource(Sunfish animatable, @Nullable GeoRenderer<Sunfish> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/sunfish/sunfish.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(Sunfish animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Sunfish animatable, @Nullable GeoRenderer<Sunfish> renderer) {
        return switch (animatable.getVariant()) {
            case 1 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/sunfish/sunfish_1.png");
            case 2 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/sunfish/sunfish_golden.png");
            default -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/sunfish/sunfish_0.png");
        };
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Sunfish animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Sunfish animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/sunfish/sunfish.animation.json");
    }
}