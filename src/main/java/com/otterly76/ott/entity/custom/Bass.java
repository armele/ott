package com.otterly76.ott.entity.custom;

import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Bass extends AbstractSchoolingFish implements GeoEntity {
    protected static final RawAnimation SWIM = RawAnimation.begin().thenLoop("animation.ott.bass.swim");
    protected static final RawAnimation FLOP = RawAnimation.begin().thenLoop("animation.ott.bass.flop");
    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);

    public Bass(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public int getMaxSchoolSize() {
        return 5;
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ModItems.BASS_BUCKET.get());
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource damageSource) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return ModSounds.BASS_FLOP.get();
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }

    protected <E extends Bass> PlayState predicate(final AnimationState<E> event) {
        if (!this.isInWater()) {
            event.getController().setAnimation(FLOP);
        } else {
            event.getController().setAnimation(SWIM);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 2, this::predicate));
    }
}
