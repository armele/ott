package com.otterly76.ott.effect;

import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.sound.ModSounds;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class SobbingMobEffect extends MobEffect {
    public SobbingMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity living, int amplifier) {
        if (!living.level().isClientSide && living.level() instanceof ServerLevel serverLevel) {
            if (living.tickCount % 40 == 0) {
                living.playSound(ModSounds.ENTITY_PLAYER_CRY.get(), 1.0F, 1.0F);
            }
            serverLevel.sendParticles(ModParticle.TEAR.get(), living.getX(), living.getEyeY(), living.getZ(), 5, 0.2, 0.2, 0.2, 0.05);
        }
        return true;
    }
}