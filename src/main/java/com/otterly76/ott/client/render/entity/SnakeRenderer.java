package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SnakeModel;
import com.otterly76.ott.entity.custom.SnakeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SnakeRenderer extends GeoEntityRenderer<SnakeEntity> {
    public SnakeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnakeModel());
    }
}