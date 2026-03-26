package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.ChupacabraModel;
import com.otterly76.ott.entity.custom.ChupacabraEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ChupacabraRenderer extends GeoEntityRenderer<ChupacabraEntity> {
    public ChupacabraRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ChupacabraModel());
    }
}