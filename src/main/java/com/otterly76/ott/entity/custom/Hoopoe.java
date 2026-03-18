package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.UUID;

public class Hoopoe extends Animal implements GeoEntity, FlyingAnimal, NeutralMob {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Byte> HOOPOE_FLAGS = SynchedEntityData.defineId(Hoopoe.class, EntityDataSerializers.BYTE);
    private static final EntityDataAccessor<Integer> ANGER = SynchedEntityData.defineId(Hoopoe.class, EntityDataSerializers.INT);
    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    @Nullable
    private UUID angryAt;
    private int cannotEnterNestTicks;
    public int ticksLeftToFindNest;
    @Nullable
    public BlockPos nestPos;
    public int timeUntilNextEgg;
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.MELON_SLICE); // Missing raw pillbug

    public Hoopoe(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.75F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.7)
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 3.0);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanPassDoors(true);
        flyingpathnavigation.setCanFloat(true);
        return flyingpathnavigation;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.4));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Wolf.class, 8.0F, 1.3, 1.3));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new HoopoeWanderGoal(this, 1.0));
        this.goalSelector.addGoal(0, new FindNestGoal());
        this.goalSelector.addGoal(1, new EnterNestGoal());
        this.goalSelector.addGoal(1, new MoveToNestGoal());
    }

    @Override
    public @NotNull ItemStack getPickedResult(@NotNull HitResult target) {
        return new ItemStack(ModItems.HOOPOE_SPAWN_EGG.get());
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        return ModEntities.HOOPOE.get().create(pLevel);
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.CHICKEN_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isImmobile() {
        return super.isImmobile();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && this.timeUntilNextEgg > 0) {
            --this.timeUntilNextEgg;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.cannotEnterNestTicks > 0) {
            --this.cannotEnterNestTicks;
        }
        if (this.ticksLeftToFindNest > 0) {
            --this.ticksLeftToFindNest;
        }
        if (this.level().isClientSide) {
            return;
        }
        if (this.tickCount % 20 == 0 && !this.isNestValid()) {
            this.nestPos = null;
        }
    }

    public void setCannotEnterNestTicks(int ticks) {
        this.cannotEnterNestTicks = ticks;
    }

    public boolean canEnterNest() {
        return this.cannotEnterNestTicks <= 0 && this.getTarget() == null && (this.level().isNight() || this.level().isRaining() || this.timeUntilNextEgg == 0);
    }

    public boolean hasNest() {
        return this.nestPos != null;
    }

    public boolean isNestValid() {
        BlockPos pos = this.nestPos;
        if (pos == null) {
            return false;
        }
        net.minecraft.world.level.block.entity.BlockEntity be = this.level().getBlockEntity(pos);
        return be instanceof com.otterly76.ott.block.entity.OakNestEntity;
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(HOOPOE_FLAGS, (byte) 0);
        builder.define(ANGER, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("CannotEnterNestTicks", this.cannotEnterNestTicks);
        compound.putInt("timeUntilNextEgg", this.timeUntilNextEgg);
        if (this.nestPos != null) {
            compound.put("NestPos", NbtUtils.writeBlockPos(this.nestPos));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.cannotEnterNestTicks = compound.getInt("CannotEnterNestTicks");
        this.timeUntilNextEgg = compound.getInt("timeUntilNextEgg");
        if (compound.contains("NestPos")) {
            this.nestPos = NbtUtils.readBlockPos(compound, "NestPos").orElse(null);
        }
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(ANGER);
    }

    @Override
    public void setRemainingPersistentAngerTime(int angerTime) {
        this.entityData.set(ANGER, angerTime);
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return this.angryAt;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID angryAt) {
        this.angryAt = angryAt;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        return false;
    }

    void startMovingTo(BlockPos pos) {
        Vec3 vec3d = Vec3.atCenterOf(pos);
        int i = 0;
        BlockPos blockPos = this.blockPosition();
        int j = (int)vec3d.y - blockPos.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }

        int k = 6;
        int l = 8;
        int m = blockPos.distManhattan(pos);
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }

        Vec3 vec3d2 = AirRandomPos.getPosTowards(this, k, l, i, vec3d, (float)Math.PI / 10F);
        if (vec3d2 != null) {
            this.navigation.setSpeedModifier(0.5F);
            this.navigation.moveTo(vec3d2.x, vec3d2.y, vec3d2.z, 1.0);
        }
    }

    private boolean doesNestHaveSpace(BlockPos pos) {
        net.minecraft.world.level.block.entity.BlockEntity be = this.level().getBlockEntity(pos);
        if (be instanceof com.otterly76.ott.block.entity.OakNestEntity nest) {
            return !nest.isFullOfHoopoes();
        }
        return false;
    }

    private abstract class NotAngryGoal extends Goal {
        public abstract boolean canHoopoeStart();
        public abstract boolean canHoopoeContinue();

        @Override
        public boolean canUse() {
            return this.canHoopoeStart() && Hoopoe.this.getTarget() == null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canHoopoeContinue() && Hoopoe.this.getTarget() == null;
        }
    }

    private class FindNestGoal extends NotAngryGoal {
        @Override
        public boolean canHoopoeStart() {
            return Hoopoe.this.ticksLeftToFindNest == 0 && !Hoopoe.this.hasNest() && Hoopoe.this.canEnterNest();
        }

        @Override
        public boolean canHoopoeContinue() {
            return false;
        }

        @Override
        public void start() {
            Hoopoe.this.ticksLeftToFindNest = 200;
            // Searching for nearby nests via POI or range check
            BlockPos blockpos = Hoopoe.this.blockPosition();
            Iterable<BlockPos> it = BlockPos.betweenClosed(blockpos.offset(-16, -8, -16), blockpos.offset(16, 8, 16));
            for (BlockPos pos : it) {
                if (Hoopoe.this.level().getBlockState(pos).is(com.otterly76.ott.block.ModBlocks.OAK_NEST.get())) {
                    if (Hoopoe.this.doesNestHaveSpace(pos)) {
                        Hoopoe.this.nestPos = pos.immutable();
                        break;
                    }
                }
            }
        }
    }

    public class MoveToNestGoal extends NotAngryGoal {
        int ticks = 0;

        MoveToNestGoal() {
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canHoopoeStart() {
            return Hoopoe.this.nestPos != null && Hoopoe.this.canEnterNest() && !Hoopoe.this.nestPos.closerToCenterThan(Hoopoe.this.position(), 2.0);
        }

        @Override
        public boolean canHoopoeContinue() {
            return this.canHoopoeStart();
        }

        @Override
        public void start() {
            this.ticks = 0;
        }

        @Override
        public void stop() {
            Hoopoe.this.navigation.stop();
        }

        @Override
        public void tick() {
            if (Hoopoe.this.nestPos != null) {
                this.ticks++;
                if (this.ticks > 600) {
                    Hoopoe.this.nestPos = null;
                } else if (!Hoopoe.this.navigation.isInProgress()) {
                    Hoopoe.this.startMovingTo(Hoopoe.this.nestPos);
                }
            }
        }
    }

    class EnterNestGoal extends NotAngryGoal {
        @Override
        public boolean canHoopoeStart() {
            BlockPos pos = Hoopoe.this.nestPos;
            if (pos != null && Hoopoe.this.canEnterNest()) {
                if (pos.closerToCenterThan(Hoopoe.this.position(), 2.0)) {
                    return Hoopoe.this.doesNestHaveSpace(pos);
                }
            }
            return false;
        }

        @Override
        public boolean canHoopoeContinue() {
            return false;
        }

        @Override
        public void start() {
            BlockPos pos = Hoopoe.this.nestPos;
            if (pos != null) {
                net.minecraft.world.level.block.entity.BlockEntity be = Hoopoe.this.level().getBlockEntity(pos);
                if (be instanceof com.otterly76.ott.block.entity.OakNestEntity nest) {
                    nest.tryEnterNest(Hoopoe.this);
                }
            }
        }
    }

    @Override
    protected void checkFallDamage(double pY, boolean pOnGround, @NotNull BlockState pState, @NotNull BlockPos pPos) {
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        if (this.isFlying()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.hoopoe.fly"));
            state.getController().setAnimationSpeed(2.0);
        } else if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.hoopoe.walk"));
            state.getController().setAnimationSpeed(1.0);
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.hoopoe.idle"));
            state.getController().setAnimationSpeed(1.0);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    static class HoopoeWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public HoopoeWanderGoal(PathfinderMob p_186224_, double p_186225_) {
            super(p_186224_, p_186225_);
        }

        @Override
        protected @Nullable Vec3 getPosition() {
            Vec3 vec3 = null;
            if (this.mob.isInWater()) {
                vec3 = LandRandomPos.getPos(this.mob, 15, 15);
            }

            if (this.mob.getRandom().nextFloat() >= this.probability) {
                vec3 = this.getTreePos();
            }

            return vec3 == null ? super.getPosition() : vec3;
        }

        @Nullable
        private Vec3 getTreePos() {
            BlockPos blockpos = this.mob.blockPosition();
            BlockPos.MutableBlockPos mutable1 = new BlockPos.MutableBlockPos();
            BlockPos.MutableBlockPos mutable2 = new BlockPos.MutableBlockPos();

            for (BlockPos blockpos1 : BlockPos.betweenClosed(Mth.floor(this.mob.getX() - 3.0), Mth.floor(this.mob.getY() - 6.0), Mth.floor(this.mob.getZ() - 3.0), Mth.floor(this.mob.getX() + 3.0), Mth.floor(this.mob.getY() + 6.0), Mth.floor(this.mob.getZ() + 3.0))) {
                if (!blockpos.equals(blockpos1)) {
                    BlockState blockstate = this.mob.level().getBlockState(mutable2.setWithOffset(blockpos1, Direction.DOWN));
                    boolean flag = blockstate.getBlock() instanceof LeavesBlock || blockstate.is(BlockTags.LOGS);
                    if (flag && this.mob.level().isEmptyBlock(blockpos1) && this.mob.level().isEmptyBlock(mutable1.setWithOffset(blockpos1, Direction.UP))) {
                        return Vec3.atBottomCenterOf(blockpos1);
                    }
                }
            }

            return null;
        }
    }
}