package com.otterly76.ott.registry;

import com.otterly76.ott.util.block.CreakingHeartState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ModBlockStateProperties {
    public static final BooleanProperty TIP = BooleanProperty.create("tip");
    public static final BooleanProperty NATURAL = BooleanProperty.create("natural");
    public static final EnumProperty<CreakingHeartState> CREAKING_HEART_STATE = EnumProperty.create("creaking_heart_state", CreakingHeartState.class);
    public static final IntegerProperty HYDRATION_LEVEL = IntegerProperty.create("hydration", 0, 3);
    public static final IntegerProperty SALTY = IntegerProperty.create("salty", 0, 3);
    public static final IntegerProperty HOOPOES = IntegerProperty.create("hoopoes", 0, 3);
    public static final IntegerProperty HOOPOE_EGGS = IntegerProperty.create("hoopoe_eggs", 0, 4);
    public static final EnumProperty<com.otterly76.ott.block.properties.QuadDirection> BIG_LILY_PAD_POSITION = EnumProperty.create("position", com.otterly76.ott.block.properties.QuadDirection.class);
}