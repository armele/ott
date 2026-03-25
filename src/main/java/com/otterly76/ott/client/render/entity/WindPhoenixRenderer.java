package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.WindPhoenixModel;
import com.otterly76.ott.entity.custom.WindPhoenix;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WindPhoenixRenderer extends GeoEntityRenderer<WindPhoenix> {
    public WindPhoenixRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WindPhoenixModel());
    }
}