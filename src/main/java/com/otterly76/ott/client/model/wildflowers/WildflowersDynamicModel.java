package com.otterly76.ott.client.model.wildflowers;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Baked model for wildflowers that deterministically picks one of 9 texture
 * variants (a–i) based on block position, so each block in the world shows a
 * single randomly-chosen color set instead of all nine overlapping.
 *
 * <p>{@code MultiPartBakedModel} passes every sub-model {@code RandomSource.create(k)}
 * where {@code k} is drawn once from the position-seeded random. All layers
 * start from the same seed, so consuming {@code layerIndex} extra values before
 * the selection draw gives each flower slot (1–4) a different texture while
 * remaining deterministic for the same position.</p>
 */
public class WildflowersDynamicModel extends BakedModelWrapper<BakedModel> {

    private final List<BakedModel> variants;
    /** 0-based index used to offset into the random sequence (layer 1 = 0, layer 4 = 3). */
    private final int layerIndex;

    WildflowersDynamicModel(List<BakedModel> variants, int layerIndex) {
        super(variants.getFirst());
        this.variants = variants;
        this.layerIndex = layerIndex;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                              @NotNull RandomSource rand, @NotNull ModelData extraData,
                                              @Nullable RenderType renderType) {
        // Advance past earlier layers so each flower slot draws a unique variant.
        for (int i = 0; i < layerIndex; i++) {
            rand.nextInt(variants.size());
        }
        int index = rand.nextInt(variants.size());
        return variants.get(index).getQuads(state, side, rand, extraData, renderType);
    }
}
