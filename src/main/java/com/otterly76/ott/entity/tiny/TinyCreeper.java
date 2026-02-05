package com.otterly76.ott.entity.tiny;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TinyCreeper extends Creeper {
    public TinyCreeper(@NotNull EntityType<? extends Creeper> entityType, @NotNull Level level) {
        super(entityType, level);
    }


    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Creeper.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.25D * 1.35D)
                .add(Attributes.SCALE, 0.5D);
    }
}