package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.block.entity.WeatheringStationBlockEntity;
import com.otterly76.ott.handler.WeatheringHandler;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.item.custom.CopperBucketItem;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WeatheringStationBlock extends BaseEntityBlock {
    public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 3);
    protected static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    public static final MapCodec<WeatheringStationBlock> CODEC = simpleCodec(WeatheringStationBlock::new);

    public WeatheringStationBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(LEVEL, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LEVEL);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new WeatheringStationBlockEntity(pos, state);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof WeatheringStationBlockEntity station) {
            // Handle Fluids
            if (FluidUtil.getFluidHandler(stack).isPresent()) {
                ItemStack originalStack = stack.copy();
                if (FluidUtil.interactWithFluidHandler(player, hand, station.getWaterTank())) {
                    int newLevel = station.getWaterLevel();
                    if (state.getValue(LEVEL) != newLevel) {
                        level.setBlock(pos, state.setValue(LEVEL, newLevel), 3);
                    }
                    
                    // Fix copper bucket return if FluidUtil returned a vanilla bucket
                    ItemStack result = player.getItemInHand(hand);
                    if (result.is(Items.BUCKET) && originalStack.getItem() instanceof CopperBucketItem) {
                        player.setItemInHand(hand, ItemUtils.createFilledResult(originalStack, player, new ItemStack(ModItems.COPPER_BUCKET.get())));
                    } else if (result.is(Items.WATER_BUCKET) && originalStack.is(ModItems.COPPER_BUCKET.get())) {
                        player.setItemInHand(hand, ItemUtils.createFilledResult(originalStack, player, new ItemStack(ModItems.COPPER_WATER_BUCKET.get())));
                    }
                    
                    return ItemInteractionResult.SUCCESS;
                }
                // If it's a fluid handler (like a bucket), we should still consume the action to prevent default behavior
                // (like placing the fluid on top of the station)
                return ItemInteractionResult.SUCCESS;
            }

            // Handle Items
            ItemStack heldItem = player.getItemInHand(hand);
            if (!heldItem.isEmpty()) {
                if (WeatheringHandler.getNextItem(heldItem).isPresent()) {
                    for (int i = 0; i < station.getInventory().getSlots(); i++) {
                        if (station.getInventory().getStackInSlot(i).isEmpty()) {
                            if (!level.isClientSide) {
                                station.getInventory().setStackInSlot(i, heldItem.split(1));
                                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
                            }
                            return ItemInteractionResult.SUCCESS;
                        }
                    }
                }
                // Consume interaction if holding any item to prevent accidental extraction from useWithoutItem
                return ItemInteractionResult.SUCCESS;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof WeatheringStationBlockEntity station) {
            // Extract from the last non-empty slot
            for (int i = station.getInventory().getSlots() - 1; i >= 0; i--) {
                if (!station.getInventory().getStackInSlot(i).isEmpty()) {
                    if (!level.isClientSide) {
                        ItemStack extracted = station.getInventory().extractItem(i, 64, false);
                        if (!player.getInventory().add(extracted)) {
                            player.drop(extracted, false);
                        }
                        level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0f, 1.0f);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof WeatheringStationBlockEntity station) {
                for (int i = 0; i < station.getInventory().getSlots(); i++) {
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), station.getInventory().getStackInSlot(i));
                }
            }
            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.WEATHERING_STATION.get(), WeatheringStationBlockEntity::tick);
    }
}
