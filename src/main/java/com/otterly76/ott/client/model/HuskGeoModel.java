package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.HuskGeoEntity;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.HuskVariant;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.Husk;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Optional;

public class HuskGeoModel<T extends Husk & HuskGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/zombie/husk.geo.json");
    }

    @Override
    @SuppressWarnings("unchecked")
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

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
        if (headwear != null) {
            headwear.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            headwear.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        if (rightArm != null) rightArm.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 2.0F * limbSwingAmount * 0.5F);
        if (leftArm != null) leftArm.setRotX(Mth.cos(limbSwing * 0.6662F) * 2.0F * limbSwingAmount * 0.5F);
        if (rightLeg != null) rightLeg.setRotX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        if (leftLeg != null) leftLeg.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount);

        // Vanilla Zombie arms reaching forward
        if (rightArm != null) rightArm.setRotX(rightArm.getRotX() + (float)Math.PI / 2F);
        if (leftArm != null) leftArm.setRotX(leftArm.getRotX() + (float)Math.PI / 2F);
    }
}