package com.otterly76.ott.mixin.common;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(LayeredCauldronBlock.class)
public class LayeredCauldronBlockMixin {

    @Inject(method = "lowerFillLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void ott$lowerCopperFillLevel(BlockState state, Level level, BlockPos pos, CallbackInfo ci, int levelValue, BlockState newState) {
        // If the old state was a copper water cauldron or copper powder snow cauldron, and the new state is vanilla iron cauldron
        if (newState.is(Blocks.CAULDRON)) {
            net.minecraft.world.level.block.Block currentBlock = state.getBlock();
            net.minecraft.world.level.block.Block empty = null;

            // Check if it's in Water map
            for (var entry : ModBlocks.COPPER_WATER_CAULDRONS.entrySet()) {
                if (entry.getValue().get() == currentBlock) {
                    empty = ModBlocks.COPPER_CAULDRONS.get(entry.getKey()).get();
                    break;
                }
            }

            if (empty == null) {
                // Check if it's in Powder Snow map
                for (var entry : ModBlocks.COPPER_POWDER_SNOW_CAULDRONS.entrySet()) {
                    if (entry.getValue().get() == currentBlock) {
                        empty = ModBlocks.COPPER_CAULDRONS.get(entry.getKey()).get();
                        break;
                    }
                }
            }

            if (empty != null) {
                level.setBlockAndUpdate(pos, empty.defaultBlockState());
                ci.cancel();
            }
        }
    }
}