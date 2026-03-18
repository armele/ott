package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Barreleye;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BarreleyeModel extends GeoModel<Barreleye> {
    @Override
    public @NotNull ResourceLocation getModelResource(@NotNull Barreleye animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/barreleye/barreleye_fish.geo.json");
    }

    @Override
    public @NotNull ResourceLocation getTextureResource(@NotNull Barreleye animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/barreleye/barreleye_fish.png");
    }

    @Override
    public @NotNull ResourceLocation getAnimationResource(@NotNull Barreleye animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/barreleye/barreleye_fish.animation.json");
    }

    @Override
    public void setCustomAnimations(Barreleye animatable, long instanceId, AnimationState<Barreleye> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            GeoBone root = this.getAnimationProcessor().getBone("root");
            if (root != null && animatable.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7 && animatable.isInWater()) {
                assert extraDataOfType != null;
                root.setRotY(extraDataOfType.netHeadYaw() * ((float)Math.PI / 180F));
                root.setRotX(extraDataOfType.headPitch() * ((float)Math.PI / 180F));
            }
        }
    }
}