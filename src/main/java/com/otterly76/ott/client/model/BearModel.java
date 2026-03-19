package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Bear;
import com.otterly76.ott.entity.custom.BlackBearEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BearModel extends GeoModel<Bear> {
    @Override
    public ResourceLocation getModelResource(Bear bear) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/bear/bear.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Bear bear) {
        boolean isBlack = bear instanceof BlackBearEntity;
        String base = isBlack ? "black_bear" : "bear";

        if (isBlack) {
            if (bear.isAngry()) {
                return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/bear/black_bear_angry.png");
            } else if (bear.isEating()) {
                if (bear.getMainHandItem().is(Items.SWEET_BERRIES)) {
                    return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/bear/black_bear_berries.png");
                } else if (bear.getMainHandItem().is(Items.HONEYCOMB)) {
                    return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/bear/black_bear_honey.png");
                }
            }
        }

        if (bear.isBaby()) {
            return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/bear/" + base + "_baby.png");
        }

        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/bear/" + base + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bear bear) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/bear/bear.animation.json");
    }

    @Override
    public void setCustomAnimations(Bear entity, long instanceId, AnimationState<Bear> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        if (head == null) head = this.getAnimationProcessor().getBone("skull");

        if (head != null) {
            if (entity.isBaby()) {
                head.setScaleX(1.8F);
                head.setScaleY(1.8F);
                head.setScaleZ(1.8F);
            } else {
                head.setScaleX(1.0F);
                head.setScaleY(1.0F);
                head.setScaleZ(1.0F);
            }
            if (!entity.isSleeping() && !entity.isEating() && !entity.isSitting()) {
                assert extraDataOfType != null;
                head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
                head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }
    }
}