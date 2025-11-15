package com.otterly76.ott.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.StainedGlassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class GradientStainedGlassBlock extends StainedGlassBlock implements IGradientBlock {
    private final DyeColor firstColor;
    private final DyeColor secondColor;
    private final Function<DyeColor, String> textureNameMapper;

    protected GradientStainedGlassBlock(Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper) {
        super(firstColor, properties);
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
    public ResourceLocation getRenderType() {
        return ResourceLocation.withDefaultNamespace(RenderType.translucent().name);
    }

    @Override
    public Block getBlockFromColor(DyeColor color) {
        return switch (color) {
            case BLACK -> Blocks.BLACK_STAINED_GLASS;
            case WHITE -> Blocks.WHITE_STAINED_GLASS;
            case ORANGE -> Blocks.ORANGE_STAINED_GLASS;
            case MAGENTA -> Blocks.MAGENTA_STAINED_GLASS;
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_STAINED_GLASS;
            case YELLOW -> Blocks.YELLOW_STAINED_GLASS;
            case LIME -> Blocks.LIME_STAINED_GLASS;
            case PINK -> Blocks.PINK_STAINED_GLASS;
            case GRAY -> Blocks.GRAY_STAINED_GLASS;
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_STAINED_GLASS;
            case CYAN -> Blocks.CYAN_STAINED_GLASS;
            case PURPLE -> Blocks.PURPLE_STAINED_GLASS;
            case BLUE -> Blocks.BLUE_STAINED_GLASS;
            case BROWN -> Blocks.BROWN_STAINED_GLASS;
            case GREEN -> Blocks.GREEN_STAINED_GLASS;
            case RED -> Blocks.RED_STAINED_GLASS;
        };
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
