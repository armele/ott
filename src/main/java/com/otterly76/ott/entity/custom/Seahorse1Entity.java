package com.otterly76.ott.entity.custom;

import com.otterly76.ott.item.ModItems;
import net.minecraft.core.BlockPos;
import com.otterly76.ott.util.entity.BucketableUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
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

import java.util.concurrent.ThreadLocalRandom;

public class Seahorse1Entity extends WaterAnimal implements GeoEntity, Bucketable {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Seahorse1Entity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(Seahorse1Entity.class, EntityDataSerializers.INT);
    
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.seahorse.idle");
    private static final RawAnimation SWIM_ANIMATION = RawAnimation.begin().thenLoop("animation.seahorse.swim");

    private final AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Seahorse1Entity(EntityType<? extends WaterAnimal> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.25d).add(Attributes.MAX_HEALTH, 4.0d);
    }

    public static boolean canSpawn(EntityType<Seahorse1Entity> type, ServerLevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return pos.getY() >= 45 && pos.getY() <= 64 && world.getBlockState(pos).is(Blocks.WATER);
    }

    @Override
    @SuppressWarnings("deprecation")
    public @Nullable SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor world, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnReason, @Nullable SpawnGroupData entityData) {
        this.entityData.set(COLOR, betterNiceColor());
        return super.finalizeSpawn(world, difficulty, spawnReason, entityData);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        this.setColor(nbt.getInt("Color"));
        this.setFromBucket(nbt.getBoolean("FromBucket"));
        super.readAdditionalSaveData(nbt);
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

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        nbt.putInt("Color", this.getColor());
        nbt.putBoolean("FromBucket", this.fromBucket());
        super.addAdditionalSaveData(nbt);
    }

    @Override
    protected void defineSynchedData(@NotNull SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
        builder.define(COLOR, betterNiceColor());
    }

    public void createChild(ServerLevel world) {
        Seahorse1Entity child = (Seahorse1Entity) getType().create(world);
        if (child != null) {
            child.setPosRaw(this.getX(), this.getY(), this.getZ());
            world.addFreshEntity(child);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(1, new RandomSwimmingGoal(this, 1.0, 10));
        this.goalSelector.addGoal(2, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, Ingredient.of(Items.SEAGRASS), false));
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.is(Items.SEAGRASS)) {
            if (!this.level().isClientSide) {
                this.gameEvent(GameEvent.ENTITY_INTERACT, this);
                createChild((ServerLevel) this.level());
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            }
            return InteractionResult.CONSUME;
        }
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.TROPICAL_FISH_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.TROPICAL_FISH_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SoundEvents.TROPICAL_FISH_HURT;
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.SEAHORSE_1_SPAWN_EGG.get()); // FIXME: Should be a bucket if we add it
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.factory;
    }

    public void setColor(int color) {
        this.entityData.set(COLOR, color);
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }

    public static int betterNiceColor() {
        return ThreadLocalRandom.current().nextInt(0x01000000);
    }

    @Override
    public void aiStep() {
        super.aiStep();
    }

    private PlayState predicate(AnimationState<? extends Seahorse1Entity> event) {
        if (event.isMoving()) {
            return event.setAndContinue(SWIM_ANIMATION);
        } else {
            return event.setAndContinue(IDLE_ANIMATION);
        }
    }
}
