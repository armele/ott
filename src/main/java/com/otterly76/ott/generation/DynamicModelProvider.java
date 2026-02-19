package com.otterly76.ott.generation;

import com.otterly76.ott.handler.BlockConversionHandler;
import com.otterly76.ott.mixin.client.BlockModelGeneratorsAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DynamicModelProvider implements DataProvider {
    private final PackOutput.PathProvider blockStatePathProvider;

    public DynamicModelProvider(DataProviderContext context) {
        this.blockStatePathProvider = context.getPackOutput().createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput output) {
        CompletableFuture<?>[] futures = BlockConversionHandler.getBlockConversions().entrySet().stream()
                .map(entry -> {
                    Block oldBlock = entry.getKey();
                    Block newBlock = entry.getValue();
                    ResourceLocation modelLoc = ModelLocationUtils.getModelLocation(oldBlock);
                    MultiVariantGenerator generator = MultiVariantGenerator.multiVariant(newBlock, Variant.variant().with(VariantProperties.MODEL, modelLoc)).with(BlockModelGeneratorsAccessor.ott$invokeCreateHorizontalFacingDispatch());
                    return DataProvider.saveStable(output, generator.get(), this.blockStatePathProvider.json(BuiltInRegistries.BLOCK.getKey(newBlock)));
                })
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures);
    }

    @Override
    public @NotNull String getName() {
        return "Dynamic Anvil Model Provider";
    }
}
