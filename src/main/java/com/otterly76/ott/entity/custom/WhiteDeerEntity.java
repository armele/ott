package com.otterly76.ott.entity.custom;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import com.otterly76.ott.entity.ModEntities;

public class WhiteDeerEntity extends Deer {
    public WhiteDeerEntity(EntityType<? extends Deer> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected EntityType<? extends AgeableMob> getBreedOffspringType() {
        return ModEntities.WHITE_DEER.get();
    }
}