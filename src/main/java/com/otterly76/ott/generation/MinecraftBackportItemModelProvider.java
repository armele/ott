package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.CopperChestBlock;
import com.otterly76.ott.block.custom.CopperGolemStatueBlock;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.world.item.ItemDisplayContext;
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
            if (path.equals("wildflowers") || path.equals("leaf_litter") || path.equals("dried_ghast") || path.equals("potted_pale_oak_sapling")) {
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

            if (block instanceof CopperChestBlock) {
                getBuilder(path).parent(new ModelFile.UncheckedModelFile(mcLoc("builtin/entity")))
                        .guiLight(BlockModel.GuiLight.FRONT)
                        .transforms()
                        .transform(ItemDisplayContext.GUI)
                        .rotation(30, 45, 0)
                        .scale(0.625f, 0.625f, 0.625f)
                        .end()
                        .transform(ItemDisplayContext.GROUND)
                        .translation(0, 3, 0)
                        .scale(0.25f, 0.25f, 0.25f)
                        .end()
                        .transform(ItemDisplayContext.HEAD)
                        .rotation(0, 180, 0)
                        .end()
                        .transform(ItemDisplayContext.FIXED)
                        .rotation(0, 180, 0)
                        .scale(0.5f, 0.5f, 0.5f)
                        .end()
                        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                        .rotation(75, 315, 0)
                        .translation(0, 2.5f, 0)
                        .scale(0.375f, 0.375f, 0.375f)
                        .end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                        .rotation(0, 315, 0)
                        .scale(0.4f, 0.4f, 0.4f)
                        .end();
                return;
            }

            if (block instanceof CopperGolemStatueBlock) {
                statueItem(path);
                return;
            }

            if (block instanceof IronBarsBlock) {
                String textureName = path.replace("waxed_", "");
                getBuilder(path).parent(getExistingFile(mcLoc("item/iron_bars")))
                        .texture("bars", mcLoc("block/" + textureName))
                        .texture("particle", mcLoc("block/" + textureName));
                return;
            }

            if (path.contains("copper_chain") && !path.contains("chainmail")) {
                String textureName = path.replace("waxed_", "");
                getBuilder(path).parent(getExistingFile(mcLoc("item/chain")))
                        .texture("1", mcLoc("block/" + textureName))
                        .texture("particle", mcLoc("block/" + textureName));
                return;
            }

            if (block instanceof LanternBlock || block instanceof TorchBlock) {
                // Requested display settings
                getBuilder(path).parent(getExistingFile(mcLoc("block/" + path)))
                        .transforms()
                        .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                        .rotation(60, 45, 0)
                        .translation(0, 2, 2)
                        .scale(0.375f, 0.375f, 0.375f)
                        .end()
                        .transform(ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                        .rotation(60, 45, 0)
                        .translation(0, 2, 2)
                        .scale(0.375f, 0.375f, 0.375f)
                        .end()
                        .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                        .rotation(0, 45, 0)
                        .translation(0, 3.5f, 0)
                        .scale(0.4f, 0.4f, 0.4f)
                        .end()
                        .transform(ItemDisplayContext.FIRST_PERSON_LEFT_HAND)
                        .rotation(0, 45, 0)
                        .translation(0, 3.5f, 0)
                        .scale(0.4f, 0.4f, 0.4f)
                        .end()
                        .transform(ItemDisplayContext.GUI)
                        .rotation(30, 225, 0)
                        .translation(0, 2, 0)
                        .scale(0.9f, 0.9f, 0.9f)
                        .end()
                        .transform(ItemDisplayContext.GROUND)
                        .translation(0, 3, 0)
                        .scale(0.25f, 0.25f, 0.25f)
                        .end()
                        .transform(ItemDisplayContext.FIXED)
                        .translation(0, 2, 0)
                        .scale(0.5f, 0.5f, 0.5f)
                        .end();
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

            if (block instanceof ChainBlock) {
                parentItemToBlockModel(path, "block/" + path);
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

        generatedItem(ModItems.NETHERITE_HORSE_ARMOR.getId().getPath());

        // Copper Tools
        handheldItem(ModItems.COPPER_SWORD.getId().getPath());
        handheldItem(ModItems.COPPER_SHOVEL.getId().getPath());
        handheldItem(ModItems.COPPER_PICKAXE.getId().getPath());
        handheldItem(ModItems.COPPER_AXE.getId().getPath());
        handheldItem(ModItems.COPPER_HOE.getId().getPath());
        generatedItem(ModItems.COPPER_SHEARS.getId().getPath());

        handheldItem(ModItems.EXPOSED_COPPER_SWORD.getId().getPath());
        handheldItem(ModItems.EXPOSED_COPPER_SHOVEL.getId().getPath());
        handheldItem(ModItems.EXPOSED_COPPER_PICKAXE.getId().getPath());
        handheldItem(ModItems.EXPOSED_COPPER_AXE.getId().getPath());
        handheldItem(ModItems.EXPOSED_COPPER_HOE.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_SHEARS.getId().getPath());

        handheldItem(ModItems.WEATHERED_COPPER_SWORD.getId().getPath());
        handheldItem(ModItems.WEATHERED_COPPER_SHOVEL.getId().getPath());
        handheldItem(ModItems.WEATHERED_COPPER_PICKAXE.getId().getPath());
        handheldItem(ModItems.WEATHERED_COPPER_AXE.getId().getPath());
        handheldItem(ModItems.WEATHERED_COPPER_HOE.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_SHEARS.getId().getPath());

        handheldItem(ModItems.OXIDIZED_COPPER_SWORD.getId().getPath());
        handheldItem(ModItems.OXIDIZED_COPPER_SHOVEL.getId().getPath());
        handheldItem(ModItems.OXIDIZED_COPPER_PICKAXE.getId().getPath());
        handheldItem(ModItems.OXIDIZED_COPPER_AXE.getId().getPath());
        handheldItem(ModItems.OXIDIZED_COPPER_HOE.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_SHEARS.getId().getPath());

        // Copper Armor
        generatedItem(ModItems.COPPER_HELMET.getId().getPath());
        generatedItem(ModItems.COPPER_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.COPPER_LEGGINGS.getId().getPath());
        generatedItem(ModItems.COPPER_BOOTS.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_HELMET.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_LEGGINGS.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_BOOTS.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_HELMET.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_LEGGINGS.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_BOOTS.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_HELMET.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_LEGGINGS.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_BOOTS.getId().getPath());
        
        // Copper Chainmail Armor
        generatedItem(ModItems.COPPER_CHAINMAIL_HELMET.getId().getPath());
        generatedItem(ModItems.COPPER_CHAINMAIL_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.COPPER_CHAINMAIL_LEGGINGS.getId().getPath());
        generatedItem(ModItems.COPPER_CHAINMAIL_BOOTS.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_CHAINMAIL_HELMET.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_CHAINMAIL_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_CHAINMAIL_LEGGINGS.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_CHAINMAIL_BOOTS.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_CHAINMAIL_HELMET.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_CHAINMAIL_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_CHAINMAIL_LEGGINGS.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_CHAINMAIL_BOOTS.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_CHAINMAIL_HELMET.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_CHAINMAIL_CHESTPLATE.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_CHAINMAIL_LEGGINGS.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_CHAINMAIL_BOOTS.getId().getPath());

        generatedItem(ModItems.COPPER_HORSE_ARMOR.getId().getPath());
        generatedItem(ModItems.EXPOSED_COPPER_HORSE_ARMOR.getId().getPath());
        generatedItem(ModItems.WEATHERED_COPPER_HORSE_ARMOR.getId().getPath());
        generatedItem(ModItems.OXIDIZED_COPPER_HORSE_ARMOR.getId().getPath());
        generatedItem(ModItems.COPPER_NUGGET.getId().getPath());

        generatedItem(ModItems.COPPER_BUCKET.getId().getPath());
        generatedItem(ModItems.COPPER_WATER_BUCKET.getId().getPath());
        generatedItem(ModItems.COPPER_LAVA_BUCKET.getId().getPath());
        generatedItem(ModItems.COPPER_MILK_BUCKET.getId().getPath());
        generatedItem(ModItems.COPPER_POWDER_SNOW_BUCKET.getId().getPath());

        // Lightning Rods & Grates
        String[] copperStates = {"", "exposed_", "weathered_", "oxidized_"};
        for (String state : copperStates) {
            parentItemToBlockModel(state + "lightning_rod", "block/" + state + "lightning_rod");
            parentItemToBlockModel("waxed_" + state + "lightning_rod", "block/waxed_" + state + "lightning_rod");

            parentItemToBlockModel(state + "copper_grate", "block/" + state + "copper_grate");
            parentItemToBlockModel("waxed_" + state + "copper_grate", "block/waxed_" + state + "copper_grate");

            getBuilder(state + "copper_hopper").parent(getExistingFile(modLoc("item/copper_hopper_item_base")))
                    .texture("top", mcLoc("block/" + state + "copper_hopper_top"))
                    .texture("inside", mcLoc("block/" + state + "copper_hopper_inside"))
                    .texture("outside", mcLoc("block/" + state + "copper_hopper_outside"))
                    .texture("particle", mcLoc("block/" + state + "copper_hopper_outside"));
            getBuilder("waxed_" + state + "copper_hopper").parent(getExistingFile(modLoc("item/copper_hopper_item_base")))
                    .texture("top", mcLoc("block/" + state + "copper_hopper_top"))
                    .texture("inside", mcLoc("block/" + state + "copper_hopper_inside"))
                    .texture("outside", mcLoc("block/" + state + "copper_hopper_outside"))
                    .texture("particle", mcLoc("block/" + state + "copper_hopper_outside"));

            String ladderTextureName = "block/" + state + "copper_ladder";
            generatedItem(state + "copper_ladder", ladderTextureName);
            generatedItem("waxed_" + state + "copper_ladder", ladderTextureName);

            parentItemToBlockModel(state + "copper_cauldron", "block/" + state + "copper_cauldron");
            parentItemToBlockModel("waxed_" + state + "copper_cauldron", "block/waxed_" + state + "copper_cauldron");

            String railTextureName = "block/" + state + "copper_rail";
            generatedItem(state + "copper_rail", railTextureName);
            generatedItem("waxed_" + state + "copper_rail", railTextureName);
        }

        spawnEggItem(ModItems.COPPER_GOLEM_SPAWN_EGG.getId().getPath());

        withExistingParent(ModItems.PALE_OAK_BOAT.getId().getPath(), mcLoc("item/oak_boat"))
                .texture("texture", mcLoc("item/entity/boat/pale_oak"));
        withExistingParent(ModItems.PALE_OAK_CHEST_BOAT.getId().getPath(), mcLoc("item/oak_chest_boat"))
                .texture("texture", mcLoc("item/entity/chest_boat/pale_oak"));

        String[] vanillaDyes = {
                "black_dye", "blue_dye", "brown_dye", "cyan_dye", "gray_dye", "green_dye",
                "light_blue_dye", "light_gray_dye", "lime_dye", "magenta_dye", "orange_dye",
                "pink_dye", "purple_dye", "red_dye", "white_dye", "yellow_dye"
        };
        for (String dye : vanillaDyes) {
            String color = dye.replace("_dye", "");
            ResourceLocation texture = color.equals("white") ? mcLoc("item/white_dye") : ResourceLocation.fromNamespaceAndPath("ott", "item/color_set/" + color);
            getBuilder(dye).parent(getExistingFile(mcLoc("item/glass_bottle")))
                    .texture("0", texture);
        }
    }

    private void statueItem(String name) {
        getBuilder(name).parent(new ModelFile.UncheckedModelFile(mcLoc("builtin/entity")))
                .guiLight(BlockModel.GuiLight.FRONT)
                .transforms()
                .transform(ItemDisplayContext.GUI)
                .rotation(30, 45, 0)
                .translation(0, -2, 0)
                .scale(0.58f, 0.58f, 0.58f)
                .end()
                .transform(ItemDisplayContext.GROUND)
                .rotation(0, 0, 0)
                .translation(0, 2, 0)
                .scale(0.4f, 0.4f, 0.4f)
                .end()
                .transform(ItemDisplayContext.FIXED)
                .rotation(0, 180, 0)
                .translation(0, -1, 0)
                .scale(0.6f, 0.6f, 0.6f)
                .end()
                .transform(ItemDisplayContext.THIRD_PERSON_RIGHT_HAND)
                .rotation(0, -50, 0)
                .translation(0, 1, 0)
                .scale(0.47f, 0.47f, 0.47f)
                .end()
                .transform(ItemDisplayContext.FIRST_PERSON_RIGHT_HAND)
                .rotation(0, 315, 0)
                .translation(0, 1, 0)
                .scale(0.5f, 0.5f, 0.5f)
                .end();
    }

    private void spawnEggItem(String name) {
        withExistingParent(name, mcLoc("item/template_spawn_egg"));
    }

    private void doorItemFromTextures(String doorItemName) {
        // Expects minecraft textures:
        // - textures/block/<doorItemName>_bottom.png
        // - textures/block/<doorItemName>_top.png
        String textureName = doorItemName.startsWith("waxed_") ? doorItemName.substring(6) : doorItemName;
        ResourceLocation top = mcLoc("block/" + textureName + "_top");
        ResourceLocation bottom = mcLoc("block/" + textureName + "_bottom");

        withExistingParent(doorItemName, mcLoc("item/door_base"))
                .texture("particle", top)
                .texture("bottom", bottom)
                .texture("top", top);
    }

    private void generatedItem(String name) {
        generatedItem(name, name);
    }

    private void generatedItem(String name, String textureName) {
        String path = textureName.contains("/") ? textureName : "item/" + textureName;
        withExistingParent(name, mcLoc("item/generated"))
                .texture("layer0", mcLoc(path));
    }

    private void parentItemToBlockModel(String itemName, String blockModelPath) {
        getBuilder(itemName).parent(new ModelFile.UncheckedModelFile(mcLoc(blockModelPath)));
    }

    private void handheldItem(String name) {
        String path = name.contains("/") ? name : "item/" + name;
        withExistingParent(name, mcLoc("item/handheld"))
                .texture("layer0", mcLoc(path));
    }
}