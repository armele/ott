package com.otterly76.ott.client.render.blockentity;

import com.otterly76.ott.block.entity.FireflyJarBlockEntity;
import com.otterly76.ott.client.model.FireflyJarModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FireflyJarRenderer extends GeoBlockRenderer<FireflyJarBlockEntity> {
    public FireflyJarRenderer(BlockEntityRendererProvider.Context context) {
        super(new FireflyJarModel());
    }
}