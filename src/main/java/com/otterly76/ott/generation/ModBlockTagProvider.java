package com.otterly76.ott.generation;

import com.otterly76.ott.Constants;
import com.otterly76.ott.block.*;
import com.otterly76.ott.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {

    public ModBlockTagProvider(PackOutput output,
                               CompletableFuture<HolderLookup.Provider> lookupProvider,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Constants.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // --- 1. DEFINE ALL TAG KEYS (The "Identity") ---
        TagKey<Block> ottConcreteKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "concrete"));
        TagKey<Block> ottConcretePowderKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "concrete_powder"));
        TagKey<Block> ottWoolKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "wool"));
        TagKey<Block> ottStainedGlassKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "stained_glass"));
        TagKey<Block> ottTerracottaKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "terracotta"));

        TagKey<Block> structurizeWeakKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("structurize", "weak_solid_blocks"));

        TagKey<Block> cConcretesKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "concretes"));
        TagKey<Block> cConcretePowdersKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "concrete_powders"));
        TagKey<Block> cWoolKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "wool"));
        TagKey<Block> cTerracottaKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "terracotta"));
        TagKey<Block> cDyedKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "dyed"));
        TagKey<Block> cGlassKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "glass"));
        TagKey<Block> cGlassBlocksKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "glass_blocks"));
        TagKey<Block> cGlassBlocksCheapKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "glass_blocks_cheap"));

        TagKey<Block> paleOakLogsKey = ModTags.Blocks.PALE_OAK_LOGS;

        TagKey<Block> ottHedgesKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "hedge"));

        TagKey<Block> doDefaultKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("domum_ornamentum", "default"));
        TagKey<Block> doConcreteKey = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("domum_ornamentum", "concrete"));


        // --- 2. INITIALIZE BUILDERS (The "Appenders") ---
        var ottConcrete = this.tag(ottConcreteKey);
        var ottConcretePowder = this.tag(ottConcretePowderKey);
        var ottWool = this.tag(ottWoolKey);
        var ottStainedGlass = this.tag(ottStainedGlassKey);
        var ottTerracotta = this.tag(ottTerracottaKey);

        var mcConcrete = this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("concrete")));
        var mcConcretePowder = this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("concrete_powder")));
        var mcStainedGlass = this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.withDefaultNamespace("stained_glass")));

        var pickaxeTag = this.tag(BlockTags.MINEABLE_WITH_PICKAXE);
        var shovelTag = this.tag(BlockTags.MINEABLE_WITH_SHOVEL);
        var axeTag = this.tag(BlockTags.MINEABLE_WITH_AXE);
        var hoeTag = this.tag(BlockTags.MINEABLE_WITH_HOE);
        var shearsTag = this.tag(BlockTags.create(ResourceLocation.withDefaultNamespace("mineable/shears")));

        this.tag(ModTags.Blocks.PATHS).add(Blocks.DIRT_PATH).addTag(com.minecolonies.api.items.ModTags.pathingBlocks);
        this.tag(ModTags.Blocks.STONE)
                .addTag(BlockTags.BASE_STONE_OVERWORLD)
                .addTag(BlockTags.BASE_STONE_NETHER)
                .add(Blocks.STONE, Blocks.ANDESITE, Blocks.DIORITE, Blocks.GRANITE, Blocks.DEEPSLATE, Blocks.TUFF);

        // --- 3. REGISTRY LOOP (Populate ott: and behavior tags) ---
        ModBlocks.BLOCKS.getEntries().forEach(deferredBlock -> {
            Block block = deferredBlock.value();
            this.tag(doDefaultKey).add(block); // Everything goes to DO

            switch (block) {
                case GradientConcreteBlock concrete -> {
                    ottConcrete.add(concrete);
                    mcConcrete.add(concrete);
                    pickaxeTag.add(concrete);
                }
                case GradientConcretePowderBlock powder -> {
                    ottConcretePowder.add(powder);
                    mcConcretePowder.add(powder);
                    shovelTag.add(powder);
                }
                case GradientWoolBlock wool -> {
                    ottWool.add(wool);
                    this.tag(BlockTags.WOOL).add(wool);
                }
                case GradientTerracottaBlock terracotta -> {
                    ottTerracotta.add(terracotta);
                    this.tag(BlockTags.TERRACOTTA).add(terracotta);
                    pickaxeTag.add(terracotta);
                }
                case GradientStainedGlassBlock glass -> {
                    ottStainedGlass.add(glass);
                    mcStainedGlass.add(glass);
                    this.tag(BlockTags.IMPERMEABLE).add(glass);
                }
                default -> { }
            }
        });

        // --- 4. HIERARCHY (Nesting our tags into Common and MineColonies) ---

        // Linking to DO categories
        this.tag(doConcreteKey).addTag(ottConcreteKey);

        // Linking to Common (c:)
        this.tag(cConcretesKey).addTag(ottConcreteKey);
        this.tag(cConcretePowdersKey).addTag(ottConcretePowderKey);
        this.tag(cWoolKey).addTag(ottWoolKey);
        this.tag(cTerracottaKey).addTag(ottTerracottaKey);
        this.tag(cGlassKey).addTag(ottStainedGlassKey);
        this.tag(cGlassBlocksKey).addTag(ottStainedGlassKey);
        this.tag(cGlassBlocksCheapKey).addTag(ottStainedGlassKey);
        this.tag(cDyedKey).addTag(ottConcreteKey).addTag(ottConcretePowderKey).addTag(ottWoolKey).addTag(ottStainedGlassKey).addTag(ottTerracottaKey);

        // Linking to MineColonies
        this.tag(com.minecolonies.api.items.ModTags.tier1blocks).addTag(ottWoolKey).addTag(ottTerracottaKey);
        this.tag(com.minecolonies.api.items.ModTags.tier2blocks).addTag(ottConcreteKey).addTag(ottConcretePowderKey).addTag(ottWoolKey).addTag(ottStainedGlassKey).addTag(ottTerracottaKey);

        this.tag(structurizeWeakKey).addTag(ottConcretePowderKey);

        // Linking to Common Wood
        this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "planks"))).addTag(BlockTags.PLANKS);
        this.tag(TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath("c", "logs"))).addTag(BlockTags.LOGS);

        // --- 5. STATIC & INDIVIDUAL ADDITIONS ---
        ModBlocks.LIMESTONE.forEach(d -> pickaxeTag.add(d.value()));
        ModBlocks.SEAGLASS.forEach(d -> this.tag(BlockTags.IMPERMEABLE).add(d.value()));

        var ottHedges = this.tag(ottHedgesKey);
        ottHedges.add(ModBlocks.HEDGE.value());
        ModBlocks.PARTICLE_HEDGES.values().forEach(h -> ottHedges.add(h.value()));
        ModBlocks.CREEPING_HEDGES.values().forEach(h -> ottHedges.add(h.value()));

        axeTag.add(ModBlocks.CREAKING_HEART.value(), ModBlocks.FLIMSY_PROTECTIVE_LANTERN.value(), ModBlocks.PROTECTIVE_LANTERN.value(), ModBlocks.STURDY_PROTECTIVE_LANTERN.value());
        hoeTag.add(ModBlocks.PALE_MOSS_BLOCK.value(), ModBlocks.PALE_MOSS_CARPET.value(), ModBlocks.HEDGE.value());
        hoeTag.add(ModBlocks.PALE_OAK_LEAVES.value());
        pickaxeTag.add(ModBlocks.RESIN_BRICKS.value(), ModBlocks.CHISELED_RESIN_BRICKS.value(), ModBlocks.RESIN_BRICK_SLAB.value(), ModBlocks.RESIN_BLOCK.value(), ModBlocks.RESIN_BRICK_STAIRS.value(), ModBlocks.RESIN_BRICK_WALL.value());

        shearsTag.add(ModBlocks.PALE_OAK_LEAVES.value(), ModBlocks.PALE_HANGING_MOSS.value(), ModBlocks.PALE_MOSS_BLOCK.value(), ModBlocks.PALE_MOSS_CARPET.value(), ModBlocks.CLOSED_EYEBLOSSOM.value(), ModBlocks.OPEN_EYEBLOSSOM.value());

        this.tag(BlockTags.create(ResourceLocation.withDefaultNamespace("combination_step_sound_blocks"))).add(ModBlocks.RESIN_CLUMP.value());
        this.tag(BlockTags.FENCE_GATES).add(ModBlocks.PALE_OAK_FENCE_GATE.value());
        this.tag(BlockTags.LOGS_THAT_BURN).addTag(paleOakLogsKey);
        this.tag(BlockTags.SLABS).add(ModBlocks.RESIN_BRICK_SLAB.value());
        this.tag(BlockTags.STAIRS).add(ModBlocks.RESIN_BRICK_STAIRS.value());
        this.tag(BlockTags.WALLS).add(ModBlocks.RESIN_BRICK_WALL.value());

        this.tag(BlockTags.STANDING_SIGNS).add(ModBlocks.PALE_OAK_SIGN.value());
        this.tag(BlockTags.WALL_SIGNS).add(ModBlocks.PALE_OAK_WALL_SIGN.value());
        this.tag(BlockTags.CEILING_HANGING_SIGNS).add(ModBlocks.PALE_OAK_HANGING_SIGN.value());
        this.tag(BlockTags.WALL_HANGING_SIGNS).add(ModBlocks.PALE_OAK_WALL_HANGING_SIGN.value());

        this.tag(BlockTags.WOODEN_BUTTONS).add(ModBlocks.PALE_OAK_BUTTON.value());
        this.tag(BlockTags.WOODEN_DOORS).add(ModBlocks.PALE_OAK_DOOR.value());
        this.tag(BlockTags.WOODEN_FENCES).add(ModBlocks.PALE_OAK_FENCE.value());
        this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(ModBlocks.PALE_OAK_PRESSURE_PLATE.value());
        this.tag(BlockTags.WOODEN_SLABS).add(ModBlocks.PALE_OAK_SLAB.value());
        this.tag(BlockTags.WOODEN_STAIRS).add(ModBlocks.PALE_OAK_STAIRS.value());
        this.tag(BlockTags.WOODEN_TRAPDOORS).add(ModBlocks.PALE_OAK_TRAPDOOR.value());

        ModBlocks.WOOD_SETS.values().forEach(set -> {
            this.tag(doDefaultKey).add(set.log().value(), set.wood().value(), set.strippedLog().value(), set.strippedWood().value(), set.planks().value(), set.leaves().value());
            this.tag(BlockTags.LOGS_THAT_BURN).add(set.log().value(), set.wood().value(), set.strippedLog().value(), set.strippedWood().value());
            this.tag(BlockTags.PLANKS).add(set.planks().value());
            this.tag(BlockTags.LEAVES).add(set.leaves().value());
            this.tag(BlockTags.MINEABLE_WITH_HOE).add(set.leaves().value());
            shearsTag.add(set.leaves().value());
            this.tag(BlockTags.WOODEN_SLABS).add(set.slab().value());
            this.tag(BlockTags.WOODEN_STAIRS).add(set.stairs().value());
            this.tag(BlockTags.WOODEN_FENCES).add(set.fence().value());
            this.tag(BlockTags.FENCE_GATES).add(set.fenceGate().value());
            this.tag(BlockTags.WOODEN_DOORS).add(set.door().value());
            this.tag(BlockTags.WOODEN_TRAPDOORS).add(set.trapdoor().value());
            this.tag(BlockTags.WOODEN_BUTTONS).add(set.button().value());
            this.tag(BlockTags.WOODEN_PRESSURE_PLATES).add(set.pressurePlate().value());
            this.tag(BlockTags.STANDING_SIGNS).add(set.sign().value());
            this.tag(BlockTags.WALL_SIGNS).add(set.wallSign().value());
            this.tag(BlockTags.CEILING_HANGING_SIGNS).add(set.hangingSign().value());
            this.tag(BlockTags.WALL_HANGING_SIGNS).add(set.wallHangingSign().value());
        });

        // --- 6. VANILLA OVERRIDES ---
        this.tag(BlockTags.LEAVES).add(ModBlocks.PALE_OAK_LEAVES.value());
        this.tag(BlockTags.PLANKS).add(ModBlocks.PALE_OAK_PLANKS.value());
        this.tag(BlockTags.DIRT).add(ModBlocks.PALE_MOSS_BLOCK.value());
        this.tag(BlockTags.REPLACEABLE_BY_TREES).add(ModBlocks.PALE_MOSS_BLOCK.value());
        this.tag(BlockTags.FLOWERS).add(ModBlocks.CLOSED_EYEBLOSSOM.value(), ModBlocks.OPEN_EYEBLOSSOM.value());
        this.tag(BlockTags.SMALL_FLOWERS).add(ModBlocks.CLOSED_EYEBLOSSOM.value(), ModBlocks.OPEN_EYEBLOSSOM.value());

        this.tag(paleOakLogsKey)
                .add(ModBlocks.PALE_OAK_LOG.value(), ModBlocks.STRIPPED_PALE_OAK_LOG.value(), ModBlocks.PALE_OAK_WOOD.value(), ModBlocks.STRIPPED_PALE_OAK_WOOD.value());
    }
}