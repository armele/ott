package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagProvider extends EntityTypeTagsProvider {

    public ModEntityTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ModTags.EntityTypes.SKELETON_MOBS).add(
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.WITHER_SKELETON,
                EntityType.BOGGED
        );

        this.tag(ModTags.EntityTypes.ACCEPTS_IRON_GOLEM_GIFT).add(EntityType.VILLAGER);
        this.tag(ModTags.EntityTypes.CANDIDATE_FOR_IRON_GOLEM_GIFT).add(EntityType.VILLAGER);
        this.tag(ModTags.EntityTypes.OTT_ENTITIES).add(
                ModEntities.ALLIGATOR.get(),
                ModEntities.BASS.get(),
                ModEntities.BLACK_BEAR.get(),
                ModEntities.BROWN_BEAR.get(),
                ModEntities.BLUEJAY.get(),
                ModEntities.BUTTERFLY.get(),
                ModEntities.CANARY.get(),
                ModEntities.CARDINAL.get(),
                ModEntities.CATERPILLAR.get(),
                ModEntities.CATFISH.get(),
                ModEntities.DEER.get(),
                ModEntities.DUCK.get(),
                ModEntities.ELEPHANT.get(),
                ModEntities.FIREFLY.get(),
                ModEntities.FINCH.get(),
                ModEntities.GIRAFFE.get(),
                ModEntities.HIPPO.get(),
                ModEntities.LION.get(),
                ModEntities.LIZARD.get(),
                ModEntities.RHINO.get(),
                ModEntities.ROBIN.get(),
                ModEntities.SNAIL.get(),
                ModEntities.SPARROW.get(),
                ModEntities.TORTOISE.get(),
                ModEntities.VULTURE.get(),
                ModEntities.ZEBRA.get(),
                ModEntities.MOOSE.get(),
                ModEntities.MAMMOTH.get(),
                ModEntities.MYCELIUM_MAMMOTH.get(),
                ModEntities.CAPYBARA.get(),
                ModEntities.HEDGEHOG.get(),
                ModEntities.JELLYFISH.get(),
                ModEntities.JELLYFISH_2.get(),
                ModEntities.JELLYFISH_3.get(),
                ModEntities.KIWI.get(),
                ModEntities.PENGUIN.get(),
                ModEntities.SEA_URCHIN.get(),
                ModEntities.SEAL.get(),
                ModEntities.SEAHORSE_1.get(),
                ModEntities.SHRIMP_1.get(),
                ModEntities.STARFISH_1.get(),
                ModEntities.DRAGONFLY.get(),
                ModEntities.DUMBO_OCTOPUS.get(),
                ModEntities.FERRET.get(),
                ModEntities.JUMPING_SPIDER.get(),
                ModEntities.KOI_FISH.get(),
                ModEntities.OTTER.get(),
                ModEntities.RED_PANDA.get(),
                ModEntities.SEA_BUNNY.get(),
                ModEntities.BEAVER.get(),
                ModEntities.CHUPACABRA.get(),
                ModEntities.COUGAR.get(),
                ModEntities.COYOTE.get(),
                ModEntities.HOWLER.get(),
                ModEntities.MARMOT.get(),
                ModEntities.MOUSE.get(),
                ModEntities.PIT_VIPER.get(),
                ModEntities.RATTLESNAKE.get(),
                ModEntities.RINGTAIL.get(),
                ModEntities.SASQUATCH.get(),
                ModEntities.SKINWALKER.get(),
                ModEntities.SNAKE.get(),
                ModEntities.SQUONK.get(),
                ModEntities.TURKEY.get(),
                ModEntities.WECHUGE.get(),
                ModEntities.WENDIGO.get(),
                ModEntities.WOLVERINE.get(),
                ModEntities.CICHLID.get(),
                ModEntities.LEOPARD_CAT.get(),
                ModEntities.ECHIDNA.get(),
                ModEntities.GUITARFISH.get(),
                ModEntities.BONNETHEAD_SHARK.get(),
                ModEntities.BURROWING_OWL.get(),
                ModEntities.BUSHDOG.get(),
                ModEntities.QUAIL.get(),
                ModEntities.CANDYCANE_SNAIL.get(),
                ModEntities.FIRE_SALAMANDER.get(),
                ModEntities.RIVER_TURTLE.get(),
                ModEntities.GOBLIN_SHARK.get(),
                ModEntities.GUINEA_FOWL.get(),
                ModEntities.IMPALA.get(),
                ModEntities.MANTA_RAY.get(),
                ModEntities.STORK.get(),
                ModEntities.MOLE.get(),
                ModEntities.TREE_KANGAROO.get(),
                ModEntities.STINGRAY.get(),
                ModEntities.PALLAS_CAT.get(),
                ModEntities.PINK_LAND_IGUANA.get(),
                ModEntities.PSYCHO_JELLY.get(),
                ModEntities.SPOONBILL.get(),
                ModEntities.GIANT_SOFTSHELL_TURTLE.get()
        );

        this.tag(ModTags.EntityTypes.SMART_ANIMALS).add(
                EntityType.PIG, EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN,
                ModEntities.CAPYBARA.get(),
                ModEntities.HEDGEHOG.get(),
                ModEntities.JELLYFISH.get(),
                ModEntities.JELLYFISH_2.get(),
                ModEntities.JELLYFISH_3.get(),
                ModEntities.KIWI.get(),
                ModEntities.PENGUIN.get(),
                ModEntities.SEA_URCHIN.get(),
                ModEntities.SEAL.get(),
                ModEntities.SEAHORSE_1.get(),
                ModEntities.SHRIMP_1.get(),
                ModEntities.STARFISH_1.get(),
                ModEntities.DRAGONFLY.get(),
                ModEntities.DUMBO_OCTOPUS.get(),
                ModEntities.FERRET.get(),
                ModEntities.JUMPING_SPIDER.get(),
                ModEntities.KOI_FISH.get(),
                ModEntities.OTTER.get(),
                ModEntities.RED_PANDA.get(),
                ModEntities.SEA_BUNNY.get(),
                ModEntities.BEAVER.get(),
                ModEntities.COUGAR.get(),
                ModEntities.COYOTE.get(),
                ModEntities.MARMOT.get(),
                ModEntities.MOUSE.get(),
                ModEntities.PIT_VIPER.get(),
                ModEntities.RATTLESNAKE.get(),
                ModEntities.RINGTAIL.get(),
                ModEntities.SASQUATCH.get(),
                ModEntities.SNAKE.get(),
                ModEntities.TURKEY.get(),
                ModEntities.WOLVERINE.get()
        );

        this.tag(ModTags.EntityTypes.SAFE_EGG_WALKERS).add(
                ModEntities.CAPYBARA.get(),
                ModEntities.HEDGEHOG.get(),
                ModEntities.JELLYFISH.get(),
                ModEntities.JELLYFISH_2.get(),
                ModEntities.JELLYFISH_3.get(),
                ModEntities.KIWI.get(),
                ModEntities.PENGUIN.get(),
                ModEntities.SEA_URCHIN.get(),
                ModEntities.SEAL.get(),
                ModEntities.SEAHORSE_1.get(),
                ModEntities.SHRIMP_1.get(),
                ModEntities.STARFISH_1.get(),
                ModEntities.DRAGONFLY.get(),
                ModEntities.DUMBO_OCTOPUS.get(),
                ModEntities.FERRET.get(),
                ModEntities.JUMPING_SPIDER.get(),
                ModEntities.KOI_FISH.get(),
                ModEntities.OTTER.get(),
                ModEntities.RED_PANDA.get(),
                ModEntities.SEA_BUNNY.get()
        );

        this.tag(net.minecraft.tags.EntityTypeTags.CAN_BREATHE_UNDER_WATER).add(
                ModEntities.DUMBO_OCTOPUS.get(),
                ModEntities.KOI_FISH.get(),
                ModEntities.OTTER.get(),
                ModEntities.SEA_BUNNY.get(),
                ModEntities.SHRIMP_1.get(),
                ModEntities.SEAHORSE_1.get(),
                ModEntities.JELLYFISH.get(),
                ModEntities.JELLYFISH_2.get(),
                ModEntities.JELLYFISH_3.get(),
                ModEntities.STARFISH_1.get(),
                ModEntities.SEA_URCHIN.get(),
                ModEntities.MAN_O_WAR.get(),
                ModEntities.STINGRAY.get(),
                ModEntities.SUNFISH.get(),
                ModEntities.KRILL.get(),
                ModEntities.ANGELFISH.get(),
                ModEntities.BARRELEYE.get(),
                ModEntities.FLOUNDER.get(),
                ModEntities.CICHLID.get(),
                ModEntities.GUITARFISH.get(),
                ModEntities.BONNETHEAD_SHARK.get(),
                ModEntities.GOBLIN_SHARK.get(),
                ModEntities.MANTA_RAY.get(),
                ModEntities.PSYCHO_JELLY.get(),
                ModEntities.RIVER_TURTLE.get(),
                ModEntities.GIANT_SOFTSHELL_TURTLE.get()
        );

        this.tag(net.minecraft.tags.EntityTypeTags.UNDEAD).add(
                ModEntities.SKINWALKER.get(),
                ModEntities.WENDIGO.get(),
                ModEntities.WECHUGE.get()
        );

        this.tag(ModTags.EntityTypes.FROG_FOOD).add(
                ModEntities.DRAGONFLY.get(),
                ModEntities.BUTTERFLY.get()
        );

        this.tag(ModTags.EntityTypes.CATFISH_HOSTILES).add(ModEntities.SHRIMP_1.get());

        this.tag(ModTags.EntityTypes.ALLIGATOR_HOSTILES).add(
                ModEntities.CAPYBARA.get(),
                ModEntities.SEAL.get()
        );

        this.tag(ModTags.EntityTypes.LION_HOSTILES).add(
                ModEntities.CAPYBARA.get(),
                ModEntities.SEAL.get()
        );

        this.tag(ModTags.EntityTypes.BEAR_HOSTILES).add(
                ModEntities.CAPYBARA.get(),
                ModEntities.SEAL.get()
        );

        this.tag(ModTags.EntityTypes.VULTURE_HOSTILES).add(
                ModEntities.HEDGEHOG.get(),
                ModEntities.SHRIMP_1.get(),
                ModEntities.STARFISH_1.get()
        );

        this.tag(ModTags.EntityTypes.DEER_PREDATORS).add(
                ModEntities.BLACK_BEAR.get(),
                ModEntities.BROWN_BEAR.get(),
                ModEntities.LION.get(),
                ModEntities.ALLIGATOR.get()
        );

        this.tag(ModTags.EntityTypes.SNAKE_HOSTILES).add(
                ModEntities.BLUEJAY.get(),
                ModEntities.CANARY.get(),
                ModEntities.CARDINAL.get(),
                ModEntities.FINCH.get(),
                ModEntities.ROBIN.get(),
                ModEntities.SPARROW.get()
        );

        this.tag(ModTags.EntityTypes.BOAR_HOSTILES).add(
                ModEntities.BLACK_BEAR.get(),
                ModEntities.BROWN_BEAR.get(),
                ModEntities.LION.get()
        );

        this.tag(ModTags.EntityTypes.HIPPO_HOSTILES).add(
                ModEntities.LION.get(),
                ModEntities.ALLIGATOR.get()
        );
        this.tag(ModTags.EntityTypes.FISH).add(
                ModEntities.BASS.get(),
                ModEntities.CATFISH.get(),
                ModEntities.KOI_FISH.get(),
                ModEntities.SHRIMP_1.get(),
                ModEntities.SEAHORSE_1.get(),
                ModEntities.STINGRAY.get(),
                ModEntities.SUNFISH.get(),
                ModEntities.KRILL.get(),
                ModEntities.ANGELFISH.get(),
                ModEntities.BARRELEYE.get(),
                ModEntities.FLOUNDER.get(),
                ModEntities.CICHLID.get(),
                ModEntities.GUITARFISH.get(),
                ModEntities.BONNETHEAD_SHARK.get(),
                ModEntities.GOBLIN_SHARK.get(),
                ModEntities.MANTA_RAY.get()
        );

        this.tag(ModTags.EntityTypes.ELEPHANT_HOSTILES).add(
                ModEntities.LION.get()
        );
    }
}