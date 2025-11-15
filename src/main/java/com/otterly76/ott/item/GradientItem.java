package com.otterly76.ott.item;

import com.otterly76.ott.block.IGradientBlock;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class GradientItem<T extends Block & IGradientBlock> extends BlockItem {

    private final T gradientBlock;

    public GradientItem(Properties properties, T gradientBlock) {
        super(gradientBlock, properties);
        this.gradientBlock = gradientBlock;
    }

    @Override
    @NotNull
    public Component getName(final @NotNull ItemStack itemStack) {
        return Component.translatable("com.otterly76.ott.block.gradient.name", gradientBlock.getBlockFromColor(gradientBlock.getFirstColor()).getName(), gradientBlock.getBlockFromColor(gradientBlock.getSecondColor()).getName());
    }
}
