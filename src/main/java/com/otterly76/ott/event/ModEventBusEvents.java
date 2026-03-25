package com.otterly76.ott.event;

import com.otterly76.ott.entity.Creaking;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.client.model.CreakingModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.entity.custom.HappyGhast;
import com.otterly76.ott.entity.custom.Shrimp1Entity;
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
        event.put(ModEntities.STINGRAY.get(), com.otterly76.ott.entity.custom.Stingray.setAttributes().build());
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
        event.put(ModEntities.JELLYFISH.get(), com.otterly76.ott.entity.custom.JellyfishEntity.createAttributes().build());
        event.put(ModEntities.SEAHORSE_1.get(), com.otterly76.ott.entity.custom.Seahorse1Entity.createAttributes().build());
        event.put(ModEntities.SHRIMP_1.get(), Shrimp1Entity.createAttributes().build());
        event.put(ModEntities.STARFISH_1.get(), com.otterly76.ott.entity.custom.Starfish1Entity.createAttributes().build());
        event.put(ModEntities.JELLYFISH_2.get(), com.otterly76.ott.entity.custom.Jellyfish2Entity.createAttributes().build());
        event.put(ModEntities.JELLYFISH_3.get(), com.otterly76.ott.entity.custom.Jellyfish3Entity.createAttributes().build());

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
        event.put(ModEntities.SAND_HERMIT.get(), com.otterly76.ott.entity.custom.SandHermit.createAttributes().build());
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
                ModEntities.STINGRAY.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Stingray::canSpawn,
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
                ModEntities.JELLYFISH.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.JellyfishEntity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SEAHORSE_1.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Seahorse1Entity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.SHRIMP_1.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Shrimp1Entity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.STARFISH_1.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Starfish1Entity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.JELLYFISH_2.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Jellyfish2Entity::canSpawn,
                RegisterSpawnPlacementsEvent.Operation.OR
        );
        event.register(
                ModEntities.JELLYFISH_3.get(),
                SpawnPlacementTypes.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                com.otterly76.ott.entity.custom.Jellyfish3Entity::canSpawn,
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