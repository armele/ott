package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class OttWoodSetBlockStateProvider extends BlockStateProvider {

    public OttWoodSetBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.WOOD_SETS.forEach(this::registerWoodSet);
    }

    private void registerWoodSet(String setName, ModBlocks.WoodSetBlocks set) {
        ResourceLocation planksTex = modLoc("block/" + setName + "/planks");
        ResourceLocation logSide = modLoc("block/" + setName + "/log");
        ResourceLocation logTop = modLoc("block/" + setName + "/log_top");
        ResourceLocation strippedLogSide = modLoc("block/" + setName + "/stripped_log");
        ResourceLocation strippedLogTop = modLoc("block/" + setName + "/stripped_log_top");

        axisBlock(set.log().get(), logSide, logTop);
        axisBlock(set.wood().get(), logSide, logSide);
        axisBlock(set.strippedLog().get(), strippedLogSide, strippedLogTop);
        axisBlock(set.strippedWood().get(), strippedLogSide, strippedLogSide);

        // Planks model
        String planksModelName = setName + "/planks";
        ModelFile planksModel = models().cubeAll(planksModelName, planksTex);
        simpleBlock(set.planks().get(), planksModel);

        stairsBlock(set.stairs().get(), planksTex);

        // FIX: custom slab (avoids ExistingFileHelper existence check)
        registerPlanksSlab(setName, set.slab().get(), planksTex, modLoc("block/" + planksModelName));

        fenceBlock(set.fence().get(), planksTex);
        models().fenceInventory(set.fence().getId().getPath() + "_inventory", planksTex);

        fenceGateBlock(set.fenceGate().get(), planksTex);

        pressurePlateBlock(set.pressurePlate().get(), planksTex);
        buttonBlock(set.button().get(), planksTex);

        doorBlock(
                set.door().get(),
                modLoc("block/" + setName + "/door_bottom"),
                modLoc("block/" + setName + "/door_top")
        );

        trapdoorBlock(
                set.trapdoor().get(),
                modLoc("block/" + setName + "/trapdoor"),
                true
        );

        ModelFile saplingModel = models()
                .cross(set.sapling().getId().getPath(), modLoc("block/" + setName + "/sapling"))
                .renderType("cutout");
        simpleBlock(set.sapling().get(), saplingModel);

        Block potted = set.pottedSapling().get();
        if (potted instanceof FlowerPotBlock) {
            ModelFile pottedModel = models()
                    .withExistingParent(set.pottedSapling().getId().getPath(), mcLoc("block/flower_pot_cross"))
                    .texture("plant", modLoc("block/" + setName + "/sapling"))
                    .renderType("cutout");
            simpleBlock(potted, pottedModel);
        }

        registerFluffyLeaves(setName, set.leaves().get());
    }

    private void registerPlanksSlab(String setName, SlabBlock slab, ResourceLocation planksTex, ResourceLocation doubleModelLoc) {
        String slabName = blockPath(slab);

        ModelFile slabModel = models()
                .withExistingParent(slabName, mcLoc("block/slab"))
                .texture("bottom", planksTex)
                .texture("top", planksTex)
                .texture("side", planksTex);

        ModelFile slabTopModel = models()
                .withExistingParent(slabName + "_top", mcLoc("block/slab_top"))
                .texture("bottom", planksTex)
                .texture("top", planksTex)
                .texture("side", planksTex);

        ModelFile doubleModel = new ModelFile.UncheckedModelFile(doubleModelLoc);

        getVariantBuilder(slab)
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM)
                .addModels(new ConfiguredModel(slabModel))
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP)
                .addModels(new ConfiguredModel(slabTopModel))
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE)
                .addModels(new ConfiguredModel(doubleModel));
    }

    // ... keep your registerFluffyLeaves(...) and blockPath(...) as you have them ...
    private void registerFluffyLeaves(String setName, Block leavesBlock) {
        String leavesId = blockPath(leavesBlock);

        ResourceLocation leavesTexture = modLoc("block/" + setName + "/leaves");

        ModelFile l0 = models().withExistingParent(leavesId, modLoc("block/leaves"))
                .texture("all", leavesTexture);
        ModelFile l1 = models().withExistingParent(leavesId + "1", modLoc("block/leaves1"))
                .texture("all", leavesTexture);
        ModelFile l2 = models().withExistingParent(leavesId + "2", modLoc("block/leaves2"))
                .texture("all", leavesTexture);
        ModelFile l3 = models().withExistingParent(leavesId + "3", modLoc("block/leaves3"))
                .texture("all", leavesTexture);
        ModelFile l4 = models().withExistingParent(leavesId + "4", modLoc("block/leaves4"))
                .texture("all", leavesTexture);

        getVariantBuilder(leavesBlock)
                .partialState()
                .addModels(
                        ConfiguredModel.builder().modelFile(l0).weight(1).build()[0],
                        ConfiguredModel.builder().modelFile(l1).weight(1).build()[0],
                        ConfiguredModel.builder().modelFile(l2).weight(1).build()[0],
                        ConfiguredModel.builder().modelFile(l3).weight(1).build()[0],
                        ConfiguredModel.builder().modelFile(l4).weight(1).build()[0]
                );
    }

    private static String blockPath(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        return key.getPath();
    }
}