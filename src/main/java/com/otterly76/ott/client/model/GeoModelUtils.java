package com.otterly76.ott.client.model;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class GeoModelUtils {

    /**
     * Standard Minecraft 2-leg walking animation.
     */
    public static void applyLimbSwing2Legs(AnimationState<?> animationState, GeoBone leftLeg, GeoBone rightLeg) {
        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        if (rightLeg != null) rightLeg.setRotX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        if (leftLeg != null) leftLeg.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount);
    }

    /**
     * Standard Minecraft 4-leg walking animation.
     */
    public static void applyLimbSwing4Legs(AnimationState<?> animationState, GeoBone leg1, GeoBone leg2, GeoBone leg3, GeoBone leg4) {
        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        if (leg1 != null) leg1.setRotX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        if (leg2 != null) leg2.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount);
        if (leg3 != null) leg3.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount);
        if (leg4 != null) leg4.setRotX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
    }

    /**
     * Standard Minecraft humanoid walking animation.
     */
    public static void applyLimbSwingHumanoid(AnimationState<?> animationState, GeoBone leftArm, GeoBone rightArm, GeoBone leftLeg, GeoBone rightLeg) {
        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        if (rightArm != null) rightArm.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F);
        if (leftArm != null) leftArm.setRotX(Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F);
        if (rightLeg != null) rightLeg.setRotX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        if (leftLeg != null) leftLeg.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount);
    }

    /**
     * Standard Minecraft bird (chicken) wing flapping animation.
     */
    public static <T extends Chicken & GeoAnimatable> void applyBirdFlap(T animatable, AnimationState<T> animationState, GeoBone leftWing, GeoBone rightWing) {
        float flap = Mth.lerp(animationState.getPartialTick(), animatable.oFlap, animatable.flap);
        float flapSpeed = Mth.lerp(animationState.getPartialTick(), animatable.oFlapSpeed, animatable.flapSpeed);
        float bob = (Mth.sin(flap) + 1.0F) * flapSpeed;

        if (rightWing != null) rightWing.setRotZ(bob);
        if (leftWing != null) leftWing.setRotZ(-bob);
    }

    /**
     * Standard Minecraft head rotation logic.
     */
    public static void applyHeadRotation(AnimationState<?> animationState, GeoBone... bones) {
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (entityData != null) {
            float headPitch = entityData.headPitch() * Mth.DEG_TO_RAD;
            float netHeadYaw = entityData.netHeadYaw() * Mth.DEG_TO_RAD;
            for (GeoBone bone : bones) {
                if (bone != null) {
                    bone.setRotX(headPitch);
                    bone.setRotY(netHeadYaw);
                }
            }
        }
    }
}