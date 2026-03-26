package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.entity.BucketableUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.JumpControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.minecraft.world.entity.ai.util.RandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SeaBunnyEntity extends WaterAnimal implements Bucketable, OttGeoEntity {
    private static final EntityDataAccessor<Boolean> CLIMBING = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(SeaBunnyEntity.class, EntityDataSerializers.BOOLEAN);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation MOVE = RawAnimation.begin().thenLoop("sea_bunny_move");
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("sea_bunny");

    protected int harvestCooldown;

    public SeaBunnyEntity(EntityType<? extends SeaBunnyEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SeaBunnyMoveControl(this);
        this.jumpControl = new SeaBunnyJumpControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.08D);
    }

    @Override
    public boolean requiresCustomPersistence() {
        return super.requiresCustomPersistence() || this.fromBucket();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(CLIMBING, false);
        builder.define(VARIANT, 0);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new RandomStrollGoal(this, 1.0D));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Climbing", this.isClimbing());
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setClimbing(compound.getBoolean("Climbing"));
        this.setVariant(compound.getInt("Variant"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    @NotNull
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.SEA_BUNNY_HURT.get();
    }

    @NotNull
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.SEA_BUNNY_DEATH.get();
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @Override
    public int getBaseExperienceReward() {
        return this.random.nextInt(2, 5);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.harvestCooldown > 0) {
            this.harvestCooldown--;
        }
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack bucketStack) {
        BucketableUtils.saveDefaultDataToBucketTag(this, bucketStack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucketStack, nbt -> {
            nbt.putInt("Variant", this.getVariant());
        });
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag bucketCompound) {
        BucketableUtils.loadDefaultDataFromBucketTag(this, bucketCompound);
        setVariant(bucketCompound.getInt("Variant"));
    }

    @NotNull
    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SEA_BUNNY_BUCKET.get());
    }

    @Override
    @SuppressWarnings("deprecation")
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor levelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (mobSpawnType == MobSpawnType.BUCKET) return spawnGroupData;
        this.setVariant(this.random.nextInt(0, 3));
        return super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
    }

    @NotNull
    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_AXOLOTL;
    }

    @NotNull
    @Override
    protected PathNavigation createNavigation(@NotNull Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.setClimbing(this.horizontalCollision && this.getNavigation().isInProgress());
        }
    }

    @Override
    public float getWalkTargetValue(@NotNull BlockPos blockPos) {
        return this.level().getBlockState(blockPos).getFluidState().isEmpty() ? 1.0F : 5.0F;
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    @Override
    public void travel(@NotNull Vec3 speed) {
        super.travel(speed);
        if (this.horizontalCollision && this.onClimbable()) {
            this.setDeltaMovement(this.getDeltaMovement().subtract(0.0D, 0.12D, 0.0D));
        }
    }

    @NotNull
    @Override
    protected InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand interactionHand) {
        ItemStack handStack = player.getItemInHand(interactionHand);
        if (handStack.is(Items.BUCKET) || handStack.is(Items.WATER_BUCKET)) {
            return Bucketable.bucketMobPickup(player, interactionHand, this).orElse(super.mobInteract(player, interactionHand));
        }
        return super.mobInteract(player, interactionHand);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    protected <T extends SeaBunnyEntity> PlayState predicate(software.bernie.geckolib.animation.AnimationState<T> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(MOVE);
        } else {
            event.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public boolean isClimbing() {
        return this.entityData.get(CLIMBING);
    }

    public void setClimbing(boolean climbing) {
        this.entityData.set(CLIMBING, climbing);
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 0, 2));
    }

    static class SeaBunnyMoveControl extends MoveControl {
        public SeaBunnyMoveControl(SeaBunnyEntity seaBunny) {
            super(seaBunny);
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                double d0 = this.wantedX - this.mob.getX();
                double d2 = this.wantedZ - this.mob.getZ();
                float f = (float) (Mth.atan2(d2, d0) * (double) (180F / (float) Math.PI)) - 90.0F;

                this.mob.setYRot(this.rotlerp(this.mob.getYRot(), f, 90.0F));
                this.mob.yBodyRot = this.mob.getYRot();

                float speed = (float) this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED);
                speed *= this.mob.isInWater() ? 2.0F + (float)this.speedModifier : (float)this.speedModifier;
                this.mob.setSpeed(speed);
            } else {
                this.mob.setSpeed(0.0F);
            }
        }
    }

    static class SeaBunnyJumpControl extends JumpControl {
        public SeaBunnyJumpControl(SeaBunnyEntity seaBunny) {
            super(seaBunny);
        }

        @Override
        public void jump() {
        }
    }

    static class RandomStrollGoal extends net.minecraft.world.entity.ai.goal.RandomStrollGoal {
        public RandomStrollGoal(SeaBunnyEntity seaBunny, double speedModifier) {
            super(seaBunny, speedModifier, 20);
        }

        @Nullable
        @Override
        protected Vec3 getPosition() {
            return RandomPos.generateRandomPos(this.mob, this::getRandomBlockPos);
        }

        @Nullable
        private BlockPos getRandomBlockPos() {
            BlockPos dirPos = RandomPos.generateRandomDirection(this.mob.getRandom(), 2, 2);
            BlockPos dirRandomPos = RandomPos.generateRandomPosTowardDirection(this.mob, 2, this.mob.getRandom(), dirPos);
            BlockPos finalPos = RandomPos.moveUpOutOfSolid(dirRandomPos, this.mob.level().getMaxBuildHeight(), (blockPos) -> GoalUtils.isSolid(this.mob, blockPos));
            return this.mob.level().getBlockState(finalPos).getFluidState().isEmpty() ? null : finalPos;
        }
    }
}