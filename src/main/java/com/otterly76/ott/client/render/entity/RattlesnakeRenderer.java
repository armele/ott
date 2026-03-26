package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.RattlesnakeModel;
import com.otterly76.ott.entity.custom.RattlesnakeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RattlesnakeRenderer extends GeoEntityRenderer<RattlesnakeEntity> {
    public RattlesnakeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RattlesnakeModel());
    }
}