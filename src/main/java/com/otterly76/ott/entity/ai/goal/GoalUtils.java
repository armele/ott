package com.otterly76.ott.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class GoalUtils {
    @Nullable
    public static Vec3 getRandomSwimmablePosWithSeabed(PathfinderMob pPathfinder, int pRadius, int pVerticalDistance) {
        Vec3 testPos = DefaultRandomPos.getPos(pPathfinder, pRadius, pVerticalDistance);
        int MaxSearchAmount = pRadius * pRadius * pRadius;

        for (int x = 0; testPos != null && x < MaxSearchAmount; ++x) {
            Vec3 belowPos = testPos.add(0.0, -1.0, 0.0);
            if (pPathfinder.level().getBlockState(BlockPos.containing(belowPos)).isFaceSturdy(pPathfinder.level(), BlockPos.containing(testPos), pPathfinder.getDirection().getOpposite()) && pPathfinder.level().getBlockState(BlockPos.containing(testPos)).isPathfindable(PathComputationType.WATER)) {
                return testPos;
            }

            if (x == MaxSearchAmount - 1) {
                return testPos;
            }

            testPos = DefaultRandomPos.getPos(pPathfinder, pRadius, pVerticalDistance);
        }

        return null;
    }

    @Nullable
    public static Vec3 getRandomSwimmablePosThatIsntTheSameDepth(PathfinderMob pPathfinder, int pRadius, int pVerticalDistance) {
        Vec3 testPos = DefaultRandomPos.getPos(pPathfinder, pRadius, pVerticalDistance);
        int MaxSearchAmount = pRadius * pRadius * pRadius;

        for (int x = 0; testPos != null && x < MaxSearchAmount; ++x) {
            if (Math.abs(Math.abs(testPos.y) - Math.abs(pPathfinder.position().y)) > 1.0) {
                return testPos;
            }

            if (x == MaxSearchAmount - 1) {
                return testPos;
            }

            testPos = DefaultRandomPos.getPos(pPathfinder, pRadius, pVerticalDistance);
        }

        return null;
    }
}
