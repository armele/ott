package com.otterly76.ott.mixin.common;

import com.otterly76.ott.util.block.ModSkullType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SkullBlock.class)
public class SkullBlockMixin {

    @Inject(method = "getShape", at = @At("HEAD"), cancellable = true)
    private void ott$dragonHeadFullCube(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        SkullBlock.Type type = ((SkullBlock) (Object) this).getType();
        if (type == SkullBlock.Types.DRAGON || type == ModSkullType.DRAGON_SKULL) {
            cir.setReturnValue(Shapes.block());
        }
    }
}
