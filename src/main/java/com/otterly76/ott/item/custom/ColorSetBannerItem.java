package com.otterly76.ott.item.custom;

import net.minecraft.world.item.BannerItem;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ColorSetBannerItem extends BannerItem {
    public ColorSetBannerItem(Block standing, Block wall, Properties properties) {
        super(standing, wall, properties);
    }

    @Override
    @SuppressWarnings("removal")
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            @Override
            public @NotNull net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return com.otterly76.ott.client.render.item.ColorSetBannerItemRenderer.getInstance();
            }
        });
    }
}