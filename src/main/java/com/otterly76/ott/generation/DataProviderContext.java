package com.otterly76.ott.generation;

import com.google.common.base.Suppliers;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.VanillaRegistries;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public class DataProviderContext {
    private final String modId;
    private final PackOutput packOutput;
    private final Supplier<CompletableFuture<HolderLookup.Provider>> registries;

    public DataProviderContext(String modId, PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
        this(modId, packOutput, () -> registries);
    }

    private DataProviderContext(String modId, PackOutput packOutput, Supplier<CompletableFuture<HolderLookup.Provider>> registries) {
        this.modId = modId;
        this.packOutput = packOutput;
        this.registries = registries;
    }

    public static DataProviderContext fromModId(String modId) {
        return fromModId(modId, Path.of(""));
    }

    public static DataProviderContext fromModId(String modId, Path path) {
        return new DataProviderContext(modId, new PackOutput(path), Suppliers.memoize(() -> CompletableFuture.supplyAsync(VanillaRegistries::createLookup, Util.backgroundExecutor())));
    }

    public String getModId() {
        return this.modId;
    }

    public PackOutput getPackOutput() {
        return this.packOutput;
    }

    public CompletableFuture<HolderLookup.Provider> getRegistries() {
        return this.registries.get();
    }

    public DataProviderContext withRegistries(CompletableFuture<HolderLookup.Provider> registries) {
        return new DataProviderContext(this.modId, this.packOutput, registries);
    }

    @FunctionalInterface
    public interface Factory extends Function<DataProviderContext, DataProvider> {
    }
}
