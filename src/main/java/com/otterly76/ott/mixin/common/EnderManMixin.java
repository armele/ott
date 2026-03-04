package com.otterly76.ott.mixin.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnderMan.class)
public abstract class EnderManMixin extends MobMixin {
    protected EnderManMixin(EntityType<? extends EnderMan> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isBaby() {
        return this.ott$isBaby();
    }
}