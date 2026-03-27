package com.otterly76.ott.entity.custom;

import com.otterly76.ott.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.UUID;

public class MaulerEntity extends PathfinderMob implements NeutralMob, GeoEntity {

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(MaulerEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> IS_BURROWED = SynchedEntityData.defineId(MaulerEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> ANGER_TIME = SynchedEntityData.defineId(MaulerEntity.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);

    @Nullable private UUID persistentAngerTarget;

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public MaulerEntity(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.MOVEMENT_SPEED, 0.3)
                .add(Attributes.ATTACK_DAMAGE, 8.0)
                .add(Attributes.FOLLOW_RANGE, 16.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(VARIANT, 0);
        builder.define(IS_BURROWED, false);
        builder.define(ANGER_TIME, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Variant", this.getVariant());
        tag.putBoolean("Burrowed", this.isBurrowed());
        this.addPersistentAngerSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setVariant(tag.getInt("Variant"));
        this.setBurrowed(tag.getBoolean("Burrowed"));
        this.readPersistentAngerSaveData(this.level(), tag);
    }

    public int getVariant() { return this.entityData.get(VARIANT); }
    public void setVariant(int v) { this.entityData.set(VARIANT, v); }
    public boolean isBurrowed() { return this.entityData.get(IS_BURROWED); }
    public void setBurrowed(boolean b) { this.entityData.set(IS_BURROWED, b); }

    @Override
    public int getRemainingPersistentAngerTime() { return this.entityData.get(ANGER_TIME); }
    @Override
    public void setRemainingPersistentAngerTime(int time) { this.entityData.set(ANGER_TIME, time); }
    @Override
    @Nullable
    public UUID getPersistentAngerTarget() { return this.persistentAngerTarget; }
    @Override
    public void setPersistentAngerTarget(@Nullable UUID uuid) { this.persistentAngerTarget = uuid; }
    @Override
    public void startPersistentAngerTimer() { this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random)); }

    @Override
    public void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {
        this.playSound(ModSounds.MAULER_HURT.get(), 0.15F, 1.0F);
    }

    @Override
    protected @NotNull SoundEvent getAmbientSound() { return ModSounds.MAULER_GROWL.get(); }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull DamageSource source) { return ModSounds.MAULER_HURT.get(); }

    @Override
    protected @NotNull SoundEvent getDeathSound() { return ModSounds.MAULER_DEATH.get(); }

    @Override
    public void registerControllers(AnimatableManager.@NotNull ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 5, state ->
                state.setAndContinue(IDLE)));
    }

    @Override
    public @NotNull AnimatableInstanceCache getAnimatableInstanceCache() { return geoCache; }
}
