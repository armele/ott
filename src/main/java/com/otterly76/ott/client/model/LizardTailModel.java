package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.LizardTail;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class LizardTailModel extends GeoModel<LizardTail> {
    @Override
    public ResourceLocation getModelResource(LizardTail animatable, GeoRenderer<LizardTail> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/lizard/lizard_tail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LizardTail animatable, GeoRenderer<LizardTail> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/lizard/lizard_" + animatable.getVariant() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(LizardTail animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/lizard/lizard_tail.animation.json");
    }

    @Override
    public ResourceLocation getModelResource(LizardTail animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(LizardTail animatable) {
        return getTextureResource(animatable, null);
    }
}
