package com.otterly76.ott.mixin.common;

import com.otterly76.ott.entity.gecko.SnowGolemGeoEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.SnowGolem;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

@Mixin(SnowGolem.class)
public abstract class SnowGolemMixin extends MobMixin implements SnowGolemGeoEntity {

    @Unique
    private final AnimatableInstanceCache ott$animatableInstanceCache = GeckoLibUtil.createInstanceCache(this);

    protected SnowGolemMixin(EntityType<? extends SnowGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.ott$animatableInstanceCache;
    }
}
