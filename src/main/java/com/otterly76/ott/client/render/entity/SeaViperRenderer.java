package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SeaViperModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.SeaViper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SeaViperRenderer extends GeoEntityRenderer<SeaViper> {
    public SeaViperRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SeaViperModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}