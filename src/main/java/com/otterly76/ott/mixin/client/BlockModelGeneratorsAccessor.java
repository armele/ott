package com.otterly76.ott.mixin.client;

import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Consumer;

@Mixin(BlockModelGenerators.class)
public interface BlockModelGeneratorsAccessor {
    @Accessor("blockStateOutput")
    Consumer<BlockStateGenerator> ott$getBlockStateOutput();

    @Invoker("skipAutoItemBlock")
    void ott$invokeSkipAutoItemBlock(Block block);

    @Invoker("createHorizontalFacingDispatch")
    static PropertyDispatch ott$invokeCreateHorizontalFacingDispatch() {
        throw new UnsupportedOperationException();
    }
}
