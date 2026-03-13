package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.IGradientBlock;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.crop.ThornyHedgeSprouts;
import com.otterly76.ott.hedge.ModHedgeVariants;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;
import org.jetbrains.annotations.NotNull;

public class OttBlockStateProvider extends ModBlockStateProvider {
    public OttBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.WOOD_SETS.forEach(this::registerWoodSet);
        
        ModBlocks.SEAGLASS.forEach(block -> simpleBlockWithItem(block.get(), cubeAll(block.get())));
        ModBlocks.LIMESTONE.forEach(block -> simpleBlockWithItem(block.get(), cubeAll(block.get())));
        
        ModBlocks.TESTBLOCK.forEach(block -> {
            ModelFile model = models().getExistingFile(modLoc("block/" + block.getId().getPath()));
            simpleBlock(block.get(), model);
        });
        
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
        ResourceLocation sideTexture = modLoc("block/" + block.get().getRegistryID().getPath());
        final ModelFile blockModel = models().cube("block/" + block.get().getRegistryID().getPath(), mcLoc("block/" + block.get().getTextureName(block.get().getSecondColor())), mcLoc("block/" + block.get().getTextureName(block.get().getFirstColor())), sideTexture, sideTexture, sideTexture, sideTexture)
                .texture("particle", mcLoc("block/" + block.get().getTextureName(block.get().getFirstColor())))
                .renderType(block.get().getRenderType());
        itemModels().simpleBlockItem(block.get());
        directionalBlock(block.get(), blockModel);
    }

    private void registerColorSet(String color, ModBlocks.ColorSetBlocks set) {
        // Concrete, Terracotta, Wool
        tintedCubeAll(set.concrete().get(), mcLoc("block/white_concrete"));
        tintedCubeAll(set.terracotta().get(), mcLoc("block/white_terracotta"));
        tintedCubeAll(set.wool().get(), mcLoc("block/white_wool"));

        // Concrete Powder
        tintedCubeAll(set.concretePowder().get(), mcLoc("block/white_concrete_powder"));

        // Stained Glass
        tintedCubeAll(set.stainedGlass().get(), mcLoc("block/white_stained_glass"), "translucent");

        // Stained Glass Pane
        tintedPaneBlock(set.stainedGlassPane().get(), mcLoc("block/white_stained_glass"), mcLoc("block/white_stained_glass_pane_top"));

        // Glazed Terracotta
        horizontalBlock(set.glazedTerracotta().get(), models().withExistingParent(color + "_glazed_terracotta", mcLoc("block/template_glazed_terracotta"))
                .texture("pattern", modLoc("block/glazed_terracotta/" + color)));

        // Shulker Box
        tintedCubeAll(set.shulkerBox().get(), mcLoc("block/white_shulker_box"));

        // Candle
        registerCandle(set.candle().get(), color);

        // Bed
        registerBed(set.bed().get(), color);

        // Carpet
        tintedCarpet(set.carpet().get(), mcLoc("block/white_wool"));

        // Banner
        registerBanner(set.banner().get(), set.wallBanner().get(), color);
    }

    private void tintedCubeAll(Block block, ResourceLocation texture) {
        tintedCubeAll(block, texture, "solid");
    }

    private void tintedCubeAll(Block block, ResourceLocation texture, String renderType) {
        simpleBlock(block, models().withExistingParent(blockPath(block), mcLoc("block/block"))
                .texture("all", texture)
                .texture("particle", texture)
                .renderType(mcLoc(renderType))
                .element()
                .from(0, 0, 0)
                .to(16, 16, 16)
                .allFaces((dir, face) -> face.texture("#all").cullface(dir).tintindex(0))
                .end());
    }

    private void tintedCarpet(Block block, ResourceLocation texture) {
        simpleBlock(block, models().withExistingParent(blockPath(block), mcLoc("block/block"))
                .texture("all", texture)
                .texture("particle", texture)
                .element()
                .from(0, 0, 0)
                .to(16, 1, 16)
                .face(net.minecraft.core.Direction.UP).texture("#all").tintindex(0).end()
                .face(net.minecraft.core.Direction.DOWN).texture("#all").cullface(net.minecraft.core.Direction.DOWN).tintindex(0).end()
                .face(net.minecraft.core.Direction.NORTH).texture("#all").cullface(net.minecraft.core.Direction.NORTH).tintindex(0).end()
                .face(net.minecraft.core.Direction.SOUTH).texture("#all").cullface(net.minecraft.core.Direction.SOUTH).tintindex(0).end()
                .face(net.minecraft.core.Direction.WEST).texture("#all").cullface(net.minecraft.core.Direction.WEST).tintindex(0).end()
                .face(net.minecraft.core.Direction.EAST).texture("#all").cullface(net.minecraft.core.Direction.EAST).tintindex(0).end()
                .end());
    }

    private void tintedPaneBlock(IronBarsBlock block, ResourceLocation side, ResourceLocation edge) {
        String baseName = blockPath(block);
        ModelFile post = tintedPaneModel(baseName + "_post", side, edge, true, false, false);
        ModelFile sideModel = tintedPaneModel(baseName + "_side", side, edge, false, true, false);
        ModelFile sideAlt = tintedPaneModel(baseName + "_side_alt", side, edge, false, false, true);

        getMultipartBuilder(block)
                .part().modelFile(post).addModel().end()
                .part().modelFile(sideModel).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(sideAlt).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(sideModel).rotationY(180).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(sideAlt).rotationY(180).addModel().condition(BlockStateProperties.WEST, true).end();
    }

    private ModelFile tintedPaneModel(String name, ResourceLocation side, ResourceLocation edge, boolean post, boolean sideM, boolean sideAlt) {
        var builder = models().withExistingParent(name, mcLoc("block/block"))
                .texture("edge", edge)
                .texture("pane", side)
                .texture("particle", side)
                .renderType(mcLoc("translucent"));

        if (post) {
            builder.element().from(7, 0, 7).to(9, 16, 9)
                    .face(net.minecraft.core.Direction.NORTH).uvs(7, 0, 9, 16).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.SOUTH).uvs(7, 0, 9, 16).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.WEST).uvs(7, 0, 9, 16).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.EAST).uvs(7, 0, 9, 16).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.UP).uvs(7, 7, 9, 9).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.DOWN).uvs(7, 7, 9, 9).texture("#edge").cullface(net.minecraft.core.Direction.DOWN).tintindex(0).end()
                    .end();
        } else if (sideM) { // NORTH
            builder.element().from(7, 0, 0).to(9, 16, 7)
                    .face(net.minecraft.core.Direction.NORTH).uvs(7, 0, 9, 16).texture("#edge").cullface(net.minecraft.core.Direction.NORTH).tintindex(0).end()
                    .face(net.minecraft.core.Direction.SOUTH).uvs(7, 0, 9, 16).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.WEST).uvs(0, 0, 7, 16).texture("#pane").tintindex(0).end()
                    .face(net.minecraft.core.Direction.EAST).uvs(7, 0, 0, 16).texture("#pane").tintindex(0).end()
                    .face(net.minecraft.core.Direction.UP).uvs(7, 0, 9, 7).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.DOWN).uvs(7, 0, 9, 7).texture("#edge").cullface(net.minecraft.core.Direction.DOWN).tintindex(0).end()
                    .end();
        } else if (sideAlt) { // EAST
            builder.element().from(9, 0, 7).to(16, 16, 9)
                    .face(net.minecraft.core.Direction.NORTH).uvs(9, 0, 16, 16).texture("#pane").tintindex(0).end()
                    .face(net.minecraft.core.Direction.SOUTH).uvs(16, 0, 9, 16).texture("#pane").tintindex(0).end()
                    .face(net.minecraft.core.Direction.WEST).uvs(7, 0, 9, 16).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.EAST).uvs(7, 0, 9, 16).texture("#edge").cullface(net.minecraft.core.Direction.EAST).tintindex(0).end()
                    .face(net.minecraft.core.Direction.UP).uvs(9, 7, 16, 9).texture("#edge").tintindex(0).end()
                    .face(net.minecraft.core.Direction.DOWN).uvs(9, 7, 16, 9).texture("#edge").cullface(net.minecraft.core.Direction.DOWN).tintindex(0).end()
                    .end();
        }

        return builder;
    }

    private void registerBed(Block bed, String color) {
        ResourceLocation woolTex = mcLoc("block/white_wool");
        // For the dummy block models used for particles/items, we use the tinted wool
        ModelFile head = models().withExistingParent(color + "_bed_head", modLoc("block/templates/tinted_cube_all"))
                .texture("all", woolTex);
        ModelFile foot = models().withExistingParent(color + "_bed_foot", modLoc("block/templates/tinted_cube_all"))
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

    private void registerCandle(Block candle, String color) {
        ResourceLocation candleTex = mcLoc("block/candle");

        ModelFile one = models().withExistingParent(color + "_candle_one_candle", modLoc("block/templates/tinted_template_candle"))
                .texture("all", candleTex);
        ModelFile oneLit = models().withExistingParent(color + "_candle_one_candle_lit", modLoc("block/templates/tinted_template_candle"))
                .texture("all", candleTex);
        ModelFile two = models().withExistingParent(color + "_candle_two_candles", modLoc("block/templates/tinted_template_two_candles"))
                .texture("all", candleTex);
        ModelFile twoLit = models().withExistingParent(color + "_candle_two_candles_lit", modLoc("block/templates/tinted_template_two_candles"))
                .texture("all", candleTex);
        ModelFile three = models().withExistingParent(color + "_candle_three_candles", modLoc("block/templates/tinted_template_three_candles"))
                .texture("all", candleTex);
        ModelFile threeLit = models().withExistingParent(color + "_candle_three_candles_lit", modLoc("block/templates/tinted_template_three_candles"))
                .texture("all", candleTex);
        ModelFile four = models().withExistingParent(color + "_candle_four_candles", modLoc("block/templates/tinted_template_four_candles"))
                .texture("all", candleTex);
        ModelFile fourLit = models().withExistingParent(color + "_candle_four_candles_lit", modLoc("block/templates/tinted_template_four_candles"))
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

    private void registerBanner(Block banner, Block wallBanner, String color) {
        ModelFile model = models().getBuilder(color + "_banner").parent(new ModelFile.UncheckedModelFile("builtin/entity"));

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
        registerSapling(set.sapling().get(), set.pottedSapling().get(), setName);
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