package com.otterly76.ott.worldgen.structure.processor;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.Ott;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

public class ScheduleTickProcessor extends StructureProcessor {
    public static final ScheduleTickProcessor INSTANCE = new ScheduleTickProcessor();
    public static final MapCodec<ScheduleTickProcessor> CODEC = MapCodec.unit(() -> INSTANCE);
    public static final StructureProcessorType<ScheduleTickProcessor> TYPE = () -> CODEC;

    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockPos pivot, StructureTemplate.@NotNull StructureBlockInfo relative, StructureTemplate.@NotNull StructureBlockInfo absolute, @NotNull StructurePlaceSettings settings) {
        if (levelReader instanceof WorldGenLevel level) {
            Ott.scheduleTick(level.getLevel(), absolute.pos(), absolute.state().getBlock(), 0);
            FluidState fluidState = absolute.state().getFluidState();
            if (!fluidState.isEmpty()) {
                Ott.scheduleTick(level.getLevel(), absolute.pos(), fluidState.getType(), 0);
            }
        }

        return absolute;
    }

    protected @NotNull StructureProcessorType<?> getType() {
        return TYPE;
    }
}




