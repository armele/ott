package com.otterly76.ott.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.datafixers.util.Either;
import com.otterly76.ott.worldgen.structure.AlternateJigsawConfig;
import com.otterly76.ott.worldgen.structure.AlternateJigsawStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.DimensionPadding;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasBinding;
import net.minecraft.world.level.levelgen.structure.pools.alias.PoolAliasLookup;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;
import net.minecraft.world.level.levelgen.structure.templatesystem.LiquidSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

import java.util.List;
import java.util.Optional;

@Mixin(JigsawStructure.class)
public class JigsawStructureMixin {
    @Shadow
    @Final
    private HeightProvider startHeight;

    @Shadow
    @Final
    private List<PoolAliasBinding> poolAliases;

    @SuppressWarnings({"OptionalUsedAsFieldOrParameterType", "rawtypes", "unchecked"})
    @WrapOperation(
            method = "findGenerationPoint",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/levelgen/structure/pools/JigsawPlacement;addPieces(Lnet/minecraft/world/level/levelgen/structure/Structure$GenerationContext;Lnet/minecraft/core/Holder;Ljava/util/Optional;ILnet/minecraft/core/BlockPos;ZLjava/util/Optional;ILnet/minecraft/world/level/levelgen/structure/pools/alias/PoolAliasLookup;Lnet/minecraft/world/level/levelgen/structure/pools/DimensionPadding;Lnet/minecraft/world/level/levelgen/structure/templatesystem/LiquidSettings;)Ljava/util/Optional;"
            )
    )
    private Optional<Structure.GenerationStub> ott$redirectAddPieces(
            Structure.GenerationContext context,
            Holder<StructureTemplatePool> startPool,
            Optional startJigsawName,
            int size,
            BlockPos pos,
            boolean useExpansionHack,
            Optional heightmapProjection,
            int maxDistToCenter,
            PoolAliasLookup lookup,
            DimensionPadding padding,
            LiquidSettings liquidSettings,
            Operation<Optional<Structure.GenerationStub>> original
    ) {
        return AlternateJigsawStructure.generate(
                context,
                new AlternateJigsawConfig(
                        startPool,
                        (Optional<ResourceLocation>) startJigsawName,
                        ConstantInt.of(size),
                        false,
                        this.startHeight,
                        useExpansionHack,
                        ((Optional<Heightmap.Types>) heightmapProjection).map(Either::<com.otterly76.ott.worldgen.structure.SurfaceSnap, Heightmap.Types>right),
                        new AlternateJigsawConfig.MaxDistance(maxDistToCenter),
                        this.poolAliases,
                        padding,
                        liquidSettings
                ),
                true,
                size,
                pos,
                lookup
        );
    }
}
