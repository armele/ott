package com.otterly76.ott.block;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public abstract class SimpleGradientBlock extends Block implements IGradientBlock {
    private final DyeColor firstColor;
    private final DyeColor secondColor;
    private final Function<DyeColor, String> textureNameMapper;

    protected SimpleGradientBlock(Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState().setValue(DirectionalBlock.FACING, Direction.UP));
        this.firstColor = firstColor;
        this.secondColor = secondColor;
        this.textureNameMapper = textureNameMapper;
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
    public BlockState getStateForPlacement(final @NotNull BlockPlaceContext context) {
        return super.getStateForPlacement(context).setValue(DirectionalBlock.FACING, context.getClickedFace());
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
}
