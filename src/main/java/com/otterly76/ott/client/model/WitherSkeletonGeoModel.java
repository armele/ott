package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.WitherSkeletonGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import com.otterly76.ott.entity.variant.WitherSkeletonVariant;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.WitherSkeleton;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

import java.util.Optional;

public class WitherSkeletonGeoModel<T extends WitherSkeleton & WitherSkeletonGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/wither_skeleton/wither_skeleton.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<WitherSkeletonVariant> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<WitherSkeletonVariant> variant = holder.ott$getVariantData();
            if (variant.isPresent()) {
                ClientAsset asset = variant.get().modelAndTexture().asset();
                int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
            }
        }
        return Ott.resource("textures/entity/skeleton/wither_skeleton_1.png");
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
        GeoBone rightArm = this.getAnimationProcessor().getBone("right_arm");
        GeoBone leftArm = this.getAnimationProcessor().getBone("left_arm");
        GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
        GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");

        GeoModelUtils.applyHeadRotation(animationState, head);
        GeoModelUtils.applyLimbSwingHumanoid(animationState, leftArm, rightArm, leftLeg, rightLeg);
    }
}