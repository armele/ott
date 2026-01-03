package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.client.NutritionHudOverlay;
import com.otterly76.ott.client.gui.TrashScreen;
import com.otterly76.ott.client.render.PrismaticColorHandler;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.entity.client.CreakingRenderer;
import com.otterly76.ott.entity.client.ModModelLayers;
import com.otterly76.ott.entity.client.OttWoodSetBoatRenderer;
import com.otterly76.ott.entity.client.PaleOakBoatRenderer;
import com.otterly76.ott.inventory.ModMenuTypes;
import com.otterly76.ott.particle.*;
import com.otterly76.ott.util.WoodTypeVariant;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.awt.*;

@SuppressWarnings("MethodRefCanBeReplacedWithLambda")
public class ClientModEvents {

    public static void register(IEventBus modBus) {
        modBus.addListener(ClientModEvents::registerGuiLayers);
        modBus.addListener(ClientModEvents::onClientSetup);
        modBus.addListener(ClientModEvents::registerParticleFactories);
        modBus.addListener(ClientModEvents::registerBlockColors);
        modBus.addListener(ClientModEvents::registerItemColors);
        modBus.addListener(ClientModEvents::registerMenuScreens);
        modBus.addListener(ClientModEvents::registerRenderers);
        modBus.addListener(ClientModEvents::registerLayerDefinitions);
    }

    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL,
                ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "nutrition_overlay"),
                new NutritionHudOverlay());
    }

    @SuppressWarnings("DuplicatedCode")
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticle.PALE_OAK_LEAVES.get(), PaleOakParticle.Provider::new);
        event.registerSpriteSet(ModParticle.TRAIL.get(), TrailParticle.Provider::new);

        event.registerSpriteSet(ModParticle.GROUND_FOG.get(), GroundFogParticle.DefaultFactory::new);

        event.registerSpriteSet(ModParticle.STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.BLOOMING_STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.BLOOMING_MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
    }

    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.TRASH_MENU.get(), TrashScreen::new);
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CREAKING.get(), CreakingRenderer::new);
        event.registerEntityRenderer(ModEntities.PALE_OAK_BOAT.get(), (context) -> new PaleOakBoatRenderer(context, false));
        event.registerEntityRenderer(ModEntities.PALE_OAK_CHEST_BOAT.get(), (context) -> new PaleOakBoatRenderer(context, true));

        ModEntities.WOOD_SET_BOATS.forEach((setName, type) ->
                event.registerEntityRenderer(type.get(), (context) -> new OttWoodSetBoatRenderer(context, false))
        );
        ModEntities.WOOD_SET_CHEST_BOATS.forEach((setName, type) ->
                event.registerEntityRenderer(type.get(), (context) -> new OttWoodSetBoatRenderer(context, true))
        );
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.PALE_OAK_BOAT, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.PALE_OAK_CHEST_BOAT, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.OTT_WOOD_SET_BOAT, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.OTT_WOOD_SET_CHEST_BOAT, ChestBoatModel::createBodyModel);
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register(PrismaticColorHandler.create(PrismaticColorHandler.Type.FULL_3D, 32f, 1.0f, 0.0f, 1.0f, 0.0f), ModBlocks.TESTBLOCK_02.get());
        event.register(PrismaticColorHandler.create(PrismaticColorHandler.Type.HORIZONTAL, 16f, 0.5f, 0.5f, 0.7f, 0.0f), ModBlocks.TESTBLOCK_03.get());
        event.register(PrismaticColorHandler.create(PrismaticColorHandler.Type.VERTICAL, 8f, 0.25f, 0.3f, 0.5f, 0.0f), ModBlocks.TESTBLOCK_10.get());

        event.register(PrismaticColorHandler.create(PrismaticColorHandler.Type.FULL_3D, 16f, 0.9f, 0.0f, 1.0f, 0.0f), ModBlocks.TESTBLOCK_11.get());
        event.register(PrismaticColorHandler.create(PrismaticColorHandler.Type.FULL_3D, 32f, 1.0f, 0.0f, 1.0f, 0.0f), ModBlocks.TESTBLOCK_12.get());
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        ModBlocks.getAllGradientBlocks().forEach(deferredBlock -> {
            event.register((stack, tintIndex) -> {
                if (stack.getItem() instanceof net.minecraft.world.item.BlockItem blockItem &&
                        blockItem.getBlock() instanceof com.otterly76.ott.block.IGradientBlock gradientBlock) {

                    if (tintIndex == 0) return gradientBlock.getFirstColor().getTextureDiffuseColor();
                    if (tintIndex == 1) return gradientBlock.getSecondColor().getTextureDiffuseColor();
                }
                return -1;
            }, deferredBlock.get());
        });

        event.register((stack, tintIndex) -> {
                    if (tintIndex == 0) {
                        // Spinning through the rainbow every 2 seconds
                        float hue = (float) ((System.nanoTime() / 1_000_000_000.0) / 2.0) % 1.0f;
                        return Color.HSBtoRGB(hue, 0.7f, 1.0f);
                    }
                    return -1;
                },
                ModBlocks.TESTBLOCK_02.get(), ModBlocks.TESTBLOCK_03.get(), ModBlocks.TESTBLOCK_10.get(), ModBlocks.TESTBLOCK_11.get(), ModBlocks.TESTBLOCK_12.get());

    }

    @SuppressWarnings("deprecation")
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Sheets.addWoodType(WoodTypeVariant.PALE_OAK.getWoodType());

            ModBlocks.WOOD_SETS.keySet().forEach(setName ->
                    Sheets.addWoodType(WoodTypeVariant.ott(setName))
            );

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CLOSED_EYEBLOSSOM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.OPEN_EYEBLOSSOM.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PALE_HANGING_MOSS.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PALE_MOSS_CARPET.get(), RenderType.cutout());

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.RESIN_CLUMP.get(), RenderType.cutout());

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PALE_OAK_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_PALE_OAK_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.STARLIGHT_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_STARLIGHT_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.MIDNIGHT_SAPLING.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.POTTED_MIDNIGHT_SAPLING.get(), RenderType.cutout());

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PALE_OAK_LEAVES.get(), RenderType.cutoutMipped());

            ModBlocks.PARTICLE_HEDGES.values().forEach(b ->
                    ItemBlockRenderTypes.setRenderLayer(b.get(), RenderType.cutout())
            );
            ModBlocks.CREEPING_HEDGES.values().forEach(b ->
                    ItemBlockRenderTypes.setRenderLayer(b.get(), RenderType.cutout())
            );

            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PALE_OAK_DOOR.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.PALE_OAK_TRAPDOOR.get(), RenderType.cutout());

            ModBlocks.WOOD_SETS.values().forEach(set -> {
                ItemBlockRenderTypes.setRenderLayer(set.door().get(), RenderType.cutout());
                ItemBlockRenderTypes.setRenderLayer(set.trapdoor().get(), RenderType.cutout());
            });

            ModBlocks.SEAGLASS.forEach(block ->
                    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent()));

            ModBlocks.getAllGradientStainedGlassBlocks().forEach(block ->
                    ItemBlockRenderTypes.setRenderLayer(block.get(), RenderType.translucent()));

        });
    }
}