package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.goal.BabyHurtByTargetGoal;
import com.otterly76.ott.entity.ai.goal.BabyPanicGoal;
import com.otterly76.ott.entity.ai.goal.DistancedFollowParentGoal;
import com.otterly76.ott.entity.ai.navigation.MMPathNavigatorGround;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;
import java.util.UUID;

public class Elephant extends TamableAnimal implements NeutralMob, OttGeoEntity, Saddleable {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ott.elephant.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ott.elephant.walk2");
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.BOOLEAN);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(ModTags.ItemTags.ELEPHANT_FOOD_ITEMS);
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private UUID persistentAngerTarget;

    public Elephant(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 1.2)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.75D)
                .add(Attributes.FOLLOW_RANGE, 15.0D);
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
            spawnData = new AgeableMobGroupData(true);
        }
        AgeableMob.AgeableMobGroupData ageableMobGroupData = (AgeableMob.AgeableMobGroupData) spawnData;
        if (ageableMobGroupData.getGroupSize() > 1) {
            this.setAge(-24000);
        }
        ageableMobGroupData.increaseGroupSizeByOne();
        RandomSource random = level.getRandom();
        Objects.requireNonNull(this.getAttribute(Attributes.FOLLOW_RANGE)).addPermanentModifier(new AttributeModifier(ResourceLocation.fromNamespaceAndPath("ott", "random_spawn_bonus"), random.triangle(0.0, 0.11485000000000001), AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
        this.entityData.set(SADDLED, false);
        return spawnData;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, net.minecraft.world.entity.animal.Bee.class, 8.0f, 1.3, 1.3));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.addGoal(2, new BabyPanicGoal(this, 1.25));
        this.goalSelector.addGoal(3, new DistancedFollowParentGoal(this, 1.25, 12.0, 5.0, 3.5));
        this.goalSelector.addGoal(4, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.ELEPHANT_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.ELEPHANT_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ELEPHANT_DEATH.get();
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.isTame()) {
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                var foodProperties = itemstack.getFoodProperties(this);
                if (foodProperties != null) {
                    this.heal((float)foodProperties.nutrition());
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (itemstack.is(Items.SADDLE) && !this.isSaddled()) {
                this.equipSaddle(itemstack, SoundSource.NEUTRAL);
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (!this.isBaby() && this.isSaddled() && !player.isSecondaryUseActive()) {
                if (!this.level().isClientSide) {
                    player.startRiding(this);
                }
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
            if (player.isSecondaryUseActive()) {
                this.setOrderedToSit(!this.isOrderedToSit());
                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        } else if (this.isFood(itemstack)) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (this.random.nextInt(3) == 0 && !net.neoforged.neoforge.event.EventHooks.onAnimalTame(this, player)) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isSaddled() {
        return this.entityData.get(SADDLED);
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby() && this.isTame();
    }

    @Override
    public void equipSaddle(@Nullable ItemStack stack, @Nullable SoundSource source) {
        this.entityData.set(SADDLED, true);
        if (source != null) {
            this.level().playSound(null, this, SoundEvents.PIG_SADDLE, source, 0.5F, 1.0F);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(REMAINING_ANGER_TIME, 0);
        builder.define(SADDLED, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
        compound.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readPersistentAngerSaveData(this.level(), compound);
        this.entityData.set(SADDLED, compound.getBoolean("Saddled"));
    }

    @Override
    protected void tickRidden(@NotNull Player player, @NotNull Vec3 movement) {
        super.tickRidden(player, movement);
        this.setRot(player.getYRot(), player.getXRot() * 0.5F);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(Player player, @NotNull Vec3 movement) {
        float f = player.xxa * 0.5F;
        float f1 = player.zza;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        return new Vec3(f, 0.0D, f1);
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player player) {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return this.getFirstPassenger() instanceof Player ? (Player)this.getFirstPassenger() : null;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(REMAINING_ANGER_TIME);
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.entityData.set(REMAINING_ANGER_TIME, time);
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.persistentAngerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide) {
            this.updatePersistentAnger((ServerLevel) this.level(), true);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return ModEntities.ELEPHANT.get().create(serverLevel);
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.3D);
        } else {
            this.setSprinting(false);
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 5, this::predicate).setSoundKeyframeHandler(this::soundListener));
    }

    private void soundListener(SoundKeyframeEvent<Elephant> event) {
        if (this.level().isClientSide) {
            String sound = event.getKeyframeData().getSound();
            if (sound.contains("step")) {
                this.playSound(ModSounds.ELEPHANT_STEP.get(), 0.15F, 1.0F);
            } else if (sound.equals("drink")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.ELEPHANT_DRINK.get(), this.getSoundSource(), 0.5F, 1.0F, false);
            } else if (sound.equals("attack")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.ELEPHANT_TRUMPET.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            } else if (sound.equals("water")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.ELEPHANT_WATER.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
    }

    protected <T extends Elephant> PlayState predicate(AnimationState<T> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(WALK);
        } else {
            state.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return geoCache;
    }

    public static boolean canSpawn(EntityType<? extends Animal> type, LevelAccessor world, MobSpawnType reason, BlockPos pos, RandomSource random) {
        return world.getBlockState(pos.below()).is(net.minecraft.tags.BlockTags.ANIMALS_SPAWNABLE_ON) && isBrightEnoughToSpawn(world, pos);
    }
}
