package com.otterly76.ott.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public abstract class FenceJumpMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void ott$allowPlayerToJump(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context, CallbackInfoReturnable<VoxelShape> cir) {
        Block block = state.getBlock();
        if (!(block instanceof FenceBlock || block instanceof FenceGateBlock || block instanceof WallBlock)) {
            return;
        }

        if (context instanceof EntityCollisionContext ecc) {
            Entity entity = ecc.getEntity();

            if (entity instanceof Player) {
                cir.setReturnValue(state.getShape(level, pos, context));
            }
        }
    }
}