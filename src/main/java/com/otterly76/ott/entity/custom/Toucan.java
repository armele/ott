package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.CommonHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Set;

public class Toucan extends TamableAnimal implements GeoEntity, FlyingAnimal {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Toucan.class, EntityDataSerializers.INT);
    public static final Ingredient TEMPTATION_ITEM = Ingredient.of(Items.MELON_SLICE);
    private static final Set<Item> TAME_FOOD = Set.of(Items.MELON_SLICE);
    private int ticksSinceEaten;
    public int timeUntilNextEgg;

    public Toucan(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
        this.moveControl = new FlyingMoveControl(this, 10, false);
        this.setPathfindingMalus(PathType.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_FIRE, -1.0F);
        this.setPathfindingMalus(PathType.COCOA, -1.0F);
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
                .add(Attributes.FLYING_SPEED, 0.5)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.4));
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.25, TEMPTATION_ITEM, false));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new ToucanWanderGoal(this, 1.0));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0, 3.0F, 7.0F));
        this.goalSelector.addGoal(8, new ToucanCollectMelons(1.2, 16, 12));
    }

    @Override
    public @NotNull ItemStack getPickedResult(@NotNull HitResult target) {
        return new ItemStack(ModItems.TOUCAN_SPAWN_EGG.get());
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return TEMPTATION_ITEM.test(pStack);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && this.isAlive() && !this.isBaby() && --this.timeUntilNextEgg <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.TOUCAN_EGG.get());
            this.timeUntilNextEgg = this.random.nextInt(6000) + 6000;
        }
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel pLevel, @NotNull AgeableMob pOtherParent) {
        Toucan toucan = ModEntities.TOUCAN.get().create(pLevel);
        if (toucan != null) {
            int i = this.random.nextBoolean() ? this.getVariant() : ((Toucan) pOtherParent).getVariant();
            toucan.setVariant(i);
            if (this.isTame()) {
                toucan.setOwnerUUID(this.getOwnerUUID());
                toucan.setTame(true, true);
            }
        }
        return toucan;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        if (!this.isTame() && TAME_FOOD.contains(itemstack.getItem())) {
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (!this.isSilent()) {
                this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PARROT_EAT, this.getSoundSource(), 1.0F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
            }
            if (!this.level().isClientSide) {
                if (this.random.nextInt(5) == 0) {
                    this.tame(pPlayer);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (itemstack.is(Items.COOKIE)) {
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            this.addEffect(new MobEffectInstance(MobEffects.POISON, 900));
            if (pPlayer.isCreative() || !this.isInvulnerable()) {
                this.hurt(this.damageSources().playerAttack(pPlayer), Float.MAX_VALUE);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (this.isTame() && this.isOwnedBy(pPlayer) && !this.level().isClientSide && !this.isFood(itemstack)) {
            this.setOrderedToSit(!this.isOrderedToSit());
            this.jumping = false;
            this.navigation.stop();
            this.setTarget(null);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(pPlayer, pHand);
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (this.isInvulnerableTo(pSource)) {
            return false;
        }
        if (!this.level().isClientSide) {
            this.setOrderedToSit(false);
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        FlyingPathNavigation flyingpathnavigation = new FlyingPathNavigation(this, pLevel);
        flyingpathnavigation.setCanOpenDoors(false);
        flyingpathnavigation.setCanPassDoors(true);
        flyingpathnavigation.setCanFloat(true);
        return flyingpathnavigation;
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull net.minecraft.nbt.CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull net.minecraft.nbt.CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    public static boolean canSpawn(EntityType<Toucan> pParrot, LevelAccessor pLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
        return pLevel.getBlockState(pPos.below()).is(BlockTags.PARROTS_SPAWNABLE_ON) && isBrightEnoughToSpawn(pLevel, pPos);
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSounds.TOUCAN_AMBIENT.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ModSounds.TOUCAN_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return ModSounds.TOUCAN_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.PARROT_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    @Override
    public @NotNull Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.5F * this.getEyeHeight(), this.getBbWidth() * 0.4F);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
        this.setVariant(this.random.nextFloat() <= 0.5F ? 1 : 0);
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private <T extends GeoEntity> PlayState predicate(AnimationState<T> state) {
        if (this.isFlying()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.toucan.fly"));
        } else if (this.isInSittingPose()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.toucan.sit"));
        } else if (state.isMoving()) {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.toucan.walk"));
        } else {
            state.getController().setAnimation(RawAnimation.begin().thenLoop("animation.toucan.idle"));
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    static class ToucanWanderGoal extends WaterAvoidingRandomFlyingGoal {
        public ToucanWanderGoal(PathfinderMob p_186224_, double p_186225_) {
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

    public class ToucanCollectMelons extends MoveToBlockGoal {
        private static final int WAIT_TICKS = 40;
        protected int ticksWaited;

        public ToucanCollectMelons(double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(Toucan.this, pSpeedModifier, pSearchRange, pVerticalSearchRange);
        }

        @Override
        public double acceptedDistance() {
            return 2.0;
        }

        @Override
        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        @Override
        protected boolean isValidTarget(@NotNull LevelReader pLevel, @NotNull BlockPos pPos) {
            BlockState blockstate = pLevel.getBlockState(pPos);
            return blockstate.is(Blocks.MELON) || blockstate.is(Blocks.PUMPKIN);
        }

        @Override
        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= WAIT_TICKS) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            }
            super.tick();
        }

        protected void onReachedTarget() {
            if (CommonHooks.canEntityDestroy(Toucan.this.level(), this.blockPos, Toucan.this)) {
                BlockState blockstate = Toucan.this.level().getBlockState(this.blockPos);
                if (blockstate.is(Blocks.MELON)) {
                    this.pickMelon(blockstate);
                } else if (blockstate.is(Blocks.PUMPKIN)) {
                    this.pickPumpkin(blockstate);
                }
            }
        }

        private void pickMelon(BlockState pState) {
            ItemStack itemstack = Toucan.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemstack.isEmpty()) {
                Toucan.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.MELON_SLICE));
            } else {
                Toucan.this.spawnAtLocation(Items.MELON_SLICE, Toucan.this.random.nextInt(2) + 1);
            }
            Toucan.this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
            Toucan.this.level().destroyBlock(this.blockPos, false);
        }

        private void pickPumpkin(BlockState pState) {
            ItemStack itemstack = Toucan.this.getItemBySlot(EquipmentSlot.MAINHAND);
            if (itemstack.isEmpty()) {
                Toucan.this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.PUMPKIN_PIE)); // User code had Items.f_42046_ which is Pumpkin seeds, but spawn Items.f_42499_ which is Pumpkin Pie?
            } else {
                Toucan.this.spawnAtLocation(Items.PUMPKIN_PIE, Toucan.this.random.nextInt(2) + 1);
            }
            Toucan.this.playSound(SoundEvents.ITEM_PICKUP, 1.0F, 1.0F);
            Toucan.this.level().destroyBlock(this.blockPos, false);
        }

        @Override
        public boolean canUse() {
            return Toucan.this.isTame() && !Toucan.this.isBaby() && super.canUse();
        }

        @Override
        public void start() {
            this.ticksWaited = 0;
            super.start();
        }
    }
}
