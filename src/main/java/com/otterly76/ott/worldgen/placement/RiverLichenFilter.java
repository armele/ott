package com.otterly76.ott.worldgen.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Stream;

public class RiverLichenFilter extends PlacementModifier {
    public static final MapCodec<RiverLichenFilter> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("min_y").forGetter(filter -> filter.minY),
            Codec.INT.fieldOf("max_y").forGetter(filter -> filter.maxY)
    ).apply(instance, RiverLichenFilter::new));

    public static final PlacementModifierType<RiverLichenFilter> TYPE = () -> CODEC;

    private final int minY;
    private final int maxY;

    public RiverLichenFilter(int minY, int maxY) {
        this.minY = minY;
        this.maxY = maxY;
    }

    @Override
    public @NotNull Stream<BlockPos> getPositions(@NotNull PlacementContext context, @NotNull RandomSource random, @NotNull BlockPos pos) {
        if (pos.getY() >= this.minY && pos.getY() <= this.maxY) {
            return Stream.of(pos);
        }
        return Stream.empty();
    }

    @Override
    public @NotNull PlacementModifierType<?> type() {
        return TYPE;
    }
}