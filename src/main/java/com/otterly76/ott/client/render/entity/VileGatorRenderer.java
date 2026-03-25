package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.VileGatorModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.VileGator;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class VileGatorRenderer extends GeoEntityRenderer<VileGator> {
    public VileGatorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VileGatorModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}