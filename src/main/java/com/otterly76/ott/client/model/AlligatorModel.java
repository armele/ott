package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Alligator;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;
import org.jetbrains.annotations.Nullable;

public class AlligatorModel extends GeoModel<Alligator> {
    @Override
    @Deprecated
    public ResourceLocation getModelResource(Alligator animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(Alligator animatable, @Nullable GeoRenderer<Alligator> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/alligator/alligator.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Alligator animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Alligator animatable, @Nullable GeoRenderer<Alligator> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/alligator/alligator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Alligator animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/alligator/alligator.animation.json");
    }

    @Override
    public void setCustomAnimations(Alligator entity, long instanceId, AnimationState<Alligator> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (extraDataOfType == null) return;
        GeoBone head = this.getAnimationProcessor().getBone("head");
        if (head == null) head = this.getAnimationProcessor().getBone("skull");

        if (head != null) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
