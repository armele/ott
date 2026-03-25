package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.WindPhoenix;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WindPhoenixModel extends GeoModel<WindPhoenix> {
    @Override
    public ResourceLocation getModelResource(WindPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "geo/entity/wind_phoenix/wind_phoenix.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WindPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "textures/entity/wind_phoenix/wind_phoenix.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WindPhoenix animatable) {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "animations/entity/wind_phoenix/wind_phoenix.animation.json");
    }
}