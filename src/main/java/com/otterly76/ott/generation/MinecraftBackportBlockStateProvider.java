package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.block.custom.HangingMossBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.*;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MinecraftBackportBlockStateProvider extends BlockStateProvider {

    public MinecraftBackportBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "minecraft", existingFileHelper);
    }

    @Override
    @SuppressWarnings("IfCanBeSwitch")
    protected void registerStatesAndModels() {
        // Leaves (basic model; we can switch back to fluffy variants later)
        simpleBlock(ModBlocks.PALE_OAK_LEAVES.get());

        // Pale moss block (cube_all)
        simpleBlock(ModBlocks.PALE_MOSS_BLOCK.get());

        // Pale moss carpet (vanilla carpet model)
        ModelFile paleMossCarpetModel = models()
                .withExistingParent("pale_moss_carpet", mcLoc("block/carpet"))
                .texture("wool", mcLoc("block/pale_moss_carpet"));
        simpleBlock(ModBlocks.PALE_MOSS_CARPET.get(), paleMossCarpetModel);
        itemModels().withExistingParent("pale_moss_carpet", mcLoc("block/pale_moss_carpet"));

        // --- Logs / Wood (vanilla-style) ---
        RotatedPillarBlock paleLog = ModBlocks.PALE_OAK_LOG.get();
        RotatedPillarBlock paleWood = ModBlocks.PALE_OAK_WOOD.get();
        RotatedPillarBlock strippedLog = ModBlocks.STRIPPED_PALE_OAK_LOG.get();
        RotatedPillarBlock strippedWood = ModBlocks.STRIPPED_PALE_OAK_WOOD.get();

        // LOG: side + top
        axisBlock(
                paleLog,
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/pale_oak_log"),
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/pale_oak_log_top")
        );

        // WOOD: log texture on all sides (no pale_oak_wood.png needed)
        axisBlock(
                paleWood,
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/pale_oak_log"),
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/pale_oak_log")
        );

        // STRIPPED LOG: side + top
        axisBlock(
                strippedLog,
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/stripped_pale_oak_log"),
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/stripped_pale_oak_log_top")
        );

        // STRIPPED WOOD: stripped log texture on all sides
        axisBlock(
                strippedWood,
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/stripped_pale_oak_log"),
                ResourceLocation.fromNamespaceAndPath("minecraft", "block/stripped_pale_oak_log")
        );

        // --- Pale Oak wood set (core building blocks) ---
        ResourceLocation palePlanks = mcLoc("block/pale_oak_planks");

        // Planks
        simpleBlockWithItem(ModBlocks.PALE_OAK_PLANKS.get(), models().cubeAll("pale_oak_planks", palePlanks));

        // Stairs / Slab
        stairsBlock(ModBlocks.PALE_OAK_STAIRS.get(), palePlanks);
        slabBlock(ModBlocks.PALE_OAK_SLAB.get(), palePlanks, palePlanks);

        // Fence / Gate
        fenceBlock(ModBlocks.PALE_OAK_FENCE.get(), palePlanks);
        models().fenceInventory("pale_oak_fence_inventory", palePlanks);

        fenceGateBlock(ModBlocks.PALE_OAK_FENCE_GATE.get(), palePlanks);

        // Pressure plate / Button
        pressurePlateBlock(ModBlocks.PALE_OAK_PRESSURE_PLATE.get(), palePlanks);
        buttonBlock(ModBlocks.PALE_OAK_BUTTON.get(), palePlanks);

        // Door / Trapdoor
        // These expect the standard vanilla texture naming you already have:
        // - block/pale_oak_door_top
        // - block/pale_oak_door_bottom
        // - block/pale_oak_trapdoor
        doorBlock(ModBlocks.PALE_OAK_DOOR.get(),
                mcLoc("block/pale_oak_door_bottom"),
                mcLoc("block/pale_oak_door_top")
        );

        trapdoorBlock(ModBlocks.PALE_OAK_TRAPDOOR.get(),
                mcLoc("block/pale_oak_trapdoor"),
                true
        );

        // --- Pale Oak signs (block-entity rendered) ---
        // Using planks texture for particles.

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

        // --- Pale Oak sapling + potted sapling ---
        ModelFile paleOakSapling = models().cross(
                "pale_oak_sapling",
                mcLoc("block/pale_oak_sapling")
        );
        simpleBlockWithItem(ModBlocks.PALE_OAK_SAPLING.get(), paleOakSapling);

        ModelFile pottedPaleOakSapling = models().withExistingParent(
                "potted_pale_oak_sapling",
                mcLoc("block/flower_pot_cross")
        ).texture("plant", mcLoc("block/pale_oak_sapling"));
        simpleBlock(ModBlocks.POTTED_PALE_OAK_SAPLING.get(), pottedPaleOakSapling);

        // --- Pale Hanging Moss (TIP vs BASE) ---
        ModelFile baseModel = paleHangingMossModel("pale_hanging_moss", "block/pale_hanging_moss");
        ModelFile tipModel = paleHangingMossTipModel("pale_hanging_moss_tip", "block/pale_hanging_moss_tip");

        getVariantBuilder(ModBlocks.PALE_HANGING_MOSS.get())
                .partialState().with(HangingMossBlock.TIP, false).modelForState().modelFile(baseModel).addModel()
                .partialState().with(HangingMossBlock.TIP, true).modelForState().modelFile(tipModel).addModel();

        ModelFile closedEyeblossom = models().cross(
                "closed_eyeblossom",
                mcLoc("block/closed_eyeblossom")
        );

        simpleBlockWithItem(ModBlocks.CLOSED_EYEBLOSSOM.get(), closedEyeblossom);

        ModelFile openEyeblossom = models().cross(
                "open_eyeblossom",
                mcLoc("block/open_eyeblossom")
        );

        simpleBlockWithItem(ModBlocks.OPEN_EYEBLOSSOM.get(), openEyeblossom);

        // --- Potted Eyeblossoms ---
        // Blockstates/models for flower pots use the builtin flower pot parent and the plant texture.
        ModelFile pottedClosedEyeblossom = models().withExistingParent(
                "potted_closed_eyeblossom",
                mcLoc("block/flower_pot_cross")
        ).texture("plant", mcLoc("block/closed_eyeblossom"));

        simpleBlock(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(), pottedClosedEyeblossom);

        ModelFile pottedOpenEyeblossom = models().withExistingParent(
                "potted_open_eyeblossom",
                mcLoc("block/flower_pot_cross")
        ).texture("plant", mcLoc("block/open_eyeblossom"));

        simpleBlock(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(), pottedOpenEyeblossom);

        simpleBlock(ModBlocks.RESIN_BLOCK.get());
        simpleBlock(ModBlocks.RESIN_BRICKS.get());
        simpleBlock(ModBlocks.CHISELED_RESIN_BRICKS.get());

        // Resin wall/stairs/slab
        ResourceLocation resinBricksTex = mcLoc("block/resin_bricks");
        stairsBlock(ModBlocks.RESIN_BRICK_STAIRS.get(), resinBricksTex);
        slabBlock(ModBlocks.RESIN_BRICK_SLAB.get(), resinBricksTex, resinBricksTex);
        wallBlock(ModBlocks.RESIN_BRICK_WALL.get(), resinBricksTex);

        // Ensure the WALL INVENTORY model exists (item model provider points at it)
        models().wallInventory("resin_brick_wall_inventory", resinBricksTex);

        // Resin clump (GlowLichen-style multipart)
        ModelFile resinClumpModel = resinClumpModel();

        MultiPartBlockStateBuilder clump = getMultipartBuilder(ModBlocks.RESIN_CLUMP.get());

        // Per-face parts (match the template rotations)
        clump.part().modelFile(resinClumpModel).addModel().condition(BlockStateProperties.NORTH, true).end();
        clump.part().modelFile(resinClumpModel).rotationY(90).uvLock(true).addModel().condition(BlockStateProperties.EAST, true).end();
        clump.part().modelFile(resinClumpModel).rotationY(180).uvLock(true).addModel().condition(BlockStateProperties.SOUTH, true).end();
        clump.part().modelFile(resinClumpModel).rotationY(270).uvLock(true).addModel().condition(BlockStateProperties.WEST, true).end();
        clump.part().modelFile(resinClumpModel).rotationX(270).uvLock(true).addModel().condition(BlockStateProperties.UP, true).end();
        clump.part().modelFile(resinClumpModel).rotationX(90).uvLock(true).addModel().condition(BlockStateProperties.DOWN, true).end();

        // “All faces false” case: apply multiple planes so it looks like a clump
        clump.part().modelFile(resinClumpModel).addModel()
                .condition(BlockStateProperties.DOWN, false)
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.UP, false)
                .condition(BlockStateProperties.WEST, false)
                .end();

        clump.part().modelFile(resinClumpModel).rotationY(90).uvLock(true).addModel()
                .condition(BlockStateProperties.DOWN, false)
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.UP, false)
                .condition(BlockStateProperties.WEST, false)
                .end();

        clump.part().modelFile(resinClumpModel).rotationY(180).uvLock(true).addModel()
                .condition(BlockStateProperties.DOWN, false)
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.UP, false)
                .condition(BlockStateProperties.WEST, false)
                .end();

        clump.part().modelFile(resinClumpModel).rotationY(270).uvLock(true).addModel()
                .condition(BlockStateProperties.DOWN, false)
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.UP, false)
                .condition(BlockStateProperties.WEST, false)
                .end();

        clump.part().modelFile(resinClumpModel).rotationX(270).uvLock(true).addModel()
                .condition(BlockStateProperties.DOWN, false)
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.UP, false)
                .condition(BlockStateProperties.WEST, false)
                .end();

        clump.part().modelFile(resinClumpModel).rotationX(90).uvLock(true).addModel()
                .condition(BlockStateProperties.DOWN, false)
                .condition(BlockStateProperties.EAST, false)
                .condition(BlockStateProperties.NORTH, false)
                .condition(BlockStateProperties.SOUTH, false)
                .condition(BlockStateProperties.UP, false)
                .condition(BlockStateProperties.WEST, false)
                .end();

        // Creaking heart (start simple; if it has axis/variants, we’ll upgrade to a variant builder)
        // Creaking heart (axis + active variants)
        ModelFile heartY = models().withExistingParent("creaking_heart", mcLoc("block/cube_column"))
                .texture("end", mcLoc("block/creaking_heart_top"))
                .texture("side", mcLoc("block/creaking_heart"));

        ModelFile heartHorizontal = models().withExistingParent("creaking_heart_horizontal", mcLoc("block/cube_column_horizontal"))
                .texture("end", mcLoc("block/creaking_heart_top"))
                .texture("side", mcLoc("block/creaking_heart"));

        ModelFile heartActiveY = models().withExistingParent("creaking_heart_active", mcLoc("block/cube_column"))
                .texture("end", mcLoc("block/creaking_heart_active_top"))
                .texture("side", mcLoc("block/creaking_heart_active"));

        ModelFile heartActiveHorizontal = models().withExistingParent("creaking_heart_active_horizontal", mcLoc("block/cube_column_horizontal"))
                .texture("end", mcLoc("block/creaking_heart_active_top"))
                .texture("side", mcLoc("block/creaking_heart_active"));

        getVariantBuilder(ModBlocks.CREAKING_HEART.get()).forAllStates(state -> {
            boolean active = state.getValue(CreakingHeartBlock.ACTIVE);
            Direction.Axis axis = state.getValue(BlockStateProperties.AXIS);

            ModelFile model;
            int xRot = 0;
            int yRot = 0;

            if (axis == Direction.Axis.Y) {
                model = active ? heartActiveY : heartY;
            } else {
                model = active ? heartActiveHorizontal : heartHorizontal;
                xRot = 90;
                // Match the template you posted:
                // axis=z => x=90
                // axis=x => x=90, y=90
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
    }

    private ModelFile resinClumpModel() {
        // Mirrors the backport template model: a flat plane with north/south faces
        BlockModelBuilder b = models().getBuilder("resin_clump")
                .ao(false)
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
        // Matches your BASE_SHAPE: (1,0,1) -> (15,16,15)
        BlockModelBuilder b = models().getBuilder(modelName)
                .parent(models().getExistingFile(mcLoc("block/block")))
                .texture("texture", mcLoc(texturePath))
                .texture("particle", mcLoc(texturePath));

        b.element()
                .from(1, 0, 1).to(15, 16, 15)
                .allFaces((dir, face) -> face.texture("#texture"))
                .end();

        return b;
    }

    @SuppressWarnings("SameParameterValue")
    private ModelFile paleHangingMossTipModel(String modelName, String texturePath) {
        // Matches your TIP_SHAPE: (1,2,1) -> (15,16,15)
        BlockModelBuilder b = models().getBuilder(modelName)
                .parent(models().getExistingFile(mcLoc("block/block")))
                .texture("texture", mcLoc(texturePath))
                .texture("particle", mcLoc(texturePath));

        b.element()
                .from(1, 2, 1).to(15, 16, 15)
                .allFaces((dir, face) -> face.texture("#texture"))
                .end();

        return b;
    }
}