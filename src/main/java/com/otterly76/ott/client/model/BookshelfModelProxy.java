package com.otterly76.ott.client.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BookshelfModelProxy extends BakedModelWrapper<BakedModel> {
    private final BakedModel vanillaModel;
    private final List<BakedModel> fancyVariants;

    public BookshelfModelProxy(BakedModel vanillaModel, List<BakedModel> fancyVariants) {
        super(vanillaModel);
        this.vanillaModel = vanillaModel;
        this.fancyVariants = fancyVariants;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        // If DO is calling, always give them the vanilla cube quads.
        if (isCalledByDomumOrnamentum()) {
            return vanillaModel.getQuads(state, side, rand, data, renderType);
        }

        if (!fancyVariants.isEmpty()) {
            // rand is seeded by the block's position, so this picks
            // a consistent variant for each block in the world.
            int index = Math.abs((int) rand.nextLong()) % fancyVariants.size();
            return fancyVariants.get(index).getQuads(state, side, rand, data, renderType);
        }

        return originalModel.getQuads(state, side, rand, data, renderType);
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return fancyVariants.isEmpty() ? super.getParticleIcon(data) : fancyVariants.getFirst().getParticleIcon(data);
    }

    private boolean isCalledByDomumOrnamentum() {
        for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
            String name = element.getClassName();
            if (name.contains("domumornamentum") || name.contains("ldbc")) {
                return true;
            }
        }
        return false;
    }
}
