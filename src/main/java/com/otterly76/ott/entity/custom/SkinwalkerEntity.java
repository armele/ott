package com.otterly76.ott.entity.custom;

import org.jetbrains.annotations.NotNull;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
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

public class SkinwalkerEntity extends Monster implements GeoEntity {
    private static final EntityDataAccessor<Boolean> STUNNED = SynchedEntityData.defineId(SkinwalkerEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public SkinwalkerEntity(EntityType<? extends Monster> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 40.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
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
                return super.canUse() && !SkinwalkerEntity.this.isStunned();
            }
        });
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return super.canUse() && !SkinwalkerEntity.this.isStunned();
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

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ENTITY_SKINWALKER_IDLE.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.ENTITY_SKINWALKER_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSounds.ENTITY_SKINWALKER_HURT.get();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            Player player = this.level().getNearestPlayer(this, 16.0D);
            if (player == null || this.distanceToSqr(player) > 256.0D) {
                this.transformToWolf();
            }
        }
    }

    private void transformToWolf() {
        if (this.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 20, 0.5, 0.5, 0.5, 0.05);
            serverLevel.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ZOMBIE_VILLAGER_CONVERTED, this.getSoundSource(), 1.0F, 1.0F);
            BewitchedGreywolfEntity wolf = ModEntities.BEWITCHED_TIMBER_WOLF.get().create(this.level());
            if (wolf != null) {
                wolf.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
                this.level().addFreshEntity(wolf);
                this.discard();
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "movement", 5, event -> {
            if (this.isStunned()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.skinwalker.idle"));
            }
            if (event.isMoving()) {
                return event.setAndContinue(RawAnimation.begin().thenLoop("animation.skinwalker.walk"));
            }
            return event.setAndContinue(RawAnimation.begin().thenLoop("animation.skinwalker.idle"));
        }));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}