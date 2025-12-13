package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class MinecraftBackportItemModelProvider extends ItemModelProvider {

    public MinecraftBackportItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "minecraft", existingFileHelper);
    }

    @Override
    @SuppressWarnings("IfCanBeSwitch")
    protected void registerModels() {
        // BlockItems for ALL backported blocks
        ModBlocks.MINECRAFT_BLOCKS.getEntries().forEach(deferredBlock -> {
            ResourceLocation id = deferredBlock.getId(); // minecraft:<path>
            String path = id.getPath();
            Block block = deferredBlock.get();

            // Doors use item textures, not block models
            if (block instanceof DoorBlock) {
                generatedItem(path);
                return;
            }

            // Resin clump item should be a flat sprite (item/generated), not a block-model parent
            if (deferredBlock == ModBlocks.RESIN_CLUMP) {
                generatedItem(path);
                return;
            }

            // Fence item uses <name>_inventory
            if (block instanceof FenceBlock) {
                parentItemToBlockModel(path, "block/" + path + "_inventory");
                return;
            }

            // Wall item uses <name>_inventory
            if (block instanceof WallBlock) {
                parentItemToBlockModel(path, "block/" + path + "_inventory");
                return;
            }

            // Trapdoor item typically points at the bottom model
            if (block instanceof TrapDoorBlock) {
                parentItemToBlockModel(path, "block/" + path + "_bottom");
                return;
            }

            // Default: item model -> corresponding block model
            parentItemToBlockModel(path, "block/" + path);
        });

        // Standalone backported items that use item textures (not block models)
        generatedItem(ModItems.RESIN_BRICK.getId().getPath());

        generatedItem(ModItems.PALE_OAK_SIGN.getId().getPath());
        generatedItem(ModItems.PALE_OAK_HANGING_SIGN.getId().getPath());

        generatedItem(ModItems.PALE_OAK_BOAT.getId().getPath());
        generatedItem(ModItems.PALE_OAK_CHEST_BOAT.getId().getPath());
    }

    private void generatedItem(String name) {
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", mcLoc("item/" + name));
    }

    private void parentItemToBlockModel(String itemName, String blockModelPath) {
        // Avoid existence checks during generation order (block models provider may run before/after this one).
        getBuilder(itemName).parent(new ModelFile.UncheckedModelFile(mcLoc(blockModelPath)));
    }
}