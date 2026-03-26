package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.SkinwalkerModel;
import com.otterly76.ott.entity.custom.SkinwalkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class SkinwalkerRenderer extends GeoEntityRenderer<SkinwalkerEntity> {
    public SkinwalkerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SkinwalkerModel());
    }
}