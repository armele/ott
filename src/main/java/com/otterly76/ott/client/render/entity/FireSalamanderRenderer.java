package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.FireSalamanderModel;
import com.otterly76.ott.entity.custom.FireSalamanderEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FireSalamanderRenderer extends GeoEntityRenderer<FireSalamanderEntity> {
    public FireSalamanderRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FireSalamanderModel());
    }
}
