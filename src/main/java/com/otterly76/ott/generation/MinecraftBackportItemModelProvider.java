package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
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
        ModBlocks.MINECRAFT_BLOCKS.getEntries().forEach(deferredBlock -> {
            ResourceLocation id = deferredBlock.getId();
            String path = id.getPath();
            Block block = deferredBlock.get();

            if (path.startsWith("potted_") || block instanceof FlowerPotBlock) {
                return;
            }
            if (path.endsWith("_wall_sign") || path.endsWith("_wall_hanging_sign")) {
                return;
            }

            if (block instanceof DoorBlock) {
                generatedItem(path);
                return;
            }

            if (deferredBlock == ModBlocks.RESIN_CLUMP) {
                generatedItem(path);
                return;
            }

            if (block instanceof FenceBlock) {
                parentItemToBlockModel(path, "block/" + path + "_inventory");
                return;
            }

            if (block instanceof WallBlock) {
                parentItemToBlockModel(path, "block/" + path + "_inventory");
                return;
            }

            if (block instanceof TrapDoorBlock) {
                parentItemToBlockModel(path, "block/" + path + "_bottom");
                return;
            }

            parentItemToBlockModel(path, "block/" + path);
        });

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
        getBuilder(itemName).parent(new ModelFile.UncheckedModelFile(mcLoc(blockModelPath)));
    }
}