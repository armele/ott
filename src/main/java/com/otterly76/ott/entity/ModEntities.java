package com.otterly76.ott.entity;

import com.otterly76.ott.entity.vehicle.PaleOakBoat;
import com.otterly76.ott.entity.vehicle.PaleOakChestBoat;
import com.otterly76.ott.entity.vehicle.OttWoodSetBoatEntity;
import com.otterly76.ott.entity.vehicle.OttWoodSetChestBoatEntity;
import com.otterly76.ott.wood.ModWoodSets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.Builder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.entity.vehicle.ChestBoat;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "minecraft");

    public static final DeferredRegister<EntityType<?>> OTT_ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, "ott");

    public static final Supplier<EntityType<Creaking>> CREAKING = ENTITY_TYPES.register("creaking",
            () -> Builder.of(Creaking::new, MobCategory.MONSTER).sized(0.9F, 2.7F).eyeHeight(2.3F).clientTrackingRange(8).build("creaking"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.HappyGhast>> HAPPY_GHAST = ENTITY_TYPES.register("happy_ghast",
            () -> Builder.of(com.otterly76.ott.entity.custom.HappyGhast::new, MobCategory.CREATURE).sized(4.0F, 4.0F).eyeHeight(2.6F).passengerAttachments(new Vec3[]{new Vec3(0.0F, 4.0F, 1.8), new Vec3(-1.8, 4.0F, 0.0F), new Vec3(0.0F, 4.0F, -1.8), new Vec3(1.8, 4.0F, 0.0F)}).ridingOffset(0.5F).clientTrackingRange(10).build("happy_ghast"));
    
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CopperGolem>> COPPER_GOLEM = ENTITY_TYPES.register("copper_golem",
            () -> Builder.of(com.otterly76.ott.entity.custom.CopperGolem::new, MobCategory.MISC).sized(0.6F, 1.2F).build("copper_golem"));

    public static final Supplier<EntityType<TorchArrowEntity>> TORCH_ARROW = OTT_ENTITY_TYPES.register("torch_arrow",
            () -> Builder.<TorchArrowEntity>of(TorchArrowEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build("torch_arrow"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.ManOWar>> MAN_O_WAR = OTT_ENTITY_TYPES.register("man_o_war",
            () -> Builder.of(com.otterly76.ott.entity.custom.ManOWar::new, MobCategory.WATER_CREATURE).sized(0.7F, 0.7F).clientTrackingRange(8).build("man_o_war"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Duck>> DUCK = OTT_ENTITY_TYPES.register("duck",
            () -> Builder.of(com.otterly76.ott.entity.custom.Duck::new, MobCategory.CREATURE).sized(0.4F, 0.7F).clientTrackingRange(8).build("duck"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.ThrownDuckEgg>> THROWN_DUCK_EGG = OTT_ENTITY_TYPES.register("thrown_duck_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.ThrownDuckEgg>of(com.otterly76.ott.entity.projectile.ThrownDuckEgg::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("thrown_duck_egg"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Goose>> GOOSE = OTT_ENTITY_TYPES.register("goose",
            () -> Builder.of(com.otterly76.ott.entity.custom.Goose::new, MobCategory.CREATURE).sized(0.4F, 0.7F).clientTrackingRange(8).build("goose"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Stingray>> STINGRAY = OTT_ENTITY_TYPES.register("stingray",
            () -> Builder.of(com.otterly76.ott.entity.custom.Stingray::new, MobCategory.WATER_CREATURE).sized(1.2F, 0.3F).clientTrackingRange(8).build("stingray"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Sunfish>> SUNFISH = OTT_ENTITY_TYPES.register("sunfish",
            () -> Builder.of(com.otterly76.ott.entity.custom.Sunfish::new, MobCategory.WATER_CREATURE).sized(0.7F, 1.2F).clientTrackingRange(8).build("sunfish"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Krill>> KRILL = OTT_ENTITY_TYPES.register("krill",
            () -> Builder.of(com.otterly76.ott.entity.custom.Krill::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.4F).clientTrackingRange(8).build("krill"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Angelfish>> ANGELFISH = OTT_ENTITY_TYPES.register("angelfish",
            () -> Builder.of(com.otterly76.ott.entity.custom.Angelfish::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.5F).clientTrackingRange(8).build("angelfish"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Barreleye>> BARRELEYE = OTT_ENTITY_TYPES.register("barreleye",
            () -> Builder.of(com.otterly76.ott.entity.custom.Barreleye::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.5F).clientTrackingRange(8).build("barreleye"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Flounder>> FLOUNDER = OTT_ENTITY_TYPES.register("flounder",
            () -> Builder.of(com.otterly76.ott.entity.custom.Flounder::new, MobCategory.WATER_AMBIENT).sized(0.6F, 0.3F).clientTrackingRange(8).build("flounder"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.MarineIguana>> MARINE_IGUANA = OTT_ENTITY_TYPES.register("marine_iguana",
            () -> Builder.of(com.otterly76.ott.entity.custom.MarineIguana::new, MobCategory.CREATURE).sized(0.8F, 0.4F).eyeHeight(0.3F).clientTrackingRange(8).build("marine_iguana"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Gecko>> GECKO = OTT_ENTITY_TYPES.register("gecko",
            () -> Builder.of(com.otterly76.ott.entity.custom.Gecko::new, MobCategory.CREATURE).sized(0.5F, 0.3F).eyeHeight(0.2F).clientTrackingRange(8).build("gecko"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Emu>> EMU = OTT_ENTITY_TYPES.register("emu",
            () -> Builder.of(com.otterly76.ott.entity.custom.Emu::new, MobCategory.CREATURE).sized(0.9F, 1.9F).eyeHeight(1.8F).clientTrackingRange(8).build("emu"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Hoopoe>> HOOPOE = OTT_ENTITY_TYPES.register("hoopoe",
            () -> Builder.of(com.otterly76.ott.entity.custom.Hoopoe::new, MobCategory.CREATURE).sized(0.5F, 0.5F).eyeHeight(0.3F).clientTrackingRange(8).build("hoopoe"));
    
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Pheasant>> PHEASANT = OTT_ENTITY_TYPES.register("pheasant",
            () -> Builder.of(com.otterly76.ott.entity.custom.Pheasant::new, MobCategory.CREATURE).sized(0.6F, 0.8F).eyeHeight(0.6F).clientTrackingRange(8).build("pheasant"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Toucan>> TOUCAN = OTT_ENTITY_TYPES.register("toucan",
            () -> Builder.of(com.otterly76.ott.entity.custom.Toucan::new, MobCategory.CREATURE).sized(0.5F, 0.5F).eyeHeight(0.3F).clientTrackingRange(8).build("toucan"));


    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BrownBearEntity>> BROWN_BEAR = OTT_ENTITY_TYPES.register("brown_bear",
            () -> Builder.of(com.otterly76.ott.entity.custom.BrownBearEntity::new, MobCategory.CREATURE).sized(1.4F, 1.7F).clientTrackingRange(10).build("brown_bear"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BlackBearEntity>> BLACK_BEAR = OTT_ENTITY_TYPES.register("black_bear",
            () -> Builder.of(com.otterly76.ott.entity.custom.BlackBearEntity::new, MobCategory.CREATURE).sized(1.4F, 1.7F).clientTrackingRange(10).build("black_bear"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.DeerEntity>> DEER = OTT_ENTITY_TYPES.register("deer",
            () -> Builder.of(com.otterly76.ott.entity.custom.DeerEntity::new, MobCategory.CREATURE).sized(1.3F, 1.6F).clientTrackingRange(10).build("deer"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.ReindeerEntity>> REINDEER = OTT_ENTITY_TYPES.register("reindeer",
            () -> Builder.of(com.otterly76.ott.entity.custom.ReindeerEntity::new, MobCategory.CREATURE).sized(1.3F, 1.6F).clientTrackingRange(10).build("reindeer"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.WhiteDeerEntity>> WHITE_DEER = OTT_ENTITY_TYPES.register("white_deer",
            () -> Builder.of(com.otterly76.ott.entity.custom.WhiteDeerEntity::new, MobCategory.CREATURE).sized(1.3F, 1.6F).clientTrackingRange(10).build("white_deer"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Bird>> BLUEJAY = OTT_ENTITY_TYPES.register("bluejay",
            () -> Builder.of(com.otterly76.ott.entity.custom.Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).eyeHeight(0.36F).clientTrackingRange(8).build("bluejay"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Bird>> CANARY = OTT_ENTITY_TYPES.register("canary",
            () -> Builder.of(com.otterly76.ott.entity.custom.Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).eyeHeight(0.36F).clientTrackingRange(8).build("canary"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Bird>> CARDINAL = OTT_ENTITY_TYPES.register("cardinal",
            () -> Builder.of(com.otterly76.ott.entity.custom.Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).eyeHeight(0.36F).clientTrackingRange(8).build("cardinal"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Bird>> FINCH = OTT_ENTITY_TYPES.register("finch",
            () -> Builder.of(com.otterly76.ott.entity.custom.Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).eyeHeight(0.36F).clientTrackingRange(8).build("finch"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Bird>> ROBIN = OTT_ENTITY_TYPES.register("robin",
            () -> Builder.of(com.otterly76.ott.entity.custom.Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).eyeHeight(0.36F).clientTrackingRange(8).build("robin"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Bird>> SPARROW = OTT_ENTITY_TYPES.register("sparrow",
            () -> Builder.of(com.otterly76.ott.entity.custom.Bird::new, MobCategory.CREATURE).sized(0.5F, 0.6F).eyeHeight(0.36F).clientTrackingRange(8).build("sparrow"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Catfish>> CATFISH = OTT_ENTITY_TYPES.register("catfish",
            () -> Builder.of(com.otterly76.ott.entity.custom.Catfish::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(8).build("catfish"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Bass>> BASS = OTT_ENTITY_TYPES.register("bass",
            () -> Builder.of(com.otterly76.ott.entity.custom.Bass::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.4F).clientTrackingRange(8).build("bass"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Butterfly>> BUTTERFLY = OTT_ENTITY_TYPES.register("butterfly",
            () -> Builder.of(com.otterly76.ott.entity.custom.Butterfly::new, MobCategory.AMBIENT).sized(0.5F, 0.5F).clientTrackingRange(8).build("butterfly"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Caterpillar>> CATERPILLAR = OTT_ENTITY_TYPES.register("caterpillar",
            () -> Builder.of(com.otterly76.ott.entity.custom.Caterpillar::new, MobCategory.AMBIENT).sized(0.4F, 0.3F).clientTrackingRange(8).build("caterpillar"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Firefly>> FIREFLY = OTT_ENTITY_TYPES.register("firefly",
            () -> Builder.of(com.otterly76.ott.entity.custom.Firefly::new, MobCategory.AMBIENT).sized(0.25F, 0.25F).clientTrackingRange(8).build("firefly"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Alligator>> ALLIGATOR = OTT_ENTITY_TYPES.register("alligator",
            () -> Builder.of(com.otterly76.ott.entity.custom.Alligator::new, MobCategory.CREATURE).sized(1.8F, 0.8F).clientTrackingRange(10).build("alligator"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Elephant>> ELEPHANT = OTT_ENTITY_TYPES.register("elephant",
            () -> Builder.of(com.otterly76.ott.entity.custom.Elephant::new, MobCategory.CREATURE).sized(2.5F, 3.5F).clientTrackingRange(10).build("elephant"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Giraffe>> GIRAFFE = OTT_ENTITY_TYPES.register("giraffe",
            () -> Builder.of(com.otterly76.ott.entity.custom.Giraffe::new, MobCategory.CREATURE).sized(1.9F, 5.4F).clientTrackingRange(10).build("giraffe"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Hippo>> HIPPO = OTT_ENTITY_TYPES.register("hippo",
            () -> Builder.of(com.otterly76.ott.entity.custom.Hippo::new, MobCategory.CREATURE).sized(1.6F, 1.8F).clientTrackingRange(10).build("hippo"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Lion>> LION = OTT_ENTITY_TYPES.register("lion",
            () -> Builder.of(com.otterly76.ott.entity.custom.Lion::new, MobCategory.CREATURE).sized(1.4F, 1.4F).clientTrackingRange(10).build("lion"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Rhino>> RHINO = OTT_ENTITY_TYPES.register("rhino",
            () -> Builder.of(com.otterly76.ott.entity.custom.Rhino::new, MobCategory.CREATURE).sized(1.6F, 1.8F).clientTrackingRange(10).build("rhino"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Lizard>> LIZARD = OTT_ENTITY_TYPES.register("lizard",
            () -> Builder.of(com.otterly76.ott.entity.custom.Lizard::new, MobCategory.CREATURE).sized(0.8F, 0.5F).clientTrackingRange(10).build("lizard"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.LizardTail>> LIZARD_TAIL = OTT_ENTITY_TYPES.register("lizard_tail",
            () -> Builder.of(com.otterly76.ott.entity.custom.LizardTail::new, MobCategory.CREATURE).sized(0.7F, 0.5F).clientTrackingRange(10).build("lizard_tail"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Snail>> SNAIL = OTT_ENTITY_TYPES.register("snail",
            () -> Builder.of(com.otterly76.ott.entity.custom.Snail::new, MobCategory.CREATURE).sized(0.7F, 0.7F).eyeHeight(0.3F).clientTrackingRange(10).build("snail"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Tortoise>> TORTOISE = OTT_ENTITY_TYPES.register("tortoise",
            () -> Builder.of(com.otterly76.ott.entity.custom.Tortoise::new, MobCategory.CREATURE).sized(1.2F, 0.875F).clientTrackingRange(10).build("tortoise"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Vulture>> VULTURE = OTT_ENTITY_TYPES.register("vulture",
            () -> Builder.of(com.otterly76.ott.entity.custom.Vulture::new, MobCategory.CREATURE).sized(0.9F, 0.5F).clientTrackingRange(10).build("vulture"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Zebra>> ZEBRA = OTT_ENTITY_TYPES.register("zebra",
            () -> Builder.of(com.otterly76.ott.entity.custom.Zebra::new, MobCategory.CREATURE).sized(1.3964844F, 1.5F).clientTrackingRange(10).build("zebra"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Moose>> MOOSE = OTT_ENTITY_TYPES.register("moose",
            () -> Builder.of(com.otterly76.ott.entity.custom.Moose::new, MobCategory.CREATURE).sized(1.7F, 2.0F).clientTrackingRange(10).build("moose"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Mammoth>> MAMMOTH = OTT_ENTITY_TYPES.register("mammoth",
            () -> Builder.of(com.otterly76.ott.entity.custom.Mammoth::new, MobCategory.CREATURE).sized(2.5F, 3.5F).clientTrackingRange(10).build("mammoth"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.MyceliumMammoth>> MYCELIUM_MAMMOTH = OTT_ENTITY_TYPES.register("mycelium_mammoth",
            () -> Builder.of(com.otterly76.ott.entity.custom.MyceliumMammoth::new, MobCategory.CREATURE).sized(2.5F, 3.5F).clientTrackingRange(10).build("mycelium_mammoth"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.HedgehogEntity>> HEDGEHOG = OTT_ENTITY_TYPES.register("hedgehog",
            () -> Builder.of(com.otterly76.ott.entity.custom.HedgehogEntity::new, MobCategory.CREATURE).sized(0.6F, 0.5F).build("hedgehog"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.KiwiEntity>> KIWI = OTT_ENTITY_TYPES.register("kiwi",
            () -> Builder.of(com.otterly76.ott.entity.custom.KiwiEntity::new, MobCategory.CREATURE).sized(0.5F, 0.6F).build("kiwi"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CapybaraEntity>> CAPYBARA = OTT_ENTITY_TYPES.register("capybara",
            () -> Builder.of(com.otterly76.ott.entity.custom.CapybaraEntity::new, MobCategory.CREATURE).sized(1.1F, 1.0F).build("capybara"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SealEntity>> SEAL = OTT_ENTITY_TYPES.register("seal",
            () -> Builder.of(com.otterly76.ott.entity.custom.SealEntity::new, MobCategory.CREATURE).sized(1.1F, 0.9F).build("seal"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.PenguinEntity>> PENGUIN = OTT_ENTITY_TYPES.register("penguin",
            () -> Builder.of(com.otterly76.ott.entity.custom.PenguinEntity::new, MobCategory.CREATURE).sized(0.6F, 1.2F).build("penguin"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SeaUrchinEntity>> SEA_URCHIN = OTT_ENTITY_TYPES.register("sea_urchin",
            () -> Builder.of(com.otterly76.ott.entity.custom.SeaUrchinEntity::new, MobCategory.CREATURE).sized(0.5F, 0.65F).build("sea_urchin"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.JellyfishEntity>> JELLYFISH = OTT_ENTITY_TYPES.register("jellyfish",
            () -> Builder.of(com.otterly76.ott.entity.custom.JellyfishEntity::new, MobCategory.WATER_CREATURE).sized(1.0F, 1.0F).eyeHeight(0.5F).build("jellyfish"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Seahorse1Entity>> SEAHORSE_1 = OTT_ENTITY_TYPES.register("seahorse_1",
            () -> Builder.of(com.otterly76.ott.entity.custom.Seahorse1Entity::new, MobCategory.WATER_CREATURE).sized(0.4F, 1.5F).build("seahorse_1"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.ShrimpEntity>> SHRIMP = OTT_ENTITY_TYPES.register("shrimp",
            () -> Builder.of(com.otterly76.ott.entity.custom.ShrimpEntity::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.5F).build("shrimp"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Starfish1Entity>> STARFISH_1 = OTT_ENTITY_TYPES.register("starfish_1",
            () -> Builder.of(com.otterly76.ott.entity.custom.Starfish1Entity::new, MobCategory.WATER_CREATURE).sized(1.5F, 0.7F).build("starfish_1"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Jellyfish2Entity>> JELLYFISH_2 = OTT_ENTITY_TYPES.register("jellyfish_2",
            () -> Builder.of(com.otterly76.ott.entity.custom.Jellyfish2Entity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.8F).build("jellyfish_2"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Jellyfish3Entity>> JELLYFISH_3 = OTT_ENTITY_TYPES.register("jellyfish_3",
            () -> Builder.of(com.otterly76.ott.entity.custom.Jellyfish3Entity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.8F).build("jellyfish_3"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.FennecFox>> FENNEC_FOX = OTT_ENTITY_TYPES.register("fennec_fox",
            () -> Builder.of(com.otterly76.ott.entity.custom.FennecFox::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).build("fennec_fox"));


    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.EmuEggEntity>> EMU_EGG = OTT_ENTITY_TYPES.register("emu_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.EmuEggEntity>of(com.otterly76.ott.entity.projectile.EmuEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("emu_egg"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.KiwiEggEntity>> KIWI_EGG = OTT_ENTITY_TYPES.register("kiwi_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.KiwiEggEntity>of(com.otterly76.ott.entity.projectile.KiwiEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("kiwi_egg"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.PenguinEggEntity>> PENGUIN_EGG = OTT_ENTITY_TYPES.register("penguin_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.PenguinEggEntity>of(com.otterly76.ott.entity.projectile.PenguinEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("penguin_egg"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.QuillArrowEntity>> QUILL_ARROW = OTT_ENTITY_TYPES.register("quill_arrow",
            () -> Builder.<com.otterly76.ott.entity.projectile.QuillArrowEntity>of(com.otterly76.ott.entity.projectile.QuillArrowEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(20).build("quill_arrow"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.HoopoeEggEntity>> HOOPOE_EGG = OTT_ENTITY_TYPES.register("hoopoe_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.HoopoeEggEntity>of(com.otterly76.ott.entity.projectile.HoopoeEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("hoopoe_egg"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.PheasantEggEntity>> PHEASANT_EGG = OTT_ENTITY_TYPES.register("pheasant_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.PheasantEggEntity>of(com.otterly76.ott.entity.projectile.PheasantEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("pheasant_egg"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.ToucanEggEntity>> TOUCAN_EGG = OTT_ENTITY_TYPES.register("toucan_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.ToucanEggEntity>of(com.otterly76.ott.entity.projectile.ToucanEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("toucan_egg"));

    public static final Supplier<EntityType<PaleOakBoat>> PALE_OAK_BOAT = ENTITY_TYPES.register("pale_oak_boat",
            () -> Builder.<PaleOakBoat>of(PaleOakBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("pale_oak_boat"));

    public static final Supplier<EntityType<PaleOakChestBoat>> PALE_OAK_CHEST_BOAT = ENTITY_TYPES.register("pale_oak_chest_boat",
            () -> Builder.<PaleOakChestBoat>of(PaleOakChestBoat::new, MobCategory.MISC).sized(1.375F, 0.5625F).clientTrackingRange(10).build("pale_oak_chest_boat"));

    public static final Map<String, Supplier<EntityType<Boat>>> WOOD_SET_BOATS = new LinkedHashMap<>();
    public static final Map<String, Supplier<EntityType<ChestBoat>>> WOOD_SET_CHEST_BOATS = new LinkedHashMap<>();

    static {
        ModWoodSets.ALL.forEach(set -> {
            String name = set.name();

            WOOD_SET_BOATS.put(name, OTT_ENTITY_TYPES.register(name + "_boat",
                    () -> Builder.<Boat>of(OttWoodSetBoatEntity::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .build(name + "_boat")));

            WOOD_SET_CHEST_BOATS.put(name, OTT_ENTITY_TYPES.register(name + "_chest_boat",
                    () -> Builder.<ChestBoat>of(OttWoodSetChestBoatEntity::new, MobCategory.MISC)
                            .sized(1.375F, 0.5625F)
                            .clientTrackingRange(10)
                            .build(name + "_chest_boat")));
        });
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        OTT_ENTITY_TYPES.register(eventBus);
    }
}