package com.otterly76.ott.entity.custom;

import org.jetbrains.annotations.NotNull;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import net.minecraft.core.particles.ParticleTypes;

public class WechugeEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Boolean> STUNNED = SynchedEntityData.defineId(WechugeEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public WechugeEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STUNNED, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, false) {
            @Override
            public boolean canUse() {
                return super.canUse() && !WechugeEntity.this.isStunned();
            }
        });
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !WechugeEntity.this.isStunned();
            }
        });
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.level().isClientSide) {
            if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D) {
                this.level().addParticle(ParticleTypes.SNOWFLAKE, this.getRandomX(0.5D), this.getY(), this.getRandomZ(0.5D), 0, 0, 0);
            }
            Player player = this.level().getNearestPlayer(this, 32.0D);
            if (player != null) {
                double dist = this.distanceToSqr(player);
                if (dist < 256.0D) {
                    int count = (int)(2 * (1.0 - Math.sqrt(dist) / 16.0));
                    for (int i = 0; i < count; i++) {
                        this.level().addParticle(ParticleTypes.CLOUD, this.getRandomX(1.5D), this.getRandomY(), this.getRandomZ(1.5D), 0, 0, 0);
                    }
                }
            }
        }
    }

    public boolean isStunned() {
        return this.entityData.get(STUNNED);
    }

    public void setStunned(boolean stunned) {
        this.entityData.set(STUNNED, stunned);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_WECHUGE_IDLE.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.ENTITY_WECHUGE_GROWL.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSounds.ENTITY_WECHUGE_DEATH.get();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (this.isStunned()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.wechuge.stun"));
            }
            if (event.isMoving()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.wechuge.walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.wechuge.idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}