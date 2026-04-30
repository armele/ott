package com.otterly76.ott.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.WallSkullBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * WallSkullBlock that always reports an empty occlusion shape, preventing it
 * from culling faces on adjacent blocks. noOcclusion() in Properties does not
 * reliably prevent this for WallSkullBlock in NeoForge 1.21.1.
 */
public class NonOccludingWallSkullBlock extends WallSkullBlock {

    public NonOccludingWallSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
        super(type, properties);
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    protected boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return false;
    }
}
