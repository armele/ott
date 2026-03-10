package com.otterly76.ott.item;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.variant.ChickenVariants;
import com.otterly76.ott.registry.ModArmorMaterials;
import com.otterly76.ott.registry.ModDataComponents;
import com.otterly76.ott.registry.ModJukeboxSongs;
import com.otterly76.ott.entity.vehicle.OttWoodSetBoatEntity;
import com.otterly76.ott.entity.vehicle.OttWoodSetChestBoatEntity;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.level.block.Block;
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

    public static final Map<String, DeferredItem<Item>> HARNESSES = new HashMap<>();
    public static final Map<String, DeferredItem<Item>> BUNDLES = new HashMap<>();

    // Standard ItemTags
    public static final DeferredItem<Item> OTTER = ITEMS.register("otter", () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DRAGON_SKULL = ITEMS.register("dragon_skull", () -> new net.minecraft.world.item.StandingAndWallBlockItem(ModBlocks.DRAGON_SKULL.get(), ModBlocks.DRAGON_WALL_SKULL.get(), new net.minecraft.world.item.Item.Properties(), net.minecraft.core.Direction.DOWN));
    public static final DeferredHolder<Item, BlockItem> THORNY_HEDGE = registerBlockItem("thorny_hedge", ModBlocks.THORNY_HEDGE);
    public static final DeferredItem<Item> THORNY_HEDGE_SPROUTS = ITEMS.register("thorny_hedge_sprouts", () -> new ItemNameBlockItem(ModBlocks.THORNY_HEDGE_SPROUTS.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> PROTECTIVE_LANTERN = registerBlockItem("protective_lantern", ModBlocks.PROTECTIVE_LANTERN);

    public static final DeferredHolder<Item, BlockItem> WATER_LANTERN = registerBlockItem("water_lantern", ModBlocks.WATER_LANTERN);
    public static final DeferredHolder<Item, BlockItem> LAVA_LANTERN = registerBlockItem("lava_lantern", ModBlocks.LAVA_LANTERN);
    public static final DeferredHolder<Item, BlockItem> SMITE_LANTERN = registerBlockItem("smite_lantern", ModBlocks.SMITE_LANTERN);

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

    public static DeferredItem<AnimalArmorItem> NETHERITE_HORSE_ARMOR;
    public static DeferredItem<SignItem> PALE_OAK_SIGN;
    public static DeferredItem<HangingSignItem> PALE_OAK_HANGING_SIGN;
    public static DeferredItem<PaleOakBoatItem> PALE_OAK_BOAT;
    public static DeferredItem<PaleOakBoatItem> PALE_OAK_CHEST_BOAT;

    public static void register(IEventBus eventBus) {
        // 1. Run dynamic logic to set up the registration entries
        initializeDynamicItems();

        // 2. Attach the registers to the mod event bus
        ITEMS.register(eventBus);
    }

    private static void initializeDynamicItems() {
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

            WOOD_SET_SIGNS.put(setName, registerSign(setName + "_sign", setBlocks.sign(), setBlocks.wallSign()));

            WOOD_SET_HANGING_SIGNS.put(setName, registerHangingSign(setName + "_hanging_sign", setBlocks.hangingSign(), setBlocks.wallHangingSign()));

            WOOD_SET_BOATS.put(setName, ITEMS.register(setName + "_boat",
                    () -> new ModBoatItem(ModEntities.WOOD_SET_BOATS.get(setName), new Item.Properties().stacksTo(1),
                            boat -> { if (boat instanceof OttWoodSetBoatEntity b) b.setWoodSetName(setName); })));

            WOOD_SET_CHEST_BOATS.put(setName, ITEMS.register(setName + "_chest_boat",
                    () -> new ModBoatItem(ModEntities.WOOD_SET_CHEST_BOATS.get(setName), new Item.Properties().stacksTo(1),
                            boat -> { if (boat instanceof OttWoodSetChestBoatEntity b) b.setWoodSetName(setName); })));
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