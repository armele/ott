package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SpoonbillModel;
import com.otterly76.ott.entity.custom.SpoonbillEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SpoonbillRenderer extends GeoEntityRenderer<SpoonbillEntity> {
    public SpoonbillRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SpoonbillModel());
    }
}
