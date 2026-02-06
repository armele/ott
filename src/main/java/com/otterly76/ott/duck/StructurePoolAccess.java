package com.otterly76.ott.duck;

import com.otterly76.ott.worldgen.structure.OttTemplates;

public interface StructurePoolAccess {
    OttTemplates ott$getTemplates();

    void ott$compileRawTemplates();
}
