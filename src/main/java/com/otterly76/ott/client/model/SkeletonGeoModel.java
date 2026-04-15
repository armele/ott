package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.SkeletonGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.SkeletonVariant;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import com.otterly76.ott.util.entity.OttBabyMob;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Skeleton;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class SkeletonGeoModel<T extends Skeleton & SkeletonGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/skeleton/skeleton.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<SkeletonVariant> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<SkeletonVariant> variant = holder.ott$getVariantData();
            if (variant.isPresent()) {
                ClientAsset asset = variant.get().modelAndTexture().asset();
                int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
            }
        }
        return Ott.resource("textures/entity/skeleton/skeleton_1.png");
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

        float headScale = animatable instanceof OttBabyMob babyMob && babyMob.ott$isBaby() ? 2.0f : 1.0f;
        if (head != null) { head.setScaleX(headScale); head.setScaleY(headScale); head.setScaleZ(headScale); }
        if (headwear != null) { headwear.setScaleX(headScale); headwear.setScaleY(headScale); headwear.setScaleZ(headScale); }
    }
}