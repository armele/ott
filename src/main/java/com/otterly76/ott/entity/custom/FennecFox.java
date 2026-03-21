package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.core.OttAnimal;
import com.otterly76.ott.entity.core.OttGeoEntity;
import com.otterly76.ott.entity.core.SleepingAnimal;
import com.otterly76.ott.entity.ai.goal.SleepGoal;
import com.otterly76.ott.entity.ai.navigation.MMPathNavigatorGround;
import com.otterly76.ott.entity.ai.navigation.SmartBodyHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FennecFox extends OttAnimal implements OttGeoEntity, SleepingAnimal {
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(FennecFox.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(FennecFox.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(FennecFox.class, EntityDataSerializers.BOOLEAN);

    protected static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.ott.fennec_fox.idle");
    protected static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.ott.fennec_fox.walk");
    protected static final RawAnimation RUN = RawAnimation.begin().thenLoop("animation.ott.fennec_fox.run");
    protected static final RawAnimation SLEEP = RawAnimation.begin().thenLoop("animation.ott.fennec_fox.sleep");
    protected static final RawAnimation SIT = RawAnimation.begin().thenLoop("animation.ott.fennec_fox.sit");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public FennecFox(EntityType<? extends OttAnimal> type, Level world) {
        super(type, world);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(SLEEPING, false);
        builder.define(SITTING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
        compound.putBoolean("Sleeping", this.isSleeping());
        compound.putBoolean("Sitting", this.isSitting());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
        this.setSleeping(compound.getBoolean("Sleeping"));
        this.setSitting(compound.getBoolean("Sitting"));
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public boolean canSleep() {
        return true;
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5D));
        this.goalSelector.addGoal(2, new SleepGoal<>(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, Ingredient.of(Items.SWEET_BERRIES), false));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
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
        this.setVariant(this.random.nextInt(4));
        return super.finalizeSpawn(level, difficulty, reason, spawnData);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        FennecFox child = ModEntities.FENNEC_FOX.get().create(serverLevel);
        if (child != null && ageableMob instanceof FennecFox parent) {
            child.setVariant(this.random.nextBoolean() ? this.getVariant() : parent.getVariant());
        }
        return child;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.SWEET_BERRIES);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.FOX_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.FOX_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends FennecFox> PlayState predicate(final AnimationState<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(SLEEP);
            return PlayState.CONTINUE;
        }
        if (this.isSitting()) {
            event.getController().setAnimation(SIT);
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

    private void soundListener(SoundKeyframeEvent<FennecFox> event) {
        if (this.level().isClientSide) {
            String sound = event.getKeyframeData().getSound();
            if (sound.contains("step")) {
                this.playSound(SoundEvents.FOX_SNIFF, 0.1F, 1.0F);
            } else if (sound.equals("sleep")) {
                this.playSound(SoundEvents.FOX_SLEEP, 0.5F, 1.0F);
            }
        }
    }
}
