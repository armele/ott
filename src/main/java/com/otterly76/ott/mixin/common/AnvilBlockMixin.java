package com.otterly76.ott.mixin.common;

import com.otterly76.ott.block.entity.AnvilBlockEntity;
import com.otterly76.ott.block.entity.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.world.level.block.Block;

@Mixin(value = AnvilBlock.class, priority = 2000)
public abstract class AnvilBlockMixin extends FallingBlock implements EntityBlock {
    public AnvilBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private static void ott$damage(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        for (Map.Entry<String, Supplier<? extends Block>> entry : com.otterly76.ott.block.ModBlocks.COPPER_ANVILS.entrySet()) {
            if (state.is(entry.getValue().get())) {
                String key = entry.getKey();
                String newKey = null;
                if (!key.contains("chipped_") && !key.contains("damaged_")) {
                    // Normal -> Chipped
                    if (key.startsWith("waxed_")) {
                        newKey = "waxed_chipped_" + key.substring("waxed_".length());
                    } else {
                        newKey = "chipped_" + key;
                    }
                } else if (key.contains("chipped_")) {
                    // Chipped -> Damaged
                    newKey = key.replace("chipped_", "damaged_");
                }

                if (newKey != null) {
                    Block newBlock = com.otterly76.ott.block.ModBlocks.COPPER_ANVILS.get(newKey).get();
                    cir.setReturnValue(newBlock.defaultBlockState().setValue(AnvilBlock.FACING, state.getValue(AnvilBlock.FACING)));
                } else {
                    cir.setReturnValue(null);
                }
                return;
            }
        }
    }

    @Inject(method = "useWithoutItem", at = @At("HEAD"), cancellable = true)
    protected void ott$useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (level.isClientSide) {
            cir.setReturnValue(InteractionResult.SUCCESS);
        } else {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof AnvilBlockEntity blockEntity) {
                player.openMenu(blockEntity);
                player.awardStat(Stats.INTERACT_WITH_ANVIL);
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModBlockEntities.ANVIL_BLOCK_ENTITY_TYPE.get().create(pos, state);
    }

    @Override
    public @Nullable MenuProvider getMenuProvider(@NotNull BlockState state, Level level, @NotNull BlockPos pos) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        return blockentity instanceof MenuProvider ? (MenuProvider)blockentity : null;
    }

    @Override
    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockentity = level.getBlockEntity(pos);
            if (blockentity instanceof AnvilBlockEntity) {
                Containers.dropContents(level, pos, (AnvilBlockEntity)blockentity);
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@NotNull BlockState blockState, Level level, @NotNull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }
}
