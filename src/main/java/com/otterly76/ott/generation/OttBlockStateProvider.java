package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredHolder;

public class OttBlockStateProvider extends BlockStateProvider {

    public OttBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, "ott", exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // Iterate over your populated list of leaves
        for (DeferredHolder<Block, ? extends Block> blockObj : ModBlocks.LEAVES) {
            leavesBlock(blockObj.get());
        }
    }

    // Helper method to generate the 5 models + 1 blockstate
    private void leavesBlock(Block block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();

        // The suffixes corresponding to your 5 parent templates:
        // block/leaves, block/leaves1, block/leaves2, block/leaves3, block/leaves4
        String[] suffixes = {"", "1", "2", "3", "4"};

        ConfiguredModel[] models = new ConfiguredModel[suffixes.length];

        for (int i = 0; i < suffixes.length; i++) {
            String suffix = suffixes[i];

            // 1. Create the Model JSON for this variant
            // output: assets/ott/models/block/<name><suffix>.json
            // parent: assets/ott/models/block/leaves<suffix>.json  (Must exist in your resources!)
            // texture: uses the base name because all variants share the same texture file
            ModelFile leafModel = models().withExistingParent(name + suffix, modLoc("block/leaves_notint" + suffix))
                    .texture("all", "block/leaves/" + name);

            models[i] = new ConfiguredModel(leafModel);
        }

        // 2. Create the BlockState JSON
        // output: assets/ott/blockstates/<name>.json
        // This registers a state that picks randomly from the 5 models we just created
        simpleBlock(block, models);

        // 3. Item Model
        // output: assets/ott/models/item/<name>.json
        // Uses the first model (the one with no suffix) as the item representation
        simpleBlockItem(block, models[0].model);
    }
}