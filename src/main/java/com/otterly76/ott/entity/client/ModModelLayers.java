package com.otterly76.ott.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    public static final ModelLayerLocation CREAKING = registerLayer("creaking");
    public static final ModelLayerLocation PALE_OAK_BOAT = registerLayer("pale_oak_boat");
    public static final ModelLayerLocation PALE_OAK_CHEST_BOAT = registerLayer("pale_oak_chest_boat");

    private static ModelLayerLocation registerLayer(String name) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("minecraft", name), "main");
    }
}