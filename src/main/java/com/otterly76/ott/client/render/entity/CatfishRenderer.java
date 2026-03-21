package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.CatfishModel;
import com.otterly76.ott.entity.custom.Catfish;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CatfishRenderer extends GeoEntityRenderer<Catfish> {
    public CatfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CatfishModel());
    }
}
