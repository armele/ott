package com.otterly76.ott.mixin;

import com.otterly76.ott.config.OttConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SnowLayerBlock.class})
public class SnowLayerBlockMixin extends Block implements Fallable {
    public SnowLayerBlockMixin(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Unique
    private boolean ott$isFree(BlockState blockState) {
        return blockState.isAir() || blockState.canBeReplaced() && !blockState.is(Blocks.SNOW);
    }

    @Inject(
            method = {"updateShape"},
            at = {@At("HEAD")},
            cancellable = true
    )
    protected void snowLayerUpdateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor world, BlockPos pos, BlockPos pos2, CallbackInfoReturnable<BlockState> cir) {
        if (world instanceof Level level) {
            BlockPos below = pos.below();
            if (this.ott$isFree(level.getBlockState(below)) && pos.getY() >= level.getMinBuildHeight()) {
                level.scheduleTick(pos, this, 2);
                System.out.println("Pos: " + below);
                cir.setReturnValue(state);
                cir.cancel();
            }

        }
    }

    public void onLand(@NotNull Level level, @NotNull BlockPos blockPos, @NotNull BlockState blockState, @NotNull BlockState blockState2, @NotNull FallingBlockEntity fallingBlockEntity) {
        if (level instanceof ServerLevel world) {
            if (OttConfig.SNOW.PARTICLES.get()) {
                world.sendParticles(ParticleTypes.SNOWFLAKE, fallingBlockEntity.getX(), fallingBlockEntity.getY() + (double)0.125F * (double) blockState.getValue(SnowLayerBlock.LAYERS), fallingBlockEntity.getZ(), 20, 0.35, 0.1, 0.35, 0.03);
            }

            if (OttConfig.SNOW.PLAY_SOUND.get()) {
                world.playSound(null, blockPos, SoundEvents.SNOW_PLACE, SoundSource.BLOCKS, 1.0F, 0.9F);
            }
        }
    }

    public void tick(@NotNull BlockState state, ServerLevel world, BlockPos pos, @NotNull RandomSource random) {
        if (this.ott$isFree(world.getBlockState(pos.below()))) {
            FallingBlockEntity fall = FallingBlockEntity.fall(world, pos, state);
            fall.dropItem = false;
            world.addFreshEntity(fall);
            world.removeBlock(pos, false);
        }

    }
}