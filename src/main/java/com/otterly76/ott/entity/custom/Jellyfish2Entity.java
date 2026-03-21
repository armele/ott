package com.otterly76.ott.entity.custom;

import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import com.otterly76.ott.util.entity.BucketableUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
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

public class Jellyfish2Entity extends WaterAnimal implements GeoEntity, Bucketable {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Jellyfish2Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(Jellyfish2Entity.class, EntityDataSerializers.INT);
    
    private static final RawAnimation HOVER_ANIMATION = RawAnimation.begin().thenLoop("animation.jellyfish.hover");
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.jellyfish.idle");

    private int randomTimer;
    private float tx;
    private float ty;
    private float tz;

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Jellyfish2Entity(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
        this.randomTimer = this.getRandom().nextInt(61);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 4.0d).add(Attributes.MOVEMENT_SPEED, 0.5d);
    }

    public static boolean canSpawn(EntityType<Jellyfish2Entity> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return pos.getY() >= 45 && pos.getY() <= 64 && world.getBlockState(pos).is(Blocks.WATER);
    }

    @Override
    public boolean isVisuallySwimming() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new JellyFishRandomMovementGoal(this));
        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new TryFindWaterGoal(this));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            if (this.isInWater()) {
                if (--this.randomTimer <= 0) {
                    this.randomTimer = this.getRandom().nextInt(21);
                    this.setDeltaMovement(this.tx * 1.2, this.ty * 1.6, this.tz * 1.2);
                }
            } else {
                if (!this.onGround()) {
                    this.setDeltaMovement(this.getDeltaMovement().add(0, -0.05, 0));
                }
            }
        }
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag nbt) {
        BucketableUtils.loadDefaultDataFromBucketTag(this, nbt);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        BucketableUtils.saveDefaultDataToBucketTag(this, stack);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    public void setColor(int color) {
        this.entityData.set(COLOR, color);
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        this.entityData.set(COLOR, this.random.nextInt(16));
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
        builder.define(COLOR, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("FromBucket", this.fromBucket());
        nbt.putInt("Color", this.getColor());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.setFromBucket(nbt.getBoolean("FromBucket"));
        this.setColor(nbt.getInt("Color"));
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.JELLYFISH_2_SPAWN_EGG.get()); // FIXME: Should be a bucket
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    private PlayState predicate(AnimationState<Jellyfish2Entity> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D || Math.abs(this.getDeltaMovement().y) > 1.0E-6D) {
            return event.setAndContinue(HOVER_ANIMATION);
        } else {
            return event.setAndContinue(IDLE_ANIMATION);
        }
    }

    public void setMovementVector(float x, float y, float z) {
        this.tx = x;
        this.ty = y;
        this.tz = z;
    }

    public boolean hasMovementVector() {
        return this.tx != 0.0f || this.ty != 0.0f || this.tz != 0.0f;
    }

    static class JellyFishRandomMovementGoal extends Goal {
        private final Jellyfish2Entity entity;

        public JellyFishRandomMovementGoal(Jellyfish2Entity entity) {
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            int i = this.entity.getNoActionTime();
            if (i > 100) {
                this.entity.setMovementVector(0.0f, 0.0f, 0.0f);
            } else if (this.entity.getRandom().nextInt(50) == 0 || !this.entity.wasTouchingWater || !this.entity.hasMovementVector()) {
                float f = this.entity.getRandom().nextFloat() * ((float) Math.PI * 2);
                float g = Mth.cos(f) * 0.2f;
                float h = -0.1f + this.entity.getRandom().nextFloat() * 0.2f;
                float j = Mth.sin(f) * 0.2f;
                this.entity.setMovementVector(g, h, j);
            }
        }
    }
}