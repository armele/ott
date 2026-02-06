package com.otterly76.ott.worldgen.structure;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public enum SurfaceSnap implements StringRepresentable {
    CEILING("ceiling", 1),
    FLOOR("floor", -1);

    public static final Codec<SurfaceSnap> CODEC = StringRepresentable.fromEnum(SurfaceSnap::values);
    private final String name;
    private final int offset;

    SurfaceSnap(String name, int offset) {
        this.name = name;
        this.offset = offset;
    }

    public Optional<Integer> findY(BlockPos pos, Structure.GenerationContext context, LevelHeightAccessor heightAccessor, RandomState randomState) {
        NoiseColumn column = context.chunkGenerator().getBaseColumn(pos.getX(), pos.getZ(), heightAccessor, randomState);
        int y = pos.getY();

        boolean thisCheckSolid;
        for (boolean lastCheckSolid = true; !heightAccessor.isOutsideBuildHeight(y); lastCheckSolid = thisCheckSolid) {
            y += this.offset;
            var state = column.getBlock(y);
            thisCheckSolid = !state.isAir() && state.getFluidState().isEmpty();

            if (!lastCheckSolid && thisCheckSolid) {
                return Optional.of(y);
            }
        }

        return Optional.empty();
    }

    public @NotNull String getSerializedName() {
        return this.name;
    }
}
