package com.otterly76.ott.mixin.common;


import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.handler.BlockConversionHandler;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({WorldGenRegion.class})
abstract class WorldGenRegionMixin {
    @ModifyVariable(
            method = {"setBlock"},
            at = @At(
                    value = "LOAD",
                    ordinal = 0
            ),
            argsOnly = true
    )
    public BlockState setBlock(BlockState blockState) {
        return OttConfig.ANVILS.CONVERT_VANILLA_ANVIL_DURING_WORLD_GEN.get() ? BlockConversionHandler.convertFromVanillaBlock(blockState) : blockState;
    }
}
