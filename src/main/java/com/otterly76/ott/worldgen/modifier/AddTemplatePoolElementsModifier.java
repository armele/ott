package com.otterly76.ott.worldgen.modifier;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.otterly76.ott.mixin.common.StructureTemplatePoolAccessor;
import com.otterly76.ott.worldgen.OttCodecs;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.ArrayList;
import java.util.List;

public record AddTemplatePoolElementsModifier(int priority, HolderSet<StructureTemplatePool> templatePools, List<Pair<StructurePoolElement, Integer>> elements) implements Modifier {
    public static final MapCodec<AddTemplatePoolElementsModifier> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(PRIORITY_DEFAULT.forGetter(AddTemplatePoolElementsModifier::priority), OttCodecs.registrySet(Registries.TEMPLATE_POOL, "template_pools").forGetter(AddTemplatePoolElementsModifier::templatePools), Codec.mapPair(StructurePoolElement.CODEC.fieldOf("element"), Codec.intRange(1, 150).fieldOf("weight")).codec().listOf().fieldOf("elements").forGetter(AddTemplatePoolElementsModifier::elements)).apply(instance, AddTemplatePoolElementsModifier::new));

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }

    public void applyModifier() {
        this.templatePools.stream().map(Holder::value).forEach(this::applyModifier);
    }

    private void applyModifier(StructureTemplatePool templatePool) {
        StructureTemplatePoolAccessor poolAccessor = (StructureTemplatePoolAccessor)templatePool;
        List<Pair<StructurePoolElement, Integer>> rawTemplates = new ArrayList<>(poolAccessor.getRawTemplates());
        rawTemplates.addAll(this.elements());
        poolAccessor.setRawTemplates(rawTemplates);
        ObjectArrayList<StructurePoolElement> vanillaTemplates = new ObjectArrayList<>(poolAccessor.getVanillaTemplates());

        for(Pair<StructurePoolElement, Integer> pair : this.elements()) {
            for(int i = 0; i < pair.getSecond(); ++i) {
                vanillaTemplates.add(pair.getFirst());
            }
        }

        poolAccessor.setVanillaTemplates(vanillaTemplates);
    }
}