package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GeistModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.Geist;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
public class GeistRenderer extends GeoEntityRenderer<Geist> {
    public GeistRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GeistModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}