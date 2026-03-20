package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public abstract class Deer extends TamableAnimal implements GeoEntity, Saddleable {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.sf_nba.deer.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.sf_nba.deer.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.sf_nba.deer.run");
    protected static final RawAnimation BABY_RUN = RawAnimation.begin().thenLoop("animation.sf_nba.deer.baby_run");
    protected static final RawAnimation EAT = RawAnimation.begin().thenLoop("animation.sf_nba.deer.eat");

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(Deer.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);
    private int eatAnimationTick;
    private int panicTicks = 0;

    protected Deer(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0D).add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SADDLED, false);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.6D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.APPLE), true));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.5D, 2.0D, (living) -> !this.isTame()));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.5D));
        this.goalSelector.addGoal(6, new DeerEatBlockGoal(this));
        this.goalSelector.addGoal(7, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(8, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.APPLE);
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
        } else if (itemstack.is(Items.APPLE)) {
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
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Saddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.entityData.set(SADDLED, tag.getBoolean("Saddled"));
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
    public void aiStep() {
        if (this.level().isClientSide) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }
        super.aiStep();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 10) {
            this.eatAnimationTick = 40;
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.DEER_EAT.get(), this.getSoundSource(), 1.0F, 1.0F);
        } else {
            super.handleEntityEvent(id);
        }
    }

    public boolean isEating() {
        return this.eatAnimationTick > 0;
    }

    @Override
    public void customServerAiStep() {
        super.customServerAiStep();
        this.setSprinting(this.getMoveControl().hasWanted() && this.getMoveControl().getSpeedModifier() >= 1.5D);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean lastHurt = super.hurt(source, amount);
        if (lastHurt && !this.level().isClientSide) {
            int ticks = 100 + this.random.nextInt(100);
            this.panicTicks = ticks;
            List<? extends Deer> deers = this.level().getEntitiesOfClass(Deer.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            for (Deer deer : deers) {
                deer.panicTicks = ticks;
            }
        }
        return lastHurt;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (panicTicks > 0) {
                panicTicks--;
            }
        }
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? ModSounds.DEER_AMBIENT_BABY.get() : ModSounds.DEER_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return this.isBaby() ? ModSounds.DEER_HURT_BABY.get() : ModSounds.DEER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.DEER_DEATH.get();
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(ModSounds.DEER_STEP.get(), 0.15F, 1.0F);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Deer> PlayState predicate(final AnimationState<E> event) {
        if (event.isMoving()) {
            if (this.isSprinting()) {
                if (this.isBaby()) {
                    event.getController().setAnimation(BABY_RUN);
                } else {
                    event.getController().setAnimation(RUN);
                }
                event.getController().setAnimationSpeed(2.3D);
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

    protected <E extends Deer> PlayState eatPredicate(final AnimationState<E> event) {
        if (this.isEating()) {
            event.getController().setAnimation(EAT);
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    private void soundListener(SoundKeyframeEvent<Deer> event) {
        if (this.level().isClientSide) {
            String sound = event.getKeyframeData().getSound();
            if (sound.contains("step")) {
                this.playStepSound(this.blockPosition(), this.level().getBlockState(this.blockPosition()));
            } else if (sound.equals("eat")) {
                this.level().playLocalSound(this.getX(), this.getY(), this.getZ(), ModSounds.DEER_EAT.get(), this.getSoundSource(), 1.0F, 1.0F, false);
            }
        }
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate).setSoundKeyframeHandler(this::soundListener));
        controllers.add(new AnimationController<>(this, "eat_controller", 5, this::eatPredicate).setSoundKeyframeHandler(this::soundListener));
    }

    static class DeerEatBlockGoal extends EatBlockGoal {
        private final Deer deer;
        public DeerEatBlockGoal(Deer deer) {
            super(deer);
            this.deer = deer;
        }
        @Override
        public void start() {
            super.start();
            this.deer.level().broadcastEntityEvent(this.deer, (byte)10);
        }
    }
}