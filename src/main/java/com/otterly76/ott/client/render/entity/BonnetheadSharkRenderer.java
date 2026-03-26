package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BonnetheadSharkModel;
import com.otterly76.ott.entity.custom.BonnetheadSharkEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BonnetheadSharkRenderer extends GeoEntityRenderer<BonnetheadSharkEntity> {
    public BonnetheadSharkRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BonnetheadSharkModel());
    }
}
