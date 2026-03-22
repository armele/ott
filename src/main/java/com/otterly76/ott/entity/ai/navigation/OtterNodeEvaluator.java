package com.otterly76.ott.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.AmphibiousNodeEvaluator;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import org.jetbrains.annotations.NotNull;

public class OtterNodeEvaluator extends AmphibiousNodeEvaluator {

    public OtterNodeEvaluator() {
        super(false);
    }

    @Override
    public int getNeighbors(Node @NotNull [] nodes, @NotNull Node node) {
        int walkableNeighbors = super.getNeighbors(nodes, node);
        PathType abovePathType = this.getCachedPathType(node.x, node.y + 1, node.z);
        PathType pathType = this.getCachedPathType(node.x, node.y, node.z);
        int yRange;
        if (this.mob.getPathfindingMalus(abovePathType) >= 0.0F && pathType != PathType.STICKY_HONEY) {
            yRange = this.mob.isUnderWater() && (pathType == PathType.WATER || pathType == PathType.WATER_BORDER)
                    ? 32
                    : Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
        } else {
            yRange = 0;
        }

        double floorLevel = this.getFloorLevel(new BlockPos(node.x, node.y, node.z));
        Node upNode = this.findAcceptedNode(node.x, node.y + 1, node.z, Math.max(0, yRange - 1), floorLevel, Direction.UP, pathType);
        Node downNode = this.findAcceptedNode(node.x, node.y - 1, node.z, yRange, floorLevel, Direction.DOWN, pathType);
        if (upNode != null && this.isNeighborValid(upNode, node)) {
            nodes[walkableNeighbors++] = upNode;
        }

        if (downNode != null && this.isNeighborValid(downNode, node) && pathType != PathType.TRAPDOOR) {
            nodes[walkableNeighbors++] = downNode;
        }

        return walkableNeighbors;
    }

}