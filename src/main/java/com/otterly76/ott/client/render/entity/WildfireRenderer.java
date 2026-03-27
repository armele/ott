package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.WildfireModel;
import com.otterly76.ott.entity.custom.WildfireEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WildfireRenderer extends GeoEntityRenderer<WildfireEntity> {
    public WildfireRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WildfireModel());
        this.shadowRadius = 0.8f;
    }
}
