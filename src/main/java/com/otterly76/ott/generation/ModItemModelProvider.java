package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.color.ModColorSets;
import com.otterly76.ott.color.ModPatterns;
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
            String woodDir = "block/" + setName + "/";
            parentItemToBlockModel(set.log().getId().getPath(), woodDir + set.log().getId().getPath());
            parentItemToBlockModel(set.wood().getId().getPath(), woodDir + set.wood().getId().getPath());
            parentItemToBlockModel(set.strippedLog().getId().getPath(), woodDir + set.strippedLog().getId().getPath());
            parentItemToBlockModel(set.strippedWood().getId().getPath(), woodDir + set.strippedWood().getId().getPath());

            parentItemToBlockModel(set.planks().getId().getPath(), woodDir + set.planks().getId().getPath());

            parentItemToBlockModel(set.stairs().getId().getPath(), woodDir + set.stairs().getId().getPath());
            parentItemToBlockModel(set.slab().getId().getPath(), woodDir + set.slab().getId().getPath());

            parentItemToBlockModel(set.fence().getId().getPath(), woodDir + set.fence().getId().getPath() + "_inventory");
            parentItemToBlockModel(set.fenceGate().getId().getPath(), woodDir + set.fenceGate().getId().getPath());

            // Buttons: item should use the *_inventory block model (vanilla behavior)
            parentItemToBlockModel(set.button().getId().getPath(), woodDir + set.button().getId().getPath() + "_inventory");
            parentItemToBlockModel(set.pressurePlate().getId().getPath(), woodDir + set.pressurePlate().getId().getPath());

            // Doors: item uses item/door_base (3D held item)
            doorItemFromTextures(setName, set.door().getId());

            // trapdoor uses the usual *_bottom model which you have
            parentItemToBlockModel(set.trapdoor().getId().getPath(), woodDir + set.trapdoor().getId().getPath() + "_bottom");

            parentItemToBlockModel(set.leaves().getId().getPath(), woodDir + set.leaves().getId().getPath());

            ResourceLocation beamTex  = modLoc("block/beam/"    + setName + "_beam");
            ResourceLocation pergTex  = modLoc("block/pergola/" + setName + "_pergola");
            ResourceLocation plankTex = modLoc("block/wood/" + setName + "/planks");
            withExistingParent(set.beam().getId().getPath(),    modLoc("item/template_beam"))
                    .texture("0", beamTex).texture("particle", beamTex);
            withExistingParent(set.pergola().getId().getPath(), modLoc("item/template_pergola"))
                    .texture("0", pergTex).texture("particle", pergTex);
            withExistingParent(set.supportBeam().getId().getPath(), modLoc("item/template_support_beam"))
                    .texture("texture", beamTex).texture("slab", plankTex).texture("particle", beamTex);
            parentItemToBlockModel(set.planksPlate().getId().getPath(), "block/" + setName + "/" + set.planksPlate().getId().getPath());
            parentItemToBlockModel(set.planksEdge().getId().getPath(),  "block/" + setName + "/" + set.planksEdge().getId().getPath());
            parentItemToBlockModel(set.bannister().getId().getPath(),    "block/" + setName + "/" + set.bannister().getId().getPath());
            parentItemToBlockModel(set.supportSlab().getId().getPath(), "block/" + setName + "/" + set.supportSlab().getId().getPath());

            // Signs: use vanilla 3D sign item models, swap textures
            withExistingParent(setName + "_sign", mcLoc("item/sign_base"))
                    .texture("sign", mcLoc("item/entity/signs/" + setName))
                    .texture("particle", mcLoc("item/entity/signs/" + setName));

            withExistingParent(setName + "_hanging_sign", mcLoc("item/hanging_sign_base"))
                    .texture("sign", mcLoc("item/entity/signs/hanging/" + setName))
                    .texture("particle", mcLoc("item/entity/signs/hanging/" + setName));

            // Boats: parent to our template which has builtin/entity + display transforms.
            // This makes isCustomRenderer()=true so our ModBoatItemRenderer is called with the correct wood texture.
            withExistingParent(setName + "_boat", modLoc("item/boat_template"));
            withExistingParent(setName + "_chest_boat", modLoc("item/chest_boat_template"));

            parentItemToBlockModel(set.beehive().getId().getPath(), "block/beehive/" + setName + "_beehive");
            parentItemToBlockModel(set.shelf().getId().getPath(), "block/" + setName + "/" + setName + "_shelf_inventory");
        });

        // Oak structural item models are pre-existing hand-written files; skip to avoid duplicates.
        ModBlocks.VANILLA_STRUCTURAL_SETS.forEach((setName, set) -> {
            if (setName.equals("oak")) return;
            ResourceLocation beamTex = modLoc("block/beam/"    + setName + "_beam");
            ResourceLocation pergTex = modLoc("block/pergola/" + setName + "_pergola");
            ResourceLocation plankTex = ResourceLocation.withDefaultNamespace("block/" + setName + "_planks");
            withExistingParent(set.beam().getId().getPath(),    modLoc("item/template_beam"))
                    .texture("0", beamTex).texture("particle", beamTex);
            withExistingParent(set.pergola().getId().getPath(), modLoc("item/template_pergola"))
                    .texture("0", pergTex).texture("particle", pergTex);
            withExistingParent(set.supportBeam().getId().getPath(), modLoc("item/template_support_beam"))
                    .texture("texture", beamTex).texture("slab", plankTex).texture("particle", beamTex);
            parentItemToBlockModel(set.planksPlate().getId().getPath(), "block/" + setName + "/" + set.planksPlate().getId().getPath());
            parentItemToBlockModel(set.planksEdge().getId().getPath(),  "block/" + setName + "/" + set.planksEdge().getId().getPath());
            parentItemToBlockModel(set.bannister().getId().getPath(),    "block/" + setName + "/" + set.bannister().getId().getPath());
            parentItemToBlockModel(set.supportSlab().getId().getPath(), "block/" + setName + "/" + set.supportSlab().getId().getPath());
        });

        ModBlocks.WOOD_DOORS.forEach((wood, styleMap) ->
            styleMap.keySet().forEach(style -> {
                String itemName = style + "_" + wood + "_door";
                withExistingParent(itemName, mcLoc("item/door_base"))
                        .texture("particle", modLoc("block/" + wood + "/" + wood + "_door/" + style + "_" + wood + "_door_top"))
                        .texture("bottom",   modLoc("block/" + wood + "/" + wood + "_door/" + style + "_" + wood + "_door_bottom"))
                        .texture("top",      modLoc("block/" + wood + "/" + wood + "_door/" + style + "_" + wood + "_door_top"));
            })
        );

        ModBlocks.COLOR_SETS.forEach((color, set) -> {
            String colorDir = "block/" + color + "/";
            parentItemToBlockModel(set.concrete().getId().getPath(), colorDir + set.concrete().getId().getPath());
            parentItemToBlockModel(set.terracotta().getId().getPath(), colorDir + set.terracotta().getId().getPath());
            parentItemToBlockModel(set.wool().getId().getPath(), colorDir + set.wool().getId().getPath());
            parentItemToBlockModel(set.concretePowder().getId().getPath(), colorDir + set.concretePowder().getId().getPath());
            getBuilder(set.stainedGlass().getId().getPath())
                    .parent(new ModelFile.UncheckedModelFile(modLoc(colorDir + set.stainedGlass().getId().getPath())))
                    .renderType("minecraft:translucent");
            withExistingParent(set.stainedGlassPane().getId().getPath(), mcLoc("item/glass_pane"))
                    .texture("front", modLoc("block/color_set/" + color + "/stained_glass"))
                    .texture("side", modLoc("block/color_set/" + color + "/stained_glass_pane_top"))
                    .renderType("minecraft:translucent");
            parentItemToBlockModel(set.glazedTerracotta().getId().getPath(), colorDir + set.glazedTerracotta().getId().getPath());

            // Shulker box item: use template to inherit transforms and standard renderer
            withExistingParent(set.shulkerBox().getId().getPath(), mcLoc("item/template_shulker_box"));

            // Candle item: parent to block model
            parentItemToBlockModel(set.candle().getId().getPath(), colorDir + color + "_candle_one_candle");

            // Carpet item: parent to block model
            parentItemToBlockModel(set.carpet().getId().getPath(), colorDir + set.carpet().getId().getPath());

            // Banner item: extend the vanilla banner template
            withExistingParent(set.banner().getId().getPath(), mcLoc("item/template_banner"));

            // Bed item: extend the vanilla bed item model to use standard renderer
            withExistingParent(set.bed().getId().getPath(), mcLoc("item/template_bed"));
        });

        generatedItemFromTexture(ModItems.UNFIRED_CLAY_ROOF_TILE.getId().getPath(), modLoc("block/base/clay_tile"));
        generatedItemFromTexture(ModItems.PLASTER_BUCKET.getId().getPath(), modLoc("item/plaster_bucket"));

        // Clay tile items — 32 tinted colors
        for (ModPatterns.ColorInfo color : ModPatterns.ALL_COLORS) {
            generatedItemFromTexture(color.name() + "_clay_tile", modLoc("item/color_set/" + color.name() + "/clay_tile"));
        }
        generatedItemFromTexture(ModItems.TINY_COAL.getId().getPath(), modLoc("item/tiny_coal"));
        generatedItemFromTexture(ModItems.TINY_CHARCOAL.getId().getPath(), modLoc("item/tiny_charcoal"));
        generatedItemFromTexture(ModItems.CLAM.getId().getPath(), modLoc("item/clam"));
        generatedItemFromTexture(ModItems.KOI_FISH.getId().getPath(), modLoc("item/koi_fish"));
        generatedItemFromTexture(ModItems.PEARL.getId().getPath(), modLoc("item/pearl"));
        generatedItemFromTexture(ModItems.SILK.getId().getPath(), modLoc("item/silk"));
        
        generatedItemFromTexture(ModItems.JELLYFISH_JELLY.getId().getPath(), modLoc("item/jellyfish_jelly"));
        generatedItemFromTexture(ModItems.SEA_URCHIN_CAVIAR.getId().getPath(), modLoc("item/sea_urchin_caviar"));
        generatedItemFromTexture(ModItems.KIWI_EGG.getId().getPath(), modLoc("item/kiwi_egg"));

        generatedItemFromTexture(ModItems.BASS.getId().getPath(), modLoc("item/bass"));
        generatedItemFromTexture(ModItems.COOKED_BASS.getId().getPath(), modLoc("item/cooked_bass"));
        generatedItemFromTexture(ModItems.CATFISH.getId().getPath(), modLoc("item/catfish"));
        generatedItemFromTexture(ModItems.COOKED_CATFISH.getId().getPath(), modLoc("item/cooked_catfish"));
        generatedItemFromTexture(ModItems.LIZARD_TAIL.getId().getPath(), modLoc("item/lizard_tail"));
        generatedItemFromTexture(ModItems.COOKED_LIZARD_TAIL.getId().getPath(), modLoc("item/lizard_tail")); // Use placeholder
        generatedItemFromTexture(ModItems.COOKED_EGG.getId().getPath(), mcLoc("item/egg")); // Use placeholder
        generatedItemFromTexture(ModItems.RAW_SUNFISH_MEAT.getId().getPath(), modLoc("item/raw_sunfish_meat"));
        generatedItemFromTexture(ModItems.COOKED_SUNFISH_MEAT.getId().getPath(), modLoc("item/cooked_sunfish_meat"));
        generatedItemFromTexture(ModItems.RAW_GOLDEN_SUNFISH_MEAT.getId().getPath(), modLoc("item/raw_golden_sunfish_meat"));
        generatedItemFromTexture(ModItems.COOKED_GOLDEN_SUNFISH_MEAT.getId().getPath(), modLoc("item/cooked_golden_sunfish_meat"));
        generatedItemFromTexture(ModItems.RAW_KRILL.getId().getPath(), modLoc("item/raw_krill"));
        generatedItemFromTexture(ModItems.FRIED_KRILL.getId().getPath(), modLoc("item/fried_krill"));

        generatedItemFromTexture(ModItems.RAW_SNAIL.getId().getPath(), modLoc("item/raw_snail"));
        generatedItemFromTexture(ModItems.COOKED_SNAIL.getId().getPath(), modLoc("item/cooked_snail"));
        generatedItemFromTexture(ModItems.RAW_SHRIMP.getId().getPath(), modLoc("item/raw_shrimp_1"));
        generatedItemFromTexture(ModItems.STEAMED_SHRIMP.getId().getPath(), modLoc("item/steamed_shrimp"));
        generatedItemFromTexture(ModItems.RAW_WILD_BIRD_MEAT.getId().getPath(), modLoc("item/wild_bird_meat"));
        generatedItemFromTexture(ModItems.COOKED_WILD_BIRD_MEAT.getId().getPath(), modLoc("item/cooked_wild_bird_meat"));
        generatedItemFromTexture(ModItems.RAW_WILD_GAME_MEAT.getId().getPath(), modLoc("item/raw_wild_game_meat"));
        generatedItemFromTexture(ModItems.COOKED_WILD_GAME_MEAT.getId().getPath(), modLoc("item/cooked_wild_game_meat"));
        generatedItemFromTexture(ModItems.RAW_CRAB_MEAT.getId().getPath(), modLoc("item/raw_crab_meat"));
        generatedItemFromTexture(ModItems.STEAMED_CRAB_MEAT.getId().getPath(), modLoc("item/steamed_crab_meat"));

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
        
        generatedItemFromTexture(ModItems.CAPYBARA_SPAWN_EGG.getId().getPath(), modLoc("item/spawn_eggs/capybara_spawn_egg"));
        generatedItemFromTexture(ModItems.HEDGEHOG_SPAWN_EGG.getId().getPath(), modLoc("item/spawn_eggs/hedgehog_spawn_egg"));
        generatedItemFromTexture(ModItems.LARGE_JELLYFISH_SPAWN_EGG.getId().getPath(), modLoc("item/spawn_eggs/jellyfish_spawn_egg"));
        generatedItemFromTexture(ModItems.KIWI_SPAWN_EGG.getId().getPath(), modLoc("item/spawn_eggs/kiwi_spawn_egg"));
        generatedItemFromTexture(ModItems.PENGUIN_SPAWN_EGG.getId().getPath(), modLoc("item/spawn_eggs/penguin_spawn_egg"));
        generatedItemFromTexture(ModItems.SEAL_SPAWN_EGG.getId().getPath(), modLoc("item/spawn_eggs/seal_spawn_egg"));
        generatedItemFromTexture(ModItems.SEA_URCHIN_SPAWN_EGG.getId().getPath(), modLoc("item/spawn_eggs/sea_urchin_spawn_egg"));

        spawnEggItem(ModItems.SEAHORSE_SPAWN_EGG);
        spawnEggItem(ModItems.ETHEREAL_SHRIMP_SPAWN_EGG);
        spawnEggItem(ModItems.STARFISH_SPAWN_EGG);
        spawnEggItem(ModItems.SMALL_JELLYFISH_SPAWN_EGG);
        spawnEggItem(ModItems.MEDIUM_JELLYFISH_SPAWN_EGG);

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

        spawnEggItem(ModItems.GHOST_SPAWN_EGG);
        spawnEggItem(ModItems.SPECTRE_SPAWN_EGG);
        spawnEggItem(ModItems.HAUNT_SPAWN_EGG);
        spawnEggItem(ModItems.GEIST_SPAWN_EGG);
        spawnEggItem(ModItems.TREE_ENT_SPAWN_EGG);
        spawnEggItem(ModItems.HERMIT_KING_SPAWN_EGG);
        spawnEggItem(ModItems.SEA_VIPER_SPAWN_EGG);
        spawnEggItem(ModItems.YETI_SPAWN_EGG);
        spawnEggItem(ModItems.VILE_GATOR_SPAWN_EGG);
        spawnEggItem(ModItems.PHOENIX_SPAWN_EGG);
        spawnEggItem(ModItems.BABY_PHOENIX_SPAWN_EGG);
        spawnEggItem(ModItems.BONE_STALKER_SPAWN_EGG);
        spawnEggItem(ModItems.SHADOW_SPAWN_EGG);
        spawnEggItem(ModItems.CHERRY_TREE_ENT_SPAWN_EGG);
        spawnEggItem(ModItems.GOLDEN_HERMIT_KING_SPAWN_EGG);
        spawnEggItem(ModItems.CORAL_SEA_VIPER_SPAWN_EGG);
        spawnEggItem(ModItems.ARID_YETI_SPAWN_EGG);
        spawnEggItem(ModItems.WIND_PHOENIX_SPAWN_EGG);
        spawnEggItem(ModItems.BABY_WIND_PHOENIX_SPAWN_EGG);
        spawnEggItem(ModItems.BOGGED_BONE_STALKER_SPAWN_EGG);
        spawnEggItem(ModItems.BOGGED_SHADOW_SPAWN_EGG);
        spawnEggItem(ModItems.GILDED_TREE_ENT_SPAWN_EGG);


        spawnEggItem(ModItems.BEAVER_SPAWN_EGG);
        spawnEggItem(ModItems.CHUPACABRA_SPAWN_EGG);
        spawnEggItem(ModItems.COUGAR_SPAWN_EGG);
        spawnEggItem(ModItems.COYOTE_SPAWN_EGG);
        spawnEggItem(ModItems.HOWLER_SPAWN_EGG);
        spawnEggItem(ModItems.MARMOT_SPAWN_EGG);
        spawnEggItem(ModItems.MOUSE_SPAWN_EGG);
        spawnEggItem(ModItems.PIT_VIPER_SPAWN_EGG);
        spawnEggItem(ModItems.RATTLESNAKE_SPAWN_EGG);
        spawnEggItem(ModItems.RINGTAIL_SPAWN_EGG);
        spawnEggItem(ModItems.SASQUATCH_SPAWN_EGG);
        spawnEggItem(ModItems.SKINWALKER_SPAWN_EGG);
        spawnEggItem(ModItems.SNAKE_SPAWN_EGG);
        spawnEggItem(ModItems.SQUONK_SPAWN_EGG);
        spawnEggItem(ModItems.TURKEY_SPAWN_EGG);
        spawnEggItem(ModItems.WECHUGE_SPAWN_EGG);
        spawnEggItem(ModItems.WENDIGO_SPAWN_EGG);
        spawnEggItem(ModItems.WOLVERINE_SPAWN_EGG);
        spawnEggItem(ModItems.CICHLID_SPAWN_EGG);
        spawnEggItem(ModItems.LEOPARD_CAT_SPAWN_EGG);
        spawnEggItem(ModItems.ECHIDNA_SPAWN_EGG);
        spawnEggItem(ModItems.GUITARFISH_SPAWN_EGG);
        spawnEggItem(ModItems.BONNETHEAD_SHARK_SPAWN_EGG);
        spawnEggItem(ModItems.BURROWING_OWL_SPAWN_EGG);
        spawnEggItem(ModItems.BUSHDOG_SPAWN_EGG);
        spawnEggItem(ModItems.QUAIL_SPAWN_EGG);
        spawnEggItem(ModItems.CANDYCANE_SNAIL_SPAWN_EGG);
        spawnEggItem(ModItems.FIRE_SALAMANDER_SPAWN_EGG);
        spawnEggItem(ModItems.RIVER_TURTLE_SPAWN_EGG);
        spawnEggItem(ModItems.GOBLIN_SHARK_SPAWN_EGG);
        spawnEggItem(ModItems.GUINEA_FOWL_SPAWN_EGG);
        spawnEggItem(ModItems.IMPALA_SPAWN_EGG);
        spawnEggItem(ModItems.MANTA_RAY_SPAWN_EGG);
        spawnEggItem(ModItems.STORK_SPAWN_EGG);
        spawnEggItem(ModItems.MOLE_SPAWN_EGG);
        spawnEggItem(ModItems.TREE_KANGAROO_SPAWN_EGG);
        spawnEggItem(ModItems.PALLAS_CAT_SPAWN_EGG);
        spawnEggItem(ModItems.PINK_LAND_IGUANA_SPAWN_EGG);
        spawnEggItem(ModItems.PSYCHO_JELLY_SPAWN_EGG);
        spawnEggItem(ModItems.SPOONBILL_SPAWN_EGG);
        spawnEggItem(ModItems.GIANT_SOFTSHELL_TURTLE_SPAWN_EGG);
        spawnEggItem(ModItems.FIDDLER_CRAB_SPAWN_EGG);
        spawnEggItem(ModItems.NAUTILUS_SPAWN_EGG);
        spawnEggItem(ModItems.ZOMBIE_NAUTILUS_SPAWN_EGG);
        spawnEggItem(ModItems.PARCHED_SPAWN_EGG);
        spawnEggItem(ModItems.CAMEL_HUSK_SPAWN_EGG);

        generatedItemFromTexture(ModItems.DUMBO_OCTOPUS_BUCKET.getId().getPath(), modLoc("item/dumbo_octopus_bucket"));
        generatedItemFromTexture(ModItems.KOI_FISH_BUCKET.getId().getPath(), modLoc("item/koi_fish_bucket"));
        generatedItemFromTexture(ModItems.BASS_BUCKET.getId().getPath(), modLoc("item/bass_bucket"));
        generatedItemFromTexture(ModItems.CATFISH_BUCKET.getId().getPath(), modLoc("item/catfish_bucket"));
        generatedItemFromTexture(ModItems.STINGRAY_BUCKET.getId().getPath(), modLoc("item/stingray_bucket"));
        generatedItemFromTexture(ModItems.SEA_BUNNY_BUCKET.getId().getPath(), modLoc("item/sea_bunny_bucket"));
        generatedItemFromTexture(ModItems.CICHLID_BUCKET.getId().getPath(), modLoc("item/cichlid_bucket"));
        generatedItemFromTexture(ModItems.GUITARFISH_BUCKET.getId().getPath(), modLoc("item/guitarfish_bucket"));
        generatedItemFromTexture(ModItems.BONNETHEAD_SHARK_BUCKET.getId().getPath(), modLoc("item/bonnethead_shark_bucket"));
        generatedItemFromTexture(ModItems.GOBLIN_SHARK_BUCKET.getId().getPath(), modLoc("item/goblin_shark_bucket"));
        generatedItemFromTexture(ModItems.PSYCHO_JELLY_BUCKET.getId().getPath(), modLoc("item/psycho_jelly_bucket"));
        generatedItemFromTexture(ModItems.LARGE_JELLYFISH_BUCKET.getId().getPath(), modLoc("item/large_jellyfish_bucket"));
        generatedItemFromTexture(ModItems.SMALL_JELLYFISH_BUCKET.getId().getPath(), modLoc("item/small_jellyfish_bucket"));
        generatedItemFromTexture(ModItems.MEDIUM_JELLYFISH_BUCKET.getId().getPath(), modLoc("item/medium_jellyfish_bucket"));
        generatedItemFromTexture(ModItems.SEAHORSE_BUCKET.getId().getPath(), modLoc("item/seahorse_bucket"));
        generatedItemFromTexture(ModItems.ETHEREAL_SHRIMP_BUCKET.getId().getPath(), modLoc("item/shrimp_bucket"));

        generatedItemFromTexture(ModItems.RAW_CICHLID.getId().getPath(), modLoc("item/raw_cichlid"));
        generatedItemFromTexture(ModItems.COOKED_CICHLID.getId().getPath(), modLoc("item/raw_cichlid"));
        generatedItemFromTexture(ModItems.RAW_BONNETHEAD.getId().getPath(), modLoc("item/raw_bonnethead"));
        generatedItemFromTexture(ModItems.COOKED_BONNETHEAD.getId().getPath(), modLoc("item/raw_bonnethead"));
        generatedItemFromTexture(ModItems.RAW_GUITARFISH.getId().getPath(), modLoc("item/raw_guitarfish"));
        generatedItemFromTexture(ModItems.COOKED_GUITARFISH.getId().getPath(), modLoc("item/raw_guitarfish"));
        generatedItemFromTexture(ModItems.RAW_GOBLIN_SHARK.getId().getPath(), modLoc("item/raw_goblin_shark"));
        generatedItemFromTexture(ModItems.COOKED_GOBLIN_SHARK.getId().getPath(), modLoc("item/raw_goblin_shark"));
        
        parentItemToBlockModel(ModItems.SILK_COCOON.getId().getPath(), "block/silk_cocoon");
        parentItemToBlockModel(ModItems.GLASS_JAR.getId().getPath(), "block/glass_jar");

        // Mosaic recess blocks — manual blockstates, item models generated here
        parentItemToBlockModel("water_mosaic_recess",  "block/water_mosaic/water_mosaic_recess");
        parentItemToBlockModel("earth_mosaic_recess",  "block/earth_mosaic/earth_mosaic_recess");
        parentItemToBlockModel("fire_mosaic_recess",   "block/fire_mosaic/fire_mosaic_recess");
        parentItemToBlockModel("spirit_mosaic_recess", "block/spirit_mosaic/spirit_mosaic_recess");
        parentItemToBlockModel("air_mosaic_recess",    "block/air_mosaic/air_mosaic_recess");
        getBuilder(ModItems.BUG_NET.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("item/bug_net_model")));
        getBuilder(ModItems.FIREFLY_IN_A_JAR.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("item/firefly_jar_item_base")));
        getBuilder(ModItems.FIREFLIES_IN_A_JAR.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("item/firefly_jar_item_base")));
        getBuilder(ModItems.FIREFLY_JAR.getId().getPath()).parent(new ModelFile.UncheckedModelFile(modLoc("item/firefly_jar_item_base")));

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