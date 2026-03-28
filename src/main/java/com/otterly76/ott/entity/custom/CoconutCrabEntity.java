package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Predicate;

public class CoconutCrabEntity extends Animal implements NeutralMob, GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");

    private static final EntityDataAccessor<Boolean> HAS_COCONUT =
            SynchedEntityData.defineId(CoconutCrabEntity.class, EntityDataSerializers.BOOLEAN);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable private UUID persistentAngerTarget;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public CoconutCrabEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    // -------------------------------------------------------------------------
    // GeoEntity
    // -------------------------------------------------------------------------

    @Override
    public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "locomotion", 4, state -> {
            if (state.isMoving()) {
                return state.setAndContinue(WALK);
            }
            return state.setAndContinue(IDLE);
        }));
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    // -------------------------------------------------------------------------
    // Animal / NeutralMob
    // -------------------------------------------------------------------------

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new CrabAvoidGoal<>(this, Player.class, 8.0F, 2.0D, 2.0D));
        this.goalSelector.addGoal(2, new CrabMeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new CrabHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new CrabNearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        return super.hurt(source, this.hasCoconut() ? amount / 2.0F : amount);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.getHealth() <= this.getMaxHealth() / 2.0F && this.hasCoconut()) {
            this.breakCoconut();
        }
        if (!this.level().isClientSide()) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }
    }

    private void breakCoconut() {
        this.setHasCoconut(false);
        this.stopBeingAngry();
        this.playSound(ModSounds.COCONUT_SMASH.get(), 0.7F, 1.0F);
        if (this.level().getGameRules().getRule(GameRules.RULE_DOMOBLOOT).get()) {
            ItemEntity item = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(),
                    new ItemStack(ModBlocks.COCONUT.get(), 2));
            item.setDefaultPickUpDelay();
            this.level().addFreshEntity(item);
        }
    }

    // -------------------------------------------------------------------------
    // Coconut state
    // -------------------------------------------------------------------------

    public void setHasCoconut(boolean hasCoconut) {
        this.entityData.set(HAS_COCONUT, hasCoconut);
    }

    public boolean hasCoconut() {
        return this.entityData.get(HAS_COCONUT);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_COCONUT, true);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Coconut", this.hasCoconut());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setHasCoconut(tag.getBoolean("Coconut"));
    }

    // -------------------------------------------------------------------------
    // Sounds
    // -------------------------------------------------------------------------

    @Override
    protected @NotNull SoundEvent getAmbientSound() {
        return ModSounds.COCONUT_CRAB_AMBIENT.get();
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.COCONUT_CRAB_HURT.get();
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return ModSounds.COCONUT_CRAB_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState block) {
        this.playSound(SoundEvents.SPIDER_STEP, 0.15F, 1.0F);
    }

    // -------------------------------------------------------------------------
    // Misc overrides
    // -------------------------------------------------------------------------

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public boolean canBeLeashed() {
        return false;
    }

    // -------------------------------------------------------------------------
    // NeutralMob
    // -------------------------------------------------------------------------

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.remainingPersistentAngerTime = time;
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    // -------------------------------------------------------------------------
    // Inner goal classes
    // -------------------------------------------------------------------------

    static class CrabMeleeAttackGoal extends MeleeAttackGoal {
        private final CoconutCrabEntity crab;

        CrabMeleeAttackGoal(CoconutCrabEntity mob, double speedModifier, boolean followIfNotSeen) {
            super(mob, speedModifier, followIfNotSeen);
            this.crab = mob;
        }

        @Override
        public boolean canUse() {
            return this.crab.hasCoconut() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.crab.hasCoconut() && super.canContinueToUse();
        }
    }

    static class CrabHurtByTargetGoal extends HurtByTargetGoal {
        private final CoconutCrabEntity crab;

        CrabHurtByTargetGoal(CoconutCrabEntity mob) {
            super(mob);
            this.crab = mob;
        }

        @Override
        public boolean canUse() {
            return this.crab.hasCoconut() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.crab.hasCoconut() && super.canContinueToUse();
        }
    }

    static class CrabAvoidGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
        private final CoconutCrabEntity crab;

        CrabAvoidGoal(CoconutCrabEntity mob, Class<T> entityClass, float maxDist, double walkSpeed, double sprintSpeed) {
            super(mob, entityClass, maxDist, walkSpeed, sprintSpeed, EntitySelector.NO_SPECTATORS::test);
            this.crab = mob;
        }

        @Override
        public boolean canUse() {
            return !this.crab.hasCoconut() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.crab.hasCoconut() && super.canContinueToUse();
        }
    }

    static class CrabNearestAttackableTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
        private final CoconutCrabEntity crab;

        CrabNearestAttackableTargetGoal(CoconutCrabEntity mob, Class<T> targetType, int interval,
                                        boolean mustSee, boolean mustReach,
                                        @Nullable Predicate<LivingEntity> predicate) {
            super(mob, targetType, interval, mustSee, mustReach, predicate);
            this.crab = mob;
        }

        @Override
        public boolean canUse() {
            return this.crab.hasCoconut() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.crab.hasCoconut() && super.canContinueToUse();
        }
    }
}
