package com.otterly76.ott.worldgen.processor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class BlockSwapStructureProcessor extends StructureProcessor {
    public static final MapCodec<BlockSwapStructureProcessor> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(Codec.unboundedMap(ResourceKey.codec(Registries.BLOCK), ResourceKey.codec(Registries.BLOCK)).fieldOf("blocks").forGetter(BlockSwapStructureProcessor::blockSwapMap)).apply(instance, BlockSwapStructureProcessor::new));
    public static final StructureProcessorType<BlockSwapStructureProcessor> TYPE = () -> CODEC;
    private final Map<ResourceKey<Block>, ResourceKey<Block>> blockSwapMap;

    public BlockSwapStructureProcessor(Map<ResourceKey<Block>, ResourceKey<Block>> blockSwapMap) {
        this.blockSwapMap = blockSwapMap;
    }

    public Map<ResourceKey<Block>, ResourceKey<Block>> blockSwapMap() {
        return this.blockSwapMap;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader levelReader, @NotNull BlockPos pos, @NotNull BlockPos pivot, StructureTemplate.@NotNull StructureBlockInfo relative, StructureTemplate.StructureBlockInfo absolute, @NotNull StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        HolderLookup.RegistryLookup<Block> registry = levelReader.registryAccess().lookupOrThrow(Registries.BLOCK);

        // Fixed: Use BuiltInRegistries to get the key instead of deprecated builtInRegistryHolder()
        Optional<ResourceKey<Block>> optionalKey = BuiltInRegistries.BLOCK.getResourceKey(absolute.state().getBlock());

        if (optionalKey.isPresent()) {
            ResourceKey<Block> key = optionalKey.get();
            if (this.blockSwapMap.containsKey(key)) {
                Optional<Holder.Reference<Block>> newBlock = registry.get(this.blockSwapMap.get(key));
                if (newBlock.isPresent()) {
                    return new StructureTemplate.StructureBlockInfo(absolute.pos(), newBlock.get().value().withPropertiesOf(absolute.state()), absolute.nbt());
                }
            }
        }

        return absolute;
    }

    @Override
    protected @NotNull StructureProcessorType<?> getType() {
        return TYPE;
    }
}
