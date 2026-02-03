package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public class OttWoodSetBlockStateProvider extends BlockStateProvider {

    public OttWoodSetBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.WOOD_SETS.forEach(this::registerWoodSet);
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

        // Planks model (use flat model name to match the rest of the wood set output)
        String planksModelName = setName + "_planks";
        ModelFile planksModel = models().cubeAll(planksModelName, planksTex);
        simpleBlock(set.planks().get(), planksModel);

        stairsBlock(set.stairs().get(), planksTex);

        // Custom slab (avoids ExistingFileHelper existence check)
        registerPlanksSlab(setName, set.slab().get(), planksTex, modLoc("block/" + planksModelName));

        fenceBlock(set.fence().get(), planksTex);
        models().fenceInventory(set.fence().getId().getPath() + "_inventory", planksTex);

        fenceGateBlock(set.fenceGate().get(), planksTex);

        pressurePlateBlock(set.pressurePlate().get(), planksTex);

        buttonBlock(set.button().get(), planksTex);

        // Generate the button inventory model for the item to reference:
        // assets/ott/models/block/<button>_inventory.json
        models().withExistingParent(set.button().getId().getPath() + "_inventory", mcLoc("block/button_inventory"))
                .texture("texture", planksTex);

        // Doors/Trapdoors: ensure cutout render type on generated models
        registerCutoutDoor(setName, set.door().get());
        registerCutoutTrapdoor(setName, set.trapdoor().get());

        // Signs (blockstates/models)
        signBlock(set.sign().get(), set.wallSign().get(), planksTex);
        hangingSignBlock(set.hangingSign().get(), set.wallHangingSign().get(), planksTex);

        // Leaves: fluffy/random variants
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

    private void registerCutoutDoor(String setName, DoorBlock door) {
        String doorName = blockPath(door);

        ResourceLocation bottomTex = modLoc("block/wood/" + setName + "/door_bottom");
        ResourceLocation topTex = modLoc("block/wood/" + setName + "/door_top");

        ModelFile bottomLeft = models()
                .withExistingParent(doorName + "_bottom_left", mcLoc("block/door_bottom_left"))
                .texture("bottom", bottomTex)
                .texture("top", topTex)
                .renderType("cutout");

        ModelFile bottomRight = models()
                .withExistingParent(doorName + "_bottom_right", mcLoc("block/door_bottom_right"))
                .texture("bottom", bottomTex)
                .texture("top", topTex)
                .renderType("cutout");

        ModelFile topLeft = models()
                .withExistingParent(doorName + "_top_left", mcLoc("block/door_top_left"))
                .texture("bottom", bottomTex)
                .texture("top", topTex)
                .renderType("cutout");

        ModelFile topRight = models()
                .withExistingParent(doorName + "_top_right", mcLoc("block/door_top_right"))
                .texture("bottom", bottomTex)
                .texture("top", topTex)
                .renderType("cutout");

        getVariantBuilder(door)
                .forAllStates(state -> {
                    boolean open = state.getValue(BlockStateProperties.OPEN);
                    boolean right = state.getValue(BlockStateProperties.DOOR_HINGE) == DoorHingeSide.RIGHT;
                    boolean upper = state.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER;
                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);

                    // FIX: model parents for doors are 90° offset vs facing.toYRot()
                    int yRot = (((int) facing.toYRot()) + 90) % 360;

                    ModelFile model;
                    if (upper) {
                        model = (open ^ right) ? topRight : topLeft;
                    } else {
                        model = (open ^ right) ? bottomRight : bottomLeft;
                    }

                    if (open) {
                        yRot = (yRot + (right ? 90 : -90)) % 360;
                        if (yRot < 0) yRot += 360;
                    }

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(yRot)
                            .build();
                });
    }

    private void registerCutoutTrapdoor(String setName, TrapDoorBlock trapdoor) {
        String trapdoorName = blockPath(trapdoor);
        ResourceLocation tex = modLoc("block/wood/" + setName + "/trapdoor");

        ModelFile bottom = models().trapdoorBottom(trapdoorName + "_bottom", tex).renderType("cutout");
        ModelFile top = models().trapdoorTop(trapdoorName + "_top", tex).renderType("cutout");
        ModelFile open = models().trapdoorOpen(trapdoorName + "_open", tex).renderType("cutout");

        getVariantBuilder(trapdoor)
                .forAllStates(state -> {
                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    boolean isOpen = state.getValue(BlockStateProperties.OPEN);
                    Half half = state.getValue(BlockStateProperties.HALF);

                    ModelFile model = isOpen ? open : (half == Half.TOP ? top : bottom);

                    // FIX: model parents for trapdoors are 180° offset vs facing.toYRot()
                    int yRot = (((int) facing.toYRot()) + 180) % 360;

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(yRot)
                            .build();
                });
    }

    private void registerFluffyLeaves(String setName, Block leavesBlock) {
        String leavesId = blockPath(leavesBlock);
        ResourceLocation leavesTexture = modLoc("block/wood/" + setName + "/leaves");

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

    private static @NotNull String blockPath(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        return key.getPath();
    }
}