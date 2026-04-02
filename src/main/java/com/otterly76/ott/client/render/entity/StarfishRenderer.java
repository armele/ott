package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.StarfishModel;
import com.otterly76.ott.entity.custom.StarfishEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class StarfishRenderer extends GeoEntityRenderer<StarfishEntity> {
    public StarfishRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StarfishModel());
    }
}
