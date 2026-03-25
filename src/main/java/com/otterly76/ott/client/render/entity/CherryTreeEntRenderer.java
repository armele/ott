package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CherryTreeEntModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.CherryTreeEnt;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CherryTreeEntRenderer extends GeoEntityRenderer<CherryTreeEnt> {
    public CherryTreeEntRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CherryTreeEntModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}