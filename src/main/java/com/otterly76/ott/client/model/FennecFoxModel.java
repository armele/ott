package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.FennecFox;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class FennecFoxModel extends GeoModel<FennecFox> {
    @Override
    public ResourceLocation getModelResource(FennecFox animatable, GeoRenderer<FennecFox> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/fennec_fox/fennec_fox.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FennecFox animatable, GeoRenderer<FennecFox> renderer) {
        String variant = switch (animatable.getVariant()) {
            case 1 -> "creamy";
            case 2 -> "pale";
            case 3 -> "red";
            default -> "beige";
        };
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/fennec_fox/" + variant + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(FennecFox animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/fennec_fox/fennec_fox.animation.json");
    }

    @Override
    public void setCustomAnimations(FennecFox animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<FennecFox> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            this.getBone("head").ifPresent(bone -> {
                assert entityData != null;
                bone.setRotX(entityData.headPitch() * 0.017453292F);
                bone.setRotY(entityData.netHeadYaw() * 0.017453292F);
            });
        }
    }

    @Override
    public ResourceLocation getModelResource(FennecFox animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(FennecFox animatable) {
        return getTextureResource(animatable, null);
    }
}
