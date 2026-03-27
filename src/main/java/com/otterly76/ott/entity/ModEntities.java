package com.otterly76.ott.entity;

import com.otterly76.ott.entity.custom.Shrimp1Entity;
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

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.StingrayEntity>> STINGRAY = OTT_ENTITY_TYPES.register("stingray",
            () -> Builder.of(com.otterly76.ott.entity.custom.StingrayEntity::new, MobCategory.WATER_CREATURE).sized(1.2F, 0.3F).clientTrackingRange(8).build("stingray"));

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

    public static final Supplier<EntityType<Shrimp1Entity>> SHRIMP_1 = OTT_ENTITY_TYPES.register("shrimp_1",
            () -> Builder.of(Shrimp1Entity::new, MobCategory.WATER_AMBIENT).sized(0.5F, 0.5F).build("shrimp_1"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Starfish1Entity>> STARFISH_1 = OTT_ENTITY_TYPES.register("starfish_1",
            () -> Builder.of(com.otterly76.ott.entity.custom.Starfish1Entity::new, MobCategory.WATER_CREATURE).sized(1.5F, 0.7F).build("starfish_1"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Jellyfish2Entity>> JELLYFISH_2 = OTT_ENTITY_TYPES.register("jellyfish_2",
            () -> Builder.of(com.otterly76.ott.entity.custom.Jellyfish2Entity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.8F).build("jellyfish_2"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Jellyfish3Entity>> JELLYFISH_3 = OTT_ENTITY_TYPES.register("jellyfish_3",
            () -> Builder.of(com.otterly76.ott.entity.custom.Jellyfish3Entity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.8F).build("jellyfish_3"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.DragonflyEntity>> DRAGONFLY = OTT_ENTITY_TYPES.register("dragonfly",
            () -> Builder.of(com.otterly76.ott.entity.custom.DragonflyEntity::new, MobCategory.AMBIENT).sized(0.9F, 0.4F).build("dragonfly"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.DumboOctopusEntity>> DUMBO_OCTOPUS = OTT_ENTITY_TYPES.register("dumbo_octopus",
            () -> Builder.of(com.otterly76.ott.entity.custom.DumboOctopusEntity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.4F).build("dumbo_octopus"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.FerretEntity>> FERRET = OTT_ENTITY_TYPES.register("ferret",
            () -> Builder.of(com.otterly76.ott.entity.custom.FerretEntity::new, MobCategory.CREATURE).sized(0.8F, 0.7F).build("ferret"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.JumpingSpiderEntity>> JUMPING_SPIDER = OTT_ENTITY_TYPES.register("jumping_spider",
            () -> Builder.of(com.otterly76.ott.entity.custom.JumpingSpiderEntity::new, MobCategory.CREATURE).sized(0.5F, 0.4F).build("jumping_spider"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.KoiFishEntity>> KOI_FISH = OTT_ENTITY_TYPES.register("koi_fish",
            () -> Builder.of(com.otterly76.ott.entity.custom.KoiFishEntity::new, MobCategory.WATER_AMBIENT).sized(0.6F, 0.3F).build("koi_fish"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.OtterEntity>> OTTER = OTT_ENTITY_TYPES.register("otter",
            () -> Builder.of(com.otterly76.ott.entity.custom.OtterEntity::new, MobCategory.CREATURE).sized(0.8F, 0.6F).build("otter"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.RedPandaEntity>> RED_PANDA = OTT_ENTITY_TYPES.register("red_panda",
            () -> Builder.of(com.otterly76.ott.entity.custom.RedPandaEntity::new, MobCategory.CREATURE).sized(0.75F, 0.65F).build("red_panda"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.WaterBuffaloEntity>> WATER_BUFFALO = OTT_ENTITY_TYPES.register("water_buffalo",
            () -> Builder.of(com.otterly76.ott.entity.custom.WaterBuffaloEntity::new, MobCategory.CREATURE).sized(0.9F, 1.4F).build("water_buffalo"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SeaBunnyEntity>> SEA_BUNNY = OTT_ENTITY_TYPES.register("sea_bunny",
            () -> Builder.of(com.otterly76.ott.entity.custom.SeaBunnyEntity::new, MobCategory.WATER_AMBIENT).sized(0.45F, 0.3F).build("sea_bunny"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.FennecFox>> FENNEC_FOX = OTT_ENTITY_TYPES.register("fennec_fox",
            () -> Builder.of(com.otterly76.ott.entity.custom.FennecFox::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).build("fennec_fox"));


    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.EmuEggEntity>> EMU_EGG = OTT_ENTITY_TYPES.register("emu_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.EmuEggEntity>of(com.otterly76.ott.entity.projectile.EmuEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("emu_egg"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.KiwiEggEntity>> KIWI_EGG = OTT_ENTITY_TYPES.register("kiwi_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.KiwiEggEntity>of(com.otterly76.ott.entity.projectile.KiwiEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("kiwi_egg"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.PenguinEggEntity>> PENGUIN_EGG = OTT_ENTITY_TYPES.register("penguin_egg",
            () -> Builder.<com.otterly76.ott.entity.projectile.PenguinEggEntity>of(com.otterly76.ott.entity.projectile.PenguinEggEntity::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("penguin_egg"));


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

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SmallFirefly>> SMALL_FIREFLY = OTT_ENTITY_TYPES.register("small_firefly",
            () -> Builder.of(com.otterly76.ott.entity.custom.SmallFirefly::new, MobCategory.AMBIENT).sized(0.1F, 0.1F).clientTrackingRange(8).build("small_firefly"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BeaverEntity>> BEAVER = OTT_ENTITY_TYPES.register("beaver",
            () -> Builder.of(com.otterly76.ott.entity.custom.BeaverEntity::new, MobCategory.CREATURE).sized(0.7F, 0.6F).clientTrackingRange(8).build("beaver"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.ChupacabraEntity>> CHUPACABRA = OTT_ENTITY_TYPES.register("chupacabra",
            () -> Builder.of(com.otterly76.ott.entity.custom.ChupacabraEntity::new, MobCategory.MONSTER).sized(0.6F, 0.8F).clientTrackingRange(8).build("chupacabra"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.projectile.ChupacabraSpitEntity>> CHUPACABRA_SPIT = OTT_ENTITY_TYPES.register("chupacabra_spit",
            () -> Builder.<com.otterly76.ott.entity.projectile.ChupacabraSpitEntity>of(com.otterly76.ott.entity.projectile.ChupacabraSpitEntity::new, MobCategory.MISC).sized(0.5F, 0.5F).clientTrackingRange(4).updateInterval(1).build("chupacabra_spit"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CougarEntity>> COUGAR = OTT_ENTITY_TYPES.register("cougar",
            () -> Builder.of(com.otterly76.ott.entity.custom.CougarEntity::new, MobCategory.CREATURE).sized(0.8F, 0.9F).clientTrackingRange(8).build("cougar"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CoyoteEntity>> COYOTE = OTT_ENTITY_TYPES.register("coyote",
            () -> Builder.of(com.otterly76.ott.entity.custom.CoyoteEntity::new, MobCategory.CREATURE).sized(0.6F, 0.8F).clientTrackingRange(8).build("coyote"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.HowlerEntity>> HOWLER = OTT_ENTITY_TYPES.register("howler",
            () -> Builder.of(com.otterly76.ott.entity.custom.HowlerEntity::new, MobCategory.MONSTER).sized(1.3F, 1.3F).clientTrackingRange(8).build("howler"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.MarmotEntity>> MARMOT = OTT_ENTITY_TYPES.register("marmot",
            () -> Builder.of(com.otterly76.ott.entity.custom.MarmotEntity::new, MobCategory.CREATURE).sized(0.7F, 0.6F).clientTrackingRange(8).build("marmot"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.MouseEntity>> MOUSE = OTT_ENTITY_TYPES.register("mouse",
            () -> Builder.of(com.otterly76.ott.entity.custom.MouseEntity::new, MobCategory.CREATURE).sized(0.3F, 0.3F).clientTrackingRange(8).build("mouse"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.PitViperEntity>> PIT_VIPER = OTT_ENTITY_TYPES.register("pit_viper",
            () -> Builder.of(com.otterly76.ott.entity.custom.PitViperEntity::new, MobCategory.CREATURE).sized(1.0F, 0.5F).clientTrackingRange(8).build("pit_viper"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.RattlesnakeEntity>> RATTLESNAKE = OTT_ENTITY_TYPES.register("rattlesnake",
            () -> Builder.of(com.otterly76.ott.entity.custom.RattlesnakeEntity::new, MobCategory.CREATURE).sized(1.0F, 0.5F).clientTrackingRange(8).build("rattlesnake"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.RingtailEntity>> RINGTAIL = OTT_ENTITY_TYPES.register("ringtail",
            () -> Builder.of(com.otterly76.ott.entity.custom.RingtailEntity::new, MobCategory.CREATURE).sized(0.6F, 0.8F).clientTrackingRange(8).build("ringtail"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SasquatchEntity>> SASQUATCH = OTT_ENTITY_TYPES.register("sasquatch",
            () -> Builder.of(com.otterly76.ott.entity.custom.SasquatchEntity::new, MobCategory.CREATURE).sized(0.8F, 2.6F).clientTrackingRange(8).build("sasquatch"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SkinwalkerEntity>> SKINWALKER = OTT_ENTITY_TYPES.register("skinwalker",
            () -> Builder.of(com.otterly76.ott.entity.custom.SkinwalkerEntity::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8).build("skinwalker"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BewitchedGreywolfEntity>> BEWITCHED_TIMBER_WOLF = OTT_ENTITY_TYPES.register("bewitched_timber_wolf",
            () -> Builder.of(com.otterly76.ott.entity.custom.BewitchedGreywolfEntity::new, MobCategory.MONSTER).sized(0.6F, 0.85F).clientTrackingRange(8).build("bewitched_timber_wolf"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SnakeEntity>> SNAKE = OTT_ENTITY_TYPES.register("snake",
            () -> Builder.of(com.otterly76.ott.entity.custom.SnakeEntity::new, MobCategory.CREATURE).sized(1.0F, 0.5F).clientTrackingRange(8).build("snake"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SquonkEntity>> SQUONK = OTT_ENTITY_TYPES.register("squonk",
            () -> Builder.of(com.otterly76.ott.entity.custom.SquonkEntity::new, MobCategory.MONSTER).sized(0.9F, 0.9F).clientTrackingRange(8).build("squonk"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.TurkeyEntity>> TURKEY = OTT_ENTITY_TYPES.register("turkey",
            () -> Builder.of(com.otterly76.ott.entity.custom.TurkeyEntity::new, MobCategory.CREATURE).sized(0.8F, 0.7F).clientTrackingRange(8).build("turkey"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.WechugeEntity>> WECHUGE = OTT_ENTITY_TYPES.register("wechuge",
            () -> Builder.of(com.otterly76.ott.entity.custom.WechugeEntity::new, MobCategory.MONSTER).sized(0.8F, 2.2F).clientTrackingRange(8).build("wechuge"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.WendigoEntity>> WENDIGO = OTT_ENTITY_TYPES.register("wendigo",
            () -> Builder.of(com.otterly76.ott.entity.custom.WendigoEntity::new, MobCategory.MONSTER).sized(0.8F, 1.8F).clientTrackingRange(8).build("wendigo"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.WolverineEntity>> WOLVERINE = OTT_ENTITY_TYPES.register("wolverine",
            () -> Builder.of(com.otterly76.ott.entity.custom.WolverineEntity::new, MobCategory.CREATURE).sized(0.9F, 0.8F).clientTrackingRange(8).build("wolverine"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Ghost>> GHOST = OTT_ENTITY_TYPES.register("ghost",
            () -> Builder.of(com.otterly76.ott.entity.custom.Ghost::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8).build("ghost"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Spectre>> SPECTRE = OTT_ENTITY_TYPES.register("spectre",
            () -> Builder.of(com.otterly76.ott.entity.custom.Spectre::new, MobCategory.MONSTER).sized(1.5F, 2.5F).clientTrackingRange(8).build("spectre"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Haunt>> HAUNT = OTT_ENTITY_TYPES.register("haunt",
            () -> Builder.of(com.otterly76.ott.entity.custom.Haunt::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8).build("haunt"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Geist>> GEIST = OTT_ENTITY_TYPES.register("geist",
            () -> Builder.of(com.otterly76.ott.entity.custom.Geist::new, MobCategory.MONSTER).sized(0.6F, 0.6F).clientTrackingRange(8).build("geist"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.TreeEnt>> TREE_ENT = OTT_ENTITY_TYPES.register("tree_ent",
            () -> Builder.of(com.otterly76.ott.entity.custom.TreeEnt::new, MobCategory.MONSTER).sized(1.2F, 3.5F).clientTrackingRange(8).build("tree_ent"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.HermitKing>> HERMIT_KING = OTT_ENTITY_TYPES.register("hermit_king",
            () -> Builder.of(com.otterly76.ott.entity.custom.HermitKing::new, MobCategory.MONSTER).sized(2.0F, 1.5F).clientTrackingRange(8).build("hermit_king"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SandHermit>> SAND_HERMIT = OTT_ENTITY_TYPES.register("sand_hermit",
            () -> Builder.of(com.otterly76.ott.entity.custom.SandHermit::new, MobCategory.MONSTER).sized(0.8F, 0.6F).clientTrackingRange(8).build("sand_hermit"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SeaViper>> SEA_VIPER = OTT_ENTITY_TYPES.register("sea_viper",
            () -> Builder.of(com.otterly76.ott.entity.custom.SeaViper::new, MobCategory.MONSTER).sized(0.8F, 0.8F).clientTrackingRange(8).build("sea_viper"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Yeti>> YETI = OTT_ENTITY_TYPES.register("yeti",
            () -> Builder.of(com.otterly76.ott.entity.custom.Yeti::new, MobCategory.MONSTER).sized(1.2F, 2.5F).clientTrackingRange(8).build("yeti"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.VileGator>> VILE_GATOR = OTT_ENTITY_TYPES.register("vile_gator",
            () -> Builder.of(com.otterly76.ott.entity.custom.VileGator::new, MobCategory.MONSTER).sized(1.2F, 0.8F).clientTrackingRange(8).build("vile_gator"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Phoenix>> PHOENIX = OTT_ENTITY_TYPES.register("phoenix",
            () -> Builder.of(com.otterly76.ott.entity.custom.Phoenix::new, MobCategory.MONSTER).sized(1.0F, 1.5F).clientTrackingRange(8).build("phoenix"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BabyPhoenix>> BABY_PHOENIX = OTT_ENTITY_TYPES.register("baby_phoenix",
            () -> Builder.of(com.otterly76.ott.entity.custom.BabyPhoenix::new, MobCategory.MONSTER).sized(0.5F, 0.7F).clientTrackingRange(8).build("baby_phoenix"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BoneStalker>> BONE_STALKER = OTT_ENTITY_TYPES.register("bone_stalker",
            () -> Builder.of(com.otterly76.ott.entity.custom.BoneStalker::new, MobCategory.MONSTER).sized(0.6F, 1.9F).clientTrackingRange(8).build("bone_stalker"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.Shadow>> SHADOW = OTT_ENTITY_TYPES.register("shadow",
            () -> Builder.of(com.otterly76.ott.entity.custom.Shadow::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8).build("shadow"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CherryTreeEnt>> CHERRY_TREE_ENT = OTT_ENTITY_TYPES.register("cherry_tree_ent",
            () -> Builder.of(com.otterly76.ott.entity.custom.CherryTreeEnt::new, MobCategory.MONSTER).sized(1.2F, 3.5F).clientTrackingRange(8).build("cherry_tree_ent"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.GoldenHermitKing>> GOLDEN_HERMIT_KING = OTT_ENTITY_TYPES.register("golden_hermit_king",
            () -> Builder.of(com.otterly76.ott.entity.custom.GoldenHermitKing::new, MobCategory.MONSTER).sized(2.0F, 1.5F).clientTrackingRange(8).build("golden_hermit_king"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CoralSeaViper>> CORAL_SEA_VIPER = OTT_ENTITY_TYPES.register("coral_sea_viper",
            () -> Builder.of(com.otterly76.ott.entity.custom.CoralSeaViper::new, MobCategory.MONSTER).sized(0.8F, 0.8F).clientTrackingRange(8).build("coral_sea_viper"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.AridYeti>> ARID_YETI = OTT_ENTITY_TYPES.register("arid_yeti",
            () -> Builder.of(com.otterly76.ott.entity.custom.AridYeti::new, MobCategory.MONSTER).sized(1.2F, 2.5F).clientTrackingRange(8).build("arid_yeti"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.WindPhoenix>> WIND_PHOENIX = OTT_ENTITY_TYPES.register("wind_phoenix",
            () -> Builder.of(com.otterly76.ott.entity.custom.WindPhoenix::new, MobCategory.MONSTER).sized(1.0F, 1.5F).clientTrackingRange(8).build("wind_phoenix"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BabyWindPhoenix>> BABY_WIND_PHOENIX = OTT_ENTITY_TYPES.register("baby_wind_phoenix",
            () -> Builder.of(com.otterly76.ott.entity.custom.BabyWindPhoenix::new, MobCategory.MONSTER).sized(0.5F, 0.7F).clientTrackingRange(8).build("baby_wind_phoenix"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BoggedBoneStalker>> BOGGED_BONE_STALKER = OTT_ENTITY_TYPES.register("bogged_bone_stalker",
            () -> Builder.of(com.otterly76.ott.entity.custom.BoggedBoneStalker::new, MobCategory.MONSTER).sized(0.6F, 1.9F).clientTrackingRange(8).build("bogged_bone_stalker"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BoggedShadow>> BOGGED_SHADOW = OTT_ENTITY_TYPES.register("bogged_shadow",
            () -> Builder.of(com.otterly76.ott.entity.custom.BoggedShadow::new, MobCategory.MONSTER).sized(0.6F, 1.8F).clientTrackingRange(8).build("bogged_shadow"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.GildedTreeEnt>> GILDED_TREE_ENT = OTT_ENTITY_TYPES.register("gilded_tree_ent",
            () -> Builder.of(com.otterly76.ott.entity.custom.GildedTreeEnt::new, MobCategory.MONSTER).sized(1.2F, 3.5F).clientTrackingRange(8).build("gilded_tree_ent"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CichlidEntity>> CICHLID = OTT_ENTITY_TYPES.register("cichlid",
            () -> Builder.of(com.otterly76.ott.entity.custom.CichlidEntity::new, MobCategory.WATER_AMBIENT).sized(0.4F, 0.4F).clientTrackingRange(8).build("cichlid"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.LeopardCatEntity>> LEOPARD_CAT = OTT_ENTITY_TYPES.register("leopard_cat",
            () -> Builder.of(com.otterly76.ott.entity.custom.LeopardCatEntity::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).build("leopard_cat"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.EchidnaEntity>> ECHIDNA = OTT_ENTITY_TYPES.register("echidna",
            () -> Builder.of(com.otterly76.ott.entity.custom.EchidnaEntity::new, MobCategory.CREATURE).sized(0.5F, 0.5F).clientTrackingRange(8).build("echidna"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.GuitarfishEntity>> GUITARFISH = OTT_ENTITY_TYPES.register("guitarfish",
            () -> Builder.of(com.otterly76.ott.entity.custom.GuitarfishEntity::new, MobCategory.WATER_CREATURE).sized(1.0F, 0.3F).clientTrackingRange(8).build("guitarfish"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BonnetheadSharkEntity>> BONNETHEAD_SHARK = OTT_ENTITY_TYPES.register("bonnethead_shark",
            () -> Builder.of(com.otterly76.ott.entity.custom.BonnetheadSharkEntity::new, MobCategory.WATER_CREATURE).sized(1.2F, 0.6F).clientTrackingRange(8).build("bonnethead_shark"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BurrowingOwlEntity>> BURROWING_OWL = OTT_ENTITY_TYPES.register("burrowing_owl",
            () -> Builder.of(com.otterly76.ott.entity.custom.BurrowingOwlEntity::new, MobCategory.CREATURE).sized(0.5F, 0.5F).clientTrackingRange(8).build("burrowing_owl"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.BushdogEntity>> BUSHDOG = OTT_ENTITY_TYPES.register("bushdog",
            () -> Builder.of(com.otterly76.ott.entity.custom.BushdogEntity::new, MobCategory.CREATURE).sized(0.6F, 0.6F).clientTrackingRange(8).build("bushdog"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.QuailEntity>> QUAIL = OTT_ENTITY_TYPES.register("quail",
            () -> Builder.of(com.otterly76.ott.entity.custom.QuailEntity::new, MobCategory.CREATURE).sized(0.4F, 0.5F).clientTrackingRange(8).build("quail"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.CandycaneSnailEntity>> CANDYCANE_SNAIL = OTT_ENTITY_TYPES.register("candycane_snail",
            () -> Builder.of(com.otterly76.ott.entity.custom.CandycaneSnailEntity::new, MobCategory.CREATURE).sized(0.4F, 0.4F).clientTrackingRange(8).build("candycane_snail"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.FireSalamanderEntity>> FIRE_SALAMANDER = OTT_ENTITY_TYPES.register("fire_salamander",
            () -> Builder.of(com.otterly76.ott.entity.custom.FireSalamanderEntity::new, MobCategory.CREATURE).sized(0.5F, 0.3F).clientTrackingRange(8).build("fire_salamander"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.RiverTurtleEntity>> RIVER_TURTLE = OTT_ENTITY_TYPES.register("river_turtle",
            () -> Builder.of(com.otterly76.ott.entity.custom.RiverTurtleEntity::new, MobCategory.CREATURE).sized(0.8F, 0.4F).clientTrackingRange(8).build("river_turtle"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.GoblinSharkEntity>> GOBLIN_SHARK = OTT_ENTITY_TYPES.register("goblin_shark",
            () -> Builder.of(com.otterly76.ott.entity.custom.GoblinSharkEntity::new, MobCategory.WATER_CREATURE).sized(1.5F, 0.7F).clientTrackingRange(8).build("goblin_shark"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.GuineaFowlEntity>> GUINEA_FOWL = OTT_ENTITY_TYPES.register("guinea_fowl",
            () -> Builder.of(com.otterly76.ott.entity.custom.GuineaFowlEntity::new, MobCategory.CREATURE).sized(0.6F, 0.8F).clientTrackingRange(8).build("guinea_fowl"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.ImpalaEntity>> IMPALA = OTT_ENTITY_TYPES.register("impala",
            () -> Builder.of(com.otterly76.ott.entity.custom.ImpalaEntity::new, MobCategory.CREATURE).sized(1.2F, 1.4F).clientTrackingRange(8).build("impala"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.MantaRayEntity>> MANTA_RAY = OTT_ENTITY_TYPES.register("manta_ray",
            () -> Builder.of(com.otterly76.ott.entity.custom.MantaRayEntity::new, MobCategory.WATER_CREATURE).sized(1.5F, 0.4F).clientTrackingRange(8).build("manta_ray"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.StorkEntity>> STORK = OTT_ENTITY_TYPES.register("stork",
            () -> Builder.of(com.otterly76.ott.entity.custom.StorkEntity::new, MobCategory.CREATURE).sized(0.9F, 1.8F).clientTrackingRange(8).build("stork"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.MoleEntity>> MOLE = OTT_ENTITY_TYPES.register("mole",
            () -> Builder.of(com.otterly76.ott.entity.custom.MoleEntity::new, MobCategory.CREATURE).sized(0.5F, 0.4F).clientTrackingRange(8).build("mole"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.TreeKangarooEntity>> TREE_KANGAROO = OTT_ENTITY_TYPES.register("tree_kangaroo",
            () -> Builder.of(com.otterly76.ott.entity.custom.TreeKangarooEntity::new, MobCategory.CREATURE).sized(0.7F, 1.2F).clientTrackingRange(8).build("tree_kangaroo"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.PallasCatEntity>> PALLAS_CAT = OTT_ENTITY_TYPES.register("pallas_cat",
            () -> Builder.of(com.otterly76.ott.entity.custom.PallasCatEntity::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).build("pallas_cat"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.PinkLandIguanaEntity>> PINK_LAND_IGUANA = OTT_ENTITY_TYPES.register("pink_land_iguana",
            () -> Builder.of(com.otterly76.ott.entity.custom.PinkLandIguanaEntity::new, MobCategory.CREATURE).sized(0.8F, 0.4F).clientTrackingRange(8).build("pink_land_iguana"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.PsychoJellyEntity>> PSYCHO_JELLY = OTT_ENTITY_TYPES.register("psycho_jelly",
            () -> Builder.of(com.otterly76.ott.entity.custom.PsychoJellyEntity::new, MobCategory.WATER_AMBIENT).sized(0.7F, 0.7F).clientTrackingRange(8).build("psycho_jelly"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.SpoonbillEntity>> SPOONBILL = OTT_ENTITY_TYPES.register("spoonbill",
            () -> Builder.of(com.otterly76.ott.entity.custom.SpoonbillEntity::new, MobCategory.CREATURE).sized(0.7F, 1.2F).clientTrackingRange(8).build("spoonbill"));

    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.GiantSoftshellTurtleEntity>> GIANT_SOFTSHELL_TURTLE = OTT_ENTITY_TYPES.register("giant_softshell_turtle",
            () -> Builder.of(com.otterly76.ott.entity.custom.GiantSoftshellTurtleEntity::new, MobCategory.CREATURE).sized(1.2F, 0.5F).clientTrackingRange(8).build("giant_softshell_turtle"));

    // --- Friends and Foes ---
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.FiddlerCrabEntity>> FIDDLER_CRAB = OTT_ENTITY_TYPES.register("fiddler_crab",
            () -> Builder.of(com.otterly76.ott.entity.custom.FiddlerCrabEntity::new, MobCategory.CREATURE).sized(0.875F, 0.5625F).clientTrackingRange(10).build("fiddler_crab"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.GlareEntity>> GLARE = OTT_ENTITY_TYPES.register("glare",
            () -> Builder.of(com.otterly76.ott.entity.custom.GlareEntity::new, MobCategory.AMBIENT).sized(0.875F, 1.1875F).clientTrackingRange(8).build("glare"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.IceologerEntity>> ICEOLOGER = OTT_ENTITY_TYPES.register("iceologer",
            () -> Builder.of(com.otterly76.ott.entity.custom.IceologerEntity::new, MobCategory.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(10).build("iceologer"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.IceologerIceChunkEntity>> ICE_CHUNK = OTT_ENTITY_TYPES.register("ice_chunk",
            () -> Builder.of(com.otterly76.ott.entity.custom.IceologerIceChunkEntity::new, MobCategory.MISC).fireImmune().sized(2.5F, 1.0F).clientTrackingRange(6).build("ice_chunk"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.MaulerEntity>> MAULER = OTT_ENTITY_TYPES.register("mauler",
            () -> Builder.of(com.otterly76.ott.entity.custom.MaulerEntity::new, MobCategory.CREATURE).sized(0.5625F, 0.5625F).clientTrackingRange(10).build("mauler"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.RascalEntity>> RASCAL = OTT_ENTITY_TYPES.register("rascal",
            () -> Builder.of(com.otterly76.ott.entity.custom.RascalEntity::new, MobCategory.CREATURE).sized(0.9F, 1.25F).clientTrackingRange(10).build("rascal"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.TuffGolemEntity>> TUFF_GOLEM = OTT_ENTITY_TYPES.register("tuff_golem",
            () -> Builder.of(com.otterly76.ott.entity.custom.TuffGolemEntity::new, MobCategory.MISC).sized(0.75F, 1.0625F).clientTrackingRange(10).build("tuff_golem"));
    public static final Supplier<EntityType<com.otterly76.ott.entity.custom.WildfireEntity>> WILDFIRE = OTT_ENTITY_TYPES.register("wildfire",
            () -> Builder.of(com.otterly76.ott.entity.custom.WildfireEntity::new, MobCategory.MONSTER).fireImmune().sized(1.12F, 3.0F).clientTrackingRange(10).build("wildfire"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
        OTT_ENTITY_TYPES.register(eventBus);
    }
}