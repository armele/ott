package com.otterly76.ott.mixin.common;

import net.minecraft.world.entity.monster.AbstractSkeleton;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractSkeleton.class)
public abstract class AbstractSkeletonMixin extends MobMixin {
    protected AbstractSkeletonMixin(net.minecraft.world.entity.EntityType<? extends AbstractSkeleton> entityType, net.minecraft.world.level.Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isBaby() {
        return this.ott$isBaby();
    }
}
