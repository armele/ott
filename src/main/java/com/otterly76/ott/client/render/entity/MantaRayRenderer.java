package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.MantaRayModel;
import com.otterly76.ott.entity.custom.MantaRayEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MantaRayRenderer extends GeoEntityRenderer<MantaRayEntity> {
    public MantaRayRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MantaRayModel());
    }
}
