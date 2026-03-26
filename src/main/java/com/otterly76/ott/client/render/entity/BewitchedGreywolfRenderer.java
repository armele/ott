package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.BewitchedGreywolfModel;
import com.otterly76.ott.entity.custom.BewitchedGreywolfEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BewitchedGreywolfRenderer extends GeoEntityRenderer<BewitchedGreywolfEntity> {
    public BewitchedGreywolfRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BewitchedGreywolfModel());
    }
}