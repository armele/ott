package com.otterly76.ott.entity.client;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import static com.otterly76.ott.Constants.MOD_ID;

public class ModModelLayers {
    public static final ModelLayerLocation CREAKING = registerLayer("creaking");
    public static final ModelLayerLocation PALE_OAK_BOAT = registerLayer("pale_oak_boat");
    public static final ModelLayerLocation PALE_OAK_CHEST_BOAT = registerLayer("pale_oak_chest_boat");

    public static final ModelLayerLocation OTT_WOOD_SET_BOAT = registerOttLayer("wood_set_boat");
    public static final ModelLayerLocation OTT_WOOD_SET_CHEST_BOAT = registerOttLayer("wood_set_chest_boat");

    private static ModelLayerLocation registerLayer(String name) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath("minecraft", name), "main");
    }

    private static ModelLayerLocation registerOttLayer(String name) {
        return new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(MOD_ID, name), "main");
    }
}