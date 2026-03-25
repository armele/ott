package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CoralSeaViperModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.CoralSeaViper;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CoralSeaViperRenderer extends GeoEntityRenderer<CoralSeaViper> {
    public CoralSeaViperRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CoralSeaViperModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}