package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.IceChunkModel;
import com.otterly76.ott.entity.custom.IceologerIceChunkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IceChunkRenderer extends GeoEntityRenderer<IceologerIceChunkEntity> {
    public IceChunkRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new IceChunkModel());
        this.shadowRadius = 1.0f;
    }
}
