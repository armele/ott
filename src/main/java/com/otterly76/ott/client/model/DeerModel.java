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

public class DeerModel extends GeoModel<Deer> {
    @Override
    public ResourceLocation getModelResource(Deer deer) {
        if (deer.isBaby()) {
            return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/deer/fawn.geo.json");
        }
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/deer/deer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Deer deer) {
        if (deer.isBaby()) {
            return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/deer/deer_baby.png");
        }
        
        String base = "deer";
        if (deer instanceof ReindeerEntity) base = "reindeer";
        else if (deer instanceof WhiteDeerEntity) base = "white_deer";
        
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
        GeoBone head = this.getAnimationProcessor().getBone("head");
        if (head == null) head = this.getAnimationProcessor().getBone("skull");

        if (head != null && !entity.isEating()) {
            assert extraDataOfType != null;
            head.setRotX(extraDataOfType.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(extraDataOfType.netHeadYaw() * Mth.DEG_TO_RAD);
        }

        GeoBone saddle = this.getAnimationProcessor().getBone("saddle");
        if (saddle != null) {
            saddle.setHidden(!entity.isSaddled());
        }
    }
}