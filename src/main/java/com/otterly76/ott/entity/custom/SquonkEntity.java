package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import com.otterly76.ott.registry.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;

public class SquonkEntity extends PathfinderMob implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SquonkEntity(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.2D));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            for (Player player : this.level().players()) {
                if (player.hasLineOfSight(this) && this.distanceToSqr(player) < 256.0D) {
                    player.addEffect(new MobEffectInstance(ModEffects.SOBBING, 200));
                }
            }
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_SQUONK_IDLE.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.ENTITY_SQUONK_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ENTITY_SQUONK_DEATH.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (event.isMoving()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.squonk.walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.squonk.idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}