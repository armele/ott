package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.custom.Catfish;
import com.otterly76.ott.entity.custom.Lion;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends MobMixin {
    protected CreeperMixin(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At(value = "HEAD"), method = "registerGoals")
    public void ott$onRegisterGoals(CallbackInfo ci) {
        Creeper creeper = (Creeper) (Object) this;
        creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, Lion.class, 6.0f, 1.0, 1.2));
        creeper.goalSelector.addGoal(3, new AvoidEntityGoal<>(creeper, Catfish.class, 6.0f, 1.0, 1.2));
    }

    @Override
    public boolean isBaby() {
        return this.ott$isBaby();
    }
}
