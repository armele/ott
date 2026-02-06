package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;


public class HangingMossBlock extends Block implements BonemealableBlock {
    public static final BooleanProperty TIP = BooleanProperty.create("tip");
    public static final MapCodec<HangingMossBlock> CODEC = simpleCodec(HangingMossBlock::new);
    private static final int SIDE_PADDING = 1;
    private static final VoxelShape TIP_SHAPE = Block.box(1.0F, 2.0F, 1.0F, 15.0F, 16.0F, 15.0F);
    private static final VoxelShape BASE_SHAPE = Block.box(1.0F, 0.0F, 1.0F, 15.0F, 16.0F, 15.0F);

    public HangingMossBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(TIP, true));
    }

    public @NotNull MapCodec<HangingMossBlock> codec() {
        return CODEC;
    }

    protected @NotNull VoxelShape getShape(BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
        return blockState.getValue(TIP) ? TIP_SHAPE : BASE_SHAPE;
    }

    public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, RandomSource randomSource) {
        if (randomSource.nextInt(500) == 0) {
            BlockState blockState2 = level.getBlockState(blockPos.above());
            if (blockState2.is(ModTags.Blocks.PALE_OAK_LOGS) || blockState2.is(ModBlocks.PALE_OAK_LEAVES.get())) {
                level.playLocalSound(blockPos.getX(), blockPos.getY(), blockPos.getZ(), ModSounds.PALE_HANGING_MOSS_IDLE.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
            }
        }
    }

    protected boolean propagatesSkylightDown(@NotNull BlockState p_320652_, @NotNull BlockGetter p_320953_, @NotNull BlockPos p_320082_) {
        return true;
    }

    protected boolean canSurvive(@NotNull BlockState blockState, @NotNull LevelReader levelReader, @NotNull BlockPos blockPos) {
        return this.canStayAtPosition(levelReader, blockPos);
    }

    private boolean canStayAtPosition(BlockGetter blockGetter, BlockPos blockPos) {
        BlockPos blockPos2 = blockPos.relative(Direction.UP);
        BlockState blockState = blockGetter.getBlockState(blockPos2);
        return MultifaceBlock.canAttachTo(blockGetter, Direction.UP, blockPos2, blockState) || blockState.is(ModBlocks.PALE_HANGING_MOSS);
    }

    protected @NotNull BlockState updateShape(@NotNull BlockState blockState, @NotNull Direction direction, @NotNull BlockState blockState2, @NotNull LevelAccessor levelAccessor, @NotNull BlockPos blockPos, @NotNull BlockPos blockPos2) {
        if (!this.canStayAtPosition(levelAccessor, blockPos)) {
            levelAccessor.scheduleTick(blockPos, this, 1);
        }

        return blockState.setValue(TIP, !levelAccessor.getBlockState(blockPos.below()).is(this));
    }

    protected void tick(@NotNull BlockState blockState, @NotNull ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource randomSource) {
        if (!this.canStayAtPosition(serverLevel, blockPos)) {
            serverLevel.destroyBlock(blockPos, true);
        }

    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TIP);
    }

    public boolean isValidBonemealTarget(LevelReader levelReader, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return this.canGrowInto(levelReader.getBlockState(this.getTip(levelReader, blockPos).below()));
    }

    private boolean canGrowInto(BlockState blockState) {
        return blockState.isAir();
    }

    public BlockPos getTip(BlockGetter blockGetter, BlockPos blockPos) {
        BlockPos.MutableBlockPos mutableBlockPos = blockPos.mutable();

        BlockState blockState;
        do {
            mutableBlockPos.move(Direction.DOWN);
            blockState = blockGetter.getBlockState(mutableBlockPos);
        } while(blockState.is(this));

        return mutableBlockPos.relative(Direction.UP).immutable();
    }

    public boolean isBonemealSuccess(@NotNull Level level, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return true;
    }

    public void performBonemeal(@NotNull ServerLevel serverLevel, @NotNull RandomSource randomSource, @NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        BlockPos blockPos2 = this.getTip(serverLevel, blockPos).below();
        if (this.canGrowInto(serverLevel.getBlockState(blockPos2))) {
            serverLevel.setBlockAndUpdate(blockPos2, blockState.setValue(TIP, true));
        }

    }
}
