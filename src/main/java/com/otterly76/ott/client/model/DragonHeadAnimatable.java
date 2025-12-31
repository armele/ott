package com.otterly76.ott.client.model;

import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.minecraft.world.level.block.SkullBlock;

public class DragonHeadAnimatable implements GeoAnimatable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private SkullBlock.Type headType = SkullBlock.Types.DRAGON; // Default

    public void setHeadType(SkullBlock.Type type) {
        this.headType = type;
    }

    public SkullBlock.Type getHeadType() {
        return this.headType;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public double getTick(Object itemStack) { return 0; }
}