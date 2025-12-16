package com.otterly76.ott.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.FenceGateBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FenceGateBlock.class)
public abstract class FenceGateBlockMixin {

    @Inject(method = "getCollisionShape", at = @At("HEAD"), cancellable = true)
    private void ott$openGateCollidesForAnimals(BlockState state, BlockGetter level, BlockPos pos,
                                                CollisionContext context,
                                                CallbackInfoReturnable<VoxelShape> cir) {
        if (!state.getValue(FenceGateBlock.OPEN)) return;

        if (context instanceof EntityCollisionContext ecc) {
            Entity e = ecc.getEntity();
            if (e instanceof Animal) {
                // Open for players/NPCs, blocked for animals.
                cir.setReturnValue(Shapes.block());
            }
        }
    }
}