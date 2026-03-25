package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.HauntModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.Haunt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HauntRenderer extends GeoEntityRenderer<Haunt> {
    public HauntRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HauntModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}