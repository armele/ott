package com.otterly76.ott.block.custom;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SaltLampBlock extends Block {
    public static final MapCodec<SaltLampBlock> CODEC = simpleCodec(SaltLampBlock::new);
    protected static final VoxelShape BASE = Block.box(2.0, 0.0, 2.0, 14.0, 2.0, 14.0);
    protected static final VoxelShape SALT = Block.box(4.0, 0.0, 4.0, 12.0, 13.0, 12.0);
    public static final VoxelShape SHAPE_COMMON = Shapes.or(BASE, SALT);
    public static final BooleanProperty LIT = BlockStateProperties.LIT;

    public SaltLampBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(LIT, false));
    }

    @Override
    protected @NotNull MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return SHAPE_COMMON;
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Player pPlayer, @NotNull BlockHitResult pHit) {
        pLevel.setBlock(pPos, pState.cycle(LIT), 2);
        pLevel.playSound(pPlayer, pPos, SoundEvents.LEVER_CLICK, SoundSource.NEUTRAL, 0.3F, 0.6F);
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(LIT, pContext.getLevel().hasNeighborSignal(pContext.getClickedPos()));
    }

    @Override
    protected void neighborChanged(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Block pBlock, @NotNull BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean flag = pLevel.hasNeighborSignal(pPos);
            if (flag != pState.getValue(LIT)) {
                if (flag) {
                    pLevel.scheduleTick(pPos, this, 4);
                } else {
                    pLevel.setBlock(pPos, pState.setValue(LIT, false), 2);
                }
            }
        }
    }

    @Override
    protected void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (pState.getValue(LIT) && !pLevel.hasNeighborSignal(pPos)) {
            pLevel.setBlock(pPos, pState.cycle(LIT), 2);
        }
    }

    @Override
    public void animateTick(@NotNull BlockState p_222503_, @NotNull Level p_222504_, @NotNull BlockPos p_222505_, @NotNull RandomSource p_222506_) {
        if (p_222503_.getValue(LIT)) {
            int i = p_222505_.getX();
            int j = p_222505_.getY();
            int k = p_222505_.getZ();
            double d0 = (double)i + p_222506_.nextDouble();
            double d1 = (double)j + 0.7;
            double d2 = (double)k + p_222506_.nextDouble();
            p_222504_.addParticle(ParticleTypes.WAX_ON, d0, d1, d2, 0.0, 0.0, 0.0);
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

            for(int l = 0; l < 14; ++l) {
                blockpos$mutableblockpos.set(i + Mth.nextInt(p_222506_, -10, 10), j - p_222506_.nextInt(10), k + Mth.nextInt(p_222506_, -10, 10));
                BlockState blockstate = p_222504_.getBlockState(blockpos$mutableblockpos);
                if (!blockstate.isCollisionShapeFullBlock(p_222504_, blockpos$mutableblockpos)) {
                    p_222504_.addParticle(ParticleTypes.WAX_ON, (double)blockpos$mutableblockpos.getX() + p_222506_.nextDouble(), (double)blockpos$mutableblockpos.getY() + p_222506_.nextDouble(), (double)blockpos$mutableblockpos.getZ() + p_222506_.nextDouble(), 0.0, 0.0, 0.0);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(LIT);
    }
}