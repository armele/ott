package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.DrownedGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.DrownedVariant;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Drowned;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

import java.util.Optional;

public class DrownedGeoModel<T extends Drowned & DrownedGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        return Ott.resource("geo/entity/zombie/drowned.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(T animatable, @Nullable GeoRenderer<T> renderer) {
        VariantDataHolder<Object> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<Object> variant = holder.ott$getVariantData();
            if (variant.isPresent() && variant.get() instanceof DrownedVariant drownedVariant) {
                ClientAsset asset = drownedVariant.modelAndTexture().asset();
                int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
            }
        }
        return Ott.resource("textures/entity/zombie/drowned_1.png");
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

        float headScale = animatable.isBaby() ? 1.5f : 1.0f;
        if (head != null) { head.setScaleX(headScale); head.setScaleY(headScale); head.setScaleZ(headScale); }
        if (headwear != null) { headwear.setScaleX(headScale); headwear.setScaleY(headScale); headwear.setScaleZ(headScale); }

        // Vanilla Zombie arms reaching forward (when not swimming)
        if (!animatable.isSwimming()) {
            if (rightArm != null) rightArm.setRotX(rightArm.getRotX() + (float)Math.PI / 2F);
            if (leftArm != null) leftArm.setRotX(leftArm.getRotX() + (float)Math.PI / 2F);
        }
    }
}