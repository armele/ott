package com.otterly76.ott.entity.tiny;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TinyDrowned extends Drowned {
    public TinyDrowned(@NotNull EntityType<? extends Drowned> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Drowned.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23D * 1.35D)
                .add(Attributes.SCALE, 0.5D);
    }
}