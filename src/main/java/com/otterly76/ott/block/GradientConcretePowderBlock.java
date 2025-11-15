package com.otterly76.ott.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ConcretePowderBlock;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class GradientConcretePowderBlock extends ConcretePowderBlock implements IGradientBlock {
    private final DyeColor firstColor;
    private final DyeColor secondColor;
    private final Function<DyeColor, String> textureNameMapper;

    protected GradientConcretePowderBlock(Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper) {
        super(getConcreteGradientBlock(firstColor, secondColor), properties);
        this.registerDefaultState(this.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.UP));
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.textureNameMapper = textureNameMapper;
    }

    private static Block getConcreteGradientBlock(DyeColor firstColor, DyeColor secondColor) {
        return ModBlocks.getAllGradientConcreteBlocks().stream().filter(block -> block.get().getFirstColor().equals(firstColor) && block.get().getSecondColor().equals(secondColor)).findFirst().orElseThrow(() -> new IllegalArgumentException(String.format("No matching concrete block found for first and second colors %s and %s", firstColor, secondColor))).get();
    }

    @Override
    public DyeColor getFirstColor() {
        return firstColor;
    }

    @Override
    public DyeColor getSecondColor() {
        return secondColor;
    }

    @Override
    public String getTextureName(DyeColor dyeColor) {
        return textureNameMapper.apply(dyeColor);
    }

    @Override
    public ResourceLocation getRegistryID() {
        return BuiltInRegistries.BLOCK.getKey(this);
    }

    @Override
    public Block getBlockFromColor(DyeColor color) {
        return switch (color) {
            case BLACK -> Blocks.BLACK_CONCRETE_POWDER;
            case WHITE -> Blocks.WHITE_CONCRETE_POWDER;
            case ORANGE -> Blocks.ORANGE_CONCRETE_POWDER;
            case MAGENTA -> Blocks.MAGENTA_CONCRETE_POWDER;
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_CONCRETE_POWDER;
            case YELLOW -> Blocks.YELLOW_CONCRETE_POWDER;
            case LIME -> Blocks.LIME_CONCRETE_POWDER;
            case PINK -> Blocks.PINK_CONCRETE_POWDER;
            case GRAY -> Blocks.GRAY_CONCRETE_POWDER;
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_CONCRETE_POWDER;
            case CYAN -> Blocks.CYAN_CONCRETE_POWDER;
            case PURPLE -> Blocks.PURPLE_CONCRETE_POWDER;
            case BLUE -> Blocks.BLUE_CONCRETE_POWDER;
            case BROWN -> Blocks.BROWN_CONCRETE_POWDER;
            case GREEN -> Blocks.GREEN_CONCRETE_POWDER;
            case RED -> Blocks.RED_CONCRETE_POWDER;
        };
    }

    @Override
    @NotNull
    public BlockState getStateForPlacement(final @NotNull BlockPlaceContext context) {
        BlockGetter blockgetter = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        BlockState blockstate = blockgetter.getBlockState(blockpos);
        return shouldSolidify(blockgetter, blockpos, blockstate) ? this.concrete.getStateForPlacement(context) : super.getStateForPlacement(context).setValue(DirectionalBlock.FACING, context.getClickedFace());
    }

    @Override
    @NotNull
    protected BlockState updateShape(@NotNull BlockState oldBlockState, @NotNull Direction direction, @NotNull BlockState newBlockState, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos oldPos, @NotNull BlockPos newPos) {
        return touchesLiquid(levelAccessor, oldPos, oldBlockState) ? this.concrete.defaultBlockState().setValue(DirectionalBlock.FACING, oldBlockState.getValue(DirectionalBlock.FACING)) : super.updateShape(oldBlockState, direction, newBlockState, levelAccessor, oldPos, newPos);
    }

    @Override
    public void onLand(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldBlockState, BlockState newBlockState, @NotNull FallingBlockEntity fallingBlockEntity) {
        if (shouldSolidify(level, pos, oldBlockState, newBlockState.getFluidState())) {
            level.setBlock(pos, this.concrete.defaultBlockState().setValue(DirectionalBlock.FACING, oldBlockState.getValue(DirectionalBlock.FACING)), 3);
        }
    }

    @Override
    protected void createBlockStateDefinition(@NotNull StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DirectionalBlock.FACING);
    }

    @Override
    @NotNull
    public MutableComponent getName() {
        return Component.translatable("com.otterly76.ott.block.gradient.name", getBlockFromColor(firstColor).getName(), getBlockFromColor(secondColor).getName());
    }

    private static boolean shouldSolidify(BlockGetter p_52081_, BlockPos p_52082_, BlockState p_52083_, net.minecraft.world.level.material.FluidState fluidState) {
        return p_52083_.canBeHydrated(p_52081_, p_52082_, fluidState, p_52082_) || touchesLiquid(p_52081_, p_52082_, p_52083_);
    }

    public static boolean shouldSolidify(BlockGetter p_52081_, BlockPos p_52082_, BlockState p_52083_) {
        return shouldSolidify(p_52081_, p_52082_, p_52083_, p_52081_.getFluidState(p_52082_));
    }

    private static boolean touchesLiquid(BlockGetter p_52065_, BlockPos p_52066_, BlockState state) {
        boolean flag = false;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = p_52066_.mutable();

        for (Direction direction : Direction.values()) {
            BlockState blockstate = p_52065_.getBlockState(blockpos$mutableblockpos);
            if (direction != Direction.DOWN || state.canBeHydrated(p_52065_, p_52066_, blockstate.getFluidState(), blockpos$mutableblockpos)) {
                blockpos$mutableblockpos.setWithOffset(p_52066_, direction);
                blockstate = p_52065_.getBlockState(blockpos$mutableblockpos);
                if (state.canBeHydrated(p_52065_, p_52066_, blockstate.getFluidState(), blockpos$mutableblockpos) && !blockstate.isFaceSturdy(p_52065_, p_52066_, direction.getOpposite())) {
                    flag = true;
                    break;
                }
            }
        }

        return flag;
    }
}
