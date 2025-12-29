package com.otterly76.ott.generation;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> lookupProvider,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, "ott", existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.addVanillaTags();

        TagKey<Block> DO_DEFAULT = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("domum_ornamentum", "default"));
        var doTag = this.tag(DO_DEFAULT);

        // 1. Add static lists and individual blocks
        ModBlocks.ALL_GRADIENT_BLOCKS.forEach(block -> doTag.add(block.get()));
        ModBlocks.LIMESTONE.forEach(block -> doTag.add(block.get()));
        ModBlocks.SEAGLASS.forEach(block -> doTag.add(block.get()));

        doTag.add(
                ModBlocks.HEDGE.get(),
                ModBlocks.PALE_MOSS_BLOCK.get(),
                ModBlocks.PALE_OAK_LOG.get(),
                ModBlocks.PALE_OAK_WOOD.get(),
                ModBlocks.PALE_OAK_PLANKS.get(),
                ModBlocks.STRIPPED_PALE_OAK_LOG.get(),
                ModBlocks.STRIPPED_PALE_OAK_WOOD.get(),
                ModBlocks.PALE_OAK_LEAVES.get(),
                ModBlocks.RESIN_BLOCK.get(),
                ModBlocks.RESIN_BRICKS.get(),
                ModBlocks.CHISELED_RESIN_BRICKS.get()
        );

        // 2. Add Map-based collections (Hedges)
        ModBlocks.PARTICLE_HEDGES.values().forEach(block -> doTag.add(block.get()));
        ModBlocks.CREEPING_HEDGES.values().forEach(block -> doTag.add(block.get()));

        // 3. Add everything relevant from the Wood Sets
        ModBlocks.WOOD_SETS.values().forEach(set -> {
            doTag.add(
                    set.log().get(),
                    set.wood().get(),
                    set.strippedLog().get(),
                    set.strippedWood().get(),
                    set.planks().get(),
                    set.leaves().get()
            );
        });

        this.tag(BlockTags.COMBINATION_STEP_SOUND_BLOCKS).add(ModBlocks.RESIN_CLUMP.get());
        this.tag(BlockTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.get());
        this.tag(BlockTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.get());
        this.tag(BlockTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.get());

        // Define your mod tag contents...
        this.tag(ModTags.Blocks.PALE_OAK_LOGS).add(
                ModBlocks.PALE_OAK_LOG.get(),
                ModBlocks.STRIPPED_PALE_OAK_LOG.get(),
                ModBlocks.PALE_OAK_WOOD.get(),
                ModBlocks.STRIPPED_PALE_OAK_WOOD.get()
        );

        // ...then hook it into vanilla behavior tags
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(ModTags.Blocks.PALE_OAK_LOGS);

        this.tag(BlockTags.LEAVES).add(ModBlocks.PALE_OAK_LEAVES.get());
        this.tag(BlockTags.SAPLINGS).add(ModBlocks.PALE_OAK_SAPLING.get());
        this.tag(BlockTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.get());

        // ott wood sets: tag them automatically
        ModBlocks.WOOD_SETS.values().forEach(set -> {
            this.tag(BlockTags.LOGS_THAT_BURN).add(
                    set.log().get(),
                    set.wood().get(),
                    set.strippedLog().get(),
                    set.strippedWood().get()
            );

            this.tag(BlockTags.PLANKS).add(set.planks().get());
            this.tag(BlockTags.LEAVES).add(set.leaves().get());

            this.tag(BlockTags.WOODEN_SLABS).add(set.slab().get());
            this.tag(BlockTags.WOODEN_STAIRS).add(set.stairs().get());
            this.tag(BlockTags.WOODEN_FENCES).add(set.fence().get());
            this.tag(BlockTags.FENCE_GATES).add(set.fenceGate().get());
            this.tag(BlockTags.WOODEN_DOORS).add(set.door().get());
            this.tag(BlockTags.WOODEN_TRAPDOORS).add(set.trapdoor().get());
            this.tag(BlockTags.WOODEN_BUTTONS).add(set.button().get());
            this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(set.pressurePlate().get());

            // Sign block tags
            this.tag(BlockTags.STANDING_SIGNS).add(set.sign().get());
            this.tag(BlockTags.WALL_SIGNS).add(set.wallSign().get());
            this.tag(BlockTags.CEILING_HANGING_SIGNS).add(set.hangingSign().get());
            this.tag(BlockTags.WALL_HANGING_SIGNS).add(set.wallHangingSign().get());

        });

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                ModBlocks.RESIN_BRICKS.get(),
                ModBlocks.CHISELED_RESIN_BRICKS.get(),
                ModBlocks.RESIN_BRICK_SLAB.get(),
                ModBlocks.RESIN_BLOCK.get(),
                ModBlocks.RESIN_BRICK_STAIRS.get(),
                ModBlocks.RESIN_BRICK_WALL.get()
        );
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(
                ModBlocks.CREAKING_HEART.get(),
                ModBlocks.FLIMSY_PROTECTIVE_LANTERN.get(),
                ModBlocks.PROTECTIVE_LANTERN.get(),
                ModBlocks.STURDY_PROTECTIVE_LANTERN.get());

        this.tag(BlockTags.MINEABLE_WITH_HOE).add(
                ModBlocks.PALE_MOSS_BLOCK.get(),
                ModBlocks.PALE_MOSS_CARPET.get(),
                ModBlocks.HEDGE.get());

        var shovelTag = this.tag(BlockTags.MINEABLE_WITH_SHOVEL);
        ModBlocks.getAllGradientConcretePowderBlocks().forEach(block -> shovelTag.add(block.get()));

        var pickaxeTag = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
        ModBlocks.getAllGradientConcreteBlocks().forEach(block -> pickaxeTag.add(block.get()));
        ModBlocks.getAllGradientTerracottaBlocks().forEach(block -> pickaxeTag.add(block.get()));

        var woolTag = this.tag(BlockTags.WOOL);
        ModBlocks.getAllGradientWoolBlocks().forEach(block -> woolTag.add(block.get()));

        var terracottaTag = this.tag(BlockTags.TERRACOTTA);
        ModBlocks.getAllGradientTerracottaBlocks().forEach(block -> terracottaTag.add(block.get()));

        var stainedGlassTag = this.tag(BlockTags.IMPERMEABLE);
        ModBlocks.getAllGradientStainedGlassBlocks().forEach(block -> stainedGlassTag.add(block.get()));

        this.tag(BlockTags.DIRT).add(ModBlocks.PALE_MOSS_BLOCK.get());
        this.tag(BlockTags.REPLACEABLE_BY_TREES).add(ModBlocks.PALE_MOSS_BLOCK.get());

        this.tag(BlockTags.FLOWERS).add(ModBlocks.CLOSED_EYEBLOSSOM.get(), ModBlocks.OPEN_EYEBLOSSOM.get());
        this.tag(BlockTags.SMALL_FLOWERS).add(ModBlocks.CLOSED_EYEBLOSSOM.get(), ModBlocks.OPEN_EYEBLOSSOM.get());

        this.tag(BlockTags.STANDING_SIGNS).add(ModBlocks.PALE_OAK_SIGN.get());
        this.tag(BlockTags.WALL_SIGNS).add(ModBlocks.PALE_OAK_WALL_SIGN.get());
        this.tag(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.PALE_OAK_HANGING_SIGN.get());
        this.tag(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.PALE_OAK_WALL_HANGING_SIGN.get());
    }

    protected void addVanillaTags() {
    }
}