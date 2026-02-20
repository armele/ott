package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.config.OttConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@FunctionalInterface
public interface RenderConditions {
    RenderConditions DEFAULT = () -> true;
    RenderConditions FARM_ANIMALS = OttConfig.GENERAL.HAS_FARM_ANIMAL_VARIANTS::get;
    RenderConditions SHEEP_UNDERCOAT = OttConfig.GENERAL.USE_SHEEP_WOOL_UNDERCOAT::get;

    boolean apply();
}