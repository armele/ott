package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.entity.ModBlockEntities;
import com.otterly76.ott.block.entity.OakNestEntity;
import com.otterly76.ott.entity.custom.Hoopoe;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.registry.ModBlockStateProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class OakNestBlock extends BaseEntityBlock {
    public static final MapCodec<OakNestBlock> CODEC = simpleCodec(OakNestBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty HOOPOES = ModBlockStateProperties.HOOPOES;
    public static final IntegerProperty EGGS = ModBlockStateProperties.HOOPOE_EGGS;

    public OakNestBlock(BlockBehaviour.Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HOOPOES, 0).setValue(EGGS, 0));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        if (state.getValue(EGGS) > 0 && player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty()) {
            if (!world.isClientSide()) {
                BlockEntity be = world.getBlockEntity(pos);
                if (be instanceof OakNestEntity oakNestEntity) {
                    oakNestEntity.angerHoopoes(player, state, OakNestEntity.NestState.EMERGENCY);
                }
            }

            world.setBlock(pos, state.setValue(EGGS, state.getValue(EGGS) - 1), 3);
            BlockPos itemPos = pos.relative(state.getValue(FACING));
            world.playSound(null, itemPos, SoundEvents.CHICKEN_EGG, SoundSource.NEUTRAL, 1.0F, 1.0F);
            popResource(world, itemPos, new ItemStack(ModItems.HOOPOE_EGG.get()));
            return InteractionResult.sidedSuccess(world.isClientSide());
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public void playerDestroy(@NotNull Level level, @NotNull Player player, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @Nullable BlockEntity blockEntity, @NotNull ItemStack itemStack) {
        super.playerDestroy(level, player, blockPos, blockState, blockEntity, itemStack);
        if (!level.isClientSide && blockEntity instanceof OakNestEntity oakNestEntity) {
            // Simplified Silk Touch check for 1.21.1
            if (!player.isCreative()) {
                oakNestEntity.angerHoopoes(player, blockState, OakNestEntity.NestState.EMERGENCY);
                level.updateNeighborsAt(blockPos, this);
                this.angerNearbyHoopoes(level, blockPos);
            }
        }
    }

    private void angerNearbyHoopoes(Level world, BlockPos pos) {
        List<Hoopoe> hoopoeList = world.getEntitiesOfClass(Hoopoe.class, (new AABB(pos)).inflate(8.0, 6.0, 8.0));
        if (!hoopoeList.isEmpty()) {
            List<Player> playerList = world.getEntitiesOfClass(Player.class, (new AABB(pos)).inflate(8.0, 6.0, 8.0));
            if (!playerList.isEmpty()) {
                for (Hoopoe hoopoe : hoopoeList) {
                    if (hoopoe.getTarget() == null) {
                        hoopoe.setTarget(playerList.get(world.random.nextInt(playerList.size())));
                    }
                }
            }
        }
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HOOPOES, EGGS);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new OakNestEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, @NotNull BlockState blockState, @NotNull BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.OAK_NEST.get(), OakNestEntity::serverTick);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState blockState, @NotNull Player player) {
        if (!world.isClientSide() && player.isCreative() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS)) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof OakNestEntity oakNestEntity) {
                if (!oakNestEntity.hasNoHoopoes()) {
                    ItemStack itemStack = new ItemStack(this);
                    CompoundTag nbtCompound = new CompoundTag();
                    nbtCompound.put("Hoopoes", oakNestEntity.getHoopoes());
                    // 1.21.1 component-based data logic would go here, using NBT for now
                    ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                    itemEntity.setDefaultPickUpDelay();
                    world.addFreshEntity(itemEntity);
                }
            }
        }
        return super.playerWillDestroy(world, pos, blockState, player);
    }
}