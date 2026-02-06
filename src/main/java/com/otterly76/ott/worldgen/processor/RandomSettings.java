package com.otterly76.ott.worldgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.Ott;
import com.otterly76.ott.worldgen.processor.enums.RandomMode;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public record RandomSettings(RandomMode mode, ResourceLocation name) {
    private static final Codec<RandomSettings> FULL_CODEC = RecordCodecBuilder.create((instance) -> instance.group(RandomMode.CODEC.fieldOf("mode").orElse(RandomMode.PER_BLOCK).forGetter(RandomSettings::mode), ResourceLocation.CODEC.fieldOf("name").forGetter(RandomSettings::name)).apply(instance, RandomSettings::new));
    public static final Codec<RandomSettings> CODEC;

    public RandomSettings(RandomMode mode) {
        this(mode, Ott.resource("default"));
    }

    public RandomSource create(WorldGenLevel level, BlockPos piecePos, StructureTemplate.StructureBlockInfo blockPos) {
        return RandomSource.create(level.getSeed() + (long)this.name.hashCode()).forkPositional().at(this.mode.select(piecePos, blockPos));
    }

    static {
        CODEC = Codec.withAlternative(FULL_CODEC, RandomMode.CODEC, RandomSettings::new);
    }
}
