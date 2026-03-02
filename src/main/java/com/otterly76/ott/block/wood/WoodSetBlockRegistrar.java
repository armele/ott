package com.otterly76.ott.block.wood;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.util.block.BlockSetTypeVariant;
import com.otterly76.ott.util.block.WoodTypeVariant;
import com.otterly76.ott.worldgen.ModTreeGrowers;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import static com.otterly76.ott.block.ModBlocks.BLOCKS;

public class WoodSetBlockRegistrar {
    public static ModBlocks.WoodSetBlocks registerOttWoodSet(String set) {
        // Naming
        String logName = set + "_log";
        String woodName = set + "_wood";
        String strippedLogName = "stripped_" + set + "_log";
        String strippedWoodName = "stripped_" + set + "_wood";
        String planksName = set + "_planks";
        String stairsName = set + "_stairs";
        String slabName = set + "_slab";
        String fenceName = set + "_fence";
        String fenceGateName = set + "_fence_gate";
        String doorName = set + "_door";
        String trapdoorName = set + "_trapdoor";
        String buttonName = set + "_button";
        String pressurePlateName = set + "_pressure_plate";
        String leavesName = set + "_leaves";
        String saplingName = set + "_sapling";

        String signName = set + "_sign";
        String wallSignName = set + "_wall_sign";
        String hangingSignName = set + "_hanging_sign";
        String wallHangingSignName = set + "_wall_hanging_sign";

        // Blocks
        DeferredBlock<RotatedPillarBlock> log = BLOCKS.register(logName,
                () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)));

        DeferredBlock<RotatedPillarBlock> wood = BLOCKS.register(woodName,
                () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)));

        DeferredBlock<RotatedPillarBlock> strippedLog = BLOCKS.register(strippedLogName,
                () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)));

        DeferredBlock<RotatedPillarBlock> strippedWood = BLOCKS.register(strippedWoodName,
                () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)));

        DeferredBlock<Block> planks = BLOCKS.register(planksName,
                () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

        DeferredBlock<StairBlock> stairs = BLOCKS.register(stairsName,
                () -> new StairBlock(planks.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));

        DeferredBlock<SlabBlock> slab = BLOCKS.register(slabName,
                () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

        DeferredBlock<FenceBlock> fence = BLOCKS.register(fenceName,
                () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)));

        DeferredBlock<FenceGateBlock> fenceGate = BLOCKS.register(fenceGateName,
                () -> new FenceGateBlock(
                        WoodTypeVariant.ott(set),
                        BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)));

        DeferredBlock<DoorBlock> door = BLOCKS.register(doorName,
                () -> new DoorBlock(BlockSetTypeVariant.ott(set), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)));

        DeferredBlock<TrapDoorBlock> trapdoor = BLOCKS.register(trapdoorName,
                () -> new TrapDoorBlock(BlockSetTypeVariant.ott(set), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)));

        DeferredBlock<ButtonBlock> button = BLOCKS.register(buttonName,
                () -> new ButtonBlock(BlockSetTypeVariant.ott(set), 30, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)));

        DeferredBlock<PressurePlateBlock> pressurePlate = BLOCKS.register(pressurePlateName,
                () -> new PressurePlateBlock(BlockSetTypeVariant.ott(set), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)));

        DeferredBlock<LeavesBlock> leaves = BLOCKS.register(leavesName,
                () -> new LeavesBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LEAVES)));

        DeferredBlock<SaplingBlock> sapling = BLOCKS.register(saplingName,
                () -> new SaplingBlock(ModTreeGrowers.OAK, BlockBehaviour.Properties.of().strength(4.0f).sound(SoundType.WOOD).noOcclusion()));

        DeferredBlock<FlowerPotBlock> pottedSapling = BLOCKS.register("potted_" + saplingName,
                () -> new FlowerPotBlock(() -> (FlowerPotBlock) Blocks.FLOWER_POT, sapling, BlockBehaviour.Properties.of().strength(4.0f).sound(SoundType.WOOD).noOcclusion()));

        // Signs (blocks only; the item is registered in ModItems)
        DeferredBlock<StandingSignBlock> sign = BLOCKS.register(signName,
                () -> new StandingSignBlock(WoodTypeVariant.ott(set), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)));

        DeferredBlock<WallSignBlock> wallSign = BLOCKS.register(wallSignName,
                () -> new WallSignBlock(WoodTypeVariant.ott(set), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN)));

        DeferredBlock<CeilingHangingSignBlock> hangingSign = BLOCKS.register(hangingSignName,
                () -> new CeilingHangingSignBlock(WoodTypeVariant.ott(set), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN)));

        DeferredBlock<WallHangingSignBlock> wallHangingSign = BLOCKS.register(wallHangingSignName,
                () -> new WallHangingSignBlock(WoodTypeVariant.ott(set), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN)));

        return new ModBlocks.WoodSetBlocks(
                log, wood, strippedLog, strippedWood,
                planks, stairs, slab,
                fence, fenceGate,
                door, trapdoor,
                button, pressurePlate,
                leaves,
                sapling,
                pottedSapling,
                sign, wallSign, hangingSign, wallHangingSign
        );
    }
}
