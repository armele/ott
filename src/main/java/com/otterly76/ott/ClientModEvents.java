package com.otterly76.ott;

import com.mojang.blaze3d.platform.NativeImage;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.client.NutritionHudOverlay;
import com.otterly76.ott.client.gui.TrashScreen;
import com.otterly76.ott.client.model.BookshelfModelProxy;
import com.otterly76.ott.client.render.PrismaticColorHandler;
import com.otterly76.ott.client.render.texture.FXAtlasSpriteSource;
import com.otterly76.ott.config.OttConfig;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.client.model.chicken.ColdChickenModel;
import com.otterly76.ott.client.model.cow.ColdCowModel;
import com.otterly76.ott.client.model.cow.WarmCowModel;
import com.otterly76.ott.client.model.pig.ColdPigModel;
import com.otterly76.ott.client.render.entity.CreakingRenderer;
import com.otterly76.ott.client.render.entity.HappyGhastRenderer;
import com.otterly76.ott.client.render.entity.ModBoatRenderer;
import com.otterly76.ott.client.render.entity.TorchArrowRenderer;
import com.otterly76.ott.client.model.CreakingModel;
import com.otterly76.ott.client.model.HappyGhastModel;
import com.otterly76.ott.client.model.HappyGhastHarnessModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.inventory.ModMenuTypes;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.client.handler.DryFoliageColorReloadListener;
import com.otterly76.ott.client.handler.ItemPropertyRegistrar;
import com.otterly76.ott.client.handler.LeafColorReloadListener;
import com.otterly76.ott.client.util.LeafColors;
import com.otterly76.ott.particle.*;
import com.otterly76.ott.block.entity.ModBlockEntities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.util.Mth;
import net.minecraft.world.level.GrassColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.*;
import com.otterly76.ott.client.tooltip.FoodTooltipComponent;
import com.otterly76.ott.client.tooltip.ClientFoodTooltipComponent;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.function.IntUnaryOperator;

import static com.otterly76.ott.Constants.MOD_ID;

@SuppressWarnings({"MethodRefCanBeReplacedWithLambda"})
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {
    public static int particleCount;
    public static int fogCount;
    public static final IntUnaryOperator desaturateOperation = (rgba) -> {
        Color col = new Color(rgba, true);
        int gray = Math.max(Math.max(col.getRed(), col.getGreen()), col.getBlue());
        return (col.getAlpha() & 255) << 24 | (gray & 255) << 16 | (gray & 255) << 8 | gray & 255;
    };

    public static void register(IEventBus modBus) {
        net.neoforged.fml.ModLoadingContext.get().getActiveContainer().registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
        modBus.addListener(ClientModEvents::registerGuiLayers);
        modBus.addListener(ClientModEvents::onClientSetup);
        modBus.addListener(ClientModEvents::onRegisterSpriteLoader);
        modBus.addListener(ClientModEvents::registerParticleFactories);
        modBus.addListener(ClientModEvents::registerBlockColors);
        modBus.addListener(ClientModEvents::registerItemColors);
        modBus.addListener(ClientModEvents::registerMenuScreens);
        modBus.addListener(ClientModEvents::registerRenderers);
        modBus.addListener(ClientModEvents::registerLayerDefinitions);
        modBus.addListener(ClientModEvents::onRegisterAdditional);
        modBus.addListener(ClientModEvents::onModelBaking);
        modBus.addListener(com.otterly76.ott.client.handler.BlockModelHandler::onModelBaking);
        modBus.addListener(com.otterly76.ott.client.handler.EmissiveModelHandler::onModelBake);
        modBus.addListener(com.otterly76.ott.client.handler.EmissiveModelHandler::onRegisterAdditionalModels);
        modBus.addListener(ClientModEvents::onRegisterReloadListeners);
        modBus.addListener(ClientModEvents::onRegisterTooltipComponents);
    }

    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(DryFoliageColorReloadListener.INSTANCE);
        event.registerReloadListener(LeafColorReloadListener.INSTANCE);
    }

    public static void onRegisterTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(FoodTooltipComponent.class, ClientFoodTooltipComponent::new);
    }

    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        String[] suffixes = {"", "2", "3", "4", "5"};
        for (String s : suffixes) {
            // FIX: Side-loaded models MUST use the 'standalone' variant in 1.21.1
            ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/bookshelf" + s);
            event.register(ModelResourceLocation.standalone(loc));
        }
    }

    public static void onModelBaking(ModelEvent.ModifyBakingResult event) {
        // 1. Fetch your baked fancy models using the 'standalone' variant
        java.util.List<BakedModel> fancyModels = new java.util.ArrayList<>();
        String[] suffixes = {"", "2", "3", "4", "5"};

        for (String s : suffixes) {
            ResourceLocation loc = ResourceLocation.fromNamespaceAndPath(MOD_ID, "block/bookshelf" + s);
            BakedModel baked = event.getModels().get(ModelResourceLocation.standalone(loc));
            if (baked != null) fancyModels.add(baked);
        }

        // 2. Wrap ALL vanilla bookshelf registry entries
        for (ModelResourceLocation mrl : event.getModels().keySet()) {
            ResourceLocation id = mrl.id();

            // Catch both the block variants and the inventory item
            if (id.getNamespace().equals("minecraft") && (id.getPath().equals("bookshelf") || id.getPath().equals("block/bookshelf"))) {
                BakedModel bakedVanilla = event.getModels().get(mrl);

                if (bakedVanilla != null && !fancyModels.isEmpty()) {
                    // Inject our Proxy into the vanilla registry slots
                    event.getModels().put(mrl, new BookshelfModelProxy(bakedVanilla, fancyModels));
                }
            }
        }
    }

    public static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.FOOD_LEVEL,
                ResourceLocation.fromNamespaceAndPath(MOD_ID, "nutrition_overlay"),
                new NutritionHudOverlay());
    }

    @SuppressWarnings("DuplicatedCode")
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticle.WILL_O_WISP.get(), WillOWispParticle.Provider::new);
        event.registerSpriteSet(ModParticle.PALE_OAK_LEAVES.get(), FallingLeavesParticle.PaleOakProvider::new);
        event.registerSpriteSet(ModParticle.FIREFLY.get(), FireflyParticle.Provider::new);
        event.registerSpriteSet(ModParticle.TINTED_LEAVES.get(), FallingLeavesParticle.TintedLeavesProvider::new);
        event.registerSpriteSet(ModParticle.TINTED_NEEDLES.get(), FallingLeavesParticle.TintedLeavesProvider::new);
        event.registerSpriteSet(ModParticle.TRAIL.get(), TrailParticle.Provider::new);
        event.registerSpriteSet(ModParticle.GROUND_FOG.get(), GroundFogParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.BLOOMING_STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.BLOOMING_MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.RAIN.get(), RainParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.SNOW.get(), SnowParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.DUST_MOTE.get(), DustMoteParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.DUST.get(), DustParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.SHRUB.get(), ShrubParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.FOG.get(), FogParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.RIPPLE.get(), RippleParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.STREAK.get(), StreakParticle.DefaultFactory::new);
    }

    public static void applyWaterTint(TextureSheetParticle particle, ClientLevel clientLevel, BlockPos blockPos) {
        Color waterColor = new Color(BiomeColors.getAverageWaterColor(clientLevel, blockPos));
        Color fogColor = new Color(clientLevel.getBiome(blockPos).value().getFogColor());
        float rCol = Mth.lerp((float)OttConfig.WEATHER.TINT_MIX.get() / 100.0F, (float)waterColor.getRed(), (float)fogColor.getRed()) / 255.0F;
        float gCol = Mth.lerp((float)OttConfig.WEATHER.TINT_MIX.get() / 100.0F, (float)waterColor.getGreen(), (float)fogColor.getGreen()) / 255.0F;
        float bCol = Mth.lerp((float)OttConfig.WEATHER.TINT_MIX.get() / 100.0F, (float)waterColor.getBlue(), (float)fogColor.getBlue()) / 255.0F;
        particle.setColor(rCol, gCol, bCol);
    }

    public static NativeImage loadTexture(ResourceLocation resourceLocation) throws IOException {
        Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(resourceLocation);
        try (InputStream inputStream = resource.open()) {
            return NativeImage.read(inputStream);
        }
    }

    public static SpriteContents splitImage(NativeImage image, int segment, String id) {
        int size = image.getWidth();
        NativeImage sprite = new NativeImage(size, size, false);
        image.copyRect(sprite, 0, size * segment, 0, 0, size, size, true, true);
        return new SpriteContents(ResourceLocation.fromNamespaceAndPath(MOD_ID, id + segment), new FrameSize(size, size), sprite, ResourceMetadata.EMPTY);
    }

public static float yLevelWindAdjustment(double y) {
        float factor = (float) (y / 128.0);
        return Math.clamp(factor, 0.0F, 1.0F);
    }

    public static int getRippleResolution(List<SpriteContents> contents) {
        try {
            if (OttConfig.WEATHER.USE_RESOURCEPACK_RESOLUTION.get()) {
                ResourceLocation resourceLocation = ResourceLocation.withDefaultNamespace("big_smoke_0");
                for (SpriteContents spriteContents : contents) {
                    if (spriteContents.name().equals(resourceLocation) && spriteContents.width() < 256) {
                        return spriteContents.width();
                    }
                }
            }
        } catch (IllegalStateException ignored) {
            // Config not loaded, proceed to default resolution check
        }

        int resolution = 16; // Default fallback
        try {
            resolution = OttConfig.WEATHER.RIPPLE_RESOLUTION.get();
        } catch (IllegalStateException ignored) {
            // Config not loaded, use hardcoded default
        }

        return Math.clamp(resolution, 4, 256);
    }

    public static SpriteContents generateRipple(int i, int size) {
        float radius = (float)size / 2.0F / 8.0F * (float)(i + 1);
        NativeImage image = new NativeImage(size, size, true);
        int colorint = 0xFFFFFFFF;
        generateBresenhamCircle(image, size, (int)Math.clamp(radius, 1.0, (double)size / 2.0 - 1.0), colorint);
        return new SpriteContents(ResourceLocation.fromNamespaceAndPath(MOD_ID, "ripple" + i), new FrameSize(size, size), image, ResourceMetadata.EMPTY);
    }

    public static void generateBresenhamCircle(NativeImage image, int imgSize, int radius, int colorint) {
        int centerX = imgSize / 2;
        int centerY = imgSize / 2;
        int x = 0;
        int y = radius;
        int d = 3 - 2 * radius;
        drawCirclePixel(centerX, centerY, x, radius, image, colorint);
        while(y >= x) {
            if (d > 0) {
                --y;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }
            ++x;
            drawCirclePixel(centerX, centerY, x, y, image, colorint);
        }
    }

    private static void drawCirclePixel(int xc, int yc, int x, int y, NativeImage img, int col) {
        img.setPixelRGBA(xc + x, yc + y, col);
        img.setPixelRGBA(xc - x, yc + y, col);
        img.setPixelRGBA(xc + x, yc - y, col);
        img.setPixelRGBA(xc - x, yc - y, col);
        img.setPixelRGBA(xc + y, yc + x, col);
        img.setPixelRGBA(xc - y, yc + x, col);
        img.setPixelRGBA(xc + y, yc - x, col);
        img.setPixelRGBA(xc - y, yc - x, col);
    }

    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.TRASH_MENU.get(), TrashScreen::new);

        event.register(ModMenuTypes.ANVIL_MENU_TYPE.get(), com.otterly76.ott.client.gui.ModAnvilScreen::new);
    }

    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.CREAKING.get(), CreakingRenderer::new);
        event.registerEntityRenderer(ModEntities.HAPPY_GHAST.get(), HappyGhastRenderer::new);
        event.registerEntityRenderer(ModEntities.PALE_OAK_BOAT.get(), (context) -> new ModBoatRenderer(context, false));
        event.registerEntityRenderer(ModEntities.PALE_OAK_CHEST_BOAT.get(), (context) -> new ModBoatRenderer(context, true));
        ModEntities.WOOD_SET_BOATS.forEach((setName, type) ->
                event.registerEntityRenderer(type.get(), (context) -> new ModBoatRenderer(context, false))
        );
        ModEntities.WOOD_SET_CHEST_BOATS.forEach((setName, type) ->
                event.registerEntityRenderer(type.get(), (context) -> new ModBoatRenderer(context, true))
        );

        event.registerEntityRenderer(ModEntities.TINY_SKELETON.get(), SkeletonRenderer::new);
        event.registerEntityRenderer(ModEntities.TINY_CREEPER.get(), CreeperRenderer::new);
        event.registerEntityRenderer(ModEntities.TINY_ENDERMAN.get(), EndermanRenderer::new);
        event.registerEntityRenderer(ModEntities.TINY_BOGGED.get(), BoggedRenderer::new);
        event.registerEntityRenderer(ModEntities.TINY_DROWNED.get(), DrownedRenderer::new);
        event.registerEntityRenderer(ModEntities.TINY_HUSK.get(), HuskRenderer::new);
        event.registerEntityRenderer(ModEntities.TINY_STRAY.get(), StrayRenderer::new);
        event.registerEntityRenderer(ModEntities.TINY_WITHER_SKELETON.get(), WitherSkeletonRenderer::new);
        event.registerEntityRenderer(ModEntities.TORCH_ARROW.get(), TorchArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.COPPER_GOLEM.get(), com.otterly76.ott.client.render.entity.CopperGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.MAN_O_WAR.get(), com.otterly76.ott.client.render.entity.ManOWarRenderer::new);

        event.registerBlockEntityRenderer(ModBlockEntities.ANVIL_BLOCK_ENTITY_TYPE.get(), com.otterly76.ott.client.render.AnvilRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SHELF.get(), com.otterly76.ott.client.render.blockentity.ShelfRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COPPER_CHEST.get(), com.otterly76.ott.client.render.blockentity.CopperChestRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COPPER_GOLEM_STATUE.get(), com.otterly76.ott.client.render.blockentity.CopperGolemStatueRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CREAKING, CreakingModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.PALE_OAK_BOAT, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.PALE_OAK_CHEST_BOAT, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.OTT_WOOD_SET_BOAT, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.OTT_WOOD_SET_CHEST_BOAT, ChestBoatModel::createBodyModel);

        event.registerLayerDefinition(ModModelLayers.COLD_PIG, ColdPigModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.COLD_CHICKEN, ColdChickenModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.COLD_COW, ColdCowModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.WARM_COW, WarmCowModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.COPPER_GOLEM, com.otterly76.ott.client.model.CopperGolemModel::createBodyLayer);

        event.registerLayerDefinition(ModModelLayers.HAPPY_GHAST, () -> HappyGhastModel.createBodyLayer(CubeDeformation.NONE));
        event.registerLayerDefinition(ModModelLayers.HAPPY_GHAST_HARNESS, HappyGhastHarnessModel::createHarnessLayer);
        event.registerLayerDefinition(ModModelLayers.HAPPY_GHAST_ROPES, () -> HappyGhastModel.createBodyLayer(new CubeDeformation(0.2F)));
    }

    public static void onRegisterSpriteLoader(RegisterSpriteSourceTypesEvent event) {
        event.register(Ott.resource("fxsprite"), FXAtlasSpriteSource.TYPE);
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tint) -> level != null && pos != null ? LeafColors.getAverageDryFoliageColor(pos) : -10732494, ModBlocks.LEAF_LITTER.get());
        event.register((state, level, pos, tint) -> level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.getDefaultColor(), ModBlocks.BUSH.get());
        event.register((state, level, pos, tint) -> {
            if (tint == 0) {
                return -1;
            } else {
                return level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.getDefaultColor();
            }
        }, ModBlocks.WILDFLOWERS.get());

        event.register(PrismaticColorHandler.create(PrismaticColorHandler.Type.FULL_3D, 32f, 1.0f, 0.0f, 1.0f, 0.0f), ModBlocks.TESTBLOCK_02.get());
        event.register(PrismaticColorHandler.create(PrismaticColorHandler.Type.HORIZONTAL, 16f, 0.5f, 0.5f, 0.7f, 0.0f), ModBlocks.TESTBLOCK_03.get());
        event.register(PrismaticColorHandler.create(PrismaticColorHandler.Type.VERTICAL, 8f, 0.25f, 0.3f, 0.5f, 0.0f), ModBlocks.TESTBLOCK_10.get());
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> event.getBlockColors().getColor(((net.minecraft.world.item.BlockItem)stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), ModBlocks.BUSH.get(), ModBlocks.WILDFLOWERS.get());

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
                        float hue = (float) ((System.nanoTime() / 1_000_000_000.0) / 2.0) % 1.0f;
                        return Color.HSBtoRGB(hue, 0.7f, 1.0f);
                    }
                    return -1;
                },
                ModBlocks.TESTBLOCK_02.get(), ModBlocks.TESTBLOCK_03.get(), ModBlocks.TESTBLOCK_10.get());

        event.register((stack, tintIndex) -> {
            if (tintIndex == 0) return 0xFFFFC400;
            return -1;
        }, ModItems.TORCH_ARROW.get());
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemPropertyRegistrar.bootstrap();
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        com.otterly76.ott.client.handler.NameTagTooltipHandler.onItemTooltip(event.getItemStack(), event.getToolTip(), event.getContext(), event.getEntity(), event.getFlags());
    }
}