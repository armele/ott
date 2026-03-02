package com.otterly76.ott.generation;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DynamicModelProvider implements DataProvider {

    public DynamicModelProvider(DataProviderContext context) {
        PackOutput.PathProvider blockStatePathProvider = context.getPackOutput().createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        return CompletableFuture.completedFuture(null);
    }

    @Override
    public @NotNull String getName() {
        return "Dynamic Model Provider";
    }
}
