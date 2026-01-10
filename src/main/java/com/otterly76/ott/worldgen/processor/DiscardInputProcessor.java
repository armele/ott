package com.otterly76.ott.worldgen.processor;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscardInputProcessor extends StructureProcessor {
    public static final DiscardInputProcessor INSTANCE = new DiscardInputProcessor();
    public static final MapCodec<DiscardInputProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
    public static final StructureProcessorType<DiscardInputProcessor> TYPE = () -> CODEC;

    public StructureTemplate.@Nullable StructureBlockInfo process(@NotNull LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockPos pivot, StructureTemplate.@NotNull StructureBlockInfo relative, StructureTemplate.@NotNull StructureBlockInfo absolute, @NotNull StructurePlaceSettings settings) {
        return null;
    }

    protected @NotNull StructureProcessorType<?> getType() {
        return TYPE;
    }
}