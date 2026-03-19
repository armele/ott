package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.DeerModel;
import com.otterly76.ott.entity.custom.Deer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DeerRenderer extends GeoEntityRenderer<Deer> {
    public DeerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new DeerModel());
        this.shadowRadius = 0.6f;
    }
}