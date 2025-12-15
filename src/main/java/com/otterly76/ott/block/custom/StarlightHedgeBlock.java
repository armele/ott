package com.otterly76.ott.block.custom;

import com.otterly76.ott.particle.ModParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class StarlightHedgeBlock extends Block {
    private static final float SPAWN_CHANCE_PER_TICK = 0.20f;
    private static final int PARTICLES_PER_SPAWN = 1;

    private static final double FACE_NUDGE = 0.02;

    public StarlightHedgeBlock(Properties props) {
        super(props);
    }

    @Override
    public void animateTick(@NotNull BlockState state, Level level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isClientSide()) return;
        if (random.nextFloat() >= SPAWN_CHANCE_PER_TICK) return;

        double cx = pos.getX() + 0.5;
        double cy = pos.getY() + 0.5;
        double cz = pos.getZ() + 0.5;

        for (int i = 0; i < PARTICLES_PER_SPAWN; i++) {
            Direction face;
            float r = random.nextFloat();

            if (r < 0.90f) {
                // 90%: pick one of the 4 sides
                face = switch (random.nextInt(4)) {
                    case 0 -> Direction.NORTH;
                    case 1 -> Direction.SOUTH;
                    case 2 -> Direction.WEST;
                    default -> Direction.EAST;
                };
            } else {
                // 10%: top/bottom
                face = random.nextBoolean() ? Direction.UP : Direction.DOWN;
            }

            double x = cx;
            double y = cy;
            double z = cz;

            switch (face) {
                case UP, DOWN -> {
                    x = pos.getX() + random.nextDouble();
                    z = pos.getZ() + random.nextDouble();
                    y = pos.getY() + (face == Direction.UP ? 1.0 + FACE_NUDGE : -FACE_NUDGE);
                }
                case NORTH, SOUTH -> {
                    x = pos.getX() + random.nextDouble();
                    y = pos.getY() + random.nextDouble();
                    z = pos.getZ() + (face == Direction.SOUTH ? 1.0 + FACE_NUDGE : -FACE_NUDGE);
                }
                case EAST, WEST -> {
                    y = pos.getY() + random.nextDouble();
                    z = pos.getZ() + random.nextDouble();
                    x = pos.getX() + (face == Direction.EAST ? 1.0 + FACE_NUDGE : -FACE_NUDGE);
                }
            }

            // Gentle “peel off” push away from the chosen face
            double peel = 0.006;
            double xd = face.getStepX() * peel + (random.nextDouble() - 0.5) * 0.001;
            double yd = -(0.001 + random.nextDouble() * 0.0015);
            double zd = face.getStepZ() * peel + (random.nextDouble() - 0.5) * 0.001;

            // Don’t push up/down faces outward (keeps “drip” feel)
            if (face == Direction.UP || face == Direction.DOWN) {
                xd = (random.nextDouble() - 0.5) * 0.001;
                zd = (random.nextDouble() - 0.5) * 0.001;
            }

            level.addParticle(ModParticle.STARLIGHT_LEAF.get(), x, y, z, xd, yd, zd);
        }
    }
}