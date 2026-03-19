package com.otterly76.ott.client.model;

import com.otterly76.ott.entity.custom.Bird;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class BirdModel extends GeoModel<Bird> {
    @Override
    public ResourceLocation getModelResource(Bird animatable, @Nullable GeoRenderer<Bird> renderer) {
        String name = BuiltInRegistries.ENTITY_TYPE.getKey(animatable.getType()).getPath();
        return ResourceLocation.fromNamespaceAndPath("ott", "geo/entity/" + name + "/" + name + ".geo.json");
    }

    @Override
    @Deprecated
    public ResourceLocation getModelResource(Bird animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(Bird animatable, @Nullable GeoRenderer<Bird> renderer) {
        String name = BuiltInRegistries.ENTITY_TYPE.getKey(animatable.getType()).getPath();
        return ResourceLocation.fromNamespaceAndPath("ott", "textures/entity/" + name + "/" + name + ".png");
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureResource(Bird animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getAnimationResource(Bird animatable) {
        String name = BuiltInRegistries.ENTITY_TYPE.getKey(animatable.getType()).getPath();
        return ResourceLocation.fromNamespaceAndPath("ott", "animations/entity/" + name + "/" + name + ".animation.json");
    }
}