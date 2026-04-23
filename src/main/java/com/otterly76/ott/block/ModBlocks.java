package com.otterly76.ott.block;

import com.otterly76.ott.entity.custom.Butterfly;
import com.otterly76.ott.block.custom.*;
import com.otterly76.ott.color.ModPatterns;
import com.otterly76.ott.particle.ModParticle;
import net.minecraft.world.level.biome.Biome;
import com.otterly76.ott.crop.ThornyHedgeSprouts;
import com.otterly76.ott.hedge.ModHedgeVariants;
import com.otterly76.ott.sound.ModSounds;
import com.otterly76.ott.util.block.BlockSetTypeVariant;
import com.otterly76.ott.util.block.WoodTypeVariant;
import com.otterly76.ott.util.block.ModSkullType;
import net.minecraft.world.level.block.grower.TreeGrower;
import com.otterly76.ott.worldgen.feature.TheGardenAwakensFeatures;
import com.otterly76.ott.wood.ModWoodSets;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.otterly76.ott.Constants.MOD_ID;

@SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "SameReturnValue"})
public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Blocks MINECRAFT_BLOCKS = DeferredRegister.createBlocks("minecraft");
    public static final DeferredRegister.Items MINECRAFT_ITEMS = DeferredRegister.createItems("minecraft");

    public static final List<DeferredBlock<? extends IGradientBlock>> ALL_GRADIENT_BLOCKS = new ArrayList<>();

    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_CONCRETE_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_TERRACOTTA_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_WOOL_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_STAINED_GLASS_BLOCKS = new ArrayList<>();
    private static final List<DeferredBlock<? extends IGradientBlock>> ALL_CONCRETE_POWDER_BLOCKS = new ArrayList<>();

    public static final List<DeferredBlock<? extends Block>> SEAGLASS = new ArrayList<>();

    public static final List<DeferredBlock<? extends Block>> TESTBLOCK = new ArrayList<>();
    
    public static final Map<String, DeferredBlock<ElevatorBlock>> ELEVATORS = new LinkedHashMap<>();
    public static final Map<String, DeferredBlock<FutonBlock>>   FUTONS    = new LinkedHashMap<>();


    private static <T extends Block> DeferredBlock<T> register(String name, java.util.function.Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    private static <T extends Block> DeferredBlock<T> registerTestblock(String name, java.util.function.Supplier<T> block) {
        DeferredBlock<T> ret = register(name, block);
        TESTBLOCK.add(ret);
        return ret;
    }

    public static final DeferredBlock<Block> TESTBLOCK_00 = registerTestblock("testblock_00", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_01 = registerTestblock("testblock_01", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_02 = registerTestblock("testblock_02", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_03 = registerTestblock("testblock_03", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_10 = registerTestblock("testblock_10", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_11 = registerTestblock("testblock_11", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_12 = registerTestblock("testblock_12", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_13 = registerTestblock("testblock_13", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_20 = registerTestblock("testblock_20", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_21 = registerTestblock("testblock_21", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_22 = registerTestblock("testblock_22", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_23 = registerTestblock("testblock_23", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_30 = registerTestblock("testblock_30", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_31 = registerTestblock("testblock_31", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_32 = registerTestblock("testblock_32", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> TESTBLOCK_33 = registerTestblock("testblock_33", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    public static final DeferredBlock<ChrysalisBlock> CHRYSALIS = register("chrysalis", () -> new ChrysalisBlock(Block.Properties.of().mapColor(MapColor.PLANT).strength(0.2F).sound(SoundType.GRASS).noOcclusion().randomTicks()));


    private static <T extends Block> DeferredBlock<T> registerSeaglass(String name, java.util.function.Supplier<T> block) {
        DeferredBlock<T> ret = register(name, block);
        SEAGLASS.add(ret);
        return ret;
    }

    public static final DeferredBlock<Block> ETHEREAL1_BUBBLES_SEAGLASS = registerSeaglass("ethereal1_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_SEAGLASS = registerSeaglass("ethereal1_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_SMOOTH_SEAGLASS = registerSeaglass("ethereal1_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL1_WAVES_SEAGLASS = registerSeaglass("ethereal1_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_BUBBLES_SEAGLASS = registerSeaglass("ethereal2_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_SEAGLASS = registerSeaglass("ethereal2_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_SMOOTH_SEAGLASS = registerSeaglass("ethereal2_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL2_WAVES_SEAGLASS = registerSeaglass("ethereal2_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_BUBBLES_SEAGLASS = registerSeaglass("ethereal3_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_SEAGLASS = registerSeaglass("ethereal3_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_SMOOTH_SEAGLASS = registerSeaglass("ethereal3_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL3_WAVES_SEAGLASS = registerSeaglass("ethereal3_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_BUBBLES_SEAGLASS = registerSeaglass("ethereal4_bubbles_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_SEAGLASS = registerSeaglass("ethereal4_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_SMOOTH_SEAGLASS = registerSeaglass("ethereal4_smooth_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));
    public static final DeferredBlock<Block> ETHEREAL4_WAVES_SEAGLASS = registerSeaglass("ethereal4_waves_seaglass", () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()));

    public static final DeferredBlock<Block> SALT_BLOCK = register("salt_block", () -> new Block(Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
    public static final DeferredBlock<Block> POLISHED_SALT_BLOCK = register("polished_salt_block", () -> new Block(Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.SNARE).strength(0.5F).sound(SoundType.SAND)));
    public static final DeferredBlock<Block> SALT_LAMP = register("salt_lamp", () -> new SaltLampBlock(Properties.of().mapColor(MapColor.SNOW).instrument(NoteBlockInstrument.IRON_XYLOPHONE).strength(0.3F).sound(SoundType.GLASS).lightLevel(state -> state.getValue(SaltLampBlock.LIT) ? 15 : 0).noOcclusion()));
    public static final DeferredBlock<Block> SALT_DUST = register("salt_dust", () -> new SaltPlacedBlock(Properties.of().mapColor(MapColor.SNOW).noCollission().instabreak().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> OAK_NEST = register("oak_nest", () -> new OakNestBlock(Properties.of().mapColor(MapColor.WOOD).strength(0.5F).sound(SoundType.WOOD).noOcclusion()));

    public static final DeferredBlock<BeehiveBlock> ACACIA_BEEHIVE   = register("acacia_beehive",   () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> BAMBOO_BEEHIVE   = register("bamboo_beehive",   () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> BIRCH_BEEHIVE    = register("birch_beehive",    () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> CHERRY_BEEHIVE   = register("cherry_beehive",   () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> CRIMSON_BEEHIVE  = register("crimson_beehive",  () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> DARK_OAK_BEEHIVE = register("dark_oak_beehive", () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> JUNGLE_BEEHIVE   = register("jungle_beehive",   () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> MANGROVE_BEEHIVE = register("mangrove_beehive", () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> PALE_OAK_BEEHIVE = register("pale_oak_beehive", () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> SPRUCE_BEEHIVE   = register("spruce_beehive",   () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> STARLIGHT_BEEHIVE = register("starlight_beehive", () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));
    public static final DeferredBlock<BeehiveBlock> WARPED_BEEHIVE   = register("warped_beehive",   () -> new BeehiveBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));

    public static final DeferredBlock<AlligatorEggBlock> ALLIGATOR_EGG = register("alligator_egg", () -> new AlligatorEggBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.5F).sound(SoundType.METAL).noOcclusion().randomTicks()));
    public static final DeferredBlock<TortoiseEggBlock> TORTOISE_EGG = register("tortoise_egg", () -> new TortoiseEggBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.5F).sound(SoundType.METAL).noOcclusion().randomTicks()));
    public static final DeferredBlock<SnailEggBlock> SNAIL_EGG = register("snail_egg", () -> new SnailEggBlock(BlockBehaviour.Properties.of().mapColor(MapColor.GRASS).strength(0.0F).sound(SoundType.FROGSPAWN).noCollission().noOcclusion()));
    public static final DeferredBlock<Block> REFINED_GLOWSTONE = register("refined_glowstone", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLOWSTONE)));
    public static final DeferredBlock<Block> GLOW_GOOP = register("glow_goop", () -> new GlowGoopBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).noCollission().noOcclusion().instabreak().lightLevel(GlowGoopBlock.LIGHT_EMISSION).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<com.otterly76.ott.block.custom.SilkCocoonBlock> SILK_COCOON = register("silk_cocoon", () -> new com.otterly76.ott.block.custom.SilkCocoonBlock(BlockBehaviour.Properties.of().instabreak().sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY)));


    public static final DeferredBlock<SaplingBlock> PALE_OAK_SAPLING = registerBackportedBlock("pale_oak_sapling", () -> new SaplingBlock(new TreeGrower("pale_oak", Optional.of(TheGardenAwakensFeatures.PALE_OAK_BONEMEAL), Optional.empty(), Optional.empty()), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SAPLING).mapColor(MapColor.COLOR_LIGHT_GRAY).noCollission().randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)), false);
    public static final DeferredBlock<FlowerPotBlock> POTTED_PALE_OAK_SAPLING = registerBackportedBlock("potted_pale_oak_sapling", () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, PALE_OAK_SAPLING, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_OAK_SAPLING).noOcclusion()), false);

    public static final DeferredBlock<Block> PROTECTIVE_LANTERN = BLOCKS.register("protective_lantern",
            () -> new ProtectiveLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));

    public static final DeferredBlock<Block> WATER_LANTERN = BLOCKS.register("water_lantern",
            () -> new FluidLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN), FluidLanternBlock.Type.WATER));

    public static final DeferredBlock<Block> LAVA_LANTERN = BLOCKS.register("lava_lantern",
            () -> new FluidLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN), FluidLanternBlock.Type.LAVA));

    public static final DeferredBlock<BigLilyPadBlock> BIG_LILY_PAD = BLOCKS.register("big_lily_pad",
            () -> new BigLilyPadBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LILY_PAD).noOcclusion()));

    public static final DeferredBlock<Block> SMITE_LANTERN = BLOCKS.register("smite_lantern",
            () -> new SmiteLanternBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.LANTERN)));

    /**
     * Your original "real hedge" block (damage/bonemeal/etc). Keep separate.
     */
    public static final DeferredBlock<ThornyHedgeBlock> THORNY_HEDGE =
            BLOCKS.register("thorny_hedge", () -> new ThornyHedgeBlock(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.WOOD).noOcclusion()));

    public static final DeferredBlock<Block> THORNY_HEDGE_SPROUTS =
            BLOCKS.register("thorny_hedge_sprouts", () -> new ThornyHedgeSprouts(Block.Properties.ofFullCopy(Blocks.WHEAT)));

    public static final Map<String, DeferredBlock<Block>> PARTICLE_HEDGES = new LinkedHashMap<>();
    public static final Map<String, DeferredBlock<Block>> CREEPING_HEDGES = new LinkedHashMap<>();


    private static <T extends Block> DeferredBlock<T> registerBackportedBlock(String name, java.util.function.Supplier<T> block) {
        return registerBackportedBlock(name, block, true);
    }

    private static <T extends Block> DeferredBlock<T> registerBackportedBlock(String name, java.util.function.Supplier<T> block, boolean createItem) {
        DeferredBlock<T> ret = MINECRAFT_BLOCKS.register(name, block);
        if (createItem) {
            MINECRAFT_ITEMS.register(name, () -> new net.minecraft.world.item.BlockItem(ret.get(), new net.minecraft.world.item.Item.Properties()));
        }
        return ret;
    }

    public static final DeferredBlock<Block> PALE_MOSS_BLOCK = registerBackportedBlock("pale_moss_block", () -> new PaleMossBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_BLOCK).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<Block> PALE_MOSS_CARPET = registerBackportedBlock("pale_moss_carpet", () -> new MossyCarpetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MOSS_CARPET).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<HangingMossBlock> PALE_HANGING_MOSS = registerBackportedBlock("pale_hanging_moss", () -> new HangingMossBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HANGING_ROOTS).mapColor(MapColor.COLOR_LIGHT_GRAY).sound(SoundType.MOSS_CARPET).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<RotatedPillarBlock> PALE_OAK_LOG = registerBackportedBlock("pale_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).mapColor(state -> state.getValue(RotatedPillarBlock.AXIS) == Direction.Axis.Y ? MapColor.QUARTZ : MapColor.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> PALE_OAK_WOOD = registerBackportedBlock("pale_oak_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD).mapColor(MapColor.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PALE_OAK_LOG = registerBackportedBlock("stripped_pale_oak_log", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG).mapColor(MapColor.QUARTZ)));
    public static final DeferredBlock<RotatedPillarBlock> STRIPPED_PALE_OAK_WOOD = registerBackportedBlock("stripped_pale_oak_wood", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD).mapColor(MapColor.QUARTZ)));

    public static final DeferredBlock<Block> PALE_OAK_PLANKS = registerBackportedBlock("pale_oak_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).mapColor(MapColor.QUARTZ)));
    public static final DeferredBlock<StairBlock> PALE_OAK_STAIRS = registerBackportedBlock("pale_oak_stairs", () -> new StairBlock(PALE_OAK_PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(PALE_OAK_PLANKS.get())));
    public static final DeferredBlock<SlabBlock> PALE_OAK_SLAB = registerBackportedBlock("pale_oak_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(PALE_OAK_PLANKS.get())));
    public static final DeferredBlock<FenceBlock> PALE_OAK_FENCE = registerBackportedBlock("pale_oak_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<FenceGateBlock> PALE_OAK_FENCE_GATE = registerBackportedBlock("pale_oak_fence_gate", () -> new FenceGateBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<DoorBlock> PALE_OAK_DOOR = registerBackportedBlock("pale_oak_door", () -> new DoorBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<TrapDoorBlock> PALE_OAK_TRAPDOOR = registerBackportedBlock("pale_oak_trapdoor", () -> new TrapDoorBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<PressurePlateBlock> PALE_OAK_PRESSURE_PLATE = registerBackportedBlock("pale_oak_pressure_plate", () -> new PressurePlateBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final DeferredBlock<ButtonBlock> PALE_OAK_BUTTON = registerBackportedBlock("pale_oak_button", () -> new ButtonBlock(BlockSetTypeVariant.PALE_OAK.getBlockSetType(), 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));

    public static final DeferredBlock<StandingSignBlock> PALE_OAK_SIGN = registerBackportedBlock("pale_oak_sign", () -> new StandingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)), false);
    public static final DeferredBlock<WallSignBlock> PALE_OAK_WALL_SIGN = registerBackportedBlock("pale_oak_wall_sign", () -> new WallSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)), false);
    public static final DeferredBlock<CeilingHangingSignBlock> PALE_OAK_HANGING_SIGN = registerBackportedBlock("pale_oak_hanging_sign", () -> new CeilingHangingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)), false);
    public static final DeferredBlock<WallHangingSignBlock> PALE_OAK_WALL_HANGING_SIGN = registerBackportedBlock("pale_oak_wall_hanging_sign", () -> new WallHangingSignBlock(WoodTypeVariant.PALE_OAK.getWoodType(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).mapColor(MapColor.COLOR_LIGHT_GRAY)), false);

    public static final DeferredBlock<LeavesBlock> PALE_OAK_LEAVES = registerBackportedBlock("pale_oak_leaves", () -> new ParticleLeavesBlock(50, ModParticle.PALE_OAK_LEAVES, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES).mapColor(MapColor.TERRACOTTA_GREEN)));

    public static final DeferredBlock<EyeblossomBlock> CLOSED_EYEBLOSSOM = registerBackportedBlock("closed_eyeblossom", () -> new EyeblossomBlock(false, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).mapColor(MapColor.COLOR_LIGHT_GRAY).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).randomTicks()));
    public static final DeferredBlock<EyeblossomBlock> OPEN_EYEBLOSSOM = registerBackportedBlock("open_eyeblossom", () -> new EyeblossomBlock(true, BlockBehaviour.Properties.ofFullCopy(Blocks.POPPY).mapColor(MapColor.COLOR_ORANGE).noCollission().instabreak().sound(SoundType.GRASS).offsetType(BlockBehaviour.OffsetType.XZ).pushReaction(PushReaction.DESTROY).lightLevel((state) -> 11).randomTicks()));
    public static final DeferredBlock<FlowerPotBlock> POTTED_CLOSED_EYEBLOSSOM = registerBackportedBlock("potted_closed_eyeblossom", () -> new EyeblossomFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, CLOSED_EYEBLOSSOM, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY).randomTicks()), false);
    public static final DeferredBlock<FlowerPotBlock> POTTED_OPEN_EYEBLOSSOM = registerBackportedBlock("potted_open_eyeblossom", () -> new EyeblossomFlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, OPEN_EYEBLOSSOM, BlockBehaviour.Properties.ofFullCopy(Blocks.POTTED_POPPY).lightLevel((state) -> 11).randomTicks()), false);

    public static final DeferredBlock<CreakingHeartBlock> CREAKING_HEART = registerBackportedBlock("creaking_heart", () -> new CreakingHeartBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).strength(10.0F).sound(ModSounds.CREAKING_HEART).lightLevel(state -> state.getValue(com.otterly76.ott.registry.ModBlockStateProperties.CREAKING_HEART_STATE) == com.otterly76.ott.util.block.CreakingHeartState.AWAKE ? 15 : 0)));
    public static final DeferredBlock<ResinClumpBlock> RESIN_CLUMP = registerBackportedBlock("resin_clump", () -> new ResinClumpBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SCULK_VEIN).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN)));
    public static final DeferredBlock<Block> RESIN_BLOCK = registerBackportedBlock("resin_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN)));
    public static final DeferredBlock<Block> RESIN_BRICKS = registerBackportedBlock("resin_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));
    public static final DeferredBlock<StairBlock> RESIN_BRICK_STAIRS = registerBackportedBlock("resin_brick_stairs", () -> new StairBlock(RESIN_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_STAIRS).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));
    public static final DeferredBlock<SlabBlock> RESIN_BRICK_SLAB = registerBackportedBlock("resin_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_SLAB).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));
    public static final DeferredBlock<WallBlock> RESIN_BRICK_WALL = registerBackportedBlock("resin_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICK_WALL).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));
    public static final DeferredBlock<Block> CHISELED_RESIN_BRICKS = registerBackportedBlock("chiseled_resin_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BRICKS).mapColor(MapColor.COLOR_ORANGE).sound(ModSounds.RESIN_BRICKS)));

    public static final DeferredBlock<DriedGhastBlock> DRIED_GHAST = registerBackportedBlock("dried_ghast", () -> new DriedGhastBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instabreak().sound(ModSounds.DRIED_GHAST).noOcclusion().randomTicks()));
    public static final DeferredBlock<ActualBushBlock> BUSH = registerBackportedBlock("bush", () -> new ActualBushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).replaceable().noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<FireflyBushBlock> FIREFLY_BUSH = registerBackportedBlock("firefly_bush", () -> new FireflyBushBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).ignitedByLava().lightLevel((state) -> 2).noCollission().instabreak().sound(SoundType.SWEET_BERRY_BUSH).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<Block> WILDFLOWERS = registerBackportedBlock("wildflowers", () -> new com.otterly76.ott.block.custom.WildflowersBlock(BlockBehaviour.Properties.of().mapColor(MapColor.PLANT).noCollission().sound(SoundType.PINK_PETALS).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<LeafLitterBlock> LEAF_LITTER = registerBackportedBlock("leaf_litter", () -> new LeafLitterBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).replaceable().noCollission().sound(ModSounds.LEAF_LITTER).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<CactusFlowerBlock> CACTUS_FLOWER = registerBackportedBlock("cactus_flower", () -> new CactusFlowerBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).noCollission().instabreak().ignitedByLava().sound(ModSounds.CACTUS_FLOWER).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<ShortDryGrassBlock> SHORT_DRY_GRASS = registerBackportedBlock("short_dry_grass", () -> new ShortDryGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).replaceable().noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().offsetType(BlockBehaviour.OffsetType.XYZ).pushReaction(PushReaction.DESTROY)));
    public static final DeferredBlock<TallDryGrassBlock> TALL_DRY_GRASS = registerBackportedBlock("tall_dry_grass", () -> new TallDryGrassBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).replaceable().noCollission().instabreak().sound(SoundType.GRASS).ignitedByLava().offsetType(BlockBehaviour.OffsetType.XYZ).pushReaction(PushReaction.DESTROY)));

    public static final DeferredBlock<CopperChestBlock> COPPER_CHEST = registerBackportedBlock("copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> EXPOSED_COPPER_CHEST = registerBackportedBlock("exposed_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> WEATHERED_COPPER_CHEST = registerBackportedBlock("weathered_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> OXIDIZED_COPPER_CHEST = registerBackportedBlock("oxidized_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);

    public static final DeferredBlock<CopperChestBlock> WAXED_COPPER_CHEST = registerBackportedBlock("waxed_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.UNAFFECTED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> WAXED_EXPOSED_COPPER_CHEST = registerBackportedBlock("waxed_exposed_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.EXPOSED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> WAXED_WEATHERED_COPPER_CHEST = registerBackportedBlock("waxed_weathered_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.WEATHERED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<CopperChestBlock> WAXED_OXIDIZED_COPPER_CHEST = registerBackportedBlock("waxed_oxidized_copper_chest", () -> new CopperChestBlock(WeatheringCopper.WeatherState.OXIDIZED, BlockBehaviour.Properties.of().strength(2.5f).sound(SoundType.COPPER)), false);
    public static final DeferredBlock<Block> WEATHERING_STATION = register("weathering_station", () -> new com.otterly76.ott.block.custom.WeatheringStationBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<Block> WOODCUTTER = register("woodcutter", () -> new com.otterly76.ott.block.custom.WoodcutterBlock(BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.WOOD).requiresCorrectToolForDrops().noOcclusion()));

    public static final List<DeferredBlock<com.otterly76.ott.block.shelf.ShelfBlock>> SHELVES = new ArrayList<>();
    public static final Map<String, Supplier<? extends com.otterly76.ott.block.custom.CopperGolemStatueBlock>> COPPER_GOLEM_STATUES = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_BUTTONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_DOORS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_TRAPDOORS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_LANTERNS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_SOUL_LANTERNS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_CHAINS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_BARS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_HOPPERS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_LADDERS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_CAULDRONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_WATER_CAULDRONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_LAVA_CAULDRONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_POWDER_SNOW_CAULDRONS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_RAILS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_ANVILS = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> COPPER_PRESSURE_PLATES = new LinkedHashMap<>();
    public static final Map<String, Supplier<? extends Block>> LIGHTNING_RODS = new LinkedHashMap<>();
    public static final DeferredBlock<Block> COPPER_TORCH = registerBackportedBlock("copper_torch", () -> new CopperTorchBlock(ModParticle.COPPER_FIRE_FLAME, BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(s -> 14).sound(SoundType.WOOD)), false);
    public static final DeferredBlock<Block> COPPER_WALL_TORCH = registerBackportedBlock("copper_wall_torch", () -> new CopperWallTorchBlock(ModParticle.COPPER_FIRE_FLAME, BlockBehaviour.Properties.of().noCollission().instabreak().lightLevel(s -> 14).sound(SoundType.WOOD).lootFrom(COPPER_TORCH)), false);
    public static final DeferredBlock<SkullBlock> DRAGON_SKULL = BLOCKS.register("dragon_skull", () -> new SkullBlock(ModSkullType.DRAGON_SKULL, BlockBehaviour.Properties.ofFullCopy(Blocks.DRAGON_HEAD)));
    public static final DeferredBlock<WallSkullBlock> DRAGON_WALL_SKULL = BLOCKS.register("dragon_wall_skull", () -> new WallSkullBlock(ModSkullType.DRAGON_SKULL, BlockBehaviour.Properties.ofFullCopy(Blocks.DRAGON_WALL_HEAD).lootFrom(DRAGON_SKULL)));

    private static void register3DBlockItem(DeferredBlock<? extends Block> block) {
        MINECRAFT_ITEMS.register(block.getId().getPath(), () -> new com.otterly76.ott.item.custom.Copper3DBlockItem(block.get(), new net.minecraft.world.item.Item.Properties()));
    }

    private static void registerDynamicBlocks() {
        for (Butterfly.Variant variant : Butterfly.Variant.values()) {
            BUTTERFLY_JARS.put(variant, BLOCKS.register("butterfly_jar_" + variant.getName(),
                    () -> new com.otterly76.ott.block.custom.ButterflyJarBlock(variant, BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(0.3F).sound(SoundType.GLASS).noOcclusion())));
        }

        MINECRAFT_ITEMS.register("copper_torch", () -> new net.minecraft.world.item.StandingAndWallBlockItem(COPPER_TORCH.get(), COPPER_WALL_TORCH.get(), new net.minecraft.world.item.Item.Properties(), net.minecraft.core.Direction.DOWN));

        register3DBlockItem(COPPER_CHEST);
        register3DBlockItem(EXPOSED_COPPER_CHEST);
        register3DBlockItem(WEATHERED_COPPER_CHEST);
        register3DBlockItem(OXIDIZED_COPPER_CHEST);
        register3DBlockItem(WAXED_COPPER_CHEST);
        register3DBlockItem(WAXED_EXPOSED_COPPER_CHEST);
        register3DBlockItem(WAXED_WEATHERED_COPPER_CHEST);
        register3DBlockItem(WAXED_OXIDIZED_COPPER_CHEST);

        String[] shelfWoods = {"oak", "spruce", "birch", "jungle", "acacia", "dark_oak", "mangrove", "cherry", "bamboo", "crimson", "warped", "pale_oak"};
        for (String wood : shelfWoods) {
            SHELVES.add(registerBackportedBlock(wood + "_shelf", () -> new com.otterly76.ott.block.shelf.ShelfBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion())));
        }

        String[] copperStates = {"", "exposed_", "weathered_", "oxidized_"};
        WeatheringCopper.WeatherState[] states = {WeatheringCopper.WeatherState.UNAFFECTED, WeatheringCopper.WeatherState.EXPOSED, WeatheringCopper.WeatherState.WEATHERED, WeatheringCopper.WeatherState.OXIDIZED};

        for (int i = 0; i < copperStates.length; i++) {
            String stateName = copperStates[i];
            WeatheringCopper.WeatherState state = states[i];

            COPPER_BUTTONS.put(stateName, registerBackportedBlock(stateName + "copper_button", () -> new com.otterly76.ott.block.custom.CopperButtonBlock(state, net.minecraft.world.level.block.state.properties.BlockSetType.IRON, 30, BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.COPPER))));
            COPPER_BUTTONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_button", () -> new com.otterly76.ott.block.custom.CopperButtonBlock(state, net.minecraft.world.level.block.state.properties.BlockSetType.IRON, 30, BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.COPPER))));

            COPPER_PRESSURE_PLATES.put(stateName, registerBackportedBlock(stateName + "copper_pressure_plate", () -> new com.otterly76.ott.block.custom.CopperPressurePlateBlock(state, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.COPPER))));
            COPPER_PRESSURE_PLATES.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_pressure_plate", () -> new com.otterly76.ott.block.custom.CopperPressurePlateBlock(state, net.minecraft.world.level.block.state.properties.BlockSetType.COPPER, BlockBehaviour.Properties.of().strength(0.5f).sound(SoundType.COPPER))));

            // In 1.21.1, all 8 variants of copper doors and trapdoors are vanilla.
            // We use vanilla instances from the Blocks class to avoid duplicate registration issues and NPEs during baking.
            switch (stateName) {
                case "" -> {
                    COPPER_DOORS.put("", () -> Blocks.COPPER_DOOR);
                    COPPER_DOORS.put("waxed_", () -> Blocks.WAXED_COPPER_DOOR);
                    COPPER_TRAPDOORS.put("", () -> Blocks.COPPER_TRAPDOOR);
                    COPPER_TRAPDOORS.put("waxed_", () -> Blocks.WAXED_COPPER_TRAPDOOR);
                }
                case "exposed_" -> {
                    COPPER_DOORS.put("exposed_", () -> Blocks.EXPOSED_COPPER_DOOR);
                    COPPER_DOORS.put("waxed_exposed_", () -> Blocks.WAXED_EXPOSED_COPPER_DOOR);
                    COPPER_TRAPDOORS.put("exposed_", () -> Blocks.EXPOSED_COPPER_TRAPDOOR);
                    COPPER_TRAPDOORS.put("waxed_exposed_", () -> Blocks.WAXED_EXPOSED_COPPER_TRAPDOOR);
                }
                case "weathered_" -> {
                    COPPER_DOORS.put("weathered_", () -> Blocks.WEATHERED_COPPER_DOOR);
                    COPPER_DOORS.put("waxed_weathered_", () -> Blocks.WAXED_WEATHERED_COPPER_DOOR);
                    COPPER_TRAPDOORS.put("weathered_", () -> Blocks.WEATHERED_COPPER_TRAPDOOR);
                    COPPER_TRAPDOORS.put("waxed_weathered_", () -> Blocks.WAXED_WEATHERED_COPPER_TRAPDOOR);
                }
                case "oxidized_" -> {
                    COPPER_DOORS.put("oxidized_", () -> Blocks.OXIDIZED_COPPER_DOOR);
                    COPPER_DOORS.put("waxed_oxidized_", () -> Blocks.WAXED_OXIDIZED_COPPER_DOOR);
                    COPPER_TRAPDOORS.put("oxidized_", () -> Blocks.OXIDIZED_COPPER_TRAPDOOR);
                    COPPER_TRAPDOORS.put("waxed_oxidized_", () -> Blocks.WAXED_OXIDIZED_COPPER_TRAPDOOR);
                }
            }

            var statue = registerBackportedBlock(stateName + "copper_golem_statue", () -> new com.otterly76.ott.block.custom.CopperGolemStatueBlock(state, BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.COPPER)), false);
            COPPER_GOLEM_STATUES.put(stateName, statue);
            register3DBlockItem(statue);

            var waxedStatue = registerBackportedBlock("waxed_" + stateName + "copper_golem_statue", () -> new com.otterly76.ott.block.custom.CopperGolemStatueBlock(state, BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.COPPER)), false);
            COPPER_GOLEM_STATUES.put("waxed_" + stateName, waxedStatue);
            register3DBlockItem(waxedStatue);

            COPPER_LANTERNS.put(stateName, registerBackportedBlock(stateName + "copper_lantern", () -> new com.otterly76.ott.block.custom.WeatheringCopperLanternBlock(state, BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.LANTERN).lightLevel(s -> 15).noOcclusion())));
            COPPER_LANTERNS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_lantern", () -> new com.otterly76.ott.block.custom.WeatheringCopperLanternBlock(state, BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.LANTERN).lightLevel(s -> 15).noOcclusion())));

            COPPER_SOUL_LANTERNS.put(stateName, registerBackportedBlock(stateName + "copper_soul_lantern", () -> new com.otterly76.ott.block.custom.WeatheringCopperLanternBlock(state, BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.LANTERN).lightLevel(s -> 10).noOcclusion())));
            COPPER_SOUL_LANTERNS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_soul_lantern", () -> new com.otterly76.ott.block.custom.WeatheringCopperLanternBlock(state, BlockBehaviour.Properties.of().strength(3.5f).sound(SoundType.LANTERN).lightLevel(s -> 10).noOcclusion())));

            COPPER_CHAINS.put(stateName, registerBackportedBlock(stateName + "copper_chain", () -> new com.otterly76.ott.block.custom.WeatheringCopperChainBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.CHAIN).noOcclusion())));
            COPPER_CHAINS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_chain", () -> new com.otterly76.ott.block.custom.WeatheringCopperChainBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.CHAIN).noOcclusion())));

            COPPER_BARS.put(stateName, registerBackportedBlock(stateName + "copper_bars", () -> new com.otterly76.ott.block.custom.WeatheringCopperBarsBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.COPPER).noOcclusion())));
            COPPER_BARS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_bars", () -> new com.otterly76.ott.block.custom.WeatheringCopperBarsBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.COPPER).noOcclusion())));

            COPPER_HOPPERS.put(stateName, registerBackportedBlock(stateName + "copper_hopper", () -> new com.otterly76.ott.block.custom.CopperHopperBlock(state, BlockBehaviour.Properties.of().strength(3.0f, 4.8f).sound(SoundType.COPPER).noOcclusion())));
            COPPER_HOPPERS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_hopper", () -> new com.otterly76.ott.block.custom.CopperHopperBlock(state, BlockBehaviour.Properties.of().strength(3.0f, 4.8f).sound(SoundType.COPPER).noOcclusion())));

            COPPER_LADDERS.put(stateName, registerBackportedBlock(stateName + "copper_ladder", () -> new com.otterly76.ott.block.custom.WeatheringCopperLadderBlock(state, BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.COPPER).noOcclusion().requiresCorrectToolForDrops())));
            COPPER_LADDERS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_ladder", () -> new com.otterly76.ott.block.custom.WeatheringCopperLadderBlock(state, BlockBehaviour.Properties.of().strength(1.5f).sound(SoundType.COPPER).noOcclusion().requiresCorrectToolForDrops())));

            COPPER_CAULDRONS.put(stateName, registerBackportedBlock(stateName + "copper_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperCauldronBlock(state, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_EMPTY, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(2.0F).sound(SoundType.COPPER).noOcclusion().pushReaction(PushReaction.BLOCK))));
            COPPER_CAULDRONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperCauldronBlock(state, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_EMPTY, BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).requiresCorrectToolForDrops().strength(2.0F).sound(SoundType.COPPER).noOcclusion().pushReaction(PushReaction.BLOCK))));

            // Filled Cauldrons
            // Note: Interaction maps are set to COPPER_EMPTY/WATER/etc.
            COPPER_WATER_CAULDRONS.put(stateName, registerBackportedBlock(stateName + "copper_water_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLayeredCauldronBlock(state, Biome.Precipitation.RAIN, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_WATER, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));
            COPPER_WATER_CAULDRONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_water_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLayeredCauldronBlock(state, Biome.Precipitation.RAIN, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_WATER, BlockBehaviour.Properties.ofFullCopy(Blocks.WATER_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));

            COPPER_LAVA_CAULDRONS.put(stateName, registerBackportedBlock(stateName + "copper_lava_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLavaCauldronBlock(state, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_LAVA, BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));
            COPPER_LAVA_CAULDRONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_lava_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLavaCauldronBlock(state, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_LAVA, BlockBehaviour.Properties.ofFullCopy(Blocks.LAVA_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));

            COPPER_POWDER_SNOW_CAULDRONS.put(stateName, registerBackportedBlock(stateName + "copper_powder_snow_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLayeredCauldronBlock(state, Biome.Precipitation.SNOW, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_POWDER_SNOW, BlockBehaviour.Properties.ofFullCopy(Blocks.POWDER_SNOW_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));
            COPPER_POWDER_SNOW_CAULDRONS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_powder_snow_cauldron", () -> new com.otterly76.ott.block.custom.WeatheringCopperLayeredCauldronBlock(state, Biome.Precipitation.SNOW, com.otterly76.ott.handler.CauldronInteractionHandler.COPPER_POWDER_SNOW, BlockBehaviour.Properties.ofFullCopy(Blocks.POWDER_SNOW_CAULDRON).mapColor(MapColor.COLOR_ORANGE).sound(SoundType.COPPER).pushReaction(PushReaction.BLOCK)), false));

            COPPER_RAILS.put(stateName, registerBackportedBlock(stateName + "copper_rail", () -> new com.otterly76.ott.block.custom.WeatheringCopperRailBlock(state, BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.COPPER))));
            COPPER_RAILS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "copper_rail", () -> new com.otterly76.ott.block.custom.WeatheringCopperRailBlock(state, BlockBehaviour.Properties.of().noCollission().strength(0.7F).sound(SoundType.COPPER))));

            // In 1.21.1, all 8 variants of lightning rods and copper grates are vanilla?
            // Wait, only lightning_rod is vanilla. Weathered variants of lightning rods are backported.
            // All 8 variants of copper grates ARE vanilla in 1.21.1.
            if (stateName.isEmpty()) {
                LIGHTNING_RODS.put("", () -> Blocks.LIGHTNING_ROD);
            } else {
                LIGHTNING_RODS.put(stateName, registerBackportedBlock(stateName + "lightning_rod", () -> new com.otterly76.ott.block.custom.WeatheringCopperLightningRodBlock(state, BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.COPPER))));
            }
            LIGHTNING_RODS.put("waxed_" + stateName, registerBackportedBlock("waxed_" + stateName + "lightning_rod", () -> new com.otterly76.ott.block.custom.WeatheringCopperLightningRodBlock(state, BlockBehaviour.Properties.of().strength(3.0f).sound(SoundType.COPPER))));
        }

        for (String damagePrefix : new String[]{"", "chipped_", "damaged_"}) {
            for (int i = 0; i < copperStates.length; i++) {
                String stateName = copperStates[i];
                WeatheringCopper.WeatherState state = states[i];
                COPPER_ANVILS.put(damagePrefix + stateName, registerBackportedBlock(damagePrefix + stateName + "copper_anvil", () -> new com.otterly76.ott.block.custom.WeatheringCopperAnvilBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.ANVIL).requiresCorrectToolForDrops())));
                COPPER_ANVILS.put("waxed_" + damagePrefix + stateName, registerBackportedBlock("waxed_" + damagePrefix + stateName + "copper_anvil", () -> new com.otterly76.ott.block.custom.WeatheringCopperAnvilBlock(state, BlockBehaviour.Properties.of().strength(5.0f).sound(SoundType.ANVIL).requiresCorrectToolForDrops())));
            }
        }

        registerGradientBlocks(Blocks.WHITE_CONCRETE, GradientConcreteBlock::new, ALL_CONCRETE_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_TERRACOTTA, GradientTerracottaBlock::new, ALL_TERRACOTTA_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_WOOL, GradientWoolBlock::new, ALL_WOOL_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_STAINED_GLASS, GradientStainedGlassBlock::new, ALL_STAINED_GLASS_BLOCKS::add);
        registerGradientBlocks(Blocks.WHITE_CONCRETE_POWDER, GradientConcretePowderBlock::new, ALL_CONCRETE_POWDER_BLOCKS::add);

        // Register all ott wood sets
        ModWoodSets.ALL.forEach(set -> WOOD_SETS.put(set.name(), com.otterly76.ott.block.wood.WoodSetBlockRegistrar.registerOttWoodSet(set.name())));

        // Register vanilla wood structural blocks
        // Oak already has dedicated static block fields; wrap them rather than re-registering.
        VANILLA_STRUCTURAL_SETS.put("oak", new WoodStructuralBlocks(
                OAK_PERGOLA, OAK_BEAM, OAK_PLANKS_PLATE, OAK_PLANKS_EDGE,
                OAK_BANNISTER, OAK_SUPPORT_SLAB, OAK_SUPPORT_BEAM, OAK_GEOMETRIC_WINDOW));
        for (String name : List.of("spruce", "birch", "jungle", "acacia", "dark_oak",
                "mangrove", "cherry", "bamboo", "crimson", "warped", "pale_oak")) {
            VANILLA_STRUCTURAL_SETS.put(name, com.otterly76.ott.block.wood.WoodSetBlockRegistrar.registerVanillaStructural(name));
        }

        // Register wood wall blocks
        for (String name : List.of("oak", "spruce", "birch", "jungle", "acacia", "dark_oak",
                "mangrove", "cherry", "bamboo", "crimson", "warped")) {
            VANILLA_WALLS.put(name, BLOCKS.register(name + "_wall",
                    () -> new WallBlock(Properties.of().strength(2.0f).sound(SoundType.WOOD))));
        }

        // Register all ott color sets
        com.otterly76.ott.color.ModColorSets.ALL.forEach(set -> COLOR_SETS.put(set.name(), com.otterly76.ott.block.color.ColorSetBlockRegistrar.registerOttColorSet(set.name())));

        // Register seaglass for all colors (vanilla dyes + custom color sets)
        for (com.otterly76.ott.color.ModPatterns.ColorInfo color : com.otterly76.ott.color.ModPatterns.ALL_COLORS) {
            final String c = color.name();
            SEAGLASS_SETS.put(c, new SeaglassColorBlocks(
                    register(c + "_seaglass",         () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion())),
                    register(c + "_bubbles_seaglass",  () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion())),
                    register(c + "_smooth_seaglass",   () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion())),
                    register(c + "_waves_seaglass",    () -> new Block(Properties.of().strength(4.0f).requiresCorrectToolForDrops().sound(SoundType.GLASS).noOcclusion()))
            ));
        }

        registerPatternBlocks();

        // Register all particle hedges
        ModHedgeVariants.ALL.forEach(variant -> {
            PARTICLE_HEDGES.put(variant.name(), BLOCKS.register(
                    variant.name() + "_hedge",
                    () -> new ParticleHedgeBlock(
                            Properties.of().strength(1.0f).sound(SoundType.GRASS).noOcclusion(),
                            variant.leafParticle()
                    )
            ));

            CREEPING_HEDGES.put(variant.name(), BLOCKS.register(
                    variant.name() + "_creeping_hedge",
                    () -> new ParticleCreepingHedgeBlock(
                            Properties.of().strength(1.0f).sound(SoundType.GRASS).noOcclusion(),
                            variant.leafParticle(),
                            variant.creepOverlayTexture()
                    )
            ));
        });

        registerElevators();
        registerFutons();
    }

    private static void registerFutons() {
        for (com.otterly76.ott.color.ModPatterns.ColorInfo color : com.otterly76.ott.color.ModPatterns.ALL_COLORS) {
            final String colorName = color.name();
            final net.minecraft.world.item.DyeColor dyeColor = toDyeColor(colorName);
            FUTONS.put(colorName, BLOCKS.register(
                    colorName + "_futon",
                    () -> new FutonBlock(dyeColor, Properties.of().strength(0.5f).sound(SoundType.WOOL).noOcclusion())
            ));
        }
    }

    private static net.minecraft.world.item.DyeColor toDyeColor(String name) {
        for (net.minecraft.world.item.DyeColor c : net.minecraft.world.item.DyeColor.values()) {
            if (c.getName().equals(name)) return c;
        }
        return net.minecraft.world.item.DyeColor.WHITE;
    }

    private static void registerElevators() {
        for (ModPatterns.ColorInfo color : ModPatterns.ALL_COLORS) {
            final String colorName = color.name();
            ELEVATORS.put(colorName, BLOCKS.register(
                    colorName + "_elevator",
                    () -> new ElevatorBlock(colorName,
                            BlockBehaviour.Properties.of()
                                    .strength(0.8f)
                                    .sound(SoundType.WOOL))
            ));
        }
    }

    private static void registerPatternBlocks() {
        for (String pattern : com.otterly76.ott.color.ModPatterns.PATTERNS) {
            boolean isPillar = com.otterly76.ott.color.ModPatterns.PILLAR_PATTERNS.contains(pattern);
            Map<String, DeferredBlock<Block>>        colorMap   = new LinkedHashMap<>();
            Map<String, DeferredBlock<PlateBlock>>   plateMap   = new LinkedHashMap<>();
            Map<String, DeferredBlock<EdgeBlock>>    edgeMap    = new LinkedHashMap<>();
            Map<String, DeferredBlock<BeamBlock>>    beamMap    = new LinkedHashMap<>();
            Map<String, DeferredBlock<PergolaBlock>> pergolaMap = new LinkedHashMap<>();
            Map<String, DeferredBlock<Block>>        windowMap  = new LinkedHashMap<>();
            for (com.otterly76.ott.color.ModPatterns.ColorInfo color : com.otterly76.ott.color.ModPatterns.ALL_COLORS) {
                String base = color.name() + "_" + pattern;
                colorMap.put(color.name(), isPillar
                        ? registerAsPillar(base)
                        : register(base, () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE))));
                plateMap.put(color.name(),   register(base + "_plate",           () -> new PlateBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).noOcclusion())));
                edgeMap.put(color.name(),    register(base + "_edge",            () -> new EdgeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).noOcclusion())));
                beamMap.put(color.name(),    register(base + "_beam",            () -> new BeamBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.STONE).noOcclusion())));
                pergolaMap.put(color.name(), register(base + "_pergola",         () -> new PergolaBlock(BlockBehaviour.Properties.of().strength(2.0f).sound(SoundType.STONE).noOcclusion())));
                windowMap.put(color.name(),  register(base + "_geometric_window", () -> new com.otterly76.ott.block.custom.GeometricWindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE).noOcclusion())));
            }
            PATTERN_BLOCKS.put(pattern,   colorMap);
            PATTERN_PLATES.put(pattern,   plateMap);
            PATTERN_EDGES.put(pattern,    edgeMap);
            PATTERN_BEAMS.put(pattern,    beamMap);
            PATTERN_PERGOLAS.put(pattern, pergolaMap);
            PATTERN_WINDOWS.put(pattern,  windowMap);
        }
    }

    /** Registers a {@link RotatedPillarBlock} and stores it in the {@code DeferredBlock<Block>} map via an unchecked cast (safe: RotatedPillarBlock extends Block). */
    @SuppressWarnings("unchecked")
    private static DeferredBlock<Block> registerAsPillar(String name) {
        DeferredBlock<?> holder = BLOCKS.register(name,
                () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE)));
        return (DeferredBlock<Block>) holder;
    }

    private static <T extends Block & IGradientBlock> void registerGradientBlocks(Block block, GradientBlockBuilder<T> builder, Consumer<DeferredBlock<? extends IGradientBlock>> adder) {
        // We loop through ALL colors for both slots
        for (final DyeColor color1 : DyeColor.values()) {
            for (final DyeColor color2 : DyeColor.values()) {
                // Only skip if the colors are identical
                if (color1 != color2) {
                    final String blockBaseName = BuiltInRegistries.BLOCK.getKey(block).getPath().replace("white_", "");

                    // This will naturally create both "red_blue_..." and "blue_red_..."
                    final String fullName = String.format("%s_%s_%s", color1.getName(), color2.getName(), blockBaseName);

                    DeferredBlock<? extends IGradientBlock> gradientBlock = BLOCKS.register(fullName, () ->
                            builder.create(BlockBehaviour.Properties.ofFullCopy(block), color1, color2, color -> "%s_%s".formatted(color.getName(), blockBaseName))
                    );

                    adder.accept(gradientBlock);
                    ALL_GRADIENT_BLOCKS.add(gradientBlock);
                }
            }
        }
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientBlocks() {
        return ALL_GRADIENT_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientConcreteBlocks() {
        return ALL_CONCRETE_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientTerracottaBlocks() {
        return ALL_TERRACOTTA_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientWoolBlocks() {
        return ALL_WOOL_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientStainedGlassBlocks() {
        return ALL_STAINED_GLASS_BLOCKS;
    }

    public static Collection<DeferredBlock<? extends IGradientBlock>> getAllGradientConcretePowderBlocks() {
        return ALL_CONCRETE_POWDER_BLOCKS;
    }

    /**
     * ott wood sets (ott namespace). Key = set name (e.g. "starlight").
     */
    public static final Map<String, WoodSetBlocks> WOOD_SETS = new LinkedHashMap<>();

    /** Wood wall blocks (ott namespace). Key = wood name (e.g. "oak", "spruce"). */
    public static final Map<String, DeferredBlock<WallBlock>> VANILLA_WALLS = new LinkedHashMap<>();

    /**
     * ott color sets (ott namespace). Key = color name (e.g. "aquamarine").
     */
    public static final Map<String, ColorSetBlocks> COLOR_SETS = new LinkedHashMap<>();

    /**
     * Seaglass color sets (ott namespace). Key = color name, covers all vanilla dyes + custom color sets.
     */
    public static final Map<String, SeaglassColorBlocks> SEAGLASS_SETS = new LinkedHashMap<>();

    /**
     * ott pattern blocks (ott namespace). Key1 = pattern name, Key2 = color name.
     */
    public static final Map<String, Map<String, DeferredBlock<Block>>>        PATTERN_BLOCKS   = new LinkedHashMap<>();
    public static final Map<String, Map<String, DeferredBlock<PlateBlock>>>   PATTERN_PLATES   = new LinkedHashMap<>();
    public static final Map<String, Map<String, DeferredBlock<EdgeBlock>>>    PATTERN_EDGES    = new LinkedHashMap<>();
    public static final Map<String, Map<String, DeferredBlock<BeamBlock>>>    PATTERN_BEAMS    = new LinkedHashMap<>();
    public static final Map<String, Map<String, DeferredBlock<PergolaBlock>>> PATTERN_PERGOLAS = new LinkedHashMap<>();
    public static final Map<String, Map<String, DeferredBlock<Block>>>        PATTERN_WINDOWS  = new LinkedHashMap<>();

    public record ColorSetBlocks(
            DeferredBlock<CandleBlock> candle,
            DeferredBlock<Block> concrete,
            DeferredBlock<ColoredFallingBlock> concretePowder,
            DeferredBlock<GlazedTerracottaBlock> glazedTerracotta,
            DeferredBlock<ShulkerBoxBlock> shulkerBox,
            DeferredBlock<StainedGlassBlock> stainedGlass,
            DeferredBlock<StainedGlassPaneBlock> stainedGlassPane,
            DeferredBlock<Block> terracotta,
            DeferredBlock<Block> wool,
            DeferredBlock<BedBlock> bed,
            DeferredBlock<CarpetBlock> carpet,
            DeferredBlock<BannerBlock> banner,
            DeferredBlock<WallBannerBlock> wallBanner,
            DeferredBlock<PlateBlock> plate,
            DeferredBlock<EdgeBlock> edge,
            DeferredBlock<BeamBlock> beam,
            DeferredBlock<PergolaBlock> pergola,
            DeferredBlock<Block> geometricWindow
    ) {}

    public record SeaglassColorBlocks(
            DeferredBlock<Block> seaglass,
            DeferredBlock<Block> bubblesSeaglass,
            DeferredBlock<Block> smoothSeaglass,
            DeferredBlock<Block> wavesSeaglass
    ) {}

    public record WoodSetBlocks(
            DeferredBlock<RotatedPillarBlock> log,
            DeferredBlock<RotatedPillarBlock> wood,
            DeferredBlock<RotatedPillarBlock> strippedLog,
            DeferredBlock<RotatedPillarBlock> strippedWood,
            DeferredBlock<Block> planks,
            DeferredBlock<StairBlock> stairs,
            DeferredBlock<SlabBlock> slab,
            DeferredBlock<FenceBlock> fence,
            DeferredBlock<FenceGateBlock> fenceGate,
            DeferredBlock<DoorBlock> door,
            DeferredBlock<TrapDoorBlock> trapdoor,
            DeferredBlock<ButtonBlock> button,
            DeferredBlock<PressurePlateBlock> pressurePlate,
            DeferredBlock<LeavesBlock> leaves,
            DeferredBlock<SaplingBlock> sapling,
            DeferredBlock<FlowerPotBlock> pottedSapling,
            DeferredBlock<StandingSignBlock> sign,
            DeferredBlock<WallSignBlock> wallSign,
            DeferredBlock<CeilingHangingSignBlock> hangingSign,
            DeferredBlock<WallHangingSignBlock> wallHangingSign,
            DeferredBlock<com.otterly76.ott.block.custom.PergolaBlock> pergola,
            DeferredBlock<com.otterly76.ott.block.custom.BeamBlock> beam,
            DeferredBlock<com.otterly76.ott.block.custom.PlateBlock> planksPlate,
            DeferredBlock<com.otterly76.ott.block.custom.EdgeBlock> planksEdge,
            DeferredBlock<com.otterly76.ott.block.custom.BannisterBlock> bannister,
            DeferredBlock<com.otterly76.ott.block.custom.SupportSlabBlock> supportSlab,
            DeferredBlock<com.otterly76.ott.block.custom.SupportBeamBlock> supportBeam,
            DeferredBlock<Block> geometricWindow
    )
    {
    }

    /**
     * Vanilla wood structural blocks (ott namespace). Key = vanilla set name (e.g. "oak").
     */
    public static final Map<String, WoodStructuralBlocks> VANILLA_STRUCTURAL_SETS = new LinkedHashMap<>();

    public record WoodStructuralBlocks(
            DeferredBlock<com.otterly76.ott.block.custom.PergolaBlock> pergola,
            DeferredBlock<com.otterly76.ott.block.custom.BeamBlock> beam,
            DeferredBlock<com.otterly76.ott.block.custom.PlateBlock> planksPlate,
            DeferredBlock<com.otterly76.ott.block.custom.EdgeBlock> planksEdge,
            DeferredBlock<com.otterly76.ott.block.custom.BannisterBlock> bannister,
            DeferredBlock<com.otterly76.ott.block.custom.SupportSlabBlock> supportSlab,
            DeferredBlock<com.otterly76.ott.block.custom.SupportBeamBlock> supportBeam,
            DeferredBlock<Block> geometricWindow
    ) {}

    public static final DeferredBlock<Block> GLASS_JAR = BLOCKS.register("glass_jar",
            () -> new com.otterly76.ott.block.custom.GlassJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));

    public static final DeferredBlock<Block> FIREFLY_IN_A_JAR = BLOCKS.register("firefly_in_a_jar",
            () -> new com.otterly76.ott.block.custom.FireflyJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().lightLevel((state) -> 7)));

    public static final DeferredBlock<Block> FIREFLIES_IN_A_JAR = BLOCKS.register("fireflies_in_a_jar",
            () -> new com.otterly76.ott.block.custom.FireflyJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().lightLevel((state) -> 11)));

    public static final DeferredBlock<Block> FIREFLY_JAR = BLOCKS.register("firefly_jar",
            () -> new com.otterly76.ott.block.custom.FireflyJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(0.3F).sound(SoundType.GLASS).noOcclusion().lightLevel((state) -> 15)));

    public static final Map<Butterfly.Variant, DeferredBlock<Block>> BUTTERFLY_JARS = new HashMap<>();
    public static final DeferredBlock<Block> CATERPILLAR_JAR = BLOCKS.register("caterpillar_jar",
            () -> new com.otterly76.ott.block.custom.CaterpillarJarBlock(BlockBehaviour.Properties.of().mapColor(MapColor.NONE).strength(0.3F).sound(SoundType.GLASS).noOcclusion()));

    // --- Ecologics ---
    public static final DeferredBlock<CoconutBlock> COCONUT = BLOCKS.register("coconut",
            () -> new CoconutBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).strength(2.0F).sound(SoundType.WOOD).noOcclusion()));

    // --- Friends and Foes ---
    public static final DeferredBlock<CrabEggBlock> CRAB_EGG = BLOCKS.register("crab_egg",
            () -> new CrabEggBlock(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).strength(0.5F).sound(SoundType.METAL).noOcclusion().randomTicks()));

    // -------------------------------------------------------------------------
    // --- Mosaic / Fresco decorative blocks ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block> WATER_MOSAIC_BORDER = register("water_mosaic_border",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> WATER_MOSAIC_GEOMETRIC = register("water_mosaic_geometric",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> WATER_MOSAIC_PATTERN = register("water_mosaic_pattern",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> WATER_MOSAIC_DELICATE = register("water_mosaic_delicate",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> WATER_MOSAIC_TRADITIONAL = register("water_mosaic_traditional",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<com.otterly76.ott.block.custom.WaterMosaicRecessBlock> WATER_MOSAIC_RECESS = register("water_mosaic_recess",
            () -> new com.otterly76.ott.block.custom.WaterMosaicRecessBlock(
                    net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState(),
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.WaterMosaicRecessBlock> SPIRIT_MOSAIC_RECESS = register("spirit_mosaic_recess",
            () -> new com.otterly76.ott.block.custom.WaterMosaicRecessBlock(
                    net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState(),
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.WaterMosaicRecessBlock> AIR_MOSAIC_RECESS = register("air_mosaic_recess",
            () -> new com.otterly76.ott.block.custom.WaterMosaicRecessBlock(
                    net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState(),
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.WaterMosaicRecessBlock> EARTH_MOSAIC_RECESS = register("earth_mosaic_recess",
            () -> new com.otterly76.ott.block.custom.WaterMosaicRecessBlock(
                    net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState(),
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.WaterMosaicRecessBlock> FIRE_MOSAIC_RECESS = register("fire_mosaic_recess",
            () -> new com.otterly76.ott.block.custom.WaterMosaicRecessBlock(
                    net.minecraft.world.level.block.Blocks.STONE_BRICKS.defaultBlockState(),
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<Block> EARTH_MOSAIC_BORDER = register("earth_mosaic_border",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> EARTH_MOSAIC_GEOMETRIC = register("earth_mosaic_geometric",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> EARTH_MOSAIC_PATTERN = register("earth_mosaic_pattern",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> EARTH_MOSAIC_DELICATE = register("earth_mosaic_delicate",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> EARTH_MOSAIC_TRADITIONAL = register("earth_mosaic_traditional",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> FIRE_MOSAIC_BORDER = register("fire_mosaic_border",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> FIRE_MOSAIC_GEOMETRIC = register("fire_mosaic_geometric",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> FIRE_MOSAIC_PATTERN = register("fire_mosaic_pattern",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> FIRE_MOSAIC_DELICATE = register("fire_mosaic_delicate",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> FIRE_MOSAIC_TRADITIONAL = register("fire_mosaic_traditional",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SPIRIT_MOSAIC_BORDER = register("spirit_mosaic_border",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SPIRIT_MOSAIC_GEOMETRIC = register("spirit_mosaic_geometric",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SPIRIT_MOSAIC_PATTERN = register("spirit_mosaic_pattern",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SPIRIT_MOSAIC_DELICATE = register("spirit_mosaic_delicate",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> SPIRIT_MOSAIC_TRADITIONAL = register("spirit_mosaic_traditional",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> AIR_MOSAIC_BORDER = register("air_mosaic_border",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> AIR_MOSAIC_GEOMETRIC = register("air_mosaic_geometric",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> AIR_MOSAIC_PATTERN = register("air_mosaic_pattern",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> AIR_MOSAIC_DELICATE = register("air_mosaic_delicate",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> AIR_MOSAIC_TRADITIONAL = register("air_mosaic_traditional",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> MOSAIC_FLOOR = register("mosaic_floor",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> MOSAIC_FLOOR_DELICATE = register("mosaic_floor_delicate",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> MOSAIC_FLOOR_ROSETTE = register("mosaic_floor_rosette",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ROMAN_FRESCO_RED = register("roman_fresco_red",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));
    public static final DeferredBlock<Block> ROMAN_FRESCO_BLACK = register("roman_fresco_black",
            () -> new Block(Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE)));

    // -------------------------------------------------------------------------
    // --- Stone brick functional blocks ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<com.otterly76.ott.block.custom.ArrowslitBlock> STONE_BRICKS_ARROWSLIT = register("stone_bricks_arrowslit",
            () -> new com.otterly76.ott.block.custom.ArrowslitBlock(
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.MachincolationBlock> STONE_BRICKS_MACHICOLATION = register("stone_bricks_machicolation",
            () -> new com.otterly76.ott.block.custom.MachincolationBlock(
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.WaterTrickleSourceBlock> WATER_SOURCE_TRICKLE = register("water_source_trickle",
            () -> new com.otterly76.ott.block.custom.WaterTrickleSourceBlock(
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.FaucetBlock> STONE_BRICKS_FAUCET = register("stone_bricks_faucet",
            () -> new com.otterly76.ott.block.custom.FaucetBlock(
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.PoolBlock> STONE_BRICKS_POOL = register("stone_bricks_pool",
            () -> new com.otterly76.ott.block.custom.PoolBlock(
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.SmallPoolBlock> STONE_BRICKS_SMALL_POOL = register("stone_bricks_small_pool",
            () -> new com.otterly76.ott.block.custom.SmallPoolBlock(
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.WaterJetBlock> STONE_BRICKS_WATER_JET = register("stone_bricks_water_jet",
            () -> new com.otterly76.ott.block.custom.WaterJetBlock(
                    Properties.of().strength(1.5f).requiresCorrectToolForDrops().sound(SoundType.STONE).noOcclusion()));

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Limestone ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block> LIMESTONE_MASONRY = register("limestone_masonry",
            () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<EdgeBlock> LIMESTONE_MASONRY_EDGE = register("limestone_masonry_edge",
            () -> new EdgeBlock(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<PlateBlock> LIMESTONE_MASONRY_PLATE = register("limestone_masonry_plate",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<PlateBlock> LIMESTONE_BANNISTER = register("limestone_bannister",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion()));
    public static final DeferredBlock<Block> COBBLED_LIMESTONE = register("cobbled_limestone",
            () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> PLAIN_LIMESTONE = register("limestone",
            () -> new Block(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> MIXED_LIMESTONE_BRICKS = register("mixed_limestone_bricks",
            () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Marble (Roman) ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block> BLACK_MARBLE              = register("black_marble",              () -> new Block(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> BLACK_MARBLE_BRICKS       = register("black_marble_bricks",       () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> BLACK_MARBLE_SMALL_BRICKS = register("black_marble_small_bricks", () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> BLACK_MARBLE_TILES        = register("black_marble_tiles",        () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> BLACK_POLISHED_MARBLE     = register("black_polished_marble",     () -> new Block(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> BLACK_MARBLE_PILLAR     = register("black_marble_pillar",     () -> new RotatedPillarBlock(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> BLACK_MARBLE_PILLAR_CAP = register("black_marble_pillar_cap", () -> new RotatedPillarBlock(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> WHITE_MARBLE              = register("white_marble",              () -> new Block(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<Block> WHITE_MARBLE_BRICKS       = register("white_marble_bricks",       () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> WHITE_MARBLE_SMALL_BRICKS = register("white_marble_small_bricks", () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> WHITE_MARBLE_TILES        = register("white_marble_tiles",        () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<Block> WHITE_POLISHED_MARBLE     = register("white_polished_marble",     () -> new Block(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> WHITE_MARBLE_PILLAR     = register("white_marble_pillar",     () -> new RotatedPillarBlock(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<RotatedPillarBlock> WHITE_MARBLE_PILLAR_CAP  = register("white_marble_pillar_cap",  () -> new RotatedPillarBlock(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<TileBlock> BLACK_MARBLE_FLOOR_TILE = register("black_marble_floor_tile", () -> new TileBlock(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<TileBlock> WHITE_MARBLE_FLOOR_TILE = register("white_marble_floor_tile", () -> new TileBlock(Properties.ofFullCopy(Blocks.STONE)));
    public static final DeferredBlock<PlateBlock> WHITE_MARBLE_FANCY_FENCE = register("white_marble_fancy_fence",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.STONE).strength(3.0f, 5.0f).noOcclusion()));
    public static final DeferredBlock<PlateBlock> BLACK_MARBLE_FANCY_FENCE = register("black_marble_fancy_fence",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.STONE).strength(3.0f, 5.0f).noOcclusion()));

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Sandstone decorative (Roman) ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<PlateBlock> SANDSTONE_PLATE = register("sandstone_plate",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<EdgeBlock> SANDSTONE_EDGE = register("sandstone_edge",
            () -> new EdgeBlock(Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<PlateBlock> SANDSTONE_CRENELATION = register("sandstone_crenelation",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.CUT_SANDSTONE)));
    public static final DeferredBlock<PlateBlock> CUT_SANDSTONE_PLATE = register("cut_sandstone_plate",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.CUT_SANDSTONE)));
    public static final DeferredBlock<EdgeBlock> CUT_SANDSTONE_EDGE = register("cut_sandstone_edge",
            () -> new EdgeBlock(Properties.ofFullCopy(Blocks.CUT_SANDSTONE)));
    public static final DeferredBlock<PlateBlock> SMOOTH_SANDSTONE_PLATE = register("smooth_sandstone_plate",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.SMOOTH_SANDSTONE)));
    public static final DeferredBlock<EdgeBlock> SMOOTH_SANDSTONE_EDGE = register("smooth_sandstone_edge",
            () -> new EdgeBlock(Properties.ofFullCopy(Blocks.SMOOTH_SANDSTONE)));
    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Flat/Gray Roof Tiles + Roofing Slates (General) ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block> ROOFING_SLATES = register("roofing_slates",
            () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<EdgeBlock> ROOFING_SLATES_EDGE = register("roofing_slates_edge",
            () -> new EdgeBlock(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<PlateBlock> ROOFING_SLATES_PLATE = register("roofing_slates_plate",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.STONE_BRICKS)));

    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Rammed Dirt, Stepping Stones (General) ---
    // -------------------------------------------------------------------------
    // -------------------------------------------------------------------------
    // --- DoTB Phase 2: Thatch (General) ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block> WHEAT_THATCH = register("wheat_thatch",
            () -> new Block(Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.COLOR_YELLOW).strength(1.0f).sound(SoundType.GRASS)));
    public static final DeferredBlock<EdgeBlock> WHEAT_THATCH_EDGE = register("wheat_thatch_edge",
            () -> new EdgeBlock(Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.COLOR_YELLOW).strength(1.0f).sound(SoundType.GRASS)));
    public static final DeferredBlock<PlateBlock> WHEAT_THATCH_PLATE = register("wheat_thatch_plate",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.COLOR_YELLOW).strength(1.0f).sound(SoundType.GRASS)));
    public static final DeferredBlock<Block> BAMBOO_THATCH = register("bamboo_thatch",
            () -> new Block(Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.COLOR_YELLOW).strength(1.0f).sound(SoundType.GRASS)));
    public static final DeferredBlock<EdgeBlock> BAMBOO_THATCH_EDGE = register("bamboo_thatch_edge",
            () -> new EdgeBlock(Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.COLOR_YELLOW).strength(1.0f).sound(SoundType.GRASS)));
    public static final DeferredBlock<PlateBlock> BAMBOO_THATCH_PLATE = register("bamboo_thatch_plate",
            () -> new PlateBlock(Properties.ofFullCopy(Blocks.GRASS_BLOCK).mapColor(MapColor.COLOR_YELLOW).strength(1.0f).sound(SoundType.GRASS)));


    // -------------------------------------------------------------------------
    // --- DoTB Phase 3: Stone Bricks Masonry + German misc (German) ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block>      STONE_BRICKS_MASONRY        = register("stone_bricks_masonry",        () -> new Block(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<com.otterly76.ott.block.custom.EdgeBlock>  STONE_BRICKS_MASONRY_EDGE  = register("stone_bricks_masonry_edge",  () -> new com.otterly76.ott.block.custom.EdgeBlock(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<com.otterly76.ott.block.custom.PlateBlock> STONE_BRICKS_MASONRY_PLATE = register("stone_bricks_masonry_plate", () -> new com.otterly76.ott.block.custom.PlateBlock(Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final DeferredBlock<RakedGravelBlock> CURVED_RAKED_GRAVEL   = register("curved_raked_gravel",   () -> new RakedGravelBlock(true,  Properties.ofFullCopy(Blocks.GRAVEL)));
    public static final DeferredBlock<RakedGravelBlock> STRAIGHT_RAKED_GRAVEL = register("straight_raked_gravel", () -> new RakedGravelBlock(false, Properties.ofFullCopy(Blocks.GRAVEL)));


    // -------------------------------------------------------------------------
    // --- Sandstone Slender ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block>      SANDSTONE_SLENDER_BRICKS                   = register("sandstone_slender_bricks",                   () -> new Block(Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<WallBlock>  SANDSTONE_SLENDER_BRICKS_WALL              = register("sandstone_slender_bricks_wall",              () -> new WallBlock(Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<EdgeBlock>  SANDSTONE_SLENDER_BRICKS_EDGE              = register("sandstone_slender_bricks_edge",              () -> new EdgeBlock(Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<PlateBlock> SANDSTONE_SLENDER_BRICKS_PLATE             = register("sandstone_slender_bricks_plate",             () -> new PlateBlock(Properties.ofFullCopy(Blocks.SANDSTONE)));

    public static final DeferredBlock<Block>      SANDSTONE_SLENDER_TURQUOISE_PATTERN        = register("sandstone_slender_turquoise_pattern",        () -> new Block(Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<WallBlock>  SANDSTONE_SLENDER_TURQUOISE_PATTERN_WALL   = register("sandstone_slender_turquoise_pattern_wall",   () -> new WallBlock(Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<EdgeBlock>  SANDSTONE_SLENDER_TURQUOISE_PATTERN_EDGE   = register("sandstone_slender_turquoise_pattern_edge",   () -> new EdgeBlock(Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<PlateBlock> SANDSTONE_SLENDER_TURQUOISE_PATTERN_PLATE  = register("sandstone_slender_turquoise_pattern_plate",  () -> new PlateBlock(Properties.ofFullCopy(Blocks.SANDSTONE)));

    // -------------------------------------------------------------------------
    // --- Ornamented Carpets + Wool ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block>       ORNAMENTED_RED_WOOL         = register("ornamented_red_wool",       () -> new Block(Properties.of().strength(0.8F).sound(SoundType.WOOL)));
    public static final DeferredBlock<Block>       DELICATE_RED_WOOL           = register("delicate_red_wool",         () -> new Block(Properties.of().strength(0.8F).sound(SoundType.WOOL)));
    public static final DeferredBlock<CarpetBlock> ORNAMENTED_RED_CARPET       = register("ornamented_red_carpet",     () -> new CarpetBlock(Properties.of().strength(0.1F).sound(SoundType.WOOL)));
    public static final DeferredBlock<CarpetBlock> DELICATE_RED_CARPET         = register("delicate_red_carpet",       () -> new CarpetBlock(Properties.of().strength(0.1F).sound(SoundType.WOOL)));
    public static final DeferredBlock<Block>       ORNAMENTED_BLUE_WOOL        = register("ornamented_blue_wool",      () -> new Block(Properties.of().strength(0.8F).sound(SoundType.WOOL)));
    public static final DeferredBlock<Block>       DELICATE_BLUE_WOOL          = register("delicate_blue_wool",        () -> new Block(Properties.of().strength(0.8F).sound(SoundType.WOOL)));
    public static final DeferredBlock<CarpetBlock> ORNAMENTED_BLUE_CARPET      = register("ornamented_blue_carpet",    () -> new CarpetBlock(Properties.of().strength(0.1F).sound(SoundType.WOOL)));
    public static final DeferredBlock<CarpetBlock> DELICATE_BLUE_CARPET        = register("delicate_blue_carpet",      () -> new CarpetBlock(Properties.of().strength(0.1F).sound(SoundType.WOOL)));
    public static final DeferredBlock<Block>       ORNAMENTED_GREEN_WOOL       = register("ornamented_green_wool",     () -> new Block(Properties.of().strength(0.8F).sound(SoundType.WOOL)));
    public static final DeferredBlock<Block>       DELICATE_GREEN_WOOL         = register("delicate_green_wool",       () -> new Block(Properties.of().strength(0.8F).sound(SoundType.WOOL)));
    public static final DeferredBlock<CarpetBlock> ORNAMENTED_GREEN_CARPET     = register("ornamented_green_carpet",   () -> new CarpetBlock(Properties.of().strength(0.1F).sound(SoundType.WOOL)));
    public static final DeferredBlock<CarpetBlock> DELICATE_GREEN_CARPET       = register("delicate_green_carpet",     () -> new CarpetBlock(Properties.of().strength(0.1F).sound(SoundType.WOOL)));
    public static final DeferredBlock<Block>       ORNAMENTED_PURPLE_WOOL      = register("ornamented_purple_wool",    () -> new Block(Properties.of().strength(0.8F).sound(SoundType.WOOL)));
    public static final DeferredBlock<Block>       DELICATE_PURPLE_WOOL        = register("delicate_purple_wool",      () -> new Block(Properties.of().strength(0.8F).sound(SoundType.WOOL)));
    public static final DeferredBlock<CarpetBlock> ORNAMENTED_PURPLE_CARPET    = register("ornamented_purple_carpet",  () -> new CarpetBlock(Properties.of().strength(0.1F).sound(SoundType.WOOL)));
    public static final DeferredBlock<CarpetBlock> DELICATE_PURPLE_CARPET      = register("delicate_purple_carpet",    () -> new CarpetBlock(Properties.of().strength(0.1F).sound(SoundType.WOOL)));

    // -------------------------------------------------------------------------
    // --- DoTB Phase 4: Gold Plated Smooth (Persian) ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<Block>      GOLD_PLATED_SMOOTH_BLOCK   = register("gold_plated_smooth_block",   () -> new Block(Properties.ofFullCopy(Blocks.GOLD_BLOCK)));
    public static final DeferredBlock<EdgeBlock>  GOLD_PLATED_SMOOTH_EDGE    = register("gold_plated_smooth_edge",    () -> new EdgeBlock(Properties.ofFullCopy(Blocks.GOLD_BLOCK)));
    public static final DeferredBlock<PlateBlock> GOLD_PLATED_SMOOTH_PLATE   = register("gold_plated_smooth_plate",   () -> new PlateBlock(Properties.ofFullCopy(Blocks.GOLD_BLOCK)));

    // -------------------------------------------------------------------------
    // --- Oak structural blocks ---
    // -------------------------------------------------------------------------
    public static final DeferredBlock<com.otterly76.ott.block.custom.PergolaBlock> OAK_PERGOLA = register("oak_pergola",
            () -> new com.otterly76.ott.block.custom.PergolaBlock(
                    Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.BeamBlock> OAK_BEAM = register("oak_beam",
            () -> new com.otterly76.ott.block.custom.BeamBlock(
                    Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.PlateBlock> OAK_PLANKS_PLATE = register("oak_planks_plate",
            () -> new com.otterly76.ott.block.custom.PlateBlock(
                    Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.EdgeBlock> OAK_PLANKS_EDGE = register("oak_planks_edge",
            () -> new com.otterly76.ott.block.custom.EdgeBlock(
                    Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.BannisterBlock> OAK_BANNISTER = register("oak_bannister",
            () -> new com.otterly76.ott.block.custom.BannisterBlock(
                    Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.SupportSlabBlock> OAK_SUPPORT_SLAB = register("oak_support_slab",
            () -> new com.otterly76.ott.block.custom.SupportSlabBlock(
                    Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<com.otterly76.ott.block.custom.SupportBeamBlock> OAK_SUPPORT_BEAM = register("oak_support_beam",
            () -> new com.otterly76.ott.block.custom.SupportBeamBlock(
                    Properties.of().strength(2.0f).sound(SoundType.WOOD).noOcclusion()));
    public static final DeferredBlock<Block> OAK_GEOMETRIC_WINDOW = register("oak_geometric_window",
            () -> new com.otterly76.ott.block.custom.GeometricWindowBlock(Properties.of().strength(1.5f).sound(SoundType.WOOD).noOcclusion()));
    // =========================================================================
    // === DoTB Phase 5: General Decorative & Functional ===
    // =========================================================================
    public static final DeferredBlock<PlacedLanternBlock>    STONE_LANTERN                   = register("stone_lantern",                   () -> new PlacedLanternBlock(Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion().lightLevel(s -> 15)));
    public static final DeferredBlock<LitPlacedLanternBlock> IRON_FANCY_LANTERN             = register("iron_fancy_lantern",              () -> new LitPlacedLanternBlock(Properties.of().strength(3.5f).sound(SoundType.METAL).requiresCorrectToolForDrops().noOcclusion().lightLevel(s -> s.getValue(LitPlacedLanternBlock.LIT) ? 15 : 0)));
    public static final DeferredBlock<StarlightLampBlock>    STARLIGHT_LAMP                  = register("starlight_lamp",                  () -> new StarlightLampBlock(Properties.of().strength(0.5F).sound(SoundType.GLASS).noOcclusion().lightLevel(s -> 15)));

    // =========================================================================
    // === DoTB Phase 2: Roman Marble extras ===
    // =========================================================================

    // =========================================================================
    // === DoTB Phase 2: Roman Birch Furniture ===
    // =========================================================================

    // =========================================================================
    // === DoTB Phase 3: German Waxed Oak extras ===
    // =========================================================================

    // =========================================================================
    // === DoTB Phase 3: Japanese Spruce extras ===
    // =========================================================================
    public static final DeferredBlock<FenceBlock>            SPRUCE_LOG_FENCE                = register("spruce_log_fence",                () -> new FenceBlock(Properties.ofFullCopy(Blocks.SPRUCE_PLANKS)));

    // =========================================================================
    // === DoTB Phase 3: Japanese Bamboo extras ===
    // =========================================================================

    // =========================================================================
    // === DoTB Phase 3: Japanese Furniture & Decor ===
    // =========================================================================


    // =========================================================================
    // === Stone Variant Blocks (simple CTM — Chipped style) ===
    // =========================================================================
    private static final Properties ST = Properties.ofFullCopy(Blocks.STONE);

    // --- Plain cube_all ---
    public static final DeferredBlock<Block> ANGRY_STONE                         = register("angry_stone",                         () -> new Block(ST));
    public static final DeferredBlock<Block> BLANK_STONE_CARVING                 = register("blank_stone_carving",                 () -> new Block(ST));
    public static final DeferredBlock<Block> BRICK_BORDERED_STONE                = register("brick_bordered_stone",                () -> new Block(ST));
    public static final DeferredBlock<Block> CARVED_STONE                        = register("carved_stone",                        () -> new Block(ST));
    public static final DeferredBlock<Block> CHECKERED_STONE_TILES               = register("checkered_stone_tiles",               () -> new Block(ST));
    public static final DeferredBlock<Block> COBBLED_STONE                       = register("cobbled_stone",                       () -> new Block(ST));
    public static final DeferredBlock<Block> CRACKED_DISORDERED_STONE_BRICKS     = register("cracked_disordered_stone_bricks",     () -> new Block(ST));
    public static final DeferredBlock<Block> CRACKED_FLAT_STONE_TILES            = register("cracked_flat_stone_tiles",            () -> new Block(ST));
    public static final DeferredBlock<Block> CREEPER_STONE_CARVING               = register("creeper_stone_carving",               () -> new Block(ST));
    public static final DeferredBlock<Block> CRYING_STONE                        = register("crying_stone",                        () -> new Block(ST));
    public static final DeferredBlock<Block> CURLY_STONE_PILLAR                  = register("curly_stone_pillar",                  () -> new Block(ST));
    public static final DeferredBlock<Block> CUT_BLANK_STONE                     = register("cut_blank_stone",                     () -> new Block(ST));
    public static final DeferredBlock<Block> DUH_STONE                           = register("duh_stone",                           () -> new Block(ST));
    public static final DeferredBlock<Block> ENGRAVED_STONE                      = register("engraved_stone",                      () -> new Block(ST));
    public static final DeferredBlock<Block> ETCHED_STONE_BRICKS                 = register("etched_stone_bricks",                 () -> new Block(ST));
    public static final DeferredBlock<Block> FINE_STONE_PILLAR                   = register("fine_stone_pillar",                   () -> new Block(ST));
    public static final DeferredBlock<Block> FLAT_STONE_TILES                    = register("flat_stone_tiles",                    () -> new Block(ST));
    public static final DeferredBlock<Block> GLAD_STONE                          = register("glad_stone",                          () -> new Block(ST));
    public static final DeferredBlock<Block> INLAYED_STONE                       = register("inlayed_stone",                       () -> new Block(ST));
    public static final DeferredBlock<Block> INSCRIBED_STONE                     = register("inscribed_stone",                     () -> new Block(ST));
    public static final DeferredBlock<Block> LAYED_STONE_BRICKS                  = register("layed_stone_bricks",                  () -> new Block(ST));
    public static final DeferredBlock<Block> LODED_STONE                         = register("loded_stone",                         () -> new Block(ST));
    public static final DeferredBlock<Block> OFFSET_STONE_BRICKS                 = register("offset_stone_bricks",                 () -> new Block(ST));
    public static final DeferredBlock<Block> ORNATE_STONE_PILLAR                 = register("ornate_stone_pillar",                 () -> new Block(ST));
    public static final DeferredBlock<Block> OVERLAPPING_STONE_TILES             = register("overlapping_stone_tiles",             () -> new Block(ST));
    public static final DeferredBlock<Block> PILLAR_STONE_BRICKS                 = register("pillar_stone_bricks",                 () -> new Block(ST));
    public static final DeferredBlock<Block> POLISHED_STONE                      = register("polished_stone",                      () -> new Block(ST));
    public static final DeferredBlock<Block> PRISMAL_STONE_REMNANTS              = register("prismal_stone_remnants",              () -> new Block(ST));
    public static final DeferredBlock<Block> ROUGH_STONE                         = register("rough_stone",                         () -> new Block(ST));
    public static final DeferredBlock<Block> ROUNDED_STONE_BRICKS                = register("rounded_stone_bricks",                () -> new Block(ST));
    public static final DeferredBlock<Block> RUNIC_CARVED_STONE                  = register("runic_carved_stone",                  () -> new Block(ST));
    public static final DeferredBlock<Block> SAD_STONE                           = register("sad_stone",                           () -> new Block(ST));
    public static final DeferredBlock<Block> SANDED_STONE                        = register("sanded_stone",                        () -> new Block(ST));
    public static final DeferredBlock<Block> SIMPLE_STONE_PILLAR                 = register("simple_stone_pillar",                 () -> new Block(ST));
    public static final DeferredBlock<Block> SMALL_STONE_BRICKS                  = register("small_stone_bricks",                  () -> new Block(ST));
    public static final DeferredBlock<Block> SMOOTH_INLAYED_STONE                = register("smooth_inlayed_stone",                () -> new Block(ST));
    public static final DeferredBlock<Block> SMOOTHED_DOUBLE_INLAYED_STONE       = register("smoothed_double_inlayed_stone",       () -> new Block(ST));
    public static final DeferredBlock<Block> SPIDER_STONE_CARVING                = register("spider_stone_carving",                () -> new Block(ST));
    public static final DeferredBlock<Block> SPIRALED_STONE                      = register("spiraled_stone",                      () -> new Block(ST));
    public static final DeferredBlock<Block> STACKED_STONE_BRICKS                = register("stacked_stone_bricks",                () -> new Block(ST));
    public static final DeferredBlock<Block> STONE_MINI_TILES                    = register("stone_mini_tiles",                    () -> new Block(ST));
    public static final DeferredBlock<Block> STONE_SCALES                        = register("stone_scales",                        () -> new Block(ST));
    public static final DeferredBlock<Block> THICK_INLAYED_STONE                 = register("thick_inlayed_stone",                 () -> new Block(ST));
    public static final DeferredBlock<Block> TILED_BORDERED_STONE                = register("tiled_bordered_stone",                () -> new Block(ST));
    public static final DeferredBlock<Block> TILED_STONE                         = register("tiled_stone",                         () -> new Block(ST));
    public static final DeferredBlock<Block> TINY_BRICK_BORDERED_STONE           = register("tiny_brick_bordered_stone",           () -> new Block(ST));
    public static final DeferredBlock<Block> TINY_LAYERED_STONE_BRICKS           = register("tiny_layered_stone_bricks",           () -> new Block(ST));
    public static final DeferredBlock<Block> TINY_LAYERED_STONE_SLABS            = register("tiny_layered_stone_slabs",            () -> new Block(ST));
    public static final DeferredBlock<Block> TINY_STONE_BRICKS                   = register("tiny_stone_bricks",                   () -> new Block(ST));
    public static final DeferredBlock<Block> TRODDEN_STONE                       = register("trodden_stone",                       () -> new Block(ST));
    public static final DeferredBlock<Block> UNAMUSED_STONE                      = register("unamused_stone",                      () -> new Block(ST));
    public static final DeferredBlock<Block> VERTICAL_CUT_STONE                  = register("vertical_cut_stone",                  () -> new Block(ST));
    public static final DeferredBlock<Block> VERTICAL_DISORDERED_STONE_BRICKS    = register("vertical_disordered_stone_bricks",    () -> new Block(ST));
    public static final DeferredBlock<Block> WEATHERED_STONE                     = register("weathered_stone",                     () -> new Block(ST));

    // --- Column (RotatedPillarBlock) ---
    public static final DeferredBlock<RotatedPillarBlock> STONE_PILLAR           = register("stone_pillar",                        () -> new RotatedPillarBlock(ST));

    // --- Static decorative (plain cube_all, future CTM cousins pending) ---
    public static final DeferredBlock<Block>              BORDERED_STONE         = register("bordered_stone",      () -> new Block(ST));
    public static final DeferredBlock<Block>              EDGED_STONE_BRICKS     = register("edged_stone_bricks",  () -> new Block(ST));
    public static final DeferredBlock<Block>              MASSIVE_STONE_BRICKS   = register("massive_stone_bricks",() -> new Block(ST));
    public static final DeferredBlock<RotatedPillarBlock> CUT_STONE_COLUMN       = register("cut_stone_column",    () -> new RotatedPillarBlock(ST));
    public static final DeferredBlock<RotatedPillarBlock> SMOOTH_STONE_COLUMN    = register("smooth_stone_column", () -> new RotatedPillarBlock(ST));
    public static final DeferredBlock<RotatedPillarBlock> TILED_STONE_COLUMN     = register("tiled_stone_column",  () -> new RotatedPillarBlock(ST));
    public static final DeferredBlock<Block> PURPUR_PILLAR_CTM                   = register("purpur_pillar",            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPUR_BLOCK)));
    public static final DeferredBlock<Block> SANDSTONE_CTM                       = register("sandstone",                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SANDSTONE)));
    public static final DeferredBlock<Block> RED_SANDSTONE_CTM                   = register("red_sandstone",            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_SANDSTONE)));
    public static final DeferredBlock<Block> POLISHED_ANDESITE_CTM               = register("polished_andesite",        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_ANDESITE)));
    public static final DeferredBlock<Block> POLISHED_BLACKSTONE_CTM             = register("polished_blackstone",      () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_BLACKSTONE)));
    public static final DeferredBlock<Block> POLISHED_DIORITE_CTM                = register("polished_diorite",         () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_DIORITE)));
    public static final DeferredBlock<Block> POLISHED_GRANITE_CTM                = register("polished_granite",         () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.POLISHED_GRANITE)));
    public static final DeferredBlock<Block> NETHERITE_BLOCK_CTM                 = register("netherite_block",          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));
    public static final DeferredBlock<Block> SMOOTH_STONE_CTM                    = register("smooth_stone",             () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOOTH_STONE)));
    public static final DeferredBlock<RotatedPillarBlock> CHISELED_PLASTERED_STONE_PILLAR = register("chiseled_plastered_stone_pillar", () -> new RotatedPillarBlock(ST));
    // CTM vertical pillars
    public static final DeferredBlock<Block> BONE_BLOCK_PILLAR        = register("bone_block_pillar",        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BONE_BLOCK)));
    public static final DeferredBlock<Block> COAL_BLOCK_PILLAR        = register("coal_block_pillar",        () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COAL_BLOCK)));
    public static final DeferredBlock<Block> COBBLED_DEEPSLATE_PILLAR = register("cobbled_deepslate_pillar", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final DeferredBlock<Block> COBBLESTONE_PILLAR       = register("cobblestone_pillar",       () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE)));
    public static final DeferredBlock<Block> COPPER_BLOCK_PILLAR      = register("copper_block_pillar",      () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WAXED_COPPER_BLOCK)));
    public static final DeferredBlock<Block> LAPIS_BLOCK_PILLAR       = register("lapis_block_pillar",       () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.LAPIS_BLOCK)));
    public static final DeferredBlock<Block> NETHERITE_BLOCK_PILLAR   = register("netherite_block_pillar",   () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.NETHERITE_BLOCK)));
    public static final DeferredBlock<Block> OBSIDIAN_PILLAR          = register("obsidian_pillar",          () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OBSIDIAN)));

    public static void register(IEventBus eventBus) {
        registerDynamicBlocks();
        BLOCKS.register(eventBus);
        MINECRAFT_BLOCKS.register(eventBus);
        MINECRAFT_ITEMS.register(eventBus);
    }


    @FunctionalInterface
    private interface GradientBlockBuilder<T extends Block & IGradientBlock> {
        T create(Properties properties, DyeColor firstColor, DyeColor secondColor, Function<DyeColor, String> textureNameMapper);
    }
}