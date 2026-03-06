package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.VexGeoEntity;
import com.otterly76.ott.entity.variant.VexVariant;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Vex;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class VexGeoModel<T extends Vex & VexGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/vex/vex.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<Object> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<Object> variant = holder.ott$getVariantData();
            if (variant.isPresent() && variant.get() instanceof VexVariant vexVariant) {
                ClientAsset asset = vexVariant.modelAndTexture().asset();
                int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                String suffix = animatable.isCharging() ? "_charging_" : "_";
                return asset.id().withPath((path) -> "textures/" + path + suffix + index + ".png");
            }
        }
        return Ott.resource("textures/entity/illager/" + (animatable.isCharging() ? "vex_charging_1.png" : "vex_1.png"));
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

        if (body != null) {
            body.setRotX(limbSwingAmount * 0.4F);
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

        // Arms - Vex arms are usually out when charging
        if (animatable.isCharging()) {
            if (rightArm != null) {
                rightArm.setRotX(0.7853982F);
                rightArm.setRotY(-0.19634955F);
            }
            if (leftArm != null) {
                leftArm.setRotX(0.7853982F);
                leftArm.setRotY(0.19634955F);
            }
        } else {
            // Idle arms
            float bob = Mth.cos(ageInTicks * 0.2F) * 0.05F;
            if (rightArm != null) {
                rightArm.setRotX(0.7853982F + bob);
                rightArm.setRotY(-0.19634955F);
            }
            if (leftArm != null) {
                leftArm.setRotX(0.7853982F + bob);
                leftArm.setRotY(0.19634955F);
            }
        }
    }
}