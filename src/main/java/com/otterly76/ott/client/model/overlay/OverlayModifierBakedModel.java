package com.otterly76.ott.client.model.overlay;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Wraps an original block baked model and appends one or more overlay baked models.
 *
 * <p>The original model provides normal block rendering; the overlays add
 * cutout terrain-transition quads on top.  Model data is gathered from all
 * sub-models and forwarded through a per-model array stored in {@link ModelData}.
 */
public class OverlayModifierBakedModel implements net.minecraft.client.resources.model.BakedModel {

    private static final ModelProperty<ModelData[]> OVERLAY_DATA = new ModelProperty<>();

    private final net.minecraft.client.resources.model.BakedModel original;
    private final List<net.minecraft.client.resources.model.BakedModel> overlays;

    public OverlayModifierBakedModel(
            net.minecraft.client.resources.model.BakedModel original,
            List<net.minecraft.client.resources.model.BakedModel> overlays) {
        this.original = original;
        this.overlays = List.copyOf(overlays);
    }

    // ---- ModelData ----------------------------------------------------------

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos,
                                           @NotNull BlockState state, @NotNull ModelData existing) {
        ModelData origData = original.getModelData(level, pos, state, existing);
        ModelData[] overlayDatas = new ModelData[overlays.size()];
        for (int i = 0; i < overlays.size(); i++) {
            overlayDatas[i] = overlays.get(i).getModelData(level, pos, state, existing);
        }
        return origData.derive().with(OVERLAY_DATA, overlayDatas).build();
    }

    // ---- getQuads -----------------------------------------------------------

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource rand, @NotNull ModelData data,
                                             @Nullable RenderType renderType) {
        // For item rendering (state == null) only use the original model
        if (state == null) {
            return original.getQuads(null, side, rand, data, renderType);
        }

        List<BakedQuad> quads = new ArrayList<>(original.getQuads(state, side, rand, data, renderType));

        ModelData[] overlayDatas = data.get(OVERLAY_DATA);
        for (int i = 0; i < overlays.size(); i++) {
            ModelData od = (overlayDatas != null && i < overlayDatas.length)
                    ? overlayDatas[i] : ModelData.EMPTY;
            net.minecraft.client.resources.model.BakedModel overlay = overlays.get(i);
            // Add overlay quads when:
            //   • renderType is null (item/unconstrained rendering), or
            //   • renderType matches the overlay's own type (CUTOUT for normal blocks), or
            //   • renderType is TRANSLUCENT — means the original block is translucent (e.g. ice)
            //     and we piggyback the overlay onto the same pass so it renders on top of the ice.
            if (renderType == null
                    || overlay.getRenderTypes(state, rand, od).contains(renderType)
                    || renderType == RenderType.translucent()) {
                quads.addAll(overlay.getQuads(state, side, rand, od, renderType));
            }
        }
        return quads;
    }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side,
                                             @NotNull RandomSource rand) {
        return original.getQuads(state, side, rand);
    }

    // ---- getRenderTypes -----------------------------------------------------

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state,
                                                      @NotNull RandomSource rand,
                                                      @NotNull ModelData data) {
        ChunkRenderTypeSet origTypes = original.getRenderTypes(state, rand, data);
        if (origTypes.contains(RenderType.translucent())) {
            // For translucent blocks (e.g. ice), the overlay piggybacks on the translucent pass.
            // Do NOT add CUTOUT — that would render the overlay behind the ice (CUTOUT < TRANSLUCENT).
            return origTypes;
        }
        ModelData[] overlayDatas = data.get(OVERLAY_DATA);
        ChunkRenderTypeSet types = origTypes;
        for (int i = 0; i < overlays.size(); i++) {
            ModelData od = (overlayDatas != null && i < overlayDatas.length)
                    ? overlayDatas[i] : ModelData.EMPTY;
            types = ChunkRenderTypeSet.union(types, overlays.get(i).getRenderTypes(state, rand, od));
        }
        return types;
    }

    // ---- Delegation to original ---------------------------------------------

    @Override
    public boolean useAmbientOcclusion() { return original.useAmbientOcclusion(); }

    @Override
    public boolean isGui3d() { return original.isGui3d(); }

    @Override
    public boolean usesBlockLight() { return original.usesBlockLight(); }

    @Override
    public boolean isCustomRenderer() { return original.isCustomRenderer(); }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() { return original.getParticleIcon(); }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        return original.getParticleIcon(data);
    }

    @Override
    public @NotNull ItemOverrides getOverrides() { return original.getOverrides(); }

    @SuppressWarnings("deprecation")
    @Override
    public @NotNull ItemTransforms getTransforms() { return original.getTransforms(); }

    @Override
    public @NotNull net.minecraft.client.resources.model.BakedModel applyTransform(
            @NotNull ItemDisplayContext transformType,
            @NotNull PoseStack poseStack,
            boolean applyLeftHandTransform) {
        return original.applyTransform(transformType, poseStack, applyLeftHandTransform);
    }

    @Override
    public @NotNull List<net.minecraft.client.resources.model.BakedModel> getRenderPasses(
            @NotNull ItemStack stack, boolean fabulous) {
        return original.getRenderPasses(stack, fabulous);
    }

    @Override
    public @NotNull List<RenderType> getRenderTypes(@NotNull ItemStack stack, boolean fabulous) {
        return original.getRenderTypes(stack, fabulous);
    }
}
