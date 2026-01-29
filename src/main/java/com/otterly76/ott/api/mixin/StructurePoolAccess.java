package com.otterly76.ott.api.mixin;


import com.otterly76.ott.worldgen.structure.OttTemplates;

public interface StructurePoolAccess {
    OttTemplates ott$getTemplates();

    void ott$compileRawTemplates();
}
