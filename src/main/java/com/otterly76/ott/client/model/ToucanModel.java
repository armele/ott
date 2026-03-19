package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Toucan;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class ToucanModel extends GeoModel<Toucan> {
    @Override
    public ResourceLocation getModelResource(Toucan animatable, @Nullable GeoRenderer<Toucan> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/toucan/toucan.geo.json");
    }

    @Override
    @Deprecated
    public @NotNull ResourceLocation getModelResource(@NotNull Toucan animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Toucan animatable, @Nullable GeoRenderer<Toucan> renderer) {
        return animatable.getVariant() == 1
                ? ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/toucan/toucan_red.png")
                : ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/toucan/toucan.png");
    }

    @Override
    @Deprecated
    public @NotNull ResourceLocation getTextureResource(@NotNull Toucan animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(@NotNull Toucan animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/toucan/toucan.animation.json");
    }

    @Override
    public void setCustomAnimations(Toucan animatable, long instanceId, AnimationState<Toucan> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            GeoBone root = this.getAnimationProcessor().getBone("Toucan");
            if (root != null) {
                if (animatable.isBaby()) {
                    root.setScaleX(0.5F);
                    root.setScaleY(0.5F);
                    root.setScaleZ(0.5F);
                } else {
                    root.setScaleX(1.0F);
                    root.setScaleY(1.0F);
                    root.setScaleZ(1.0F);
                }
            }
        }
    }
}