package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CreakingHeartBlock;
import com.otterly76.ott.registry.ModBlockStateProperties;
import com.otterly76.ott.util.block.CreakingHeartState;
import com.otterly76.ott.block.custom.HangingMossBlock;
import com.otterly76.ott.block.custom.MossyCarpetBlock;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.state.properties.ChestType;
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


        // Copper Chains
        ModBlocks.COPPER_CHAINS.forEach((name, block) -> {
            String stateName = name.startsWith("waxed_") ? name.substring(6) : name;
            ResourceLocation texture = mcLoc("block/" + stateName + "copper_chain");
            ModelFile model = models().withExistingParent(name + "copper_chain", mcLoc("block/chain"))
                    .texture("1", texture)
                    .texture("particle", texture)
                    .renderType("cutout");
            
            getVariantBuilder(block.get()).forAllStates(state -> {
                Direction.Axis axis = state.getValue(ChainBlock.AXIS);
                return ConfiguredModel.builder()
                        .modelFile(model)
                        .rotationX(axis == Direction.Axis.Y ? 0 : 90)
                        .rotationY(axis == Direction.Axis.X ? 90 : 0)
                        .build();
            });
        });

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
                    .rotationY(((int) facing.toYRot() + 180) % 360)
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
                    .rotationY(((int) facing.toYRot() + 180) % 360)
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
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
        itemModels().withExistingParent("dried_ghast", mcLoc("block/dried_ghast_hydration_0"));

        // Cactus Flower
        getVariantBuilder(ModBlocks.CACTUS_FLOWER.get()).partialState().setModels(new ConfiguredModel(models().getExistingFile(mcLoc("block/cactus_flower"))));

        // Dry Grass
        simpleBlockWithItem(ModBlocks.SHORT_DRY_GRASS.get(), models().cross("short_dry_grass", mcLoc("block/short_dry_grass")).renderType("cutout"));
        simpleBlockWithItem(ModBlocks.TALL_DRY_GRASS.get(), models().cross("tall_dry_grass", mcLoc("block/tall_dry_grass")).renderType("cutout"));

        // Copper Blocks
        String[] copperStates = {"", "exposed_", "weathered_", "oxidized_"};
        for (String state : copperStates) {
            String waxedPrefix = "waxed_" + state;

            // Cauldrons
            registerCopperCauldronSet(state);
            registerCopperCauldronSet(waxedPrefix);

            // Bars
            registerCopperBars(ModBlocks.COPPER_BARS.get(state).get(), mcLoc("block/" + state + "copper_bars"));
            registerCopperBars(ModBlocks.COPPER_BARS.get(waxedPrefix).get(), mcLoc("block/" + state + "copper_bars"));

            // Buttons
            String blockTexturePath = state.isEmpty() ? "copper_block" : state + "copper";
            ResourceLocation blockTexture = mcLoc("block/" + blockTexturePath);
            buttonBlock((ButtonBlock) ModBlocks.COPPER_BUTTONS.get(state).get(), blockTexture);
            buttonBlock((ButtonBlock) ModBlocks.COPPER_BUTTONS.get(waxedPrefix).get(), blockTexture);

            // Generate the button inventory model for the item to reference:
            models().withExistingParent(state + "copper_button_inventory", mcLoc("block/button_inventory"))
                    .texture("texture", blockTexture);
            models().withExistingParent(waxedPrefix + "copper_button_inventory", mcLoc("block/button_inventory"))
                    .texture("texture", blockTexture);

            // Doors
            registerCutoutDoor((DoorBlock) ModBlocks.COPPER_DOORS.get(state).get(), mcLoc("block/" + state + "copper_door_bottom"), mcLoc("block/" + state + "copper_door_top"));
            registerCutoutDoor((DoorBlock) ModBlocks.COPPER_DOORS.get(waxedPrefix).get(), mcLoc("block/" + state + "copper_door_bottom"), mcLoc("block/" + state + "copper_door_top"));

            // Trapdoors
            registerCutoutTrapdoor((TrapDoorBlock) ModBlocks.COPPER_TRAPDOORS.get(state).get(), mcLoc("block/" + state + "copper_trapdoor"));
            registerCutoutTrapdoor((TrapDoorBlock) ModBlocks.COPPER_TRAPDOORS.get(waxedPrefix).get(), mcLoc("block/" + state + "copper_trapdoor"));

            // Lanterns
            registerCopperLantern(ModBlocks.COPPER_LANTERNS.get(state).get(), mcLoc("block/" + state + "copper_lantern"));
            registerCopperLantern(ModBlocks.COPPER_LANTERNS.get(waxedPrefix).get(), mcLoc("block/" + state + "copper_lantern"));

            registerCopperLantern(ModBlocks.COPPER_SOUL_LANTERNS.get(state).get(), mcLoc("block/" + state + "copper_soul_lantern"));
            registerCopperLantern(ModBlocks.COPPER_SOUL_LANTERNS.get(waxedPrefix).get(), mcLoc("block/" + state + "copper_soul_lantern"));

            // Golem Statues
            registerCopperGolemStatue(ModBlocks.COPPER_GOLEM_STATUES.get(state).get(), blockTexture);
            registerCopperGolemStatue(ModBlocks.COPPER_GOLEM_STATUES.get(waxedPrefix).get(), blockTexture);

            // Lightning Rods
            Block rod = state.isEmpty() ? Blocks.LIGHTNING_ROD : ModBlocks.LIGHTNING_RODS.get(state).get();
            registerLightningRod(rod, state);
            registerLightningRod(ModBlocks.LIGHTNING_RODS.get(waxedPrefix).get(), state);

            // Grates
            registerCopperGrate(BuiltInRegistries.BLOCK.get(mcLoc(state + "copper_grate")), state);
            registerCopperGrate(BuiltInRegistries.BLOCK.get(mcLoc(waxedPrefix + "copper_grate")), state);

            // Hoppers
            registerCopperHopper(ModBlocks.COPPER_HOPPERS.get(state).get(), state);
            registerCopperHopper(ModBlocks.COPPER_HOPPERS.get(waxedPrefix).get(), state);

            // Ladders
            registerCopperLadder(ModBlocks.COPPER_LADDERS.get(state).get(), mcLoc("block/" + state + "copper_ladder"));
            registerCopperLadder(ModBlocks.COPPER_LADDERS.get(waxedPrefix).get(), mcLoc("block/" + state + "copper_ladder"));

            // Rails
            registerCopperRail(ModBlocks.COPPER_RAILS.get(state).get(), state);
            registerCopperRail(ModBlocks.COPPER_RAILS.get(waxedPrefix).get(), state);

            // Anvils
            for (String damagePrefix : new String[]{"", "chipped_", "damaged_"}) {
                registerCopperAnvil(ModBlocks.COPPER_ANVILS.get(damagePrefix + state).get(), state, damagePrefix);
                registerCopperAnvil(ModBlocks.COPPER_ANVILS.get("waxed_" + damagePrefix + state).get(), state, damagePrefix);
            }
        }

        // Chests (They are not in a map)
        registerCopperChest(ModBlocks.COPPER_CHEST.get(), mcLoc("block/copper_block"));
        registerCopperChest(ModBlocks.EXPOSED_COPPER_CHEST.get(), mcLoc("block/exposed_copper"));
        registerCopperChest(ModBlocks.WEATHERED_COPPER_CHEST.get(), mcLoc("block/weathered_copper"));
        registerCopperChest(ModBlocks.OXIDIZED_COPPER_CHEST.get(), mcLoc("block/oxidized_copper"));
        registerCopperChest(ModBlocks.WAXED_COPPER_CHEST.get(), mcLoc("block/copper_block"));
        registerCopperChest(ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get(), mcLoc("block/exposed_copper"));
        registerCopperChest(ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get(), mcLoc("block/weathered_copper"));
        registerCopperChest(ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get(), mcLoc("block/oxidized_copper"));
    }

    protected void registerCopperBars(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile post = models().withExistingParent(name + "_post", mcLoc("block/template_bars_post")).texture("bars", texture).texture("edge", texture).renderType("cutout");
        ModelFile side = models().withExistingParent(name + "_side", mcLoc("block/template_bars_side")).texture("bars", texture).texture("edge", texture).renderType("cutout");
        ModelFile sideAlt = models().withExistingParent(name + "_side_alt", mcLoc("block/template_bars_side_alt")).texture("bars", texture).texture("edge", texture).renderType("cutout");
        ModelFile cap = models().withExistingParent(name + "_cap", mcLoc("block/template_bars_cap")).texture("bars", texture).texture("edge", texture).renderType("cutout");
        ModelFile capAlt = models().withExistingParent(name + "_cap_alt", mcLoc("block/template_bars_cap_alt")).texture("bars", texture).texture("edge", texture).renderType("cutout");
        ModelFile postEnds = models().withExistingParent(name + "_post_ends", mcLoc("block/template_bars_post_ends")).texture("bars", texture).texture("edge", texture).renderType("cutout");

        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

        builder.part().modelFile(postEnds).addModel().end();
        builder.part().modelFile(post).addModel().condition(NORTH, false).condition(EAST, false).condition(SOUTH, false).condition(WEST, false).end();
        builder.part().modelFile(cap).addModel().condition(NORTH, true).condition(EAST, false).condition(SOUTH, false).condition(WEST, false).end();
        builder.part().modelFile(cap).rotationY(90).addModel().condition(NORTH, false).condition(EAST, true).condition(SOUTH, false).condition(WEST, false).end();
        builder.part().modelFile(capAlt).addModel().condition(NORTH, false).condition(EAST, false).condition(SOUTH, true).condition(WEST, false).end();
        builder.part().modelFile(capAlt).rotationY(90).addModel().condition(NORTH, false).condition(EAST, false).condition(SOUTH, false).condition(WEST, true).end();
        builder.part().modelFile(side).addModel().condition(NORTH, true).end();
        builder.part().modelFile(side).rotationY(90).addModel().condition(EAST, true).end();
        builder.part().modelFile(sideAlt).addModel().condition(SOUTH, true).end();
        builder.part().modelFile(sideAlt).rotationY(90).addModel().condition(WEST, true).end();
    }

    protected void registerCopperLantern(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile lantern = models().withExistingParent(name, mcLoc("block/template_lantern"))
                .texture("lantern", texture)
                .renderType("cutout");
        ModelFile hangingLantern = models().withExistingParent(name + "_hanging", mcLoc("block/template_hanging_lantern"))
                .texture("lantern", texture)
                .renderType("cutout");

        getVariantBuilder(block).forAllStates(state -> {
            boolean hanging = state.getValue(LanternBlock.HANGING);
            return ConfiguredModel.builder()
                    .modelFile(hanging ? hangingLantern : lantern)
                    .build();
        });
    }

    protected void registerCopperGolemStatue(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().withExistingParent(name, mcLoc("block/block"))
                .texture("particle", texture);

        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(HorizontalDirectionalBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    protected void registerCopperHopper(Block block, String state) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().withExistingParent(name, mcLoc("block/hopper"))
                .texture("top", mcLoc("block/" + state + "copper_hopper_top"))
                .texture("inside", mcLoc("block/" + state + "copper_hopper_inside"))
                .texture("outside", mcLoc("block/" + state + "copper_hopper_outside"))
                .renderType("cutout");
        ModelFile modelSide = models().withExistingParent(name + "_side", mcLoc("block/hopper_side"))
                .texture("top", mcLoc("block/" + state + "copper_hopper_top"))
                .texture("inside", mcLoc("block/" + state + "copper_hopper_inside"))
                .texture("side", mcLoc("block/" + state + "copper_hopper_outside"))
                .renderType("cutout");

        getVariantBuilder(block).forAllStates(s -> {
            Direction facing = s.getValue(HopperBlock.FACING);
            ModelFile m = facing == Direction.DOWN ? model : modelSide;
            return ConfiguredModel.builder()
                    .modelFile(m)
                    .rotationY(facing == Direction.DOWN ? 0 : ((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    protected void registerCopperChest(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().withExistingParent(name, mcLoc("block/cube_all"))
                .texture("all", texture);
        ModelFile modelLeft = models().withExistingParent(name + "_left", mcLoc("block/cube_all"))
                .texture("all", texture);
        ModelFile modelRight = models().withExistingParent(name + "_right", mcLoc("block/cube_all"))
                .texture("all", texture);

        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(ChestBlock.FACING);
            ChestType type = state.getValue(ChestBlock.TYPE);
            ModelFile m = type == ChestType.LEFT ? modelLeft : (type == ChestType.RIGHT ? modelRight : model);
            return ConfiguredModel.builder()
                    .modelFile(m)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    protected void registerLightningRod(Block block, String state) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().withExistingParent(name, mcLoc("block/template_lightning_rod"))
                .texture("texture", mcLoc("block/" + state + "lightning_rod"))
                .renderType("cutout");

        getVariantBuilder(block).forAllStates(s -> {
            Direction facing = s.getValue(LightningRodBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationX(facing == Direction.DOWN ? 180 : (facing.getAxis().isHorizontal() ? 90 : 0))
                    .rotationY(facing.getAxis().isVertical() ? 0 : ((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    protected void registerCopperGrate(Block block, String state) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().withExistingParent(name, mcLoc("block/template_copper_grate"))
                .texture("all", mcLoc("block/" + state + "copper_grate"))
                .texture("particle", mcLoc("block/" + state + "copper_grate"))
                .renderType("cutout");

        simpleBlock(block, model);
    }

    protected void registerCopperLadder(Block block, ResourceLocation texture) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelFile model = models().withExistingParent(name, mcLoc("block/ladder"))
                .texture("texture", texture)
                .texture("particle", texture)
                .renderType("cutout");

        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(LadderBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    protected void registerCopperRail(Block block, String state) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ResourceLocation straight = mcLoc("block/" + state + "copper_rail");
        ResourceLocation curved = mcLoc("block/" + state + "copper_rail_corner");

        ModelFile flat = models().withExistingParent(name + "_flat", mcLoc("block/rail"))
                .texture("rail", straight)
                .texture("particle", straight)
                .renderType("cutout");
        ModelFile curvedModel = models().withExistingParent(name + "_curved", mcLoc("block/rail_corner"))
                .texture("rail", curved)
                .texture("particle", curved)
                .renderType("cutout");
        ModelFile raisedNe = models().withExistingParent(name + "_raised_ne", mcLoc("block/rail_raised_ne"))
                .texture("rail", straight)
                .texture("particle", straight)
                .renderType("cutout");
        ModelFile raisedSw = models().withExistingParent(name + "_raised_sw", mcLoc("block/rail_raised_sw"))
                .texture("rail", straight)
                .texture("particle", straight)
                .renderType("cutout");

        getVariantBuilder(block).forAllStates(stateBlock -> {
            net.minecraft.world.level.block.state.properties.RailShape shape = stateBlock.getValue(RailBlock.SHAPE);
            return switch (shape) {
                case NORTH_SOUTH -> ConfiguredModel.builder().modelFile(flat).build();
                case EAST_WEST -> ConfiguredModel.builder().modelFile(flat).rotationY(90).build();
                case ASCENDING_EAST -> ConfiguredModel.builder().modelFile(raisedSw).rotationY(270).build();
                case ASCENDING_WEST -> ConfiguredModel.builder().modelFile(raisedNe).rotationY(270).build();
                case ASCENDING_NORTH -> ConfiguredModel.builder().modelFile(raisedNe).rotationY(180).build();
                case ASCENDING_SOUTH -> ConfiguredModel.builder().modelFile(raisedSw).rotationY(180).build();
                case SOUTH_EAST -> ConfiguredModel.builder().modelFile(curvedModel).build();
                case SOUTH_WEST -> ConfiguredModel.builder().modelFile(curvedModel).rotationY(90).build();
                case NORTH_WEST -> ConfiguredModel.builder().modelFile(curvedModel).rotationY(180).build();
                case NORTH_EAST -> ConfiguredModel.builder().modelFile(curvedModel).rotationY(270).build();
            };
        });
    }

    protected void registerCopperAnvil(Block block, String state, String damage) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        ResourceLocation body = mcLoc("block/" + state + "copper_anvil");
        ResourceLocation top = mcLoc("block/" + damage + state + "copper_anvil_top");

        ModelFile model = models().withExistingParent(name, mcLoc("block/template_anvil"))
                .texture("body", body)
                .texture("top", top)
                .texture("tools", mcLoc("block/anvil_detail"))
                .texture("particle", body)
                .renderType("cutout");

        getVariantBuilder(block).forAllStates(stateBlock -> {
            Direction facing = stateBlock.getValue(AnvilBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(model)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
    }

    protected void registerCopperCauldronSet(String stateKey) {
        String stateName = stateKey.startsWith("waxed_") ? stateKey.substring(6) : stateKey;
        ResourceLocation copperTex = mcLoc("block/" + stateName + "copper_cauldron");

        // Base Model
        ModelFile baseModel = models().withExistingParent(stateKey + "copper_cauldron", mcLoc("block/copper_cauldron"))
                .texture("3", copperTex)
                .renderType("cutout");

        // Empty
        simpleBlock(ModBlocks.COPPER_CAULDRONS.get(stateKey).get(), baseModel);

        // Water
        registerFluidCauldron(ModBlocks.COPPER_WATER_CAULDRONS.get(stateKey).get(), baseModel, mcLoc("block/water_still"), true, true);

        // Lava
        registerFluidCauldron(ModBlocks.COPPER_LAVA_CAULDRONS.get(stateKey).get(), baseModel, mcLoc("block/lava_still"), false, false);

        // Powder Snow
        registerFluidCauldron(ModBlocks.COPPER_POWDER_SNOW_CAULDRONS.get(stateKey).get(), baseModel, mcLoc("block/powder_snow"), true, false);
    }

    protected void registerFluidCauldron(Block block, ModelFile baseModel, ResourceLocation fluidTex, boolean layered, boolean water) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        MultiPartBlockStateBuilder builder = getMultipartBuilder(block);

        // Base cauldron is always there
        builder.part().modelFile(baseModel).addModel().end();

        if (layered) {
            for (int level = 1; level <= 3; level++) {
                ModelFile contentModel = getFluidContentModel(name + "_level" + level, fluidTex, level, water);
                builder.part().modelFile(contentModel).addModel()
                        .condition(LayeredCauldronBlock.LEVEL, level)
                        .end();
            }
        } else {
            ModelFile contentModel = getFluidContentModel(name + "_full", fluidTex, 3, water);
            builder.part().modelFile(contentModel).addModel().end();
        }
    }

    private ModelFile getFluidContentModel(String name, ResourceLocation fluidTex, int level, boolean water) {
        float y = 5 + (level == 3 ? 9 : (level == 2 ? 7 : 3));
        BlockModelBuilder builder = models().getBuilder(name)
                .texture("particle", fluidTex)
                .texture("content", fluidTex);

        BlockModelBuilder.ElementBuilder element = builder.element()
                .from(3, y, 3).to(13, y, 13);

        if (water) {
            element.face(Direction.UP).uvs(3, 3, 13, 13).texture("#content").tintindex(0).end()
                   .face(Direction.DOWN).uvs(3, 3, 13, 13).texture("#content").tintindex(0).end();
        } else {
            element.face(Direction.UP).uvs(3, 3, 13, 13).texture("#content").end()
                   .face(Direction.DOWN).uvs(3, 3, 13, 13).texture("#content").end();
        }

        return builder.renderType("cutout");
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