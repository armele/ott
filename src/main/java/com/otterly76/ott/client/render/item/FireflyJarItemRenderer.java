package com.otterly76.ott.client.render.item;

import com.otterly76.ott.client.model.FireflyJarItemModel;
import com.otterly76.ott.item.custom.FireflyJarItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class FireflyJarItemRenderer extends GeoItemRenderer<FireflyJarItem> {
    public FireflyJarItemRenderer() {
        super(new FireflyJarItemModel());
    }
}