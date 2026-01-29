package com.otterly76.ott.worldgen.placement;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class RiverLichenFilter extends PlacementModifier {
    public static final MapCodec<RiverLichenFilter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("min_y").forGetter(filter -> filter.minY),
            Codec.INT.fieldOf("max_y").forGetter(filter -> filter.maxY),
            DensityFunction.CODEC.fieldOf("river_parameters").forGetter(filter -> filter.riverParameters)
    ).apply(instance, RiverLichenFilter::new));

    public static final PlacementModifierType<RiverLichenFilter> TYPE = () -> CODEC;

    private final int minY;
    private final int maxY;
    private final Holder<DensityFunction> riverParameters;

    public RiverLichenFilter(int minY, int maxY, Holder<DensityFunction> riverParameters) {
        this.minY = minY;
        this.maxY = maxY;
        this.riverParameters = riverParameters;
    }

    @Override
    public @NotNull Stream<BlockPos> getPositions(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        // Quick Y-check first for performance
        if (pos.getY() < this.minY || pos.getY() > this.maxY) {
            return Stream.empty();
        }

        // Sample the density function at the current position
        DensityFunction.SinglePointContext densityContext = new DensityFunction.SinglePointContext(pos.getX(), pos.getY(), pos.getZ());
        double density = this.riverParameters.value().compute(densityContext);

        // Check if we are inside the river (parameters return 1.0)
        if (density >= 0.9) {
            return Stream.of(pos);
        }

        return Stream.empty();
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return TYPE;
    }
}




