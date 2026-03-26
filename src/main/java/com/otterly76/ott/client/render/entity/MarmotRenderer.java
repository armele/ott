package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.MarmotModel;
import com.otterly76.ott.entity.custom.MarmotEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MarmotRenderer extends GeoEntityRenderer<MarmotEntity> {
    public MarmotRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MarmotModel());
    }
}