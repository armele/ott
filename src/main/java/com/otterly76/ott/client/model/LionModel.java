package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Lion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class LionModel extends GeoModel<Lion> {
    @Override
    public ResourceLocation getModelResource(Lion entity, @Nullable GeoRenderer<Lion> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/lion/lion.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(Lion entity) {
        return this.getModelResource(entity, null);
    }

    @Override
    public ResourceLocation getTextureResource(Lion entity, @Nullable GeoRenderer<Lion> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/lion/lion.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Lion entity) {
        return this.getTextureResource(entity, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Lion entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/lion/lion.animation.json");
    }

    @Override
    public void setCustomAnimations(Lion entity, long instanceId, @Nullable AnimationState<Lion> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (extraDataOfType == null) return;
        GeoBone skull = this.getAnimationProcessor().getBone("skull");
        GeoBone mane = this.getAnimationProcessor().getBone("mane");

        if (skull != null) {
            if (entity.isBaby()) {
                skull.setScaleX(1.4F);
                skull.setScaleY(1.4F);
                skull.setScaleZ(1.4F);
            } else {
                skull.setScaleX(1.0F);
                skull.setScaleY(1.0F);
                skull.setScaleZ(1.0F);
            }
            if (!entity.isSleeping()) {
                skull.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
                skull.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        }
        if (mane != null) mane.setHidden(!entity.hasMane() || entity.isBaby());
    }
}