package com.otterly76.ott.client.model;

import com.otterly76.ott.block.entity.ElevatorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.ChunkRenderTypeSet;
import net.neoforged.neoforge.client.model.BakedModelWrapper;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ElevatorBakedModel extends BakedModelWrapper<BakedModel> {

    public ElevatorBakedModel(BakedModel original) {
        super(original);
    }

    @Override
    public @NotNull List<net.minecraft.client.renderer.block.model.BakedQuad> getQuads(
            @Nullable BlockState state, @Nullable Direction side,
            @NotNull RandomSource rand, @NotNull ModelData data,
            @Nullable RenderType renderType) {

        BlockState camo = data.get(ElevatorBlockEntity.CAMO_STATE);
        if (camo != null && !camo.isAir()) {
            BakedModel camoModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(camo);
            ChunkRenderTypeSet camoTypes = camoModel.getRenderTypes(camo, rand, ModelData.EMPTY);
            if (renderType == null || camoTypes.contains(renderType)) {
                return camoModel.getQuads(camo, side, rand, ModelData.EMPTY, renderType);
            }
            return List.of();
        }
        return super.getQuads(state, side, rand, data, renderType);
    }

    @Override
    public @NotNull ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        BlockState camo = data.get(ElevatorBlockEntity.CAMO_STATE);
        if (camo != null && !camo.isAir()) {
            BakedModel camoModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(camo);
            return camoModel.getRenderTypes(camo, rand, ModelData.EMPTY);
        }
        return super.getRenderTypes(state, rand, data);
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        BlockState camo = data.get(ElevatorBlockEntity.CAMO_STATE);
        if (camo != null && !camo.isAir()) {
            BakedModel camoModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(camo);
            return camoModel.getParticleIcon(ModelData.EMPTY);
        }
        return super.getParticleIcon(data);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }
}