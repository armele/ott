package com.otterly76.ott;

import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.client.NutritionHudOverlay;
import com.otterly76.ott.particle.HedgeLeafParticle;
import com.otterly76.ott.particle.ModParticle;
import com.otterly76.ott.particle.PaleOakParticle;
import com.otterly76.ott.particle.TrailParticle;
import com.otterly76.ott.util.WoodTypeVariant;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

@SuppressWarnings("MethodRefCanBeReplacedWithLambda")
public class ClientModEvents {

    public static void register(IEventBus modBus) {
        modBus.addListener(ClientModEvents::registerGuiLayers);
        modBus.addListener(ClientModEvents::onClientSetup);
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

        event.registerSpriteSet(ModParticle.STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.BLOOMING_STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.BLOOMING_MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
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
        });
    }
}