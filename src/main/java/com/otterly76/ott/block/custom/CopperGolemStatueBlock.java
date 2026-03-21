package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.block.entity.CopperGolemStatueBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.ItemAbilities;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CopperGolemStatueBlock extends BaseEntityBlock implements WeatheringCopper, SimpleWaterloggedBlock {
    public static final MapCodec<CopperGolemStatueBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            WeatheringCopper.WeatherState.CODEC.fieldOf("weathering_state").forGetter(CopperGolemStatueBlock::getAge),
                            propertiesCodec()
                    )
                    .apply(instance, CopperGolemStatueBlock::new)
    );

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<Pose> POSE = EnumProperty.create("pose", Pose.class);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 13.0D, 12.0D);

    private final WeatherState weatherState;

    public CopperGolemStatueBlock(WeatherState weatherState, BlockBehaviour.Properties properties) {
        super(properties);
        this.weatherState = weatherState;
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(POSE, Pose.STANDING)
                .setValue(WATERLOGGED, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull WeatherState getAge() {
        return this.weatherState;
    }

    @Override
    public void randomTick(@NotNull BlockState state, @NotNull ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return WeatheringCopper.getNext(state.getBlock()).isPresent();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, POSE, WATERLOGGED);
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite())
                .setValue(WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull net.minecraft.world.InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (stack.canPerformAction(ItemAbilities.AXE_SCRAPE)) {
            if (this.weatherState == WeatherState.UNAFFECTED) {
                if (!level.isClientSide) {
                    com.otterly76.ott.entity.custom.CopperGolem golem = com.otterly76.ott.entity.ModEntities.COPPER_GOLEM.get().create(level);
                    if (golem != null) {
                        golem.setWeatherState(WeatherState.UNAFFECTED);
                        golem.moveTo((double)pos.getX() + 0.5, (double)pos.getY() + 0.05, (double)pos.getZ() + 0.5, state.getValue(FACING).toYRot(), 0.0F);
                        level.addFreshEntity(golem);
                        level.removeBlock(pos, false);
                        stack.hurtAndBreak(1, player, net.minecraft.world.entity.LivingEntity.getSlotForHand(hand));
                    }
                }
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        
        if (stack.isEmpty() && player.isSecondaryUseActive()) {
            if (!level.isClientSide) {
                level.setBlock(pos, state.setValue(POSE, state.getValue(POSE).getNextPose()), 3);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new CopperGolemStatueBlockEntity(pos, state);
    }

    @Override
    public @NotNull FluidState getFluidState(@NotNull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public enum Pose implements StringRepresentable {
        STANDING("standing"),
        RUNNING("running"),
        SITTING("sitting"),
        STAR("star");

        private final String name;

        Pose(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public Pose getNextPose() {
            return values()[(this.ordinal() + 1) % values().length];
        }
    }
}
