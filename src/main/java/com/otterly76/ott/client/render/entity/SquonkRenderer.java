package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SquonkModel;
import com.otterly76.ott.entity.custom.SquonkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SquonkRenderer extends GeoEntityRenderer<SquonkEntity> {
    public SquonkRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SquonkModel());
    }
}