package com.otterly76.ott.client.model.wildflowers;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Baked model for wildflowers that deterministically picks one of 9 texture
 * variants (a–i) based on block position, so each block in the world shows a
 * single randomly-chosen color set instead of all nine overlapping.
 */
public class WildflowersDynamicModel extends BakedModelWrapper<BakedModel> {

    static final ModelProperty<Integer> VARIANT = new ModelProperty<>();

    private final List<BakedModel> variants;

    WildflowersDynamicModel(List<BakedModel> variants) {
        super(variants.getFirst());
        this.variants = variants;
    }

    /** Hash the block position to a variant index in [0, variants.size()). */
    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
                                           @NotNull BlockState state, @NotNull ModelData existing) {
        long seed = pos.asLong();
        seed ^= seed >>> 33;
        seed *= 0xff51afd7ed558ccdL;
        seed ^= seed >>> 33;
        int index = Math.floorMod(seed, variants.size());
        return existing.derive().with(VARIANT, index).build();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                              @NotNull RandomSource rand, @NotNull ModelData extraData,
                                              @Nullable RenderType renderType) {
        Integer index = extraData.get(VARIANT);
        return variants.get(index != null ? index : 0).getQuads(state, side, rand, extraData, renderType);
    }
}
