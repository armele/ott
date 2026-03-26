package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class WendigoEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Boolean> STUNNED = SynchedEntityData.defineId(WendigoEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> KILL_COUNT = SynchedEntityData.defineId(WendigoEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WendigoEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.35D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STUNNED, false);
        builder.define(KILL_COUNT, 0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !WendigoEntity.this.isStunned();
            }
        });
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !WendigoEntity.this.isStunned();
            }
        });
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    public boolean isStunned() {
        return this.entityData.get(STUNNED);
    }

    public void setStunned(boolean stunned) {
        this.entityData.set(STUNNED, stunned);
    }

    public int getKillCount() {
        return this.entityData.get(KILL_COUNT);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_WENDIGO_IDLE.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.ENTITY_WENDIGO_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSounds.ENTITY_WENDIGO_DEATH.get();
    }

    @Override
    public void awardKillScore(@NotNull Entity victim, int score, @NotNull DamageSource damageSource) {
        super.awardKillScore(victim, score, damageSource);
        int currentKills = this.getKillCount();
        if (currentKills < 10) {
            this.entityData.set(KILL_COUNT, currentKills + 1);
            Objects.requireNonNull(this.getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(10.0D + (currentKills + 1) * 2.0D);
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(60.0D + (currentKills + 1) * 10.0D);
            this.heal(10.0F);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (this.isStunned()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.wendigo.stun"));
            }
            if (event.isMoving()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.wendigo.walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.wendigo.idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}