package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Toucan;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class ToucanModel extends GeoModel<Toucan> {
    @Override
    public @NotNull ResourceLocation getModelResource(@NotNull Toucan animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/toucan/toucan.geo.json");
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(@NotNull Toucan animatable) {
        return animatable.getVariant() == 1
                ? ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/toucan/toucan_red.png")
                : ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/toucan/toucan.png");
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