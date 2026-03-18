package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.BirdGeoEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

/**
 * Base model class for birds (Duck, Goose) to handle shared animation and variant logic.
 */

public abstract class BirdGeoModel<T extends Chicken & BirdGeoEntity> extends GeoModel<T> {
    private final String name;

    public BirdGeoModel(String name) {
        this.name = name;
    }

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/" + name + "/" + name + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        int variant = animatable.getVariant() + 1; // variant 0 -> name_1, 1 -> name_2
        return Ott.resource("textures/entity/" + name + "/" + name + "_" + variant + ".png");
    }

    @Override
    @Nullable
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        // Try both "head" and specific bone names
        GeoBone head = this.getAnimationProcessor().getBone("head");
        if (head == null || head.getChildBones().isEmpty()) {
            GeoBone namedHead = this.getAnimationProcessor().getBone(name + "_head");
            if (namedHead != null) head = namedHead;
        }

        GeoBone leftLeg = this.getAnimationProcessor().getBone(name + "_left_leg");
        GeoBone rightLeg = this.getAnimationProcessor().getBone(name + "_right_leg");
        GeoBone leftWing = this.getAnimationProcessor().getBone(name + "_left_wing");
        GeoBone rightWing = this.getAnimationProcessor().getBone(name + "_right_wing");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null && entityData != null) {
            head.setRotY(-entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotZ(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        GeoModelUtils.applyLimbSwing2Legs(animationState, leftLeg, rightLeg);
        GeoModelUtils.applyBirdFlap(animatable, animationState, leftWing, rightWing);
    }
}
