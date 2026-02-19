package com.otterly76.ott.generation;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Half;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

public abstract class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    protected void registerCutoutDoor(DoorBlock door, ResourceLocation bottomTex, ResourceLocation topTex) {
        String doorName = blockPath(door);

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

    protected void registerCutoutTrapdoor(TrapDoorBlock trapdoor, ResourceLocation tex) {
        String trapdoorName = blockPath(trapdoor);

        ModelFile bottom = models().trapdoorBottom(trapdoorName + "_bottom", tex).renderType("cutout");
        ModelFile top = models().trapdoorTop(trapdoorName + "_top", tex).renderType("cutout");
        ModelFile open = models().trapdoorOpen(trapdoorName + "_open", tex).renderType("cutout");

        getVariantBuilder(trapdoor)
                .forAllStates(state -> {
                    Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
                    boolean isOpen = state.getValue(BlockStateProperties.OPEN);
                    Half half = state.getValue(BlockStateProperties.HALF);

                    ModelFile model = isOpen ? open : (half == Half.TOP ? top : bottom);

                    int yRot = (((int) facing.toYRot()) + 180) % 360;

                    return ConfiguredModel.builder()
                            .modelFile(model)
                            .rotationY(yRot)
                            .build();
                });
    }

    protected static @NotNull String blockPath(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        return key.getPath();
    }
}
