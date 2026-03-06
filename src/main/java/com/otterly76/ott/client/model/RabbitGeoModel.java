package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.RabbitGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.RabbitVariant;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Rabbit;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class RabbitGeoModel<T extends Rabbit & RabbitGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/rabbit/rabbit.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<RabbitVariant> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<RabbitVariant> variant = holder.ott$getVariantData();
            if (variant.isPresent()) {
                ClientAsset asset = variant.get().modelAndTexture().asset();
                int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
            }
        }
        return Ott.resource("textures/entity/rabbit/brown_1.png");
    }

    @Override
    @Nullable
    public ResourceLocation getAnimationResource(T animatable) {
        return null;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);

        GeoBone head = this.getAnimationProcessor().getBone("rabbit_head");
        GeoBone body = this.getAnimationProcessor().getBone("rabbit_body");
        GeoBone leftBackLeg = this.getAnimationProcessor().getBone("rabbit_left_back_leg");
        GeoBone rightBackLeg = this.getAnimationProcessor().getBone("rabbit_right_back_leg");
        GeoBone leftFrontLeg = this.getAnimationProcessor().getBone("rabbit_leg_front_left");
        GeoBone rightFrontLeg = this.getAnimationProcessor().getBone("rabbit_leg_front_right");

        GeoModelUtils.applyHeadRotation(animationState, head);

        float jumpCompletion = animatable.getJumpCompletion(animationState.getPartialTick());
        float jumpFactor = Mth.sin(jumpCompletion * (float) Math.PI);

        if (leftBackLeg != null) leftBackLeg.setRotX((jumpFactor * 50.0F - 21.0F) * Mth.DEG_TO_RAD);
        if (rightBackLeg != null) rightBackLeg.setRotX((jumpFactor * 50.0F - 21.0F) * Mth.DEG_TO_RAD);
        if (leftFrontLeg != null) leftFrontLeg.setRotX((jumpFactor * -40.0F - 11.0F) * Mth.DEG_TO_RAD);
        if (rightFrontLeg != null) rightFrontLeg.setRotX((jumpFactor * -40.0F - 11.0F) * Mth.DEG_TO_RAD);

        if (body != null) body.setRotX(jumpFactor * -20.0F * Mth.DEG_TO_RAD);
    }
}