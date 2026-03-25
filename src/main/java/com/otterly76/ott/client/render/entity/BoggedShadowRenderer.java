package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BoggedShadowModel;
import com.otterly76.ott.client.render.layers.LivingEntityEmissiveGeoLayer;
import com.otterly76.ott.entity.custom.BoggedShadow;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BoggedShadowRenderer extends GeoEntityRenderer<BoggedShadow> {
    public BoggedShadowRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BoggedShadowModel());
        this.addRenderLayer(new LivingEntityEmissiveGeoLayer<>(this));
    }
}