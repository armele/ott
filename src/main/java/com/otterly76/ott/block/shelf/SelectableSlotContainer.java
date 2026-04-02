package com.otterly76.ott.block.shelf;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.OptionalInt;

public interface SelectableSlotContainer {
    int getRows();
    int getColumns();

    default OptionalInt getHitSlot(BlockHitResult hitResult, Direction direction) {
        Direction direction1 = hitResult.getDirection();
        if (direction != direction1) {
            return OptionalInt.empty();
        } else {
            Vec3 vec3 = hitResult.getLocation().subtract(Vec3.atLowerCornerOf(hitResult.getBlockPos()));
            return switch (direction1) {
                case NORTH -> OptionalInt.of(getSection((float)(1.0 - vec3.x), getColumns()) + getSection((float)(1.0 - vec3.y), getRows()) * getColumns());
                case SOUTH -> OptionalInt.of(getSection((float)vec3.x, getColumns()) + getSection((float)(1.0 - vec3.y), getRows()) * getColumns());
                case WEST -> OptionalInt.of(getSection((float)vec3.z, getColumns()) + getSection((float)(1.0 - vec3.y), getRows()) * getColumns());
                case EAST -> OptionalInt.of(getSection((float)(1.0 - vec3.z), getColumns()) + getSection((float)(1.0 - vec3.y), getRows()) * getColumns());
                case DOWN, UP -> OptionalInt.empty();
            };
        }
    }

    private static int getSection(float p_262047_, int p_262100_) {
        int i = (int)(p_262047_ * (float)p_262100_);
        return Math.clamp(i, 0, p_262100_ - 1);
    }
}
