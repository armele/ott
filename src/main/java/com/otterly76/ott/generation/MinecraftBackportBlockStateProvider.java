package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.registry.ModBlockStateProperties;
import com.otterly76.ott.util.block.CreakingHeartState;
import com.otterly76.ott.block.custom.HangingMossBlock;
import com.otterly76.ott.block.custom.MossyCarpetBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WallSide;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.*;

public class MinecraftBackportBlockStateProvider extends ModBlockStateProvider {

    public MinecraftBackportBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "minecraft", existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        simpleBlock(ModBlocks.PALE_MOSS_BLOCK.get());
        
        ModelFile paleMossCarpetModel = models()
                .withExistingParent("pale_moss_carpet", mcLoc("block/carpet"))
                .texture("wool", mcLoc("block/pale_moss_carpet"))
                .renderType("cutout");

        MultiPartBlockStateBuilder carpetBuilder = getMultipartBuilder(ModBlocks.PALE_MOSS_CARPET.get());
        carpetBuilder.part().modelFile(paleMossCarpetModel).addModel().condition(MossyCarpetBlock.BASE, true).end();

        for (Direction dir : Direction.Plane.HORIZONTAL) {
            EnumProperty<WallSide> prop = MossyCarpetBlock.getPropertyForFace(dir);
            int yRot = (int) dir.getOpposite().toYRot();
            assert prop != null;
            carpetBuilder.part().modelFile(models().getExistingFile(mcLoc("block/pale_moss_carpet_side_small"))).rotationY(yRot).addModel()
                    .condition(prop, WallSide.LOW).end();
            carpetBuilder.part().modelFile(models().getExistingFile(mcLoc("block/pale_moss_carpet_side_tall"))).rotationY(yRot).addModel()
                    .condition(prop, WallSide.TALL).end();
        }

        itemModels().withExistingParent("pale_moss_carpet", mcLoc("block/pale_moss_carpet"));

        RotatedPillarBlock paleLog = ModBlocks.PALE_OAK_LOG.get();
        RotatedPillarBlock paleWood = ModBlocks.PALE_OAK_WOOD.get();
        RotatedPillarBlock strippedLog = ModBlocks.STRIPPED_PALE_OAK_LOG.get();
        RotatedPillarBlock strippedWood = ModBlocks.STRIPPED_PALE_OAK_WOOD.get();

        axisBlock(
                paleLog,
                mcLoc("block/pale_oak_log"),
                mcLoc("block/pale_oak_log_top")
        );

        axisBlock(
                paleWood,
                mcLoc("block/pale_oak_log"),
                mcLoc("block/pale_oak_log")
        );

        axisBlock(
                strippedLog,
                mcLoc("block/stripped_pale_oak_log"),
                mcLoc("block/stripped_pale_oak_log_top")
        );

        axisBlock(
                strippedWood,
                mcLoc("block/stripped_pale_oak_log"),
                mcLoc("block/stripped_pale_oak_log")
        );

        ResourceLocation palePlanks = mcLoc("block/pale_oak_planks");

        simpleBlockWithItem(ModBlocks.PALE_OAK_PLANKS.get(), models().cubeAll("pale_oak_planks", palePlanks));

        stairsBlock(ModBlocks.PALE_OAK_STAIRS.get(), palePlanks);
        slabBlock(ModBlocks.PALE_OAK_SLAB.get(), palePlanks, palePlanks);

        fenceBlock(ModBlocks.PALE_OAK_FENCE.get(), palePlanks);
        models().fenceInventory("pale_oak_fence_inventory", palePlanks);

        fenceGateBlock(ModBlocks.PALE_OAK_FENCE_GATE.get(), palePlanks);

        pressurePlateBlock(ModBlocks.PALE_OAK_PRESSURE_PLATE.get(), palePlanks);
        buttonBlock(ModBlocks.PALE_OAK_BUTTON.get(), palePlanks);

        // Generate the button inventory model for the item to reference:
        // assets/minecraft/models/block/pale_oak_button_inventory.json
        models().withExistingParent("pale_oak_button_inventory", mcLoc("block/button_inventory"))
                .texture("texture", palePlanks);

        registerCutoutDoor(ModBlocks.PALE_OAK_DOOR.get(), mcLoc("block/pale_oak_door_bottom"), mcLoc("block/pale_oak_door_top"));
        registerCutoutTrapdoor(ModBlocks.PALE_OAK_TRAPDOOR.get(), mcLoc("block/pale_oak_trapdoor"));

        signBlock(
                ModBlocks.PALE_OAK_SIGN.get(),
                ModBlocks.PALE_OAK_WALL_SIGN.get(),
                palePlanks
        );

        hangingSignBlock(
                ModBlocks.PALE_OAK_HANGING_SIGN.get(),
                ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get(),
                palePlanks
        );

        ModelFile baseModel = paleHangingMossModel("pale_hanging_moss", "block/pale_hanging_moss");
        ModelFile tipModel = paleHangingMossTipModel("pale_hanging_moss_tip", "block/pale_hanging_moss_tip");

        getVariantBuilder(ModBlocks.PALE_HANGING_MOSS.get())
                .partialState().with(HangingMossBlock.TIP, false).modelForState().modelFile(baseModel).addModel()
                .partialState().with(HangingMossBlock.TIP, true).modelForState().modelFile(tipModel).addModel();

        ModelFile closedEyeblossom = models()
                .cross("closed_eyeblossom", mcLoc("block/closed_eyeblossom"))
                .renderType("cutout");
        simpleBlockWithItem(ModBlocks.CLOSED_EYEBLOSSOM.get(), closedEyeblossom);

        ModelFile openEyeblossom = models()
                .cross("open_eyeblossom", mcLoc("block/open_eyeblossom"))
                .renderType("cutout");
        simpleBlockWithItem(ModBlocks.OPEN_EYEBLOSSOM.get(), openEyeblossom);

        ModelFile pottedClosedEyeblossom = models()
                .withExistingParent("potted_closed_eyeblossom", mcLoc("block/flower_pot_cross"))
                .texture("plant", mcLoc("block/closed_eyeblossom"))
                .renderType("cutout");
        simpleBlock(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(), pottedClosedEyeblossom);

        ModelFile pottedOpenEyeblossom = models()
                .withExistingParent("potted_open_eyeblossom", mcLoc("block/flower_pot_cross"))
                .texture("plant", mcLoc("block/open_eyeblossom"))
                .renderType("cutout");
        simpleBlock(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(), pottedOpenEyeblossom);

        simpleBlock(ModBlocks.RESIN_BLOCK.get());
        simpleBlock(ModBlocks.RESIN_BRICKS.get());
        simpleBlock(ModBlocks.CHISELED_RESIN_BRICKS.get());

        ResourceLocation resinBricksTex = mcLoc("block/resin_bricks");
        stairsBlock(ModBlocks.RESIN_BRICK_STAIRS.get(), resinBricksTex);
        slabBlock(ModBlocks.RESIN_BRICK_SLAB.get(), resinBricksTex, resinBricksTex);
        wallBlock(ModBlocks.RESIN_BRICK_WALL.get(), resinBricksTex);

        models().wallInventory("resin_brick_wall_inventory", resinBricksTex);

        ModelFile resinClump = resinClumpModel();
        MultiPartBlockStateBuilder clump = getMultipartBuilder(ModBlocks.RESIN_CLUMP.get());

        clump.part().modelFile(resinClump).addModel().condition(NORTH, true).end();
        clump.part().modelFile(resinClump).rotationY(90).uvLock(true).addModel().condition(EAST, true).end();
        clump.part().modelFile(resinClump).rotationY(180).uvLock(true).addModel().condition(SOUTH, true).end();
        clump.part().modelFile(resinClump).rotationY(270).uvLock(true).addModel().condition(WEST, true).end();
        clump.part().modelFile(resinClump).rotationX(270).uvLock(true).addModel().condition(UP, true).end();
        clump.part().modelFile(resinClump).rotationX(90).uvLock(true).addModel().condition(DOWN, true).end();

        clump.part().modelFile(resinClump).addModel()
                .condition(DOWN, false).condition(EAST, false).condition(NORTH, false)
                .condition(SOUTH, false).condition(UP, false).condition(WEST, false)
                .end();

        clump.part().modelFile(resinClump).rotationY(90).uvLock(true).addModel()
                .condition(DOWN, false).condition(EAST, false).condition(NORTH, false)
                .condition(SOUTH, false).condition(UP, false).condition(WEST, false)
                .end();

        clump.part().modelFile(resinClump).rotationY(180).uvLock(true).addModel()
                .condition(DOWN, false).condition(EAST, false).condition(NORTH, false)
                .condition(SOUTH, false).condition(UP, false).condition(WEST, false)
                .end();

        clump.part().modelFile(resinClump).rotationY(270).uvLock(true).addModel()
                .condition(DOWN, false).condition(EAST, false).condition(NORTH, false)
                .condition(SOUTH, false).condition(UP, false).condition(WEST, false)
                .end();

        clump.part().modelFile(resinClump).rotationX(270).uvLock(true).addModel()
                .condition(DOWN, false).condition(EAST, false).condition(NORTH, false)
                .condition(SOUTH, false).condition(UP, false).condition(WEST, false)
                .end();

        clump.part().modelFile(resinClump).rotationX(90).uvLock(true).addModel()
                .condition(DOWN, false).condition(EAST, false).condition(NORTH, false)
                .condition(SOUTH, false).condition(UP, false).condition(WEST, false)
                .end();

        ModelFile heartY = models()
                .withExistingParent("creaking_heart", mcLoc("block/cube_column"))
                .texture("end", mcLoc("block/creaking_heart_top"))
                .texture("side", mcLoc("block/creaking_heart"));

        ModelFile heartHorizontal = models()
                .withExistingParent("creaking_heart_horizontal", mcLoc("block/cube_column_horizontal"))
                .texture("end", mcLoc("block/creaking_heart_top"))
                .texture("side", mcLoc("block/creaking_heart"));

        ModelFile heartActiveY = models()
                .withExistingParent("creaking_heart_active", mcLoc("block/cube_column"))
                .texture("end", mcLoc("block/creaking_heart_active_top"))
                .texture("side", mcLoc("block/creaking_heart_active"));

        ModelFile heartActiveHorizontal = models()
                .withExistingParent("creaking_heart_active_horizontal", mcLoc("block/cube_column_horizontal"))
                .texture("end", mcLoc("block/creaking_heart_active_top"))
                .texture("side", mcLoc("block/creaking_heart_active"));

        getVariantBuilder(ModBlocks.CREAKING_HEART.get()).forAllStates(state -> {
            boolean active = state.getValue(CreakingHeartBlock.STATE) == CreakingHeartState.AWAKE;
            Direction.Axis axis = state.getValue(AXIS);

            ModelFile model;
            int xRot = 0;
            int yRot = 0;

            if (axis == Direction.Axis.Y) {
                model = active ? heartActiveY : heartY;
            } else {
                model = active ? heartActiveHorizontal : heartHorizontal;
                xRot = 90;
                if (axis == Direction.Axis.X) {
                    yRot = 90;
                }
            }

            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(xRot)
                    .rotationY(yRot)
                    .build();
        });

        // Spring to Life
        simpleBlockWithItem(ModBlocks.BUSH.get(), models().withExistingParent("bush", "minecraft:block/tinted_cross").texture("cross", mcLoc("block/bush")).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.FIREFLY_BUSH.get(), models().withExistingParent("firefly_bush", "minecraft:block/tinted_cross").texture("cross", mcLoc("block/firefly_bush")).renderType("cutout"));

        getVariantBuilder(ModBlocks.WILDFLOWERS.get()).forAllStates(state -> {
            Direction facing = state.getValue(HORIZONTAL_FACING);
            int amount = state.getValue(FLOWER_AMOUNT);
            return ConfiguredModel.builder()
                    .modelFile(models().withExistingParent("wildflowers_" + amount, mcLoc("block/template_flowerbed_" + amount))
                            .texture("flowerbed", mcLoc("block/wildflowers"))
                            .renderType("cutout"))
                    .rotationY((int) facing.toYRot())
                    .build();
        });
        itemModels().withExistingParent("wildflowers", mcLoc("item/generated"))
                .texture("layer0", mcLoc("item/wildflowers"));

        getVariantBuilder(ModBlocks.LEAF_LITTER.get()).forAllStates(state -> {
            Direction facing = state.getValue(HORIZONTAL_FACING);
            int amount = state.getValue(FLOWER_AMOUNT);
            return ConfiguredModel.builder()
                    .modelFile(models().withExistingParent("leaf_litter_" + amount, mcLoc("block/template_leaf_litter_" + amount))
                            .texture("texture", mcLoc("block/leaf_litter"))
                            .renderType("cutout"))
                    .rotationY((int) facing.toYRot())
                    .build();
        });
        itemModels().withExistingParent("leaf_litter", mcLoc("item/generated"))
                .texture("layer0", mcLoc("item/leaf_litter"));

        getVariantBuilder(ModBlocks.DRIED_GHAST.get()).forAllStates(state -> {
            Direction facing = state.getValue(HORIZONTAL_FACING);
            int hydration = state.getValue(ModBlockStateProperties.HYDRATION_LEVEL);
            String name = "dried_ghast_hydration_" + hydration;
            return ConfiguredModel.builder()
                    .modelFile(models().withExistingParent(name, mcLoc("block/dried_ghast"))
                            .texture("bottom", mcLoc("block/dried_ghast_hydration_" + hydration + "_bottom"))
                            .texture("east", mcLoc("block/dried_ghast_hydration_" + hydration + "_east"))
                            .texture("north", mcLoc("block/dried_ghast_hydration_" + hydration + "_north"))
                            .texture("south", mcLoc("block/dried_ghast_hydration_" + hydration + "_south"))
                            .texture("tentacles", mcLoc("block/dried_ghast_hydration_" + hydration + "_tentacles"))
                            .texture("top", mcLoc("block/dried_ghast_hydration_" + hydration + "_top"))
                            .texture("west", mcLoc("block/dried_ghast_hydration_" + hydration + "_west"))
                            .renderType("cutout"))
                    .rotationY((int) facing.toYRot())
                    .build();
        });
        itemModels().withExistingParent("dried_ghast", mcLoc("block/dried_ghast_hydration_0"));

        simpleBlockWithItem(ModBlocks.CACTUS_FLOWER.get(), models().cross("cactus_flower", mcLoc("block/cactus_flower")).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.SHORT_DRY_GRASS.get(), models().cross("short_dry_grass", mcLoc("block/short_dry_grass")).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.TALL_DRY_GRASS.get(), models().cross("tall_dry_grass", mcLoc("block/tall_dry_grass")).renderType("cutout"));
        
        getVariantBuilder(ModBlocks.PALE_OAK_LEAVES.get()).partialState().addModels(
                new ConfiguredModel(models().getExistingFile(mcLoc("block/pale_oak_leaves")), 0, 0, false, 1),
                new ConfiguredModel(models().getExistingFile(mcLoc("block/pale_oak_leaves1")), 0, 0, false, 1),
                new ConfiguredModel(models().getExistingFile(mcLoc("block/pale_oak_leaves2")), 0, 0, false, 1),
                new ConfiguredModel(models().getExistingFile(mcLoc("block/pale_oak_leaves3")), 0, 0, false, 1),
                new ConfiguredModel(models().getExistingFile(mcLoc("block/pale_oak_leaves4")), 0, 0, false, 1)
        );
    }

    private ModelFile resinClumpModel() {
        BlockModelBuilder b = models().getBuilder("resin_clump")
                .ao(false)
                .renderType("cutout")
                .texture("particle", mcLoc("block/resin_clump"))
                .texture("texture", mcLoc("block/resin_clump"));

        b.element()
                .from(0, 0, 0.1F).to(16, 16, 0.1F)
                .face(Direction.NORTH).uvs(16, 0, 0, 16).texture("#texture").end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#texture").end()
                .end();

        return b;
    }

    @SuppressWarnings("SameParameterValue")
    private ModelFile paleHangingMossModel(String modelName, String texturePath) {
        return models()
                .cross(modelName, mcLoc(texturePath))
                .renderType("cutout");
    }

    @SuppressWarnings("SameParameterValue")
    private ModelFile paleHangingMossTipModel(String modelName, String texturePath) {
        return models()
                .cross(modelName, mcLoc(texturePath))
                .renderType("cutout");
    }

}
