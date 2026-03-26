package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CougarModel;
import com.otterly76.ott.entity.custom.CougarEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CougarRenderer extends GeoEntityRenderer<CougarEntity> {
    public CougarRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CougarModel());
    }
}