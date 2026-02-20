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

            // EXCLUSIONS
            if (path.equals("wildflowers") || path.equals("leaf_litter") || path.equals("dried_ghast") || path.equals("pale_oak_sapling") || path.equals("potted_pale_oak_sapling")) {
                return;
            }
            if (path.endsWith("_wall_sign") || path.endsWith("_wall_hanging_sign")) {
                return;
            }

            // Doors: 3D door item model (vanilla-style)
            if (block instanceof DoorBlock) {
                doorItemFromTextures(path);
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

            // Buttons: item should use *_inventory
            if (block instanceof ButtonBlock) {
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
        generatedItem(ModItems.MUSIC_DISC_TEARS.getId().getPath());
        generatedItem(ModItems.MUSIC_DISC_LAVA_CHICKEN.getId().getPath());
        generatedItem(ModItems.BLUE_EGG.getId().getPath());
        generatedItem(ModItems.BROWN_EGG.getId().getPath());

        spawnEggItem(ModItems.CREAKING_SPAWN_EGG.getId().getPath());
        spawnEggItem(ModItems.HAPPY_GHAST_SPAWN_EGG.getId().getPath());

        ModItems.HARNESSES.values().forEach(item -> generatedItem(item.getId().getPath()));

        generatedItem(ModItems.COPPER_NUGGET.getId().getPath());
        handheldItem(ModItems.COPPER_SWORD.getId().getPath());
        handheldItem(ModItems.COPPER_SHOVEL.getId().getPath());
        handheldItem(ModItems.COPPER_PICKAXE.getId().getPath());
        handheldItem(ModItems.COPPER_AXE.getId().getPath());
        handheldItem(ModItems.COPPER_HOE.getId().getPath());

        generatedItem(ModItems.COPPER_HELMET.getId().getPath());
        generatedItem(ModItems.COPPER_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.COPPER_LEGGINGS.getId().getPath());
        generatedItem(ModItems.COPPER_BOOTS.getId().getPath());

        generatedItem(ModItems.COPPER_HORSE_ARMOR.getId().getPath());
        generatedItem(ModItems.NETHERITE_HORSE_ARMOR.getId().getPath());

        withExistingParent(ModItems.PALE_OAK_BOAT.getId().getPath(), mcLoc("item/oak_boat"))
                .texture("texture", mcLoc("item/entity/boat/pale_oak"));
        withExistingParent(ModItems.PALE_OAK_CHEST_BOAT.getId().getPath(), mcLoc("item/oak_chest_boat"))
                .texture("texture", mcLoc("item/entity/chest_boat/pale_oak"));
    }

    private void spawnEggItem(String name) {
        withExistingParent(name, mcLoc("item/template_spawn_egg"));
    }

    private void doorItemFromTextures(String doorItemName) {
        // Expects minecraft textures:
        // - textures/block/<doorItemName>_bottom.png
        // - textures/block/<doorItemName>_top.png
        ResourceLocation top = mcLoc("block/" + doorItemName + "_top");
        ResourceLocation bottom = mcLoc("block/" + doorItemName + "_bottom");

        withExistingParent(doorItemName, mcLoc("item/door_base"))
                .texture("particle", top)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    private void generatedItem(String name) {
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", mcLoc("item/" + name));
    }

    private void parentItemToBlockModel(String itemName, String blockModelPath) {
        getBuilder(itemName).parent(new ModelFile.UncheckedModelFile(mcLoc(blockModelPath)));
    }

    private void handheldItem(String name) {
        withExistingParent(name, mcLoc("item/handheld"))
                .texture("layer0", mcLoc("item/" + name));
    }
}