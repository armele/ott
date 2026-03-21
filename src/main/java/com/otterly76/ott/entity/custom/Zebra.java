package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.core.OttGeoEntity;
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
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
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

public class Zebra extends AbstractChestedHorse implements OttGeoEntity, Saddleable {
    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ott.zebra.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ott.zebra.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.ott.zebra.run");
    protected static final RawAnimation REAR = RawAnimation.begin().thenPlay("animation.ott.zebra.rear");

    private static final EntityDataAccessor<Boolean> SADDLED = SynchedEntityData.defineId(Zebra.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Zebra(EntityType<? extends AbstractChestedHorse> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return createBaseHorseAttributes().add(Attributes.MAX_HEALTH, 15.0).add(Attributes.MOVEMENT_SPEED, 0.225).add(Attributes.JUMP_STRENGTH, 0.5);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        this.entityData.set(SADDLED, false);
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(3, new ZebraTemptGoal(this, 1.2, Ingredient.of(Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE), false));
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return ModEntities.ZEBRA.get().create(level);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return Ingredient.of(Items.APPLE, Items.GOLDEN_APPLE, Items.ENCHANTED_GOLDEN_APPLE).test(stack);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.isFood(itemstack) && !this.isBaby() && !this.isTamed()) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            this.setEating(true);
            if (this.random.nextInt(3) == 0 && !net.neoforged.neoforge.event.EventHooks.onAnimalTame(this, player)) {
                this.setTamed(true);
                this.setOwnerUUID(player.getUUID());
                this.level().broadcastEntityEvent(this, (byte)7);
            } else {
                this.level().broadcastEntityEvent(this, (byte)6);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        if (this.isTamed() && !this.isSaddled() && !this.isBaby() && itemstack.is(Items.SADDLE)) {
            this.equipSaddle(itemstack, SoundSource.NEUTRAL);
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
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
        return this.isAlive() && !this.isBaby() && this.isTamed();
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
    protected SoundEvent getAmbientSound() {
        return ModSounds.ZEBRA_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.ZEBRA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.ZEBRA_DEATH.get();
    }

    @Override
    protected SoundEvent getAngrySound() {
        return ModSounds.ZEBRA_REAR.get();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Zebra> PlayState predicate(final AnimationState<E> event) {
        if (this.isStanding()) {
            event.getController().setAnimation(REAR);
            return PlayState.CONTINUE;
        }
        if (event.isMoving()) {
            if (this.isSprinting()) {
                event.getController().setAnimation(RUN);
            } else {
                event.getController().setAnimation(WALK);
            }
            return PlayState.CONTINUE;
        }
        event.getController().setAnimation(IDLE);
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate).setSoundKeyframeHandler(this::soundListener));
    }

    private void soundListener(SoundKeyframeEvent<Zebra> event) {
        if (this.level().isClientSide) {
            String sound = event.getKeyframeData().getSound();
            if (sound.contains("step")) {
                this.playSound(SoundEvents.HORSE_STEP, 0.15F, 1.0F);
            } else if (sound.equals("rear")) {
                this.playSound(ModSounds.ZEBRA_REAR.get(), 1.0F, 1.0F);
            }
        }
    }

    static class ZebraTemptGoal extends TemptGoal {
        private final Zebra zebra;

        public ZebraTemptGoal(Zebra zebra, double speedModifier, Ingredient ingredient, boolean canScare) {
            super(zebra, speedModifier, ingredient, canScare);
            this.zebra = zebra;
        }

        @Override
        public boolean canUse() {
            return !this.zebra.isTamed() && super.canUse();
        }

        @Override
        public void tick() {
            super.tick();
            if (this.zebra.getTarget() != null && this.zebra.distanceToSqr(this.zebra.getTarget()) < 6.25) {
                this.zebra.getNavigation().stop();
            }
        }
    }
}
