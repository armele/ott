package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.HowlerModel;
import com.otterly76.ott.entity.custom.HowlerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class HowlerRenderer extends GeoEntityRenderer<HowlerEntity> {
    public HowlerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new HowlerModel());
    }
}