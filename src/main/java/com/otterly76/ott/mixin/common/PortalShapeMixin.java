package com.otterly76.ott.mixin.common;

import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PortalShape.class)
public class PortalShapeMixin {

    @Redirect(method = {"getDistanceUntilEdgeAboveFrame", "hasTopFrame", "getDistanceUntilTop"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$StatePredicate;test(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)Z"))
    private boolean ott$isPortalFrame(BlockBehaviour.StatePredicate instance, BlockState state, BlockGetter level, BlockPos pos) {
        if (OttConfig.WORLDGEN.ALLOW_CUSTOM_PORTAL_FRAMES.get() && state.is(ModTags.Blocks.PORTAL_FRAME_BLOCKS)) {
            return true;
        }
        return instance.test(state, level, pos);
    }
}
