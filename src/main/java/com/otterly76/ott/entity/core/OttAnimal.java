package com.otterly76.ott.entity.core;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class OttAnimal extends Animal {
    protected OttAnimal(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
}
