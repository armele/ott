package com.otterly76.ott.generation;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
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
        registerCutoutDoor(door, bottomTex, topTex, "");
    }

    protected void registerCutoutDoor(DoorBlock door, ResourceLocation bottomTex, ResourceLocation topTex, String dir) {
        String doorName = dir + blockPath(door);

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

        getVariantBuilder(door).forAllStatesExcept(state -> {
            int yRot = (int) state.getValue(DoorBlock.FACING).toYRot() + 90;
            boolean right = state.getValue(DoorBlock.HINGE) == DoorHingeSide.RIGHT;
            boolean open = state.getValue(DoorBlock.OPEN);
            if (open) {
                yRot += 90;
            }
            if (right && open) {
                yRot += 180;
            }
            yRot %= 360;

            ModelFile model = state.getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? (right ? bottomRight : bottomLeft) : (right ? topRight : topLeft);
            return ConfiguredModel.builder().modelFile(model).rotationY(yRot).build();
        }, DoorBlock.POWERED);
    }

    protected void registerCutoutTrapdoor(TrapDoorBlock trapdoor, ResourceLocation tex) {
        registerCutoutTrapdoor(trapdoor, tex, "");
    }

    protected void registerCutoutTrapdoor(TrapDoorBlock trapdoor, ResourceLocation tex, String dir) {
        String trapdoorName = dir + blockPath(trapdoor);

        ModelFile bottom = models().trapdoorBottom(trapdoorName + "_bottom", tex).renderType("cutout");
        ModelFile top = models().trapdoorTop(trapdoorName + "_top", tex).renderType("cutout");
        ModelFile open = models().trapdoorOpen(trapdoorName + "_open", tex).renderType("cutout");

        getVariantBuilder(trapdoor).forAllStates(state -> {
            int xRot = 0;
            int yRot = (int) state.getValue(TrapDoorBlock.FACING).toYRot();
            yRot = (yRot + 180) % 360;

            ModelFile model = bottom;
            if (state.getValue(TrapDoorBlock.OPEN)) {
                model = open;
            } else {
                if (state.getValue(TrapDoorBlock.HALF) == Half.TOP) {
                    model = top;
                }
            }

            if (state.getValue(TrapDoorBlock.OPEN)) {
                if (state.getValue(TrapDoorBlock.HALF) == Half.TOP) {
                    xRot = 180;
                    yRot = (yRot + 180) % 360;
                }
            }

            return ConfiguredModel.builder().modelFile(model).rotationX(xRot).rotationY(yRot).build();
        });
    }

    protected static @NotNull String blockPath(Block block) {
        ResourceLocation key = BuiltInRegistries.BLOCK.getKey(block);
        return key.getPath();
    }
}
