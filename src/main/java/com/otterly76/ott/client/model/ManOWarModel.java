package com.otterly76.ott.client.model;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.custom.ManOWar;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ManOWarModel<T extends ManOWar> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T manOWar) {
        return Constants.loc("geo/entity/man_o_war/man_o_war.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T manOWar) {
        return Constants.loc("textures/entity/manowar/" + manOWar.getColor().getSerializedName() + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T manOWar) {
        return Constants.loc("animations/entity/man_o_war.animation.json");
    }
}