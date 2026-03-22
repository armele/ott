package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Jellyfish3Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class Jellyfish3Model extends GeoModel<Jellyfish3Entity> {
    @Override
    public ResourceLocation getModelResource(Jellyfish3Entity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(Jellyfish3Entity animatable, @Nullable GeoRenderer<Jellyfish3Entity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/jellyfish/jellyfish_2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Jellyfish3Entity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Jellyfish3Entity animatable, @Nullable GeoRenderer<Jellyfish3Entity> renderer) {
        String color = DyeColor.byId(animatable.getColor()).getName();
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/jellyfish/jellyfish_2_" + color + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Jellyfish3Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/jellyfish/jellyfish2.animation.json");
    }
}