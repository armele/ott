package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.StingrayModel;
import com.otterly76.ott.entity.custom.StingrayEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StingrayRenderer extends GeoEntityRenderer<StingrayEntity> {
    public StingrayRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StingrayModel());
    }
}
