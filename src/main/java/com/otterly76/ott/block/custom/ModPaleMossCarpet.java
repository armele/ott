package com.otterly76.ott.block.custom;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.WallSide;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class ModPaleMossCarpet extends Block {

    public static final EnumProperty<WallSide> EAST_WALL = EnumProperty.create("east", WallSide.class);
    public static final EnumProperty<WallSide> NORTH_WALL = EnumProperty.create("north", WallSide.class);
    public static final EnumProperty<WallSide> SOUTH_WALL = EnumProperty.create("south", WallSide.class);
    public static final EnumProperty<WallSide> WEST_WALL = EnumProperty.create("west", WallSide.class);

    private static final Map<Direction, EnumProperty<WallSide>> PROPERTY_BY_DIRECTION = ImmutableMap.<Direction, EnumProperty<WallSide>>builder()
            .put(Direction.EAST, EAST_WALL)
            .put(Direction.NORTH, NORTH_WALL)
            .put(Direction.SOUTH, SOUTH_WALL)
            .put(Direction.WEST, WEST_WALL)
            .build();

    private static final Map<Direction, EnumProperty<WallSide>> ANOTHER_PROPERTY_MAP = ImmutableMap.<Direction, EnumProperty<WallSide>>builder()
            .put(Direction.EAST, EAST_WALL)
            .put(Direction.NORTH, NORTH_WALL)
            .put(Direction.SOUTH, SOUTH_WALL)
            .put(Direction.WEST, WEST_WALL)
            .build();

    public ModPaleMossCarpet(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public static @Nullable EnumProperty<WallSide> getPropertyForFace(Direction direction) {
        return PROPERTY_BY_DIRECTION.get(direction);
    }
}