package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BabyPhoenixModel;
import com.otterly76.ott.entity.custom.BabyPhoenix;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BabyPhoenixRenderer extends GeoEntityRenderer<BabyPhoenix> {
    public BabyPhoenixRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BabyPhoenixModel());
    }
}