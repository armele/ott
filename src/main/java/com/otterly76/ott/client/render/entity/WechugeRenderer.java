package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.WechugeModel;
import com.otterly76.ott.entity.custom.WechugeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WechugeRenderer extends GeoEntityRenderer<WechugeEntity> {
    public WechugeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WechugeModel());
    }
}