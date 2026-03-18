package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Stingray;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class StingrayModel extends GeoModel<Stingray> {
    @Override
    public ResourceLocation getModelResource(Stingray animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/stingray/stingray.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Stingray animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/stingray/stingray_0.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Stingray animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/stingray/stingray.animation.json");
    }

    @Override
    public void setCustomAnimations(Stingray animatable, long instanceId, AnimationState<Stingray> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            GeoBone root = this.getAnimationProcessor().getBone("root");
            if (root != null && animatable.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7 && animatable.isInWater()) {
                assert extraDataOfType != null;
                root.setRotY(extraDataOfType.netHeadYaw() * ((float) Math.PI / 180F));
                root.setRotX(extraDataOfType.headPitch() * ((float) Math.PI / 180F));
            }
        }
    }
}




