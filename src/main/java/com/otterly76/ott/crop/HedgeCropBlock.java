package com.otterly76.ott.crop;

import com.otterly76.ott.item.ModItems;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.NotNull;

public class HedgeCropBlock extends CropBlock {
    public HedgeCropBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull ItemLike getBaseSeedId() {
        // This links the block back to the seed item for the "pick block" action
        return ModItems.HEDGE_SPROUTS.get();
    }
}