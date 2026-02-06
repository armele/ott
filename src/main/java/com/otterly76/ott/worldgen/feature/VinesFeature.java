package com.otterly76.ott.worldgen.feature;

import com.otterly76.ott.worldgen.feature.config.VinesConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Optional;

public class VinesFeature extends Feature<VinesConfig> {
    public static final VinesFeature FEATURE = new VinesFeature();

    public VinesFeature() {
        super(VinesConfig.CODEC);
    }

    public boolean place(FeaturePlaceContext<VinesConfig> context) {
        VinesConfig config = context.config();
        WorldGenLevel level = context.level();
        BlockPos.MutableBlockPos pos = context.origin().mutable();
        Optional<Block> states = config.blocks().getRandom(context.random());
        if (states.isEmpty()) {
            return false;
        } else {
            boolean anyPlaced = false;

            for(int i = 0; i < config.maxLength().sample(context.random()) && level.isEmptyBlock(pos); ++i) {
                Block vine = states.get();
                boolean placed = false;

                for(Direction direction : Direction.values()) {
                    if (direction != Direction.DOWN) {
                        if (VineBlock.isAcceptableNeighbour(level, pos.relative(direction), direction) && config.canPlaceOn(level.getBlockState(pos.relative(direction)))) {
                            level.setBlock(pos, vine.defaultBlockState().setValue(VineBlock.getPropertyForFace(direction), true), 2);
                            placed = true;
                        }

                        BlockState aboveState = level.getBlockState(pos.above());
                        if (aboveState.getBlock() instanceof VineBlock && (aboveState.getValue(VineBlock.NORTH) || aboveState.getValue(VineBlock.EAST) || aboveState.getValue(VineBlock.SOUTH) || aboveState.getValue(VineBlock.WEST))) {
                            level.setBlock(pos, vine.withPropertiesOf(aboveState).setValue(VineBlock.UP, false), 2);
                            placed = true;
                        }
                    }
                }

                if (!placed) {
                    break;
                }

                anyPlaced = true;
                pos.move(Direction.DOWN);
            }

            return anyPlaced;
        }
    }
}
