package com.otterly76.ott.mixin.common;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = ExperienceOrb.class, priority = 1500)
public abstract class ExperienceOrbMixin extends Entity {

    @Shadow
    public int value;

    @Shadow
    private int count;

    @Shadow
    private int age;

    public ExperienceOrbMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void ott$onTick(CallbackInfo ci) {
        if (!this.level().isClientSide && OttConfig.CLUMPS.ENABLED.get() && this.isAlive() && this.age % 20 == 0) {
            double radius = OttConfig.CLUMPS.RADIUS.get();
            List<ExperienceOrb> nearbyOrbs = this.level().getEntitiesOfClass(
                    ExperienceOrb.class,
                    this.getBoundingBox().inflate(radius),
                    orb -> orb != (Object) this && orb.isAlive()
            );

            for (ExperienceOrb other : nearbyOrbs) {
                this.value += other.getValue();
                this.count += ((ExperienceOrbAccessor) other).ott$getCount();
                other.discard();
            }
        }
    }

    @Override
    public void gameEvent(@NotNull Holder<GameEvent> event, Entity entity) {
        // Experience orbs should not trigger game events (vibrations)
    }
}