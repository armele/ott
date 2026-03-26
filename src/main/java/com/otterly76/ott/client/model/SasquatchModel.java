package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SasquatchEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class SasquatchModel extends GeoModel<SasquatchEntity> {
    @Override
    public ResourceLocation getModelResource(SasquatchEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/sasquatch/sasquatch.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SasquatchEntity animatable) {
        net.minecraft.client.player.LocalPlayer player = net.minecraft.client.Minecraft.getInstance().player;
        if (player != null && animatable.distanceToSqr(player) > 400.0D) {
            return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/sasquatch/sasquatch_hidden.png");
        }
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/sasquatch/sasquatch.png");
    }

    @Override
    public ResourceLocation getAnimationResource(SasquatchEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/sasquatch/sasquatch.animation.json");
    }
}