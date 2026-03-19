package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.ButterflyModel;
import com.otterly76.ott.entity.custom.Butterfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ButterflyRenderer extends GeoEntityRenderer<Butterfly> {
    public ButterflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ButterflyModel<>());
    }
}