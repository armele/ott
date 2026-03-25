package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GhostModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.Ghost;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GhostRenderer extends GeoEntityRenderer<Ghost> {
    public GhostRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GhostModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}