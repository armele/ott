package com.otterly76.ott.worldgen.modifier;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.duck.StructurePoolAccess;
import com.otterly76.ott.mixin.common.HolderReferenceAccessor;
import com.otterly76.ott.mixin.common.JigsawStructureAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import com.otterly76.ott.worldgen.placementcondition.LandBasePlacementCondition;
import com.otterly76.ott.worldgen.structure.AlternateJigsawStructure;
import com.otterly76.ott.worldgen.structure.DelegatingConfig;
import com.otterly76.ott.worldgen.structure.DelegatingStructure;
import com.otterly76.ott.worldgen.structure.OttTemplates;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.structures.JigsawStructure;

import java.util.HashSet;
import java.util.Set;

public record AutoLandBaseModifier(int priority, HolderSet<Structure> exclude, HolderSet<Structure> include) implements Modifier {
    public static final MapCodec<AutoLandBaseModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            PRIORITY_DEFAULT.forGetter(AutoLandBaseModifier::priority),
            RegistryCodecs.homogeneousList(Registries.STRUCTURE).optionalFieldOf("exclude", HolderSet.direct()).forGetter(AutoLandBaseModifier::exclude),
            RegistryCodecs.homogeneousList(Registries.STRUCTURE).optionalFieldOf("include", HolderSet.direct()).forGetter(AutoLandBaseModifier::include)
    ).apply(instance, AutoLandBaseModifier::new));

    @Override
    public void applyModifier(RegistryAccess registries) {
        registries.registryOrThrow(Registries.STRUCTURE).holders().forEach((structure) -> {
            if (this.exclude.contains(structure)) return;

            boolean shouldBeLandBase = this.include.contains(structure);
            if (!shouldBeLandBase) {
                shouldBeLandBase = isStructureLandBased(structure.value());
            }

            if (shouldBeLandBase) {
                applyLandBase(structure);
            }
        });
    }

    private boolean isStructureLandBased(Structure structure) {
        if (structure instanceof AlternateJigsawStructure alternateJigsaw) {
            if (alternateJigsaw.config().startProjection().isPresent()) {
                return true;
            }
            return isPoolLandBased(alternateJigsaw.config().startPool(), new HashSet<>());
        } else if (structure instanceof JigsawStructure jigsaw) {
            JigsawStructureAccessor accessor = (JigsawStructureAccessor) (Object) jigsaw;
            if (accessor.getProjectStartToHeightmap().isPresent()) {
                return true;
            }
            return isPoolLandBased(accessor.getStartPool(), new HashSet<>());
        }
        return false;
    }

    private boolean isPoolLandBased(Holder<StructureTemplatePool> pool, Set<Holder<StructureTemplatePool>> visited) {
        if (visited.contains(pool)) return false;
        visited.add(pool);

        StructureTemplatePool poolValue = pool.value();
        if (!(poolValue instanceof StructurePoolAccess access)) return false;

        OttTemplates templates = access.ott$getTemplates();
        for (StructurePoolElement element : templates) {
            if (element.getProjection() == StructureTemplatePool.Projection.TERRAIN_MATCHING) {
                return true;
            }
        }
        return false;
    }

    private void applyLandBase(Holder<Structure> structure) {
        if (structure.value() instanceof DelegatingStructure delegating) {
            delegating.config().setSpawnCondition(LandBasePlacementCondition.INSTANCE, true);
        } else if (structure instanceof Holder.Reference<Structure> reference) {
            Structure delegating = new DelegatingStructure(new DelegatingConfig(Holder.direct(structure.value()), LandBasePlacementCondition.INSTANCE));
            @SuppressWarnings("unchecked")
            HolderReferenceAccessor<Structure> holderAccessor = (HolderReferenceAccessor<Structure>) structure;
            holderAccessor.setValue(delegating);
        }
    }

    @Override
    public void applyModifier() {}

    @Override
    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}