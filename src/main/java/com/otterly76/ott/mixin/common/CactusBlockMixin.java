package com.otterly76.ott.mixin.common;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CactusBlock.class})
public abstract class CactusBlockMixin extends Block {
    @Shadow
    @Final
    public static IntegerProperty AGE;

    @Shadow
    protected abstract boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos);

    public CactusBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Inject(
        method = {"randomTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"},
        at = {@At("HEAD")}
    )
    private void vb$growCactusFlower(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        BlockPos above = pos.above();
        if (level.isEmptyBlock(above)) {
            int height = 1;
            int age = state.getValue(AGE);

            while(level.getBlockState(pos.below(height)).is(this)) {
                if (height++ == 3 && age == 15) {
                    return;
                }
            }

            if (age == 8 && this.canSurvive(this.defaultBlockState(), level, above)) {
                double flowerChance = height >= 3 ? 0.25 : 0.1;
                if (random.nextDouble() <= flowerChance) {
                    level.setBlockAndUpdate(above, ModBlocks.CACTUS_FLOWER.get().defaultBlockState());
                }
            }
        }

    }
}