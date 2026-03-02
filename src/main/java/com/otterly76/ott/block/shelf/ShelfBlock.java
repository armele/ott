package com.otterly76.ott.block.shelf;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.OptionalInt;

public class ShelfBlock extends BaseEntityBlock implements SelectableSlotContainer, SideChainPartBlock, SimpleWaterloggedBlock {
    public static final MapCodec<ShelfBlock> CODEC = simpleCodec(ShelfBlock::new);
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<SideChainPart> SIDE_CHAIN_PART = EnumProperty.create("side_chain_part", SideChainPart.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ShelfBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POWERED, false)
                .setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWERED, SIDE_CHAIN_PART, WATERLOGGED);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case NORTH -> Block.box(0, 12, 11, 16, 14, 16);
            case SOUTH -> Block.box(0, 12, 0, 16, 14, 5);
            case WEST -> Block.box(11, 12, 0, 16, 14, 16);
            case EAST -> Block.box(0, 12, 0, 5, 14, 16);
            default -> Block.box(0, 12, 0, 16, 14, 16);
        };
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ShelfBlockEntity(pos, state);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    public @NotNull BlockState rotate(@NotNull BlockState state, @NotNull Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(@NotNull BlockState state, @NotNull Mirror mirror) {
        return this.rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull OptionalInt getHitSlot(@NotNull BlockHitResult hitResult, @NotNull Direction direction) {
        Direction hitFace = hitResult.getDirection();
        if (hitFace == direction.getOpposite()) {
            return OptionalInt.empty();
        }
        
        Vec3 vec3 = hitResult.getLocation().subtract(Vec3.atLowerCornerOf(hitResult.getBlockPos()));
        double x = vec3.x;
        double y = vec3.y;
        double z = vec3.z;
        
        // Project to the shelf's plane
        double horizontalPos;
        if (direction.getAxis() == Direction.Axis.Z) {
            horizontalPos = direction == Direction.NORTH ? 1.0 - x : x;
        } else {
            horizontalPos = direction == Direction.EAST ? 1.0 - z : z;
        }
        
        int slot = (int)(horizontalPos * 3);
        return OptionalInt.of(Math.max(0, Math.min(2, slot)));
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof ShelfBlockEntity shelf) {
            OptionalInt optionalint = this.getHitSlot(hitResult, state.getValue(FACING));
            if (optionalint.isPresent()) {
                int slot = optionalint.getAsInt();
                ItemStack shelfStack = shelf.getItem(slot);
                if (shelfStack.isEmpty()) {
                    if (!stack.isEmpty()) {
                        if (!level.isClientSide) {
                            shelf.setItem(slot, stack.split(1));
                        }
                        return ItemInteractionResult.sidedSuccess(level.isClientSide);
                    }
                } else {
                    if (stack.isEmpty()) {
                        if (!level.isClientSide) {
                            ItemStack extracted = shelf.removeItem(slot, 1);
                            if (!player.getInventory().add(extracted)) {
                                player.drop(extracted, false);
                            }
                        }
                        return ItemInteractionResult.sidedSuccess(level.isClientSide);
                    }
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (blockentity instanceof ShelfBlockEntity shelf) {
            OptionalInt optionalint = this.getHitSlot(hitResult, state.getValue(FACING));
            if (optionalint.isPresent()) {
                int slot = optionalint.getAsInt();
                ItemStack shelfStack = shelf.getItem(slot);
                if (!shelfStack.isEmpty()) {
                    if (!level.isClientSide) {
                        ItemStack extracted = shelf.removeItem(slot, 1);
                        if (!player.getInventory().add(extracted)) {
                            player.drop(extracted, false);
                        }
                    }
                    return InteractionResult.sidedSuccess(level.isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(@NotNull BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        
        if (state.getValue(POWERED)) {
            Direction facing = state.getValue(FACING);
            Direction leftDir = facing.getCounterClockWise();
            Direction rightDir = facing.getClockWise();
            
            boolean hasLeft = isPoweredShelf(level, pos.relative(leftDir), facing);
            boolean hasRight = isPoweredShelf(level, pos.relative(rightDir), facing);
            
            SideChainPart part = SideChainPart.UNCONNECTED;
            if (hasLeft && hasRight) part = SideChainPart.CENTER;
            else if (hasLeft) part = SideChainPart.LEFT;
            else if (hasRight) part = SideChainPart.RIGHT;
            
            return state.setValue(SIDE_CHAIN_PART, part);
        }
        
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    private boolean isPoweredShelf(LevelAccessor level, BlockPos pos, Direction facing) {
        BlockState state = level.getBlockState(pos);
        return state.getBlock() instanceof ShelfBlock && state.getValue(POWERED) && state.getValue(FACING) == facing;
    }

    @Override
    public int getRows() { return 1; }
    @Override
    public int getColumns() { return 3; }

    @Override
    public SideChainPart getSideChainPart(@NotNull BlockState state) { return state.getValue(SIDE_CHAIN_PART); }
    @Override
    public @NotNull BlockState setSideChainPart(@NotNull BlockState state, @NotNull SideChainPart part) { return state.setValue(SIDE_CHAIN_PART, part); }
    @Override
    public @NotNull Direction getFacing(@NotNull BlockState state) { return state.getValue(FACING); }
    @Override
    public boolean isConnectable(@NotNull BlockState state) { return true; }
    @Override
    public void neighborChanged(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos neighborPos, boolean isMoving) {
        if (!level.isClientSide) {
            boolean isPowered = level.hasNeighborSignal(pos);
            if (isPowered != state.getValue(POWERED)) {
                BlockState newState = state.setValue(POWERED, isPowered);
                if (!isPowered) {
                    newState = newState.setValue(SIDE_CHAIN_PART, SideChainPart.UNCONNECTED);
                }
                level.setBlock(pos, newState, 3);
                level.playSound(null, pos, isPowered ? com.otterly76.ott.sound.ModSounds.SHELF_ACTIVATE.get() : com.otterly76.ott.sound.ModSounds.SHELF_DEACTIVATE.get(), net.minecraft.sounds.SoundSource.BLOCKS, 1.0F, 1.0F);
                
                // Notify neighbors to update their side chains
                Direction facing = state.getValue(FACING);
                level.updateNeighborsAt(pos.relative(facing.getClockWise()), this);
                level.updateNeighborsAt(pos.relative(facing.getCounterClockWise()), this);
            }
        }
    }

    @Override
    public int getMaxChainLength() { return 3; }
}
