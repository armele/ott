package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.MarineIguana;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class MarineIguanaModel extends GeoModel<MarineIguana> {
    @Override
    public ResourceLocation getModelResource(MarineIguana animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/marine_iguana/marine_iguana.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MarineIguana animatable) {
        if (animatable.isGojira()) {
            return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/marine_iguana/marine_iguana_gojira.png");
        }
        return switch (animatable.getVariant()) {
            case 1 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/marine_iguana/marine_iguana_neon.png");
            case 2 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/marine_iguana/marine_iguana_1.png");
            case 3 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/marine_iguana/marine_iguana_2.png");
            case 4 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/marine_iguana/marine_iguana_3.png");
            default -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/marine_iguana/marine_iguana.png");
        };
    }

    @Override
    public ResourceLocation getAnimationResource(MarineIguana animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/marine_iguana/marine_iguana.animation.json");
    }

    @Override
    public void setCustomAnimations(MarineIguana animatable, long instanceId, AnimationState<MarineIguana> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            GeoBone root = this.getAnimationProcessor().getBone("marine_iguana");
            GeoBone head = this.getAnimationProcessor().getBone("head");
            
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

                if (animatable.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7 && animatable.isInWater()) {
                    assert extraDataOfType != null;
                    root.setRotY(extraDataOfType.netHeadYaw() * ((float) Math.PI / 180F));
                    root.setRotX(extraDataOfType.headPitch() * ((float) Math.PI / 180F));
                }
            }
        }
    }
}