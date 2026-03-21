package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.core.EggLayingAnimal;
import com.otterly76.ott.entity.core.OttAnimal;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.entity.ai.goal.BabyHurtByTargetGoal;
import com.otterly76.ott.entity.ai.goal.BabyPanicGoal;
import com.otterly76.ott.entity.ai.goal.LayEggGoal;
import com.otterly76.ott.entity.ai.goal.SmoothFloatGoal;
import com.otterly76.ott.entity.ai.navigation.MMPathNavigatorGround;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.tags.BlockTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;

public class Hippo extends OttAnimal implements OttGeoEntity, EggLayingAnimal {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ott.hippo.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ott.hippo.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.ott.hippo.run");
    protected static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.ott.hippo.swim");
    private static final EntityDataAccessor<Boolean> HAS_EGG = SynchedEntityData.defineId(Hippo.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> IS_LAYING_EGG = SynchedEntityData.defineId(Hippo.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int layEggCounter;

    public Hippo(EntityType<? extends OttAnimal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2F)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
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
        if (spawnData == null) {
            spawnData = new AgeableMob.AgeableMobGroupData(1.0F);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return ModEntities.HIPPO.get().create(serverLevel);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SmoothFloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, net.minecraft.world.entity.animal.Bee.class, 8.0f, 1.3, 1.3));
        this.goalSelector.addGoal(1, new HippoAttackBoatsGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new LayEggGoal<>(this, 1.0D));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, true,
                entity -> entity.getType().is(ModTags.EntityTypes.HIPPO_HOSTILES) && !entity.isBaby()));
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(Items.MELON_SLICE);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HAS_EGG, false);
        builder.define(IS_LAYING_EGG, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("HasEgg", this.hasEgg());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setHasEgg(compoundTag.getBoolean("HasEgg"));
    }

    @Override
    public boolean hasEgg() {
        return this.entityData.get(HAS_EGG);
    }

    @Override
    public void setHasEgg(boolean hasEgg) {
        this.entityData.set(HAS_EGG, hasEgg);
    }

    @Override
    public boolean isLayingEgg() {
        return this.entityData.get(IS_LAYING_EGG);
    }

    @Override
    public void setLayingEgg(boolean isLayingEgg) {
        this.entityData.set(IS_LAYING_EGG, isLayingEgg);
    }

    @Override
    public int getLayEggCounter() {
        return this.layEggCounter;
    }

    @Override
    public void setLayEggCounter(int layEggCounter) {
        this.layEggCounter = layEggCounter;
    }

    @Override
    public net.minecraft.world.level.block.Block getEggBlock() {
        return Blocks.AIR;
    }

    @Override
    public net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> getEggLayableBlockTag() {
        return ModTags.Blocks.HIPPO_EGG_LAYABLE_ON;
    }

    public static boolean canSpawn(EntityType<? extends net.minecraft.world.entity.animal.Animal> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, net.minecraft.util.RandomSource random) {
        return (level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) || level.getBlockState(pos.below()).is(Blocks.WATER)) && isBrightEnoughToSpawn(level, pos);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isLayingEgg()) {
            this.setDeltaMovement(0, this.getDeltaMovement().y, 0);
        }
    }

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.25D);
        } else {
            this.setSprinting(false);
        }
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos pos, @NotNull LevelReader level) {
        if (level.getFluidState(pos).is(FluidTags.WATER)) {
            return 10.0F;
        } else {
            return super.getWalkTargetValue(pos, level);
        }
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.HIPPO_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.HIPPO_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.HIPPO_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(ModSounds.HIPPO_STEP.get(), 0.15F, 1.0F);
    }

    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    private <E extends Hippo> PlayState predicate(final AnimationState<E> event) {
        if (this.isInWaterOrBubble()) {
            event.getController().setAnimation(SWIM);
        } else if (event.isMoving()) {
            if (this.isSprinting()) {
                event.getController().setAnimation(RUN);
                event.getController().setAnimationSpeed(2.0F);
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

    private <E extends Hippo> PlayState attackPredicate(final AnimationState<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            event.getController().forceAnimationReset();
            event.getController().setAnimation(RawAnimation.begin().thenPlay("animation.ott.hippo.attack"));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    private void soundListener(SoundKeyframeEvent<Hippo> event) {
        Hippo hippo = event.getAnimatable();
        if (hippo.level().isClientSide) {
            if (event.getKeyframeData().getSound().startsWith("step")) {
                hippo.level().playLocalSound(hippo.getX(), hippo.getY(), hippo.getZ(), ModSounds.HIPPO_STEP.get(), hippo.getSoundSource(), 0.3F, hippo.getVoicePitch(), false);
            } else if (event.getKeyframeData().getSound().equals("open_mouth")) {
                hippo.level().playLocalSound(hippo.getX(), hippo.getY(), hippo.getZ(), ModSounds.HIPPO_OPEN_MOUTH.get(), hippo.getSoundSource(), 1.0F, hippo.getVoicePitch(), false);
            } else if (event.getKeyframeData().getSound().equals("close_mouth")) {
                hippo.level().playLocalSound(hippo.getX(), hippo.getY(), hippo.getZ(), ModSounds.HIPPO_CLOSE_MOUTH.get(), hippo.getSoundSource(), 1.0F, hippo.getVoicePitch(), false);
            }
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<Hippo> controller = new AnimationController<>(this, "controller", 5, this::predicate);
        controller.setSoundKeyframeHandler(this::soundListener);
        controllers.add(controller);
        controllers.add(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }

    static class HippoAttackBoatsGoal extends Goal {
        private final Hippo hippo;
        private Boat boat;

        public HippoAttackBoatsGoal(Hippo hippo) {
            this.hippo = hippo;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.hippo.isBaby()) {
                return false;
            }
            java.util.List<Boat> boats = this.hippo.level().getEntitiesOfClass(Boat.class, this.hippo.getBoundingBox().inflate(16.0D));
            if (!boats.isEmpty()) {
                for (Boat boat : boats) {
                    if (boat.getDeltaMovement().horizontalDistanceSqr() > 0.01D) {
                        this.boat = boat;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return this.boat != null && this.boat.isAlive() && this.boat.getDeltaMovement().horizontalDistanceSqr() > 0.01D && this.hippo.distanceToSqr(this.boat) < 256.0D;
        }

        @Override
        public void tick() {
            this.hippo.getLookControl().setLookAt(this.boat, 30.0F, 30.0F);
            this.hippo.getNavigation().moveTo(this.boat, 1.5D);
            if (this.hippo.distanceToSqr(this.boat) < 16.0D) {
                this.hippo.swing(InteractionHand.MAIN_HAND);
                this.boat.hurt(this.hippo.damageSources().mobAttack(this.hippo), 4.0F);
            }
        }
    }
}
