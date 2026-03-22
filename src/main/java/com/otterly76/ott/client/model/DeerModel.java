package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Deer;
import com.otterly76.ott.entity.custom.ReindeerEntity;
import com.otterly76.ott.entity.custom.WhiteDeerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;
import org.jetbrains.annotations.Nullable;

public class DeerModel extends GeoModel<Deer> {
    @Override
    @Deprecated
    public ResourceLocation getModelResource(Deer deer) {
        return getModelResource(deer, null);
    }

    @Override
    public ResourceLocation getModelResource(Deer deer, @Nullable GeoRenderer<Deer> renderer) {
        if (deer.isBaby()) {
            return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/deer/fawn.geo.json");
        }
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/deer/deer.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Deer deer) {
        return getTextureResource(deer, null);
    }

    @Override
    public ResourceLocation getTextureResource(Deer deer, @Nullable GeoRenderer<Deer> renderer) {
        if (deer.isBaby()) {
            return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/deer/deer_baby.png");
        }
        
        String base = "deer";
        if (deer instanceof ReindeerEntity reindeer) {
            if (reindeer.isRedNose()) {
                base = "reindeer_red_nose";
            } else {
                base = "reindeer";
            }
        } else if (deer instanceof WhiteDeerEntity) {
            base = "white_deer";
        }

        if (deer.isDoe()) {
            base += "_doe";
        }
        
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/deer/" + base + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Deer deer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/deer/deer.animation.json");
    }

    @Override
    public void setCustomAnimations(Deer entity, long instanceId, AnimationState<Deer> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
        if (animationState == null) return;

        EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if (extraDataOfType == null) return;
        GeoBone head = this.getAnimationProcessor().getBone("head");
        if (head == null) head = this.getAnimationProcessor().getBone("skull");

        if (head != null && !entity.isEating()) {
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        GeoBone saddle = this.getAnimationProcessor().getBone("saddle");
        if (saddle != null) {
            saddle.setHidden(!entity.isSaddled());
        }

        GeoBone antlers = this.getAnimationProcessor().getBone("antlers");
        if (antlers != null) {
            antlers.setHidden(entity.isDoe());
        }
    }
}