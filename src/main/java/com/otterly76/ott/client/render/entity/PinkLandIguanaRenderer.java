package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.PinkLandIguanaModel;
import com.otterly76.ott.entity.custom.PinkLandIguanaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PinkLandIguanaRenderer extends GeoEntityRenderer<PinkLandIguanaEntity> {
    public PinkLandIguanaRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PinkLandIguanaModel());
    }
}
