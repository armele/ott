package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ai.goal.CustomRandomSwimGoal;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.ai.navigation.SmoothSwimmingMoveControlButNotBad;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.util.entity.BucketableUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Sunfish extends AbstractFish implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Sunfish.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Sunfish.class, EntityDataSerializers.INT);

    private static final RawAnimation SWIM_ANIMATION = RawAnimation.begin().thenLoop("animation.sunfish.swim");
    private static final RawAnimation FLOP_ANIMATION = RawAnimation.begin().thenLoop("animation.sunfish.flop");
    private static final RawAnimation BASK_ANIMATION = RawAnimation.begin().thenLoop("animation.sunfish.bask");

    public Sunfish(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControlButNotBad(this, 100, 6, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 4);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        SmartBodyHelper helper = new SmartBodyHelper(this);
        helper.bodyLagMoving = 0.4F;
        helper.bodyLagStill = 0.25F;
        return helper;
    }

    public static String getVariantName(int variant) {
        return switch (variant) {
            case 1 -> "chilly";
            case 2 -> "golden";
            default -> "brown";
        };
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setFromBucket(compound.getBoolean("FromBucket"));
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean pFromBucket) {
        this.entityData.set(FROM_BUCKET, pFromBucket);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        BucketableUtils.saveCustomDataToBucketTag(this, stack, (tag) -> {
            tag.putInt("BucketVariantTag", this.getVariant());
        });
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        BucketableUtils.loadDefaultDataFromBucketTag(this, tag);
        if (tag.contains("BucketVariantTag", 3)) {
            this.setVariant(tag.getInt("BucketVariantTag"));
        }
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SUNFISH_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return AbstractFish.createAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.MOVEMENT_SPEED, 0.55);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(9, new AvoidEntityGoal<>(this, Guardian.class, 8.0F, 1.0, 1.0));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(1, new CustomRandomSwimGoal(this, 1.0, 1, 30, 30, 3));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 0.8, 15) {
            @Override
            public boolean canUse() {
                return !this.mob.isInWater() && super.canUse();
            }
        });
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (this.getVariant() == 2 && this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(0.6), this.getRandomY(), this.getRandomZ(0.6), 0.0, 0.0, 0.0);
        }
    }

    @Override
    public void travel(@NotNull Vec3 pTravelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), pTravelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
            }
        } else {
            super.travel(pTravelVector);
        }
    }

    public static boolean canSpawn(EntityType<Sunfish> type, LevelAccessor accessor, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(type, accessor, reason, pos, random);
    }

    @Nullable
    @Override
    @SuppressWarnings("deprecation")
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor worldIn, @NotNull DifficultyInstance difficultyIn, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnDataIn) {
        if (this.random.nextFloat() < 0.01F) {
            this.setVariant(2);
        } else {
            Holder<Biome> holder = worldIn.getBiome(this.blockPosition());
            if (holder.is(Biomes.COLD_OCEAN) || holder.is(Biomes.DEEP_COLD_OCEAN) || holder.is(Biomes.FROZEN_OCEAN) || holder.is(Biomes.DEEP_FROZEN_OCEAN) || holder.is(Biomes.SNOWY_BEACH)) {
                this.setVariant(1);
            } else {
                this.setVariant(0);
            }
        }
        return super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource p_28281_) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level p_27480_) {
        return new WaterBoundPathNavigation(this, p_27480_);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    private <E extends GeoAnimatable> PlayState predicate(software.bernie.geckolib.animation.AnimationState<E> event) {
        if (event.isMoving() && this.isInWater()) {
            return event.setAndContinue(SWIM_ANIMATION);
        } else if (!this.isInWater() && this.onGround()) {
            return event.setAndContinue(FLOP_ANIMATION);
        } else if (!this.isInWater() && !this.onGround()) {
            return event.setAndContinue(BASK_ANIMATION);
        } else {
            return event.setAndContinue(SWIM_ANIMATION);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}