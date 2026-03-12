package com.otterly76.ott.block.properties;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

public enum QuadDirection implements StringRepresentable {
    BOTTOM_LEFT(0, "bottom_left", new Vec3i(0, 0, 0)),
    TOP_LEFT(1, "top_left", new Vec3i(0, 0, -1)),
    TOP_RIGHT(2, "top_right", new Vec3i(1, 0, -1)),
    BOTTOM_RIGHT(3, "bottom_right", new Vec3i(1, 0, 0));

    private final int index;
    private final String name;
    private final Vec3i vec3;

    QuadDirection(int index, String name, Vec3i vec3) {
        this.index = index;
        this.name = name;
        this.vec3 = vec3;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name;
    }

    public static QuadDirection getByIndex(int index) {
        for (QuadDirection quadDirection : values()) {
            if (quadDirection.index == index) {
                return quadDirection;
            }
        }
        return BOTTOM_LEFT;
    }

    public Vec3i getOffset(Direction direction) {
        Direction clockwise = direction.getClockWise();
        return switch (this) {
            case BOTTOM_LEFT -> Vec3i.ZERO;
            case TOP_LEFT -> direction.getNormal();
            case TOP_RIGHT -> new Vec3i(direction.getNormal().getX() + clockwise.getNormal().getX(),
                    0,
                    direction.getNormal().getZ() + clockwise.getNormal().getZ());
            case BOTTOM_RIGHT -> clockwise.getNormal();
        };
    }

    public BlockPos getRelativeBlockPosOffset(Direction direction) {
        return new BlockPos(this.getOffset(direction));
    }

    public int getIndex() {
        return index;
    }

    public Vec3i getVec3() {
        return vec3;
    }
}