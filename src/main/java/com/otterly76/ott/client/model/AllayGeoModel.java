package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.AllayGeoEntity;
import com.otterly76.ott.entity.variant.AllayVariant;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.allay.Allay;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class AllayGeoModel<T extends Allay & AllayGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/allay/allay.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<Object> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<Object> variant = holder.ott$getVariantData();
            if (variant.isPresent() && variant.get() instanceof AllayVariant allayVariant) {
                ClientAsset asset = allayVariant.modelAndTexture().asset();
                int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
            }
        }
        return Ott.resource("textures/entity/allay/allay_1.png");
    }

    @Override
    @Nullable
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone head = this.getAnimationProcessor().getBone("head");
        GeoBone body = this.getAnimationProcessor().getBone("body");
        GeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
        GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
        GeoBone rightWing = this.getAnimationProcessor().getBone("right_wing");
        GeoBone leftWing = this.getAnimationProcessor().getBone("left_wing");

        GeoModelUtils.applyHeadRotation(animationState, head);

        float ageInTicks = (float) animatable.tickCount + animationState.getPartialTick();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        // Flying/Floating animation
        float bob = Mth.cos(ageInTicks * 0.2F) * 0.05F;
        if (body != null) {
            body.setRotX(limbSwingAmount * 0.4F);
            body.setRotY(0);
            body.setRotZ(0);
        }

        // Wings
        if (rightWing != null) {
            rightWing.setRotX(0.43633232F);
            rightWing.setRotY(-0.61086524F + Mth.cos(ageInTicks * 0.8F) * (float)Math.PI * 0.25F);
        }
        if (leftWing != null) {
            leftWing.setRotX(0.43633232F);
            leftWing.setRotY(0.61086524F - Mth.cos(ageInTicks * 0.8F) * (float)Math.PI * 0.25F);
        }

        // Arms
        if (rightArm != null) {
            rightArm.setRotX(0.7853982F + bob);
            rightArm.setRotY(-0.19634955F);
        }
        if (leftArm != null) {
            leftArm.setRotX(0.7853982F + bob);
            leftArm.setRotY(0.19634955F);
        }

        // Dancing or Item lift
        if (animatable.isDancing()) {
            float danceTime = ageInTicks * 8.0F * Mth.DEG_TO_RAD;
            if (body != null) body.setRotZ(Mth.cos(danceTime) * 0.02F);
            if (head != null) head.setRotZ(Mth.cos(danceTime) * 0.02F);
            if (rightArm != null) rightArm.setRotZ(Mth.cos(danceTime) * 0.02F);
            if (leftArm != null) leftArm.setRotZ(Mth.cos(danceTime) * 0.02F);
        } else if (!animatable.getMainHandItem().isEmpty()) {
            if (rightArm != null) rightArm.setRotX(rightArm.getRotX() - 0.5F);
            if (leftArm != null) leftArm.setRotX(leftArm.getRotX() - 0.5F);
        }
    }
}