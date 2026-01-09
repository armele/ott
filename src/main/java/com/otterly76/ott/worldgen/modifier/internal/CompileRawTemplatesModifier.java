package com.otterly76.ott.worldgen.modifier.internal;

import com.mojang.serialization.MapCodec;
import com.otterly76.ott.Ott;
import com.otterly76.ott.duck.StructurePoolAccess;
import com.otterly76.ott.worldgen.modifier.Modifier;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public record CompileRawTemplatesModifier() implements Modifier {
    public static final MapCodec<CompileRawTemplatesModifier> CODEC = MapCodec.unit(CompileRawTemplatesModifier::new);

    public void applyModifier(RegistryAccess registries) {
        for(StructureTemplatePool pool : Ott.registry(registries, Registries.TEMPLATE_POOL).stream().toList()) {
            ((StructurePoolAccess)pool).ott$compileRawTemplates();
        }
    }

    public void applyModifier() {
    }

    public int priority() {
        return Integer.MAX_VALUE;
    }

    public MapCodec<? extends Modifier> codec() {
        return CODEC;
    }
}