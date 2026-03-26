package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.PsychoJellyModel;
import com.otterly76.ott.entity.custom.PsychoJellyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class PsychoJellyRenderer extends GeoEntityRenderer<PsychoJellyEntity> {
    public PsychoJellyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PsychoJellyModel());
    }
}
