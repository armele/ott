package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Lizard;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class LizardModel extends GeoModel<Lizard> {
    @Override
    public ResourceLocation getModelResource(Lizard animatable, GeoRenderer<Lizard> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/lizard/lizard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lizard animatable, GeoRenderer<Lizard> renderer) {
        String variant = switch (animatable.getVariant()) {
            case 0 -> "green";
            case 2 -> "beardie";
            case 3 -> "leopard_gecko";
            default -> "brown";
        };
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/lizard/" + variant + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Lizard animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/lizard/lizard.animation.json");
    }

    @Override
    public void setCustomAnimations(Lizard animatable, long instanceId, software.bernie.geckolib.animation.AnimationState<Lizard> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            this.getBone("skull").ifPresent(bone -> {
                assert entityData != null;
                bone.setRotX(entityData.headPitch() * 0.017453292F);
                bone.setRotY(entityData.netHeadYaw() * 0.017453292F);
            });
            this.getBone("tail").ifPresent(bone -> bone.setHidden(!animatable.hasTail()));
        }
    }

    @Override
    public ResourceLocation getModelResource(Lizard animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Lizard animatable) {
        return getTextureResource(animatable, null);
    }
}