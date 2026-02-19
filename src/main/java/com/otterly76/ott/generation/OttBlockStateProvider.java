package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.IGradientBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.crop.HedgeSprouts;
import com.otterly76.ott.hedge.ModHedgeVariants;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class OttBlockStateProvider extends ModBlockStateProvider {
    public OttBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.WOOD_SETS.forEach(this::registerWoodSet);
        
        simpleBlockWithItem(ModBlocks.GAPPER_PANEL_OAK.get(), models().getExistingFile(modLoc("block/gapper_panel_oak")));
        
        ModBlocks.SEAGLASS.forEach(block -> simpleBlockWithItem(block.get(), cubeAll(block.get())));
        ModBlocks.LIMESTONE.forEach(block -> simpleBlockWithItem(block.get(), cubeAll(block.get())));
        ModBlocks.TESTBLOCK.forEach(block -> {
            String name = block.getId().getPath();
            String texName = name.replace("testblock_", "testtexture_");
            simpleBlockWithItem(block.get(), models().cubeAll(name, modLoc("block/" + texName)));
        });

        ModHedgeVariants.ALL.forEach(variant -> {
            String name = variant.name();
            ResourceLocation leavesTexture;
            if (name.equals("pale_oak")) {
                leavesTexture = mcLoc("block/pale_oak_leaves");
            } else if (name.contains("blooming")) {
                leavesTexture = modLoc("block/" + name + "_hedge");
            } else {
                leavesTexture = modLoc("block/wood/" + name + "/leaves");
            }
            
            ResourceLocation hedgeModel = modLoc("block/" + name + "_hedge");
            models().withExistingParent(name + "_hedge", modLoc("block/hedge"))
                    .texture("all", leavesTexture);
            simpleBlock(ModBlocks.PARTICLE_HEDGES.get(name).get(), models().getExistingFile(hedgeModel));
            itemModels().withExistingParent(name + "_hedge", hedgeModel);

            ResourceLocation creepingModel = modLoc("block/" + name + "_creeping_hedge");
            models().withExistingParent(name + "_creeping_hedge", modLoc("block/hedge"))
                    .texture("all", leavesTexture);
            simpleBlock(ModBlocks.CREEPING_HEDGES.get(name).get(), models().getExistingFile(creepingModel));
            itemModels().withExistingParent(name + "_creeping_hedge", creepingModel);
        });

        ModBlocks.getAllGradientBlocks().forEach(this::registerGradientBlock);

        registerSapling(ModBlocks.STARLIGHT_SAPLING.get(), ModBlocks.POTTED_STARLIGHT_SAPLING.get(), "starlight");
        registerSapling(ModBlocks.MIDNIGHT_SAPLING.get(), ModBlocks.POTTED_MIDNIGHT_SAPLING.get(), "midnight");

        registerLantern(ModBlocks.PROTECTIVE_LANTERN.get(), "protective");
        registerLantern(ModBlocks.WATER_LANTERN.get(), "water");
        registerLantern(ModBlocks.LAVA_LANTERN.get(), "lava");
        registerLantern(ModBlocks.SMITE_LANTERN.get(), "smite");

        simpleBlock(ModBlocks.HEDGE.get(), models().getExistingFile(modLoc("block/hedge")));

        getVariantBuilder(ModBlocks.HEDGE_SPROUTS.get()).forAllStates(state -> {
            int age = state.getValue(HedgeSprouts.AGE);
            return ConfiguredModel.builder()
                    .modelFile(models().cross("hedge_sprouts_stage" + age, modLoc("block/hedge")).renderType("cutout"))
                    .build();
        });
    }

    private void registerSapling(Block sapling, Block potted, String name) {
        ModelFile saplingModel = models().getExistingFile(modLoc("block/" + name + "_sapling"));
        simpleBlock(sapling, saplingModel);
        itemModels().withExistingParent(name + "_sapling", mcLoc("item/generated")).texture("layer0", modLoc("block/" + name + "_leaves_sapling"));

        ModelFile pottedModel = models().getExistingFile(modLoc("block/potted_" + name + "_sapling"));
        simpleBlock(potted, pottedModel);
    }

    private void registerLantern(Block lantern, String name) {
        String baseName = name + "_lantern";
        ModelFile lanternModel = models().getExistingFile(modLoc("block/" + baseName));
        ModelFile hangingModel = models().getExistingFile(modLoc("block/" + baseName + "_hanging"));

        getVariantBuilder(lantern).forAllStates(state -> {
            boolean hanging = state.getValue(BlockStateProperties.HANGING);
            return ConfiguredModel.builder()
                    .modelFile(hanging ? hangingModel : lanternModel)
                    .build();
        });
    }

    private void registerGradientBlock(DeferredBlock<? extends IGradientBlock> block) {
        ResourceLocation sideTexture = modLoc("block/" + block.get().getRegistryID().getPath());
        final ModelFile blockModel = models().cube("block/" + block.get().getRegistryID().getPath(), mcLoc("block/" + block.get().getTextureName(block.get().getSecondColor())), mcLoc("block/" + block.get().getTextureName(block.get().getFirstColor())), sideTexture, sideTexture, sideTexture, sideTexture)
                .texture("particle", mcLoc("block/" + block.get().getTextureName(block.get().getFirstColor())))
                .renderType(block.get().getRenderType());
        itemModels().simpleBlockItem(block.get());
        directionalBlock(block.get(), blockModel);
    }

    private void registerWoodSet(String setName, ModBlocks.WoodSetBlocks set) {
        ResourceLocation planksTex = modLoc("block/wood/" + setName + "/planks");
        ResourceLocation logSide = modLoc("block/wood/" + setName + "/log");
        ResourceLocation logTop = modLoc("block/wood/" + setName + "/log_top");
        ResourceLocation strippedLogSide = modLoc("block/wood/" + setName + "/stripped_log");
        ResourceLocation strippedLogTop = modLoc("block/wood/" + setName + "/stripped_log_top");

        axisBlock(set.log().get(), logSide, logTop);
        axisBlock(set.wood().get(), logSide, logSide);
        axisBlock(set.strippedLog().get(), strippedLogSide, strippedLogTop);
        axisBlock(set.strippedWood().get(), strippedLogSide, strippedLogSide);

        String planksModelName = setName + "_planks";
        ModelFile planksModel = models().cubeAll(planksModelName, planksTex);
        simpleBlock(set.planks().get(), planksModel);

        stairsBlock(set.stairs().get(), planksTex);
        registerPlanksSlab(setName, set.slab().get(), planksTex, modLoc("block/" + planksModelName));

        fenceBlock(set.fence().get(), planksTex);
        models().fenceInventory(set.fence().getId().getPath() + "_inventory", planksTex);
        fenceGateBlock(set.fenceGate().get(), planksTex);
        pressurePlateBlock(set.pressurePlate().get(), planksTex);
        buttonBlock(set.button().get(), planksTex);

        models().withExistingParent(set.button().getId().getPath() + "_inventory", mcLoc("block/button_inventory"))
                .texture("texture", planksTex);

        registerCutoutDoor(set.door().get(), modLoc("block/wood/" + setName + "/door_bottom"), modLoc("block/wood/" + setName + "/door_top"));
        registerCutoutTrapdoor(set.trapdoor().get(), modLoc("block/wood/" + setName + "/trapdoor"));

        signBlock(set.sign().get(), set.wallSign().get(), planksTex);
        hangingSignBlock(set.hangingSign().get(), set.wallHangingSign().get(), planksTex);

        registerFluffyLeaves(setName, set.leaves().get());
    }

    private void registerPlanksSlab(String setName, SlabBlock slab, ResourceLocation planksTex, ResourceLocation doubleModelLoc) {
        String slabName = blockPath(slab);
        ModelFile slabModel = models().withExistingParent(slabName, mcLoc("block/slab")).texture("bottom", planksTex).texture("top", planksTex).texture("side", planksTex);
        ModelFile slabTopModel = models().withExistingParent(slabName + "_top", mcLoc("block/slab_top")).texture("bottom", planksTex).texture("top", planksTex).texture("side", planksTex);
        ModelFile doubleModel = new ModelFile.UncheckedModelFile(doubleModelLoc);

        getVariantBuilder(slab)
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(slabModel))
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP).addModels(new ConfiguredModel(slabTopModel))
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleModel));
    }

    private void registerFluffyLeaves(String setName, Block leavesBlock) {
        String leavesId = blockPath(leavesBlock);
        ResourceLocation leavesTexture = modLoc("block/wood/" + setName + "/leaves");

        ModelFile l0 = models().withExistingParent(leavesId, modLoc("block/leaves")).texture("all", leavesTexture);
        ModelFile l1 = models().withExistingParent(leavesId + "1", modLoc("block/leaves1")).texture("all", leavesTexture);
        ModelFile l2 = models().withExistingParent(leavesId + "2", modLoc("block/leaves2")).texture("all", leavesTexture);
        ModelFile l3 = models().withExistingParent(leavesId + "3", modLoc("block/leaves3")).texture("all", leavesTexture);
        ModelFile l4 = models().withExistingParent(leavesId + "4", modLoc("block/leaves4")).texture("all", leavesTexture);

        getVariantBuilder(leavesBlock).partialState().addModels(
                new ConfiguredModel(l0, 0, 0, false, 1),
                new ConfiguredModel(l1, 0, 0, false, 1),
                new ConfiguredModel(l2, 0, 0, false, 1),
                new ConfiguredModel(l3, 0, 0, false, 1),
                new ConfiguredModel(l4, 0, 0, false, 1)
        );
    }
}