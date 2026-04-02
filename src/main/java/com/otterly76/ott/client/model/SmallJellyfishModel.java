package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.SmallJellyfishEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class SmallJellyfishModel extends GeoModel<SmallJellyfishEntity> {
    @Override
    public ResourceLocation getModelResource(SmallJellyfishEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(SmallJellyfishEntity animatable, @Nullable GeoRenderer<SmallJellyfishEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/jellyfish/jellyfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(SmallJellyfishEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(SmallJellyfishEntity animatable, @Nullable GeoRenderer<SmallJellyfishEntity> renderer) {
        String color = DyeColor.byId(animatable.getColor()).getName();
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/jellyfish/jellyfish_" + color + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(SmallJellyfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/jellyfish/jellyfish.animation.json");
    }
}
