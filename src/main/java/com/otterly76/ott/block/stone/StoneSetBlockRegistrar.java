package com.otterly76.ott.block.stone;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.block.custom.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;

import static com.otterly76.ott.block.ModBlocks.BLOCKS;

public class StoneSetBlockRegistrar {

    public static ModBlocks.StoneSetBlocks registerStoneSet(ModStoneVariants.StoneVariant v) {
        String n = v.name();

        DeferredBlock<PlateBlock> plate = BLOCKS.register(n + "_plate",
                () -> new PlateBlock(BlockBehaviour.Properties.ofFullCopy(v.propsSource().get()).noOcclusion()));

        DeferredBlock<EdgeBlock> edge = BLOCKS.register(n + "_edge",
                () -> new EdgeBlock(BlockBehaviour.Properties.ofFullCopy(v.propsSource().get()).noOcclusion()));

        DeferredBlock<BeamBlock> beam = BLOCKS.register(n + "_beam",
                () -> new BeamBlock(BlockBehaviour.Properties.ofFullCopy(v.propsSource().get()).noOcclusion()));

        DeferredBlock<PergolaBlock> pergola = BLOCKS.register(n + "_pergola",
                () -> new PergolaBlock(BlockBehaviour.Properties.ofFullCopy(v.propsSource().get()).noOcclusion()));

        DeferredBlock<GeometricWindowBlock> geometricWindow = BLOCKS.register(n + "_geometric_window",
                () -> new GeometricWindowBlock(BlockBehaviour.Properties.ofFullCopy(v.propsSource().get()).noOcclusion()));

        DeferredBlock<BannisterBlock> bannister = BLOCKS.register(n + "_bannister",
                () -> new BannisterBlock(BlockBehaviour.Properties.ofFullCopy(v.propsSource().get()).noOcclusion()));

        DeferredBlock<SupportSlabBlock> supportSlab = BLOCKS.register(n + "_support_slab",
                () -> new SupportSlabBlock(BlockBehaviour.Properties.ofFullCopy(v.propsSource().get()).noOcclusion()));

        DeferredBlock<SupportBeamBlock> supportBeam = BLOCKS.register(n + "_support_beam",
                () -> new SupportBeamBlock(BlockBehaviour.Properties.ofFullCopy(v.propsSource().get()).noOcclusion()));

        return new ModBlocks.StoneSetBlocks(plate, edge, beam, pergola, geometricWindow, bannister, supportSlab, supportBeam);
    }
}
