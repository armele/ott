package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.YetiModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.Yeti;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class YetiRenderer extends GeoEntityRenderer<Yeti> {
    public YetiRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new YetiModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}