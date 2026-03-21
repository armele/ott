package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Gecko;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.renderer.GeoRenderer;

public class GeckoModel extends GeoModel<Gecko> {
    @Override
    public ResourceLocation getModelResource(Gecko animatable, @Nullable GeoRenderer<Gecko> renderer) {
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/gecko/gecko.geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(Gecko animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Gecko animatable, @Nullable GeoRenderer<Gecko> renderer) {
        return switch (animatable.getVariant()) {
            case 1 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/gecko/green_gecko.png");
            case 2 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/gecko/tokay_gecko.png");
            case 3 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/gecko/electric_blue_day_gecko.png");
            case 4 -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/gecko/yellow_headed_gecko.png");
            default -> ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/gecko/leopard_gecko.png");
        };
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Gecko animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Gecko animatable) {
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/gecko/gecko.animation.json");
    }

    @Override
    public void setCustomAnimations(Gecko animatable, long instanceId, AnimationState<Gecko> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        if (animationState != null) {
            EntityModelData extraDataOfType = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            GeoBone head = this.getAnimationProcessor().getBone("head");
            GeoBone root = this.getAnimationProcessor().getBone("gecko");
            if (root != null) {
                if (animatable.isBaby()) {
                    root.setScaleX(0.4F);
                    root.setScaleY(0.4F);
                    root.setScaleZ(0.4F);
                    if (head != null) {
                        head.setScaleX(1.75F);
                        head.setScaleY(1.75F);
                        head.setScaleZ(1.75F);
                    }
                } else {
                    root.setScaleX(1.0F);
                    root.setScaleY(1.0F);
                    root.setScaleZ(1.0F);
                    if (head != null) {
                        head.setScaleX(1.0F);
                        head.setScaleY(1.0F);
                        head.setScaleZ(1.0F);
                    }
                }
            }

            if (head != null && extraDataOfType != null && !animatable.isImmobile()) {
                head.setRotY(extraDataOfType.netHeadYaw() * ((float) Math.PI / 180F));
                head.setRotX(extraDataOfType.headPitch() * ((float) Math.PI / 180F));
            }
        }
    }
}
