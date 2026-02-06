package com.otterly76.ott.mixin.common;

import com.mojang.datafixers.util.Pair;
import com.otterly76.ott.duck.StructurePoolAccess;
import com.otterly76.ott.worldgen.structure.OttTemplates;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin({StructureTemplatePool.class})
public class StructureTemplatePoolMixin implements StructurePoolAccess {
    @Shadow
    @Final
    private List<Pair<StructurePoolElement, Integer>> rawTemplates;

    @Unique
    private final OttTemplates ott$templates = new OttTemplates();

    @Override
    public synchronized OttTemplates ott$getTemplates() {
        if (this.ott$templates.isEmpty()) {
            this.ott$compileRawTemplates();
        }
        return this.ott$templates;
    }

    @Override
    public synchronized void ott$compileRawTemplates() {
        if (this.ott$templates.isEmpty()) {
            this.rawTemplates.forEach((pair) -> this.ott$templates.add(pair.getFirst(), pair.getSecond()));
        }
    }
}