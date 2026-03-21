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
                ModEntities.SHRIMP.get(),
                ModEntities.STARFISH_1.get()
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
                ModEntities.SHRIMP.get(),
                ModEntities.STARFISH_1.get()
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
                ModEntities.SHRIMP.get(),
                ModEntities.STARFISH_1.get()
        );

        this.tag(ModTags.EntityTypes.CATFISH_HOSTILES).add(ModEntities.SHRIMP.get());

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
                ModEntities.SHRIMP.get(),
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

        this.tag(ModTags.EntityTypes.ELEPHANT_HOSTILES).add(
                ModEntities.LION.get()
        );
    }
}
