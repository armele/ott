package com.otterly76.ott.mixin.common;

import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Optional;

@Mixin({JigsawStructure.class})
public interface JigsawStructureAccessor {
    @Accessor("startPool")
    Holder<StructureTemplatePool> getStartPool();

    @Accessor("projectStartToHeightmap")
    Optional<Heightmap.Types> getProjectStartToHeightmap();

    @Accessor("poolAliases")
    @Mutable
    void setPoolAliases(List<PoolAliasBinding> list);

    @Accessor("poolAliases")
    List<PoolAliasBinding> getPoolAliases();
}