package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.core.OttAnimal;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.entity.core.SleepingAnimal;
import com.otterly76.ott.entity.ai.goal.BabyHurtByTargetGoal;
import com.otterly76.ott.entity.ai.goal.BabyPanicGoal;
import com.otterly76.ott.entity.ai.goal.SleepGoal;
import com.otterly76.ott.entity.ai.navigation.MMPathNavigatorGround;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class Lion extends OttAnimal implements OttGeoEntity, SleepingAnimal {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ott.lion.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ott.lion.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.ott.lion.run");
    protected static final RawAnimation PREY = RawAnimation.begin().thenLoop("animation.ott.lion.prey");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.ott.lion.sleep");
    protected static final RawAnimation SLEEP2 = RawAnimation.begin().thenLoop("animation.ott.lion.sleep2");
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Lion.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> HAS_MANE = SynchedEntityData.defineId(Lion.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Lion(@NotNull EntityType<? extends OttAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 24.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new MMPathNavigatorGround(this, level);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        super.finalizeSpawn(level, difficulty, reason, spawnData);
        AgeableMob.AgeableMobGroupData ageableMobGroupData;
        if (spawnData == null) {
            spawnData = new AgeableMob.AgeableMobGroupData(true);
            this.setHasMane(this.getRandom().nextBoolean());
        }
        if ((ageableMobGroupData = (AgeableMob.AgeableMobGroupData) spawnData).getGroupSize() > 2) {
            this.setAge(-24000);
        }
        ageableMobGroupData.increaseGroupSizeByOne();
        RandomSource random = level.getRandom();
        Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addPermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath("ott", "random_spawn_bonus"), random.triangle(0.0, 0.11485000000000001), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        return spawnData;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return ModEntities.LION.get().create(serverLevel);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, net.minecraft.world.entity.animal.Bee.class, 8.0f, 1.3, 1.3));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.6D, true));
        this.goalSelector.addGoal(2, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(3, new SleepGoal<>(this));
        this.goalSelector.addGoal(4, new LionFollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.6));
        this.goalSelector.addGoal(6, new LionFollowLeaderGoal(this, 1.1D, 8.0F, 24.0F));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                entity -> entity.getType().is(ModTags.EntityTypes.LION_HOSTILES)
                        && !entity.isBaby()
                        && !this.isSleeping() && !this.isBaby() && this.level().isNight()));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SLEEPING, false);
        builder.define(HAS_MANE, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("Mane", this.hasMane());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setHasMane(compoundTag.getBoolean("Mane"));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        this.setHasMane(this.getRandom().nextBoolean());
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level().getDayTime();
        if (this.getTarget() != null || this.level().isWaterAt(this.blockPosition())) {
            return false;
        } else {
            return dayTime > 6000 && dayTime < 13000;
        }
    }

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            double speedModifier = this.getMoveControl().getSpeedModifier();
            if (speedModifier >= 1.25D && this.onGround()) {
                this.setPose(Pose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(Pose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(Pose.STANDING);
            this.setSprinting(false);
        }
    }

    @Override
    public boolean isSteppingCarefully() {
        return this.isCrouching() || super.isSteppingCarefully();
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public void setHasMane(boolean hasMane) {
        this.entityData.set(HAS_MANE, hasMane);
    }

    public boolean hasMane() {
        return this.entityData.get(HAS_MANE);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.LION_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.LION_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.LION_DEATH.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return 900;
    }

    public static boolean canSpawn(EntityType<? extends net.minecraft.world.entity.animal.Animal> entityType, net.minecraft.world.level.LevelAccessor level, MobSpawnType spawnType, net.minecraft.core.BlockPos pos, net.minecraft.util.RandomSource random) {
        return level.getBlockState(pos.below()).is(net.minecraft.tags.BlockTags.ANIMALS_SPAWNABLE_ON) && isBrightEnoughToSpawn(level, pos);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private <E extends Lion> PlayState predicate(final AnimationState<E> event) {
        if (this.isSleeping() && this.hasMane()) {
            event.getController().setAnimation(SLEEP2);
        } else if (this.isSleeping() && !this.hasMane()) {
            event.getController().setAnimation(SLEEP);
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(RUN);
                event.getController().setAnimationSpeed(2.5F);
            } else if (this.isCrouching()) {
                event.getController().setAnimation(PREY);
                event.getController().setAnimationSpeed(0.8F);
            } else {
                event.getController().setAnimation(WALK);
                event.getController().setAnimationSpeed(1.0F);
            }
        } else {
            event.getController().setAnimation(IDLE);
            event.getController().setAnimationSpeed(1.0F);
        }
        return PlayState.CONTINUE;
    }

    private <E extends Lion> PlayState attackPredicate(final AnimationState<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            event.getController().forceAnimationReset();
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.ott.lion.attack"));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    private void soundListener(SoundKeyframeEvent<Lion> event) {
        Lion lion = event.getAnimatable();
        if (lion.level().isClientSide) {
            if (event.getKeyframeData().getSound().startsWith("step")) {
                lion.level().playLocalSound(lion.getX(), lion.getY(), lion.getZ(), ModSounds.LION_STEP.get(), lion.getSoundSource(), 0.3F, lion.getVoicePitch(), false);
            } else if (event.getKeyframeData().getSound().equals("swing")) {
                lion.level().playLocalSound(lion.getX(), lion.getY(), lion.getZ(), ModSounds.LION_SWING.get(), lion.getSoundSource(), 1.0F, lion.getVoicePitch(), false);
            } else if (event.getKeyframeData().getSound().equals("sleep")) {
                lion.level().playLocalSound(lion.getX(), lion.getY(), lion.getZ(), ModSounds.LION_SLEEP.get(), lion.getSoundSource(), 1.0F, lion.getVoicePitch(), false);
            } else if (event.getKeyframeData().getSound().equals("attack")) {
                lion.level().playLocalSound(lion.getX(), lion.getY(), lion.getZ(), ModSounds.LION_ATTACK.get(), lion.getSoundSource(), 1.0F, lion.getVoicePitch(), false);
            }
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<Lion> controller = new AnimationController<>(this, "controller", 5, this::predicate);
        controller.setSoundKeyframeHandler(this::soundListener);
        controllers.add(controller);
        controllers.add(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }


    static class LionFollowLeaderGoal extends Goal {
        private final Lion mob;
        private final Predicate<Mob> followPredicate;
        private final double speedModifier;
        private final PathNavigation navigation;
        private final float stopDistance;
        private final float areaSize;
        @Nullable
        private Lion followingMob;
        private int timeToRecalcPath;
        private float oldWaterCost;

        public LionFollowLeaderGoal(Lion mob, double speedModifier, float stopDistance, float areaSize) {
            this.mob = mob;
            this.followPredicate = followingMob -> followingMob != null && !followingMob.isBaby();
            this.speedModifier = speedModifier;
            this.navigation = mob.getNavigation();
            this.stopDistance = stopDistance;
            this.areaSize = areaSize;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.mob.isBaby() || this.mob.hasMane()) {
                return false;
            }
            List<Lion> nearbyLions = this.mob.level().getEntitiesOfClass(Lion.class, this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
            if (!nearbyLions.isEmpty()) {
                for (Lion lion : nearbyLions) {
                    if (!lion.hasMane()) continue;
                    if (lion.isInvisible()) continue;
                    this.followingMob = lion;
                    return true;
                }
                if (this.followingMob == null) {
                    for (Lion lion : nearbyLions) {
                        if (lion.isBaby()) continue;
                        if (lion.isInvisible()) continue;
                        this.followingMob = lion;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr(this.followingMob) > (double) (this.stopDistance * this.stopDistance);
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.mob.getPathfindingMalus(PathType.WATER);
            this.mob.setPathfindingMalus(PathType.WATER, 0.0f);
        }

        @Override
        public void stop() {
            this.followingMob = null;
            this.navigation.stop();
            this.mob.setPathfindingMalus(PathType.WATER, this.oldWaterCost);
        }

        @Override
        public void tick() {
            double f;
            double e;
            if (this.followingMob == null || this.mob.isLeashed()) {
                return;
            }
            this.mob.getLookControl().setLookAt(this.followingMob, 10.0f, this.mob.getMaxHeadXRot());
            if (--this.timeToRecalcPath > 0) {
                return;
            }
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            double d = this.mob.getX() - this.followingMob.getX();
            double g = d * d + (e = this.mob.getY() - this.followingMob.getY()) * e + (f = this.mob.getZ() - this.followingMob.getZ()) * f;
            if (g <= (double) (this.stopDistance * this.stopDistance)) {
                this.navigation.stop();
                LookControl lookControl = this.followingMob.getLookControl();
                if (g <= (double) this.stopDistance || lookControl.getWantedX() == this.mob.getX() && lookControl.getWantedY() == this.mob.getY() && lookControl.getWantedZ() == this.mob.getZ()) {
                    double h = this.followingMob.getX() - this.mob.getX();
                    double i = this.followingMob.getZ() - this.mob.getZ();
                    this.navigation.moveTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i, this.speedModifier);
                }
                return;
            }
            this.navigation.moveTo(this.followingMob, this.speedModifier);
        }
    }

    static class LionFollowParentGoal extends FollowParentGoal {
        private final Lion lion;

        public LionFollowParentGoal(Lion animal, double speedModifier) {
            super(animal, speedModifier);
            this.lion = animal;
        }

        @Override
        public boolean canUse() {
            return !this.lion.isSleeping() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.lion.isSleeping() && super.canContinueToUse();
        }
    }
}
