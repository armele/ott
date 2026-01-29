package com.otterly76.ott.neoforge.impl.registry;


import com.mojang.serialization.MapCodec;
import com.otterly76.ott.neoforge.impl.config.ConfigHandler;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.jetbrains.annotations.NotNull;

public record BreaksSeedParityCondition() implements ICondition {
    public static final BreaksSeedParityCondition INSTANCE = new BreaksSeedParityCondition();
    public static MapCodec<BreaksSeedParityCondition> CODEC;

    public boolean test(ICondition.@NotNull IContext context) {
        return ConfigHandler.getConfig().breaksSeedParity();
    }

    public @NotNull MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    static {
        CODEC = MapCodec.unit(INSTANCE);
    }
}




