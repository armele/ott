package com.otterly76.ott.block;

import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.block.entity.AnvilBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnvilWithInventoryBlock extends AnvilBlock implements EntityBlock {
    private final Block block;

    public AnvilWithInventoryBlock(Block block) {
        super(Properties.ofFullCopy(block));
        this.block = block;
    }

    public @NotNull String getDescriptionId() {
        return this.block.getDescriptionId();
    }

    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (level.getBlockEntity(pos) instanceof AnvilBlockEntity blockEntity) {
                player.openMenu(blockEntity);
                player.awardStat(Stats.INTERACT_WITH_ANVIL);
            }

            return InteractionResult.CONSUME;
        }
    }

    public @Nullable MenuProvider getMenuProvider(@NotNull BlockState state, Level level, @NotNull BlockPos pos) {
        return level.getBlockEntity(pos) instanceof MenuProvider menuProvider ? menuProvider : null;
    }

    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return ModBlockEntities.ANVIL_BLOCK_ENTITY_TYPE.get().create(pos, state);
    }

    public void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        Containers.dropContentsOnDestroy(state, newState, level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    public boolean hasAnalogOutputSignal(@NotNull BlockState blockState) {
        return true;
    }

    public int getAnalogOutputSignal(@NotNull BlockState blockState, Level level, @NotNull BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
    }
}