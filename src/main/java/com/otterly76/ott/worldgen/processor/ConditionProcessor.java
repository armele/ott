package com.otterly76.ott.worldgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.worldgen.OttCodecs;
import com.otterly76.ott.worldgen.processor.condition.ProcessorCondition;
import com.otterly76.ott.worldgen.processor.enums.RandomMode;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ConditionProcessor extends StructureProcessor {
    private static final Codec<List<StructureProcessor>> PROCESSOR_CODEC;
    public static final MapCodec<ConditionProcessor> CODEC;
    public static final StructureProcessorType<ConditionProcessor> TYPE;
    private final RandomSettings randomSettings;
    private final ProcessorCondition condition;
    private final List<StructureProcessor> thenRun;
    private final List<StructureProcessor> elseRun;

    public ConditionProcessor(RandomSettings randomSettings, ProcessorCondition condition, List<StructureProcessor> thenRun, List<StructureProcessor> elseRun) {
        this.randomSettings = randomSettings;
        this.condition = condition;
        this.thenRun = thenRun;
        this.elseRun = elseRun;
    }

    public RandomSettings randomSettings() {
        return this.randomSettings;
    }

    public ProcessorCondition condition() {
        return this.condition;
    }

    private List<StructureProcessor> thenRun() {
        return this.thenRun;
    }

    private List<StructureProcessor> elseRun() {
        return this.elseRun;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockPos pivot, StructureTemplate.@NotNull StructureBlockInfo relative, StructureTemplate.@NotNull StructureBlockInfo absolute, @NotNull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (!(levelReader instanceof WorldGenLevel level)) {
            return absolute;
        } else {
            RandomSource random = this.randomSettings.create(level, pos, absolute);
            StructureTemplate.StructureBlockInfo newInput = new StructureTemplate.StructureBlockInfo(relative.pos(), absolute.state(), absolute.nbt());
            StructureTemplate.StructureBlockInfo newLocation = new StructureTemplate.StructureBlockInfo(absolute.pos(), level.getBlockState(absolute.pos()), absolute.nbt());
            boolean passed = this.condition.test(level, new ProcessorCondition.Data(pos, pivot, newInput, newLocation), settings, random);
            StructureTemplate.StructureBlockInfo processedBlock = absolute;

            for(StructureProcessor processor : passed ? this.thenRun : this.elseRun) {
                // Call the correct 7-argument 'process' method
                processedBlock = processor.process(levelReader, pos, pivot, relative, processedBlock, settings, template);
                if (processedBlock == null) {
                    break;
                }
            }

            return processedBlock;
        }
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return TYPE;
    }

    static {
        PROCESSOR_CODEC = OttCodecs.compactList(StructureProcessorType.SINGLE_CODEC);
        CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(RandomSettings.CODEC.fieldOf("random_mode").orElse(new RandomSettings(RandomMode.PER_BLOCK)).forGetter(ConditionProcessor::randomSettings), ProcessorCondition.CODEC.fieldOf("if_true").forGetter(ConditionProcessor::condition), PROCESSOR_CODEC.fieldOf("then").forGetter(ConditionProcessor::thenRun), PROCESSOR_CODEC.fieldOf("else").orElse(List.of()).forGetter(ConditionProcessor::elseRun)).apply(instance, ConditionProcessor::new));
        TYPE = () -> CODEC;
    }
}