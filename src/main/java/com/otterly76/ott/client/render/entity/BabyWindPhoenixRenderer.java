package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BabyWindPhoenixModel;
import com.otterly76.ott.entity.custom.BabyWindPhoenix;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BabyWindPhoenixRenderer extends GeoEntityRenderer<BabyWindPhoenix> {
    public BabyWindPhoenixRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BabyWindPhoenixModel());
    }
}