package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.HuskGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.HuskVariant;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Husk;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class HuskGeoModel<T extends Husk & HuskGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/zombie/husk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<Object> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<Object> variant = holder.ott$getVariantData();
            if (variant.isPresent() && variant.get() instanceof HuskVariant huskVariant) {
                ClientAsset asset = huskVariant.modelAndTexture().asset();
                int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
            }
        }
        return Ott.resource("textures/entity/zombie/husk_1.png");
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
        GeoBone headwear = this.getAnimationProcessor().getBone("headwear");
        GeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
        GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
        GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
        GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");

        GeoModelUtils.applyHeadRotation(animationState, head, headwear);
        GeoModelUtils.applyLimbSwingHumanoid(animationState, leftArm, rightArm, leftLeg, rightLeg);

        // Vanilla Zombie arms reaching forward
        if (rightArm != null) rightArm.setRotX(rightArm.getRotX() + (float)Math.PI / 2F);
        if (leftArm != null) leftArm.setRotX(leftArm.getRotX() + (float)Math.PI / 2F);

        float headScale = animatable.isBaby() ? 1.5f : 1.0f;
        if (head != null) { head.setScaleX(headScale); head.setScaleY(headScale); head.setScaleZ(headScale); }
        if (headwear != null) { headwear.setScaleX(headScale); headwear.setScaleY(headScale); headwear.setScaleZ(headScale); }
    }
}




