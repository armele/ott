package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.goal.OtterPanicGoal;
import com.otterly76.ott.entity.core.OttAnimal;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.fluids.FluidType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;

public class OtterEntity extends OttAnimal implements OttGeoEntity {
    private static final EntityDataAccessor<Boolean> FLOATING = SynchedEntityData.defineId(OtterEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(OtterEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation RUN = RawAnimation.begin().thenLoop("run");
    private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("swim");
    private static final RawAnimation SWIM_2 = RawAnimation.begin().thenLoop("swim_2");
    private static final RawAnimation STANDING_EAT = RawAnimation.begin().thenLoop("standing_eat");
    private static final RawAnimation STANDING_EAT_CLAM = RawAnimation.begin().thenLoop("standing_eat_clam");
    private static final RawAnimation FLOATING_EAT = RawAnimation.begin().thenLoop("floating_eat");

    private static final Vec3i UNDERWATER_PICKUP_REACH = new Vec3i(1, 1, 1);

    private boolean needsSurface;
    private int huntDelay;
    private int eatDelay;
    private int floatTime;

    public OtterEntity(EntityType<? extends OtterEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new OtterMoveControl(this);
        this.lookControl = new OtterLookControl(this);
        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.setCanPickUpLoot(true);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.ATTACK_DAMAGE, 3.0D);
    }

    @SuppressWarnings("deprecation")
    public static boolean checkOtterSpawnRules(EntityType<OtterEntity> entityType, LevelAccessor levelAccessor, MobSpawnType spawnType, BlockPos blockPos, RandomSource random) {
        if (spawnType == MobSpawnType.SPAWN_EGG || spawnType == MobSpawnType.COMMAND || spawnType == MobSpawnType.SPAWNER || spawnType == MobSpawnType.TRIGGERED) return true;
        return blockPos.getY() > levelAccessor.getSeaLevel() - 16;
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FLOATING, false);
        builder.define(EATING, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new OtterPanicGoal(this, 1.6F));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 32.0F, 0.9D, 1.5D, (livingEntity -> livingEntity.equals(this.getLastHurtMob()) && this.tickCount - this.getLastHurtMobTimestamp() <= 100)));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.addGoal(3, new GoToSurfaceGoal(60));
        this.goalSelector.addGoal(4, new BreedGoal(this));
        this.goalSelector.addGoal(5, new SearchFoodGoal());
        this.goalSelector.addGoal(6, new FollowParentGoal(this));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractFish.class, 20, false, false, (fish) -> fish instanceof AbstractSchoolingFish && this.getHuntDelay() <= 0));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob ageableMob) {
        return ModEntities.OTTER.get().create(level);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("HuntDelay", this.getHuntDelay());
        compound.putBoolean("Floating", this.isFloating());
        compound.putInt("FloatTime", this.floatTime);
        compound.putBoolean("Eating", this.isEating());
        compound.putInt("EatDelay", this.eatDelay);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.huntDelay = compound.getInt("HuntDelay");
        this.setFloating(compound.getBoolean("Floating"));
        this.floatTime = compound.getInt("FloatTime");
        this.setEating(compound.getBoolean("Eating"));
        this.eatDelay = compound.getInt("EatDelay");
    }

    @Override
    public void awardKillScore(@NotNull Entity killedEntity, int i, @NotNull DamageSource damageSource) {
        super.awardKillScore(killedEntity, i, damageSource);
        if (killedEntity instanceof AbstractSchoolingFish) {
            this.huntDelay = 6000;
        }
    }

    @Override
    protected int getBaseExperienceReward() {
        return this.random.nextInt(3, 7);
    }

    @Override
    public void baseTick() {
        super.baseTick();

        if (this.tickCount % 60 == 0) {
            heal(0.5F);
        }
    }

    @Override
    public void onAddedToLevel() {
        super.onAddedToLevel();
    }

    @Override
    public void remove(@NotNull RemovalReason reason) {
        super.remove(reason);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.isInWater()) {
            this.setXRot(0);
            this.xRotO = 0;
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.isAlive() && this.isControlledByLocalInstance()) {
            if (this.isFloating()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                this.setYya(0.0F);
                this.setAirSupply(this.getMaxAirSupply());

                if (--this.floatTime <= 0) {
                    this.setFloating(false);
                }
            }

            if (this.isUnderWater() && (this.getAirSupply() < 200 || this.random.nextFloat() <= 0.001F)) {
                this.setNeedsSurface(true);
            }

            var held = getMainHandItem();
            if (this.isEating()) {
                if (!this.isFood(held)) {
                    this.setEating(false);
                } else if (this.eatDelay > 0) {
                    --this.eatDelay;
                    if (this.eatDelay % 4 == 0 && level() instanceof ServerLevel serverLevel) {
                        Vec3 mouthPos = calculateMouthPos();
                        serverLevel.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, held.copy()), mouthPos.x(), mouthPos.y(), mouthPos.z(), 2, 0.1D, 0.1D, 0.1D, 0.05D);
                    }
                } else if (level() instanceof ServerLevel level) {
                    breakAndEat(level, held);
                }
            } else if (this.isFood(held)) {
                if (this.isInWater()) {
                    if (this.isFloating()) {
                        this.startEating();
                    } else {
                        this.setNeedsSurface(true);
                    }
                } else if (this.onGround()) {
                    this.startEating();
                }
            }

            if (this.huntDelay > 0) {
                --this.huntDelay;
            }
        }
    }

    private void breakAndEat(ServerLevel level, ItemStack held) {
        Vec3 mouthPos = calculateMouthPos();
        level.sendParticles(new ItemParticleOption(ParticleTypes.ITEM, held.copy()), mouthPos.x(), mouthPos.y(), mouthPos.z(), 12, 0.1D, 0.1D, 0.1D, 0.05D);
        var sound = getMainHandItem().is(ModItems.CLAM.get()) && !breakingClamOnLand() ?
                ModSounds.OTTER_CLAM_BREAK.get()
                : ModSounds.OTTER_EAT.get();
        playSound(sound, 1.2F, 1.0F);
        eatOrOpen(level, held);
        setEating(false);
    }

    public void eatOrOpen(Level level, ItemStack itemStack) {
        if (itemStack.is(ModItems.CLAM.get())) {
            if (this.random.nextFloat() <= 0.07F) {
                Vec3 mouthPos = this.calculateMouthPos();
                ItemEntity pearl = new ItemEntity(level, mouthPos.x(), mouthPos.y(), mouthPos.z(), new ItemStack(ModItems.PEARL.get()));

                pearl.setDeltaMovement(this.getRandom().nextGaussian() * 0.05D, this.getRandom().nextGaussian() * 0.05D + 0.2D, this.getRandom().nextGaussian() * 0.05D);
                level.addFreshEntity(pearl);
            }
            level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.TURTLE_EGG_BREAK, SoundSource.NEUTRAL, 0.8F, 1.5F);
            itemStack.shrink(1);
        } else {
            eat(level, itemStack);
        }
    }

    @Override
    protected void pickUpItem(@NotNull ItemEntity itemEntity) {
        if (this.rejectedItem(itemEntity)) {
            return;
        }

        super.pickUpItem(itemEntity);
    }

    public boolean rejectedItem(ItemEntity itemEntity) {
        if (itemEntity.getOwner() != null) {
            return itemEntity.getOwner() == this;
        }
        return false;
    }

    @Override
    public boolean canHoldItem(@NotNull ItemStack itemStack) {
        return this.isFood(itemStack) && this.isHungryAt(itemStack);
    }

    public boolean isHungryAt(ItemStack foodStack) {
        return foodStack.is(ModItems.CLAM.get()) || this.getInLoveTime() <= 0;
    }

    @NotNull
    @Override
    protected Vec3i getPickupReach() {
        if (isUnderWater()) return UNDERWATER_PICKUP_REACH;
        return super.getPickupReach();
    }

    public Vec3 calculateMouthPos() {
        Vec3 viewVector = this.getViewVector(0.0F).scale(this.isFloating() ? 0.4D : 0.7D).add(0.0D, this.isFloating() ? 0.45D : -0.1D, 0.0D).scale(this.getScale());
        return new Vec3(this.getX() + viewVector.x(), this.getY() + viewVector.y(), this.getZ() + viewVector.z());
    }

    public void rejectFood() {
        if (!this.getMainHandItem().isEmpty()) {
            ItemStack thrownAway = this.getMainHandItem().copy();
            ItemEntity itemEntity = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), thrownAway);
            itemEntity.setPickUpDelay(40);
            itemEntity.setThrower(this);
            this.getMainHandItem().shrink(thrownAway.getCount());
            this.level().addFreshEntity(itemEntity);
        }
    }

    @NotNull
    @Override
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack handStack = player.getItemInHand(hand);
        if (!this.isEating() && this.isFood(handStack)) {
            if (this.getMainHandItem().isEmpty()) {
                this.setItemInHand(InteractionHand.MAIN_HAND, handStack.split(1));
                return InteractionResult.SUCCESS;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(ModTags.ItemTags.OTTER_FOOD) || stack.is(net.minecraft.tags.ItemTags.FISHES);
    }

    @Override
    public int getMaxAirSupply() {
        return 9600;
    }

    @Override
    public int getMaxFallDistance() {
        if (isUnderWater()) return 16;
        return super.getMaxFallDistance();
    }

    @Override
    public boolean isPushedByFluid(@NotNull FluidType type) {
        return false;
    }

    private void startEating() {
        if (this.isFood(this.getMainHandItem())) {
            this.eatDelay = this.getMainHandItem().is(ModItems.CLAM.get()) ? 80 : 60;
            this.setEating(true);
            if (breakingClamOnLand()) {
                playSound(ModSounds.OTTER_CLAM_BREAK_LAND.get(), 1.2F, 1.0F);
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluidTag) {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.08D, 0.0D));
    }

    static class BreedGoal extends net.minecraft.world.entity.ai.goal.BreedGoal {
        private final OtterEntity otter;

        public BreedGoal(OtterEntity otterEntity) {
            super(otterEntity, 1.0D);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.otter.isEating();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.otter.isEating();
        }
    }

    static class RandomLookAroundGoal extends net.minecraft.world.entity.ai.goal.RandomLookAroundGoal {
        private final OtterEntity otter;

        public RandomLookAroundGoal(OtterEntity otterEntity) {
            super(otterEntity);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.otter.isInWater() && !this.otter.isEating();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !this.otter.isInWater() && !this.otter.isEating();
        }
    }

    static class RandomStrollGoal extends net.minecraft.world.entity.ai.goal.RandomStrollGoal {
        private final OtterEntity otter;

        public RandomStrollGoal(OtterEntity otterEntity) {
            super(otterEntity, 1.0D, 20);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !(this.otter.isFloating() || this.otter.needsSurface() || this.otter.isEating());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !(this.otter.isFloating() || this.otter.needsSurface() || this.otter.isEating());
        }
    }

    static class FollowParentGoal extends net.minecraft.world.entity.ai.goal.FollowParentGoal {
        private final OtterEntity otter;

        public FollowParentGoal(OtterEntity otterEntity) {
            super(otterEntity, 1.2D);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return !this.otter.isEating() && super.canUse();
        }
    }

    static class LookAtPlayerGoal extends net.minecraft.world.entity.ai.goal.LookAtPlayerGoal {
        private final OtterEntity otter;

        public LookAtPlayerGoal(OtterEntity otterEntity) {
            super(otterEntity, Player.class, 8.0F);
            this.otter = otterEntity;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !(this.otter.isInWater() || this.otter.isEating());
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !(this.otter.isInWater() || this.otter.isEating());
        }
    }

    @Override
    public void travel(@NotNull Vec3 speed) {
        if (this.isControlledByLocalInstance() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), speed);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            this.calculateEntityAnimation(false);
        } else {
            super.travel(speed);
        }
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity entity) {
        if (super.doHurtTarget(entity)) {
            this.playSound(ModSounds.BITE_ATTACK.get(), this.getSoundVolume(), this.getVoicePitch());
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    private Vec3 findSurfacePosStraightUp() {
        BlockPos.MutableBlockPos curr = new BlockPos.MutableBlockPos(Mth.floor(this.getX()), Mth.floor(this.getEyeY()), Mth.floor(this.getZ()));

        boolean waterInSight = false;
        for (int i = 0; i < 40; i++) {
            BlockPos pos = curr.above(i);
            if (this.level().getFluidState(pos).is(FluidTags.WATER)) {
                waterInSight = true;
                continue;
            }

            if (waterInSight && this.level().getBlockState(pos).isAir()) {
                return Vec3.atCenterOf(pos).add(0.0D, 0.25D, 0.0D);
            }

        }

        return null;
    }

    @Nullable
    private Vec3 findAirPosition() {
        Iterable<BlockPos> blocksInRadius = BlockPos.betweenClosed(Mth.floor(this.getX() - 1.0D), Mth.floor(this.getBlockY()), Mth.floor(this.getZ() - 1.0D), Mth.floor(this.getX() + 1.0D), Mth.floor(this.getY() + 32.0D), Mth.floor(this.getZ() + 1.0D));

        for (BlockPos pos : blocksInRadius) {
            if (OtterEntity.this.level().getBlockState(pos).isAir()) {
                return Vec3.atBottomCenterOf(pos);
            }
        }

        return null;
    }

    @Override
    public float getScale() {
        return this.isBaby() ? 0.6F : 1.0F;
    }

    @NotNull
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor levelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
        spawnGroupData = super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
        if (mobSpawnType.equals(MobSpawnType.SPAWNER) && this.random.nextFloat() <= 0.2F) {
            for (int i = 0; i < this.random.nextInt(1, 4); i++) {
                OtterEntity baby = ModEntities.OTTER.get().create(this.level());
                if (baby != null) {
                    baby.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                    baby.setBaby(true);
                    levelAccessor.addFreshEntity(baby);
                }
            }
        }
        return spawnGroupData;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
        controllers.add(new AnimationController<>(this, "floating_hands_controller", 4, this::floatingHandsPredicate));
    }

    protected <T extends OtterEntity> PlayState predicate(software.bernie.geckolib.animation.AnimationState<T> event) {
        if (this.isFloating()) {
            event.getController().setAnimation(SWIM_2);
            return PlayState.CONTINUE;
        }

        if (this.isEating()) {
            if (this.getMainHandItem().is(ModItems.CLAM.get())) {
                event.getController().setAnimation(STANDING_EAT_CLAM);
                return PlayState.CONTINUE;
            }
            event.getController().setAnimation(STANDING_EAT);
            return PlayState.CONTINUE;
        }

        if (this.isInWater()) {
            event.getController().setAnimation(SWIM);
            return PlayState.CONTINUE;
        }

        if (event.isMoving()) {
            if (this.getDeltaMovement().length() >= 0.18F) {
                event.getController().setAnimation(RUN);
            } else {
                event.getController().setAnimation(WALK);
            }
        } else {
            event.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    protected <T extends OtterEntity> PlayState floatingHandsPredicate(software.bernie.geckolib.animation.AnimationState<T> event) {
        if (this.isFloating() && this.isEating()) {
            event.getController().setAnimation(FLOATING_EAT);
            return PlayState.CONTINUE;
        }
        event.getController().forceAnimationReset();
        return PlayState.STOP;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    private boolean breakingClamOnLand() {
        return !isFloating() && getMainHandItem().is(ModItems.CLAM.get());
    }

    public boolean isEating() {
        return this.entityData.get(EATING);
    }

    public void setEating(boolean eating) {
        this.entityData.set(EATING, eating);
    }

    public boolean isFloating() {
        return this.entityData.get(FLOATING);
    }

    public void setFloating(boolean floating) {
        this.entityData.set(FLOATING, floating);
    }

    public void startFloating(int time) {
        this.setFloating(true);
        this.floatTime = time;
    }

    public boolean needsSurface() {
        return this.needsSurface;
    }

    public void setNeedsSurface(boolean needsSurface) {
        this.needsSurface = needsSurface;
    }

    public int getHuntDelay() {
        return this.huntDelay;
    }

    @NotNull
    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.OTTER_AMBIENT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.OTTER_HURT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.OTTER_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    private boolean isReadyToFloat() {
        BlockPos eye = BlockPos.containing(this.getX(), this.getEyeY() + 0.25, this.getZ());
        return !this.isUnderWater() && this.level().getBlockState(eye).isAir() && this.level().getFluidState(eye.below()).is(FluidTags.WATER);
    }

    static class OtterMoveControl extends MoveControl {
        private final OtterEntity otter;

        public OtterMoveControl(OtterEntity otterEntity) {
            super(otterEntity);
            this.otter = otterEntity;
        }

        @Override
        public void tick() {
            if (this.otter.isInWater()) {
                if (!this.otter.needsSurface()) {
                    this.otter.setDeltaMovement(this.otter.getDeltaMovement().add(this.otter.getLookAngle().scale(this.otter.isFloating() ? 0.002F : 0.005F)));
                }

                if (!this.otter.isFloating()) {
                    if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                        double d0 = this.wantedX - this.mob.getX();
                        double d1 = this.wantedY - this.mob.getY();
                        double d2 = this.wantedZ - this.mob.getZ();
                        double distanceSqr = d0 * d0 + d1 * d1 + d2 * d2;

                        if (distanceSqr < 2.5E-7) {
                            this.mob.setZza(0.0F);
                        } else {
                            float yRot = (float) (Mth.atan2(d2, d0) * (180F / Math.PI)) - 90.0F;
                            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), yRot, 40.0F));
                            this.mob.yBodyRot = this.mob.getYRot();
                            this.mob.yHeadRot = this.mob.getYRot();
                            float speed = (float) (this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED));
                            this.mob.setSpeed(speed * 0.2F);

                            double horizontalDistance = Math.sqrt(d0 * d0 + d2 * d2);
                            if (Math.abs(d1) > 1.0E-5F || Math.abs(horizontalDistance) > 1.0E-5F) {
                                float xRot = -((float) (Mth.atan2(d1, horizontalDistance) * (180F / Math.PI)));
                                xRot = Mth.clamp(Mth.wrapDegrees(xRot), -180.0F, 180.0F);

                                if (this.otter.needsSurface() && xRot > 0.0F) {
                                    xRot = 0.0F;
                                }

                                this.mob.setXRot(this.rotlerp(this.mob.getXRot(), xRot, 45.0F));
                            }

                            BlockPos wantedPos = BlockPos.containing(this.wantedX, this.wantedY, this.wantedZ);
                            BlockState wantedBlockState = this.mob.level().getBlockState(wantedPos);

                            if (d1 > 0.6 && d0 * d0 + d2 * d2 < 4.0F && d1 <= 1.0D && wantedBlockState.getFluidState().isEmpty()) {
                                this.mob.getJumpControl().jump();
                                float waterFactor = this.otter.needsSurface() ? 0.08F : 0.14F;
                                this.mob.setSpeed(speed * waterFactor);
                            }

                            float f0 = Mth.cos(this.mob.getXRot() * ((float) Math.PI / 180F));
                            float f1 = Mth.sin(this.mob.getXRot() * ((float) Math.PI / 180F));
                            this.mob.zza = f0 * speed;
                            this.mob.yya = -f1 * (speed);
                        }
                    } else {
                        this.mob.setSpeed(0.0F);
                        this.mob.setXRot(this.rotlerp(this.mob.getXRot(), 0.0F, 10.0F));
                        this.mob.setYya(0.0F);
                        this.mob.setZza(0.0F);
                    }
                }
            } else {
                super.tick();
            }
        }
    }

    static class OtterLookControl extends LookControl {
        public OtterLookControl(Mob mob) {
            super(mob);
        }

        @Override
        public void tick() {
            if (!this.mob.isInWater()) {
                super.tick();
            }
        }
    }

    class GoToSurfaceGoal extends Goal {
        private final int timeoutTime;
        private Vec3 targetPos;
        private int timeoutTimer;

        private int stuckTicks;
        private double lastDist = Double.MAX_VALUE;

        public GoToSurfaceGoal(int timeoutTime) {
            this.timeoutTime = timeoutTime;
            this.timeoutTimer = timeoutTime;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return OtterEntity.this.isAlive() && OtterEntity.this.needsSurface() && !OtterEntity.this.onGround() && !OtterEntity.this.isFloating();
        }

        private void searchTargetPos() {
            Vec3 surface = OtterEntity.this.findSurfacePosStraightUp();
            if (surface != null) {
                this.targetPos = surface;
                return;
            }

            this.targetPos = OtterEntity.this.findAirPosition();
        }

        @Override
        public void start() {
            this.stuckTicks = 0;
            this.lastDist = Double.MAX_VALUE;
            searchTargetPos();
        }

        @Override
        public void tick() {
            if (OtterEntity.this.level().isClientSide) {
                return;
            }

            if (OtterEntity.this.isReadyToFloat()) {
                OtterEntity.this.setDeltaMovement(OtterEntity.this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                OtterEntity.this.setYya(0.0F);
                OtterEntity.this.setSpeed(0.0F);
                OtterEntity.this.startFloating(OtterEntity.this.getRandom().nextInt(80, 201));
                this.stop();
                return;
            }

            if (this.targetPos == null || !OtterEntity.this.level().getBlockState(BlockPos.containing(this.targetPos)).isAir()) {
                searchTargetPos();
                this.tickTimeout();
                return;
            }

            OtterEntity.this.getLookControl().setLookAt(this.targetPos.x(), this.targetPos.y(), this.targetPos.z(), 85.0F, 85.0F);
            OtterEntity.this.getNavigation().moveTo(this.targetPos.x(), this.targetPos.y(), this.targetPos.z(), 0.5);

            double dx = this.targetPos.x() - OtterEntity.this.getX();
            double dy = this.targetPos.y() - OtterEntity.this.getEyePosition().y();
            double dz = this.targetPos.z() - OtterEntity.this.getZ();

            double horiz = dx * dx + dz * dz;
            boolean navDone = OtterEntity.this.getNavigation().isDone();
            if ((dy > 0.0D) && OtterEntity.this.isUnderWater()) {
                if (navDone || horiz <= 0.25D) {
                    Vec3 v = OtterEntity.this.getDeltaMovement();
                    OtterEntity.this.setDeltaMovement(v.x * 0.6D, v.y + 0.01D, v.z * 0.6D);
                } else if (horiz <= 9.0D) {
                    OtterEntity.this.push(0.0D, 0.02D, 0.0D);
                }
            }

            double absDy = Math.abs(dy);
            if (absDy <= 0.85725D) {
                OtterEntity.this.setDeltaMovement(OtterEntity.this.getDeltaMovement().scale(0.35D));
            }

            if (absDy <= 0.1D && OtterEntity.this.isReadyToFloat()) {
                OtterEntity.this.setDeltaMovement(OtterEntity.this.getDeltaMovement().multiply(1.0D, 0.0D, 1.0D));
                OtterEntity.this.setYya(0.0F);
                OtterEntity.this.setSpeed(0.0F);
                OtterEntity.this.startFloating(OtterEntity.this.getRandom().nextInt(80, 201));
                this.stop();
                return;
            }

            double dist = dx * dx + dy * dy + dz * dz;
            if (dist > this.lastDist - 0.0001D) {
                this.stuckTicks++;
            } else {
                this.stuckTicks = 0;
            }
            this.lastDist = dist;

            if ((navDone && dist > 2.25D) || this.stuckTicks > 20) {
                if (OtterEntity.this.isReadyToFloat()) {
                    OtterEntity.this.startFloating(OtterEntity.this.getRandom().nextInt(80, 201));
                    this.stop();
                    return;
                }
                searchTargetPos();
                this.tickTimeout();
                this.stuckTicks = 0;
            }
        }

        private void tickTimeout() {
            if (this.timeoutTimer % 2 == 0) {
                ((ServerLevel) OtterEntity.this.level()).sendParticles(ParticleTypes.BUBBLE, OtterEntity.this.getRandomX(0.6D), OtterEntity.this.getY(), OtterEntity.this.getRandomZ(0.6D), 2, 0.0D, 0.1D, 0.0D, 0.0D);
            }
            if (this.timeoutTimer <= 0) {
                OtterEntity.this.playSound(ModSounds.OTTER_AMBIENT.get(), OtterEntity.this.getSoundVolume(), 0.3F);
                OtterEntity.this.rejectFood();
                this.stop();
                return;
            }
            --this.timeoutTimer;
        }

        @Override
        public void stop() {
            OtterEntity.this.setNeedsSurface(false);
            OtterEntity.this.getNavigation().stop();
            this.timeoutTimer = this.timeoutTime;
            this.targetPos = null;
            this.stuckTicks = 0;
            this.lastDist = Double.MAX_VALUE;
        }
    }

    class SearchFoodGoal extends Goal {
        public SearchFoodGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (!OtterEntity.this.getMainHandItem().isEmpty()) {
                return false;
            } else {
                List<ItemEntity> itemsInRadius = OtterEntity.this.level().getEntitiesOfClass(ItemEntity.class, OtterEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> OtterEntity.this.canHoldItem(itemEntity.getItem()) && !OtterEntity.this.rejectedItem(itemEntity)));
                return !itemsInRadius.isEmpty();
            }
        }

        @Override
        public void tick() {
            List<ItemEntity> itemsInRadius = OtterEntity.this.level().getEntitiesOfClass(ItemEntity.class, OtterEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> OtterEntity.this.canHoldItem(itemEntity.getItem()) && !OtterEntity.this.rejectedItem(itemEntity)));
            if (!itemsInRadius.isEmpty()) {
                Path path = OtterEntity.this.getNavigation().createPath(itemsInRadius.getFirst(), 0);
                OtterEntity.this.getNavigation().moveTo(path, 1.0D);
            }
        }

        @Override
        public void start() {
            List<ItemEntity> itemsInRadius = OtterEntity.this.level().getEntitiesOfClass(ItemEntity.class, OtterEntity.this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), (itemEntity -> OtterEntity.this.canHoldItem(itemEntity.getItem()) && !OtterEntity.this.rejectedItem(itemEntity)));
            if (!itemsInRadius.isEmpty()) {
                Path path = OtterEntity.this.getNavigation().createPath(itemsInRadius.getFirst(), 0);
                OtterEntity.this.getNavigation().moveTo(path, 1.0D);
            }
        }
    }
}