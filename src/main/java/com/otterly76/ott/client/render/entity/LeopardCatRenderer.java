package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.LeopardCatModel;
import com.otterly76.ott.entity.custom.LeopardCatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LeopardCatRenderer extends GeoEntityRenderer<LeopardCatEntity> {
    public LeopardCatRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LeopardCatModel());
    }
}
