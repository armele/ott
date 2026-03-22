package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.color.ModColorSets;
import com.otterly76.ott.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, "ott", existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // Use vanilla dragon_head as parent to inherit its display transforms (GUI, ground, hand, etc.)
        getBuilder(ModItems.DRAGON_SKULL.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile(mcLoc("item/dragon_head")));

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
                    .texture("sign", mcLoc("item/entity/signs/" + setName))
                    .texture("particle", mcLoc("item/entity/signs/" + setName));

            withExistingParent(setName + "_hanging_sign", mcLoc("item/hanging_sign_base"))
                    .texture("sign", mcLoc("item/entity/signs/hanging/" + setName))
                    .texture("particle", mcLoc("item/entity/signs/hanging/" + setName));

            // Boats: inherit vanilla item model geometry, only swap the texture
            withExistingParent(setName + "_boat", mcLoc("item/oak_boat"))
                    .texture("texture", modLoc("item/entity/boat/" + setName));

            withExistingParent(setName + "_chest_boat", mcLoc("item/oak_chest_boat"))
                    .texture("texture", modLoc("item/entity/chest_boat/" + setName));
        });

        ModBlocks.COLOR_SETS.forEach((color, set) -> {
            parentItemToBlockModel(set.concrete().getId().getPath(), "block/" + set.concrete().getId().getPath());
            parentItemToBlockModel(set.terracotta().getId().getPath(), "block/" + set.terracotta().getId().getPath());
            parentItemToBlockModel(set.wool().getId().getPath(), "block/" + set.wool().getId().getPath());
            parentItemToBlockModel(set.concretePowder().getId().getPath(), "block/" + set.concretePowder().getId().getPath());
            getBuilder(set.stainedGlass().getId().getPath())
                    .parent(new ModelFile.UncheckedModelFile(modLoc("block/" + set.stainedGlass().getId().getPath())))
                    .renderType("minecraft:translucent");
            withExistingParent(set.stainedGlassPane().getId().getPath(), mcLoc("item/glass_pane"))
                    .texture("front", modLoc("block/color_set/" + color + "/stained_glass"))
                    .texture("side", modLoc("block/color_set/" + color + "/stained_glass_pane_top"))
                    .renderType("minecraft:translucent");
            parentItemToBlockModel(set.glazedTerracotta().getId().getPath(), "block/" + set.glazedTerracotta().getId().getPath());

            // Shulker box item: use template to inherit transforms and standard renderer
            withExistingParent(set.shulkerBox().getId().getPath(), mcLoc("item/template_shulker_box"));

            // Candle item: parent to block model
            parentItemToBlockModel(set.candle().getId().getPath(), "block/" + color + "_candle_one_candle");

            // Carpet item: parent to block model
            parentItemToBlockModel(set.carpet().getId().getPath(), "block/" + set.carpet().getId().getPath());

            // Banner item: extend the vanilla banner template
            withExistingParent(set.banner().getId().getPath(), mcLoc("item/template_banner"));

            // Bed item: extend the vanilla bed item model to use standard renderer
            withExistingParent(set.bed().getId().getPath(), mcLoc("item/template_bed"));
        });

        generatedItemFromTexture(ModItems.TINY_COAL.getId().getPath(), modLoc("item/tiny_coal"));
        generatedItemFromTexture(ModItems.TINY_CHARCOAL.getId().getPath(), modLoc("item/tiny_charcoal"));
        generatedItemFromTexture(ModItems.CLAM.getId().getPath(), modLoc("item/clam"));
        generatedItemFromTexture(ModItems.DRAGONFLY_WING.getId().getPath(), modLoc("item/dragonfly_wing"));
        generatedItemFromTexture(ModItems.KOI_FISH.getId().getPath(), modLoc("item/koi_fish"));
        generatedItemFromTexture(ModItems.PEARL.getId().getPath(), modLoc("item/pearl"));
        generatedItemFromTexture(ModItems.SILK.getId().getPath(), modLoc("item/silk"));
        generatedItemFromTexture(ModItems.SEA_BUNNY_SLIME_BOTTLE.getId().getPath(), modLoc("item/sea_bunny_slime_bottle"));
        
        generatedItemFromTexture(ModItems.RAW_CAPYBARA.getId().getPath(), modLoc("item/raw_capybara_meat"));
        generatedItemFromTexture(ModItems.COOKED_CAPYBARA.getId().getPath(), modLoc("item/cooked_capybara_meat"));
        generatedItemFromTexture(ModItems.RAW_HEDGEHOG.getId().getPath(), modLoc("item/raw_hedgehog_meat"));
        generatedItemFromTexture(ModItems.COOKED_HEDGEHOG.getId().getPath(), modLoc("item/cooked_hedgehog_meat"));
        generatedItemFromTexture(ModItems.RAW_SEAL.getId().getPath(), modLoc("item/raw_seal_meat"));
        generatedItemFromTexture(ModItems.COOKED_SEAL.getId().getPath(), modLoc("item/cooked_seal_meat"));
        generatedItemFromTexture(ModItems.RAW_KIWI.getId().getPath(), modLoc("item/raw_kiwi_meat"));
        generatedItemFromTexture(ModItems.COOKED_KIWI.getId().getPath(), modLoc("item/cooked_kiwi_meat"));
        generatedItemFromTexture(ModItems.RAW_SHRIMP_1.getId().getPath(), modLoc("item/raw_shrimp_1"));
        generatedItemFromTexture(ModItems.COOKED_SHRIMP_1.getId().getPath(), modLoc("item/cooked_shrimp_1"));
        generatedItemFromTexture(ModItems.JELLYFISH_JELLY.getId().getPath(), modLoc("item/jellyfish_jelly"));
        generatedItemFromTexture(ModItems.SEA_URCHIN_CAVIAR.getId().getPath(), modLoc("item/sea_urchin_caviar"));
        generatedItemFromTexture(ModItems.KIWI_EGG.getId().getPath(), modLoc("item/kiwi_egg"));
        generatedItemFromTexture(ModItems.QUILL.getId().getPath(), modLoc("item/quill"));
        generatedItemFromTexture(ModItems.QUILL_ARROW.getId().getPath(), modLoc("item/quill_arrow"));

        // Fish and Meat
        generatedItemFromTexture(ModItems.BASS.getId().getPath(), modLoc("item/bass"));
        generatedItemFromTexture(ModItems.COOKED_BASS.getId().getPath(), modLoc("item/cooked_bass"));
        generatedItemFromTexture(ModItems.CATFISH.getId().getPath(), modLoc("item/catfish"));
        generatedItemFromTexture(ModItems.COOKED_CATFISH.getId().getPath(), modLoc("item/cooked_catfish"));
        generatedItemFromTexture(ModItems.VENISON.getId().getPath(), modLoc("item/venison"));
        generatedItemFromTexture(ModItems.COOKED_VENISON.getId().getPath(), modLoc("item/cooked_venison"));
        generatedItemFromTexture(ModItems.PHEASANT.getId().getPath(), modLoc("item/pheasant"));
        generatedItemFromTexture(ModItems.COOKED_PHEASANT.getId().getPath(), modLoc("item/cooked_pheasant"));
        generatedItemFromTexture(ModItems.BUSHMEAT.getId().getPath(), modLoc("item/bushmeat"));
        generatedItemFromTexture(ModItems.COOKED_BUSHMEAT.getId().getPath(), modLoc("item/cooked_bushmeat"));
        generatedItemFromTexture(ModItems.LIZARD_TAIL.getId().getPath(), modLoc("item/lizard_tail"));
        generatedItemFromTexture(ModItems.COOKED_LIZARD_TAIL.getId().getPath(), modLoc("item/lizard_tail")); // Use placeholder
        generatedItemFromTexture(ModItems.COOKED_EGG.getId().getPath(), mcLoc("item/egg")); // Use placeholder
        generatedItemFromTexture(ModItems.RAW_SUNFISH_MEAT.getId().getPath(), modLoc("item/raw_sunfish_meat"));
        generatedItemFromTexture(ModItems.COOKED_SUNFISH_MEAT.getId().getPath(), modLoc("item/cooked_sunfish_meat"));
        generatedItemFromTexture(ModItems.RAW_GOLDEN_SUNFISH_MEAT.getId().getPath(), modLoc("item/raw_golden_sunfish_meat"));
        generatedItemFromTexture(ModItems.COOKED_GOLDEN_SUNFISH_MEAT.getId().getPath(), modLoc("item/cooked_golden_sunfish_meat"));
        generatedItemFromTexture(ModItems.RAW_KRILL.getId().getPath(), modLoc("item/raw_krill"));
        generatedItemFromTexture(ModItems.FRIED_KRILL.getId().getPath(), modLoc("item/fried_krill"));

        // More Eggs
        // generatedItemFromTexture(ModItems.BLUE_EGG.getId().getPath(), modLoc("item/blue_egg"));
        // generatedItemFromTexture(ModItems.BROWN_EGG.getId().getPath(), modLoc("item/brown_egg"));
        generatedItemFromTexture(ModItems.DUCK_EGG.getId().getPath(), modLoc("item/duck_egg"));
        generatedItemFromTexture(ModItems.PHEASANT_EGG.getId().getPath(), modLoc("item/pheasant_egg"));
        generatedItemFromTexture(ModItems.PENGUIN_EGG.getId().getPath(), modLoc("item/penguin_egg"));
        generatedItemFromTexture(ModItems.EMU_EGG.getId().getPath(), modLoc("item/emu_egg"));
        generatedItemFromTexture(ModItems.HOOPOE_EGG.getId().getPath(), modLoc("item/hoopoe_egg"));
        generatedItemFromTexture(ModItems.TOUCAN_EGG.getId().getPath(), modLoc("item/toucan_egg"));

        getBuilder(ModItems.TORCH_ARROW.getId().getPath()).parent(new ModelFile.UncheckedModelFile(mcLoc("item/tipped_arrow")));
        
        generatedItemFromTexture(ModItems.CAPYBARA_SPAWN_EGG.getId().getPath(), modLoc("item/capybara_spawn_egg"));
        generatedItemFromTexture(ModItems.HEDGEHOG_SPAWN_EGG.getId().getPath(), modLoc("item/hedgehog_spawn_egg"));
        generatedItemFromTexture(ModItems.JELLYFISH_SPAWN_EGG.getId().getPath(), modLoc("item/jellyfish_spawn_egg"));
        generatedItemFromTexture(ModItems.KIWI_SPAWN_EGG.getId().getPath(), modLoc("item/kiwi_spawn_egg"));
        generatedItemFromTexture(ModItems.PENGUIN_SPAWN_EGG.getId().getPath(), modLoc("item/penguin_spawn_egg"));
        generatedItemFromTexture(ModItems.SEAL_SPAWN_EGG.getId().getPath(), modLoc("item/seal_spawn_egg"));
        generatedItemFromTexture(ModItems.SEA_URCHIN_SPAWN_EGG.getId().getPath(), modLoc("item/sea_urchin_spawn_egg"));

        spawnEggItem(ModItems.SEAHORSE_1_SPAWN_EGG);
        spawnEggItem(ModItems.SHRIMP_1_SPAWN_EGG);
        spawnEggItem(ModItems.STARFISH_1_SPAWN_EGG);
        spawnEggItem(ModItems.JELLYFISH_2_SPAWN_EGG);
        spawnEggItem(ModItems.JELLYFISH_3_SPAWN_EGG);

        spawnEggItem(ModItems.ANGELFISH_SPAWN_EGG);
        spawnEggItem(ModItems.BARRELEYE_SPAWN_EGG);
        spawnEggItem(ModItems.EMU_SPAWN_EGG);
        spawnEggItem(ModItems.FLOUNDER_SPAWN_EGG);
        spawnEggItem(ModItems.GECKO_SPAWN_EGG);
        spawnEggItem(ModItems.GOOSE_SPAWN_EGG);
        spawnEggItem(ModItems.HOOPOE_SPAWN_EGG);
        spawnEggItem(ModItems.KRILL_SPAWN_EGG);
        spawnEggItem(ModItems.MAMMOTH_SPAWN_EGG);
        spawnEggItem(ModItems.MAN_O_WAR_SPAWN_EGG);
        spawnEggItem(ModItems.MARINE_IGUANA_SPAWN_EGG);
        spawnEggItem(ModItems.MOOSE_SPAWN_EGG);
        spawnEggItem(ModItems.PHEASANT_SPAWN_EGG);
        spawnEggItem(ModItems.STINGRAY_SPAWN_EGG);
        spawnEggItem(ModItems.SUNFISH_SPAWN_EGG);
        spawnEggItem(ModItems.TOUCAN_SPAWN_EGG);
        spawnEggItem(ModItems.MYCELIUM_MAMMOTH_SPAWN_EGG);

        spawnEggItem(ModItems.DRAGONFLY_SPAWN_EGG);
        spawnEggItem(ModItems.DUMBO_OCTOPUS_SPAWN_EGG);
        spawnEggItem(ModItems.FERRET_SPAWN_EGG);
        spawnEggItem(ModItems.JUMPING_SPIDER_SPAWN_EGG);
        spawnEggItem(ModItems.KOI_FISH_SPAWN_EGG);
        spawnEggItem(ModItems.OTTER_SPAWN_EGG);
        spawnEggItem(ModItems.RED_PANDA_SPAWN_EGG);
        spawnEggItem(ModItems.SEA_BUNNY_SPAWN_EGG);

        generatedItemFromTexture(ModItems.DUMBO_OCTOPUS_BUCKET.getId().getPath(), modLoc("item/dumbo_octopus_bucket"));
        generatedItemFromTexture(ModItems.KOI_FISH_BUCKET.getId().getPath(), modLoc("item/koi_fish_bucket"));
        generatedItemFromTexture(ModItems.SEA_BUNNY_BUCKET.getId().getPath(), modLoc("item/sea_bunny_bucket"));

        parentItemToBlockModel(ModItems.SILK_COCOON.getId().getPath(), "block/silk_cocoon");
        parentItemToBlockModel(ModItems.SEA_BUNNY_SLIME_BLOCK.getId().getPath(), "block/sea_bunny_slime_block");

        // Custom Dyes
        ModColorSets.ALL.forEach(colorSet -> {
            String color = colorSet.name();
            getBuilder(color + "_dye").parent(getExistingFile(mcLoc("item/glass_bottle")))
                    .texture("0", modLoc("item/color_set/" + color));
        });
    }

    private void generatedItemFromTexture(String itemName, ResourceLocation texture) {
        withExistingParent(itemName, mcLoc("item/generated"))
                .texture("layer0", texture);
    }

    private void spawnEggItem(DeferredItem<Item> item) {
        withExistingParent(item.getId().getPath(), mcLoc("item/template_spawn_egg"));
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