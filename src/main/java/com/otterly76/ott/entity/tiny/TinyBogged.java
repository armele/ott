package com.otterly76.ott.entity.tiny;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

public class TinyBogged extends Bogged {
    public TinyBogged(@NotNull EntityType<? extends Bogged> entityType, @NotNull Level level) {
        super(entityType, level);
    }


    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Bogged.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D * 1.35D)
                .add(Attributes.SCALE, 0.5D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(2, new RangedAttackGoal(this, 1.25D, 40, 20.0F));
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity target, float velocity) {
        ItemStack mushroom = new ItemStack(this.getRandom().nextBoolean() ? Blocks.BROWN_MUSHROOM : Blocks.RED_MUSHROOM);
        TinyEntityHelper.performSnowballAttack(this, target, mushroom);
    }
}
