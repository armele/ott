package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.ShrimpModel;
import com.otterly76.ott.entity.custom.ShrimpEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ShrimpRenderer extends GeoEntityRenderer<ShrimpEntity> {
    public ShrimpRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ShrimpModel());
    }
}