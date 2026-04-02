package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.MediumJellyfishEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;

public class MediumJellyfishModel extends GeoModel<MediumJellyfishEntity> {
    @Override
    public ResourceLocation getModelResource(MediumJellyfishEntity animatable) {
        return getModelResource(animatable, null);
    }

    @Override
    public ResourceLocation getModelResource(MediumJellyfishEntity animatable, @Nullable GeoRenderer<MediumJellyfishEntity> renderer) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/jellyfish/jellyfish_2.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(MediumJellyfishEntity animatable) {
        return getTextureResource(animatable, null);
    }

    @Override
    public ResourceLocation getTextureResource(MediumJellyfishEntity animatable, @Nullable GeoRenderer<MediumJellyfishEntity> renderer) {
        String color = DyeColor.byId(animatable.getColor()).getName();
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/jellyfish/jellyfish_2_" + color + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(MediumJellyfishEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/jellyfish/jellyfish2.animation.json");
    }
}
