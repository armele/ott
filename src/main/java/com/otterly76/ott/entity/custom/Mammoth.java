package com.otterly76.ott.entity.custom;

import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.keyframe.event.SoundKeyframeEvent;

public class Mammoth extends Elephant {
    protected static final RawAnimation MAMMOTH_IDLE = RawAnimation.begin().thenLoop("animation.ott.mammoth.idle");
    protected static final RawAnimation MAMMOTH_WALK = RawAnimation.begin().thenLoop("animation.ott.mammoth.walk");

    public Mammoth(EntityType<? extends Elephant> entityType, Level level) {
        super(entityType, level);
    }

    public int getVariant() {
        return 0; // Standard mammoth
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return ModEntities.MAMMOTH.get().create(level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, "controller", 5, this::mammothPredicate).setSoundKeyframeHandler(this::mammothSoundListener));
    }

    private void mammothSoundListener(SoundKeyframeEvent<Mammoth> event) {
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

    protected <T extends Mammoth> PlayState mammothPredicate(AnimationState<T> state) {
        if (state.isMoving()) {
            state.getController().setAnimation(MAMMOTH_WALK);
        } else {
            state.getController().setAnimation(MAMMOTH_IDLE);
        }
        return PlayState.CONTINUE;
    }
}
