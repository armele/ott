package com.otterly76.ott;

public class Constants {
    public static final String MOD_ID = "ott";
    public static final String VERSION = "1.0.0";

    public static net.minecraft.resources.ResourceLocation loc(String path) {
        return net.minecraft.resources.ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
