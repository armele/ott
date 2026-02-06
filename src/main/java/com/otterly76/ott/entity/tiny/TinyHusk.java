package com.otterly76.ott.entity.tiny;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class TinyHusk extends Husk {
    public TinyHusk(@NotNull EntityType<? extends Husk> entityType, @NotNull Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    public static @NotNull AttributeSupplier.Builder createAttributes() {
        return Husk.createAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.23D * 1.35D)
                .add(Attributes.SCALE, 0.5D);
    }
}
