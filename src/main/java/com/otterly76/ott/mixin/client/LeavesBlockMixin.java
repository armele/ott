package com.otterly76.ott.mixin.client;

import com.otterly76.ott.client.util.FallingLeavesModule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin({LeavesBlock.class})
public class LeavesBlockMixin {
    @Unique
    private final FallingLeavesModule module = new FallingLeavesModule();

    @Inject(
        method = {"animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V"},
        at = {@At("HEAD")}
    )
    public void vb$animateTick(BlockState state, Level level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        this.module.makeFallingLeavesParticles(level, pos, random, level.getBlockState(pos.below()), pos.below());
    }
}
