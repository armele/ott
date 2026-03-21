package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
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
        this.tag(ModTags.EntityTypes.SMART_ANIMALS).add(EntityType.PIG, EntityType.COW, EntityType.SHEEP, EntityType.CHICKEN);
    }
}
