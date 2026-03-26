package com.otterly76.ott.client.render.entity;

import com.otterly76.ott.client.model.TreeKangarooModel;
import com.otterly76.ott.entity.custom.TreeKangarooEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TreeKangarooRenderer extends GeoEntityRenderer<TreeKangarooEntity> {
    public TreeKangarooRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TreeKangarooModel());
    }
}
