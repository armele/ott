package com.otterly76.ott.entity.ai.goal;

import com.otterly76.ott.util.ModTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumSet;
import java.util.List;

public class SearchForMeatGoal extends Goal {
    private final PathfinderMob mob;
    private final double speedModifier;
    private final double horizontalSearchRange;
    private final double verticalSearchRange;
    private final Ingredient meat;

    public SearchForMeatGoal(PathfinderMob mob, double speedModifier, double horizontalSearchRange, double verticalSearchRange) {
        this.setFlags(EnumSet.of(Flag.MOVE));
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.meat = Ingredient.of(ModTags.ItemTags.VULTURE_FOOD_ITEMS);
        this.horizontalSearchRange = horizontalSearchRange;
        this.verticalSearchRange = verticalSearchRange;
    }

    @Override
    public boolean canUse() {
        if (mob.getTarget() != null || mob.getLastHurtByMob() != null) {
            return false;
        }
        if (mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty()) {
            List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> meat.test(itemEntity.getItem()));
            return !list.isEmpty() && mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
        }
        return false;
    }

    @Override
    public void tick() {
        List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> meat.test(itemEntity.getItem()));
        ItemStack itemstack = mob.getItemBySlot(EquipmentSlot.MAINHAND);
        if (itemstack.isEmpty() && !list.isEmpty()) {
            mob.getNavigation().moveTo(list.getFirst(), speedModifier);
        }
    }

    @Override
    public void start() {
        List<ItemEntity> list = mob.level().getEntitiesOfClass(ItemEntity.class, mob.getBoundingBox().inflate(horizontalSearchRange, verticalSearchRange, horizontalSearchRange), itemEntity -> meat.test(itemEntity.getItem()));
        if (!list.isEmpty()) {
            mob.getNavigation().moveTo(list.getFirst(), speedModifier);
        }
    }
}