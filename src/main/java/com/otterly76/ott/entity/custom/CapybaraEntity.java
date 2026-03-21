package com.otterly76.ott.entity.custom;

import com.mojang.serialization.Dynamic;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
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

public class CapybaraEntity extends Animal implements GeoEntity {
    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("animation.capybara.idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("animation.capybara.walk");

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public CapybaraEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.getNavigation().setCanFloat(true);
    }

    @Override
    protected void customServerAiStep() {
        this.level().getProfiler().push("capybaraBrain");
        this.getBrain().tick((ServerLevel) this.level(), this);
        this.level().getProfiler().pop();

        this.level().getProfiler().push("capybaraActivityUpdate");
        CapybaraAI.updateActivity(this);
        this.level().getProfiler().pop();

        super.customServerAiStep();
    }

    @NotNull
    @Override
    protected Brain.Provider<CapybaraEntity> brainProvider() {
        return CapybaraAI.brainProvider();
    }

    @NotNull
    @Override
    protected Brain<?> makeBrain(@NotNull Dynamic<?> dynamic) {
        return CapybaraAI.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @NotNull
    @SuppressWarnings("unchecked")
    @Override
    public Brain<CapybaraEntity> getBrain() {
        return (Brain<CapybaraEntity>) super.getBrain();
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ModTags.ItemTags.CAPYBARA_FOOD);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return ModSounds.CAPYBARA_AMBIENT.get();
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return ModSounds.CAPYBARA_HURT.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return ModSounds.CAPYBARA_DEATH.get();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob partner) {
        CapybaraEntity baby = ModEntities.CAPYBARA.get().create(level);
        if (baby != null) {
            baby.setBaby(true);
        }
        return baby;
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean result = super.hurt(source, amount);
        if (result && !this.level().isClientSide()) {
            this.getBrain().setMemory(MemoryModuleType.IS_PANICKING, true);
            CapybaraAI.updateActivity(this);
        }
        return result;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "main", 5, this::mainPredicate));
    }

    private PlayState mainPredicate(AnimationState<CapybaraEntity> state) {
        if (state.isMoving()) {
            return state.setAndContinue(WALK);
        } else {
            return state.setAndContinue(IDLE);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.2D)
                .add(Attributes.FOLLOW_RANGE, 8.0D);
    }
}