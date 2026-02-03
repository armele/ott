
package com.otterly76.ott.worldgen.placementcondition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.OttCodecs;
import net.minecraft.core.BlockPos;
import net.minecraft.util.InclusiveRange;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public record HeightFilterPlacementCondition(RangeType rangeType, Optional<Heightmap.Types> heightmap, InclusiveRange<Integer> permittedRange) implements PlacementCondition {

    public static final MapCodec<HeightFilterPlacementCondition> CODEC = RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<HeightFilterPlacementCondition> instance) -> instance.group(
            HeightFilterPlacementCondition.RangeType.CODEC.fieldOf("range_type").forGetter(HeightFilterPlacementCondition::rangeType),
            Types.CODEC.optionalFieldOf("heightmap").forGetter(HeightFilterPlacementCondition::heightmap),
            OttCodecs.INT_RANGE.fieldOf("permitted_range").forGetter(HeightFilterPlacementCondition::permittedRange)
    ).apply(instance, HeightFilterPlacementCondition::new)).validate(HeightFilterPlacementCondition::validateStatic);

    // Changed to static to fix 'Non-static method cannot be referenced' error
    private static DataResult<HeightFilterPlacementCondition> validateStatic(HeightFilterPlacementCondition config) {
        return config.rangeType == HeightFilterPlacementCondition.RangeType.HEIGHTMAP_RELATIVE && config.heightmap.isEmpty()
                ? DataResult.error(() -> "Heightmap relative range type must be used with a heightmap")
                : DataResult.success(config);
    }

    @Override
    public boolean test(PlacementCondition.Context context, BlockPos pos) {
        if (this.heightmap.isEmpty()) {
            return this.permittedRange.isValueInRange(pos.getY());
        } else {
            int heightmapY = context.generator().getFirstFreeHeight(pos.getX(), pos.getZ(), this.heightmap.get(), context.heightAccessor(), context.randomState());
            int y = this.rangeType == HeightFilterPlacementCondition.RangeType.ABSOLUTE ? heightmapY : pos.getY() - heightmapY;
            return this.permittedRange.isValueInRange(y);
        }
    }

    public MapCodec<? extends PlacementCondition> codec() {
        return CODEC;
    }

    public enum RangeType implements StringRepresentable {
        ABSOLUTE("absolute"),
        HEIGHTMAP_RELATIVE("heightmap_relative");

        public static final Codec<RangeType> CODEC = StringRepresentable.fromEnum(RangeType::values);
        private final String name;

        RangeType(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }
    }
}