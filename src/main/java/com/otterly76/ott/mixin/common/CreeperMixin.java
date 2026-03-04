package com.otterly76.ott.mixin.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Creeper.class)
public abstract class CreeperMixin extends MobMixin {
    protected CreeperMixin(EntityType<? extends Creeper> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isBaby() {
        return this.ott$isBaby();
    }
}