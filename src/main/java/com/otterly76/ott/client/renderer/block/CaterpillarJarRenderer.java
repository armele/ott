package com.otterly76.ott.client.renderer.block;

import com.otterly76.ott.block.entity.CaterpillarJarBlockEntity;
import com.otterly76.ott.client.model.CaterpillarJarModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class CaterpillarJarRenderer extends GeoBlockRenderer<CaterpillarJarBlockEntity> {
    public CaterpillarJarRenderer(BlockEntityRendererProvider.Context context) {
        super(new CaterpillarJarModel());
    }
}