package com.otterly76.ott.client.registries;

import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

import static com.otterly76.ott.Constants.MOD_ID;

public class ModModelLayers {
    public static final ModelLayerLocation CREAKING = register("creaking");
    public static final ModelLayerLocation PALE_OAK_BOAT = register("pale_oak_boat");
    public static final ModelLayerLocation PALE_OAK_CHEST_BOAT = register("pale_oak_chest_boat");
    public static final ModelLayerLocation COLD_PIG = register("cold_pig");
    public static final ModelLayerLocation COLD_CHICKEN = register("cold_chicken");
    public static final ModelLayerLocation COLD_COW = register("cold_cow");
    public static final ModelLayerLocation WARM_COW = register("warm_cow");
    public static final ModelLayerLocation HAPPY_GHAST = register("happy_ghast");
    public static final ModelLayerLocation HAPPY_GHAST_HARNESS = register("happy_ghast", "harness");
    public static final ModelLayerLocation HAPPY_GHAST_ROPES = register("happy_ghast", "ropes");

    public static final ModelLayerLocation OTT_WOOD_SET_BOAT = register("wood_set_boat");
    public static final ModelLayerLocation OTT_WOOD_SET_CHEST_BOAT = register("wood_set_chest_boat");

    private static ModelLayerLocation register(String name) {
        return register(name, "main");
    }

    private static ModelLayerLocation register(String name, String layer) {
        return new ModelLayerLocation(ResourceLocation.withDefaultNamespace(name), layer);
    }
}
