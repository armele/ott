package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.QuailModel;
import com.otterly76.ott.entity.custom.QuailEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class QuailRenderer extends GeoEntityRenderer<QuailEntity> {
    public QuailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new QuailModel());
    }
}
