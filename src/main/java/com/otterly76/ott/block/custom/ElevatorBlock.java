package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.entity.ElevatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ElevatorBlock extends BaseEntityBlock {

    public static final MapCodec<ElevatorBlock> CODEC = simpleCodec(props -> new ElevatorBlock("white", props));

    private final String colorName;

    public ElevatorBlock(String colorName, Properties props) {
        super(props);
        this.colorName = colorName;
    }

    public String getColorName() {
        return colorName;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ElevatorBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull BlockState getAppearance(@NotNull BlockState state, @NotNull BlockAndTintGetter level,
            @NotNull BlockPos pos, @NotNull Direction side,
            @Nullable BlockState queryState, @Nullable BlockPos queryPos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof ElevatorBlockEntity elevator) {
            BlockState camo = elevator.getCamoState();
            if (camo != null && !camo.isAir()) {
                return camo.getAppearance(level, pos, side, queryState, queryPos);
            }
        }
        return super.getAppearance(state, level, pos, side, queryState, queryPos);
    }

    // Right-click with a block item → set camo
    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
                                                       @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player,
                                                       net.minecraft.world.@NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        // Return SUCCESS immediately on client to prevent any block placement prediction
        if (level.isClientSide) {
            return ItemInteractionResult.SUCCESS;
        }
        // Server-side: apply camo if holding a non-elevator block and not sneaking
        if (!player.isShiftKeyDown() && stack.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() != this) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ElevatorBlockEntity elevator) {
                elevator.setCamo(blockItem.getBlock().defaultBlockState());
                level.sendBlockUpdated(pos, state, state, 3);
            }
            return ItemInteractionResult.CONSUME;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    // Right-click without item → open GUI
    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
            @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ElevatorBlockEntity elevator) {
                serverPlayer.openMenu(elevator, buf -> buf.writeBlockPos(pos));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}