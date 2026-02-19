package com.otterly76.ott.client.render.model;

import net.minecraft.client.renderer.block.model.BakedQuad;

public class EmissiveQuad extends BakedQuad {
    public EmissiveQuad(BakedQuad original) {
        super(makeEmissive(original.getVertices().clone()), original.getTintIndex(), original.getDirection(), original.getSprite(), original.isShade());
    }

    private static int[] makeEmissive(int[] vertexData) {
        int vertexSize = 8;
        int lightsOffset = 6;

        for(int i = 0; i < vertexData.length; i += vertexSize) {
            vertexData[i + lightsOffset] = 15728880;
        }

        return vertexData;
    }
}
