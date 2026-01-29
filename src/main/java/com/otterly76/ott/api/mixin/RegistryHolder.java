package com.otterly76.ott.api.mixin;


import net.minecraft.core.RegistryAccess;

public interface RegistryHolder {
    RegistryAccess ott$getRegistries();

    void ott$setRegistries(RegistryAccess registryAccess);
}
