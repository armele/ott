package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CoyoteModel;
import com.otterly76.ott.entity.custom.CoyoteEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CoyoteRenderer extends GeoEntityRenderer<CoyoteEntity> {
    public CoyoteRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CoyoteModel());
    }
}