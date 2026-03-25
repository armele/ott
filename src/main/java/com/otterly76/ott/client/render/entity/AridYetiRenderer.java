package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.AridYetiModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.AridYeti;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AridYetiRenderer extends GeoEntityRenderer<AridYeti> {
    public AridYetiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AridYetiModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}