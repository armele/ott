package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.Jellyfish2Entity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import software.bernie.geckolib.model.GeoModel;

public class Jellyfish2Model extends GeoModel<Jellyfish2Entity> {
    @Override
    public ResourceLocation getModelResource(Jellyfish2Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/jellyfish/jellyfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Jellyfish2Entity animatable) {
        String color = DyeColor.byId(animatable.getColor()).getName();
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/jellyfish/jellyfish_" + color + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(Jellyfish2Entity animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/jellyfish/jellyfish.animation.json");
    }
}
