package com.otterly76.ott.worldgen.structure.pool.element;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.Ott;
import com.otterly76.ott.worldgen.OttCodecs;
import com.otterly76.ott.worldgen.placement.condition.PlacementCondition;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.InclusiveRange;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;

import java.util.Optional;

public record DelegatingConfig(StructurePoolElement delegate, Optional<ResourceLocation> name, Optional<PlacementCondition> placementCondition, Optional<InclusiveRange<Integer>> allowedDepth, Optional<Integer> forcedCount, Optional<Integer> maxCount, boolean allowBoundingBoxCollisions, boolean otherPiecesCanIntersect, Optional<TerrainAdjustment> overrideTerrainAdaption) {

    public static final MapCodec<DelegatingConfig> CODEC = RecordCodecBuilder.mapCodec((RecordCodecBuilder.Instance<DelegatingConfig> instance) -> instance.group(
            StructurePoolElement.CODEC.fieldOf("delegate").forGetter(c -> c.delegate),
            ResourceLocation.CODEC.optionalFieldOf("name").forGetter(c -> c.name),
            PlacementCondition.CODEC.optionalFieldOf("condition").forGetter(c -> c.placementCondition),
            OttCodecs.INT_RANGE.optionalFieldOf("allowed_depth").forGetter(c -> c.allowedDepth),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("forced_count").forGetter(c -> c.forcedCount),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_count").forGetter(c -> c.maxCount),
            Codec.BOOL.optionalFieldOf("allow_bounding_box_collisions", false).forGetter(c -> c.allowBoundingBoxCollisions),
            Codec.BOOL.optionalFieldOf("other_pieces_can_intersect", false).forGetter(c -> c.otherPiecesCanIntersect),
            TerrainAdjustment.CODEC.optionalFieldOf("override_terrain_adaption").forGetter(c -> c.overrideTerrainAdaption)
    ).apply(instance, DelegatingConfig::new)).validate(DelegatingConfig::validate);

    public DelegatingConfig(StructurePoolElement delegate) {
        this(delegate, Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), false, false, Optional.empty());
    }

    private static DataResult<DelegatingConfig> validate(DelegatingConfig config) {
        return config.forcedCount.isPresent() && config.maxCount.isPresent() ? DataResult.error(() -> "forced_count and max_count cannot both be present.") : DataResult.success(config);
    }

    public ResourceLocation getName() {
        return this.name.orElseGet(() -> Ott.resource("generated/" + this.delegate.hashCode()));
    }

    public boolean shouldCancelPlacement(Structure.GenerationContext context, BlockPos pos, int depth, int count) {
        boolean validDepth = this.allowedDepth.map((range) -> range.isValueInRange(depth)).orElse(true);
        boolean validCount = this.forcedCount.map((forced) -> count < forced).orElse(true) && this.maxCount.map((max) -> count < max).orElse(true);
        // Wrap the GenerationContext using PlacementCondition.Context.create
        boolean validCondition = this.placementCondition.map((condition) -> condition.test(PlacementCondition.Context.create(context), pos)).orElse(true);
        return !validDepth || !validCount || !validCondition;
    }
}

