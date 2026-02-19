package com.otterly76.ott.util.entity;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class CollisionUtils {
    public static boolean intersects(AABB box, BlockPos pos) {
        return box.intersects((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)(pos.getX() + 1), (double)(pos.getY() + 1), (double)(pos.getZ() + 1));
    }

    public static boolean collidedWithFluid(LivingEntity entity, FluidState state, BlockPos pos, Vec3 origin, Vec3 target) {
        AABB box = getFluidAABB(state, entity.level(), pos);
        return box != null && collidedWithShapeMovingFrom(entity, origin, target, List.of(box));
    }

    public static boolean collidedWithShapeMovingFrom(LivingEntity entity, Vec3 origin, Vec3 target, List<AABB> boxes) {
        AABB box = entity.getDimensions(entity.getPose()).makeBoundingBox(origin);
        Vec3 distance = target.subtract(origin);
        return collidedAlongVector(box, distance, boxes);
    }

    public static @Nullable AABB getFluidAABB(FluidState state, BlockGetter level, BlockPos pos) {
        if (state.isEmpty()) {
            return null;
        } else {
            float fluidHeight = state.getHeight(level, pos);
            return new AABB((double)pos.getX(), (double)pos.getY(), (double)pos.getZ(), (double)pos.getX() + 1.0, (double)((float)pos.getY() + fluidHeight), (double)pos.getZ() + 1.0);
        }
    }

    public static boolean collidedAlongVector(AABB entityBox, Vec3 origin, List<AABB> obstacles) {
        Vec3 center = entityBox.getCenter();
        Vec3 distance = center.add(origin);

        for(AABB obstacle : obstacles) {
            AABB box = obstacle.inflate(entityBox.getXsize() * 0.5, entityBox.getYsize() * 0.5, entityBox.getZsize() * 0.5);
            if (box.contains(distance) || box.contains(center)) {
                return true;
            }

            if (box.clip(center, distance).isPresent()) {
                return true;
            }
        }

        return false;
    }
}
