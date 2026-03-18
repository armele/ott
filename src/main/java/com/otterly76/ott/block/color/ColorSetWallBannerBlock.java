package com.otterly76.ott.block.color;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.WallBannerBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

public class ColorSetWallBannerBlock extends WallBannerBlock {
    private final String colorName;
    private final DeferredBlock<BannerBlock> standingBanner;

    public ColorSetWallBannerBlock(String colorName, DeferredBlock<BannerBlock> standingBanner, DyeColor dummyColor, Properties properties) {
        super(dummyColor, properties);
        this.colorName = colorName;
        this.standingBanner = standingBanner;
    }

    public String getColorName() {
        return colorName;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull BlockState state, @NotNull HitResult target, @NotNull LevelReader level, @NotNull BlockPos pos, @NotNull Player player) {
        return new ItemStack(standingBanner.get().asItem());
    }

    @Override
    public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ColorSetBannerBlockEntity(pos, state);
    }
}