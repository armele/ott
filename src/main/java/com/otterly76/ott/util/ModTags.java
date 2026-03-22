package com.otterly76.ott.util;

import com.otterly76.ott.Constants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Blocks {
        public static final TagKey<Block> PALE_OAK_LOGS = mcTag("pale_oak_logs");
        public static final TagKey<Block> CREAKING_HEART_HOLDERS = mcTag("creaking_heart_holders");
        public static final TagKey<Block> HAPPY_GHAST_AVOIDS = mcTag("happy_ghast_avoids");
        public static final TagKey<Block> TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS = mcTag("triggers_ambient_desert_sand_block_sounds");
        public static final TagKey<Block> TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS = mcTag("triggers_ambient_desert_dry_vegetation_block_sounds");
        public static final TagKey<Block> TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS = mcTag("triggers_ambient_dried_ghast_block_sounds");
        public static final TagKey<Block> ALLOWS_LEAF_LITTER = mcTag("allows_leaf_litter");
        public static final TagKey<Block> SPAWN_FALLING_LEAVES = mcTag("spawn_falling_leaves");
        public static final TagKey<Block> SPAWN_FALLING_NEEDLES = mcTag("spawn_falling_needles");
        public static final TagKey<Block> CAMELS_SPAWNABLE_ON = mcTag("camel_spawnable_on");
        public static final TagKey<Block> ALLIGATOR_EGG_LAYABLE_ON = createTag("alligator_egg_layable_on");
        public static final TagKey<Block> TORTOISE_EGG_LAYABLE_ON = createTag("tortoise_egg_layable_on");
        public static final TagKey<Block> HIPPO_EGG_LAYABLE_ON = createTag("hippo_egg_layable_on");
        public static final TagKey<Block> SNAIL_EGG_LAYABLE_ON = createTag("snail_egg_layable_on");
        public static final TagKey<Block> RHINO_CHARGE_BREAKABLE = createTag("rhino_charge_breakable");
        public static final TagKey<Block> VULTURES_SPAWNABLE_ON = mcTag("vultures_spawnable_on");
        public static final TagKey<Block> PORTAL_FRAME_BLOCKS = createTag("portal_frame_blocks");
        public static final TagKey<Block> C_OBSIDIAN = commonTag();

        public static final TagKey<Block> INCORRECT_FOR_COPPER_TOOL = mcTag("incorrect_for_copper_tool");
        public static final TagKey<Block> COPPER_GOLEM_SPAWN_BLOCKS = mcTag("copper_golem_spawn_blocks");
        public static final TagKey<Block> COPPER_CHESTS = mcTag("copper_chests");
        public static final TagKey<Block> WOODEN_SHELVES = mcTag("wooden_shelves");
        public static final TagKey<Block> COPPER = mcTag("copper");
        public static final TagKey<Block> LIGHTNING_RODS = mcTag("lightning_rods");
        public static final TagKey<Block> LANTERNS = mcTag("lanterns");

        public static final TagKey<Block> STONE = mcTag("stone");
        public static final TagKey<Block> PATHS = createTag("paths");
        public static final TagKey<Block> HARVEST_BLACKLIST = createTag("harvest_blacklist");
        public static final TagKey<Block> FERRET_DIG_GROUNDS = createTag("ferret_dig_grounds");

        public static TagKey<Block> woodSetLogs(String setName) {
            return createTag(setName + "_logs");
        }

        private static TagKey<Block> mcTag(String name) {
            return BlockTags.create(ResourceLocation.withDefaultNamespace(name));
        }

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        private static TagKey<Block> commonTag() {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath("c", "obsidian"));
        }
    }

    public static class ItemTags {
        public static final TagKey<Item> PALE_OAK_LOGS = mcTag("pale_oak_logs");
        public static final TagKey<Item> HAPPY_GHAST_TEMPT_ITEMS = mcTag("happy_ghast_tempt_items");
        public static final TagKey<Item> HAPPY_GHAST_FOOD = mcTag("happy_ghast_food");
        public static final TagKey<Item> HARNESSES = mcTag("harnesses");
        public static final TagKey<Item> BUNDLES = mcTag("bundles");
        public static final TagKey<Item> EGGS = mcTag("eggs");
        public static final TagKey<Item> BIRD_FOOD_ITEMS = createTag("bird_food_items");
        public static final TagKey<Item> ALLIGATOR_FOOD_ITEMS = createTag("alligator_food_items");
        public static final TagKey<Item> CATFISH_FOOD_ITEMS = createTag("catfish_food_items");
        public static final TagKey<Item> BEAR_FOOD_ITEMS = createTag("bear_food_items");
        public static final TagKey<Item> BOAR_FOOD_ITEMS = createTag("boar_food_items");
        public static final TagKey<Item> DEER_FOOD_ITEMS = createTag("deer_food_items");
        public static final TagKey<Item> DUCK_FOOD_ITEMS = createTag("duck_food_items");
        public static final TagKey<Item> FIREFLY_TEMPT_ITEMS = createTag("firefly_tempt_items");
        public static final TagKey<Item> LIZARD_FOOD_ITEMS = createTag("lizard_food_items");
        public static final TagKey<Item> MOOSE_FOOD_ITEMS = createTag("moose_food_items");
        public static final TagKey<Item> RHINO_FOOD_ITEMS = createTag("rhino_food_items");
        public static final TagKey<Item> SNAIL_FOOD_ITEMS = createTag("snail_food_items");
        public static final TagKey<Item> TORTOISE_FOOD_ITEMS = createTag("tortoise_food_items");
        public static final TagKey<Item> ZEBRA_FOOD_ITEMS = createTag("zebra_food_items");
        public static final TagKey<Item> GIRAFFE_FOOD_ITEMS = createTag("giraffe_food_items");
        public static final TagKey<Item> ELEPHANT_FOOD_ITEMS = createTag("elephant_food_items");
        public static final TagKey<Item> VULTURE_FOOD_ITEMS = createTag("vulture_food_items");
        public static final TagKey<Item> CAPYBARA_FOOD = createTag("capybara_food");
        public static final TagKey<Item> HEDGEHOG_FOOD = createTag("hedgehog_food");
        public static final TagKey<Item> KIWI_FOOD = createTag("kiwi_food");
        public static final TagKey<Item> PENGUIN_FOOD = createTag("penguin_food");
        public static final TagKey<Item> SEAL_FOOD = createTag("seal_food");
        public static final TagKey<Item> SEA_URCHIN_FOOD = createTag("sea_urchin_food");
        public static final TagKey<Item> FERRET_FOOD = createTag("ferret_food");
        public static final TagKey<Item> FERRET_TEMPT_ITEMS = createTag("ferret_tempt_items");
        public static final TagKey<Item> DRAGONFLY_FOOD = createTag("dragonfly_food");
        public static final TagKey<Item> JUMPING_SPIDER_FOOD = createTag("jumping_spider_food");
        public static final TagKey<Item> OTTER_FOOD = createTag("otter_food");
        public static final TagKey<Item> RED_PANDA_FOOD = createTag("red_panda_food");
        public static final TagKey<Item> RED_PANDA_TEMPT_ITEMS = createTag("red_panda_tempt_items");
        public static final TagKey<Item> STRIPPED_PALE_OAK_LOG = mcTag("stripped_pale_oak_log");
        public static final TagKey<Item> LANTERNS = mcTag("lanterns");
        public static final TagKey<Item> LIGHTNING_RODS = mcTag("lightning_rods");
        public static final TagKey<Item> RAILS = mcTag("rails");
        public static final TagKey<Item> ANVIL = mcTag("anvil");
        public static final TagKey<Item> FISHES = mcTag("fishes");
        public static final TagKey<Item> IS_MEAT = mcTag("is_meat");

        public static final TagKey<Item> C_COOKED_EGGS = commonTag("cooked_eggs");
        public static final TagKey<Item> C_COOKED_FISH = commonTag("cooked_fish");
        public static final TagKey<Item> C_COOKED_MEAT = commonTag("cooked_meat");
        public static final TagKey<Item> C_EGGS = commonTag("eggs");
        public static final TagKey<Item> C_RAW_FISH = commonTag("raw_fish");
        public static final TagKey<Item> C_RAW_MEAT = commonTag("raw_meat");

        public static final TagKey<Item> NF_COOKED_EGGS = nfTag("cooked_eggs");
        public static final TagKey<Item> NF_COOKED_FISHES = nfTag("cooked_fishes");
        public static final TagKey<Item> NF_EGGS = nfTag("eggs");
        public static final TagKey<Item> NF_RAW_FISHES = nfTag("raw_fishes");

        public static final TagKey<Item> C_CHAINS = commonTag("chains");
        public static final TagKey<Item> C_NUGGETS = commonTag("nuggets");
        public static final TagKey<Item> C_NUGGETS_COPPER = commonTag("nuggets/copper");
        public static final TagKey<Item> C_COPPER_NUGGETS = commonTag("copper_nuggets");
        public static final TagKey<Item> C_TOOLS_MELEE_WEAPON = commonTag("tools/melee_weapon");
        public static final TagKey<Item> C_TOOLS_MINING_TOOL = commonTag("tools/mining_tool");
        public static final TagKey<Item> C_TOOLS_SHEAR = commonTag("tools/shear");

        public static final TagKey<Item> DYEABLE_BANNERS = createTag("dyeable_banners");
        public static final TagKey<Item> DYEABLE_CANDLES = createTag("dyeable_candles");
        public static final TagKey<Item> DYEABLE_GLASS_BLOCKS = createTag("dyeable_glass_blocks");
        public static final TagKey<Item> DYEABLE_GLASS_PANES = createTag("dyeable_glass_panes");
        public static final TagKey<Item> DYEABLE_SHULKER_BOXES = createTag("dyeable_shulker_boxes");
        public static final TagKey<Item> DYEABLE_CONCRETE = createTag("dyeable_concrete");
        public static final TagKey<Item> DYEABLE_CONCRETE_POWDER = createTag("dyeable_concrete_powder");
        public static final TagKey<Item> DYEABLE_TERRACOTTA = createTag("dyeable_terracotta");
        public static final TagKey<Item> INVENTORY_OPENABLE = createTag("inventory_openable");

        public static final TagKey<Item> MODDED_STRIPPED_LOGS = createTag("create", "modded_stripped_logs");
        public static final TagKey<Item> MODDED_STRIPPED_WOOD = createTag("create", "modded_stripped_wood");

        private static TagKey<Item> mcTag(String name) {
            return net.minecraft.tags.ItemTags.create(ResourceLocation.withDefaultNamespace(name));
        }

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Item> createTag(String name) {
            return net.minecraft.tags.ItemTags.create(ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        private static TagKey<Item> nfTag(String name) {
            return net.minecraft.tags.ItemTags.create(ResourceLocation.fromNamespaceAndPath("neoforge", name));
        }

        @SuppressWarnings("SameParameterValue")
        private static TagKey<Item> createTag(String namespace, String name) {
            return net.minecraft.tags.ItemTags.create(ResourceLocation.fromNamespaceAndPath(namespace, name));
        }

        private static TagKey<Item> commonTag(String name) {
            return net.minecraft.tags.ItemTags.create(ResourceLocation.fromNamespaceAndPath("c", name));
        }

        public static TagKey<Item> woodSetLogs(String setName) {
            return createTag(setName + "_logs");
        }
    }

    public static class Biomes {
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_WARM_VARIANT_FARM_ANIMALS = createTag("spawns_warm_variant_farm_animals");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_COLD_VARIANT_FARM_ANIMALS = createTag("spawns_cold_variant_farm_animals");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_CAMELS = createTag("spawns_camels");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_BUSHES = createTag("spawns_bushes");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FIREFLY_BUSHES = createTag("spawns_firefly_bushes");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FIREFLY_BUSHES_SWAMP = createTag("spawns_firefly_bushes_swamp");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_WILDFLOWERS = createTag("spawns_wildflowers");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_NOISE_BASED_WILDFLOWERS = createTag("spawns_noise_based_wildflowers");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_DRY_GRASS = createTag("spawns_dry_grass");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_DRY_GRASS_RARELY = createTag("spawns_dry_grass_rarely");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FALLEN_OAK_TREES = createTag("spawns_fallen_oak_trees");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FALLEN_BIRCH_TREES = createTag("spawns_fallen_birch_trees");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FALLEN_BIRCH_TREES_RARELY = createTag("spawns_fallen_birch_trees_rarely");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FALLEN_SUPER_BIRCH_TREES = createTag("spawns_fallen_super_birch_trees");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FALLEN_JUNGLE_TREES = createTag("spawns_fallen_jungle_trees");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FALLEN_SPRUCE_TREES = createTag("spawns_fallen_spruce_trees");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_FALLEN_SPRUCE_TREES_RARELY = createTag("spawns_fallen_spruce_trees_rarely");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_LEAF_LITTER = createTag("spawns_leaf_litter");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_LEAF_LITTER_PATCHES = createTag("spawns_leaf_litter_patches");
        public static final TagKey<net.minecraft.world.level.biome.Biome> HAS_DARK_LEAF_LITTER = createTag("has_dark_leaf_litter");
        public static final TagKey<net.minecraft.world.level.biome.Biome> HAS_PALE_LEAF_LITTER = createTag("has_pale_leaf_litter");
        public static final TagKey<net.minecraft.world.level.biome.Biome> SPAWNS_OAK_NESTED_TREES = createTag("spawns_oak_nested_trees");
        public static final TagKey<net.minecraft.world.level.biome.Biome> IS_DESERT = createTag("is_desert");
        public static final TagKey<net.minecraft.world.level.biome.Biome> IS_SNOWY = createTag("is_snowy");
        public static final TagKey<net.minecraft.world.level.biome.Biome> IS_BIRCH_FOREST = createTag("is_birch_forest");
        public static final TagKey<net.minecraft.world.level.biome.Biome> IS_MEADOW = createTag("is_meadow");
        public static final TagKey<net.minecraft.world.level.biome.Biome> IS_HUMID = commonTag("is_humid");
        public static final TagKey<net.minecraft.world.level.biome.Biome> IS_DRY = commonTag("is_dry");

        private static TagKey<net.minecraft.world.level.biome.Biome> commonTag(String name) {
            return TagKey.create(net.minecraft.core.registries.Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", name));
        }

        private static TagKey<net.minecraft.world.level.biome.Biome> createTag(String name) {
            return TagKey.create(net.minecraft.core.registries.Registries.BIOME, ResourceLocation.fromNamespaceAndPath("minecraft", name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> PALE_GARDEN_IGNORED = createTag("pale_garden_ignored");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> FOLLOWABLE_FRIENDLY_MOBS = mcTag("followable_friendly_mobs");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> SKELETON_MOBS = createTag("skeleton_mobs");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> ACCEPTS_IRON_GOLEM_GIFT = mcTag("accepts_iron_golem_gift");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> CANDIDATE_FOR_IRON_GOLEM_GIFT = mcTag("candidate_for_iron_golem_gift");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> SMART_ANIMALS = createTag("smart_animals");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> OTT_ENTITIES = createTag("ott_entities");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> SAFE_EGG_WALKERS = createTag("safe_egg_walkers");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> CATFISH_HOSTILES = createTag("catfish_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> ALLIGATOR_HOSTILES = createTag("alligator_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> ELEPHANT_HOSTILES = createTag("elephant_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> HIPPO_HOSTILES = createTag("hippo_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> LION_HOSTILES = createTag("lion_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> BEAR_HOSTILES = createTag("bear_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> VULTURE_HOSTILES = createTag("vulture_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> BOAR_HOSTILES = createTag("boar_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> DEER_PREDATORS = createTag("deer_predators");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> SNAKE_HOSTILES = createTag("snake_hostiles");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> FISH = createTag("fish");
        public static final TagKey<net.minecraft.world.entity.EntityType<?>> FROG_FOOD = mcTag("frog_food");

        @SuppressWarnings("SameParameterValue")
        private static TagKey<net.minecraft.world.entity.EntityType<?>> createTag(String name) {
            return TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name));
        }

        @SuppressWarnings("SameParameterValue")
        private static TagKey<net.minecraft.world.entity.EntityType<?>> mcTag(String name) {
            return TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, ResourceLocation.withDefaultNamespace(name));
        }
    }
}