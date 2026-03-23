package com.otterly76.ott.client.renderer.item;

import com.otterly76.ott.client.model.CaterpillarJarItemModel;
import com.otterly76.ott.item.custom.CaterpillarJarItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class CaterpillarJarItemRenderer extends GeoItemRenderer<CaterpillarJarItem> {
    public CaterpillarJarItemRenderer() {
        super(new CaterpillarJarItemModel());
    }
}