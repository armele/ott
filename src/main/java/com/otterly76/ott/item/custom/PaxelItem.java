package com.otterly76.ott.item.custom;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.HashSet;

public class PaxelItem extends DiggerItem {

    private static final Set<ItemAbility> PAXEL_ACTIONS;
    static {
        PAXEL_ACTIONS = new HashSet<>();
        PAXEL_ACTIONS.addAll(ItemAbilities.DEFAULT_AXE_ACTIONS);
        PAXEL_ACTIONS.addAll(ItemAbilities.DEFAULT_PICKAXE_ACTIONS);
        PAXEL_ACTIONS.addAll(ItemAbilities.DEFAULT_SHOVEL_ACTIONS);
    }

    private final float tierSpeed;

    public PaxelItem(Tier tier, Item.Properties properties) {
        super(tier, BlockTags.MINEABLE_WITH_PICKAXE, properties);
        this.tierSpeed = tier.getSpeed();
    }

    @Override
    public float getDestroySpeed(@NotNull ItemStack stack, @NotNull BlockState state) {
        float speed = super.getDestroySpeed(stack, state);
        if (speed == 1.0F) {
            if (state.is(BlockTags.MINEABLE_WITH_AXE) || state.is(BlockTags.MINEABLE_WITH_SHOVEL)) {
                return this.tierSpeed;
            }
        }
        return speed;
    }

    @Override
    public boolean canPerformAction(@NotNull ItemStack stack, @NotNull ItemAbility ability) {
        return PAXEL_ACTIONS.contains(ability);
    }
}
