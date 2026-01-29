package com.otterly76.ott.worldgen.structure.pool.alias;


import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.HolderSetCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public record RandomEntries(List<ResourceKey<StructureTemplatePool>> aliases, List<HolderSet<StructureTemplatePool>> pools) implements PoolAliasBinding {
    public static final MapCodec<RandomEntries> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            ResourceKey.codec(Registries.TEMPLATE_POOL).listOf().fieldOf("aliases").forGetter(RandomEntries::aliases),
            HolderSetCodec.create(Registries.TEMPLATE_POOL, StructureTemplatePool.CODEC, false).listOf().fieldOf("pools").forGetter(RandomEntries::pools)
    ).apply(instance, RandomEntries::new));

    private static DataResult<RandomEntries> validate(RandomEntries entry) {
        if (entry.pools.size() == entry.aliases.size()) {
            Integer size = null;

            for(HolderSet<StructureTemplatePool> pool : entry.pools) {
                if (size != null && pool.size() != size) {
                    return DataResult.error(() -> "Each template pool set should have the same number of entries");
                }

                size = pool.size();
            }

            return DataResult.success(entry);
        } else {
            return DataResult.error(() -> "List of aliases and list of pools should be the same length");
        }
    }

    public void forEachResolved(@NotNull RandomSource random, @NotNull BiConsumer<ResourceKey<StructureTemplatePool>, ResourceKey<StructureTemplatePool>> consumer) {
        if (this.pools.isEmpty() || this.pools.getFirst().size() == 0) {
            return;
        }

        int index = random.nextInt(this.pools.getFirst().size());

        for (int i = 0; i < this.pools.size(); ++i) {
            Optional<ResourceKey<StructureTemplatePool>> key = this.pools.get(i).get(index).unwrapKey();
            if (key.isPresent()) {
                consumer.accept(this.aliases.get(i), key.get());
            }
        }
    }

    public @NotNull Stream<ResourceKey<StructureTemplatePool>> allTargets() {
        return Stream.of();
    }

    public @NotNull MapCodec<? extends PoolAliasBinding> codec() {
        return CODEC;
    }
}

