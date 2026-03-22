package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Emu;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class EmuModel extends GeoModel<Emu> {
    private static final ResourceLocation ADULT_MODEL = ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/emu/emu.geo.json");
    private static final ResourceLocation BABY_MODEL = ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/emu/baby_emu.geo.json");
    private static final ResourceLocation ADULT_ANIMATION = ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/emu/emu.animation.json");
    private static final ResourceLocation BABY_ANIMATION = ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/emu/baby_emu.animation.json");

    @Override
    public ResourceLocation getModelResource(Emu animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(Emu animatable, @Nullable GeoRenderer<Emu> renderer) {
        return animatable.isBaby() ? BABY_MODEL : ADULT_MODEL;
    }

    @Override
    public ResourceLocation getTextureResource(Emu animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Emu animatable, @Nullable GeoRenderer<Emu> renderer) {
        return animatable.isBaby() ? ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/emu/baby_emu.png") : ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/emu/emu.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Emu animatable) {
        return animatable.isBaby() ? BABY_ANIMATION : ADULT_ANIMATION;
    }

    @Override
    public void setCustomAnimations(Emu animatable, long instanceId, AnimationState<Emu> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            GeoBone head = this.getAnimationProcessor().getBone("head");
            if (head != null && extraDataOfType != null && !animatable.isImmobile()) {
                head.setRotY(extraDataOfType.netHeadYaw() * ((float) Math.PI / 180F));
                head.setRotX(extraDataOfType.headPitch() * ((float) Math.PI / 180F));
            }
        }
    }
}