package com.otterly76.ott.events;

import com.otterly76.ott.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.BonemealEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class UniversalBonemealHandler {
    private static final int MAX_COLUMN_HEIGHT = 16; // default upper bound for tall growth

    @SubscribeEvent
    public static void onBonemeal(BonemealEvent event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();

        // Server only for world changes
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }

        // 1) Augment vanilla grass behavior: also spread to adjacent dirt.
        if (state.is(Blocks.GRASS_BLOCK)) {
            spreadToAdjacent(serverLevel, pos, Blocks.DIRT.defaultBlockState(), Blocks.GRASS_BLOCK.defaultBlockState());
            // Let vanilla continue to grow grass/flowers (do NOT cancel)
            return;
        }

        ItemStack stack = event.getStack();
        boolean handled = false;

        // 2) Sugar Cane: grow by 1 up to MAX_COLUMN_HEIGHT
        if (state.is(Blocks.SUGAR_CANE)) {
            handled = growColumn(serverLevel, pos, Blocks.SUGAR_CANE);
        }
        // 3) Cactus: grow by 1 up to MAX_COLUMN_HEIGHT
        else if (state.is(Blocks.CACTUS)) {
            handled = growColumn(serverLevel, pos, Blocks.CACTUS);
        }
        // 4) Mycelium: spread surface + mushrooms
        else if (state.is(Blocks.MYCELIUM)) {
            boolean changed = false;
            changed |= spreadToAdjacent(serverLevel, pos, Blocks.DIRT.defaultBlockState(), Blocks.MYCELIUM.defaultBlockState());
            changed |= spreadToAdjacent(serverLevel, pos, Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.MYCELIUM.defaultBlockState());
            changed |= scatterMushrooms(serverLevel, pos);
            handled = changed;
        }
        // 5) Podzol: spread surface + ferns
        else if (state.is(Blocks.PODZOL)) {
            boolean changed = false;
            changed |= spreadToAdjacent(serverLevel, pos, Blocks.DIRT.defaultBlockState(), Blocks.PODZOL.defaultBlockState());
            changed |= spreadToAdjacent(serverLevel, pos, Blocks.GRASS_BLOCK.defaultBlockState(), Blocks.PODZOL.defaultBlockState());
            changed |= scatterFerns(serverLevel, pos);
            handled = changed;
        }
        // 6) Flowers: replicate same flower nearby
        else if (state.is(BlockTags.FLOWERS)) {
            handled = scatterSameBlock(serverLevel, pos, state.getBlock());
        }
        // 7) Dead Bush: replicate nearby
        else if (state.is(Blocks.DEAD_BUSH)) {
            handled = scatterSameBlock(serverLevel, pos, Blocks.DEAD_BUSH);
        }
        // 8) Vines: extend downward and add lateral faces
        else if (state.is(Blocks.VINE)) {
            handled = growVines(serverLevel, pos, state);
        }
        // 9) Lily pad: spread over nearby water
        else if (state.is(Blocks.LILY_PAD)) {
            handled = spreadLilyPads(serverLevel, pos);
        }
        // 10) Coral plants/fans -> grow coral blocks (underwater)
        else if (isCoralPlantOrFan(state.getBlock())) {
            handled = promoteToCoralBlock(serverLevel, pos, state.getBlock());
        }
        // 11) Chorus plant/flower: grow by 1 and place flower at the new tip (cap 16)
        else if (state.is(Blocks.CHORUS_PLANT) || state.is(Blocks.CHORUS_FLOWER)) {
            handled = growChorusOne(serverLevel, pos);
        }

        // 12) Nether Wart: advance age by 1 up to max (3)
        else if (state.is(Blocks.NETHER_WART)) {
            handled = incrementNetherWartAge(serverLevel, pos, state);
        }
        // 13) Pumpkin/Melon stems: advance stem age by 1 up to max (7), then produce fruit
        else if (state.getBlock() == Blocks.PUMPKIN_STEM || state.getBlock() == Blocks.MELON_STEM || state.getBlock() instanceof StemBlock) {
            if (state.hasProperty(BlockStateProperties.AGE_7)) {
                int age = state.getValue(BlockStateProperties.AGE_7);
                if (age < 7) {
                    serverLevel.setBlock(pos, state.setValue(BlockStateProperties.AGE_7, age + 1), 3);
                    handled = true;
                } else {
                    // Try to produce fruit
                    Block fruit = (state.getBlock() == Blocks.PUMPKIN_STEM) ? Blocks.PUMPKIN : Blocks.MELON;
                    for (Direction d : Direction.Plane.HORIZONTAL) {
                        BlockPos fruitPos = pos.relative(d);
                        if (serverLevel.isEmptyBlock(fruitPos) && serverLevel.getBlockState(fruitPos.below()).is(BlockTags.DIRT)) {
                            serverLevel.setBlock(fruitPos, fruit.defaultBlockState(), 3);
                            Block attachedStem = (fruit == Blocks.PUMPKIN) ? Blocks.ATTACHED_PUMPKIN_STEM : Blocks.ATTACHED_MELON_STEM;
                            serverLevel.setBlock(pos, attachedStem.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, d), 3);
                            handled = true;
                            break;
                        }
                    }
                }
            }
        }
        // 14) Twisting Vines: grow upward by 1 up to cap
        else if (state.is(Blocks.TWISTING_VINES) || state.is(Blocks.TWISTING_VINES_PLANT)) {
            handled = growDirectionalVine(serverLevel, pos, Direction.UP, Blocks.TWISTING_VINES, Blocks.TWISTING_VINES_PLANT);
        }
        // 15) Weeping Vines: grow downward by 1 up to cap
        else if (state.is(Blocks.WEEPING_VINES) || state.is(Blocks.WEEPING_VINES_PLANT)) {
            handled = growDirectionalVine(serverLevel, pos, Direction.DOWN, Blocks.WEEPING_VINES, Blocks.WEEPING_VINES_PLANT);
        }
        // 16) Cave Vines: grow downward by 1 up to cap
        else if (state.is(Blocks.CAVE_VINES) || state.is(Blocks.CAVE_VINES_PLANT)) {
            handled = growDirectionalVine(serverLevel, pos, Direction.DOWN, Blocks.CAVE_VINES, Blocks.CAVE_VINES_PLANT);
        }
        // 17) Sea Grass: spread nearby + grow tall
        else if (state.is(Blocks.SEAGRASS)) {
            // Grow tall if possible
            BlockPos up = pos.above();
            if (serverLevel.getBlockState(up).is(Blocks.WATER) && serverLevel.getFluidState(up).getAmount() == 8) {
                serverLevel.setBlock(pos, Blocks.TALL_SEAGRASS.defaultBlockState().setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.LOWER), 3);
                serverLevel.setBlock(up, Blocks.TALL_SEAGRASS.defaultBlockState().setValue(TallSeagrassBlock.HALF, DoubleBlockHalf.UPPER), 3);
            }
            spreadSeaGrass(serverLevel, pos);
            handled = true;
        }
        else if (state.is(Blocks.TALL_SEAGRASS)) {
            handled = spreadSeaGrass(serverLevel, pos);
        }
        // 18) Kelp: grow upward to surface
        else if (state.is(Blocks.KELP) || state.is(Blocks.KELP_PLANT)) {
            handled = growKelpOne(serverLevel, pos);
        }
        // 19) Dirt Path: revert to Grass/Mycelium/Podzol
        else if (state.is(Blocks.DIRT_PATH)) {
            handled = revertPath(serverLevel, pos);
        }

        if (handled) {
            // Consume one bone meal and mark success; spawn particles
            stack.shrink(1);
            event.setSuccessful(true);
            BoneMealItem.addGrowthParticles(serverLevel, pos, 0);
        }
    }

    private static boolean growColumn(ServerLevel level, BlockPos basePos, Block blockType) {
        // Find current top and height
        int height = 0;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(basePos.getX(), basePos.getY(), basePos.getZ());
        // Move down to base
        while (level.getBlockState(cursor).is(blockType) && cursor.getY() > level.getMinBuildHeight()) {
            cursor.move(Direction.DOWN);
        }
        if (!level.getBlockState(cursor).is(blockType)) {
            cursor.move(Direction.UP);
        }
        BlockPos base = cursor.immutable();
        // Count upwards and locate top
        BlockPos top = base;
        cursor.set(base);
        while (level.getBlockState(cursor).is(blockType)) {
            height++;
            top = cursor.immutable();
            cursor.move(Direction.UP);
            if (cursor.getY() >= level.getMaxBuildHeight()) break;
        }
        if (height >= MAX_COLUMN_HEIGHT) return false;
        BlockPos growPos = top.above();
        if (!level.isEmptyBlock(growPos)) return false;

        BlockState newState = blockType.defaultBlockState();
        if (!newState.canSurvive(level, growPos)) return false;
        level.setBlock(growPos, newState, 3);
        return true;
    }

    private static boolean spreadToAdjacent(ServerLevel level, BlockPos pos, BlockState from, BlockState to) {
        boolean any = false;
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BlockPos n = pos.relative(d);
            if (level.getBlockState(n).equals(from)) {
                level.setBlock(n, to, 3);
                any = true;
            }
        }
        return any;
    }

    private static boolean scatterMushrooms(ServerLevel level, BlockPos center) {
        boolean any = false;
        RandomSource rand = level.getRandom();
        for (int i = 0; i < 12; i++) {
            int dx = rand.nextInt(7) - 3;
            int dz = rand.nextInt(7) - 3;
            BlockPos p = center.offset(dx, 0, dz).above();
            if (!level.isEmptyBlock(p)) continue;
            Block mushroom = rand.nextBoolean() ? Blocks.BROWN_MUSHROOM : Blocks.RED_MUSHROOM;
            BlockState s = mushroom.defaultBlockState();
            if (s.canSurvive(level, p)) {
                level.setBlock(p, s, 3);
                any = true;
            }
        }
        return any;
    }

    private static boolean scatterFerns(ServerLevel level, BlockPos center) {
        boolean any = false;
        RandomSource rand = level.getRandom();
        for (int i = 0; i < 12; i++) {
            int dx = rand.nextInt(7) - 3;
            int dz = rand.nextInt(7) - 3;
            BlockPos ground = center.offset(dx, 0, dz);
            BlockPos p = ground.above();
            if (!level.isEmptyBlock(p)) continue;
            BlockState s = Blocks.FERN.defaultBlockState();
            if (s.canSurvive(level, p)) {
                level.setBlock(p, s, 3);
                any = true;
            }
        }
        return any;
    }

    private static boolean scatterSameBlock(ServerLevel level, BlockPos center, Block block) {
        boolean any = false;
        RandomSource rand = level.getRandom();
        for (int i = 0; i < 10; i++) {
            int dx = rand.nextInt(9) - 4;
            int dz = rand.nextInt(9) - 4;
            BlockPos p = center.offset(dx, 0, dz);
            if (!level.isEmptyBlock(p)) continue;

            if (block instanceof DoublePlantBlock dp) {
                if (level.isEmptyBlock(p.above()) && dp.defaultBlockState().canSurvive(level, p)) {
                    DoublePlantBlock.placeAt(level, dp.defaultBlockState(), p, 3);
                    any = true;
                }
            } else {
                BlockState s = block.defaultBlockState();
                if (s.canSurvive(level, p)) {
                    level.setBlock(p, s, 3);
                    any = true;
                }
            }
        }
        return any;
    }

    private static boolean growVines(ServerLevel level, BlockPos pos, BlockState state) {
        boolean any = false;
        // Downward copy of the same attachment state for up to 3 blocks
        BlockPos below = pos.below();
        for (int i = 0; i < 3 && level.isEmptyBlock(below) && below.getY() >= level.getMinBuildHeight(); i++) {
            if (state.canSurvive(level, below)) {
                level.setBlock(below, state, 3);
                any = true;
                below = below.below();
            } else break;
        }
        // Add lateral attachments on the clicked vine
        BlockState cur = level.getBlockState(pos);
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BlockPos adj = pos.relative(d);
            if (level.getBlockState(adj).isFaceSturdy(level, adj, d.getOpposite())) {
                if (cur.hasProperty(VineBlock.getPropertyForFace(d)) && !cur.getValue(VineBlock.getPropertyForFace(d))) {
                    cur = cur.setValue(VineBlock.getPropertyForFace(d), true);
                    any = true;
                }
            }
        }
        if (any) {
            level.setBlock(pos, cur, 3);
        }

        // Sideways spread: try to place NEW vine blocks on adjacent blocks' faces
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BlockPos side = pos.relative(d);
            if (level.isEmptyBlock(side)) {
                BlockState newState = Blocks.VINE.defaultBlockState();
                boolean canAttach = false;
                for (Direction attachDir : Direction.Plane.HORIZONTAL) {
                    BlockPos sturdyPos = side.relative(attachDir);
                    if (level.getBlockState(sturdyPos).isFaceSturdy(level, sturdyPos, attachDir.getOpposite())) {
                        newState = newState.setValue(VineBlock.getPropertyForFace(attachDir), true);
                        canAttach = true;
                    }
                }
                if (canAttach && newState.canSurvive(level, side)) {
                    level.setBlock(side, newState, 3);
                    any = true;
                }
            }
        }

        return any;
    }

    private static boolean spreadLilyPads(ServerLevel level, BlockPos center) {
        boolean any = false;
        RandomSource rand = level.getRandom();
        for (int i = 0; i < 10; i++) {
            int dx = rand.nextInt(9) - 4;
            int dz = rand.nextInt(9) - 4;
            BlockPos waterPos = center.offset(dx, 0, dz);
            // Must be water surface
            if (!level.getBlockState(waterPos).is(Blocks.WATER)) continue;
            BlockState pad = Blocks.LILY_PAD.defaultBlockState();
            if (pad.canSurvive(level, waterPos)) {
                level.setBlock(waterPos, pad, 3);
                any = true;
            }
        }
        return any;
    }

    private static boolean spreadSeaGrass(ServerLevel level, BlockPos center) {
        boolean any = false;
        RandomSource rand = level.getRandom();
        for (int i = 0; i < 10; i++) {
            int dx = rand.nextInt(9) - 4;
            int dz = rand.nextInt(9) - 4;
            int dy = rand.nextInt(3) - 1;
            BlockPos p = center.offset(dx, dy, dz);
            // Must be full water block
            if (level.getFluidState(p).is(FluidTags.WATER) && level.getFluidState(p).getAmount() == 8 && level.getBlockState(p).is(Blocks.WATER)) {
                BlockState s = Blocks.SEAGRASS.defaultBlockState();
                if (s.canSurvive(level, p)) {
                    level.setBlock(p, s, 3);
                    any = true;
                }
            }
        }
        return any;
    }

    private static boolean growKelpOne(ServerLevel level, BlockPos pos) {
        // Find top of current kelp column
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        while (withinWorld(level, cursor) && (level.getBlockState(cursor).is(Blocks.KELP) || level.getBlockState(cursor).is(Blocks.KELP_PLANT))) {
            BlockPos above = cursor.above();
            if (!withinWorld(level, above) || !(level.getBlockState(above).is(Blocks.KELP) || level.getBlockState(above).is(Blocks.KELP_PLANT))) break;
            cursor.move(Direction.UP);
        }
        BlockPos top = cursor.immutable();
        BlockPos growPos = top.above();
        // Check for water above
        if (withinWorld(level, growPos) && level.getBlockState(growPos).is(Blocks.WATER)) {
            // Convert previous top to plant, place new kelp tip above
            level.setBlock(top, Blocks.KELP_PLANT.defaultBlockState(), 3);
            level.setBlock(growPos, Blocks.KELP.defaultBlockState(), 3);
            return true;
        }
        return false;
    }

    private static boolean revertPath(ServerLevel level, BlockPos pos) {
        BlockState target = Blocks.GRASS_BLOCK.defaultBlockState();
        // Check 4 horizontal neighbors for Mycelium or Podzol preference
        for (Direction d : Direction.Plane.HORIZONTAL) {
            BlockState neighbor = level.getBlockState(pos.relative(d));
            if (neighbor.is(Blocks.MYCELIUM)) {
                target = Blocks.MYCELIUM.defaultBlockState();
                break;
            }
            if (neighbor.is(Blocks.PODZOL)) {
                target = Blocks.PODZOL.defaultBlockState();
                break;
            }
        }
        level.setBlock(pos, target, 3);
        return true;
    }

    private static boolean isCoralPlantOrFan(Block block) {
        return block instanceof BaseCoralFanBlock || block instanceof BaseCoralWallFanBlock || block instanceof CoralPlantBlock;
    }

    private static boolean promoteToCoralBlock(ServerLevel level, BlockPos pos, Block source) {
        // Map plant/fan to corresponding coral block if possible
        Block target = mapCoralPlantToBlock(source);
        if (target == null) return false;
        BlockState s = target.defaultBlockState();
        if (!s.canSurvive(level, pos)) return false;
        level.setBlock(pos, s, 3);
        return true;
    }

    private static boolean incrementNetherWartAge(ServerLevel level, BlockPos pos, BlockState state) {
        if (!state.hasProperty(NetherWartBlock.AGE)) return false;
        int age = state.getValue(NetherWartBlock.AGE);
        if (age >= 3) return false;
        level.setBlock(pos, state.setValue(NetherWartBlock.AGE, age + 1), 3);
        return true;
    }

    private static boolean growDirectionalVine(ServerLevel level, BlockPos pos, Direction dir, Block tip, Block plant) {
        // Find base by walking opposite dir while column matches tip/plant
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        while (isSameColumn(level.getBlockState(cursor), tip, plant) && withinWorld(level, cursor)) {
            BlockPos prev = cursor.relative(dir.getOpposite());
            if (!withinWorld(level, prev) || !isSameColumn(level.getBlockState(prev), tip, plant)) break;
            cursor.set(prev.getX(), prev.getY(), prev.getZ());
        }
        // Count height and find current end in dir
        int height = 0;
        BlockPos end = cursor.immutable();
        while (isSameColumn(level.getBlockState(cursor), tip, plant) && withinWorld(level, cursor)) {
            height++;
            end = cursor.immutable();
            BlockPos next = cursor.relative(dir);
            if (!withinWorld(level, next) || !isSameColumn(level.getBlockState(next), tip, plant)) break;
            cursor.set(next.getX(), next.getY(), next.getZ());
        }
        if (height >= MAX_COLUMN_HEIGHT) return false;
        BlockPos growPos = end.relative(dir);
        if (!withinWorld(level, growPos) || !level.isEmptyBlock(growPos)) return false;
        // convert previous end to plant (if it was tip), place new tip
        BlockState endState = level.getBlockState(end);
        if (endState.is(tip)) {
            level.setBlock(end, plant.defaultBlockState(), 3);
        }
        BlockState newTip = tip.defaultBlockState();
        if (!newTip.canSurvive(level, growPos)) return false;
        level.setBlock(growPos, newTip, 3);
        return true;
    }

    private static boolean withinWorld(ServerLevel level, BlockPos pos) {
        return pos.getY() >= level.getMinBuildHeight() && pos.getY() < level.getMaxBuildHeight();
    }

    private static boolean isSameColumn(BlockState state, Block tip, Block plant) {
        return state.is(tip) || state.is(plant);
    }

    private static boolean growChorusOne(ServerLevel level, BlockPos basePos) {
        // Use PLANT/Flower pair: count column of CHORUS_PLANT or CHORUS_FLOWER upwards
        int height = 0;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(basePos.getX(), basePos.getY(), basePos.getZ());
        // Move down to base of column
        while (withinWorld(level, cursor) && (level.getBlockState(cursor).is(Blocks.CHORUS_PLANT) || level.getBlockState(cursor).is(Blocks.CHORUS_FLOWER))) {
            BlockPos below = cursor.below();
            if (!withinWorld(level, below) || !(level.getBlockState(below).is(Blocks.CHORUS_PLANT) || level.getBlockState(below).is(Blocks.CHORUS_FLOWER))) break;
            cursor.move(Direction.DOWN);
        }
        BlockPos base = cursor.immutable();
        // Find top
        BlockPos top = base;
        cursor.set(base);
        while (withinWorld(level, cursor) && (level.getBlockState(cursor).is(Blocks.CHORUS_PLANT) || level.getBlockState(cursor).is(Blocks.CHORUS_FLOWER))) {
            height++;
            top = cursor.immutable();
            BlockPos up = cursor.above();
            if (!withinWorld(level, up) || !(level.getBlockState(up).is(Blocks.CHORUS_PLANT) || level.getBlockState(up).is(Blocks.CHORUS_FLOWER))) break;
            cursor.move(Direction.UP);
        }
        if (height >= MAX_COLUMN_HEIGHT) return false;
        BlockPos growPos = top.above();
        if (!withinWorld(level, growPos) || !level.isEmptyBlock(growPos)) return false;
        // Make previous top a plant (if it was a flower), place a new flower at tip
        BlockState topState = level.getBlockState(top);
        if (topState.is(Blocks.CHORUS_FLOWER)) {
            level.setBlock(top, ChorusPlantBlock.getStateWithConnections(level, top, Blocks.CHORUS_PLANT.defaultBlockState()), 3);
        }
        BlockState flower = Blocks.CHORUS_FLOWER.defaultBlockState();
        if (!flower.canSurvive(level, growPos)) return false;
        level.setBlock(growPos, flower, 3);
        return true;
    }

    @Nullable
    private static Block mapCoralPlantToBlock(Block plantOrFan) {
        // crude mapping based on block instances; fallback null if unknown
        if (plantOrFan == Blocks.TUBE_CORAL || plantOrFan == Blocks.TUBE_CORAL_FAN || plantOrFan == Blocks.TUBE_CORAL_WALL_FAN) return Blocks.TUBE_CORAL_BLOCK;
        if (plantOrFan == Blocks.BRAIN_CORAL || plantOrFan == Blocks.BRAIN_CORAL_FAN || plantOrFan == Blocks.BRAIN_CORAL_WALL_FAN) return Blocks.BRAIN_CORAL_BLOCK;
        if (plantOrFan == Blocks.BUBBLE_CORAL || plantOrFan == Blocks.BUBBLE_CORAL_FAN || plantOrFan == Blocks.BUBBLE_CORAL_WALL_FAN) return Blocks.BUBBLE_CORAL_BLOCK;
        if (plantOrFan == Blocks.FIRE_CORAL || plantOrFan == Blocks.FIRE_CORAL_FAN || plantOrFan == Blocks.FIRE_CORAL_WALL_FAN) return Blocks.FIRE_CORAL_BLOCK;
        if (plantOrFan == Blocks.HORN_CORAL || plantOrFan == Blocks.HORN_CORAL_FAN || plantOrFan == Blocks.HORN_CORAL_WALL_FAN) return Blocks.HORN_CORAL_BLOCK;
        return null;
    }
}