package com.otterly76.ott.generation;

import net.minecraft.core.HolderLookup;

import java.util.concurrent.CompletableFuture;

public interface RegistriesDataProvider {
    CompletableFuture<HolderLookup.Provider> getRegistries();
}