package com.otterly76.ott.item.custom;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ColorSetShulkerBoxItem extends BlockItem {
    public ColorSetShulkerBoxItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return com.otterly76.ott.client.render.item.ColorSetShulkerBoxItemRenderer.getInstance();
            }
        });
    }
}