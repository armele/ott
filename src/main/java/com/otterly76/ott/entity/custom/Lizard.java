package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
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

public class Lizard extends TamableAnimal implements OttGeoEntity {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Lizard.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> HAS_TAIL = SynchedEntityData.defineId(Lizard.class, EntityDataSerializers.BOOLEAN);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ott.lizard.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ott.lizard.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.ott.lizard.run");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Lizard(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 10.0F).add(Attributes.MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new SmartBodyHelper(this);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new WallClimberNavigation(this, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        Lizard lizard = ModEntities.LIZARD.get().create(level);
        if (lizard != null) {
            lizard.setVariant(this.getVariant());
        }
        return lizard;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LizardTemptGoal(this, 1.0D, Ingredient.of(Items.SPIDER_EYE), false));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public void setTame(boolean tamed, boolean broadcast) {
        super.setTame(tamed, broadcast);
        if (tamed) {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0F);
            this.setHealth(20.0F);
        } else {
            Objects.requireNonNull(this.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(10.0F);
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (this.level().isClientSide) {
            boolean flag = this.isOwnedBy(player) || this.isTame() || itemstack.is(Items.SPIDER_EYE) && !this.isTame();
            return flag ? InteractionResult.CONSUME : InteractionResult.PASS;
        } else if (this.isTame()) {
            if (this.isFood(itemstack) && this.getHealth() < this.getMaxHealth()) {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }
                this.heal(2.0F);
                return InteractionResult.SUCCESS;
            } else {
                InteractionResult interactionresult = super.mobInteract(player, hand);
                if ((!interactionresult.consumesAction() || this.isBaby()) && this.isOwnedBy(player)) {
                    this.setOrderedToSit(!this.isOrderedToSit());
                    this.jumping = false;
                    this.navigation.stop();
                    this.setTarget(null);
                    return InteractionResult.SUCCESS;
                }
                return interactionresult;
            }
        } else if (itemstack.is(Items.SPIDER_EYE)) {
            if (!player.getAbilities().instabuild) {
                itemstack.shrink(1);
            }
            if (this.random.nextInt(3) == 0) {
                this.tame(player);
                this.navigation.stop();
                this.setTarget(null);
                this.setOrderedToSit(true);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
            return InteractionResult.SUCCESS;
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.SPIDER_EYE);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.LIZARD_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return ModSounds.LIZARD_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.LIZARD_DEATH.get();
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public boolean hasTail() {
        return this.entityData.get(HAS_TAIL);
    }

    public void setHasTail(boolean hasTail) {
        this.entityData.set(HAS_TAIL, hasTail);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(HAS_TAIL, true);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("HasTail", this.hasTail());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setHasTail(compound.getBoolean("HasTail"));
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        if (!this.level().isClientSide && this.hasTail() && this.random.nextInt(4) == 0 && !this.isBaby()) {
            this.setHasTail(false);
            LizardTail tail = ModEntities.LIZARD_TAIL.get().create(this.level());
            if (tail != null) {
                tail.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                tail.setVariant(this.getVariant());
                this.level().addFreshEntity(tail);
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide && !this.hasTail() && this.tickCount % 12000 == 0) {
            this.setHasTail(true);
        }
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType reason, @Nullable SpawnGroupData spawnData) {
        Holder<Biome> holder = level.getBiome(this.blockPosition());
        if (holder.is(Biomes.SAVANNA)) {
            this.setVariant(3);
        } else if (holder.is(Biomes.DESERT)) {
            this.setVariant(2);
        } else if (holder.is(Biomes.JUNGLE) || holder.is(Biomes.SPARSE_JUNGLE) || holder.is(Biomes.BAMBOO_JUNGLE)) {
            this.setVariant(0);
        } else {
            this.setVariant(1);
        }
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Lizard> PlayState predicate(final AnimationState<E> event) {
        if (event.isMoving()) {
            if (this.isSprinting()) {
                event.getController().setAnimation(RUN);
            } else {
                event.getController().setAnimation(WALK);
            }
        } else {
            event.getController().setAnimation(IDLE);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate).setSoundKeyframeHandler(this::soundListener));
    }

    private void soundListener(SoundKeyframeEvent<Lizard> event) {
        if (this.level().isClientSide) {
            String sound = event.getKeyframeData().getSound();
            if (sound.contains("step")) {
                this.playSound(SoundEvents.CHICKEN_STEP, 0.1F, 1.0F);
            } else if (sound.equals("sleep")) {
                SoundEvent ambient = this.getAmbientSound();
                if (ambient != null) {
                    this.playSound(ambient, 0.5F, 1.0F);
                }
            }
        }
    }

    static class LizardTemptGoal extends TemptGoal {
        private final Lizard lizard;

        public LizardTemptGoal(Lizard lizard, double speedModifier, Ingredient ingredient, boolean canScare) {
            super(lizard, speedModifier, ingredient, canScare);
            this.lizard = lizard;
        }

        @Override
        public void tick() {
            super.tick();
            if (this.lizard.isTame() && this.lizard.isOrderedToSit()) {
                this.lizard.setOrderedToSit(false);
            }
        }

        @Override
        protected boolean canScare() {
            return !this.lizard.isTame() && super.canScare();
        }

        @Override
        public boolean canUse() {
            return !this.lizard.isTame() && super.canUse();
        }
    }
}
