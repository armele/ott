package com.otterly76.ott.client.model;

import com.otterly76.ott.Ott;
import com.otterly76.ott.entity.gecko.ChickenGeoEntity;
import com.otterly76.ott.entity.variant.ChickenVariant;
import com.otterly76.ott.entity.variant.ClientAsset;
import com.otterly76.ott.entity.variant.VariantDataHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Chicken;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.Optional;

public class ChickenGeoModel<T extends Chicken & ChickenGeoEntity> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T animatable) {
        return Ott.resource("geo/entity/chicken/chicken.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        VariantDataHolder<ChickenVariant> holder = VariantDataHolder.getHolder(animatable);
        if (holder != null) {
            Optional<ChickenVariant> variant = holder.ott$getVariantData();
            if (variant.isPresent()) {
                ClientAsset asset = variant.get().modelAndTexture().asset();
                final int index = (Math.abs((int) animatable.getUUID().getLeastSignificantBits()) % asset.count()) + 1;
                return asset.id().withPath((path) -> "textures/" + path + "_" + index + ".png");
            }
        }
        return Ott.resource("textures/entity/chicken/chicken_1.png");
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
        GeoBone bill = this.getAnimationProcessor().getBone("bill");
        GeoBone chin = this.getAnimationProcessor().getBone("chin");
        GeoBone leftLeg = this.getAnimationProcessor().getBone("left_leg");
        GeoBone rightLeg = this.getAnimationProcessor().getBone("right_leg");
        GeoBone leftWing = this.getAnimationProcessor().getBone("left_wing");
        GeoBone rightWing = this.getAnimationProcessor().getBone("right_wing");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (head != null) {
            float headPitch = entityData.headPitch() * Mth.DEG_TO_RAD;
            float netHeadYaw = entityData.netHeadYaw() * Mth.DEG_TO_RAD;

            head.setRotX(headPitch);
            head.setRotY(netHeadYaw);

            if (bill != null) {
                bill.setRotX(headPitch);
                bill.setRotY(netHeadYaw);
            }
            if (chin != null) {
                chin.setRotX(headPitch);
                chin.setRotY(netHeadYaw);
            }
        }

        float limbSwing = animationState.getLimbSwing();
        float limbSwingAmount = animationState.getLimbSwingAmount();

        if (rightLeg != null) rightLeg.setRotX(Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount);
        if (leftLeg != null) leftLeg.setRotX(Mth.cos(limbSwing * 0.6662F + (float)Math.PI) * 1.4F * limbSwingAmount);

        float flap = Mth.lerp(animationState.getPartialTick(), animatable.oFlap, animatable.flap);
        float flapSpeed = Mth.lerp(animationState.getPartialTick(), animatable.oFlapSpeed, animatable.flapSpeed);
        float bob = (Mth.sin(flap) + 1.0F) * flapSpeed;

        if (rightWing != null) rightWing.setRotZ(bob);
        if (leftWing != null) leftWing.setRotZ(-bob);
    }
}