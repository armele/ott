package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GuineaFowlModel;
import com.otterly76.ott.entity.custom.GuineaFowlEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GuineaFowlRenderer extends GeoEntityRenderer<GuineaFowlEntity> {
    public GuineaFowlRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GuineaFowlModel());
    }
}
