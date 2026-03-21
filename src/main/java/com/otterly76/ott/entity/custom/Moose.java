package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.core.NaturalistGeoEntity;
import com.otterly76.ott.entity.ai.navigation.MMPathNavigatorGround;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
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

import java.util.UUID;

public class Moose extends TamableAnimal implements NeutralMob, NaturalistGeoEntity, Saddleable {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.moose.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.moose.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.moose.run");
    protected static final RawAnimation ATTACK = RawAnimation.begin().thenLoop("animation.sf_nba.moose.attack");

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(Moose.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Moose(EntityType<? extends TamableAnimal> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
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
        this.entityData.set(SADDLED, false);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return ModEntities.MOOSE.get().create(level);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.getItem() == Items.WHEAT;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
        return damageSource.is(net.minecraft.world.damagesource.DamageTypes.FREEZE)
                || damageSource.is(net.minecraft.world.damagesource.DamageTypes.SWEET_BERRY_BUSH)
                || super.isInvulnerableTo(damageSource);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new HurtByTargetGoal(this).setAlertOthers());
        this.goalSelector.addGoal(1, new TemptGoal(this, 1.0, Ingredient.of(Items.WHEAT), false));
        this.goalSelector.addGoal(2, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.isFood(itemstack) && !this.isTame()) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (this.random.nextInt(3) == 0 && !net.neoforged.neoforge.event.EventHooks.onAnimalTame(this, player)) {
                this.setTame(true, true);
                this.setOwnerUUID(player.getUUID());
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (this.isTame() && this.isSaddled() && !this.isBaby() && !player.isSecondaryUseActive()) {
            if (!this.level().isClientSide) {
                player.startRiding(this);
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
        builder.define(SADDLED, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
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
        return 0;
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
    }

    @Override
    public @Nullable UUID getPersistentAngerTarget() {
        return null;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
    }

    @Override
    public void startPersistentAngerTimer() {
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? ModSounds.MOOSE_AMBIENT_BABY.get() : ModSounds.MOOSE_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? ModSounds.MOOSE_HURT_BABY.get() : ModSounds.MOOSE_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? ModSounds.MOOSE_DEATH_BABY.get() : ModSounds.MOOSE_DEATH.get();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Moose> PlayState predicate(final AnimationState<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(RUN);
                event.getController().setAnimationSpeed(1.5D);
            } else {
                event.getController().setAnimation(WALK);
                event.getController().setAnimationSpeed(1.0D);
            }
            return PlayState.CONTINUE;
        } else {
            event.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    protected <E extends Moose> PlayState attackPredicate(final AnimationState<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationController.State.STOPPED)) {
            event.getController().forceAnimationReset();
            event.getController().setAnimation(ATTACK);
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate).setSoundKeyframeHandler(this::soundListener));
        controllers.add(new AnimationController<>(this, "swingController", 2, this::attackPredicate).setSoundKeyframeHandler(this::soundListener));
    }

    private void soundListener(SoundKeyframeEvent<Moose> event) {
        if (this.level().isClientSide) {
            String sound = event.getKeyframeData().getSound();
            if (sound.equals("step")) {
                this.playSound(SoundEvents.HORSE_STEP, 0.15F, 1.0F);
            } else if (sound.equals("idle")) {
                SoundEvent ambient = this.getAmbientSound();
                if (ambient != null) {
                    this.playSound(ambient, 1.0F, 1.0F);
                }
            }
        }
    }
}