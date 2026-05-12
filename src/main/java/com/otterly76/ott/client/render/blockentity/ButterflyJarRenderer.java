package com.otterly76.ott.client.render.blockentity;

import com.otterly76.ott.block.entity.ButterflyJarBlockEntity;
import com.otterly76.ott.client.model.ButterflyJarModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ButterflyJarRenderer extends GeoBlockRenderer<ButterflyJarBlockEntity> {
    public ButterflyJarRenderer(BlockEntityRendererProvider.Context context) {
        super(new ButterflyJarModel());
    }
}
