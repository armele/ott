package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Rhino;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

@OnlyIn(Dist.CLIENT)
public class RhinoModel extends GeoModel<Rhino> {
    @Override
    public ResourceLocation getModelResource(Rhino entity, @Nullable GeoRenderer<Rhino> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/rhino/rhino.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(Rhino entity) {
        return this.getModelResource(entity, null);
    }

    @Override
    public ResourceLocation getTextureResource(Rhino entity, @Nullable GeoRenderer<Rhino> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/rhino/rhino.png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Rhino entity) {
        return this.getTextureResource(entity, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Rhino entity) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/rhino/rhino.animation.json");
    }

    @Override
    public void setCustomAnimations(@NotNull Rhino entity, long instanceId, @Nullable AnimationState<Rhino> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (extraDataOfType == null) return;
        this.getBone("skull").ifPresent(skull -> {
            if (entity.isBaby()) {
                skull.setScaleX(1.4F);
                skull.setScaleY(1.4F);
                skull.setScaleZ(1.4F);
                this.getBone("left_ear").ifPresent(leftEar -> {
                    leftEar.setScaleX(1.1F);
                    leftEar.setScaleY(1.1F);
                    leftEar.setScaleZ(1.1F);
                });
                this.getBone("right_ear").ifPresent(rightEar -> {
                    rightEar.setScaleX(1.1F);
                    rightEar.setScaleY(1.1F);
                    rightEar.setScaleZ(1.1F);
                });
            } else {
                skull.setScaleX(1.0F);
                skull.setScaleY(1.0F);
                skull.setScaleZ(1.0F);
                this.getBone("left_ear").ifPresent(leftEar -> {
                    leftEar.setScaleX(1.0F);
                    leftEar.setScaleY(1.0F);
                    leftEar.setScaleZ(1.0F);
                });
                this.getBone("right_ear").ifPresent(rightEar -> {
                    rightEar.setScaleX(1.0F);
                    rightEar.setScaleY(1.0F);
                    rightEar.setScaleZ(1.0F);
                });
            }
            if (!entity.isSprinting()) {
                skull.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
            }
        });

        this.getBone("big_horn").ifPresent(bone -> bone.setHidden(entity.isBaby()));
        this.getBone("small_horn").ifPresent(bone -> bone.setHidden(entity.isBaby()));
        this.getBone("baby_horn").ifPresent(bone -> bone.setHidden(!entity.isBaby()));
    }
}