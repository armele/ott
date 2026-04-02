package com.otterly76.ott.event;

import com.otterly76.ott.entity.Creaking;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.client.model.CreakingModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.custom.HappyGhast;
import com.otterly76.ott.entity.custom.EtherealShrimpEntity;
import com.otterly76.ott.item.ModItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

public class ModEventBusEvents {

    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CREAKING, CreakingModel::createBodyLayer);
    }

    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CREAKING.get(), Creaking.createAttributes().build());
        event.put(ModEntities.HAPPY_GHAST.get(), HappyGhast.createAttributes().build());
        event.put(ModEntities.COPPER_GOLEM.get(), com.otterly76.ott.entity.custom.CopperGolem.createAttributes().build());
        event.put(ModEntities.MAN_O_WAR.get(), com.otterly76.ott.entity.custom.ManOWar.createAttributes().build());
        event.put(ModEntities.DUCK.get(), net.minecraft.world.entity.animal.Chicken.createAttributes().build());
        event.put(ModEntities.GOOSE.get(), net.minecraft.world.entity.animal.Chicken.createAttributes().build());
        event.put(ModEntities.STINGRAY.get(), com.otterly76.ott.entity.custom.StingrayEntity.createAttributes().build());
        event.put(ModEntities.SUNFISH.get(), com.otterly76.ott.entity.custom.Sunfish.setAttributes().build());
        event.put(ModEntities.KRILL.get(), com.otterly76.ott.entity.custom.Krill.setAttributes().build());
        event.put(ModEntities.ANGELFISH.get(), com.otterly76.ott.entity.custom.Angelfish.setAttributes().build());
        event.put(ModEntities.BARRELEYE.get(), com.otterly76.ott.entity.custom.Barreleye.setAttributes().build());
        event.put(ModEntities.FLOUNDER.get(), com.otterly76.ott.entity.custom.Flounder.setAttributes().build());
        event.put(ModEntities.MARINE_IGUANA.get(), com.otterly76.ott.entity.custom.MarineIguana.setAttributes().build());
        event.put(ModEntities.GECKO.get(), com.otterly76.ott.entity.custom.Gecko.setAttributes().build());
        event.put(ModEntities.EMU.get(), com.otterly76.ott.entity.custom.Emu.setAttributes().build());
        event.put(ModEntities.HOOPOE.get(), com.otterly76.ott.entity.custom.Hoopoe.setAttributes().build());
        event.put(ModEntities.PHEASANT.get(), com.otterly76.ott.entity.custom.Pheasant.setAttributes().build());
        event.put(ModEntities.TOUCAN.get(), com.otterly76.ott.entity.custom.Toucan.setAttributes().build());
        event.put(ModEntities.CATFISH.get(), com.otterly76.ott.entity.custom.Catfish.createAttributes().build());
        event.put(ModEntities.BASS.get(), net.minecraft.world.entity.animal.AbstractSchoolingFish.createAttributes().build());
        event.put(ModEntities.BUTTERFLY.get(), com.otterly76.ott.entity.custom.Butterfly.createAttributes().build());
        event.put(ModEntities.CATERPILLAR.get(), com.otterly76.ott.entity.custom.Caterpillar.createAttributes().build());
        event.put(ModEntities.FIREFLY.get(), com.otterly76.ott.entity.custom.Firefly.createAttributes().build());
        event.put(ModEntities.ALLIGATOR.get(), com.otterly76.ott.entity.custom.Alligator.createAttributes().build());
        event.put(ModEntities.ELEPHANT.get(), com.otterly76.ott.entity.custom.Elephant.createAttributes().build());
        event.put(ModEntities.GIRAFFE.get(), com.otterly76.ott.entity.custom.Giraffe.createAttributes().build());
        event.put(ModEntities.HIPPO.get(), com.otterly76.ott.entity.custom.Hippo.createAttributes().build());
        event.put(ModEntities.LION.get(), com.otterly76.ott.entity.custom.Lion.createAttributes().build());
        event.put(ModEntities.RHINO.get(), com.otterly76.ott.entity.custom.Rhino.createAttributes().build());
        event.put(ModEntities.LIZARD.get(), com.otterly76.ott.entity.custom.Lizard.createAttributes().build());
        event.put(ModEntities.LIZARD_TAIL.get(), com.otterly76.ott.entity.custom.LizardTail.createAttributes().build());
        event.put(ModEntities.SNAIL.get(), com.otterly76.ott.entity.custom.Snail.createAttributes().build());
        event.put(ModEntities.TORTOISE.get(), com.otterly76.ott.entity.custom.Tortoise.createAttributes().build());
        event.put(ModEntities.VULTURE.get(), com.otterly76.ott.entity.custom.Vulture.createAttributes().build());
        event.put(ModEntities.ZEBRA.get(), com.otterly76.ott.entity.custom.Zebra.createAttributes().build());
        event.put(ModEntities.MOOSE.get(), com.otterly76.ott.entity.custom.Moose.createAttributes().build());
        event.put(ModEntities.MAMMOTH.get(), com.otterly76.ott.entity.custom.Mammoth.createAttributes().build());
        event.put(ModEntities.MYCELIUM_MAMMOTH.get(), com.otterly76.ott.entity.custom.MyceliumMammoth.createAttributes().build());
        event.put(ModEntities.FENNEC_FOX.get(), com.otterly76.ott.entity.custom.FennecFox.createAttributes().build());
        event.put(ModEntities.BLUEJAY.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.CANARY.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.CARDINAL.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.FINCH.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.ROBIN.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.SPARROW.get(), com.otterly76.ott.entity.custom.Bird.createAttributes().build());
        event.put(ModEntities.BROWN_BEAR.get(), com.otterly76.ott.entity.custom.Bear.createAttributes().build());
        event.put(ModEntities.BLACK_BEAR.get(), com.otterly76.ott.entity.custom.Bear.createAttributes().build());
        event.put(ModEntities.DEER.get(), com.otterly76.ott.entity.custom.Deer.createAttributes().build());
        event.put(ModEntities.REINDEER.get(), com.otterly76.ott.entity.custom.Deer.createAttributes().build());
        event.put(ModEntities.WHITE_DEER.get(), com.otterly76.ott.entity.custom.Deer.createAttributes().build());

        event.put(ModEntities.CAPYBARA.get(), com.otterly76.ott.entity.custom.CapybaraEntity.createAttributes().build());
        event.put(ModEntities.HEDGEHOG.get(), com.otterly76.ott.entity.custom.HedgehogEntity.createAttributes().build());
        event.put(ModEntities.KIWI.get(), com.otterly76.ott.entity.custom.KiwiEntity.createAttributes().build());
        event.put(ModEntities.PENGUIN.get(), com.otterly76.ott.entity.custom.PenguinEntity.createAttributes().build());
        event.put(ModEntities.SEAL.get(), com.otterly76.ott.entity.custom.SealEntity.createAttributes().build());
        event.put(ModEntities.SEA_URCHIN.get(), com.otterly76.ott.entity.custom.SeaUrchinEntity.createAttributes().build());
        event.put(ModEntities.LARGE_JELLYFISH.get(), com.otterly76.ott.entity.custom.LargeJellyfishEntity.createAttributes().build());
        event.put(ModEntities.SEAHORSE.get(), com.otterly76.ott.entity.custom.SeahorseEntity.createAttributes().build());
        event.put(ModEntities.ETHEREAL_SHRIMP.get(), EtherealShrimpEntity.createAttributes().build());
        event.put(ModEntities.STARFISH.get(), com.otterly76.ott.entity.custom.StarfishEntity.createAttributes().build());
        event.put(ModEntities.SMALL_JELLYFISH.get(), com.otterly76.ott.entity.custom.SmallJellyfishEntity.createAttributes().build());
        event.put(ModEntities.MEDIUM_JELLYFISH.get(), com.otterly76.ott.entity.custom.MediumJellyfishEntity.createAttributes().build());

        event.put(ModEntities.DRAGONFLY.get(), com.otterly76.ott.entity.custom.DragonflyEntity.createAttributes().build());
        event.put(ModEntities.DUMBO_OCTOPUS.get(), com.otterly76.ott.entity.custom.DumboOctopusEntity.createAttributes().build());
        event.put(ModEntities.FERRET.get(), com.otterly76.ott.entity.custom.FerretEntity.createAttributes().build());
        event.put(ModEntities.JUMPING_SPIDER.get(), com.otterly76.ott.entity.custom.JumpingSpiderEntity.createAttributes().build());
        event.put(ModEntities.KOI_FISH.get(), com.otterly76.ott.entity.custom.KoiFishEntity.createAttributes().build());
        event.put(ModEntities.OTTER.get(), com.otterly76.ott.entity.custom.OtterEntity.createAttributes().build());
        event.put(ModEntities.RED_PANDA.get(), com.otterly76.ott.entity.custom.RedPandaEntity.createAttributes().build());
        event.put(ModEntities.SEA_BUNNY.get(), com.otterly76.ott.entity.custom.SeaBunnyEntity.createAttributes().build());
        event.put(ModEntities.SMALL_FIREFLY.get(), com.otterly76.ott.entity.custom.SmallFirefly.createAttributes().build());

        event.put(ModEntities.GHOST.get(), com.otterly76.ott.entity.custom.Ghost.createAttributes().build());
        event.put(ModEntities.SPECTRE.get(), com.otterly76.ott.entity.custom.Spectre.createAttributes().build());
        event.put(ModEntities.HAUNT.get(), com.otterly76.ott.entity.custom.Haunt.createAttributes().build());
        event.put(ModEntities.GEIST.get(), com.otterly76.ott.entity.custom.Geist.createAttributes().build());

        event.put(ModEntities.TREE_ENT.get(), com.otterly76.ott.entity.custom.TreeEnt.createAttributes().build());
        event.put(ModEntities.HERMIT_KING.get(), com.otterly76.ott.entity.custom.HermitKing.createAttributes().build());
        event.put(ModEntities.SEA_VIPER.get(), com.otterly76.ott.entity.custom.SeaViper.createAttributes().build());
        event.put(ModEntities.YETI.get(), com.otterly76.ott.entity.custom.Yeti.createAttributes().build());
        event.put(ModEntities.VILE_GATOR.get(), com.otterly76.ott.entity.custom.VileGator.createAttributes().build());
        event.put(ModEntities.PHOENIX.get(), com.otterly76.ott.entity.custom.Phoenix.createAttributes().build());
        event.put(ModEntities.BABY_PHOENIX.get(), com.otterly76.ott.entity.custom.BabyPhoenix.createAttributes().build());
        event.put(ModEntities.BONE_STALKER.get(), com.otterly76.ott.entity.custom.BoneStalker.createAttributes().build());
        event.put(ModEntities.SHADOW.get(), com.otterly76.ott.entity.custom.Shadow.createAttributes().build());
        event.put(ModEntities.CHERRY_TREE_ENT.get(), com.otterly76.ott.entity.custom.CherryTreeEnt.createAttributes().build());
        event.put(ModEntities.GOLDEN_HERMIT_KING.get(), com.otterly76.ott.entity.custom.GoldenHermitKing.createAttributes().build());
        event.put(ModEntities.CORAL_SEA_VIPER.get(), com.otterly76.ott.entity.custom.CoralSeaViper.createAttributes().build());
        event.put(ModEntities.ARID_YETI.get(), com.otterly76.ott.entity.custom.AridYeti.createAttributes().build());
        event.put(ModEntities.WIND_PHOENIX.get(), com.otterly76.ott.entity.custom.WindPhoenix.createAttributes().build());
        event.put(ModEntities.BABY_WIND_PHOENIX.get(), com.otterly76.ott.entity.custom.BabyWindPhoenix.createAttributes().build());
        event.put(ModEntities.BOGGED_BONE_STALKER.get(), com.otterly76.ott.entity.custom.BoggedBoneStalker.createAttributes().build());
        event.put(ModEntities.BOGGED_SHADOW.get(), com.otterly76.ott.entity.custom.BoggedShadow.createAttributes().build());
        event.put(ModEntities.GILDED_TREE_ENT.get(), com.otterly76.ott.entity.custom.GildedTreeEnt.createAttributes().build());

        event.put(ModEntities.BEAVER.get(), com.otterly76.ott.entity.custom.BeaverEntity.createAttributes().build());
        event.put(ModEntities.CHUPACABRA.get(), com.otterly76.ott.entity.custom.ChupacabraEntity.createAttributes().build());
        event.put(ModEntities.COUGAR.get(), com.otterly76.ott.entity.custom.CougarEntity.createAttributes().build());
        event.put(ModEntities.COYOTE.get(), com.otterly76.ott.entity.custom.CoyoteEntity.createAttributes().build());
        event.put(ModEntities.HOWLER.get(), com.otterly76.ott.entity.custom.HowlerEntity.createAttributes().build());
        event.put(ModEntities.BEWITCHED_TIMBER_WOLF.get(), com.otterly76.ott.entity.custom.BewitchedGreywolfEntity.createAttributes().build());
        event.put(ModEntities.MARMOT.get(), com.otterly76.ott.entity.custom.MarmotEntity.createAttributes().build());
        event.put(ModEntities.MOUSE.get(), com.otterly76.ott.entity.custom.MouseEntity.createAttributes().build());
        event.put(ModEntities.PIT_VIPER.get(), com.otterly76.ott.entity.custom.PitViperEntity.createAttributes().build());
        event.put(ModEntities.RATTLESNAKE.get(), com.otterly76.ott.entity.custom.RattlesnakeEntity.createAttributes().build());
        event.put(ModEntities.RINGTAIL.get(), com.otterly76.ott.entity.custom.RingtailEntity.createAttributes().build());
        event.put(ModEntities.SASQUATCH.get(), com.otterly76.ott.entity.custom.SasquatchEntity.createAttributes().build());
        event.put(ModEntities.SKINWALKER.get(), com.otterly76.ott.entity.custom.SkinwalkerEntity.createAttributes().build());
        event.put(ModEntities.SNAKE.get(), com.otterly76.ott.entity.custom.SnakeEntity.createAttributes().build());
        event.put(ModEntities.SQUONK.get(), com.otterly76.ott.entity.custom.SquonkEntity.createAttributes().build());
        event.put(ModEntities.TURKEY.get(), com.otterly76.ott.entity.custom.TurkeyEntity.createAttributes().build());
        event.put(ModEntities.WECHUGE.get(), com.otterly76.ott.entity.custom.WechugeEntity.createAttributes().build());
        event.put(ModEntities.WENDIGO.get(), com.otterly76.ott.entity.custom.WendigoEntity.createAttributes().build());
        event.put(ModEntities.WOLVERINE.get(), com.otterly76.ott.entity.custom.WolverineEntity.createAttributes().build());

        event.put(ModEntities.CICHLID.get(), com.otterly76.ott.entity.custom.CichlidEntity.createAttributes().build());
        event.put(ModEntities.LEOPARD_CAT.get(), com.otterly76.ott.entity.custom.LeopardCatEntity.createAttributes().build());
        event.put(ModEntities.WATER_BUFFALO.get(), net.minecraft.world.entity.animal.Cow.createAttributes().build());
        event.put(ModEntities.ECHIDNA.get(), com.otterly76.ott.entity.custom.EchidnaEntity.createAttributes().build());
        event.put(ModEntities.GUITARFISH.get(), com.otterly76.ott.entity.custom.GuitarfishEntity.createAttributes().build());
        event.put(ModEntities.BONNETHEAD_SHARK.get(), com.otterly76.ott.entity.custom.BonnetheadSharkEntity.createAttributes().build());
        event.put(ModEntities.BURROWING_OWL.get(), com.otterly76.ott.entity.custom.BurrowingOwlEntity.createAttributes().build());
        event.put(ModEntities.BUSHDOG.get(), com.otterly76.ott.entity.custom.BushdogEntity.createAttributes().build());
        event.put(ModEntities.QUAIL.get(), com.otterly76.ott.entity.custom.QuailEntity.createAttributes().build());
        event.put(ModEntities.CANDYCANE_SNAIL.get(), com.otterly76.ott.entity.custom.CandycaneSnailEntity.createAttributes().build());
        event.put(ModEntities.FIRE_SALAMANDER.get(), com.otterly76.ott.entity.custom.FireSalamanderEntity.createAttributes().build());
        event.put(ModEntities.RIVER_TURTLE.get(), com.otterly76.ott.entity.custom.RiverTurtleEntity.createAttributes().build());
        event.put(ModEntities.GOBLIN_SHARK.get(), com.otterly76.ott.entity.custom.GoblinSharkEntity.createAttributes().build());
        event.put(ModEntities.GUINEA_FOWL.get(), com.otterly76.ott.entity.custom.GuineaFowlEntity.createAttributes().build());
        event.put(ModEntities.IMPALA.get(), com.otterly76.ott.entity.custom.ImpalaEntity.createAttributes().build());
        event.put(ModEntities.MANTA_RAY.get(), com.otterly76.ott.entity.custom.MantaRayEntity.createAttributes().build());
        event.put(ModEntities.STORK.get(), com.otterly76.ott.entity.custom.StorkEntity.createAttributes().build());
        event.put(ModEntities.MOLE.get(), com.otterly76.ott.entity.custom.MoleEntity.createAttributes().build());
        event.put(ModEntities.TREE_KANGAROO.get(), com.otterly76.ott.entity.custom.TreeKangarooEntity.createAttributes().build());
        event.put(ModEntities.PALLAS_CAT.get(), com.otterly76.ott.entity.custom.PallasCatEntity.createAttributes().build());
        event.put(ModEntities.PINK_LAND_IGUANA.get(), com.otterly76.ott.entity.custom.PinkLandIguanaEntity.createAttributes().build());
        event.put(ModEntities.PSYCHO_JELLY.get(), com.otterly76.ott.entity.custom.PsychoJellyEntity.createAttributes().build());
        event.put(ModEntities.SPOONBILL.get(), com.otterly76.ott.entity.custom.SpoonbillEntity.createAttributes().build());
        event.put(ModEntities.GIANT_SOFTSHELL_TURTLE.get(), com.otterly76.ott.entity.custom.GiantSoftshellTurtleEntity.createAttributes().build());
        // --- Ecologics ---
        event.put(ModEntities.COCONUT_CRAB.get(), com.otterly76.ott.entity.custom.CoconutCrabEntity.createAttributes().build());
        event.put(ModEntities.SAND_CRAB.get(), com.otterly76.ott.entity.custom.SandCrabEntity.createAttributes().build());
        // --- Friends and Foes ---
        event.put(ModEntities.FIDDLER_CRAB.get(), com.otterly76.ott.entity.custom.FiddlerCrabEntity.createAttributes().build());
        event.put(ModEntities.GLARE.get(), com.otterly76.ott.entity.custom.GlareEntity.createAttributes().build());
        event.put(ModEntities.ICEOLOGER.get(), com.otterly76.ott.entity.custom.IceologerEntity.createAttributes().build());
        event.put(ModEntities.MAULER.get(), com.otterly76.ott.entity.custom.MaulerEntity.createAttributes().build());
        event.put(ModEntities.RASCAL.get(), com.otterly76.ott.entity.custom.RascalEntity.createAttributes().build());
        event.put(ModEntities.TUFF_GOLEM.get(), com.otterly76.ott.entity.custom.TuffGolemEntity.createAttributes().build());
        event.put(ModEntities.WILDFIRE.get(), com.otterly76.ott.entity.custom.WildfireEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void onFurnaceFuelBurnTime(FurnaceFuelBurnTimeEvent event) {
        if (event.getItemStack().is(ModItems.TINY_COAL.get()) || event.getItemStack().is(ModItems.TINY_CHARCOAL.get())) {
            event.setBurnTime(200);
        }
    }

    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        // Tell the game that Allays are allowed to spawn on the ground in our biome
        event.register(
                EntityType.ALLAY,
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mob::checkMobSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.MAN_O_WAR.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.ManOWar::checkManOWarSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.KRILL.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Krill::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.ANGELFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Angelfish::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.BARRELEYE.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Barreleye::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.FLOUNDER.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Flounder::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.MARINE_IGUANA.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.MarineIguana::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.GECKO.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Gecko::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.EMU.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Emu::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.HOOPOE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.PHEASANT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Pheasant::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.TOUCAN.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Toucan::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.DUCK.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.GOOSE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.WATER_BUFFALO.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.STINGRAY.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.StingrayEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.SUNFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Sunfish::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.CATFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AbstractFish::checkSurfaceWaterAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.BASS.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                AbstractFish::checkSurfaceWaterAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.BUTTERFLY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.CATERPILLAR.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.FIREFLY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SMALL_FIREFLY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.ALLIGATOR.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Alligator::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.ELEPHANT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Elephant::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.GIRAFFE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Giraffe::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.HIPPO.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Hippo::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.LION.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Lion::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.RHINO.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Rhino::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.LIZARD.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SNAIL.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.TORTOISE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.VULTURE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Vulture::checkVultureSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.ZEBRA.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.MOOSE.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.FENNEC_FOX.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.MAMMOTH.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Elephant::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.MYCELIUM_MAMMOTH.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.MyceliumMammoth::checkMyceliumMammothSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.BLUEJAY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.CANARY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.CARDINAL.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.FINCH.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.ROBIN.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SPARROW.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Bird::checkBirdSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.BROWN_BEAR.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.BLACK_BEAR.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.DEER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.REINDEER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.WHITE_DEER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.CAPYBARA.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.HEDGEHOG.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.KIWI.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.PENGUIN.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SEAL.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.LARGE_JELLYFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.LargeJellyfishEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SEAHORSE.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.SeahorseEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.ETHEREAL_SHRIMP.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EtherealShrimpEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.STARFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.StarfishEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SMALL_JELLYFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.SmallJellyfishEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.MEDIUM_JELLYFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.MediumJellyfishEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SEA_URCHIN.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR,
                com.otterly76.ott.entity.custom.SeaUrchinEntity::checkSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );

        event.register(
                ModEntities.DRAGONFLY.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.DragonflyEntity::checkDragonflySpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.DUMBO_OCTOPUS.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.animal.WaterAnimal::checkSurfaceWaterAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.FERRET.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.JUMPING_SPIDER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.KOI_FISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.animal.WaterAnimal::checkSurfaceWaterAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.OTTER.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.OtterEntity::checkOtterSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.RED_PANDA.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.animal.Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SEA_BUNNY.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                net.minecraft.world.entity.animal.WaterAnimal::checkSurfaceWaterAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                com.otterly76.ott.block.entity.ModBlockEntities.WEATHERING_STATION.get(),
                (blockEntity, side) -> blockEntity.getInventory()
        );

        event.registerBlockEntity(
                Capabilities.FluidHandler.BLOCK,
                com.otterly76.ott.block.entity.ModBlockEntities.WEATHERING_STATION.get(),
                (blockEntity, side) -> blockEntity.getWaterTank()
        );

        event.registerBlockEntity(
                Capabilities.ItemHandler.BLOCK,
                com.otterly76.ott.block.entity.ModBlockEntities.ANVIL_BLOCK_ENTITY_TYPE.get(),
                (blockEntity, side) -> new net.neoforged.neoforge.items.wrapper.SidedInvWrapper(blockEntity, null)
        );

        event.registerItem(
                Capabilities.FluidHandler.ITEM,
                (stack, context) -> new FluidBucketWrapper(stack),
                ModItems.COPPER_BUCKET.get(),
                ModItems.COPPER_WATER_BUCKET.get(),
                ModItems.COPPER_LAVA_BUCKET.get()
        );
    }
}