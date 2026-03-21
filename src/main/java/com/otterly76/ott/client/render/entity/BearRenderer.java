package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BearModel;
import com.otterly76.ott.entity.custom.Bear;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BearRenderer extends GeoEntityRenderer<Bear> {
    public BearRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BearModel());
        this.shadowRadius = 0.7f;
    }
}
