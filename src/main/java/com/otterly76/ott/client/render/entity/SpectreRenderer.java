package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SpectreModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.Spectre;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpectreRenderer extends GeoEntityRenderer<Spectre> {
    public SpectreRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpectreModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}