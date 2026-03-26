package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CichlidModel;
import com.otterly76.ott.entity.custom.CichlidEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CichlidRenderer extends GeoEntityRenderer<CichlidEntity> {
    public CichlidRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CichlidModel());
    }
}
