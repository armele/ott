package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.item.ModItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class KoiFishEntity extends AbstractSchoolingFish implements OttGeoEntity {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(KoiFishEntity.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation SWIM = RawAnimation.begin().thenLoop("koi_fish_swim");
    private static final RawAnimation ON_LAND = RawAnimation.begin().thenLoop("koi_fish_on_land");

    public KoiFishEntity(EntityType<? extends KoiFishEntity> entityType, Level level) {
        super(entityType, level);
    }

    @NotNull
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.4D);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 1);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, OtterEntity.class, 8.0F, 1.7D, 1.4D));
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack bucketStack) {
        super.saveToBucketTag(bucketStack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, bucketStack, nbt -> {
            nbt.putInt("Variant", getVariant());
        });
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag bucketCompound) {
        super.loadFromBucketTag(bucketCompound);
        setVariant(bucketCompound.getInt("Variant"));
    }

    @Override
    public int getMaxSchoolSize() {
        return 6;
    }

    @Override
    protected int getBaseExperienceReward() {
        return this.random.nextInt(1, 4);
    }

    @NotNull
    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    @NotNull
    @Override
    protected SoundEvent getHurtSound(@NotNull net.minecraft.world.damagesource.DamageSource damageSource) {
        return SoundEvents.TROPICAL_FISH_HURT;
    }

    @NotNull
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 0.8F;
    }

    @NotNull
    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.KOI_FISH_BUCKET.get());
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor levelAccessor, @NotNull DifficultyInstance difficultyInstance, @NotNull MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (mobSpawnType == MobSpawnType.BUCKET) return spawnGroupData;
        this.setVariant(this.random.nextInt(1, 22));
        return super.finalizeSpawn(levelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 4, this::predicate));
    }

    protected <T extends KoiFishEntity> PlayState predicate(software.bernie.geckolib.animation.AnimationState<T> event) {
        if (this.isInWater()) {
            event.getController().setAnimation(SWIM);
        } else {
            event.getController().setAnimation(ON_LAND);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, Mth.clamp(variant, 1, 21));
    }
}