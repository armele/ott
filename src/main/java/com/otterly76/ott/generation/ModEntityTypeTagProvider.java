package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.entity.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.TagKey;
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
        TagKey<EntityType<?>> skeletonMobs = TagKey.create(net.minecraft.core.registries.Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "skeleton_mobs"));
        
        this.tag(skeletonMobs).add(
                EntityType.SKELETON,
                EntityType.STRAY,
                EntityType.WITHER_SKELETON,
                EntityType.BOGGED,
                ModEntities.TINY_SKELETON.get(),
                ModEntities.TINY_STRAY.get(),
                ModEntities.TINY_WITHER_SKELETON.get(),
                ModEntities.TINY_BOGGED.get()
        );

        this.tag(EntityTypeTags.SKELETONS).add(
                ModEntities.TINY_SKELETON.get(),
                ModEntities.TINY_STRAY.get(),
                ModEntities.TINY_WITHER_SKELETON.get(),
                ModEntities.TINY_BOGGED.get()
        );
    }
}
