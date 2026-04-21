package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.IGradientBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.color.ModPatterns;
import com.otterly76.ott.block.custom.BeamBlock;
import com.otterly76.ott.block.custom.PergolaBlock;
import com.otterly76.ott.block.custom.SilkCocoonBlock;
import com.otterly76.ott.block.custom.SupportBeamBlock;
import com.otterly76.ott.block.custom.SupportSlabBlock;
import com.otterly76.ott.block.properties.PillarConnection;
import com.otterly76.ott.crop.ThornyHedgeSprouts;
import com.otterly76.ott.hedge.ModHedgeVariants;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OttBlockStateProvider extends ModBlockStateProvider {
    public OttBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.WOOD_SETS.forEach(this::registerWoodSet);
        // Oak structural blocks already have pre-existing resource files; skip to avoid duplicate registration.
        ModBlocks.VANILLA_STRUCTURAL_SETS.forEach((name, set) -> {
            if (!name.equals("oak")) registerVanillaWoodStructural(name, set);
        });

        ModBlocks.SEAGLASS.forEach(block -> {
            String path = blockPath(block.get());
            ModelFile model = models().cubeAll("block/seaglass/" + path, modLoc("block/seaglass/" + path));
            simpleBlock(block.get(), model);
            itemModels().withExistingParent(path, modLoc("block/seaglass/" + path));
        });

        ModBlocks.SEAGLASS_SETS.forEach(this::registerSeaglassColor);
        itemModels().withExistingParent("mixed_limestone_bricks", modLoc("block/limestone/mixed/limestone_00"));
        simpleBlockWithItem(ModBlocks.PLAIN_LIMESTONE.get(), models().cubeAll("block/limestone/limestone", modLoc("block/limestone/limestone")));
        
        ModBlocks.TESTBLOCK.forEach(block -> {
            ModelFile model = models().getExistingFile(modLoc("block/testblock/" + block.getId().getPath()));
            simpleBlock(block.get(), model);
        });

        // Woodcutter (horizontal facing block)
        ModelFile woodcutterModel = models().getExistingFile(modLoc("block/woodcutter"));
        horizontalBlock(ModBlocks.WOODCUTTER.get(), woodcutterModel);
        itemModels().withExistingParent("woodcutter", modLoc("block/woodcutter"));

        ModHedgeVariants.ALL.forEach(variant -> {
            String name = variant.name();
            ResourceLocation leavesTexture = modLoc("block/" + name + "_hedge");

            // Regular Hedge Model
            ResourceLocation hedgeModel = modLoc("block/" + name + "_hedge");
            models().withExistingParent(name + "_hedge", mcLoc("block/cube_all"))
                    .texture("all", leavesTexture)
                    .renderType("minecraft:cutout");
            simpleBlock(ModBlocks.PARTICLE_HEDGES.get(name).get(), models().getExistingFile(hedgeModel));
            itemModels().withExistingParent(name + "_hedge", hedgeModel);

            // Creeping Hedge Model
            ResourceLocation creepingModel = modLoc("block/" + name + "_creeping_hedge");
            models().withExistingParent(name + "_creeping_hedge", mcLoc("block/cube_all"))
                    .texture("all", leavesTexture)
                    .texture("particle", variant.creepOverlayTexture())
                    .renderType("minecraft:cutout");
            simpleBlock(ModBlocks.CREEPING_HEDGES.get(name).get(), models().getExistingFile(creepingModel));
            itemModels().withExistingParent(name + "_creeping_hedge", creepingModel);
        });

        ModBlocks.getAllGradientBlocks().forEach(this::registerGradientBlock);

        ModBlocks.COLOR_SETS.forEach(this::registerColorSet);

        registerPatternBlocks();
        registerPatternShapedBlocks();
        registerElevators();

        registerLantern(ModBlocks.PROTECTIVE_LANTERN.get(), "protective");
        registerLantern(ModBlocks.WATER_LANTERN.get(), "water");
        registerLantern(ModBlocks.LAVA_LANTERN.get(), "lava");
        registerLantern(ModBlocks.SMITE_LANTERN.get(), "smite");

        simpleBlock(ModBlocks.THORNY_HEDGE.get(), models().getExistingFile(modLoc("block/thorny_hedge")));

        // Use builtin/entity for skull blocks so they rely on the BER and don't require a JSON parent
        ModelFile skullModel = models().getBuilder("dragon_skull").parent(new ModelFile.UncheckedModelFile("builtin/entity"));
        simpleBlock(ModBlocks.DRAGON_SKULL.get(), skullModel);
        simpleBlock(ModBlocks.DRAGON_WALL_SKULL.get(), skullModel);

        getVariantBuilder(ModBlocks.THORNY_HEDGE_SPROUTS.get()).forAllStates(state -> {
            int age = state.getValue(ThornyHedgeSprouts.AGE);
            return ConfiguredModel.builder()
                    .modelFile(models().cross("thorny_hedge_sprouts_stage" + age, modLoc("block/thorny_hedge")).renderType("cutout"))
                    .build();
        });

        getVariantBuilder(ModBlocks.SILK_COCOON.get()).forAllStates(state -> {
            boolean hanging = state.getValue(SilkCocoonBlock.HANGING);
            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc(hanging ? "block/hanging_silk_cocoon" : "block/silk_cocoon")))
                    .build();
        });

    }

    // Patterns that have numbered texture variants for per-position randomization.
    // Key: pattern name; Value: texture suffixes that live in block/ (not patterns/).
    private static final Map<String, List<String>> PATTERN_VARIANT_SUFFIXES = Map.of(
            "plastered_stone", List.of("_1", "_2", "_3", "_4", "_5", "_corner")
    );

    private void registerPatternBlocks() {
        ModBlocks.PATTERN_BLOCKS.forEach((pattern, colorMap) -> {
            ResourceLocation patternTexture = modLoc("block/patterns/" + pattern);
            boolean isPillar = ModPatterns.PILLAR_PATTERNS.contains(pattern);

            if (isPillar) {
                // Two-layer model: base texture (untinted) + mask texture (tinted with color)
                ResourceLocation maskTexture = modLoc("block/patterns/" + pattern + "_mask");
                ResourceLocation endTexture = modLoc("block/base/plastered_stone_1");
                ModelFile baseModel = models().withExistingParent("block/patterns/" + pattern + "_model", mcLoc("block/block"))
                        .renderType("minecraft:cutout_mipped")
                        .texture("particle", patternTexture)
                        .texture("all", patternTexture)
                        .texture("mask", maskTexture)
                        .texture("end", endTexture)
                        // Layer 1: base — sides use pattern (untinted), top/bottom use plain plastered stone
                        .element().from(0, 0, 0).to(16, 16, 16)
                        .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#end").cullface(Direction.DOWN).end()
                        .face(Direction.UP).uvs(0, 0, 16, 16).texture("#end").cullface(Direction.UP).end()
                        .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.NORTH).end()
                        .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.SOUTH).end()
                        .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.WEST).end()
                        .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.EAST).end()
                        .end()
                        // Layer 2: mask — sides only (no color overlay on end caps)
                        .element().from(0, 0, 0).to(16, 16, 16)
                        .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#mask").cullface(Direction.NORTH).tintindex(0).end()
                        .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#mask").cullface(Direction.SOUTH).tintindex(0).end()
                        .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#mask").cullface(Direction.WEST).tintindex(0).end()
                        .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#mask").cullface(Direction.EAST).tintindex(0).end()
                        .end();

                colorMap.values().forEach(block -> {
                    // Axis-based blockstate for RotatedPillarBlock
                    getVariantBuilder(block.get())
                            .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Y)
                                .modelForState().modelFile(baseModel).addModel()
                            .partialState().with(BlockStateProperties.AXIS, Direction.Axis.X)
                                .modelForState().modelFile(baseModel).rotationX(90).rotationY(90).addModel()
                            .partialState().with(BlockStateProperties.AXIS, Direction.Axis.Z)
                                .modelForState().modelFile(baseModel).rotationX(90).addModel();
                    itemModels().withExistingParent(block.getId().getPath(), baseModel.getLocation());
                });
            } else {
                // Standard single-layer tinted model
                ModelFile baseModel = models().withExistingParent("block/patterns/" + pattern + "_model", mcLoc("block/block"))
                        .texture("particle", patternTexture)
                        .texture("all", patternTexture)
                        .element().from(0, 0, 0).to(16, 16, 16)
                        .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.DOWN).tintindex(0).end()
                        .face(Direction.UP).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.UP).tintindex(0).end()
                        .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.NORTH).tintindex(0).end()
                        .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.SOUTH).tintindex(0).end()
                        .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.WEST).tintindex(0).end()
                        .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.EAST).tintindex(0).end()
                        .end();

                // Build extra variant models for patterns that have numbered textures
                List<String> extraSuffixes = PATTERN_VARIANT_SUFFIXES.getOrDefault(pattern, List.of());
                List<ModelFile> allModels = new ArrayList<>();
                allModels.add(baseModel);
                for (String suffix : extraSuffixes) {
                    ResourceLocation variantTex = modLoc("block/base/" + pattern + suffix);
                    allModels.add(models().withExistingParent("block/patterns/" + pattern + "_model" + suffix, mcLoc("block/block"))
                            .texture("particle", variantTex)
                            .texture("all", variantTex)
                            .element().from(0, 0, 0).to(16, 16, 16)
                            .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.DOWN).tintindex(0).end()
                            .face(Direction.UP).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.UP).tintindex(0).end()
                            .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.NORTH).tintindex(0).end()
                            .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.SOUTH).tintindex(0).end()
                            .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.WEST).tintindex(0).end()
                            .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.EAST).tintindex(0).end()
                            .end());
                }

                ConfiguredModel[] configuredModels = allModels.stream()
                        .map(m -> ConfiguredModel.builder().modelFile(m).build()[0])
                        .toArray(ConfiguredModel[]::new);

                colorMap.values().forEach(block -> {
                    simpleBlock(block.get(), configuredModels);
                    // Item model always uses the base texture (no randomization in inventory)
                    itemModels().withExistingParent(block.getId().getPath(), baseModel.getLocation());
                });
            }
        });
    }

    private void registerSapling(Block sapling, Block potted, String name) {
        ModelFile saplingModel = models().getExistingFile(modLoc("block/" + name + "_sapling"));
        simpleBlock(sapling, saplingModel);
        itemModels().withExistingParent(name + "_sapling", modLoc("block/" + name + "_sapling"));

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
        String path = block.get().getRegistryID().getPath();
        ResourceLocation sideTexture = modLoc("block/gradients/" + path);
        final ModelFile blockModel = models().cube("block/gradients/" + path, mcLoc("block/" + block.get().getTextureName(block.get().getSecondColor())), mcLoc("block/" + block.get().getTextureName(block.get().getFirstColor())), sideTexture, sideTexture, sideTexture, sideTexture)
                .texture("particle", mcLoc("block/" + block.get().getTextureName(block.get().getFirstColor())))
                .renderType(block.get().getRenderType());
        itemModels().withExistingParent(path, modLoc("block/gradients/" + path));
        directionalBlock(block.get(), blockModel);
    }

    private void registerColorSet(String color, ModBlocks.ColorSetBlocks set) {
        String dir = "block/" + color + "/";

        // Concrete, Terracotta, Wool
        colorSetCubeAll(set.concrete().get(), color, "concrete", dir);
        colorSetCubeAll(set.terracotta().get(), color, "terracotta", dir);
        colorSetCubeAll(set.wool().get(), color, "wool", dir);

        // Concrete Powder
        colorSetCubeAll(set.concretePowder().get(), color, "concrete_powder", dir);

        // Stained Glass
        colorSetCubeAll(set.stainedGlass().get(), color, "stained_glass", "translucent", dir);

        // Stained Glass Pane
        colorSetPaneBlock(set.stainedGlassPane().get(), color, dir);

        // Glazed Terracotta
        horizontalBlock(set.glazedTerracotta().get(), models().withExistingParent(dir + color + "_glazed_terracotta", mcLoc("block/template_glazed_terracotta"))
                .texture("pattern", modLoc("block/glazed_terracotta/" + color)));

        // Shulker Box
        colorSetCubeAll(set.shulkerBox().get(), color, "concrete", dir);

        // Candle
        registerCandle(set.candle().get(), color, dir);

        // Bed
        registerBed(set.bed().get(), color, dir);

        // Carpet
        colorSetCarpet(set.carpet().get(), color, dir);

        // Banner
        registerBanner(set.banner().get(), set.wallBanner().get(), color, dir);

        // ── Structural shapes ─────────────────────────────────────────────────
        ResourceLocation woolTex = modLoc("block/color_set/" + color + "/wool");

        // Plate
        ModelFile csPlate      = models().withExistingParent(dir + color + "_plate",       modLoc("block/plate"))
                .texture("side", woolTex).texture("top", woolTex).texture("frieze", woolTex);
        ModelFile csPlateOuter = models().withExistingParent(dir + color + "_plate_outer", modLoc("block/plate_outer"))
                .texture("top", woolTex).texture("frieze", woolTex);
        ModelFile csPlateInner = models().withExistingParent(dir + color + "_plate_inner", modLoc("block/plate_inner"))
                .texture("side", woolTex).texture("top", woolTex).texture("frieze", woolTex);
        registerFacingShapeBlock(set.plate().get(), csPlate, csPlateOuter, csPlateInner);
        itemModels().withExistingParent(color + "_plate", csPlate.getLocation());

        // Edge
        ModelFile csEdge      = models().withExistingParent(dir + color + "_edge",       modLoc("block/small_plate"))
                .texture("side", woolTex).texture("frieze", woolTex);
        ModelFile csEdgeOuter = models().withExistingParent(dir + color + "_edge_outer", modLoc("block/small_plate_outer"))
                .texture("top", woolTex).texture("frieze", woolTex);
        ModelFile csEdgeInner = models().withExistingParent(dir + color + "_edge_inner", modLoc("block/small_plate_inner"))
                .texture("side", woolTex).texture("top", woolTex).texture("frieze", woolTex);
        registerFacingShapeHalfBlock(set.edge().get(), csEdge, csEdgeOuter, csEdgeInner);
        itemModels().withExistingParent(color + "_edge", csEdge.getLocation());

        // Beam
        ModelFile csBeamY   = models().withExistingParent(dir + color + "_beam_y",      modLoc("block/oak/oak_beam_y"))
                .renderType("minecraft:cutout").texture("1", woolTex).texture("particle", woolTex);
        ModelFile csBeamX   = models().withExistingParent(dir + color + "_beam_x",      modLoc("block/oak/oak_beam_x"))
                .renderType("minecraft:cutout").texture("1", woolTex).texture("particle", woolTex);
        ModelFile csBeamXZ  = models().withExistingParent(dir + color + "_beam_x_z",    modLoc("block/oak/oak_beam_x_z"))
                .renderType("minecraft:cutout").texture("1", woolTex).texture("particle", woolTex);
        ModelFile csBeamBot = models().withExistingParent(dir + color + "_beam_bottom",  modLoc("block/oak/oak_beam_bottom"))
                .renderType("minecraft:cutout").texture("texture", woolTex).texture("particle", woolTex);
        getMultipartBuilder(set.beam().get())
                .part().modelFile(csBeamY).addModel()                .condition(BeamBlock.AXIS_Y, true).end()
                .part().modelFile(csBeamX).addModel()                .condition(BeamBlock.AXIS_X, true).condition(BeamBlock.AXIS_Z, false).end()
                .part().modelFile(csBeamX).rotationY(90).addModel()  .condition(BeamBlock.AXIS_X, false).condition(BeamBlock.AXIS_Z, true).end()
                .part().modelFile(csBeamXZ).addModel()               .condition(BeamBlock.AXIS_X, true).condition(BeamBlock.AXIS_Z, true).end()
                .part().modelFile(csBeamBot).addModel()              .condition(BeamBlock.BOTTOM, true).end();
        itemModels().withExistingParent(color + "_beam", modLoc("item/template_beam"))
                .texture("0", woolTex).texture("particle", woolTex);

        // Pergola
        ModelFile csPergolaY  = models().withExistingParent(dir + color + "_pergola_y",   modLoc("block/oak/oak_pergola_y"))
                .renderType("minecraft:cutout").texture("0", woolTex).texture("particle", woolTex);
        ModelFile csPergolaX  = models().withExistingParent(dir + color + "_pergola_x",   modLoc("block/oak/oak_pergola_x"))
                .renderType("minecraft:cutout").texture("0", woolTex).texture("particle", woolTex);
        ModelFile csPergolaXZ = models().withExistingParent(dir + color + "_pergola_x_z", modLoc("block/oak/oak_pergola_x_z"))
                .renderType("minecraft:cutout").texture("0", woolTex).texture("particle", woolTex);
        getMultipartBuilder(set.pergola().get())
                .part().modelFile(csPergolaY).addModel()               .condition(PergolaBlock.AXIS_Y, true).end()
                .part().modelFile(csPergolaX).addModel()               .condition(PergolaBlock.AXIS_X, true).condition(PergolaBlock.AXIS_Z, false).end()
                .part().modelFile(csPergolaX).rotationY(90).addModel() .condition(PergolaBlock.AXIS_X, false).condition(PergolaBlock.AXIS_Z, true).end()
                .part().modelFile(csPergolaXZ).addModel()              .condition(PergolaBlock.AXIS_X, true).condition(PergolaBlock.AXIS_Z, true).end();
        itemModels().withExistingParent(color + "_pergola", modLoc("item/template_pergola"))
                .texture("0", woolTex).texture("particle", woolTex);

        // Geometric Window
        ModelFile csWindow = models().withExistingParent(dir + color + "_geometric_window", modLoc("block/geometric_window"))
                .texture("texture", woolTex).texture("particle", woolTex)
                .renderType("minecraft:cutout");
        horizontalBlock(set.geometricWindow().get(), csWindow);
        itemModels().withExistingParent(color + "_geometric_window", csWindow.getLocation());
    }

    private void registerSeaglassColor(String color, ModBlocks.SeaglassColorBlocks set) {
        String dir = "block/" + color + "/";
        seaglassCubeAll(set.seaglass().get(),        color, "seaglass",        dir);
        seaglassCubeAll(set.bubblesSeaglass().get(),  color, "bubbles_seaglass",  dir);
        seaglassCubeAll(set.smoothSeaglass().get(),   color, "smooth_seaglass",   dir);
        seaglassCubeAll(set.wavesSeaglass().get(),    color, "waves_seaglass",    dir);
    }

    private void seaglassCubeAll(Block block, String color, String type, String dir) {
        String texture = modLoc("block/color_set/" + color + "/" + type).toString();
        ModelFile model = models().withExistingParent(dir + blockPath(block), mcLoc("block/cube_all"))
                .texture("all", texture)
                .renderType(mcLoc("translucent"));
        simpleBlock(block, model);
        itemModels().getBuilder(blockPath(block))
                .parent(new net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile(modLoc(dir + blockPath(block))))
                .renderType("minecraft:translucent");
    }

    private void colorSetCubeAll(Block block, String color, String type, String dir) {
        colorSetCubeAll(block, color, type, "solid", dir);
    }

    private void colorSetCubeAll(Block block, String color, String type, String renderType, String dir) {
        String texture = modLoc("block/color_set/" + color + "/" + type).toString();
        simpleBlock(block, models().withExistingParent(dir + blockPath(block), mcLoc("block/cube_all"))
                .texture("all", texture)
                .renderType(mcLoc(renderType)));
    }

    private void colorSetCarpet(Block block, String color, String dir) {
        ResourceLocation texture = modLoc("block/color_set/" + color + "/" + "wool");
        simpleBlock(block, models().withExistingParent(dir + blockPath(block), mcLoc("block/carpet"))
                .texture("wool", texture.toString()));
    }

    private void colorSetPaneBlock(IronBarsBlock block, String color, String dir) {
        String baseName = dir + blockPath(block);
        String side = modLoc("block/color_set/" + color + "/stained_glass").toString();
        String edge = modLoc("block/color_set/" + color + "/stained_glass_pane_top").toString();
        
        ModelFile post = colorSetPaneModel(baseName + "_post", side, edge, true, false, false);
        ModelFile sideModel = colorSetPaneModel(baseName + "_side", side, edge, false, true, false);
        ModelFile sideAlt = colorSetPaneModel(baseName + "_side_alt", side, edge, false, false, true);

        getMultipartBuilder(block)
                .part().modelFile(post).addModel().end()
                .part().modelFile(sideModel).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(sideAlt).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(sideModel).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(sideAlt).rotationY(180).addModel().condition(BlockStateProperties.WEST, true).end();
    }

    private ModelFile colorSetPaneModel(String name, String side, String edge, boolean post, boolean sideM, boolean sideAlt) {
        var builder = models().withExistingParent(name, mcLoc("block/block"))
                .texture("edge", edge)
                .texture("pane", side)
                .texture("particle", side)
                .renderType(mcLoc("translucent"));

        if (post) {
            builder.element().from(7, 0, 7).to(9, 16, 9)
                    .face(net.minecraft.core.Direction.NORTH).uvs(7, 0, 9, 16).texture("#edge").end()
                    .face(net.minecraft.core.Direction.SOUTH).uvs(7, 0, 9, 16).texture("#edge").end()
                    .face(net.minecraft.core.Direction.WEST).uvs(7, 0, 9, 16).texture("#edge").end()
                    .face(net.minecraft.core.Direction.EAST).uvs(7, 0, 9, 16).texture("#edge").end()
                    .face(net.minecraft.core.Direction.UP).uvs(7, 7, 9, 9).texture("#edge").end()
                    .face(net.minecraft.core.Direction.DOWN).uvs(7, 7, 9, 9).texture("#edge").cullface(net.minecraft.core.Direction.DOWN).end()
                    .end();
        } else if (sideM) { // NORTH
            builder.element().from(7, 0, 0).to(9, 16, 7)
                    .face(net.minecraft.core.Direction.NORTH).uvs(7, 0, 9, 16).texture("#edge").cullface(net.minecraft.core.Direction.NORTH).end()
                    .face(net.minecraft.core.Direction.SOUTH).uvs(7, 0, 9, 16).texture("#edge").end()
                    .face(net.minecraft.core.Direction.WEST).uvs(0, 0, 7, 16).texture("#pane").end()
                    .face(net.minecraft.core.Direction.EAST).uvs(7, 0, 0, 16).texture("#pane").end()
                    .face(net.minecraft.core.Direction.UP).uvs(7, 0, 9, 7).texture("#edge").end()
                    .face(net.minecraft.core.Direction.DOWN).uvs(7, 0, 9, 7).texture("#edge").cullface(net.minecraft.core.Direction.DOWN).end()
                    .end();
        } else if (sideAlt) { // EAST
            builder.element().from(9, 0, 7).to(16, 16, 9)
                    .face(net.minecraft.core.Direction.NORTH).uvs(9, 0, 16, 16).texture("#pane").end()
                    .face(net.minecraft.core.Direction.SOUTH).uvs(16, 0, 9, 16).texture("#pane").end()
                    .face(net.minecraft.core.Direction.WEST).uvs(7, 0, 9, 16).texture("#edge").end()
                    .face(net.minecraft.core.Direction.EAST).uvs(7, 0, 9, 16).texture("#edge").cullface(net.minecraft.core.Direction.EAST).end()
                    .face(net.minecraft.core.Direction.UP).uvs(9, 7, 16, 9).texture("#edge").end()
                    .face(net.minecraft.core.Direction.DOWN).uvs(9, 7, 16, 9).texture("#edge").cullface(net.minecraft.core.Direction.DOWN).end()
                    .end();
        }

        return builder;
    }

    private void registerBed(Block bed, String color, String dir) {
        ResourceLocation woolTex = modLoc("block/color_set/" + color + "/wool");
        // For the dummy block models used for particles/items, we use the tinted wool
        ModelFile head = models().withExistingParent(dir + color + "_bed_head", mcLoc("block/cube_all"))
                .texture("all", woolTex);
        ModelFile foot = models().withExistingParent(dir + color + "_bed_foot", mcLoc("block/cube_all"))
                .texture("all", woolTex);

        getVariantBuilder(bed).forAllStates(state -> {
            BedPart part = state.getValue(BedBlock.PART);
            net.minecraft.core.Direction facing = state.getValue(BedBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(part == BedPart.HEAD ? head : foot)
                    .rotationY((int) facing.toYRot())
                    .build();
        });
    }

    private void registerCandle(Block candle, String color, String dir) {
        String candleTex = modLoc("block/color_set/" + color + "/candle").toString();

        ModelFile one = models().withExistingParent(dir + color + "_candle_one_candle", mcLoc("block/template_candle"))
                .texture("all", candleTex);
        ModelFile oneLit = models().withExistingParent(dir + color + "_candle_one_candle_lit", mcLoc("block/template_candle"))
                .texture("all", candleTex);
        ModelFile two = models().withExistingParent(dir + color + "_candle_two_candles", mcLoc("block/template_two_candles"))
                .texture("all", candleTex);
        ModelFile twoLit = models().withExistingParent(dir + color + "_candle_two_candles_lit", mcLoc("block/template_two_candles"))
                .texture("all", candleTex);
        ModelFile three = models().withExistingParent(dir + color + "_candle_three_candles", mcLoc("block/template_three_candles"))
                .texture("all", candleTex);
        ModelFile threeLit = models().withExistingParent(dir + color + "_candle_three_candles_lit", mcLoc("block/template_three_candles"))
                .texture("all", candleTex);
        ModelFile four = models().withExistingParent(dir + color + "_candle_four_candles", mcLoc("block/template_four_candles"))
                .texture("all", candleTex);
        ModelFile fourLit = models().withExistingParent(dir + color + "_candle_four_candles_lit", mcLoc("block/template_four_candles"))
                .texture("all", candleTex);

        getVariantBuilder(candle).forAllStates(state -> {
            int candles = state.getValue(BlockStateProperties.CANDLES);
            boolean lit = state.getValue(BlockStateProperties.LIT);
            ModelFile model = switch (candles) {
                case 1 -> lit ? oneLit : one;
                case 2 -> lit ? twoLit : two;
                case 3 -> lit ? threeLit : three;
                case 4 -> lit ? fourLit : four;
                default -> one;
            };
            return ConfiguredModel.builder().modelFile(model).build();
        });
    }

    private void registerBanner(Block banner, Block wallBanner, String color, String dir) {
        ModelFile model = models().getBuilder(dir + color + "_banner").parent(new ModelFile.UncheckedModelFile("builtin/entity"));

        getVariantBuilder(banner).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
        getVariantBuilder(wallBanner).forAllStates(state -> ConfiguredModel.builder().modelFile(model).build());
    }

    public void paneBlockWithRenderType(net.minecraft.world.level.block.@NotNull IronBarsBlock block, @NotNull ResourceLocation side, @NotNull ResourceLocation edge, @NotNull String renderType) {
        paneBlock(block, side, edge);
        // The above helper doesn't let us set render type easily on all generated models, but we can try to find them
        String baseName = blockPath(block);
        models().getBuilder(baseName + "_post").renderType(renderType);
        models().getBuilder(baseName + "_side").renderType(renderType);
        models().getBuilder(baseName + "_side_alt").renderType(renderType);
        models().getBuilder(baseName + "_noside").renderType(renderType);
        models().getBuilder(baseName + "_noside_alt").renderType(renderType);
    }

    private void registerWoodSet(String setName, ModBlocks.WoodSetBlocks set) {
        String dir = "block/" + setName + "/";
        ResourceLocation planksTex = modLoc("block/wood/" + setName + "/planks");
        ResourceLocation logSide = modLoc("block/wood/" + setName + "/log");
        ResourceLocation logTop = modLoc("block/wood/" + setName + "/log_top");
        ResourceLocation strippedLogSide = modLoc("block/wood/" + setName + "/stripped_log");
        ResourceLocation strippedLogTop = modLoc("block/wood/" + setName + "/stripped_log_top");

        // ── Axis blocks ───────────────────────────────────────────────────────
        axisBlock(set.log().get(),
                models().cubeColumn(dir + blockPath(set.log().get()), logSide, logTop),
                models().cubeColumnHorizontal(dir + blockPath(set.log().get()) + "_horizontal", logSide, logTop));
        axisBlock(set.wood().get(),
                models().cubeColumn(dir + blockPath(set.wood().get()), logSide, logSide),
                models().cubeColumnHorizontal(dir + blockPath(set.wood().get()) + "_horizontal", logSide, logSide));
        axisBlock(set.strippedLog().get(),
                models().cubeColumn(dir + blockPath(set.strippedLog().get()), strippedLogSide, strippedLogTop),
                models().cubeColumnHorizontal(dir + blockPath(set.strippedLog().get()) + "_horizontal", strippedLogSide, strippedLogTop));
        axisBlock(set.strippedWood().get(),
                models().cubeColumn(dir + blockPath(set.strippedWood().get()), strippedLogSide, strippedLogSide),
                models().cubeColumnHorizontal(dir + blockPath(set.strippedWood().get()) + "_horizontal", strippedLogSide, strippedLogSide));

        // ── Planks ───────────────────────────────────────────────────────────
        String planksModelName = setName + "_planks";
        ModelFile planksModel = models().cubeAll(dir + planksModelName, planksTex);
        simpleBlock(set.planks().get(), planksModel);

        // ── Stairs ───────────────────────────────────────────────────────────
        String stairsName = blockPath(set.stairs().get());
        stairsBlock(set.stairs().get(),
                models().stairs(dir + stairsName, planksTex, planksTex, planksTex),
                models().stairsOuter(dir + stairsName + "_outer", planksTex, planksTex, planksTex),
                models().stairsInner(dir + stairsName + "_inner", planksTex, planksTex, planksTex));

        // ── Slab ─────────────────────────────────────────────────────────────
        registerPlanksSlab(setName, set.slab().get(), planksTex, modLoc(dir + planksModelName), dir);

        // ── Fence ────────────────────────────────────────────────────────────
        String fenceName = blockPath(set.fence().get());
        fourWayBlock(set.fence().get(),
                models().fencePost(dir + fenceName + "_post", planksTex),
                models().fenceSide(dir + fenceName + "_side", planksTex));
        models().fenceInventory(dir + fenceName + "_inventory", planksTex);

        // ── Fence Gate ───────────────────────────────────────────────────────
        String gateName = blockPath(set.fenceGate().get());
        fenceGateBlock(set.fenceGate().get(),
                models().fenceGate(dir + gateName, planksTex),
                models().fenceGateOpen(dir + gateName + "_open", planksTex),
                models().fenceGateWall(dir + gateName + "_wall", planksTex),
                models().fenceGateWallOpen(dir + gateName + "_wall_open", planksTex));

        // ── Pressure Plate ───────────────────────────────────────────────────
        String plateName = blockPath(set.pressurePlate().get());
        pressurePlateBlock(set.pressurePlate().get(),
                models().pressurePlate(dir + plateName, planksTex),
                models().pressurePlateDown(dir + plateName + "_down", planksTex));

        // ── Button ───────────────────────────────────────────────────────────
        String buttonName = blockPath(set.button().get());
        buttonBlock(set.button().get(),
                models().button(dir + buttonName, planksTex),
                models().buttonPressed(dir + buttonName + "_pressed", planksTex));
        models().withExistingParent(dir + buttonName + "_inventory", mcLoc("block/button_inventory"))
                .texture("texture", planksTex);

        // ── Door / Trapdoor ───────────────────────────────────────────────────
        registerCutoutDoor(set.door().get(), modLoc("block/wood/" + setName + "/door_bottom"), modLoc("block/wood/" + setName + "/door_top"), dir);
        registerCutoutTrapdoor(set.trapdoor().get(), modLoc("block/wood/" + setName + "/trapdoor"), dir);

        // ── Sign / Hanging Sign ───────────────────────────────────────────────
        ModelFile signModel = models().sign(dir + blockPath(set.sign().get()), planksTex);
        simpleBlock(set.sign().get(), signModel);
        simpleBlock(set.wallSign().get(), signModel);

        ModelFile hangingSignModel = models().sign(dir + blockPath(set.hangingSign().get()), planksTex);
        simpleBlock(set.hangingSign().get(), hangingSignModel);
        simpleBlock(set.wallHangingSign().get(), hangingSignModel);

        // ── Leaves ───────────────────────────────────────────────────────────
        registerFluffyLeaves(setName, set.leaves().get(), dir);

        registerSapling(set.sapling().get(), set.pottedSapling().get(), setName);

        registerWoodSetStructural(setName, set);
    }

    private void registerWoodSetStructural(String setName, ModBlocks.WoodSetBlocks set) {
        String dir = "block/" + setName + "/";
        ResourceLocation planks  = modLoc("block/wood/" + setName + "/planks");
        ResourceLocation stripped = modLoc("block/wood/" + setName + "/stripped_log");
        ResourceLocation beamTex    = modLoc("block/beam/" + setName + "_beam");
        ResourceLocation pergolaTex = modLoc("block/pergola/" + setName + "_pergola");

        // ── Beam ─────────────────────────────────────────────────────────────
        ModelFile beamY = models().withExistingParent(dir + setName + "_beam_y",      modLoc("block/oak/oak_beam_y"))
                .renderType("minecraft:cutout").texture("1", beamTex).texture("particle", beamTex);
        ModelFile beamX = models().withExistingParent(dir + setName + "_beam_x",      modLoc("block/oak/oak_beam_x"))
                .renderType("minecraft:cutout").texture("1", beamTex).texture("particle", beamTex);
        ModelFile beamXZ = models().withExistingParent(dir + setName + "_beam_x_z",   modLoc("block/oak/oak_beam_x_z"))
                .renderType("minecraft:cutout").texture("1", beamTex).texture("particle", beamTex);
        ModelFile beamBot = models().withExistingParent(dir + setName + "_beam_bottom", modLoc("block/oak/oak_beam_bottom"))
                .renderType("minecraft:cutout").texture("texture", beamTex).texture("particle", beamTex);

        getMultipartBuilder(set.beam().get())
                .part().modelFile(beamY).addModel()  .condition(BeamBlock.AXIS_Y, true).end()
                .part().modelFile(beamX).addModel()  .condition(BeamBlock.AXIS_X, true).condition(BeamBlock.AXIS_Z, false).end()
                .part().modelFile(beamX).rotationY(90).addModel().condition(BeamBlock.AXIS_X, false).condition(BeamBlock.AXIS_Z, true).end()
                .part().modelFile(beamXZ).addModel() .condition(BeamBlock.AXIS_X, true).condition(BeamBlock.AXIS_Z, true).end()
                .part().modelFile(beamBot).addModel().condition(BeamBlock.BOTTOM, true).end();

        // ── Pergola ───────────────────────────────────────────────────────────
        ModelFile pergolaY  = models().withExistingParent(dir + setName + "_pergola_y",   modLoc("block/oak/oak_pergola_y"))
                .renderType("minecraft:cutout").texture("0", pergolaTex).texture("particle", pergolaTex);
        ModelFile pergolaX  = models().withExistingParent(dir + setName + "_pergola_x",   modLoc("block/oak/oak_pergola_x"))
                .renderType("minecraft:cutout").texture("0", pergolaTex).texture("particle", pergolaTex);
        ModelFile pergolaXZ = models().withExistingParent(dir + setName + "_pergola_x_z", modLoc("block/oak/oak_pergola_x_z"))
                .renderType("minecraft:cutout").texture("0", pergolaTex).texture("particle", pergolaTex);

        getMultipartBuilder(set.pergola().get())
                .part().modelFile(pergolaY).addModel() .condition(PergolaBlock.AXIS_Y, true).end()
                .part().modelFile(pergolaX).addModel() .condition(PergolaBlock.AXIS_X, true).condition(PergolaBlock.AXIS_Z, false).end()
                .part().modelFile(pergolaX).rotationY(90).addModel().condition(PergolaBlock.AXIS_X, false).condition(PergolaBlock.AXIS_Z, true).end()
                .part().modelFile(pergolaXZ).addModel().condition(PergolaBlock.AXIS_X, true).condition(PergolaBlock.AXIS_Z, true).end();
        itemModels().withExistingParent(setName + "_beam", modLoc("item/template_beam"))
                .texture("0", beamTex).texture("particle", beamTex);
        itemModels().withExistingParent(setName + "_pergola", modLoc("item/template_pergola"))
                .texture("0", pergolaTex).texture("particle", pergolaTex);

        // ── Support shared models ─────────────────────────────────────────────
        ModelFile sup4   = models().withExistingParent(dir + setName + "_support_4_pixels",  modLoc("block/oak/oak_support_4_pixels"))
                .texture("slab", planks).texture("particle", planks);
        ModelFile sup6   = models().withExistingParent(dir + setName + "_support_6_pixels",  modLoc("block/oak/oak_support_6_pixels"))
                .texture("slab", planks).texture("particle", planks);
        ModelFile sup8   = models().withExistingParent(dir + setName + "_support_8_pixels",  modLoc("block/oak/oak_support_8_pixels"))
                .texture("slab", beamTex).texture("particle", beamTex);
        ModelFile sup10  = models().withExistingParent(dir + setName + "_support_10_pixels", modLoc("block/oak/oak_support_10_pixels"))
                .texture("slab", beamTex).texture("particle", beamTex);
        ModelFile supSlab = models().withExistingParent(dir + setName + "_support_slab",     modLoc("block/oak/oak_support_slab"))
                .texture("slab", planks).texture("particle", planks);

        // ── Support Slab ──────────────────────────────────────────────────────
        getMultipartBuilder(set.supportSlab().get())
                .part().modelFile(supSlab).addModel().end()
                .part().modelFile(sup4).addModel() .condition(SupportSlabBlock.PILLAR_CONNECTION, PillarConnection.FOUR).end()
                .part().modelFile(sup6).addModel() .condition(SupportSlabBlock.PILLAR_CONNECTION, PillarConnection.SIX).end()
                .part().modelFile(sup8).addModel() .condition(SupportSlabBlock.PILLAR_CONNECTION, PillarConnection.EIGHT).end()
                .part().modelFile(sup10).addModel().condition(SupportSlabBlock.PILLAR_CONNECTION, PillarConnection.TEN).end();

        // ── Support Beam ──────────────────────────────────────────────────────
        getMultipartBuilder(set.supportBeam().get())
                .part().modelFile(supSlab).addModel().end()
                .part().modelFile(beamX).addModel()         .condition(SupportBeamBlock.HORIZONTAL_AXIS, Direction.Axis.X).condition(SupportBeamBlock.SUBAXIS, false).end()
                .part().modelFile(beamX).rotationY(90).addModel().condition(SupportBeamBlock.HORIZONTAL_AXIS, Direction.Axis.Z).condition(SupportBeamBlock.SUBAXIS, false).end()
                .part().modelFile(beamXZ).addModel()        .condition(SupportBeamBlock.HORIZONTAL_AXIS, Direction.Axis.X).condition(SupportBeamBlock.SUBAXIS, true).end()
                .part().modelFile(beamXZ).rotationY(90).addModel().condition(SupportBeamBlock.HORIZONTAL_AXIS, Direction.Axis.Z).condition(SupportBeamBlock.SUBAXIS, true).end()
                .part().modelFile(sup4).addModel() .condition(SupportBeamBlock.PILLAR_CONNECTION, PillarConnection.FOUR).end()
                .part().modelFile(sup6).addModel() .condition(SupportBeamBlock.PILLAR_CONNECTION, PillarConnection.SIX).end()
                .part().modelFile(sup8).addModel() .condition(SupportBeamBlock.PILLAR_CONNECTION, PillarConnection.EIGHT).end()
                .part().modelFile(sup10).addModel().condition(SupportBeamBlock.PILLAR_CONNECTION, PillarConnection.TEN).end();

        // ── Planks Plate ──────────────────────────────────────────────────────
        ModelFile plate      = models().withExistingParent(dir + setName + "_planks_plate",       modLoc("block/plate"))
                .texture("side", planks).texture("top", planks).texture("frieze", planks);
        ModelFile plateOuter = models().withExistingParent(dir + setName + "_planks_plate_outer", modLoc("block/plate_outer"))
                .texture("top", planks).texture("frieze", planks);
        ModelFile plateInner = models().withExistingParent(dir + setName + "_planks_plate_inner", modLoc("block/plate_inner"))
                .texture("side", planks).texture("top", planks).texture("frieze", planks);
        registerFacingShapeBlock(set.planksPlate().get(), plate, plateOuter, plateInner);

        // ── Planks Edge ───────────────────────────────────────────────────────
        ModelFile edge      = models().withExistingParent(dir + setName + "_planks_edge",       modLoc("block/small_plate"))
                .texture("side", planks).texture("frieze", planks);
        ModelFile edgeOuter = models().withExistingParent(dir + setName + "_planks_edge_outer", modLoc("block/small_plate_outer"))
                .texture("top", planks).texture("frieze", planks);
        ModelFile edgeInner = models().withExistingParent(dir + setName + "_planks_edge_inner", modLoc("block/small_plate_inner"))
                .texture("side", planks).texture("top", planks).texture("frieze", planks);
        registerFacingShapeHalfBlock(set.planksEdge().get(), edge, edgeOuter, edgeInner);

        // ── Bannister ──────────────────────────────────────────────────────────
        ModelFile bannister      = models().withExistingParent(dir + setName + "_bannister",       modLoc("block/oak/oak_bannister"))
                .texture("0", planks).texture("particle", planks);
        ModelFile bannisterOuter = models().withExistingParent(dir + setName + "_bannister_outer", modLoc("block/oak/oak_bannister_outer"))
                .texture("0", planks).texture("particle", planks);
        ModelFile bannisterInner = models().withExistingParent(dir + setName + "_bannister_inner", modLoc("block/oak/oak_bannister_inner"))
                .texture("0", planks).texture("particle", planks);
        registerFacingShapeBlock(set.bannister().get(), bannister, bannisterOuter, bannisterInner);

        // ── Geometric Window ──────────────────────────────────────────────────
        ModelFile wsWindow = models().withExistingParent(dir + setName + "_geometric_window", modLoc("block/geometric_window"))
                .texture("texture", planks).texture("particle", planks)
                .renderType("minecraft:cutout");
        horizontalBlock(set.geometricWindow().get(), wsWindow);
        itemModels().withExistingParent(setName + "_geometric_window", wsWindow.getLocation());
    }

    private ResourceLocation vanillaPlanks(String setName) {
        if (setName.equals("bamboo")) {
            return ResourceLocation.fromNamespaceAndPath("minecraft", "block/bamboo_planks");
        }
        return ResourceLocation.fromNamespaceAndPath("minecraft", "block/" + setName + "_planks");
    }

    private ResourceLocation vanillaStripped(String setName) {
        return switch (setName) {
            case "bamboo" -> ResourceLocation.fromNamespaceAndPath("minecraft", "block/stripped_bamboo_block");
            case "crimson" -> ResourceLocation.fromNamespaceAndPath("minecraft", "block/stripped_crimson_stem");
            case "warped" -> ResourceLocation.fromNamespaceAndPath("minecraft", "block/stripped_warped_stem");
            default -> ResourceLocation.fromNamespaceAndPath("minecraft", "block/stripped_" + setName + "_log");
        };
    }

    private void registerVanillaWoodStructural(String setName, ModBlocks.WoodStructuralBlocks set) {
        String dir = "block/" + setName + "/";
        ResourceLocation planks  = vanillaPlanks(setName);
        ResourceLocation stripped = vanillaStripped(setName);

        // ── Beam ─────────────────────────────────────────────────────────────
        ModelFile beamY = models().withExistingParent(dir + setName + "_beam_y",      modLoc("block/oak/oak_beam_y"))
                .renderType("minecraft:cutout").texture("1", stripped).texture("particle", stripped);
        ModelFile beamX = models().withExistingParent(dir + setName + "_beam_x",      modLoc("block/oak/oak_beam_x"))
                .renderType("minecraft:cutout").texture("1", stripped).texture("particle", stripped);
        ModelFile beamXZ = models().withExistingParent(dir + setName + "_beam_x_z",   modLoc("block/oak/oak_beam_x_z"))
                .renderType("minecraft:cutout").texture("1", stripped).texture("particle", stripped);
        ModelFile beamBot = models().withExistingParent(dir + setName + "_beam_bottom", modLoc("block/oak/oak_beam_bottom"))
                .renderType("minecraft:cutout").texture("texture", stripped).texture("particle", stripped);

        getMultipartBuilder(set.beam().get())
                .part().modelFile(beamY).addModel()  .condition(BeamBlock.AXIS_Y, true).end()
                .part().modelFile(beamX).addModel()  .condition(BeamBlock.AXIS_X, true).condition(BeamBlock.AXIS_Z, false).end()
                .part().modelFile(beamX).rotationY(90).addModel().condition(BeamBlock.AXIS_X, false).condition(BeamBlock.AXIS_Z, true).end()
                .part().modelFile(beamXZ).addModel() .condition(BeamBlock.AXIS_X, true).condition(BeamBlock.AXIS_Z, true).end()
                .part().modelFile(beamBot).addModel().condition(BeamBlock.BOTTOM, true).end();

        // ── Pergola ───────────────────────────────────────────────────────────
        ModelFile pergolaY  = models().withExistingParent(dir + setName + "_pergola_y",   modLoc("block/oak/oak_pergola_y"))
                .renderType("minecraft:cutout").texture("0", planks).texture("particle", planks);
        ModelFile pergolaX  = models().withExistingParent(dir + setName + "_pergola_x",   modLoc("block/oak/oak_pergola_x"))
                .renderType("minecraft:cutout").texture("0", planks).texture("particle", planks);
        ModelFile pergolaXZ = models().withExistingParent(dir + setName + "_pergola_x_z", modLoc("block/oak/oak_pergola_x_z"))
                .renderType("minecraft:cutout").texture("0", planks).texture("particle", planks);

        getMultipartBuilder(set.pergola().get())
                .part().modelFile(pergolaY).addModel() .condition(PergolaBlock.AXIS_Y, true).end()
                .part().modelFile(pergolaX).addModel() .condition(PergolaBlock.AXIS_X, true).condition(PergolaBlock.AXIS_Z, false).end()
                .part().modelFile(pergolaX).rotationY(90).addModel().condition(PergolaBlock.AXIS_X, false).condition(PergolaBlock.AXIS_Z, true).end()
                .part().modelFile(pergolaXZ).addModel().condition(PergolaBlock.AXIS_X, true).condition(PergolaBlock.AXIS_Z, true).end();
        itemModels().withExistingParent(setName + "_beam", modLoc("item/template_beam"))
                .texture("0", stripped).texture("particle", stripped);
        itemModels().withExistingParent(setName + "_pergola", modLoc("item/template_pergola"))
                .texture("0", planks).texture("particle", planks);

        // ── Support shared models ─────────────────────────────────────────────
        ModelFile sup4   = models().withExistingParent(dir + setName + "_support_4_pixels",  modLoc("block/oak/oak_support_4_pixels"))
                .texture("slab", planks).texture("particle", planks);
        ModelFile sup6   = models().withExistingParent(dir + setName + "_support_6_pixels",  modLoc("block/oak/oak_support_6_pixels"))
                .texture("slab", planks).texture("particle", planks);
        ModelFile sup8   = models().withExistingParent(dir + setName + "_support_8_pixels",  modLoc("block/oak/oak_support_8_pixels"))
                .texture("slab", stripped).texture("particle", stripped);
        ModelFile sup10  = models().withExistingParent(dir + setName + "_support_10_pixels", modLoc("block/oak/oak_support_10_pixels"))
                .texture("slab", stripped).texture("particle", stripped);
        ModelFile supSlab = models().withExistingParent(dir + setName + "_support_slab",     modLoc("block/oak/oak_support_slab"))
                .texture("slab", planks).texture("particle", planks);

        // ── Support Slab ──────────────────────────────────────────────────────
        getMultipartBuilder(set.supportSlab().get())
                .part().modelFile(supSlab).addModel().end()
                .part().modelFile(sup4).addModel() .condition(SupportSlabBlock.PILLAR_CONNECTION, PillarConnection.FOUR).end()
                .part().modelFile(sup6).addModel() .condition(SupportSlabBlock.PILLAR_CONNECTION, PillarConnection.SIX).end()
                .part().modelFile(sup8).addModel() .condition(SupportSlabBlock.PILLAR_CONNECTION, PillarConnection.EIGHT).end()
                .part().modelFile(sup10).addModel().condition(SupportSlabBlock.PILLAR_CONNECTION, PillarConnection.TEN).end();

        // ── Support Beam ──────────────────────────────────────────────────────
        getMultipartBuilder(set.supportBeam().get())
                .part().modelFile(supSlab).addModel().end()
                .part().modelFile(beamX).addModel()         .condition(SupportBeamBlock.HORIZONTAL_AXIS, Direction.Axis.X).condition(SupportBeamBlock.SUBAXIS, false).end()
                .part().modelFile(beamX).rotationY(90).addModel().condition(SupportBeamBlock.HORIZONTAL_AXIS, Direction.Axis.Z).condition(SupportBeamBlock.SUBAXIS, false).end()
                .part().modelFile(beamXZ).addModel()        .condition(SupportBeamBlock.HORIZONTAL_AXIS, Direction.Axis.X).condition(SupportBeamBlock.SUBAXIS, true).end()
                .part().modelFile(beamXZ).rotationY(90).addModel().condition(SupportBeamBlock.HORIZONTAL_AXIS, Direction.Axis.Z).condition(SupportBeamBlock.SUBAXIS, true).end()
                .part().modelFile(sup4).addModel() .condition(SupportBeamBlock.PILLAR_CONNECTION, PillarConnection.FOUR).end()
                .part().modelFile(sup6).addModel() .condition(SupportBeamBlock.PILLAR_CONNECTION, PillarConnection.SIX).end()
                .part().modelFile(sup8).addModel() .condition(SupportBeamBlock.PILLAR_CONNECTION, PillarConnection.EIGHT).end()
                .part().modelFile(sup10).addModel().condition(SupportBeamBlock.PILLAR_CONNECTION, PillarConnection.TEN).end();

        // ── Planks Plate ──────────────────────────────────────────────────────
        ModelFile plate      = models().withExistingParent(dir + setName + "_planks_plate",       modLoc("block/plate"))
                .texture("side", planks).texture("top", planks).texture("frieze", planks);
        ModelFile plateOuter = models().withExistingParent(dir + setName + "_planks_plate_outer", modLoc("block/plate_outer"))
                .texture("top", planks).texture("frieze", planks);
        ModelFile plateInner = models().withExistingParent(dir + setName + "_planks_plate_inner", modLoc("block/plate_inner"))
                .texture("side", planks).texture("top", planks).texture("frieze", planks);
        registerFacingShapeBlock(set.planksPlate().get(), plate, plateOuter, plateInner);

        // ── Planks Edge ───────────────────────────────────────────────────────
        ModelFile edge      = models().withExistingParent(dir + setName + "_planks_edge",       modLoc("block/small_plate"))
                .texture("side", planks).texture("frieze", planks);
        ModelFile edgeOuter = models().withExistingParent(dir + setName + "_planks_edge_outer", modLoc("block/small_plate_outer"))
                .texture("top", planks).texture("frieze", planks);
        ModelFile edgeInner = models().withExistingParent(dir + setName + "_planks_edge_inner", modLoc("block/small_plate_inner"))
                .texture("side", planks).texture("top", planks).texture("frieze", planks);
        registerFacingShapeHalfBlock(set.planksEdge().get(), edge, edgeOuter, edgeInner);

        // ── Bannister ──────────────────────────────────────────────────────────
        ModelFile bannister      = models().withExistingParent(dir + setName + "_bannister",       modLoc("block/oak/oak_bannister"))
                .texture("0", planks).texture("particle", planks);
        ModelFile bannisterOuter = models().withExistingParent(dir + setName + "_bannister_outer", modLoc("block/oak/oak_bannister_outer"))
                .texture("0", planks).texture("particle", planks);
        ModelFile bannisterInner = models().withExistingParent(dir + setName + "_bannister_inner", modLoc("block/oak/oak_bannister_inner"))
                .texture("0", planks).texture("particle", planks);
        registerFacingShapeBlock(set.bannister().get(), bannister, bannisterOuter, bannisterInner);

        // ── Geometric Window ──────────────────────────────────────────────────
        ModelFile vWindow = models().withExistingParent(dir + setName + "_geometric_window", modLoc("block/geometric_window"))
                .texture("texture", planks).texture("particle", planks)
                .renderType("minecraft:cutout");
        horizontalBlock(set.geometricWindow().get(), vWindow);
        itemModels().withExistingParent(setName + "_geometric_window", vWindow.getLocation());
    }

    /** Variant block with HORIZONTAL_FACING × STAIRS_SHAPE (no half). Used for plate and bannister. */
    private void registerFacingShapeBlock(net.minecraft.world.level.block.Block block,
                                          ModelFile straight, ModelFile outer, ModelFile inner) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            StairsShape shape = state.getValue(BlockStateProperties.STAIRS_SHAPE);

            int yRot = switch (facing) {
                case SOUTH -> 0;
                case WEST  -> 90;
                case NORTH -> 180;
                case EAST  -> 270;
                default    -> 0;
            };
            boolean isLeft = shape == StairsShape.OUTER_LEFT || shape == StairsShape.INNER_LEFT;
            if (isLeft) yRot = (yRot + 270) % 360;

            ModelFile model = switch (shape) {
                case STRAIGHT                     -> straight;
                case OUTER_LEFT, OUTER_RIGHT      -> outer;
                case INNER_LEFT, INNER_RIGHT      -> inner;
            };
            return ConfiguredModel.builder().modelFile(model).rotationY(yRot).uvLock(true).build();
        }, BlockStateProperties.WATERLOGGED);
    }

    /** Variant block with HORIZONTAL_FACING × STAIRS_SHAPE × HALF. Used for edge. */
    private void registerFacingShapeHalfBlock(net.minecraft.world.level.block.Block block,
                                              ModelFile straight, ModelFile outer, ModelFile inner) {
        getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            StairsShape shape = state.getValue(BlockStateProperties.STAIRS_SHAPE);
            Half half = state.getValue(BlockStateProperties.HALF);

            int baseY = switch (facing) {
                case SOUTH -> 0;
                case WEST  -> 90;
                case NORTH -> 180;
                case EAST  -> 270;
                default    -> 0;
            };

            int xRot;
            int yRot;
            if (half == Half.BOTTOM) {
                xRot = 0;
                boolean isLeft = shape == StairsShape.OUTER_LEFT || shape == StairsShape.INNER_LEFT;
                yRot = isLeft ? (baseY + 270) % 360 : baseY;
            } else {
                xRot = 180;
                int topBase = (baseY + 180) % 360;
                boolean isRight = shape == StairsShape.OUTER_RIGHT || shape == StairsShape.INNER_RIGHT;
                yRot = isRight ? (topBase + 90) % 360 : topBase;
            }

            ModelFile model = switch (shape) {
                case STRAIGHT                     -> straight;
                case OUTER_LEFT, OUTER_RIGHT      -> outer;
                case INNER_LEFT, INNER_RIGHT      -> inner;
            };
            return ConfiguredModel.builder().modelFile(model).rotationX(xRot).rotationY(yRot).uvLock(true).build();
        }, BlockStateProperties.WATERLOGGED);
    }

    private void registerPlanksSlab(String setName, SlabBlock slab, ResourceLocation planksTex, ResourceLocation doubleModelLoc, String dir) {
        String slabName = blockPath(slab);
        ModelFile slabModel = models().withExistingParent(dir + slabName, mcLoc("block/slab")).texture("bottom", planksTex).texture("top", planksTex).texture("side", planksTex);
        ModelFile slabTopModel = models().withExistingParent(dir + slabName + "_top", mcLoc("block/slab_top")).texture("bottom", planksTex).texture("top", planksTex).texture("side", planksTex);
        ModelFile doubleModel = new ModelFile.UncheckedModelFile(doubleModelLoc);

        getVariantBuilder(slab)
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.BOTTOM).addModels(new ConfiguredModel(slabModel))
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.TOP).addModels(new ConfiguredModel(slabTopModel))
                .partialState().with(BlockStateProperties.SLAB_TYPE, SlabType.DOUBLE).addModels(new ConfiguredModel(doubleModel));
    }

    private void registerFluffyLeaves(String setName, Block leavesBlock, String dir) {
        String leavesId = blockPath(leavesBlock);
        ResourceLocation leavesTexture = modLoc("block/wood/" + setName + "/leaves");

        ModelFile l0 = models().withExistingParent(dir + leavesId, modLoc("block/leaves/leaves")).texture("all", leavesTexture);
        ModelFile l1 = models().withExistingParent(dir + leavesId + "1", modLoc("block/leaves/leaves1")).texture("all", leavesTexture);
        ModelFile l2 = models().withExistingParent(dir + leavesId + "2", modLoc("block/leaves/leaves2")).texture("all", leavesTexture);
        ModelFile l3 = models().withExistingParent(dir + leavesId + "3", modLoc("block/leaves/leaves3")).texture("all", leavesTexture);
        ModelFile l4 = models().withExistingParent(dir + leavesId + "4", modLoc("block/leaves/leaves4")).texture("all", leavesTexture);

        getVariantBuilder(leavesBlock).partialState().addModels(
                new ConfiguredModel(l0, 0, 0, false, 1),
                new ConfiguredModel(l1, 0, 0, false, 1),
                new ConfiguredModel(l2, 0, 0, false, 1),
                new ConfiguredModel(l3, 0, 0, false, 1),
                new ConfiguredModel(l4, 0, 0, false, 1)
        );
    }

    private void registerPatternShapedBlocks() {
        ModBlocks.PATTERN_PLATES.forEach((pattern, colorMap) -> {
            ResourceLocation patTex = modLoc("block/patterns/" + pattern);
            colorMap.forEach((colorName, block) -> {
                String colorDir = "block/" + colorName + "/";
                String base = colorDir + colorName + "_" + pattern;
                ModelFile plate      = models().withExistingParent(base + "_plate",       modLoc("block/plate"))
                        .texture("side", patTex).texture("top", patTex).texture("frieze", patTex);
                ModelFile plateOuter = models().withExistingParent(base + "_plate_outer", modLoc("block/plate_outer"))
                        .texture("top", patTex).texture("frieze", patTex);
                ModelFile plateInner = models().withExistingParent(base + "_plate_inner", modLoc("block/plate_inner"))
                        .texture("side", patTex).texture("top", patTex).texture("frieze", patTex);
                registerFacingShapeBlock(block.get(), plate, plateOuter, plateInner);
                itemModels().withExistingParent(colorName + "_" + pattern + "_plate", plate.getLocation());
            });
        });

        ModBlocks.PATTERN_EDGES.forEach((pattern, colorMap) -> {
            ResourceLocation patTex = modLoc("block/patterns/" + pattern);
            colorMap.forEach((colorName, block) -> {
                String colorDir = "block/" + colorName + "/";
                String base = colorDir + colorName + "_" + pattern;
                ModelFile edge      = models().withExistingParent(base + "_edge",       modLoc("block/small_plate"))
                        .texture("side", patTex).texture("frieze", patTex);
                ModelFile edgeOuter = models().withExistingParent(base + "_edge_outer", modLoc("block/small_plate_outer"))
                        .texture("top", patTex).texture("frieze", patTex);
                ModelFile edgeInner = models().withExistingParent(base + "_edge_inner", modLoc("block/small_plate_inner"))
                        .texture("side", patTex).texture("top", patTex).texture("frieze", patTex);
                registerFacingShapeHalfBlock(block.get(), edge, edgeOuter, edgeInner);
                itemModels().withExistingParent(colorName + "_" + pattern + "_edge", edge.getLocation());
            });
        });

        ModBlocks.PATTERN_BEAMS.forEach((pattern, colorMap) -> {
            ResourceLocation patTex = modLoc("block/patterns/" + pattern);
            colorMap.forEach((colorName, block) -> {
                String colorDir = "block/" + colorName + "/";
                String base = colorDir + colorName + "_" + pattern;
                ModelFile beamY   = models().withExistingParent(base + "_beam_y",     modLoc("block/oak/oak_beam_y"))
                        .renderType("minecraft:cutout").texture("1", patTex).texture("particle", patTex);
                ModelFile beamX   = models().withExistingParent(base + "_beam_x",     modLoc("block/oak/oak_beam_x"))
                        .renderType("minecraft:cutout").texture("1", patTex).texture("particle", patTex);
                ModelFile beamXZ  = models().withExistingParent(base + "_beam_x_z",   modLoc("block/oak/oak_beam_x_z"))
                        .renderType("minecraft:cutout").texture("1", patTex).texture("particle", patTex);
                ModelFile beamBot = models().withExistingParent(base + "_beam_bottom", modLoc("block/oak/oak_beam_bottom"))
                        .renderType("minecraft:cutout").texture("texture", patTex).texture("particle", patTex);
                getMultipartBuilder(block.get())
                        .part().modelFile(beamY).addModel()               .condition(BeamBlock.AXIS_Y, true).end()
                        .part().modelFile(beamX).addModel()               .condition(BeamBlock.AXIS_X, true).condition(BeamBlock.AXIS_Z, false).end()
                        .part().modelFile(beamX).rotationY(90).addModel() .condition(BeamBlock.AXIS_X, false).condition(BeamBlock.AXIS_Z, true).end()
                        .part().modelFile(beamXZ).addModel()              .condition(BeamBlock.AXIS_X, true).condition(BeamBlock.AXIS_Z, true).end()
                        .part().modelFile(beamBot).addModel()             .condition(BeamBlock.BOTTOM, true).end();
                itemModels().withExistingParent(colorName + "_" + pattern + "_beam", modLoc("item/template_beam"))
                        .texture("0", patTex).texture("particle", patTex);
            });
        });

        ModBlocks.PATTERN_PERGOLAS.forEach((pattern, colorMap) -> {
            ResourceLocation patTex = modLoc("block/patterns/" + pattern);
            colorMap.forEach((colorName, block) -> {
                String colorDir = "block/" + colorName + "/";
                String base = colorDir + colorName + "_" + pattern;
                ModelFile pergolaY  = models().withExistingParent(base + "_pergola_y",   modLoc("block/oak/oak_pergola_y"))
                        .renderType("minecraft:cutout").texture("0", patTex).texture("particle", patTex);
                ModelFile pergolaX  = models().withExistingParent(base + "_pergola_x",   modLoc("block/oak/oak_pergola_x"))
                        .renderType("minecraft:cutout").texture("0", patTex).texture("particle", patTex);
                ModelFile pergolaXZ = models().withExistingParent(base + "_pergola_x_z", modLoc("block/oak/oak_pergola_x_z"))
                        .renderType("minecraft:cutout").texture("0", patTex).texture("particle", patTex);
                getMultipartBuilder(block.get())
                        .part().modelFile(pergolaY).addModel()               .condition(PergolaBlock.AXIS_Y, true).end()
                        .part().modelFile(pergolaX).addModel()               .condition(PergolaBlock.AXIS_X, true).condition(PergolaBlock.AXIS_Z, false).end()
                        .part().modelFile(pergolaX).rotationY(90).addModel() .condition(PergolaBlock.AXIS_X, false).condition(PergolaBlock.AXIS_Z, true).end()
                        .part().modelFile(pergolaXZ).addModel()              .condition(PergolaBlock.AXIS_X, true).condition(PergolaBlock.AXIS_Z, true).end();
                itemModels().withExistingParent(colorName + "_" + pattern + "_pergola", modLoc("item/template_pergola"))
                        .texture("0", patTex).texture("particle", patTex);
            });
        });

        registerMiscBlocks();

        ModBlocks.PATTERN_WINDOWS.forEach((pattern, colorMap) -> {
            ResourceLocation patTex = modLoc("block/patterns/" + pattern);
            colorMap.forEach((colorName, block) -> {
                String colorDir = "block/" + colorName + "/";
                String itemName = colorName + "_" + pattern + "_geometric_window";
                ModelFile window = models().withExistingParent(colorDir + itemName, modLoc("block/geometric_window"))
                        .texture("texture", patTex).texture("particle", patTex)
                        .renderType("minecraft:cutout");
                horizontalBlock(block.get(), window);
                itemModels().withExistingParent(itemName, window.getLocation());
            });
        });

    }

    private void registerElevators() {
        ResourceLocation elevatorTexture = modLoc("block/base/elevator");

        // Single shared model with tintindex:0 — all 32 elevators point to this one model.
        ModelFile elevatorModel = models().withExistingParent("block/elevator_model", mcLoc("block/block"))
                .texture("particle", elevatorTexture)
                .texture("all", elevatorTexture)
                .element().from(0, 0, 0).to(16, 16, 16)
                .face(Direction.DOWN).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.DOWN).tintindex(0).end()
                .face(Direction.UP).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.UP).tintindex(0).end()
                .face(Direction.NORTH).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.NORTH).tintindex(0).end()
                .face(Direction.SOUTH).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.SOUTH).tintindex(0).end()
                .face(Direction.WEST).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.WEST).tintindex(0).end()
                .face(Direction.EAST).uvs(0, 0, 16, 16).texture("#all").cullface(Direction.EAST).tintindex(0).end()
                .end();

        ModBlocks.ELEVATORS.forEach((colorName, block) -> {
            simpleBlock(block.get(), elevatorModel);
            itemModels().withExistingParent(block.getId().getPath(), elevatorModel.getLocation());
        });

        ModBlocks.FUTONS.forEach((color, block) -> registerFuton(block.get(), color));
    }

    // =========================================================================
    // === Misc blocks: blockstate + item model from existing manual models ===
    // =========================================================================

    private void registerMiscBlocks() {
        // ── Black marble ──────────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.BLACK_MARBLE.get(),              models().getExistingFile(modLoc("block/black_marble/black_marble")));
        simpleBlockWithItem(ModBlocks.BLACK_MARBLE_BRICKS.get(),       models().getExistingFile(modLoc("block/black_marble/black_marble_bricks")));
        simpleBlockWithItem(ModBlocks.BLACK_MARBLE_SMALL_BRICKS.get(), models().getExistingFile(modLoc("block/black_marble/black_marble_small_bricks")));
        simpleBlockWithItem(ModBlocks.BLACK_MARBLE_TILES.get(),        models().getExistingFile(modLoc("block/black_marble/black_marble_tiles")));
        simpleBlockWithItem(ModBlocks.BLACK_POLISHED_MARBLE.get(),     models().getExistingFile(modLoc("block/black_marble/black_polished_marble")));
        simpleBlockWithItem(ModBlocks.BLACK_MARBLE_FLOOR_TILE.get(),   models().getExistingFile(modLoc("block/black_marble/black_marble_floor_tile")));
        existingFacingShapeBlockNoUvLock(ModBlocks.BLACK_MARBLE_FANCY_FENCE.get(), "block/black_marble/black_marble_fancy_fence");
        existingAxisBlock(ModBlocks.BLACK_MARBLE_PILLAR.get(),     "block/black_marble/black_marble_pillar");
        existingAxisBlock(ModBlocks.BLACK_MARBLE_PILLAR_CAP.get(), "block/black_marble/black_marble_pillar_cap");

        // ── White marble ──────────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.WHITE_MARBLE.get(),              models().getExistingFile(modLoc("block/white_marble/white_marble")));
        simpleBlockWithItem(ModBlocks.WHITE_MARBLE_BRICKS.get(),       models().getExistingFile(modLoc("block/white_marble/white_marble_bricks")));
        simpleBlockWithItem(ModBlocks.WHITE_MARBLE_SMALL_BRICKS.get(), models().getExistingFile(modLoc("block/white_marble/white_marble_small_bricks")));
        simpleBlockWithItem(ModBlocks.WHITE_MARBLE_TILES.get(),        models().getExistingFile(modLoc("block/white_marble/white_marble_tiles")));
        simpleBlockWithItem(ModBlocks.WHITE_POLISHED_MARBLE.get(),     models().getExistingFile(modLoc("block/white_marble/white_polished_marble")));
        simpleBlockWithItem(ModBlocks.WHITE_MARBLE_FLOOR_TILE.get(),   models().getExistingFile(modLoc("block/white_marble/white_marble_floor_tile")));
        existingFacingShapeBlockNoUvLock(ModBlocks.WHITE_MARBLE_FANCY_FENCE.get(), "block/white_marble/white_marble_fancy_fence");
        existingAxisBlock(ModBlocks.WHITE_MARBLE_PILLAR.get(),     "block/white_marble/white_marble_pillar");
        existingAxisBlock(ModBlocks.WHITE_MARBLE_PILLAR_CAP.get(), "block/white_marble/white_marble_pillar_cap");

        // ── Limestone ─────────────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.LIMESTONE_MASONRY.get(),  models().getExistingFile(modLoc("block/limestone/limestone_masonry")));
        simpleBlockWithItem(ModBlocks.COBBLED_LIMESTONE.get(), models().getExistingFile(modLoc("block/limestone/cobbled_limestone")));
        existingEdgeBlock(ModBlocks.LIMESTONE_MASONRY_EDGE.get(),            "block/limestone/limestone_masonry_edge");
        existingFacingShapeBlock(ModBlocks.LIMESTONE_MASONRY_PLATE.get(),    "block/limestone/limestone_masonry_plate");
        existingFacingShapeBlockNoUvLock(ModBlocks.LIMESTONE_BANNISTER.get(), "block/limestone/limestone_bannister");

        // ── Salt & misc stone ─────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.SALT_BLOCK.get(),          models().getExistingFile(modLoc("block/salt/salt_block")));
        simpleBlockWithItem(ModBlocks.POLISHED_SALT_BLOCK.get(), models().getExistingFile(modLoc("block/salt/polished_salt_block")));
        simpleBlockWithItem(ModBlocks.SMOOTH_GLOWSTONE.get(),    models().getExistingFile(modLoc("block/glowstone/smooth_glowstone")));
        simpleBlockWithItem(ModBlocks.GLASS_JAR.get(),           models().getExistingFile(modLoc("block/glass_jar")));

        // ── Gold-plated smooth ────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.GOLD_PLATED_SMOOTH_BLOCK.get(), models().getExistingFile(modLoc("block/gold_plated_smooth/gold_plated_smooth_block")));
        existingEdgeBlock(ModBlocks.GOLD_PLATED_SMOOTH_EDGE.get(),          "block/gold_plated_smooth/gold_plated_smooth_edge");
        existingFacingShapeBlock(ModBlocks.GOLD_PLATED_SMOOTH_PLATE.get(),  "block/gold_plated_smooth/gold_plated_smooth_plate");

        // ── Roofing slates ────────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.ROOFING_SLATES.get(),      models().getExistingFile(modLoc("block/roofing_slates")));
        existingEdgeBlock(ModBlocks.ROOFING_SLATES_EDGE.get(),          "block/roofing_slates_edge");
        existingFacingShapeBlock(ModBlocks.ROOFING_SLATES_PLATE.get(),  "block/roofing_slates_plate");

        // ── Wheat thatch ──────────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.WHEAT_THATCH.get(),        models().getExistingFile(modLoc("block/wheat_thatch/wheat_thatch")));
        existingEdgeBlock(ModBlocks.WHEAT_THATCH_EDGE.get(),          "block/wheat_thatch/wheat_thatch_edge");
        existingFacingShapeBlock(ModBlocks.WHEAT_THATCH_PLATE.get(),  "block/wheat_thatch/wheat_thatch_plate");

        // ── Bamboo thatch ─────────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.BAMBOO_THATCH.get(),       models().getExistingFile(modLoc("block/bamboo/bamboo_thatch")));
        existingEdgeBlock(ModBlocks.BAMBOO_THATCH_EDGE.get(),         "block/bamboo/bamboo_thatch_edge");
        existingFacingShapeBlock(ModBlocks.BAMBOO_THATCH_PLATE.get(), "block/bamboo/bamboo_thatch_plate");

        // ── Sandstone shapes ──────────────────────────────────────────────────
        existingEdgeBlock(ModBlocks.SANDSTONE_EDGE.get(),               "block/sandstone/sandstone_edge");
        existingFacingShapeBlock(ModBlocks.SANDSTONE_PLATE.get(),       "block/sandstone/sandstone_plate");
        existingFacingShapeBlockNoUvLock(ModBlocks.SANDSTONE_CRENELATION.get(), "block/sandstone/sandstone_crenelation");
        existingEdgeBlock(ModBlocks.CUT_SANDSTONE_EDGE.get(),           "block/cut_sandstone/cut_sandstone_edge");
        existingFacingShapeBlock(ModBlocks.CUT_SANDSTONE_PLATE.get(),   "block/cut_sandstone/cut_sandstone_plate");
        existingEdgeBlock(ModBlocks.SMOOTH_SANDSTONE_EDGE.get(),        "block/smooth_sandstone/smooth_sandstone_edge");
        existingFacingShapeBlock(ModBlocks.SMOOTH_SANDSTONE_PLATE.get(),"block/smooth_sandstone/smooth_sandstone_plate");

        // ── Stone bricks masonry ──────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.STONE_BRICKS_MASONRY.get(),      models().getExistingFile(modLoc("block/stone_bricks/stone_bricks_masonry")));
        existingEdgeBlock(ModBlocks.STONE_BRICKS_MASONRY_EDGE.get(),          "block/stone_bricks/stone_bricks_masonry_edge");
        existingFacingShapeBlock(ModBlocks.STONE_BRICKS_MASONRY_PLATE.get(),  "block/stone_bricks/stone_bricks_masonry_plate");

        // ── Slender sandstone + turquoise ─────────────────────────────────────
        simpleBlockWithItem(ModBlocks.SLENDER_SANDSTONE_BRICKS.get(),    models().getExistingFile(modLoc("block/slender_sandstone/slender_sandstone_bricks")));
        existingEdgeBlock(ModBlocks.SLENDER_SANDSTONE_BRICKS_EDGE.get(),          "block/slender_sandstone/slender_sandstone_bricks_edge");
        existingFacingShapeBlock(ModBlocks.SLENDER_SANDSTONE_BRICKS_PLATE.get(),  "block/slender_sandstone/slender_sandstone_bricks_plate");
        simpleBlockWithItem(ModBlocks.SLENDER_TURQUOISE_PATTERN.get(),   models().getExistingFile(modLoc("block/slender_turquoise_pattern/slender_turquoise_pattern")));
        existingEdgeBlock(ModBlocks.SLENDER_TURQUOISE_PATTERN_EDGE.get(),         "block/slender_turquoise_pattern/slender_turquoise_pattern_edge");
        existingFacingShapeBlock(ModBlocks.SLENDER_TURQUOISE_PATTERN_PLATE.get(), "block/slender_turquoise_pattern/slender_turquoise_pattern_plate");

        // ── Mosaic floors ─────────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.MOSAIC_FLOOR.get(),         models().getExistingFile(modLoc("block/mosaic_floor/mosaic_floor")));
        simpleBlockWithItem(ModBlocks.MOSAIC_FLOOR_DELICATE.get(), models().getExistingFile(modLoc("block/mosaic_floor/mosaic_floor_delicate")));
        // Rosette: 4 random model variants
        ConfiguredModel[] rosetteModels = new ConfiguredModel[4];
        for (int i = 1; i <= 4; i++) {
            rosetteModels[i - 1] = ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/mosaic_floor/mosaic_floor_rosette_" + i)))
                    .build()[0];
        }
        simpleBlock(ModBlocks.MOSAIC_FLOOR_ROSETTE.get(), rosetteModels);
        itemModels().withExistingParent("mosaic_floor_rosette", modLoc("block/mosaic_floor/mosaic_floor_rosette_1"));

        // ── Elemental mosaics (border/geometric/pattern/delicate/traditional) ─
        for (String[] pair : new String[][]{
                {"water",  "water_mosaic"},
                {"earth",  "earth_mosaic"},
                {"fire",   "fire_mosaic"},
                {"spirit", "spirit_mosaic"},
                {"air",    "air_mosaic"}
        }) {
            String element = pair[0], dir = pair[1];
            String d = "block/" + dir + "/";
            simpleBlockWithItem(elementalMosaicBlock(element, "border"),      models().getExistingFile(modLoc(d + dir + "_border")));
            simpleBlockWithItem(elementalMosaicBlock(element, "geometric"),   models().getExistingFile(modLoc(d + dir + "_geometric")));
            simpleBlockWithItem(elementalMosaicBlock(element, "pattern"),     models().getExistingFile(modLoc(d + dir + "_pattern")));
            simpleBlockWithItem(elementalMosaicBlock(element, "delicate"),    models().getExistingFile(modLoc(d + dir + "_delicate")));
            simpleBlockWithItem(elementalMosaicBlock(element, "traditional"), models().getExistingFile(modLoc(d + dir + "_traditional")));
            // recess: manual blockstate kept — complex geometry needs hand-tuned x/y rotations
        }

        // ── Roman fresco ──────────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.ROMAN_FRESCO_RED.get(),   models().getExistingFile(modLoc("block/roman_fresco/roman_fresco_red")));
        simpleBlockWithItem(ModBlocks.ROMAN_FRESCO_BLACK.get(), models().getExistingFile(modLoc("block/roman_fresco/roman_fresco_black")));

        // ── Ornamented / delicate wool & carpet ───────────────────────────────
        simpleBlockWithItem(ModBlocks.ORNAMENTED_RED_WOOL.get(),      models().getExistingFile(modLoc("block/ornamented_red_wool")));
        simpleBlockWithItem(ModBlocks.DELICATE_RED_WOOL.get(),        models().getExistingFile(modLoc("block/delicate_red_wool")));
        simpleBlockWithItem(ModBlocks.ORNAMENTED_RED_CARPET.get(),    models().getExistingFile(modLoc("block/ornamented_red_carpet")));
        simpleBlockWithItem(ModBlocks.DELICATE_RED_CARPET.get(),      models().getExistingFile(modLoc("block/delicate_red_carpet")));
        simpleBlockWithItem(ModBlocks.ORNAMENTED_BLUE_WOOL.get(),     models().getExistingFile(modLoc("block/ornamented_blue_wool")));
        simpleBlockWithItem(ModBlocks.DELICATE_BLUE_WOOL.get(),       models().getExistingFile(modLoc("block/delicate_blue_wool")));
        simpleBlockWithItem(ModBlocks.ORNAMENTED_BLUE_CARPET.get(),   models().getExistingFile(modLoc("block/ornamented_blue_carpet")));
        simpleBlockWithItem(ModBlocks.DELICATE_BLUE_CARPET.get(),     models().getExistingFile(modLoc("block/delicate_blue_carpet")));
        simpleBlockWithItem(ModBlocks.ORNAMENTED_GREEN_WOOL.get(),    models().getExistingFile(modLoc("block/ornamented_green_wool")));
        simpleBlockWithItem(ModBlocks.DELICATE_GREEN_WOOL.get(),      models().getExistingFile(modLoc("block/delicate_green_wool")));
        simpleBlockWithItem(ModBlocks.ORNAMENTED_GREEN_CARPET.get(),  models().getExistingFile(modLoc("block/ornamented_green_carpet")));
        simpleBlockWithItem(ModBlocks.DELICATE_GREEN_CARPET.get(),    models().getExistingFile(modLoc("block/delicate_green_carpet")));
        simpleBlockWithItem(ModBlocks.ORNAMENTED_PURPLE_WOOL.get(),   models().getExistingFile(modLoc("block/ornamented_purple_wool")));
        simpleBlockWithItem(ModBlocks.DELICATE_PURPLE_WOOL.get(),     models().getExistingFile(modLoc("block/delicate_purple_wool")));
        simpleBlockWithItem(ModBlocks.ORNAMENTED_PURPLE_CARPET.get(), models().getExistingFile(modLoc("block/ornamented_purple_carpet")));
        simpleBlockWithItem(ModBlocks.DELICATE_PURPLE_CARPET.get(),   models().getExistingFile(modLoc("block/delicate_purple_carpet")));

        // ── Caterpillar jar ───────────────────────────────────────────────────
        simpleBlockWithItem(ModBlocks.CATERPILLAR_JAR.get(), models().getExistingFile(modLoc("block/caterpillar_jar")));

        // ── Butterfly jars ────────────────────────────────────────────────────
        ModBlocks.BUTTERFLY_JARS.forEach((variant, block) -> {
            String name = "butterfly_jar_" + variant.getName();
            simpleBlockWithItem(block.get(), models().getExistingFile(modLoc("block/butterfly_jar/" + name)));
        });

        // ── Stone variant blocks (simple CTM — Chipped style) ─────────────────
        // Plain cube_all
        stoneBlock(ModBlocks.ANGRY_STONE);
        stoneBlock(ModBlocks.BLANK_STONE_CARVING);
        stoneBlock(ModBlocks.BRICK_BORDERED_STONE);
        stoneBlock(ModBlocks.CARVED_STONE);
        stoneBlock(ModBlocks.CHECKERED_STONE_TILES);
        stoneBlock(ModBlocks.COBBLED_STONE);
        stoneBlock(ModBlocks.CRACKED_DISORDERED_STONE_BRICKS);
        stoneBlock(ModBlocks.CRACKED_FLAT_STONE_TILES);
        stoneBlock(ModBlocks.CREEPER_STONE_CARVING);
        stoneBlock(ModBlocks.CRYING_STONE);
        stoneBlock(ModBlocks.CURLY_STONE_PILLAR);
        stoneBlock(ModBlocks.CUT_BLANK_STONE);
        stoneBlock(ModBlocks.DUH_STONE);
        stoneBlock(ModBlocks.ENGRAVED_STONE);
        stoneBlock(ModBlocks.ETCHED_STONE_BRICKS);
        stoneBlock(ModBlocks.FINE_STONE_PILLAR);
        stoneBlock(ModBlocks.FLAT_STONE_TILES);
        stoneBlock(ModBlocks.GLAD_STONE);
        stoneBlock(ModBlocks.INLAYED_STONE);
        stoneBlock(ModBlocks.INSCRIBED_STONE);
        stoneBlock(ModBlocks.LAYED_STONE_BRICKS);
        stoneBlock(ModBlocks.LODED_STONE);
        stoneBlock(ModBlocks.OFFSET_STONE_BRICKS);
        stoneBlock(ModBlocks.ORNATE_STONE_PILLAR);
        stoneBlock(ModBlocks.OVERLAPPING_STONE_TILES);
        stoneBlock(ModBlocks.PILLAR_STONE_BRICKS);
        stoneBlock(ModBlocks.POLISHED_STONE);
        stoneBlock(ModBlocks.PRISMAL_STONE_REMNANTS);
        stoneBlock(ModBlocks.ROUGH_STONE);
        stoneBlock(ModBlocks.ROUNDED_STONE_BRICKS);
        stoneBlock(ModBlocks.RUNIC_CARVED_STONE);
        stoneBlock(ModBlocks.SAD_STONE);
        stoneBlock(ModBlocks.SANDED_STONE);
        stoneBlock(ModBlocks.SIMPLE_STONE_PILLAR);
        stoneBlock(ModBlocks.SMALL_STONE_BRICKS);
        stoneBlock(ModBlocks.SMOOTH_INLAYED_STONE);
        stoneBlock(ModBlocks.SMOOTHED_DOUBLE_INLAYED_STONE);
        stoneBlock(ModBlocks.SPIDER_STONE_CARVING);
        stoneBlock(ModBlocks.SPIRALED_STONE);
        stoneBlock(ModBlocks.STACKED_STONE_BRICKS);
        stoneBlock(ModBlocks.STONE_MINI_TILES);
        stoneBlock(ModBlocks.STONE_SCALES);
        stoneBlock(ModBlocks.THICK_INLAYED_STONE);
        stoneBlock(ModBlocks.TILED_BORDERED_STONE);
        stoneBlock(ModBlocks.TILED_STONE);
        stoneBlock(ModBlocks.TINY_BRICK_BORDERED_STONE);
        stoneBlock(ModBlocks.TINY_LAYERED_STONE_BRICKS);
        stoneBlock(ModBlocks.TINY_LAYERED_STONE_SLABS);
        stoneBlock(ModBlocks.TINY_STONE_BRICKS);
        stoneBlock(ModBlocks.TRODDEN_STONE);
        stoneBlock(ModBlocks.UNAMUSED_STONE);
        stoneBlock(ModBlocks.VERTICAL_CUT_STONE);
        stoneBlock(ModBlocks.VERTICAL_DISORDERED_STONE_BRICKS);
        stoneBlock(ModBlocks.WEATHERED_STONE);
        // Column (RotatedPillarBlock)
        existingAxisBlock(ModBlocks.STONE_PILLAR.get(), "block/stone/stone_pillar");
        // CTM blocks
        stoneCTMBlock(ModBlocks.BORDERED_STONE);
        stoneCTMBlock(ModBlocks.CUT_STONE_COLUMN);
        stoneCTMBlock(ModBlocks.EDGED_STONE_BRICKS);
        stoneCTMBlock(ModBlocks.MASSIVE_STONE_BRICKS);
        stoneCTMBlock(ModBlocks.SMOOTH_STONE_COLUMN);
        stoneCTMBlock(ModBlocks.TILED_STONE_COLUMN);

        // Purpur CTM blocks
        simpleBlockWithItem(ModBlocks.CONNECTING_PURPUR_PILLAR.get(),
                models().getExistingFile(modLoc("block/purpur/connecting_purpur_pillar")));

        // Sandstone CTM blocks
        simpleBlockWithItem(ModBlocks.CONNECTING_SANDSTONE.get(),
                models().getExistingFile(modLoc("block/sandstone/connecting_sandstone")));
        simpleBlockWithItem(ModBlocks.CONNECTING_RED_SANDSTONE.get(),
                models().getExistingFile(modLoc("block/red_sandstone/connecting_red_sandstone")));
        simpleBlockWithItem(ModBlocks.CONNECTING_POLISHED_ANDESITE.get(),
                models().getExistingFile(modLoc("block/andesite/connecting_polished_andesite")));
        simpleBlockWithItem(ModBlocks.CONNECTING_POLISHED_BLACKSTONE.get(),
                models().getExistingFile(modLoc("block/blackstone/connecting_polished_blackstone")));
        simpleBlockWithItem(ModBlocks.CONNECTING_POLISHED_DIORITE.get(),
                models().getExistingFile(modLoc("block/diorite/connecting_polished_diorite")));
        simpleBlockWithItem(ModBlocks.CONNECTING_POLISHED_GRANITE.get(),
                models().getExistingFile(modLoc("block/granite/connecting_polished_granite")));
        simpleBlockWithItem(ModBlocks.CONNECTING_NETHERITE_BLOCK.get(),
                models().getExistingFile(modLoc("block/netherite/connecting_netherite_block")));
        simpleBlockWithItem(ModBlocks.CONNECTING_SMOOTH_STONE.get(),
                models().getExistingFile(modLoc("block/stone/connecting_smooth_stone")));
        existingAxisBlock(ModBlocks.CHISELED_PLASTERED_STONE_PILLAR.get(), "block/chiseled_plastered_stone_pillar");
    }

    /** Convenience: look up the elemental mosaic block by element name + type suffix. */
    private Block elementalMosaicBlock(String element, String type) {
        return switch (element + "_" + type) {
            case "water_border"      -> ModBlocks.WATER_MOSAIC_BORDER.get();
            case "water_geometric"   -> ModBlocks.WATER_MOSAIC_GEOMETRIC.get();
            case "water_pattern"     -> ModBlocks.WATER_MOSAIC_PATTERN.get();
            case "water_delicate"    -> ModBlocks.WATER_MOSAIC_DELICATE.get();
            case "water_traditional" -> ModBlocks.WATER_MOSAIC_TRADITIONAL.get();
            case "water_recess"      -> ModBlocks.WATER_MOSAIC_RECESS.get();
            case "earth_border"      -> ModBlocks.EARTH_MOSAIC_BORDER.get();
            case "earth_geometric"   -> ModBlocks.EARTH_MOSAIC_GEOMETRIC.get();
            case "earth_pattern"     -> ModBlocks.EARTH_MOSAIC_PATTERN.get();
            case "earth_delicate"    -> ModBlocks.EARTH_MOSAIC_DELICATE.get();
            case "earth_traditional" -> ModBlocks.EARTH_MOSAIC_TRADITIONAL.get();
            case "earth_recess"      -> ModBlocks.EARTH_MOSAIC_RECESS.get();
            case "fire_border"       -> ModBlocks.FIRE_MOSAIC_BORDER.get();
            case "fire_geometric"    -> ModBlocks.FIRE_MOSAIC_GEOMETRIC.get();
            case "fire_pattern"      -> ModBlocks.FIRE_MOSAIC_PATTERN.get();
            case "fire_delicate"     -> ModBlocks.FIRE_MOSAIC_DELICATE.get();
            case "fire_traditional"  -> ModBlocks.FIRE_MOSAIC_TRADITIONAL.get();
            case "fire_recess"       -> ModBlocks.FIRE_MOSAIC_RECESS.get();
            case "spirit_border"     -> ModBlocks.SPIRIT_MOSAIC_BORDER.get();
            case "spirit_geometric"  -> ModBlocks.SPIRIT_MOSAIC_GEOMETRIC.get();
            case "spirit_pattern"    -> ModBlocks.SPIRIT_MOSAIC_PATTERN.get();
            case "spirit_delicate"   -> ModBlocks.SPIRIT_MOSAIC_DELICATE.get();
            case "spirit_traditional"-> ModBlocks.SPIRIT_MOSAIC_TRADITIONAL.get();
            case "spirit_recess"     -> ModBlocks.SPIRIT_MOSAIC_RECESS.get();
            case "air_border"        -> ModBlocks.AIR_MOSAIC_BORDER.get();
            case "air_geometric"     -> ModBlocks.AIR_MOSAIC_GEOMETRIC.get();
            case "air_pattern"       -> ModBlocks.AIR_MOSAIC_PATTERN.get();
            case "air_delicate"      -> ModBlocks.AIR_MOSAIC_DELICATE.get();
            case "air_traditional"   -> ModBlocks.AIR_MOSAIC_TRADITIONAL.get();
            case "air_recess"        -> ModBlocks.AIR_MOSAIC_RECESS.get();
            default -> throw new IllegalArgumentException("Unknown mosaic: " + element + "_" + type);
        };
    }

    /**
     * Registers a PlateBlock (facing + shape, no half) using existing model files.
     * Derives inner/outer paths by appending _inner/_outer to modelPath.
     * Also registers the item model.
     */
    private void existingFacingShapeBlock(Block block, String modelPath) {
        String name = modelPath.substring(modelPath.lastIndexOf('/') + 1);
        registerFacingShapeBlock(block,
                models().getExistingFile(modLoc(modelPath)),
                models().getExistingFile(modLoc(modelPath + "_outer")),
                models().getExistingFile(modLoc(modelPath + "_inner")));
        itemModels().withExistingParent(name, modLoc(modelPath));
    }

    /**
     * Like existingFacingShapeBlock but uses uvLock=false.
     * Used for blocks whose up-face UV coordinates would map into transparent texture
     * regions after uvlock rotation (e.g. limestone_bannister, sandstone_crenelation).
     */
    private void existingFacingShapeBlockNoUvLock(Block block, String modelPath) {
        String name = modelPath.substring(modelPath.lastIndexOf('/') + 1);
        ModelFile straight = models().getExistingFile(modLoc(modelPath));
        ModelFile outer    = models().getExistingFile(modLoc(modelPath + "_outer"));
        ModelFile inner    = models().getExistingFile(modLoc(modelPath + "_inner"));
        getVariantBuilder(block).forAllStatesExcept(state -> {
            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
            StairsShape shape = state.getValue(BlockStateProperties.STAIRS_SHAPE);
            int yRot = switch (facing) {
                case SOUTH -> 0;
                case WEST  -> 90;
                case NORTH -> 180;
                case EAST  -> 270;
                default    -> 0;
            };
            boolean isLeft = shape == StairsShape.OUTER_LEFT || shape == StairsShape.INNER_LEFT;
            if (isLeft) yRot = (yRot + 270) % 360;
            ModelFile model = switch (shape) {
                case STRAIGHT                -> straight;
                case OUTER_LEFT, OUTER_RIGHT -> outer;
                case INNER_LEFT, INNER_RIGHT -> inner;
            };
            return ConfiguredModel.builder().modelFile(model).rotationY(yRot).uvLock(false).build();
        }, BlockStateProperties.WATERLOGGED);
        itemModels().withExistingParent(name, modLoc(modelPath));
    }

    /**
     * Registers an EdgeBlock (facing + shape + half) using existing model files.
     * Derives inner/outer paths by appending _inner/_outer to modelPath.
     * Also registers the item model.
     */
    private void existingEdgeBlock(Block block, String modelPath) {
        String name = modelPath.substring(modelPath.lastIndexOf('/') + 1);
        registerFacingShapeHalfBlock(block,
                models().getExistingFile(modLoc(modelPath)),
                models().getExistingFile(modLoc(modelPath + "_outer")),
                models().getExistingFile(modLoc(modelPath + "_inner")));
        itemModels().withExistingParent(name, modLoc(modelPath));
    }

    /**
     * Registers a RotatedPillarBlock (axis=x/y/z) using a single existing model file.
     * Also registers the item model.
     */
    private void existingAxisBlock(RotatedPillarBlock block, String modelPath) {
        ModelFile model = models().getExistingFile(modLoc(modelPath));
        axisBlock(block, model, model);
        String name = modelPath.substring(modelPath.lastIndexOf('/') + 1);
        itemModels().withExistingParent(name, modLoc(modelPath));
    }

    /** Registers a plain cube_all stone variant block with auto-generated model. */
    private void stoneBlock(DeferredBlock<? extends Block> block) {
        String name = block.getId().getPath();
        simpleBlockWithItem(block.get(), models().cubeAll("block/stone/" + name, modLoc("block/stone/" + name)));
    }

    /** Registers a CTM stone variant block using an existing model file in src/main/resources. */
    private void stoneCTMBlock(DeferredBlock<? extends Block> block) {
        String name = block.getId().getPath();
        simpleBlockWithItem(block.get(), models().getExistingFile(modLoc("block/stone/" + name)));
    }

    private void registerFuton(Block futon, String color) {
        ResourceLocation futonTex = modLoc("block/color_set/" + color + "/futon");
        ModelFile head = models().withExistingParent(color + "_futon_head", modLoc("block/template_futon_head"))
                .texture("futon", futonTex);
        ModelFile foot = models().withExistingParent(color + "_futon_foot", modLoc("block/template_futon_foot"))
                .texture("futon", futonTex);
        getVariantBuilder(futon).forAllStates(state -> {
            BedPart part = state.getValue(BedBlock.PART);
            net.minecraft.core.Direction facing = state.getValue(BedBlock.FACING);
            return ConfiguredModel.builder()
                    .modelFile(part == BedPart.HEAD ? head : foot)
                    .rotationY(((int) facing.toYRot() + 180) % 360)
                    .build();
        });
        itemModels().withExistingParent(color + "_futon", modLoc("item/template_futon"))
                .texture("futon", futonTex);
    }
}