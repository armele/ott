package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ai.navigation.SmoothSwimmingMoveControlButNotBad;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MantaRayEntity extends AbstractFish implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation SWIM_ANIMATION = RawAnimation.begin().thenLoop("swimgracile");
    private static final RawAnimation SWIM_FAST_ANIMATION = RawAnimation.begin().thenLoop("swimfast");

    public MantaRayEntity(EntityType<? extends AbstractFish> type, Level level) {
        super(type, level);
        this.moveControl = new SmoothSwimmingMoveControlButNotBad(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return AbstractFish.createAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.8D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 10));
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(0.02F, pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(pTravelVector);
        }
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return ItemStack.EMPTY;
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (this.moveControl.getSpeedModifier() > 1.0D) {
                return event.setAndContinue(SWIM_FAST_ANIMATION);
            }
            return event.setAndContinue(SWIM_ANIMATION);
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}