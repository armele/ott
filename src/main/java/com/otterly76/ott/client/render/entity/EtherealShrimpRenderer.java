package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.EtherealShrimpModel;
import com.otterly76.ott.entity.custom.EtherealShrimpEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EtherealShrimpRenderer extends GeoEntityRenderer<EtherealShrimpEntity> {
    public EtherealShrimpRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EtherealShrimpModel());
    }
}
