package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "ott", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent(
                ModItems.CREAKING_SPAWN_EGG.getId().getPath(),
                mcLoc("item/template_spawn_egg")
        );

        ModBlocks.WOOD_SETS.forEach((setName, set) -> {
            parentItemToBlockModel(set.log().getId().getPath(), "block/" + set.log().getId().getPath());
            parentItemToBlockModel(set.wood().getId().getPath(), "block/" + set.wood().getId().getPath());
            parentItemToBlockModel(set.strippedLog().getId().getPath(), "block/" + set.strippedLog().getId().getPath());
            parentItemToBlockModel(set.strippedWood().getId().getPath(), "block/" + set.strippedWood().getId().getPath());

            // planks model is generated as block/<set>_planks.json (flat)
            parentItemToBlockModel(set.planks().getId().getPath(), "block/" + set.planks().getId().getPath());

            parentItemToBlockModel(set.stairs().getId().getPath(), "block/" + set.stairs().getId().getPath());
            parentItemToBlockModel(set.slab().getId().getPath(), "block/" + set.slab().getId().getPath());

            parentItemToBlockModel(set.fence().getId().getPath(), "block/" + set.fence().getId().getPath() + "_inventory");
            parentItemToBlockModel(set.fenceGate().getId().getPath(), "block/" + set.fenceGate().getId().getPath());

            // Buttons: item should use the *_inventory block model (vanilla behavior)
            parentItemToBlockModel(set.button().getId().getPath(), "block/" + set.button().getId().getPath() + "_inventory");
            parentItemToBlockModel(set.pressurePlate().getId().getPath(), "block/" + set.pressurePlate().getId().getPath());

            // Doors: item uses item/door_base (3D held item)
            doorItemFromTextures(setName, set.door().getId());

            // trapdoor uses the usual *_bottom model which you have
            parentItemToBlockModel(set.trapdoor().getId().getPath(), "block/" + set.trapdoor().getId().getPath() + "_bottom");

            parentItemToBlockModel(set.leaves().getId().getPath(), "block/" + set.leaves().getId().getPath());

            // Signs: use vanilla 3D sign item models, swap textures
            withExistingParent(setName + "_sign", mcLoc("item/sign_base"))
                    .texture("sign", modLoc("item/entity/signs/" + setName))
                    .texture("particle", modLoc("item/entity/signs/" + setName));

            withExistingParent(setName + "_hanging_sign", mcLoc("item/hanging_sign_base"))
                    .texture("sign", modLoc("item/entity/signs/hanging/" + setName))
                    .texture("particle", modLoc("item/entity/signs/hanging/" + setName));

            // Boats: inherit vanilla item model geometry, only swap the texture
            withExistingParent(setName + "_boat", mcLoc("item/oak_boat"))
                    .texture("texture", modLoc("item/entity/boat/" + setName));

            withExistingParent(setName + "_chest_boat", mcLoc("item/oak_chest_boat"))
                    .texture("texture", modLoc("item/entity/chest_boat/" + setName));
        });

        generatedItemFromTexture(ModItems.TINY_COAL.getId().getPath(), modLoc("item/tiny_coal"));
        generatedItemFromTexture(ModItems.TINY_CHARCOAL.getId().getPath(), modLoc("item/tiny_charcoal"));
        
        withExistingParent(ModItems.TORCH_ARROW.getId().getPath(), mcLoc("item/tipped_arrow"));
    }

    private void generatedItemFromTexture(String itemName, ResourceLocation texture) {
        withExistingParent(itemName, mcLoc("item/generated"))
                .texture("layer0", texture);
    }

    private void parentItemToBlockModel(String itemName, String blockModelPath) {
        getBuilder(itemName).parent(new ModelFile.UncheckedModelFile(modLoc(blockModelPath)));
    }

    private void doorItemFromTextures(String setName, ResourceLocation doorId) {
        String itemName = doorId.getPath();

        ResourceLocation top = modLoc("block/wood/" + setName + "/door_top");
        ResourceLocation bottom = modLoc("block/wood/" + setName + "/door_bottom");

        withExistingParent(itemName, mcLoc("item/door_base"))
                .texture("particle", top)
                .texture("bottom", bottom)
                .texture("top", top);
    }
}