package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.PhoenixModel;
import com.otterly76.ott.entity.custom.Phoenix;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PhoenixRenderer extends GeoEntityRenderer<Phoenix> {
    public PhoenixRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PhoenixModel());
    }
}