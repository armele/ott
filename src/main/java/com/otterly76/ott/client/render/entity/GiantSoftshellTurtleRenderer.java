package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.GiantSoftshellTurtleModel;
import com.otterly76.ott.entity.custom.GiantSoftshellTurtleEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GiantSoftshellTurtleRenderer extends GeoEntityRenderer<GiantSoftshellTurtleEntity> {
    public GiantSoftshellTurtleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GiantSoftshellTurtleModel());
    }
}
