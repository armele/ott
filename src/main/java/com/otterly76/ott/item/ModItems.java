package com.otterly76.ott.item;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.color.ModColorSets;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.variant.ChickenVariants;
import com.otterly76.ott.registry.ModArmorMaterials;
import com.otterly76.ott.registry.ModDataComponents;
import com.otterly76.ott.registry.ModJukeboxSongs;
import com.otterly76.ott.entity.vehicle.OttWoodSetBoatEntity;
import com.otterly76.ott.entity.vehicle.OttWoodSetChestBoatEntity;
import com.otterly76.ott.entity.custom.Butterfly;
import com.otterly76.ott.item.custom.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.PlaceOnWaterBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Constants.MOD_ID);
    public static final DeferredRegister.Items MINECRAFT_ITEMS = ModBlocks.MINECRAFT_ITEMS;

    public static final Map<String, DeferredItem<SignItem>> WOOD_SET_SIGNS = new HashMap<>();
    public static final Map<String, DeferredItem<HangingSignItem>> WOOD_SET_HANGING_SIGNS = new HashMap<>();
    public static final Map<String, DeferredItem<ModBoatItem>> WOOD_SET_BOATS = new HashMap<>();
    public static final Map<String, DeferredItem<ModBoatItem>> WOOD_SET_CHEST_BOATS = new HashMap<>();

    public static final Map<String, DeferredItem<Item>> CUSTOM_DYES = new HashMap<>();
    public static final Map<String, DeferredItem<Item>> HARNESSES = new HashMap<>();
    public static final Map<String, DeferredItem<Item>> BUNDLES = new HashMap<>();

    // Standard Items
    public static final DeferredItem<Item> CLAM = ITEMS.register("clam", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> KOI_FISH = ITEMS.register("koi_fish", () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> PEARL = ITEMS.register("pearl", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SILK = ITEMS.register("silk", () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SILK_COCOON = registerBlockItem("silk_cocoon", ModBlocks.SILK_COCOON);

    public static final DeferredItem<Item> DRAGON_SKULL = ITEMS.register("dragon_skull", () -> new net.minecraft.world.item.StandingAndWallBlockItem(ModBlocks.DRAGON_SKULL.get(), ModBlocks.DRAGON_WALL_SKULL.get(), new net.minecraft.world.item.Item.Properties(), net.minecraft.core.Direction.DOWN));
    public static final DeferredHolder<Item, BlockItem> THORNY_HEDGE = registerBlockItem("thorny_hedge", ModBlocks.THORNY_HEDGE);
    public static final DeferredItem<Item> THORNY_HEDGE_SPROUTS = ITEMS.register("thorny_hedge_sprouts", () -> new ItemNameBlockItem(ModBlocks.THORNY_HEDGE_SPROUTS.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> PROTECTIVE_LANTERN = registerBlockItem("protective_lantern", ModBlocks.PROTECTIVE_LANTERN);
    public static final DeferredHolder<Item, BlockItem> WEATHERING_STATION = registerBlockItem("weathering_station", ModBlocks.WEATHERING_STATION);

    public static final DeferredHolder<Item, BlockItem> WATER_LANTERN = registerBlockItem("water_lantern", ModBlocks.WATER_LANTERN);
    public static final DeferredHolder<Item, BlockItem> LAVA_LANTERN = registerBlockItem("lava_lantern", ModBlocks.LAVA_LANTERN);
    public static final DeferredHolder<Item, BlockItem> SMITE_LANTERN = registerBlockItem("smite_lantern", ModBlocks.SMITE_LANTERN);
    public static final DeferredItem<Item> BIG_LILY_PAD = ITEMS.register("big_lily_pad", () -> new PlaceOnWaterBlockItem(ModBlocks.BIG_LILY_PAD.get(), new Item.Properties()));

    public static final DeferredItem<Item> TINY_COAL = ITEMS.register("tiny_coal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TINY_CHARCOAL = ITEMS.register("tiny_charcoal", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> TORCH_ARROW = ITEMS.register("torch_arrow", () -> new TorchArrowItem(new Item.Properties()));

    public static final DeferredItem<Item> MAN_O_WAR_BUCKET = ITEMS.register("man_o_war_bucket",
            () -> new MobBucketItem(ModEntities.MAN_O_WAR.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final DeferredItem<Item> MAN_O_WAR_SPAWN_EGG = ITEMS.register("man_o_war_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MAN_O_WAR, 0xFF4751E0, 0xFF5D33A4, new Item.Properties()));
    public static final DeferredItem<Item> DUCK_SPAWN_EGG = ITEMS.register("duck_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.DUCK, 0xFFFFFFFF, 0xFF29A832, new Item.Properties()));
    public static final DeferredItem<Item> GOOSE_SPAWN_EGG = ITEMS.register("goose_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GOOSE, 0xFFFFFFFF, 0xFF808080, new Item.Properties()));

    public static final DeferredItem<Item> STINGRAY_BUCKET = ITEMS.register("stingray_bucket",
            () -> new MobBucketItem(ModEntities.STINGRAY.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final DeferredItem<Item> STINGRAY_SPAWN_EGG = ITEMS.register("stingray_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.STINGRAY, 0xFF838788, 0xFF454748, new Item.Properties()));

    public static final DeferredItem<Item> CICHLID_SPAWN_EGG = ITEMS.register("cichlid_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CICHLID, 0x3F51B5, 0x1A237E, new Item.Properties()));
    public static final DeferredItem<Item> LEOPARD_CAT_SPAWN_EGG = ITEMS.register("leopard_cat_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.LEOPARD_CAT, 0xC4A484, 0x4B3621, new Item.Properties()));
    public static final DeferredItem<Item> ECHIDNA_SPAWN_EGG = ITEMS.register("echidna_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.ECHIDNA, 0x5C4033, 0xD2B48C, new Item.Properties()));
    public static final DeferredItem<Item> GUITARFISH_SPAWN_EGG = ITEMS.register("guitarfish_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.GUITARFISH, 0x708090, 0xC0C0C0, new Item.Properties()));
    public static final DeferredItem<Item> BONNETHEAD_SHARK_SPAWN_EGG = ITEMS.register("bonnethead_shark_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BONNETHEAD_SHARK, 0x708090, 0x2F4F4F, new Item.Properties()));
    public static final DeferredItem<Item> BURROWING_OWL_SPAWN_EGG = ITEMS.register("burrowing_owl_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BURROWING_OWL, 0x8B4513, 0xD2B48C, new Item.Properties()));
    public static final DeferredItem<Item> BUSHDOG_SPAWN_EGG = ITEMS.register("bushdog_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BUSHDOG, 0x8B4513, 0x5C4033, new Item.Properties()));
    public static final DeferredItem<Item> QUAIL_SPAWN_EGG = ITEMS.register("quail_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.QUAIL, 0x808080, 0x404040, new Item.Properties()));
    public static final DeferredItem<Item> CANDYCANE_SNAIL_SPAWN_EGG = ITEMS.register("candycane_snail_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CANDYCANE_SNAIL, 0xFFFFFF, 0xFF0000, new Item.Properties()));
    public static final DeferredItem<Item> FIRE_SALAMANDER_SPAWN_EGG = ITEMS.register("fire_salamander_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.FIRE_SALAMANDER, 0x000000, 0xFFD700, new Item.Properties()));
    public static final DeferredItem<Item> RIVER_TURTLE_SPAWN_EGG = ITEMS.register("river_turtle_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.RIVER_TURTLE, 0x6B8E23, 0x556B2F, new Item.Properties()));
    public static final DeferredItem<Item> GOBLIN_SHARK_SPAWN_EGG = ITEMS.register("goblin_shark_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.GOBLIN_SHARK, 0xDB7093, 0xFFC0CB, new Item.Properties()));
    public static final DeferredItem<Item> GUINEA_FOWL_SPAWN_EGG = ITEMS.register("guinea_fowl_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.GUINEA_FOWL, 0x404040, 0xFFFFFF, new Item.Properties()));
    public static final DeferredItem<Item> IMPALA_SPAWN_EGG = ITEMS.register("impala_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.IMPALA, 0xC4A484, 0xFFFFFF, new Item.Properties()));
    public static final DeferredItem<Item> MANTA_RAY_SPAWN_EGG = ITEMS.register("manta_ray_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.MANTA_RAY, 0x2F4F4F, 0xFFFFFF, new Item.Properties()));
    public static final DeferredItem<Item> STORK_SPAWN_EGG = ITEMS.register("stork_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.STORK, 0x404040, 0xFFFFFF, new Item.Properties()));
    public static final DeferredItem<Item> MOLE_SPAWN_EGG = ITEMS.register("mole_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.MOLE, 0x2F4F4F, 0x000000, new Item.Properties()));
    public static final DeferredItem<Item> TREE_KANGAROO_SPAWN_EGG = ITEMS.register("tree_kangaroo_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.TREE_KANGAROO, 0x8B4513, 0xFFD700, new Item.Properties()));
    public static final DeferredItem<Item> PALLAS_CAT_SPAWN_EGG = ITEMS.register("pallas_cat_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.PALLAS_CAT, 0x808080, 0xC0C0C0, new Item.Properties()));
    public static final DeferredItem<Item> PINK_LAND_IGUANA_SPAWN_EGG = ITEMS.register("pink_land_iguana_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.PINK_LAND_IGUANA, 0xFFC0CB, 0xDB7093, new Item.Properties()));
    public static final DeferredItem<Item> PSYCHO_JELLY_SPAWN_EGG = ITEMS.register("psycho_jelly_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.PSYCHO_JELLY, 0xFF00FF, 0x00FFFF, new Item.Properties()));
    public static final DeferredItem<Item> SPOONBILL_SPAWN_EGG = ITEMS.register("spoonbill_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SPOONBILL, 0xFFC0CB, 0xFFFFFF, new Item.Properties()));
    public static final DeferredItem<Item> GIANT_SOFTSHELL_TURTLE_SPAWN_EGG = ITEMS.register("giant_softshell_turtle_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.GIANT_SOFTSHELL_TURTLE, 0x556B2F, 0x8B4513, new Item.Properties()));

    public static final DeferredItem<Item> SUNFISH_BUCKET = ITEMS.register("sunfish_bucket",
            () -> new MobBucketItem(ModEntities.SUNFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final DeferredItem<Item> SUNFISH_SPAWN_EGG = ITEMS.register("sunfish_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SUNFISH, 0xFF967E67, 0xFFD6C6B0, new Item.Properties()));

    public static final DeferredItem<Item> KRILL_BUCKET = ITEMS.register("krill_bucket",
            () -> new MobBucketItem(ModEntities.KRILL.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final DeferredItem<Item> KRILL_SPAWN_EGG = ITEMS.register("krill_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.KRILL, 0xFFF09090, 0xFFD05050, new Item.Properties()));

    public static final DeferredItem<Item> ANGELFISH_BUCKET = ITEMS.register("angelfish_bucket",
            () -> new MobBucketItem(ModEntities.ANGELFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final DeferredItem<Item> ANGELFISH_SPAWN_EGG = ITEMS.register("angelfish_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.ANGELFISH, 0xFFE0E0E0, 0xFF202020, new Item.Properties()));

    public static final DeferredItem<Item> BARRELEYE_BUCKET = ITEMS.register("barreleye_bucket",
            () -> new MobBucketItem(ModEntities.BARRELEYE.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final DeferredItem<Item> BARRELEYE_SPAWN_EGG = ITEMS.register("barreleye_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BARRELEYE, 0xFF2D3233, 0xFF5A8452, new Item.Properties()));

    public static final DeferredItem<Item> FLOUNDER_BUCKET = ITEMS.register("flounder_bucket",
            () -> new MobBucketItem(ModEntities.FLOUNDER.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final DeferredItem<Item> FLOUNDER_SPAWN_EGG = ITEMS.register("flounder_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.FLOUNDER, 0xFF604D3F, 0xFF9C8A7B, new Item.Properties()));

    public static final DeferredItem<Item> MARINE_IGUANA_BUCKET = ITEMS.register("marine_iguana_bucket",
            () -> new MobBucketItem(ModEntities.MARINE_IGUANA.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1)));
    public static final DeferredItem<Item> MARINE_IGUANA_SPAWN_EGG = ITEMS.register("marine_iguana_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MARINE_IGUANA, 0xFF5B6149, 0xFF35392B, new Item.Properties()));
    public static final DeferredItem<Item> GECKO_SPAWN_EGG = ITEMS.register("gecko_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GECKO, 0xFFD4AF37, 0xFF4B3621, new Item.Properties()));
    public static final DeferredItem<Item> EMU_SPAWN_EGG = ITEMS.register("emu_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.EMU, 0xFF4B3621, 0xFF2D1E12, new Item.Properties()));
    public static final DeferredItem<Item> HOOPOE_SPAWN_EGG = ITEMS.register("hoopoe_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.HOOPOE, 0xFFE0C09F, 0xFF4B3621, new Item.Properties()));
    public static final DeferredItem<Item> PHEASANT_SPAWN_EGG = ITEMS.register("pheasant_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PHEASANT, 0xFF4B3621, 0xFF8B4513, new Item.Properties()));
    public static final DeferredItem<Item> TOUCAN_SPAWN_EGG = ITEMS.register("toucan_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TOUCAN, 0xFF000000, 0xFFFFA500, new Item.Properties()));
    public static final DeferredItem<Item> SALT = ITEMS.register("salt", () -> new net.minecraft.world.item.BlockItem(ModBlocks.SALT_DUST.get(), new Item.Properties()));
    public static final DeferredItem<Item> SALT_BLOCK = ITEMS.register("salt_block", () -> new net.minecraft.world.item.BlockItem(ModBlocks.SALT_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> POLISHED_SALT_BLOCK = ITEMS.register("polished_salt_block", () -> new net.minecraft.world.item.BlockItem(ModBlocks.POLISHED_SALT_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> SALT_LAMP = ITEMS.register("salt_lamp", () -> new net.minecraft.world.item.BlockItem(ModBlocks.SALT_LAMP.get(), new Item.Properties()));
    public static final DeferredItem<Item> OAK_NEST = ITEMS.register("oak_nest", () -> new net.minecraft.world.item.BlockItem(ModBlocks.OAK_NEST.get(), new Item.Properties()));
    public static final DeferredItem<Item> EMU_EGG = ITEMS.register("emu_egg", () -> new EmuEggItem(new Item.Properties()));
    public static final DeferredItem<Item> HOOPOE_EGG = ITEMS.register("hoopoe_egg", () -> new HoopoeEggItem(new Item.Properties()));
    public static final DeferredItem<Item> PHEASANT_EGG = ITEMS.register("pheasant_egg", () -> new PheasantEggItem(new Item.Properties()));
    public static final DeferredItem<Item> TOUCAN_EGG = ITEMS.register("toucan_egg", () -> new ToucanEggItem(new Item.Properties()));

    public static final DeferredItem<Item> RAW_FLOUNDER = ITEMS.register("raw_flounder",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> RAW_ANGELFISH = ITEMS.register("raw_angelfish",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> RAW_BARRELEYE = ITEMS.register("raw_barreleye",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> RAW_KRILL = ITEMS.register("raw_krill",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> FRIED_KRILL = ITEMS.register("fried_krill",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build())));
    public static final DeferredItem<Item> RAW_SUNFISH_MEAT = ITEMS.register("raw_sunfish_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_SUNFISH_MEAT = ITEMS.register("cooked_sunfish_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> RAW_GOLDEN_SUNFISH_MEAT = ITEMS.register("raw_golden_sunfish_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_GOLDEN_SUNFISH_MEAT = ITEMS.register("cooked_golden_sunfish_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));

    public static final DeferredItem<Item> CATFISH = ITEMS.register("catfish",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_CATFISH = ITEMS.register("cooked_catfish",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> BASS = ITEMS.register("bass",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_BASS = ITEMS.register("cooked_bass",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));

    public static final DeferredItem<Item> CATFISH_SPAWN_EGG = ITEMS.register("catfish_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CATFISH, 8416033, 12233092, new Item.Properties()));
    public static final DeferredItem<Item> WATER_BUFFALO_SPAWN_EGG = ITEMS.register("water_buffalo_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.WATER_BUFFALO, 0x4B3621, 0x2A1B0E, new Item.Properties()));
    public static final DeferredItem<Item> BASS_SPAWN_EGG = ITEMS.register("bass_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BASS, 8159273, 14729339, new Item.Properties()));

    public static final DeferredItem<Item> BLUEJAY_SPAWN_EGG = ITEMS.register("bluejay_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BLUEJAY, 2830129, 4289464, new Item.Properties()));
    public static final DeferredItem<Item> CANARY_SPAWN_EGG = ITEMS.register("canary_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CANARY, 16704333, 13999625, new Item.Properties()));
    public static final DeferredItem<Item> CARDINAL_SPAWN_EGG = ITEMS.register("cardinal_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CARDINAL, 13772840, 4465186, new Item.Properties()));
    public static final DeferredItem<Item> FINCH_SPAWN_EGG = ITEMS.register("finch_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.FINCH, 12013877, 6576975, new Item.Properties()));
    public static final DeferredItem<Item> ROBIN_SPAWN_EGG = ITEMS.register("robin_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.ROBIN, 4865860, 16620592, new Item.Properties()));
    public static final DeferredItem<Item> SPARROW_SPAWN_EGG = ITEMS.register("sparrow_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SPARROW, 6504493, 14603707, new Item.Properties()));

    public static final DeferredItem<Item> BROWN_BEAR_SPAWN_EGG = ITEMS.register("brown_bear_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BROWN_BEAR, 0x643D27, 0xC8A911, new Item.Properties()));
    public static final DeferredItem<Item> BLACK_BEAR_SPAWN_EGG = ITEMS.register("black_bear_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BLACK_BEAR, 0x1D1D1D, 0x3D3D3D, new Item.Properties()));
    public static final DeferredItem<Item> DEER_SPAWN_EGG = ITEMS.register("deer_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.DEER, 0x9D7155, 0xDDD188, new Item.Properties()));
    public static final DeferredItem<Item> REINDEER_SPAWN_EGG = ITEMS.register("reindeer_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.REINDEER, 0x8B4513, 0xD2B48C, new Item.Properties()));
    public static final DeferredItem<Item> WHITE_DEER_SPAWN_EGG = ITEMS.register("white_deer_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.WHITE_DEER, 0xFFFFFF, 0xE0E0E0, new Item.Properties()));


    public static final DeferredItem<Item> BUTTERFLY_SPAWN_EGG = ITEMS.register("butterfly_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.BUTTERFLY, 0x3d352e, 0xef9b13, new Item.Properties()));
    public static final DeferredItem<Item> CATERPILLAR_SPAWN_EGG = ITEMS.register("caterpillar_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CATERPILLAR, 0x475e33, 0xb6c753, new Item.Properties()));
    public static final DeferredItem<Item> FIREFLY_SPAWN_EGG = ITEMS.register("firefly_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.FIREFLY, 0x1d1a18, 0xfce94f, new Item.Properties()));

    public static final DeferredItem<Item> ALLIGATOR_SPAWN_EGG = ITEMS.register("alligator_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.ALLIGATOR, 0x5E5D24, 0xD2B961, new Item.Properties()));

    public static final DeferredItem<Item> ELEPHANT_SPAWN_EGG = ITEMS.register("elephant_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.ELEPHANT, 0x918E8D, 0x655D5A, new Item.Properties()));

    public static final DeferredItem<Item> GIRAFFE_SPAWN_EGG = ITEMS.register("giraffe_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.GIRAFFE, 0xDAA86F, 0x744420, new Item.Properties()));

    public static final DeferredItem<Item> HIPPO_SPAWN_EGG = ITEMS.register("hippo_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.HIPPO, 0xEF9A9A, 0x896562, new Item.Properties()));

    public static final DeferredItem<Item> LION_SPAWN_EGG = ITEMS.register("lion_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.LION, 0xE4BD82, 0x663A11, new Item.Properties()));

    public static final DeferredItem<Item> RHINO_SPAWN_EGG = ITEMS.register("rhino_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.RHINO, 0x74605A, 0xA79289, new Item.Properties()));

    public static final DeferredItem<Item> LIZARD_SPAWN_EGG = ITEMS.register("lizard_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.LIZARD, 10853166, 15724462, new Item.Properties()));
    public static final DeferredItem<Item> SNAIL_SPAWN_EGG = ITEMS.register("snail_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SNAIL, 5457209, 8811878, new Item.Properties()));
    public static final DeferredItem<Item> TORTOISE_SPAWN_EGG = ITEMS.register("tortoise_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.TORTOISE, 15724462, 11765582, new Item.Properties()));
    public static final DeferredItem<Item> VULTURE_SPAWN_EGG = ITEMS.register("vulture_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.VULTURE, 4010022, 15325376, new Item.Properties()));
    public static final DeferredItem<Item> ZEBRA_SPAWN_EGG = ITEMS.register("zebra_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.ZEBRA, 15263457, 1710104, new Item.Properties()));
    public static final DeferredItem<Item> MOOSE_SPAWN_EGG = ITEMS.register("moose_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.MOOSE, 0x5C4033, 0x2E1D13, new Item.Properties()));
    public static final DeferredItem<Item> MAMMOTH_SPAWN_EGG = ITEMS.register("mammoth_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.MAMMOTH, 0x4B3621, 0x2A1B0E, new Item.Properties()));
    public static final DeferredItem<Item> MYCELIUM_MAMMOTH_SPAWN_EGG = ITEMS.register("mycelium_mammoth_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.MYCELIUM_MAMMOTH, 0x6E4E37, 0x93785D, new Item.Properties()));

    public static final DeferredItem<Item> FENNEC_FOX_SPAWN_EGG = ITEMS.register("fennec_fox_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.FENNEC_FOX, 0xD3A66B, 0xF7E3B6, new Item.Properties()));

    public static final DeferredItem<Item> CAPYBARA_SPAWN_EGG = ITEMS.register("capybara_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.CAPYBARA, 0x8B5A2B, 0x5C4033, new Item.Properties()));
    public static final DeferredItem<Item> HEDGEHOG_SPAWN_EGG = ITEMS.register("hedgehog_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.HEDGEHOG, 0x5C4033, 0xD2B48C, new Item.Properties()));
    public static final DeferredItem<Item> LARGE_JELLYFISH_SPAWN_EGG = ITEMS.register("large_jellyfish_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.LARGE_JELLYFISH, 0xFFB6C1, 0xFF69B4, new Item.Properties()));
    public static final DeferredItem<Item> SEAHORSE_SPAWN_EGG = ITEMS.register("seahorse_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SEAHORSE, 0xFFD700, 0xFFA500, new Item.Properties()));
    public static final DeferredItem<Item> ETHEREAL_SHRIMP_SPAWN_EGG = ITEMS.register("ethereal_shrimp_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.ETHEREAL_SHRIMP, 0xFFC0CB, 0xFF69B4, new Item.Properties()));
    public static final DeferredItem<Item> STARFISH_SPAWN_EGG = ITEMS.register("starfish_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.STARFISH, 0xFF4500, 0xFFFF00, new Item.Properties()));
    public static final DeferredItem<Item> SMALL_JELLYFISH_SPAWN_EGG = ITEMS.register("small_jellyfish_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SMALL_JELLYFISH, 0x00FFFF, 0x0000FF, new Item.Properties()));
    public static final DeferredItem<Item> MEDIUM_JELLYFISH_SPAWN_EGG = ITEMS.register("medium_jellyfish_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.MEDIUM_JELLYFISH, 0x800080, 0xFF00FF, new Item.Properties()));
    public static final DeferredItem<Item> LARGE_JELLYFISH_BUCKET = ITEMS.register("large_jellyfish_bucket",
            () -> new MobBucketItem(ModEntities.LARGE_JELLYFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SMALL_JELLYFISH_BUCKET = ITEMS.register("small_jellyfish_bucket",
            () -> new MobBucketItem(ModEntities.SMALL_JELLYFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> MEDIUM_JELLYFISH_BUCKET = ITEMS.register("medium_jellyfish_bucket",
            () -> new MobBucketItem(ModEntities.MEDIUM_JELLYFISH.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SEAHORSE_BUCKET = ITEMS.register("seahorse_bucket",
            () -> new MobBucketItem(ModEntities.SEAHORSE.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> ETHEREAL_SHRIMP_BUCKET = ITEMS.register("ethereal_shrimp_bucket",
            () -> new MobBucketItem(ModEntities.ETHEREAL_SHRIMP.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KIWI_SPAWN_EGG = ITEMS.register("kiwi_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.KIWI, 0x8B4513, 0xD2B48C, new Item.Properties()));
    public static final DeferredItem<Item> PENGUIN_SPAWN_EGG = ITEMS.register("penguin_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.PENGUIN, 0x2F4F4F, 0xFFFFFF, new Item.Properties()));
    public static final DeferredItem<Item> SEAL_SPAWN_EGG = ITEMS.register("seal_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SEAL, 0x708090, 0xC0C0C0, new Item.Properties()));
    public static final DeferredItem<Item> SEA_URCHIN_SPAWN_EGG = ITEMS.register("sea_urchin_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SEA_URCHIN, 0x4B0082, 0x000000, new Item.Properties()));

    public static final DeferredItem<Item> DRAGONFLY_SPAWN_EGG = ITEMS.register("dragonfly_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.DRAGONFLY, 0x08EECF, 0xD3FF96, new Item.Properties()));
    public static final DeferredItem<Item> DUMBO_OCTOPUS_SPAWN_EGG = ITEMS.register("dumbo_octopus_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.DUMBO_OCTOPUS, 0xFCDC4C, 0x162630, new Item.Properties()));
    public static final DeferredItem<Item> FERRET_SPAWN_EGG = ITEMS.register("ferret_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.FERRET, 0xC5AC88, 0x37212D, new Item.Properties()));
    public static final DeferredItem<Item> JUMPING_SPIDER_SPAWN_EGG = ITEMS.register("jumping_spider_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.JUMPING_SPIDER, 0x34191E, 0x865F33, new Item.Properties()));
    public static final DeferredItem<Item> KOI_FISH_SPAWN_EGG = ITEMS.register("koi_fish_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.KOI_FISH, 0xF3ECED, 0xFB5321, new Item.Properties()));
    public static final DeferredItem<Item> OTTER_SPAWN_EGG = ITEMS.register("otter_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.OTTER, 0x352C34, 0xB49494, new Item.Properties()));
    public static final DeferredItem<Item> RED_PANDA_SPAWN_EGG = ITEMS.register("red_panda_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.RED_PANDA, 0xF4943C, 0x13131B, new Item.Properties()));
    public static final DeferredItem<Item> SEA_BUNNY_SPAWN_EGG = ITEMS.register("sea_bunny_spawn_egg",
            () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.SEA_BUNNY, 0xF4ECE4, 0x453337, new Item.Properties()));

    public static final DeferredItem<Item> BUG_NET = ITEMS.register("bug_net",
            () -> new BugNetItem(new Item.Properties().durability(64)));

    public static final DeferredItem<Item> CATERPILLAR = ITEMS.register("caterpillar",
            () -> new CaughtMobItem(ModEntities.CATERPILLAR, () -> net.minecraft.world.level.material.Fluids.EMPTY, () -> net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final Map<Butterfly.Variant, DeferredItem<Item>> BUTTERFLIES = new HashMap<>();

    static {
        for (Butterfly.Variant variant : Butterfly.Variant.values()) {
            BUTTERFLIES.put(variant, ITEMS.register("butterfly_" + variant.getName(),
                    () -> new ButterflyItem(variant, new Item.Properties().stacksTo(1))));
        }
    }

    public static final DeferredItem<Item> ALLIGATOR_EGG = ITEMS.register("alligator_egg",
            () -> new BlockItem(ModBlocks.ALLIGATOR_EGG.get(), new Item.Properties()));
    public static final DeferredItem<Item> DUCK_EGG = ITEMS.register("duck_egg",
            () -> new DuckEggItem(new Item.Properties()));
    public static final DeferredItem<Item> TORTOISE_EGG = ITEMS.register("tortoise_egg",
            () -> new BlockItem(ModBlocks.TORTOISE_EGG.get(), new Item.Properties()));
    public static final DeferredItem<Item> SNAIL_EGG = ITEMS.register("snail_egg",
            () -> new BlockItem(ModBlocks.SNAIL_EGG.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SMOOTH_GLOWSTONE = registerBlockItem("smooth_glowstone", ModBlocks.SMOOTH_GLOWSTONE);
    public static final DeferredItem<Item> GLOW_GOOP = ITEMS.register("glow_goop",
            () -> new GlowGoopItem(ModBlocks.GLOW_GOOP.get(), new Item.Properties()));
    public static final DeferredItem<Item> LIZARD_TAIL = ITEMS.register("lizard_tail",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_LIZARD_TAIL = ITEMS.register("cooked_lizard_tail",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build())));
    public static final DeferredItem<Item> COOKED_EGG = ITEMS.register("cooked_egg",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> SNAIL_SHELL = ITEMS.register("snail_shell",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> SNAIL_BUCKET = ITEMS.register("snail_bucket",
            () -> new SnailBucketItem(ModEntities.SNAIL, () -> net.minecraft.world.level.material.Fluids.EMPTY, () -> net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> JELLYFISH_JELLY = ITEMS.register("jellyfish_jelly",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> SEA_URCHIN_CAVIAR = ITEMS.register("sea_urchin_caviar",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.5F).build())));
    public static final DeferredItem<Item> KIWI_EGG = ITEMS.register("kiwi_egg",
            () -> new KiwiEggItem(new Item.Properties()));
    public static final DeferredItem<Item> PENGUIN_EGG = ITEMS.register("penguin_egg",
            () -> new PenguinEggItem(new Item.Properties()));

    public static final DeferredItem<Item> RAW_CICHLID = ITEMS.register("raw_cichlid",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_CICHLID = ITEMS.register("cooked_cichlid",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> RAW_BONNETHEAD = ITEMS.register("raw_bonnethead",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_BONNETHEAD = ITEMS.register("cooked_bonnethead",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> RAW_GUITARFISH = ITEMS.register("raw_guitarfish",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_GUITARFISH = ITEMS.register("cooked_guitarfish",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> RAW_GOBLIN_SHARK = ITEMS.register("raw_goblin_shark",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_GOBLIN_SHARK = ITEMS.register("cooked_goblin_shark",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> RAW_SNAIL = ITEMS.register("raw_snail",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> COOKED_SNAIL = ITEMS.register("cooked_snail",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> RAW_SHRIMP = ITEMS.register("raw_shrimp",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> STEAMED_SHRIMP = ITEMS.register("steamed_shrimp",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> RAW_WILD_BIRD_MEAT = ITEMS.register("raw_wild_bird_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.3F).build())));
    public static final DeferredItem<Item> COOKED_WILD_BIRD_MEAT = ITEMS.register("cooked_wild_bird_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));
    public static final DeferredItem<Item> RAW_WILD_GAME_MEAT = ITEMS.register("raw_wild_game_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build())));
    public static final DeferredItem<Item> COOKED_WILD_GAME_MEAT = ITEMS.register("cooked_wild_game_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationModifier(0.8F).build())));
    public static final DeferredItem<Item> RAW_CRAB_MEAT = ITEMS.register("raw_crab_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).build())));
    public static final DeferredItem<Item> STEAMED_CRAB_MEAT = ITEMS.register("steamed_crab_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationModifier(0.6F).build())));

    public static final DeferredHolder<Item, BlockItem> CHRYSALIS = registerBlockItem("chrysalis", ModBlocks.CHRYSALIS);

    public static final DeferredItem<Item> CATFISH_BUCKET = ITEMS.register("catfish_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.CATFISH.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BASS_BUCKET = ITEMS.register("bass_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.BASS.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> DUMBO_OCTOPUS_BUCKET = ITEMS.register("dumbo_octopus_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.DUMBO_OCTOPUS.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> KOI_FISH_BUCKET = ITEMS.register("koi_fish_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.KOI_FISH.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> SEA_BUNNY_BUCKET = ITEMS.register("sea_bunny_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.SEA_BUNNY.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> CICHLID_BUCKET = ITEMS.register("cichlid_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.CICHLID.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GUITARFISH_BUCKET = ITEMS.register("guitarfish_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.GUITARFISH.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BONNETHEAD_SHARK_BUCKET = ITEMS.register("bonnethead_shark_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.BONNETHEAD_SHARK.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GOBLIN_SHARK_BUCKET = ITEMS.register("goblin_shark_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.GOBLIN_SHARK.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> PSYCHO_JELLY_BUCKET = ITEMS.register("psycho_jelly_bucket",
            () -> new net.minecraft.world.item.MobBucketItem(ModEntities.PSYCHO_JELLY.get(), net.minecraft.world.level.material.Fluids.WATER, net.minecraft.sounds.SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static DeferredItem<Item> PALE_OAK_SAPLING;

    // Backport / Minecraft Namespace ItemTags
    public static DeferredItem<Item> RESIN_BRICK;
    public static DeferredItem<Item> MUSIC_DISC_TEARS;
    public static DeferredItem<Item> MUSIC_DISC_LAVA_CHICKEN;
    public static DeferredItem<Item> CREAKING_SPAWN_EGG;
    public static DeferredItem<Item> HAPPY_GHAST_SPAWN_EGG;
    public static DeferredItem<Item> BLUE_EGG;
    public static DeferredItem<Item> BROWN_EGG;

    public static DeferredItem<Item> COPPER_NUGGET;
    public static DeferredItem<SwordItem> COPPER_SWORD;
    public static DeferredItem<ShovelItem> COPPER_SHOVEL;
    public static DeferredItem<PickaxeItem> COPPER_PICKAXE;
    public static DeferredItem<AxeItem> COPPER_AXE;
    public static DeferredItem<HoeItem> COPPER_HOE;
    public static DeferredItem<ShearsItem> COPPER_SHEARS;

    public static DeferredItem<SwordItem> EXPOSED_COPPER_SWORD;
    public static DeferredItem<ShovelItem> EXPOSED_COPPER_SHOVEL;
    public static DeferredItem<PickaxeItem> EXPOSED_COPPER_PICKAXE;
    public static DeferredItem<AxeItem> EXPOSED_COPPER_AXE;
    public static DeferredItem<HoeItem> EXPOSED_COPPER_HOE;
    public static DeferredItem<ShearsItem> EXPOSED_COPPER_SHEARS;

    public static DeferredItem<SwordItem> WEATHERED_COPPER_SWORD;
    public static DeferredItem<ShovelItem> WEATHERED_COPPER_SHOVEL;
    public static DeferredItem<PickaxeItem> WEATHERED_COPPER_PICKAXE;
    public static DeferredItem<AxeItem> WEATHERED_COPPER_AXE;
    public static DeferredItem<HoeItem> WEATHERED_COPPER_HOE;
    public static DeferredItem<ShearsItem> WEATHERED_COPPER_SHEARS;

    public static DeferredItem<SwordItem> OXIDIZED_COPPER_SWORD;
    public static DeferredItem<ShovelItem> OXIDIZED_COPPER_SHOVEL;
    public static DeferredItem<PickaxeItem> OXIDIZED_COPPER_PICKAXE;
    public static DeferredItem<AxeItem> OXIDIZED_COPPER_AXE;
    public static DeferredItem<HoeItem> OXIDIZED_COPPER_HOE;
    public static DeferredItem<ShearsItem> OXIDIZED_COPPER_SHEARS;
    public static DeferredItem<ArmorItem> COPPER_HELMET;
    public static DeferredItem<ArmorItem> COPPER_CHESTPLATE;
    public static DeferredItem<ArmorItem> COPPER_LEGGINGS;
    public static DeferredItem<ArmorItem> COPPER_BOOTS;
    public static DeferredItem<ArmorItem> EXPOSED_COPPER_HELMET;
    public static DeferredItem<ArmorItem> EXPOSED_COPPER_CHESTPLATE;
    public static DeferredItem<ArmorItem> EXPOSED_COPPER_LEGGINGS;
    public static DeferredItem<ArmorItem> EXPOSED_COPPER_BOOTS;
    public static DeferredItem<ArmorItem> WEATHERED_COPPER_HELMET;
    public static DeferredItem<ArmorItem> WEATHERED_COPPER_CHESTPLATE;
    public static DeferredItem<ArmorItem> WEATHERED_COPPER_LEGGINGS;
    public static DeferredItem<ArmorItem> WEATHERED_COPPER_BOOTS;
    public static DeferredItem<ArmorItem> OXIDIZED_COPPER_HELMET;
    public static DeferredItem<ArmorItem> OXIDIZED_COPPER_CHESTPLATE;
    public static DeferredItem<ArmorItem> OXIDIZED_COPPER_LEGGINGS;
    public static DeferredItem<ArmorItem> OXIDIZED_COPPER_BOOTS;

    public static DeferredItem<ArmorItem> COPPER_CHAINMAIL_HELMET;
    public static DeferredItem<ArmorItem> COPPER_CHAINMAIL_CHESTPLATE;
    public static DeferredItem<ArmorItem> COPPER_CHAINMAIL_LEGGINGS;
    public static DeferredItem<ArmorItem> COPPER_CHAINMAIL_BOOTS;
    public static DeferredItem<ArmorItem> EXPOSED_COPPER_CHAINMAIL_HELMET;
    public static DeferredItem<ArmorItem> EXPOSED_COPPER_CHAINMAIL_CHESTPLATE;
    public static DeferredItem<ArmorItem> EXPOSED_COPPER_CHAINMAIL_LEGGINGS;
    public static DeferredItem<ArmorItem> EXPOSED_COPPER_CHAINMAIL_BOOTS;
    public static DeferredItem<ArmorItem> WEATHERED_COPPER_CHAINMAIL_HELMET;
    public static DeferredItem<ArmorItem> WEATHERED_COPPER_CHAINMAIL_CHESTPLATE;
    public static DeferredItem<ArmorItem> WEATHERED_COPPER_CHAINMAIL_LEGGINGS;
    public static DeferredItem<ArmorItem> WEATHERED_COPPER_CHAINMAIL_BOOTS;
    public static DeferredItem<ArmorItem> OXIDIZED_COPPER_CHAINMAIL_HELMET;
    public static DeferredItem<ArmorItem> OXIDIZED_COPPER_CHAINMAIL_CHESTPLATE;
    public static DeferredItem<ArmorItem> OXIDIZED_COPPER_CHAINMAIL_LEGGINGS;
    public static DeferredItem<ArmorItem> OXIDIZED_COPPER_CHAINMAIL_BOOTS;

    public static DeferredItem<AnimalArmorItem> COPPER_HORSE_ARMOR;
    public static DeferredItem<AnimalArmorItem> EXPOSED_COPPER_HORSE_ARMOR;
    public static DeferredItem<AnimalArmorItem> WEATHERED_COPPER_HORSE_ARMOR;
    public static DeferredItem<AnimalArmorItem> OXIDIZED_COPPER_HORSE_ARMOR;
    public static DeferredItem<Item> COPPER_GOLEM_SPAWN_EGG;

    public static DeferredItem<CopperBucketItem> COPPER_BUCKET;
    public static DeferredItem<CopperBucketItem> COPPER_WATER_BUCKET;
    public static DeferredItem<CopperBucketItem> COPPER_LAVA_BUCKET;
    public static DeferredItem<CopperMilkBucketItem> COPPER_MILK_BUCKET;
    public static DeferredItem<CopperSolidBucketItem> COPPER_POWDER_SNOW_BUCKET;

    public static DeferredItem<AnimalArmorItem> NETHERITE_HORSE_ARMOR;
    public static DeferredItem<SignItem> PALE_OAK_SIGN;
    public static DeferredItem<HangingSignItem> PALE_OAK_HANGING_SIGN;
    public static DeferredItem<PaleOakBoatItem> PALE_OAK_BOAT;
    public static DeferredItem<PaleOakBoatItem> PALE_OAK_CHEST_BOAT;

    public static final DeferredHolder<Item, BlockItem> GLASS_JAR = ITEMS.register("glass_jar", () -> new FireflyJarItem(ModBlocks.GLASS_JAR.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> FIREFLY_IN_A_JAR = ITEMS.register("firefly_in_a_jar", () -> new FireflyJarItem(ModBlocks.FIREFLY_IN_A_JAR.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> FIREFLIES_IN_A_JAR = ITEMS.register("fireflies_in_a_jar", () -> new FireflyJarItem(ModBlocks.FIREFLIES_IN_A_JAR.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> FIREFLY_JAR = ITEMS.register("firefly_jar", () -> new FireflyJarItem(ModBlocks.FIREFLY_JAR.get(), new Item.Properties()));

    public static final Map<Butterfly.Variant, DeferredItem<Item>> BUTTERFLY_JAR_ITEMS = new HashMap<>();
    public static final DeferredHolder<Item, com.otterly76.ott.item.custom.CaterpillarJarItem> CATERPILLAR_JAR = ITEMS.register("caterpillar_jar",
            () -> new com.otterly76.ott.item.custom.CaterpillarJarItem(ModBlocks.CATERPILLAR_JAR.get(), new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SMALL_FIREFLY_SPAWN_EGG = ITEMS.register("small_firefly_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SMALL_FIREFLY, 0x1E1E1E, 0xFFFF00, new Item.Properties()));

    public static final DeferredItem<Item> GHOST_SPAWN_EGG = ITEMS.register("ghost_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GHOST, 0xFFE0E0E0, 0xFFB0B0B0, new Item.Properties()));

    public static final DeferredItem<Item> SPECTRE_SPAWN_EGG = ITEMS.register("spectre_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SPECTRE, 0xFF404040, 0xFF800080, new Item.Properties()));

    public static final DeferredItem<Item> HAUNT_SPAWN_EGG = ITEMS.register("haunt_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.HAUNT, 0xFFD0A0A0, 0xFF808080, new Item.Properties()));

    public static final DeferredItem<Item> GEIST_SPAWN_EGG = ITEMS.register("geist_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GEIST, 0xFF408080, 0xFFC0C0C0, new Item.Properties()));

    public static final DeferredItem<Item> TREE_ENT_SPAWN_EGG = ITEMS.register("tree_ent_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TREE_ENT, 0x4B3621, 0x2A1B0E, new Item.Properties()));

    public static final DeferredItem<Item> HERMIT_KING_SPAWN_EGG = ITEMS.register("hermit_king_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.HERMIT_KING, 0x8B4513, 0xD2B48C, new Item.Properties()));

    public static final DeferredItem<Item> SEA_VIPER_SPAWN_EGG = ITEMS.register("sea_viper_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SEA_VIPER, 0x008B8B, 0x20B2AA, new Item.Properties()));

    public static final DeferredItem<Item> YETI_SPAWN_EGG = ITEMS.register("yeti_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.YETI, 0xFFFFFF, 0xE0E0E0, new Item.Properties()));

    public static final DeferredItem<Item> VILE_GATOR_SPAWN_EGG = ITEMS.register("vile_gator_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.VILE_GATOR, 0x5E5D24, 0xD2B961, new Item.Properties()));

    public static final DeferredItem<Item> PHOENIX_SPAWN_EGG = ITEMS.register("phoenix_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PHOENIX, 0xFF4500, 0xFFFF00, new Item.Properties()));

    public static final DeferredItem<Item> BABY_PHOENIX_SPAWN_EGG = ITEMS.register("baby_phoenix_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BABY_PHOENIX, 0xFF4500, 0xFFFF00, new Item.Properties()));

    public static final DeferredItem<Item> BONE_STALKER_SPAWN_EGG = ITEMS.register("bone_stalker_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BONE_STALKER, 0xC0C0C0, 0x808080, new Item.Properties()));

    public static final DeferredItem<Item> SHADOW_SPAWN_EGG = ITEMS.register("shadow_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SHADOW, 0x1D1D1D, 0x3D3D3D, new Item.Properties()));

    public static final DeferredItem<Item> CHERRY_TREE_ENT_SPAWN_EGG = ITEMS.register("cherry_tree_ent_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CHERRY_TREE_ENT, 0xFFB6C1, 0xFF69B4, new Item.Properties()));

    public static final DeferredItem<Item> GOLDEN_HERMIT_KING_SPAWN_EGG = ITEMS.register("golden_hermit_king_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GOLDEN_HERMIT_KING, 0xFFD700, 0xFFA500, new Item.Properties()));

    public static final DeferredItem<Item> CORAL_SEA_VIPER_SPAWN_EGG = ITEMS.register("coral_sea_viper_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CORAL_SEA_VIPER, 0xFF7F50, 0xFF6347, new Item.Properties()));

    public static final DeferredItem<Item> ARID_YETI_SPAWN_EGG = ITEMS.register("arid_yeti_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.ARID_YETI, 0xD2B48C, 0xA0522D, new Item.Properties()));

    public static final DeferredItem<Item> WIND_PHOENIX_SPAWN_EGG = ITEMS.register("wind_phoenix_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.WIND_PHOENIX, 0xADD8E6, 0x87CEEB, new Item.Properties()));

    public static final DeferredItem<Item> BABY_WIND_PHOENIX_SPAWN_EGG = ITEMS.register("baby_wind_phoenix_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BABY_WIND_PHOENIX, 0xADD8E6, 0x87CEEB, new Item.Properties()));

    public static final DeferredItem<Item> BOGGED_BONE_STALKER_SPAWN_EGG = ITEMS.register("bogged_bone_stalker_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BOGGED_BONE_STALKER, 0x556B2F, 0x8B4513, new Item.Properties()));

    public static final DeferredItem<Item> BOGGED_SHADOW_SPAWN_EGG = ITEMS.register("bogged_shadow_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BOGGED_SHADOW, 0x228B22, 0x006400, new Item.Properties()));

    public static final DeferredItem<Item> GILDED_TREE_ENT_SPAWN_EGG = ITEMS.register("gilded_tree_ent_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GILDED_TREE_ENT, 0xFFD700, 0xDAA520, new Item.Properties()));

    public static final DeferredItem<Item> BEAVER_SPAWN_EGG = ITEMS.register("beaver_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BEAVER, -11324642, -2528223, new Item.Properties()));
    public static final DeferredItem<Item> CHUPACABRA_SPAWN_EGG = ITEMS.register("chupacabra_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.CHUPACABRA, -11905738, -5775593, new Item.Properties()));
    public static final DeferredItem<Item> COUGAR_SPAWN_EGG = ITEMS.register("cougar_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.COUGAR, -5210796, -13295334, new Item.Properties()));
    public static final DeferredItem<Item> COYOTE_SPAWN_EGG = ITEMS.register("coyote_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.COYOTE, -7050677, -3228249, new Item.Properties()));
    public static final DeferredItem<Item> HOWLER_SPAWN_EGG = ITEMS.register("howler_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.HOWLER, -11844542, -2077120, new Item.Properties()));

    public static final DeferredItem<Item> MARMOT_SPAWN_EGG = ITEMS.register("marmot_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MARMOT, -9347768, -13162975, new Item.Properties()));
    public static final DeferredItem<Item> MOUSE_SPAWN_EGG = ITEMS.register("mouse_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MOUSE, -7312041, -4345946, new Item.Properties()));
    public static final DeferredItem<Item> PIT_VIPER_SPAWN_EGG = ITEMS.register("pit_viper_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.PIT_VIPER, -1, -1, new Item.Properties()));
    public static final DeferredItem<Item> RATTLESNAKE_SPAWN_EGG = ITEMS.register("rattlesnake_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.RATTLESNAKE, -1, -1, new Item.Properties()));
    public static final DeferredItem<Item> RINGTAIL_SPAWN_EGG = ITEMS.register("ringtail_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.RINGTAIL, -7570582, -2899793, new Item.Properties()));
    public static final DeferredItem<Item> SASQUATCH_SPAWN_EGG = ITEMS.register("sasquatch_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SASQUATCH, -13489885, -9872300, new Item.Properties()));
    public static final DeferredItem<Item> SKINWALKER_SPAWN_EGG = ITEMS.register("skinwalker_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.BEWITCHED_TIMBER_WOLF, -10397613, -8696780, new Item.Properties()));
    public static final DeferredItem<Item> SNAKE_SPAWN_EGG = ITEMS.register("snake_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SNAKE, -1, -1, new Item.Properties()));
    public static final DeferredItem<Item> SQUONK_SPAWN_EGG = ITEMS.register("squonk_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SQUONK, -5663100, -6393507, new Item.Properties()));
    public static final DeferredItem<Item> TURKEY_SPAWN_EGG = ITEMS.register("turkey_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TURKEY, -14144467, -7132129, new Item.Properties()));
    public static final DeferredItem<Item> WECHUGE_SPAWN_EGG = ITEMS.register("wechuge_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.WECHUGE, -6902101, -12107459, new Item.Properties()));
    public static final DeferredItem<Item> WENDIGO_SPAWN_EGG = ITEMS.register("wendigo_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.WENDIGO, -8421505, -8696780, new Item.Properties()));
    public static final DeferredItem<Item> WOLVERINE_SPAWN_EGG = ITEMS.register("wolverine_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.WOLVERINE, -14280173, -5206418, new Item.Properties()));

    // --- Friends and Foes ---
    public static final DeferredItem<Item> FIDDLER_CRAB_SPAWN_EGG = ITEMS.register("fiddler_crab_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.FIDDLER_CRAB, 0x333077, 0xFE984B, new Item.Properties()));
    public static final DeferredItem<Item> GLARE_SPAWN_EGG = ITEMS.register("glare_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.GLARE, 0x70922D, 0x6A5227, new Item.Properties()));
    public static final DeferredItem<Item> ICEOLOGER_SPAWN_EGG = ITEMS.register("iceologer_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.ICEOLOGER, 0x173873, 0x949B9B, new Item.Properties()));
    public static final DeferredItem<Item> ILLUSIONER_SPAWN_EGG = ITEMS.register("illusioner_spawn_egg",
            () -> new DeferredSpawnEggItem(() -> net.minecraft.world.entity.EntityType.ILLUSIONER, 0x603E5C, 0x888E8E, new Item.Properties()));
    public static final DeferredItem<Item> MAULER_SPAWN_EGG = ITEMS.register("mauler_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.MAULER, 0x534F25, 0x817B39, new Item.Properties()));
    public static final DeferredItem<Item> RASCAL_SPAWN_EGG = ITEMS.register("rascal_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.RASCAL, 0x05736A, 0x8A521C, new Item.Properties()));
    public static final DeferredItem<Item> TUFF_GOLEM_SPAWN_EGG = ITEMS.register("tuff_golem_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.TUFF_GOLEM, 0xA0A297, 0x5D5D52, new Item.Properties()));
    public static final DeferredItem<Item> WILDFIRE_SPAWN_EGG = ITEMS.register("wildfire_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.WILDFIRE, 0x6C3100, 0xFFD528, new Item.Properties()));
    // --- Ecologics ---
    public static final DeferredItem<BlockItem> COCONUT = ITEMS.register("coconut",
            () -> new BlockItem(ModBlocks.COCONUT.get(), new Item.Properties()));
    public static final DeferredItem<Item> COCONUT_CRAB_SPAWN_EGG = ITEMS.register("coconut_crab_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.COCONUT_CRAB, 0xEEC97C, 0x8B4513, new Item.Properties()));
    public static final DeferredItem<Item> SAND_CRAB_SPAWN_EGG = ITEMS.register("sand_crab_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.SAND_CRAB, 0xD2B48C, 0xA0522D, new Item.Properties()));

    public static final DeferredItem<Item> CRAB_CLAW = ITEMS.register("crab_claw",
            () -> new Item(new Item.Properties()));
    public static final DeferredItem<BlockItem> CRAB_EGG = ITEMS.register("crab_egg",
            () -> new BlockItem(ModBlocks.CRAB_EGG.get(), new Item.Properties()));
    public static final DeferredItem<ArmorItem> WILDFIRE_CROWN = ITEMS.register("wildfire_crown",
            () -> new ArmorItem(ModArmorMaterials.WILDFIRE_CROWN, ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1).durability(481).fireResistant()));
    public static final DeferredItem<Item> WILDFIRE_CROWN_FRAGMENT = ITEMS.register("wildfire_crown_fragment",
            () -> new Item(new Item.Properties().fireResistant()));

    // -------------------------------------------------------------------------
    // --- Mosaic / Fresco decorative block items ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> WATER_MOSAIC_BORDER      = registerBlockItem("water_mosaic_border",      ModBlocks.WATER_MOSAIC_BORDER);
    public static final DeferredHolder<Item, BlockItem> WATER_MOSAIC_GEOMETRIC   = registerBlockItem("water_mosaic_geometric",   ModBlocks.WATER_MOSAIC_GEOMETRIC);
    public static final DeferredHolder<Item, BlockItem> WATER_MOSAIC_PATTERN     = registerBlockItem("water_mosaic_pattern",     ModBlocks.WATER_MOSAIC_PATTERN);
    public static final DeferredHolder<Item, BlockItem> WATER_MOSAIC_DELICATE    = registerBlockItem("water_mosaic_delicate",    ModBlocks.WATER_MOSAIC_DELICATE);
    public static final DeferredHolder<Item, BlockItem> WATER_MOSAIC_TRADITIONAL = registerBlockItem("water_mosaic_traditional", ModBlocks.WATER_MOSAIC_TRADITIONAL);
    public static final DeferredHolder<Item, BlockItem> WATER_MOSAIC_RECESS      = registerBlockItem("water_mosaic_recess",      ModBlocks.WATER_MOSAIC_RECESS);
    public static final DeferredHolder<Item, BlockItem> MOSAIC_FLOOR             = registerBlockItem("mosaic_floor",             ModBlocks.MOSAIC_FLOOR);
    public static final DeferredHolder<Item, BlockItem> MOSAIC_FLOOR_DELICATE    = registerBlockItem("mosaic_floor_delicate",    ModBlocks.MOSAIC_FLOOR_DELICATE);
    public static final DeferredHolder<Item, BlockItem> MOSAIC_FLOOR_ROSETTE     = registerBlockItem("mosaic_floor_rosette",     ModBlocks.MOSAIC_FLOOR_ROSETTE);
    public static final DeferredHolder<Item, BlockItem> ROMAN_FRESCO_RED         = registerBlockItem("roman_fresco_red",         ModBlocks.ROMAN_FRESCO_RED);
    public static final DeferredHolder<Item, BlockItem> ROMAN_FRESCO_BLACK       = registerBlockItem("roman_fresco_black",       ModBlocks.ROMAN_FRESCO_BLACK);

    // -------------------------------------------------------------------------
    // --- Stone brick functional block items ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_ARROWSLIT     = registerBlockItem("stone_bricks_arrowslit",     ModBlocks.STONE_BRICKS_ARROWSLIT);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_MACHICOLATION = registerBlockItem("stone_bricks_machicolation",  ModBlocks.STONE_BRICKS_MACHICOLATION);
    public static final DeferredHolder<Item, BlockItem> WATER_SOURCE_TRICKLE       = registerBlockItem("water_source_trickle",        ModBlocks.WATER_SOURCE_TRICKLE);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_FAUCET        = registerBlockItem("stone_bricks_faucet",         ModBlocks.STONE_BRICKS_FAUCET);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_POOL          = registerBlockItem("stone_bricks_pool",           ModBlocks.STONE_BRICKS_POOL);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_SMALL_POOL    = registerBlockItem("stone_bricks_small_pool",     ModBlocks.STONE_BRICKS_SMALL_POOL);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_WATER_JET     = registerBlockItem("stone_bricks_water_jet",      ModBlocks.STONE_BRICKS_WATER_JET);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Limestone (French) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_BRICKS          = registerBlockItem("limestone_bricks",          ModBlocks.LIMESTONE_BRICKS);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_BRICKS_SLAB     = registerBlockItem("limestone_bricks_slab",     ModBlocks.LIMESTONE_BRICKS_SLAB);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_BRICKS_STAIRS   = registerBlockItem("limestone_bricks_stairs",   ModBlocks.LIMESTONE_BRICKS_STAIRS);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_BRICKS_WALL     = registerBlockItem("limestone_bricks_wall",     ModBlocks.LIMESTONE_BRICKS_WALL);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_BRICKS_EDGE     = registerBlockItem("limestone_bricks_edge",     ModBlocks.LIMESTONE_BRICKS_EDGE);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_BRICKS_PLATE    = registerBlockItem("limestone_bricks_plate",    ModBlocks.LIMESTONE_BRICKS_PLATE);
    public static final DeferredHolder<Item, BlockItem> LIMESTONE_BALUSTER        = registerBlockItem("limestone_baluster",        ModBlocks.LIMESTONE_BALUSTER);
    public static final DeferredHolder<Item, BlockItem> COBBLED_LIMESTONE         = registerBlockItem("cobbled_limestone",         ModBlocks.COBBLED_LIMESTONE);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Marble (Roman) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> MARBLE                    = registerBlockItem("marble",                    ModBlocks.MARBLE);
    public static final DeferredHolder<Item, BlockItem> MARBLE_PILLAR             = registerBlockItem("marble_pillar",             ModBlocks.MARBLE_PILLAR);
    public static final DeferredHolder<Item, BlockItem> MARBLE_COFFER             = registerBlockItem("marble_coffer",             ModBlocks.MARBLE_COFFER);
    public static final DeferredHolder<Item, BlockItem> MARBLE_COFFER_SLAB        = registerBlockItem("marble_coffer_slab",        ModBlocks.MARBLE_COFFER_SLAB);
    public static final DeferredHolder<Item, BlockItem> MARBLE_FANCY_FENCE        = registerBlockItem("marble_fancy_fence",        ModBlocks.MARBLE_FANCY_FENCE);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Sandstone decorative (Roman) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_PLATE                         = registerBlockItem("sandstone_plate",                         ModBlocks.SANDSTONE_PLATE);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_EDGE                          = registerBlockItem("sandstone_edge",                          ModBlocks.SANDSTONE_EDGE);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_CRENELATION                   = registerBlockItem("sandstone_crenelation",                   ModBlocks.SANDSTONE_CRENELATION);
    public static final DeferredHolder<Item, BlockItem> CUT_SANDSTONE_STAIRS                    = registerBlockItem("cut_sandstone_stairs",                    ModBlocks.CUT_SANDSTONE_STAIRS);
    public static final DeferredHolder<Item, BlockItem> CUT_SANDSTONE_PLATE                     = registerBlockItem("cut_sandstone_plate",                     ModBlocks.CUT_SANDSTONE_PLATE);
    public static final DeferredHolder<Item, BlockItem> CUT_SANDSTONE_EDGE                      = registerBlockItem("cut_sandstone_edge",                      ModBlocks.CUT_SANDSTONE_EDGE);
    public static final DeferredHolder<Item, BlockItem> SMOOTH_SANDSTONE_PLATE                  = registerBlockItem("smooth_sandstone_plate",                  ModBlocks.SMOOTH_SANDSTONE_PLATE);
    public static final DeferredHolder<Item, BlockItem> SMOOTH_SANDSTONE_EDGE                   = registerBlockItem("smooth_sandstone_edge",                   ModBlocks.SMOOTH_SANDSTONE_EDGE);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BOT_OCHRE_ROOF_TILES_TOP      = registerBlockItem("sandstone_bot_ochre_roof_tiles_top",      ModBlocks.SANDSTONE_BOT_OCHRE_ROOF_TILES_TOP);
    public static final DeferredHolder<Item, BlockItem> CUT_SANDSTONE_BOT_OCHRE_ROOF_TILES_TOP  = registerBlockItem("cut_sandstone_bot_ochre_roof_tiles_top",  ModBlocks.CUT_SANDSTONE_BOT_OCHRE_ROOF_TILES_TOP);
    public static final DeferredHolder<Item, BlockItem> SMOOTH_SANDSTONE_BOT_OCHRE_ROOF_TILES_TOP = registerBlockItem("smooth_sandstone_bot_ochre_roof_tiles_top", ModBlocks.SMOOTH_SANDSTONE_BOT_OCHRE_ROOF_TILES_TOP);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Ochre Roof Tiles (Roman) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> OCHRE_ROOF_TILES          = registerBlockItem("ochre_roof_tiles",          ModBlocks.OCHRE_ROOF_TILES);
    public static final DeferredHolder<Item, BlockItem> OCHRE_ROOF_TILES_STAIRS   = registerBlockItem("ochre_roof_tiles_stairs",   ModBlocks.OCHRE_ROOF_TILES_STAIRS);
    public static final DeferredHolder<Item, BlockItem> OCHRE_ROOF_TILES_WALL     = registerBlockItem("ochre_roof_tiles_wall",     ModBlocks.OCHRE_ROOF_TILES_WALL);
    public static final DeferredHolder<Item, BlockItem> OCHRE_ROOF_TILES_EDGE     = registerBlockItem("ochre_roof_tiles_edge",     ModBlocks.OCHRE_ROOF_TILES_EDGE);
    public static final DeferredHolder<Item, BlockItem> OCHRE_ROOF_TILES_PLATE    = registerBlockItem("ochre_roof_tiles_plate",    ModBlocks.OCHRE_ROOF_TILES_PLATE);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Flat/Gray Roof Tiles (General) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> FLAT_ROOF_TILES           = registerBlockItem("flat_roof_tiles",           ModBlocks.FLAT_ROOF_TILES);
    public static final DeferredHolder<Item, BlockItem> FLAT_ROOF_TILES_SLAB      = registerBlockItem("flat_roof_tiles_slab",      ModBlocks.FLAT_ROOF_TILES_SLAB);
    public static final DeferredHolder<Item, BlockItem> FLAT_ROOF_TILES_STAIRS    = registerBlockItem("flat_roof_tiles_stairs",    ModBlocks.FLAT_ROOF_TILES_STAIRS);
    public static final DeferredHolder<Item, BlockItem> FLAT_ROOF_TILES_WALL      = registerBlockItem("flat_roof_tiles_wall",      ModBlocks.FLAT_ROOF_TILES_WALL);
    public static final DeferredHolder<Item, BlockItem> FLAT_ROOF_TILES_EDGE      = registerBlockItem("flat_roof_tiles_edge",      ModBlocks.FLAT_ROOF_TILES_EDGE);
    public static final DeferredHolder<Item, BlockItem> FLAT_ROOF_TILES_PLATE     = registerBlockItem("flat_roof_tiles_plate",     ModBlocks.FLAT_ROOF_TILES_PLATE);
    public static final DeferredHolder<Item, BlockItem> GRAY_ROOF_TILES           = registerBlockItem("gray_roof_tiles",           ModBlocks.GRAY_ROOF_TILES);
    public static final DeferredHolder<Item, BlockItem> GRAY_ROOF_TILES_SLAB      = registerBlockItem("gray_roof_tiles_slab",      ModBlocks.GRAY_ROOF_TILES_SLAB);
    public static final DeferredHolder<Item, BlockItem> GRAY_ROOF_TILES_STAIRS    = registerBlockItem("gray_roof_tiles_stairs",    ModBlocks.GRAY_ROOF_TILES_STAIRS);
    public static final DeferredHolder<Item, BlockItem> GRAY_ROOF_TILES_WALL      = registerBlockItem("gray_roof_tiles_wall",      ModBlocks.GRAY_ROOF_TILES_WALL);
    public static final DeferredHolder<Item, BlockItem> GRAY_ROOF_TILES_EDGE      = registerBlockItem("gray_roof_tiles_edge",      ModBlocks.GRAY_ROOF_TILES_EDGE);
    public static final DeferredHolder<Item, BlockItem> GRAY_ROOF_TILES_PLATE     = registerBlockItem("gray_roof_tiles_plate",     ModBlocks.GRAY_ROOF_TILES_PLATE);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Roofing Slates (General) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> ROOFING_SLATES            = registerBlockItem("roofing_slates",            ModBlocks.ROOFING_SLATES);
    public static final DeferredHolder<Item, BlockItem> ROOFING_SLATES_SLAB       = registerBlockItem("roofing_slates_slab",       ModBlocks.ROOFING_SLATES_SLAB);
    public static final DeferredHolder<Item, BlockItem> ROOFING_SLATES_STAIRS     = registerBlockItem("roofing_slates_stairs",     ModBlocks.ROOFING_SLATES_STAIRS);
    public static final DeferredHolder<Item, BlockItem> ROOFING_SLATES_WALL       = registerBlockItem("roofing_slates_wall",       ModBlocks.ROOFING_SLATES_WALL);
    public static final DeferredHolder<Item, BlockItem> ROOFING_SLATES_EDGE       = registerBlockItem("roofing_slates_edge",       ModBlocks.ROOFING_SLATES_EDGE);
    public static final DeferredHolder<Item, BlockItem> ROOFING_SLATES_PLATE      = registerBlockItem("roofing_slates_plate",      ModBlocks.ROOFING_SLATES_PLATE);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Rammed Dirt, Stepping Stones (General) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> RAMMED_DIRT               = registerBlockItem("rammed_dirt",               ModBlocks.RAMMED_DIRT);
    public static final DeferredHolder<Item, BlockItem> STEPPING_STONES           = registerBlockItem("stepping_stones",           ModBlocks.STEPPING_STONES);
    public static final DeferredHolder<Item, BlockItem> STEPPING_STONES_SLAB      = registerBlockItem("stepping_stones_slab",      ModBlocks.STEPPING_STONES_SLAB);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Thatch (General) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> THATCH_WHEAT              = registerBlockItem("thatch_wheat",              ModBlocks.THATCH_WHEAT);
    public static final DeferredHolder<Item, BlockItem> THATCH_WHEAT_EDGE         = registerBlockItem("thatch_wheat_edge",         ModBlocks.THATCH_WHEAT_EDGE);
    public static final DeferredHolder<Item, BlockItem> THATCH_WHEAT_PLATE        = registerBlockItem("thatch_wheat_plate",        ModBlocks.THATCH_WHEAT_PLATE);
    public static final DeferredHolder<Item, BlockItem> THATCH_WHEAT_SLAB         = registerBlockItem("thatch_wheat_slab",         ModBlocks.THATCH_WHEAT_SLAB);
    public static final DeferredHolder<Item, BlockItem> THATCH_WHEAT_STAIRS       = registerBlockItem("thatch_wheat_stairs",       ModBlocks.THATCH_WHEAT_STAIRS);
    public static final DeferredHolder<Item, BlockItem> THATCH_BAMBOO             = registerBlockItem("thatch_bamboo",             ModBlocks.THATCH_BAMBOO);
    public static final DeferredHolder<Item, BlockItem> THATCH_BAMBOO_EDGE        = registerBlockItem("thatch_bamboo_edge",        ModBlocks.THATCH_BAMBOO_EDGE);
    public static final DeferredHolder<Item, BlockItem> THATCH_BAMBOO_PLATE       = registerBlockItem("thatch_bamboo_plate",       ModBlocks.THATCH_BAMBOO_PLATE);
    public static final DeferredHolder<Item, BlockItem> THATCH_BAMBOO_SLAB        = registerBlockItem("thatch_bamboo_slab",        ModBlocks.THATCH_BAMBOO_SLAB);
    public static final DeferredHolder<Item, BlockItem> THATCH_BAMBOO_STAIRS      = registerBlockItem("thatch_bamboo_stairs",      ModBlocks.THATCH_BAMBOO_STAIRS);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 3: Waxed Oak (German) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_PLANKS                   = registerBlockItem("waxed_oak_planks",                   ModBlocks.WAXED_OAK_PLANKS);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_PLANKS_SLAB              = registerBlockItem("waxed_oak_planks_slab",              ModBlocks.WAXED_OAK_PLANKS_SLAB);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_PLANKS_STAIRS            = registerBlockItem("waxed_oak_planks_stairs",            ModBlocks.WAXED_OAK_PLANKS_STAIRS);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_LOG_STRIPPED             = registerBlockItem("waxed_oak_log_stripped",             ModBlocks.WAXED_OAK_LOG_STRIPPED);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_FENCE                    = registerBlockItem("waxed_oak_fence",                    ModBlocks.WAXED_OAK_FENCE);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_FENCE_GATE               = registerBlockItem("waxed_oak_fence_gate",               ModBlocks.WAXED_OAK_FENCE_GATE);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_DOOR                     = registerBlockItem("waxed_oak_door",                     ModBlocks.WAXED_OAK_DOOR);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_TRAPDOOR                 = registerBlockItem("waxed_oak_trapdoor",                 ModBlocks.WAXED_OAK_TRAPDOOR);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_BEAM                     = registerBlockItem("waxed_oak_beam",                     ModBlocks.WAXED_OAK_BEAM);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_PERGOLA                  = registerBlockItem("waxed_oak_pergola",                  ModBlocks.WAXED_OAK_PERGOLA);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_PLANKS_PLATE             = registerBlockItem("waxed_oak_planks_plate",             ModBlocks.WAXED_OAK_PLANKS_PLATE);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_PLANKS_EDGE              = registerBlockItem("waxed_oak_planks_edge",              ModBlocks.WAXED_OAK_PLANKS_EDGE);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_SUPPORT_BEAM             = registerBlockItem("waxed_oak_support_beam",             ModBlocks.WAXED_OAK_SUPPORT_BEAM);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_SUPPORT_SLAB             = registerBlockItem("waxed_oak_support_slab",             ModBlocks.WAXED_OAK_SUPPORT_SLAB);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_BALUSTER                 = registerBlockItem("waxed_oak_baluster",                 ModBlocks.WAXED_OAK_BALUSTER);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_FRAMED_RAMMED_DIRT       = registerBlockItem("waxed_oak_framed_rammed_dirt",       ModBlocks.WAXED_OAK_FRAMED_RAMMED_DIRT);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_FRAMED_RAMMED_DIRT_PILLAR= registerBlockItem("waxed_oak_framed_rammed_dirt_pillar", ModBlocks.WAXED_OAK_FRAMED_RAMMED_DIRT_PILLAR);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_TIMBER_FRAME             = registerBlockItem("waxed_oak_timber_frame",             ModBlocks.WAXED_OAK_TIMBER_FRAME);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_TIMBER_FRAME_CORNER      = registerBlockItem("waxed_oak_timber_frame_corner",      ModBlocks.WAXED_OAK_TIMBER_FRAME_CORNER);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_TIMBER_FRAME_CROSSED     = registerBlockItem("waxed_oak_timber_frame_crossed",     ModBlocks.WAXED_OAK_TIMBER_FRAME_CROSSED);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_TIMBER_FRAME_PILLAR      = registerBlockItem("waxed_oak_timber_frame_pillar",      ModBlocks.WAXED_OAK_TIMBER_FRAME_PILLAR);
    public static final DeferredHolder<Item, BlockItem> WAXED_OAK_TIMBER_FRAME_SQUARED     = registerBlockItem("waxed_oak_timber_frame_squared",     ModBlocks.WAXED_OAK_TIMBER_FRAME_SQUARED);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 3: Stone Bricks Masonry + German misc ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_MASONRY         = registerBlockItem("stone_bricks_masonry",         ModBlocks.STONE_BRICKS_MASONRY);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_MASONRY_SLAB    = registerBlockItem("stone_bricks_masonry_slab",    ModBlocks.STONE_BRICKS_MASONRY_SLAB);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_MASONRY_STAIRS  = registerBlockItem("stone_bricks_masonry_stairs",  ModBlocks.STONE_BRICKS_MASONRY_STAIRS);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_MASONRY_WALL    = registerBlockItem("stone_bricks_masonry_wall",    ModBlocks.STONE_BRICKS_MASONRY_WALL);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_MASONRY_EDGE    = registerBlockItem("stone_bricks_masonry_edge",    ModBlocks.STONE_BRICKS_MASONRY_EDGE);
    public static final DeferredHolder<Item, BlockItem> STONE_BRICKS_MASONRY_PLATE   = registerBlockItem("stone_bricks_masonry_plate",   ModBlocks.STONE_BRICKS_MASONRY_PLATE);
    public static final DeferredHolder<Item, BlockItem> LATTICE_GLASS                = registerBlockItem("lattice_glass",                ModBlocks.LATTICE_GLASS);
    public static final DeferredHolder<Item, BlockItem> CURVED_RAKED_GRAVEL          = registerBlockItem("curved_raked_gravel",          ModBlocks.CURVED_RAKED_GRAVEL);
    public static final DeferredHolder<Item, BlockItem> STRAIGHT_RAKED_GRAVEL        = registerBlockItem("straight_raked_gravel",        ModBlocks.STRAIGHT_RAKED_GRAVEL);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 3: Charred Spruce (Japanese) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_PLANKS               = registerBlockItem("charred_spruce_planks",               ModBlocks.CHARRED_SPRUCE_PLANKS);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_PLANKS_SLAB          = registerBlockItem("charred_spruce_planks_slab",          ModBlocks.CHARRED_SPRUCE_PLANKS_SLAB);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_PLANKS_STAIRS        = registerBlockItem("charred_spruce_planks_stairs",        ModBlocks.CHARRED_SPRUCE_PLANKS_STAIRS);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_LOG_STRIPPED         = registerBlockItem("charred_spruce_log_stripped",         ModBlocks.CHARRED_SPRUCE_LOG_STRIPPED);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_FENCE                = registerBlockItem("charred_spruce_fence",                ModBlocks.CHARRED_SPRUCE_FENCE);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_FENCE_GATE           = registerBlockItem("charred_spruce_fence_gate",           ModBlocks.CHARRED_SPRUCE_FENCE_GATE);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_DOOR                 = registerBlockItem("charred_spruce_door",                 ModBlocks.CHARRED_SPRUCE_DOOR);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_TRAPDOOR             = registerBlockItem("charred_spruce_trapdoor",             ModBlocks.CHARRED_SPRUCE_TRAPDOOR);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_BOARDS               = registerBlockItem("charred_spruce_boards",               ModBlocks.CHARRED_SPRUCE_BOARDS);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_FOUNDATION           = registerBlockItem("charred_spruce_foundation",           ModBlocks.CHARRED_SPRUCE_FOUNDATION);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_FOUNDATION_SLAB      = registerBlockItem("charred_spruce_foundation_slab",      ModBlocks.CHARRED_SPRUCE_FOUNDATION_SLAB);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_BEAM                 = registerBlockItem("charred_spruce_beam",                 ModBlocks.CHARRED_SPRUCE_BEAM);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_PERGOLA              = registerBlockItem("charred_spruce_pergola",              ModBlocks.CHARRED_SPRUCE_PERGOLA);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_PLANKS_PLATE         = registerBlockItem("charred_spruce_planks_plate",         ModBlocks.CHARRED_SPRUCE_PLANKS_PLATE);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_PLANKS_EDGE          = registerBlockItem("charred_spruce_planks_edge",          ModBlocks.CHARRED_SPRUCE_PLANKS_EDGE);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_SUPPORT_BEAM         = registerBlockItem("charred_spruce_support_beam",         ModBlocks.CHARRED_SPRUCE_SUPPORT_BEAM);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_SUPPORT_SLAB         = registerBlockItem("charred_spruce_support_slab",         ModBlocks.CHARRED_SPRUCE_SUPPORT_SLAB);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_TIMBER_FRAME         = registerBlockItem("charred_spruce_timber_frame",         ModBlocks.CHARRED_SPRUCE_TIMBER_FRAME);
    public static final DeferredHolder<Item, BlockItem> CHARRED_SPRUCE_TIMBER_FRAME_PILLAR  = registerBlockItem("charred_spruce_timber_frame_pillar",  ModBlocks.CHARRED_SPRUCE_TIMBER_FRAME_PILLAR);

    // -------------------------------------------------------------------------
    // --- DoTB Phase 4: Persian Sandstone Bricks ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS                          = registerBlockItem("sandstone_bricks",                          ModBlocks.SANDSTONE_BRICKS);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_SLAB                     = registerBlockItem("sandstone_bricks_slab",                     ModBlocks.SANDSTONE_BRICKS_SLAB);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_STAIRS                   = registerBlockItem("sandstone_bricks_stairs",                   ModBlocks.SANDSTONE_BRICKS_STAIRS);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_WALL                     = registerBlockItem("sandstone_bricks_wall",                     ModBlocks.SANDSTONE_BRICKS_WALL);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_EDGE                     = registerBlockItem("sandstone_bricks_edge",                     ModBlocks.SANDSTONE_BRICKS_EDGE);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_PLATE                    = registerBlockItem("sandstone_bricks_plate",                    ModBlocks.SANDSTONE_BRICKS_PLATE);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_TURQUOISE_PATTERN        = registerBlockItem("sandstone_bricks_turquoise_pattern",        ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_TURQUOISE_PATTERN_SLAB   = registerBlockItem("sandstone_bricks_turquoise_pattern_slab",   ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_SLAB);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_TURQUOISE_PATTERN_STAIRS = registerBlockItem("sandstone_bricks_turquoise_pattern_stairs", ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_STAIRS);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_TURQUOISE_PATTERN_WALL   = registerBlockItem("sandstone_bricks_turquoise_pattern_wall",   ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_WALL);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_TURQUOISE_PATTERN_EDGE   = registerBlockItem("sandstone_bricks_turquoise_pattern_edge",   ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_EDGE);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_BRICKS_TURQUOISE_PATTERN_PLATE  = registerBlockItem("sandstone_bricks_turquoise_pattern_plate",  ModBlocks.SANDSTONE_BRICKS_TURQUOISE_PATTERN_PLATE);
    // -------------------------------------------------------------------------
    // --- DoTB Phase 4: Persian Carpets ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> PERSIAN_CARPET_RED          = registerBlockItem("persian_carpet_red",          ModBlocks.PERSIAN_CARPET_RED);
    public static final DeferredHolder<Item, BlockItem> PERSIAN_CARPET_DELICATE_RED = registerBlockItem("persian_carpet_delicate_red", ModBlocks.PERSIAN_CARPET_DELICATE_RED);
    public static final DeferredHolder<Item, BlockItem> SANDSTONE_SCULPTED_RELIEF   = registerBlockItem("sandstone_sculpted_relief",   ModBlocks.SANDSTONE_SCULPTED_RELIEF);
    // -------------------------------------------------------------------------
    // --- DoTB Phase 4: Gold Plated Smooth (Persian) ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> GOLD_PLATED_SMOOTH_BLOCK   = registerBlockItem("gold_plated_smooth_block",   ModBlocks.GOLD_PLATED_SMOOTH_BLOCK);
    public static final DeferredHolder<Item, BlockItem> GOLD_PLATED_SMOOTH_SLAB    = registerBlockItem("gold_plated_smooth_slab",    ModBlocks.GOLD_PLATED_SMOOTH_SLAB);
    public static final DeferredHolder<Item, BlockItem> GOLD_PLATED_SMOOTH_STAIRS  = registerBlockItem("gold_plated_smooth_stairs",  ModBlocks.GOLD_PLATED_SMOOTH_STAIRS);
    public static final DeferredHolder<Item, BlockItem> GOLD_PLATED_SMOOTH_WALL    = registerBlockItem("gold_plated_smooth_wall",    ModBlocks.GOLD_PLATED_SMOOTH_WALL);
    public static final DeferredHolder<Item, BlockItem> GOLD_PLATED_SMOOTH_EDGE    = registerBlockItem("gold_plated_smooth_edge",    ModBlocks.GOLD_PLATED_SMOOTH_EDGE);
    public static final DeferredHolder<Item, BlockItem> GOLD_PLATED_SMOOTH_PLATE   = registerBlockItem("gold_plated_smooth_plate",   ModBlocks.GOLD_PLATED_SMOOTH_PLATE);

    // -------------------------------------------------------------------------
    // --- Oak structural block items ---
    // -------------------------------------------------------------------------
    public static final DeferredHolder<Item, BlockItem> OAK_PERGOLA       = registerBlockItem("oak_pergola",       ModBlocks.OAK_PERGOLA);
    public static final DeferredHolder<Item, BlockItem> OAK_BEAM          = registerBlockItem("oak_beam",          ModBlocks.OAK_BEAM);
    public static final DeferredHolder<Item, BlockItem> OAK_PLANKS_PLATE  = registerBlockItem("oak_planks_plate",  ModBlocks.OAK_PLANKS_PLATE);
    public static final DeferredHolder<Item, BlockItem> OAK_PLANKS_EDGE   = registerBlockItem("oak_planks_edge",   ModBlocks.OAK_PLANKS_EDGE);
    public static final DeferredHolder<Item, BlockItem> OAK_BANNISTER      = registerBlockItem("oak_bannister",      ModBlocks.OAK_BANNISTER);
    public static final DeferredHolder<Item, BlockItem> OAK_SUPPORT_SLAB  = registerBlockItem("oak_support_slab",  ModBlocks.OAK_SUPPORT_SLAB);
    public static final DeferredHolder<Item, BlockItem> OAK_SUPPORT_BEAM  = registerBlockItem("oak_support_beam",  ModBlocks.OAK_SUPPORT_BEAM);

    public static void register(IEventBus eventBus) {
        // 1. Run dynamic logic to set up the registration entries
        initializeDynamicItems();

        // 2. Attach the registers to the mod event bus
        ITEMS.register(eventBus);
    }

    private static void initializeDynamicItems() {
        for (Butterfly.Variant variant : Butterfly.Variant.values()) {
            BUTTERFLY_JAR_ITEMS.put(variant, ITEMS.register("butterfly_jar_" + variant.getName(),
                    () -> new com.otterly76.ott.item.custom.ButterflyJarItem(ModBlocks.BUTTERFLY_JARS.get(variant).get(), variant, new Item.Properties().stacksTo(1))));
        }

        // REGISTRATION: Gradients
        ModBlocks.getAllGradientBlocks().forEach(block ->
                ITEMS.register(block.getId().getPath(), () -> new GradientItem<>(new Item.Properties(), block.get())));

        // REGISTRATION: Test, Limestone, Seaglass
        ModBlocks.TESTBLOCK.forEach(ModItems::registerBlockItem);
        ModBlocks.LIMESTONE.forEach(ModItems::registerBlockItem);
        ModBlocks.SEAGLASS.forEach(ModItems::registerBlockItem);

        // REGISTRATION: Hedges
        ModBlocks.PARTICLE_HEDGES.values().forEach(ModItems::registerBlockItem);
        ModBlocks.CREEPING_HEDGES.values().forEach(ModItems::registerBlockItem);

        // REGISTRATION: Wood Sets
        ModBlocks.WOOD_SETS.forEach((setName, setBlocks) -> {
            registerBlockItem(setBlocks.log());
            registerBlockItem(setBlocks.wood());
            registerBlockItem(setBlocks.strippedLog());
            registerBlockItem(setBlocks.strippedWood());
            registerBlockItem(setBlocks.planks());
            registerBlockItem(setBlocks.stairs());
            registerBlockItem(setBlocks.slab());
            registerBlockItem(setBlocks.fence());
            registerBlockItem(setBlocks.fenceGate());
            registerBlockItem(setBlocks.door());
            registerBlockItem(setBlocks.trapdoor());
            registerBlockItem(setBlocks.button());
            registerBlockItem(setBlocks.pressurePlate());
            registerBlockItem(setBlocks.leaves());
            registerBlockItem(setBlocks.sapling());
            registerBlockItem(setBlocks.pergola());
            registerBlockItem(setBlocks.beam());
            registerBlockItem(setBlocks.planksPlate());
            registerBlockItem(setBlocks.planksEdge());
            registerBlockItem(setBlocks.bannister());
            registerBlockItem(setBlocks.supportSlab());
            registerBlockItem(setBlocks.supportBeam());

            WOOD_SET_SIGNS.put(setName, registerSign(setName + "_sign", setBlocks.sign(), setBlocks.wallSign()));

            WOOD_SET_HANGING_SIGNS.put(setName, registerHangingSign(setName + "_hanging_sign", setBlocks.hangingSign(), setBlocks.wallHangingSign()));

            WOOD_SET_BOATS.put(setName, ITEMS.register(setName + "_boat",
                    () -> new ModBoatItem(ModEntities.WOOD_SET_BOATS.get(setName), new Item.Properties().stacksTo(1),
                            boat -> { if (boat instanceof OttWoodSetBoatEntity b) b.setWoodSetName(setName); })));

            WOOD_SET_CHEST_BOATS.put(setName, ITEMS.register(setName + "_chest_boat",
                    () -> new ModBoatItem(ModEntities.WOOD_SET_CHEST_BOATS.get(setName), new Item.Properties().stacksTo(1),
                            boat -> { if (boat instanceof OttWoodSetChestBoatEntity b) b.setWoodSetName(setName); })));
        });

        // REGISTRATION: Vanilla Wood Structural Blocks
        // Oak structural items are already registered as static fields above; skip them here.
        ModBlocks.VANILLA_STRUCTURAL_SETS.forEach((name, set) -> {
            if (name.equals("oak")) return;
            registerBlockItem(set.pergola());
            registerBlockItem(set.beam());
            registerBlockItem(set.planksPlate());
            registerBlockItem(set.planksEdge());
            registerBlockItem(set.bannister());
            registerBlockItem(set.supportSlab());
            registerBlockItem(set.supportBeam());
        });

        // REGISTRATION: Wood Wall + Lattice Block Items
        ModBlocks.VANILLA_WALLS.values().forEach(ModItems::registerBlockItem);
        ModBlocks.VANILLA_LATTICES.values().forEach(ModItems::registerBlockItem);

        // REGISTRATION: Color Sets
        ModBlocks.COLOR_SETS.forEach((color, set) -> {
            registerBlockItem(set.candle());
            registerBlockItem(set.concrete());
            registerBlockItem(set.concretePowder());
            registerBlockItem(set.glazedTerracotta());
            ITEMS.register(set.shulkerBox().getId().getPath(), () -> new com.otterly76.ott.item.custom.ColorSetShulkerBoxItem(set.shulkerBox().get(), new Item.Properties().stacksTo(1)));
            registerBlockItem(set.stainedGlass());
            registerBlockItem(set.stainedGlassPane());
            registerBlockItem(set.terracotta());
            registerBlockItem(set.wool());
            registerBlockItem(set.carpet());
            ITEMS.register(set.banner().getId().getPath(), () -> new com.otterly76.ott.item.custom.ColorSetBannerItem(set.banner().get(), set.wallBanner().get(), new Item.Properties().stacksTo(16)));
            ITEMS.register(set.bed().getId().getPath(), () -> new ColorSetBedItem(set.bed().get(), new Item.Properties()));
        });

        // REGISTRATION: Pattern Blocks
        ModBlocks.PATTERN_BLOCKS.forEach((pattern, colorMap) -> {
            colorMap.values().forEach(ModItems::registerBlockItem);
        });

        // REGISTRATION: Elevators
        ModBlocks.ELEVATORS.values().forEach(ModItems::registerBlockItem);

        // REGISTRATION: Custom Dyes
        ModColorSets.ALL.forEach(colorSet -> {
            CUSTOM_DYES.put(colorSet.name(), ITEMS.register(colorSet.name() + "_dye", () -> new Item(new Item.Properties())));
        });

        // REGISTRATION: Static Minecraft Backports
        RESIN_BRICK = MINECRAFT_ITEMS.register("resin_brick", () -> new Item(new Item.Properties()));
        MUSIC_DISC_TEARS = MINECRAFT_ITEMS.register("music_disc_tears", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(ModJukeboxSongs.TEARS.getKey())));
        MUSIC_DISC_LAVA_CHICKEN = MINECRAFT_ITEMS.register("music_disc_lava_chicken", () -> new Item(new Item.Properties().stacksTo(1).rarity(Rarity.RARE).jukeboxPlayable(ModJukeboxSongs.LAVA_CHICKEN.getKey())));
        CREAKING_SPAWN_EGG = MINECRAFT_ITEMS.register("creaking_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.CREAKING, 6250335, 16545810, new Item.Properties()));
        HAPPY_GHAST_SPAWN_EGG = MINECRAFT_ITEMS.register("happy_ghast_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.HAPPY_GHAST, 16382457, 12369084, new Item.Properties()));

        BLUE_EGG = MINECRAFT_ITEMS.register("blue_egg", () -> new EggItem(new Item.Properties().stacksTo(16).component(ModDataComponents.CHICKEN_VARIANT.get(), ChickenVariants.COLD)));
        BROWN_EGG = MINECRAFT_ITEMS.register("brown_egg", () -> new EggItem(new Item.Properties().stacksTo(16).component(ModDataComponents.CHICKEN_VARIANT.get(), ChickenVariants.WARM)));

        COPPER_NUGGET = MINECRAFT_ITEMS.register("copper_nugget", () -> new Item(new Item.Properties()));
        COPPER_SWORD = MINECRAFT_ITEMS.register("copper_sword", () -> new SwordItem(ModToolMaterials.COPPER, (new Item.Properties()).attributes(SwordItem.createAttributes(ModToolMaterials.COPPER, 3, -2.4F))));
        COPPER_SHOVEL = MINECRAFT_ITEMS.register("copper_shovel", () -> new ShovelItem(ModToolMaterials.COPPER, (new Item.Properties()).attributes(ShovelItem.createAttributes(ModToolMaterials.COPPER, 1.5F, -3.0F))));
        COPPER_PICKAXE = MINECRAFT_ITEMS.register("copper_pickaxe", () -> new PickaxeItem(ModToolMaterials.COPPER, (new Item.Properties()).attributes(PickaxeItem.createAttributes(ModToolMaterials.COPPER, 1.0F, -2.8F))));
        COPPER_AXE = MINECRAFT_ITEMS.register("copper_axe", () -> new AxeItem(ModToolMaterials.COPPER, (new Item.Properties()).attributes(AxeItem.createAttributes(ModToolMaterials.COPPER, 6.0F, -3.1F))));
        COPPER_HOE = MINECRAFT_ITEMS.register("copper_hoe", () -> new HoeItem(ModToolMaterials.COPPER, (new Item.Properties()).attributes(HoeItem.createAttributes(ModToolMaterials.COPPER, -2.0F, -1.0F))));
        COPPER_SHEARS = MINECRAFT_ITEMS.register("copper_shears", () -> new ShearsItem((new Item.Properties()).durability(190)));

        EXPOSED_COPPER_SWORD = MINECRAFT_ITEMS.register("exposed_copper_sword", () -> new SwordItem(ModToolMaterials.EXPOSED_COPPER, (new Item.Properties()).attributes(SwordItem.createAttributes(ModToolMaterials.EXPOSED_COPPER, 3, -2.4F))));
        EXPOSED_COPPER_SHOVEL = MINECRAFT_ITEMS.register("exposed_copper_shovel", () -> new ShovelItem(ModToolMaterials.EXPOSED_COPPER, (new Item.Properties()).attributes(ShovelItem.createAttributes(ModToolMaterials.EXPOSED_COPPER, 1.5F, -3.0F))));
        EXPOSED_COPPER_PICKAXE = MINECRAFT_ITEMS.register("exposed_copper_pickaxe", () -> new PickaxeItem(ModToolMaterials.EXPOSED_COPPER, (new Item.Properties()).attributes(PickaxeItem.createAttributes(ModToolMaterials.EXPOSED_COPPER, 1.0F, -2.8F))));
        EXPOSED_COPPER_AXE = MINECRAFT_ITEMS.register("exposed_copper_axe", () -> new AxeItem(ModToolMaterials.EXPOSED_COPPER, (new Item.Properties()).attributes(AxeItem.createAttributes(ModToolMaterials.EXPOSED_COPPER, 6.0F, -3.1F))));
        EXPOSED_COPPER_HOE = MINECRAFT_ITEMS.register("exposed_copper_hoe", () -> new HoeItem(ModToolMaterials.EXPOSED_COPPER, (new Item.Properties()).attributes(HoeItem.createAttributes(ModToolMaterials.EXPOSED_COPPER, -2.0F, -1.0F))));
        EXPOSED_COPPER_SHEARS = MINECRAFT_ITEMS.register("exposed_copper_shears", () -> new ShearsItem((new Item.Properties()).durability(190)));

        WEATHERED_COPPER_SWORD = MINECRAFT_ITEMS.register("weathered_copper_sword", () -> new SwordItem(ModToolMaterials.WEATHERED_COPPER, (new Item.Properties()).attributes(SwordItem.createAttributes(ModToolMaterials.WEATHERED_COPPER, 3, -2.4F))));
        WEATHERED_COPPER_SHOVEL = MINECRAFT_ITEMS.register("weathered_copper_shovel", () -> new ShovelItem(ModToolMaterials.WEATHERED_COPPER, (new Item.Properties()).attributes(ShovelItem.createAttributes(ModToolMaterials.WEATHERED_COPPER, 1.5F, -3.0F))));
        WEATHERED_COPPER_PICKAXE = MINECRAFT_ITEMS.register("weathered_copper_pickaxe", () -> new PickaxeItem(ModToolMaterials.WEATHERED_COPPER, (new Item.Properties()).attributes(PickaxeItem.createAttributes(ModToolMaterials.WEATHERED_COPPER, 1.0F, -2.8F))));
        WEATHERED_COPPER_AXE = MINECRAFT_ITEMS.register("weathered_copper_axe", () -> new AxeItem(ModToolMaterials.WEATHERED_COPPER, (new Item.Properties()).attributes(AxeItem.createAttributes(ModToolMaterials.WEATHERED_COPPER, 6.0F, -3.1F))));
        WEATHERED_COPPER_HOE = MINECRAFT_ITEMS.register("weathered_copper_hoe", () -> new HoeItem(ModToolMaterials.WEATHERED_COPPER, (new Item.Properties()).attributes(HoeItem.createAttributes(ModToolMaterials.WEATHERED_COPPER, -2.0F, -1.0F))));
        WEATHERED_COPPER_SHEARS = MINECRAFT_ITEMS.register("weathered_copper_shears", () -> new ShearsItem((new Item.Properties()).durability(190)));

        OXIDIZED_COPPER_SWORD = MINECRAFT_ITEMS.register("oxidized_copper_sword", () -> new SwordItem(ModToolMaterials.OXIDIZED_COPPER, (new Item.Properties()).attributes(SwordItem.createAttributes(ModToolMaterials.OXIDIZED_COPPER, 3, -2.4F))));
        OXIDIZED_COPPER_SHOVEL = MINECRAFT_ITEMS.register("oxidized_copper_shovel", () -> new ShovelItem(ModToolMaterials.OXIDIZED_COPPER, (new Item.Properties()).attributes(ShovelItem.createAttributes(ModToolMaterials.OXIDIZED_COPPER, 1.5F, -3.0F))));
        OXIDIZED_COPPER_PICKAXE = MINECRAFT_ITEMS.register("oxidized_copper_pickaxe", () -> new PickaxeItem(ModToolMaterials.OXIDIZED_COPPER, (new Item.Properties()).attributes(PickaxeItem.createAttributes(ModToolMaterials.OXIDIZED_COPPER, 1.0F, -2.8F))));
        OXIDIZED_COPPER_AXE = MINECRAFT_ITEMS.register("oxidized_copper_axe", () -> new AxeItem(ModToolMaterials.OXIDIZED_COPPER, (new Item.Properties()).attributes(AxeItem.createAttributes(ModToolMaterials.OXIDIZED_COPPER, 6.0F, -3.1F))));
        OXIDIZED_COPPER_HOE = MINECRAFT_ITEMS.register("oxidized_copper_hoe", () -> new HoeItem(ModToolMaterials.OXIDIZED_COPPER, (new Item.Properties()).attributes(HoeItem.createAttributes(ModToolMaterials.OXIDIZED_COPPER, -2.0F, -1.0F))));
        OXIDIZED_COPPER_SHEARS = MINECRAFT_ITEMS.register("oxidized_copper_shears", () -> new ShearsItem((new Item.Properties()).durability(190)));

        COPPER_BUCKET = MINECRAFT_ITEMS.register("copper_bucket", () -> new CopperBucketItem(Fluids.EMPTY, (new Item.Properties()).stacksTo(16)));
        COPPER_WATER_BUCKET = MINECRAFT_ITEMS.register("copper_water_bucket", () -> new CopperBucketItem(Fluids.WATER, (new Item.Properties()).craftRemainder(COPPER_BUCKET.get()).stacksTo(1)));
        COPPER_LAVA_BUCKET = MINECRAFT_ITEMS.register("copper_lava_bucket", () -> new CopperBucketItem(Fluids.LAVA, (new Item.Properties()).craftRemainder(COPPER_BUCKET.get()).stacksTo(1)));
        COPPER_MILK_BUCKET = MINECRAFT_ITEMS.register("copper_milk_bucket", () -> new CopperMilkBucketItem((new Item.Properties()).craftRemainder(COPPER_BUCKET.get()).stacksTo(1)));
        COPPER_POWDER_SNOW_BUCKET = MINECRAFT_ITEMS.register("copper_powder_snow_bucket", () -> new CopperSolidBucketItem(Blocks.POWDER_SNOW, SoundEvents.BUCKET_EMPTY_POWDER_SNOW, (new Item.Properties()).craftRemainder(COPPER_BUCKET.get()).stacksTo(1)));

        COPPER_HELMET = MINECRAFT_ITEMS.register("copper_helmet", () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET, (new Item.Properties()).durability(ArmorItem.Type.HELMET.getDurability(5))));
        COPPER_CHESTPLATE = MINECRAFT_ITEMS.register("copper_chestplate", () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
        COPPER_LEGGINGS = MINECRAFT_ITEMS.register("copper_leggings", () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS, (new Item.Properties()).durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
        COPPER_BOOTS = MINECRAFT_ITEMS.register("copper_boots", () -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS, (new Item.Properties()).durability(ArmorItem.Type.BOOTS.getDurability(5))));

        EXPOSED_COPPER_HELMET = MINECRAFT_ITEMS.register("exposed_copper_helmet", () -> new ArmorItem(ModArmorMaterials.EXPOSED_COPPER, ArmorItem.Type.HELMET, (new Item.Properties()).durability(ArmorItem.Type.HELMET.getDurability(5))));
        EXPOSED_COPPER_CHESTPLATE = MINECRAFT_ITEMS.register("exposed_copper_chestplate", () -> new ArmorItem(ModArmorMaterials.EXPOSED_COPPER, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
        EXPOSED_COPPER_LEGGINGS = MINECRAFT_ITEMS.register("exposed_copper_leggings", () -> new ArmorItem(ModArmorMaterials.EXPOSED_COPPER, ArmorItem.Type.LEGGINGS, (new Item.Properties()).durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
        EXPOSED_COPPER_BOOTS = MINECRAFT_ITEMS.register("exposed_copper_boots", () -> new ArmorItem(ModArmorMaterials.EXPOSED_COPPER, ArmorItem.Type.BOOTS, (new Item.Properties()).durability(ArmorItem.Type.BOOTS.getDurability(5))));

        WEATHERED_COPPER_HELMET = MINECRAFT_ITEMS.register("weathered_copper_helmet", () -> new ArmorItem(ModArmorMaterials.WEATHERED_COPPER, ArmorItem.Type.HELMET, (new Item.Properties()).durability(ArmorItem.Type.HELMET.getDurability(5))));
        WEATHERED_COPPER_CHESTPLATE = MINECRAFT_ITEMS.register("weathered_copper_chestplate", () -> new ArmorItem(ModArmorMaterials.WEATHERED_COPPER, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
        WEATHERED_COPPER_LEGGINGS = MINECRAFT_ITEMS.register("weathered_copper_leggings", () -> new ArmorItem(ModArmorMaterials.WEATHERED_COPPER, ArmorItem.Type.LEGGINGS, (new Item.Properties()).durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
        WEATHERED_COPPER_BOOTS = MINECRAFT_ITEMS.register("weathered_copper_boots", () -> new ArmorItem(ModArmorMaterials.WEATHERED_COPPER, ArmorItem.Type.BOOTS, (new Item.Properties()).durability(ArmorItem.Type.BOOTS.getDurability(5))));

        OXIDIZED_COPPER_HELMET = MINECRAFT_ITEMS.register("oxidized_copper_helmet", () -> new ArmorItem(ModArmorMaterials.OXIDIZED_COPPER, ArmorItem.Type.HELMET, (new Item.Properties()).durability(ArmorItem.Type.HELMET.getDurability(5))));
        OXIDIZED_COPPER_CHESTPLATE = MINECRAFT_ITEMS.register("oxidized_copper_chestplate", () -> new ArmorItem(ModArmorMaterials.OXIDIZED_COPPER, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
        OXIDIZED_COPPER_LEGGINGS = MINECRAFT_ITEMS.register("oxidized_copper_leggings", () -> new ArmorItem(ModArmorMaterials.OXIDIZED_COPPER, ArmorItem.Type.LEGGINGS, (new Item.Properties()).durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
        OXIDIZED_COPPER_BOOTS = MINECRAFT_ITEMS.register("oxidized_copper_boots", () -> new ArmorItem(ModArmorMaterials.OXIDIZED_COPPER, ArmorItem.Type.BOOTS, (new Item.Properties()).durability(ArmorItem.Type.BOOTS.getDurability(5))));

        COPPER_CHAINMAIL_HELMET = MINECRAFT_ITEMS.register("copper_chainmail_helmet", () -> new ArmorItem(ModArmorMaterials.COPPER_CHAINMAIL, ArmorItem.Type.HELMET, (new Item.Properties()).durability(ArmorItem.Type.HELMET.getDurability(5))));
        COPPER_CHAINMAIL_CHESTPLATE = MINECRAFT_ITEMS.register("copper_chainmail_chestplate", () -> new ArmorItem(ModArmorMaterials.COPPER_CHAINMAIL, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
        COPPER_CHAINMAIL_LEGGINGS = MINECRAFT_ITEMS.register("copper_chainmail_leggings", () -> new ArmorItem(ModArmorMaterials.COPPER_CHAINMAIL, ArmorItem.Type.LEGGINGS, (new Item.Properties()).durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
        COPPER_CHAINMAIL_BOOTS = MINECRAFT_ITEMS.register("copper_chainmail_boots", () -> new ArmorItem(ModArmorMaterials.COPPER_CHAINMAIL, ArmorItem.Type.BOOTS, (new Item.Properties()).durability(ArmorItem.Type.BOOTS.getDurability(5))));

        EXPOSED_COPPER_CHAINMAIL_HELMET = MINECRAFT_ITEMS.register("exposed_copper_chainmail_helmet", () -> new ArmorItem(ModArmorMaterials.EXPOSED_COPPER_CHAINMAIL, ArmorItem.Type.HELMET, (new Item.Properties()).durability(ArmorItem.Type.HELMET.getDurability(5))));
        EXPOSED_COPPER_CHAINMAIL_CHESTPLATE = MINECRAFT_ITEMS.register("exposed_copper_chainmail_chestplate", () -> new ArmorItem(ModArmorMaterials.EXPOSED_COPPER_CHAINMAIL, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
        EXPOSED_COPPER_CHAINMAIL_LEGGINGS = MINECRAFT_ITEMS.register("exposed_copper_chainmail_leggings", () -> new ArmorItem(ModArmorMaterials.EXPOSED_COPPER_CHAINMAIL, ArmorItem.Type.LEGGINGS, (new Item.Properties()).durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
        EXPOSED_COPPER_CHAINMAIL_BOOTS = MINECRAFT_ITEMS.register("exposed_copper_chainmail_boots", () -> new ArmorItem(ModArmorMaterials.EXPOSED_COPPER_CHAINMAIL, ArmorItem.Type.BOOTS, (new Item.Properties()).durability(ArmorItem.Type.BOOTS.getDurability(5))));

        WEATHERED_COPPER_CHAINMAIL_HELMET = MINECRAFT_ITEMS.register("weathered_copper_chainmail_helmet", () -> new ArmorItem(ModArmorMaterials.WEATHERED_COPPER_CHAINMAIL, ArmorItem.Type.HELMET, (new Item.Properties()).durability(ArmorItem.Type.HELMET.getDurability(5))));
        WEATHERED_COPPER_CHAINMAIL_CHESTPLATE = MINECRAFT_ITEMS.register("weathered_copper_chainmail_chestplate", () -> new ArmorItem(ModArmorMaterials.WEATHERED_COPPER_CHAINMAIL, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
        WEATHERED_COPPER_CHAINMAIL_LEGGINGS = MINECRAFT_ITEMS.register("weathered_copper_chainmail_leggings", () -> new ArmorItem(ModArmorMaterials.WEATHERED_COPPER_CHAINMAIL, ArmorItem.Type.LEGGINGS, (new Item.Properties()).durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
        WEATHERED_COPPER_CHAINMAIL_BOOTS = MINECRAFT_ITEMS.register("weathered_copper_chainmail_boots", () -> new ArmorItem(ModArmorMaterials.WEATHERED_COPPER_CHAINMAIL, ArmorItem.Type.BOOTS, (new Item.Properties()).durability(ArmorItem.Type.BOOTS.getDurability(5))));

        OXIDIZED_COPPER_CHAINMAIL_HELMET = MINECRAFT_ITEMS.register("oxidized_copper_chainmail_helmet", () -> new ArmorItem(ModArmorMaterials.OXIDIZED_COPPER_CHAINMAIL, ArmorItem.Type.HELMET, (new Item.Properties()).durability(ArmorItem.Type.HELMET.getDurability(5))));
        OXIDIZED_COPPER_CHAINMAIL_CHESTPLATE = MINECRAFT_ITEMS.register("oxidized_copper_chainmail_chestplate", () -> new ArmorItem(ModArmorMaterials.OXIDIZED_COPPER_CHAINMAIL, ArmorItem.Type.CHESTPLATE, (new Item.Properties()).durability(ArmorItem.Type.CHESTPLATE.getDurability(5))));
        OXIDIZED_COPPER_CHAINMAIL_LEGGINGS = MINECRAFT_ITEMS.register("oxidized_copper_chainmail_leggings", () -> new ArmorItem(ModArmorMaterials.OXIDIZED_COPPER_CHAINMAIL, ArmorItem.Type.LEGGINGS, (new Item.Properties()).durability(ArmorItem.Type.LEGGINGS.getDurability(5))));
        OXIDIZED_COPPER_CHAINMAIL_BOOTS = MINECRAFT_ITEMS.register("oxidized_copper_chainmail_boots", () -> new ArmorItem(ModArmorMaterials.OXIDIZED_COPPER_CHAINMAIL, ArmorItem.Type.BOOTS, (new Item.Properties()).durability(ArmorItem.Type.BOOTS.getDurability(5))));

        COPPER_HORSE_ARMOR = MINECRAFT_ITEMS.register("copper_horse_armor", () -> new AnimalArmorItem(ModArmorMaterials.COPPER, AnimalArmorItem.BodyType.EQUESTRIAN, false, (new Item.Properties()).stacksTo(1)));
        EXPOSED_COPPER_HORSE_ARMOR = MINECRAFT_ITEMS.register("exposed_copper_horse_armor", () -> new AnimalArmorItem(ModArmorMaterials.EXPOSED_COPPER, AnimalArmorItem.BodyType.EQUESTRIAN, false, (new Item.Properties()).stacksTo(1)));
        WEATHERED_COPPER_HORSE_ARMOR = MINECRAFT_ITEMS.register("weathered_copper_horse_armor", () -> new AnimalArmorItem(ModArmorMaterials.WEATHERED_COPPER, AnimalArmorItem.BodyType.EQUESTRIAN, false, (new Item.Properties()).stacksTo(1)));
        OXIDIZED_COPPER_HORSE_ARMOR = MINECRAFT_ITEMS.register("oxidized_copper_horse_armor", () -> new AnimalArmorItem(ModArmorMaterials.OXIDIZED_COPPER, AnimalArmorItem.BodyType.EQUESTRIAN, false, (new Item.Properties()).stacksTo(1)));
        COPPER_GOLEM_SPAWN_EGG = MINECRAFT_ITEMS.register("copper_golem_spawn_egg", () -> new net.neoforged.neoforge.common.DeferredSpawnEggItem(ModEntities.COPPER_GOLEM, 12215115, 6106649, new Item.Properties()));

        for (net.minecraft.world.item.DyeColor color : net.minecraft.world.item.DyeColor.values()) {
            HARNESSES.put(color.getName(), MINECRAFT_ITEMS.register(color.getName() + "_harness", () -> new HarnessItem(new Item.Properties().stacksTo(1))));
            BUNDLES.put(color.getName(), MINECRAFT_ITEMS.register(color.getName() + "_bundle", () -> new BundleItem(new Item.Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY))));
        }

        NETHERITE_HORSE_ARMOR = MINECRAFT_ITEMS.register("netherite_horse_armor", () -> new AnimalArmorItem(ArmorMaterials.NETHERITE, AnimalArmorItem.BodyType.EQUESTRIAN, false, (new Item.Properties()).stacksTo(1).fireResistant()));

        PALE_OAK_SIGN = registerMinecraftSign();
        PALE_OAK_HANGING_SIGN = registerMinecraftHangingSign();
        PALE_OAK_BOAT = MINECRAFT_ITEMS.register("pale_oak_boat", () -> new PaleOakBoatItem(false, new Item.Properties().stacksTo(1)));
        PALE_OAK_CHEST_BOAT = MINECRAFT_ITEMS.register("pale_oak_chest_boat", () -> new PaleOakBoatItem(true, new Item.Properties().stacksTo(1)));
        PALE_OAK_SAPLING = registerMinecraftBlockItem();

    }

    private static DeferredHolder<Item, BlockItem> registerBlockItem(String name, DeferredBlock<? extends Block> block) {
        return ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static void registerBlockItem(DeferredBlock<? extends Block> block) {
        ITEMS.register(block.getId().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
    }

    private static DeferredItem<Item> registerMinecraftBlockItem() {
        return MINECRAFT_ITEMS.register("pale_oak_sapling", () -> new BlockItem(ModBlocks.PALE_OAK_SAPLING.get(), new Item.Properties()));
    }

    private static DeferredItem<SignItem> registerSign(String name, DeferredBlock<? extends Block> sign, DeferredBlock<? extends Block> wallSign) {
        return ITEMS.register(name, () -> new SignItem(new Item.Properties().stacksTo(16), sign.get(), wallSign.get()));
    }

    private static DeferredItem<SignItem> registerMinecraftSign() {
        return MINECRAFT_ITEMS.register("pale_oak_sign", () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.PALE_OAK_SIGN.get(), ModBlocks.PALE_OAK_WALL_SIGN.get()));
    }

    private static DeferredItem<HangingSignItem> registerHangingSign(String name, DeferredBlock<? extends Block> sign, DeferredBlock<? extends Block> wallSign) {
        return ITEMS.register(name, () -> new HangingSignItem(sign.get(), wallSign.get(), new Item.Properties().stacksTo(16)));
    }

    private static DeferredItem<HangingSignItem> registerMinecraftHangingSign() {
        return MINECRAFT_ITEMS.register("pale_oak_hanging_sign", () -> new HangingSignItem(ModBlocks.PALE_OAK_HANGING_SIGN.get(), ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    }


}