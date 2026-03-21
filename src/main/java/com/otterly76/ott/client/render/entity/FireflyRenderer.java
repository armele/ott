package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.FireflyModel;
import com.otterly76.ott.client.render.layers.FireflyGlowLayer;
import com.otterly76.ott.entity.custom.Firefly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FireflyRenderer extends GeoEntityRenderer<Firefly> {
    public FireflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FireflyModel<>());
        this.addRenderLayer(new FireflyGlowLayer(this));
    }
}
