package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Hoopoe;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class HoopoeModel extends GeoModel<Hoopoe> {
    @Override
    public ResourceLocation getModelResource(Hoopoe animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/hoopoe/hoopoe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Hoopoe animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/hoopoe/hoopoe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Hoopoe animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/hoopoe/hoopoe.animation.json");
    }

    @Override
    public void setCustomAnimations(Hoopoe animatable, long instanceId, AnimationState<Hoopoe> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            GeoBone head = this.getAnimationProcessor().getBone("neck");
            GeoBone root = this.getAnimationProcessor().getBone("Hoopoe");
            
            if (root != null) {
                if (animatable.isBaby()) {
                    root.setScaleX(0.5F);
                    root.setScaleY(0.5F);
                    root.setScaleZ(0.5F);
                    if (head != null) {
                        head.setScaleX(1.75F);
                        head.setScaleY(1.75F);
                        head.setScaleZ(1.75F);
                    }
                } else {
                    root.setScaleX(1.0F);
                    root.setScaleY(1.0F);
                    root.setScaleZ(1.0F);
                    if (head != null) {
                        head.setScaleX(1.0F);
                        head.setScaleY(1.0F);
                        head.setScaleZ(1.0F);
                    }
                }
            }

            if (head != null && extraDataOfType != null && !animatable.isImmobile()) {
                head.setRotY(extraDataOfType.netHeadYaw() * ((float) Math.PI / 180F));
                head.setRotX(extraDataOfType.headPitch() * ((float) Math.PI / 180F));
            }
        }
    }
}