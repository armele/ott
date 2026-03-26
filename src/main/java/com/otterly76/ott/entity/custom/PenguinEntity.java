package com.otterly76.ott.entity.custom;

import com.mojang.serialization.Dynamic;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
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

public class PenguinEntity extends Animal implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.penguin.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.penguin.walk");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int eggTime = this.random.nextInt(6000) + 6000;

    public PenguinEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.tickEggLaying();
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("penguinBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();

        this.level().getProfiler().push("penguinActivityUpdate");
        PenguinAI.updateActivity(this);
        this.level().getProfiler().pop();

        super.customServerAiStep();
    }

    @Override
    protected @NotNull Brain.Provider<PenguinEntity> brainProvider() {
        return PenguinAI.brainProvider();
    }

    @Override
    protected @NotNull Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return PenguinAI.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @SuppressWarnings("unchecked")
    @Override
    public @NotNull Brain<PenguinEntity> getBrain() {
        return (Brain<PenguinEntity>) super.getBrain();
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ModTags.ItemTags.PENGUIN_FOOD);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? ModSounds.PENGUIN_AMBIENT_BABY.get() : ModSounds.PENGUIN_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return this.isBaby() ? ModSounds.PENGUIN_HURT_BABY.get() : ModSounds.PENGUIN_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isBaby() ? ModSounds.PENGUIN_DEATH_BABY.get() : ModSounds.PENGUIN_DEATH.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        PenguinEntity baby = ModEntities.PENGUIN.get().create(level);
        if (baby != null) {
            baby.setBaby(true);
        }
        return baby;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("EggTime", this.eggTime);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("EggTime")) {
            this.eggTime = compound.getInt("EggTime");
        }
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide()) {
            this.getBrain().setMemory(MemoryModuleType.IS_PANICKING, true);
            PenguinAI.updateActivity(this);
        }
        return result;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 5, this::mainPredicate));
    }

    private PlayState mainPredicate(AnimationState<PenguinEntity> state) {
        if (state.isMoving()) {
            return state.setAndContinue(WALK);
        } else {
            return state.setAndContinue(IDLE);
        }
    }

    private void tickEggLaying() {
        if (this.level().isClientSide() || this.isBaby()) return;
        if (--this.eggTime <= 0) {
            this.playSound(SoundEvents.CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            this.spawnAtLocation(ModItems.PENGUIN_EGG.get());
            this.gameEvent(GameEvent.ENTITY_PLACE, this);
            this.eggTime = this.random.nextInt(6000) + 6000;
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.175D)
                .add(Attributes.FOLLOW_RANGE, 8.0D);
    }
}

