package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.MaulerModel;
import com.otterly76.ott.entity.custom.MaulerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class MaulerRenderer extends GeoEntityRenderer<MaulerEntity> {
    public MaulerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MaulerModel());
        this.shadowRadius = 0.3f;
    }
}
