package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ai.goal.FlyingWanderGoal;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Firefly extends Animal implements FlyingAnimal, GeoEntity {
    private static final RawAnimation FLY = RawAnimation.begin().thenLoop("animation.ott.firefly.fly");
    private static final EntityDataAccessor<Integer> GLOW_TICKS = SynchedEntityData.defineId(Firefly.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> SUN_TICKS = SynchedEntityData.defineId(Firefly.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Firefly(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.WATER, -1.0F);
        this.setPathfindingMalus(PathType.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
        this.setPathfindingMalus(PathType.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D).add(Attributes.FLYING_SPEED, 0.4F).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(@NotNull BlockPos pos) {
                return !level.getBlockState(pos.below()).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, @NotNull BlockState state, @NotNull BlockPos pos) {
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new FireflyHideInGrassGoal(this, 1.0D, 8, 4));
        this.goalSelector.addGoal(3, new FlyingWanderGoal(this));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(GLOW_TICKS, 0);
        builder.define(SUN_TICKS, 0);
    }

    public boolean isGlowing() {
        return this.getGlowTicksRemaining() > 0;
    }

    public int getGlowTicksRemaining() {
        return this.entityData.get(GLOW_TICKS);
    }

    public void setGlowTicks(int ticks) {
        this.entityData.set(GLOW_TICKS, ticks);
    }

    public int getSunTicks() {
        return this.entityData.get(SUN_TICKS);
    }

    public void setSunTicks(int ticks) {
        this.entityData.set(SUN_TICKS, ticks);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.canGlow()) {
                if (this.getGlowTicksRemaining() > 0) {
                    this.setGlowTicks(this.getGlowTicksRemaining() - 1);
                } else if (this.random.nextInt(100) == 0) {
                    this.setGlowTicks(40 + this.random.nextInt(40));
                }
            } else {
                this.setGlowTicks(0);
            }

            if (this.isSunBurnTick()) {
                this.setSunTicks(this.getSunTicks() + 1);
                if (this.getSunTicks() > 100) {
                    this.discard();
                }
            } else {
                this.setSunTicks(0);
            }
        }
    }

    public boolean canGlow() {
        long time = this.level().getDayTime() % 24000;
        return time >= 13000 && time <= 23000;
    }

    public boolean isSunBurnTick() {
        if (this.level().isDay() && !this.level().isClientSide) {
            BlockPos blockpos = BlockPos.containing(this.getX(), this.getEyeY(), this.getZ());
            return this.level().getMaxLocalRawBrightness(blockpos) > 10 && this.level().canSeeSky(blockpos);
        }
        return false;
    }

    @Override
    public boolean isFlapping() {
        return this.isFlying() && this.tickCount % Mth.ceil(1.4959966F) == 0;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return true;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.FIREFLY_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.FIREFLY_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.FIREFLY_DEATH.get();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Firefly> PlayState predicate(final AnimationState<E> event) {
        event.getController().setAnimation(FLY);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    private static class FireflyHideInGrassGoal extends MoveToBlockGoal {
        private final Firefly mob;

        public FireflyHideInGrassGoal(Firefly mob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(mob, speedModifier, searchRange, verticalSearchRange);
            this.mob = mob;
        }

        @Override
        public boolean canUse() {
            return mob.level().isDay() && super.canUse();
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader level, @NotNull BlockPos pos) {
            return level.getBlockState(pos).is(BlockTags.REPLACEABLE_BY_TREES);
        }

        @Override
        public void tick() {
            BlockPos targetPos = this.getMoveToTarget();
            if (!targetPos.closerToCenterThan(mob.position(), this.acceptedDistance())) {
                ++this.tryTicks;
                if (this.shouldRecalculatePath()) {
                    mob.getNavigation().moveTo(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D, this.speedModifier);
                }
            } else {
                --this.tryTicks;
            }
        }

        @Override
        protected @NotNull BlockPos getMoveToTarget() {
            return this.blockPos;
        }
    }
}
