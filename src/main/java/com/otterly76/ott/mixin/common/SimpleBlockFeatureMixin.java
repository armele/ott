package com.otterly76.ott.mixin.common;

import com.otterly76.ott.block.custom.EyeblossomBlock;
import com.otterly76.ott.block.custom.MossyCarpetBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.SimpleBlockFeature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SimpleBlockFeature.class})
public class SimpleBlockFeatureMixin {
    @Inject(
        method = {"place(Lnet/minecraft/world/level/levelgen/feature/FeaturePlaceContext;)Z"},
        at = {@At("HEAD")},
        cancellable = true
    )
    private void onPlace(FeaturePlaceContext<SimpleBlockConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        SimpleBlockConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockState state = config.toPlace().getState(context.random(), origin);
        if (state.canSurvive(level, origin)) {
            if (state.getBlock() instanceof MossyCarpetBlock) {
                MossyCarpetBlock.placeAt(level, origin, level.getRandom(), 2);
                cir.setReturnValue(true);
            }

            if (state.getBlock() instanceof EyeblossomBlock) {
                level.scheduleTick(origin, state.getBlock(), 1);
                level.setBlock(origin, state, 2);
                cir.setReturnValue(true);
            }
        }
    }
}
