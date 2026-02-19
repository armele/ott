package com.otterly76.ott.entity.tiny;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TinyStray extends Stray {
    public TinyStray(@NotNull EntityType<? extends Stray> entityType, @NotNull Level level) {
        super(entityType, level);
    }


    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Stray.createAttributes()
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
        TinyEntityHelper.performSnowballAttack(this, target, null);
    }
}
