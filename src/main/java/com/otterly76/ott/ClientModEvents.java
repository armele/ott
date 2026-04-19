package com.otterly76.ott;
import com.otterly76.ott.client.render.entity.*;
import com.otterly76.ott.block.ModBlocks;
import com.otterly76.ott.client.NutritionHudOverlay;
import com.otterly76.ott.client.gui.RecyclingScreen;
import com.otterly76.ott.client.toast.BetterToastComponent;
import com.otterly76.ott.client.gui.NautilusInventoryScreen;
import com.otterly76.ott.client.gui.TrashScreen;
import com.otterly76.ott.client.model.BookshelfModelProxy;
import com.otterly76.ott.client.render.PrismaticColorHandler;
import com.otterly76.ott.client.render.texture.FXAtlasSpriteSource;
import com.otterly76.ott.entity.ModEntities;
import com.otterly76.ott.client.model.chicken.ColdChickenModel;
import com.otterly76.ott.client.model.pig.ColdPigModel;
import com.otterly76.ott.client.model.CreakingModel;
import com.otterly76.ott.client.model.HappyGhastModel;
import com.otterly76.ott.client.model.HappyGhastHarnessModel;
import com.otterly76.ott.client.model.nautilus.NautilusArmorModel;
import com.otterly76.ott.client.model.nautilus.NautilusModel;
import com.otterly76.ott.client.model.nautilus.NautilusSaddleModel;
import com.otterly76.ott.client.registries.ModModelLayers;
import com.otterly76.ott.inventory.ModMenuTypes;
import com.otterly76.ott.item.ModItems;
import com.otterly76.ott.client.handler.DryFoliageColorReloadListener;
import com.otterly76.ott.client.handler.ItemPropertyRegistrar;
import com.otterly76.ott.client.handler.LeafColorReloadListener;
import com.otterly76.ott.client.util.LeafColors;
import com.otterly76.ott.particle.*;
import com.otterly76.ott.block.entity.ModBlockEntities;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.entity.*;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceMetadata;
import net.minecraft.client.renderer.texture.SpriteContents;
import net.minecraft.client.resources.metadata.animation.FrameSize;
import com.otterly76.ott.config.OttConfig;
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
import java.util.function.IntUnaryOperator;

import static com.otterly76.ott.Constants.MOD_ID;

@SuppressWarnings({"MethodRefCanBeReplacedWithLambda"})
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {
    public static int particleCount = 0;
    public static int fogCount = 0;

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
        modBus.addListener(ClientModEvents::registerElevatorModelBaking);
        modBus.addListener(ClientModEvents::registerRenderers);
        modBus.addListener(ClientModEvents::registerLayerDefinitions);
        modBus.addListener(ClientModEvents::onRegisterAdditional);
        modBus.addListener(ClientModEvents::onModelBaking);
        modBus.addListener(com.otterly76.ott.client.handler.BlockModelHandler::onModelBaking);
        modBus.addListener(com.otterly76.ott.client.handler.EmissiveModelHandler::onModelBake);
        modBus.addListener(com.otterly76.ott.client.handler.EmissiveModelHandler::onRegisterAdditionalModels);
        modBus.addListener(ClientModEvents::onRegisterGeometryLoaders);
        modBus.addListener(ClientModEvents::onRegisterReloadListeners);
        modBus.addListener(ClientModEvents::onRegisterTooltipComponents);
        modBus.addListener(BetterToastComponent::onConfigReload);
        modBus.addListener(ClientModEvents::registerRecipeBookCategories);
    }

    public static void onRegisterReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(DryFoliageColorReloadListener.INSTANCE);
        event.registerReloadListener(LeafColorReloadListener.INSTANCE);
    }

    public static void onRegisterTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(FoodTooltipComponent.class, ClientFoodTooltipComponent::new);
    }

    public static void onRegisterGeometryLoaders(ModelEvent.RegisterGeometryLoaders event) {
        event.register(ResourceLocation.fromNamespaceAndPath(MOD_ID, "mosaic"),
                com.otterly76.ott.client.model.ctm.ConnectingModelLoader.INSTANCE);
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
        event.registerSpriteSet(ModParticle.FIREFLY.get(), FireflyParticle.Provider::new);

        event.registerSpriteSet(ModParticle.RAIN.get(), RainParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.SNOW.get(), SnowParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.DUST_MOTE.get(), DustMoteParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.DUST.get(), DustParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.FOG.get(), FogParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.GROUND_FOG.get(), GroundFogParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.SHRUB.get(), ShrubParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.RIPPLE.get(), RippleParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.STREAK.get(), StreakParticle.DefaultFactory::new);
        event.registerSpriteSet(ModParticle.GHOST.get(), GeistParticle.GhostProvider::new);
        event.registerSpriteSet(ModParticle.GEIST_DARK.get(), GeistParticle.GeistDarkProvider::new);
        event.registerSpriteSet(ModParticle.POISON_SPIT.get(), com.otterly76.ott.particle.PoisonSpitParticle.Provider::new);

        event.registerSpriteSet(ModParticle.STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.BLOOMING_STARLIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.BLOOMING_MIDNIGHT_LEAF.get(), HedgeLeafParticle.Provider::new);
        event.registerSpriteSet(ModParticle.COPPER_FIRE_FLAME.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(ModParticle.PALE_OAK_LEAVES.get(), FallingLeavesParticle.PaleOakProvider::new);
        event.registerSpriteSet(ModParticle.TINTED_LEAVES.get(), FallingLeavesParticle.TintedLeavesProvider::new);
        event.registerSpriteSet(ModParticle.TINTED_NEEDLES.get(), FallingLeavesParticle.TintedLeavesProvider::new);
        event.registerSpriteSet(ModParticle.TRAIL.get(), TrailParticle.Provider::new);
    }

    public static void registerMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.TRASH_MENU.get(), TrashScreen::new);
        event.register(ModMenuTypes.ANVIL_MENU_TYPE.get(), com.otterly76.ott.client.gui.ModAnvilScreen::new);
        event.register(ModMenuTypes.ELEVATOR_MENU.get(), com.otterly76.ott.client.gui.ElevatorScreen::new);
        event.register(ModMenuTypes.RECYCLING_MENU.get(), RecyclingScreen::new);
        event.register(ModMenuTypes.WOODCUTTER_MENU.get(), com.otterly76.ott.client.screen.WoodcutterScreen::new);
        event.register(ModMenuTypes.NAUTILUS_INVENTORY.get(), NautilusInventoryScreen::new);
    }

    @SuppressWarnings("unchecked")
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.NAUTILUS.get(), com.otterly76.ott.client.render.entity.NautilusRenderer::new);
        event.registerEntityRenderer(ModEntities.ZOMBIE_NAUTILUS.get(), com.otterly76.ott.client.render.entity.ZombieNautilusRenderer::new);
        event.registerEntityRenderer(ModEntities.PARCHED.get(), com.otterly76.ott.client.render.entity.ParchedRenderer::new);
        event.registerEntityRenderer(ModEntities.CAMEL_HUSK.get(), com.otterly76.ott.client.render.entity.CamelHuskRenderer::new);
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

        event.registerEntityRenderer(ModEntities.TORCH_ARROW.get(), TorchArrowRenderer::new);
        event.registerEntityRenderer(ModEntities.COPPER_GOLEM.get(), com.otterly76.ott.client.render.entity.CopperGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.MAN_O_WAR.get(), com.otterly76.ott.client.render.entity.ManOWarRenderer::new);
        event.registerEntityRenderer(ModEntities.STINGRAY.get(), com.otterly76.ott.client.render.entity.StingrayRenderer::new);
        event.registerEntityRenderer(ModEntities.SUNFISH.get(), com.otterly76.ott.client.render.entity.SunfishRenderer::new);
        event.registerEntityRenderer(ModEntities.DUCK.get(), com.otterly76.ott.client.render.entity.DuckGeoRenderer::new);
        event.registerEntityRenderer(ModEntities.GOOSE.get(), com.otterly76.ott.client.render.entity.GooseGeoRenderer::new);
        event.registerEntityRenderer(ModEntities.KRILL.get(), com.otterly76.ott.client.render.entity.KrillRenderer::new);
        event.registerEntityRenderer(ModEntities.ANGELFISH.get(), com.otterly76.ott.client.render.entity.AngelfishRenderer::new);
        event.registerEntityRenderer(ModEntities.BARRELEYE.get(), com.otterly76.ott.client.render.entity.BarreleyeRenderer::new);
        event.registerEntityRenderer(ModEntities.FLOUNDER.get(), com.otterly76.ott.client.render.entity.FlounderRenderer::new);
        event.registerEntityRenderer(ModEntities.MARINE_IGUANA.get(), com.otterly76.ott.client.render.entity.MarineIguanaRenderer::new);
        event.registerEntityRenderer(ModEntities.GECKO.get(), com.otterly76.ott.client.render.entity.GeckoRenderer::new);
        event.registerEntityRenderer(ModEntities.EMU.get(), com.otterly76.ott.client.render.entity.EmuRenderer::new);
        event.registerEntityRenderer(ModEntities.HOOPOE.get(), com.otterly76.ott.client.render.entity.HoopoeRenderer::new);
        event.registerEntityRenderer(ModEntities.PHEASANT.get(), com.otterly76.ott.client.render.entity.PheasantRenderer::new);
        event.registerEntityRenderer(ModEntities.TOUCAN.get(), com.otterly76.ott.client.render.entity.ToucanRenderer::new);
        event.registerEntityRenderer(ModEntities.CATFISH.get(), com.otterly76.ott.client.render.entity.CatfishRenderer::new);
        event.registerEntityRenderer(ModEntities.BASS.get(), com.otterly76.ott.client.render.entity.BassRenderer::new);
        event.registerEntityRenderer(ModEntities.BUTTERFLY.get(), com.otterly76.ott.client.render.entity.ButterflyRenderer::new);
        event.registerEntityRenderer(ModEntities.CATERPILLAR.get(), com.otterly76.ott.client.render.entity.CaterpillarRenderer::new);
        event.registerEntityRenderer(ModEntities.FIREFLY.get(), com.otterly76.ott.client.render.entity.FireflyRenderer::new);
        event.registerEntityRenderer(ModEntities.ALLIGATOR.get(), com.otterly76.ott.client.render.entity.AlligatorRenderer::new);
        event.registerEntityRenderer(ModEntities.ELEPHANT.get(), com.otterly76.ott.client.render.entity.ElephantRenderer::new);
        event.registerEntityRenderer(ModEntities.GIRAFFE.get(), com.otterly76.ott.client.render.entity.GiraffeRenderer::new);
        event.registerEntityRenderer(ModEntities.HIPPO.get(), com.otterly76.ott.client.render.entity.HippoRenderer::new);
        event.registerEntityRenderer(ModEntities.LION.get(), com.otterly76.ott.client.render.entity.LionRenderer::new);
        event.registerEntityRenderer(ModEntities.RHINO.get(), com.otterly76.ott.client.render.entity.RhinoRenderer::new);
        event.registerEntityRenderer(ModEntities.LIZARD.get(), com.otterly76.ott.client.render.entity.LizardRenderer::new);
        event.registerEntityRenderer(ModEntities.LIZARD_TAIL.get(), com.otterly76.ott.client.render.entity.LizardTailRenderer::new);
        event.registerEntityRenderer(ModEntities.SNAIL.get(), com.otterly76.ott.client.render.entity.SnailRenderer::new);
        event.registerEntityRenderer(ModEntities.TORTOISE.get(), com.otterly76.ott.client.render.entity.TortoiseRenderer::new);
        event.registerEntityRenderer(ModEntities.VULTURE.get(), com.otterly76.ott.client.render.entity.VultureRenderer::new);
        event.registerEntityRenderer(ModEntities.ZEBRA.get(), com.otterly76.ott.client.render.entity.ZebraRenderer::new);
        event.registerEntityRenderer(ModEntities.MOOSE.get(), com.otterly76.ott.client.render.entity.MooseRenderer::new);
        event.registerEntityRenderer(ModEntities.MAMMOTH.get(), com.otterly76.ott.client.render.entity.MammothRenderer::new);
        event.registerEntityRenderer(ModEntities.MYCELIUM_MAMMOTH.get(), com.otterly76.ott.client.render.entity.MammothRenderer::new);
        event.registerEntityRenderer(ModEntities.FENNEC_FOX.get(), com.otterly76.ott.client.render.entity.FennecFoxRenderer::new);
        event.registerEntityRenderer(ModEntities.BROWN_BEAR.get(), com.otterly76.ott.client.render.entity.BearRenderer::new);
        event.registerEntityRenderer(ModEntities.BLACK_BEAR.get(), com.otterly76.ott.client.render.entity.BearRenderer::new);
        event.registerEntityRenderer(ModEntities.DEER.get(), com.otterly76.ott.client.render.entity.DeerRenderer::new);
        event.registerEntityRenderer(ModEntities.REINDEER.get(), com.otterly76.ott.client.render.entity.DeerRenderer::new);
        event.registerEntityRenderer(ModEntities.WHITE_DEER.get(), com.otterly76.ott.client.render.entity.DeerRenderer::new);
        event.registerEntityRenderer(ModEntities.BLUEJAY.get(), com.otterly76.ott.client.render.entity.BirdRenderer::new);
        event.registerEntityRenderer(ModEntities.CANARY.get(), com.otterly76.ott.client.render.entity.BirdRenderer::new);
        event.registerEntityRenderer(ModEntities.CARDINAL.get(), com.otterly76.ott.client.render.entity.BirdRenderer::new);
        event.registerEntityRenderer(ModEntities.FINCH.get(), com.otterly76.ott.client.render.entity.BirdRenderer::new);
        event.registerEntityRenderer(ModEntities.ROBIN.get(), com.otterly76.ott.client.render.entity.BirdRenderer::new);
        event.registerEntityRenderer(ModEntities.SPARROW.get(), com.otterly76.ott.client.render.entity.BirdRenderer::new);
        event.registerEntityRenderer(ModEntities.LARGE_JELLYFISH.get(), LargeJellyfishRenderer::new);
        event.registerEntityRenderer(ModEntities.SEAHORSE.get(), SeahorseRenderer::new);
        event.registerEntityRenderer(ModEntities.ETHEREAL_SHRIMP.get(), EtherealShrimpRenderer::new);
        event.registerEntityRenderer(ModEntities.STARFISH.get(), StarfishRenderer::new);
        event.registerEntityRenderer(ModEntities.SMALL_JELLYFISH.get(), SmallJellyfishRenderer::new);
        event.registerEntityRenderer(ModEntities.MEDIUM_JELLYFISH.get(), MediumJellyfishRenderer::new);
        event.registerEntityRenderer(ModEntities.KIWI.get(), com.otterly76.ott.client.render.entity.KiwiRenderer::new);
        event.registerEntityRenderer(ModEntities.PENGUIN.get(), com.otterly76.ott.client.render.entity.PenguinRenderer::new);
        event.registerEntityRenderer(ModEntities.SEAL.get(), com.otterly76.ott.client.render.entity.SealRenderer::new);
        event.registerEntityRenderer(ModEntities.SEA_URCHIN.get(), com.otterly76.ott.client.render.entity.SeaUrchinRenderer::new);
        event.registerEntityRenderer(ModEntities.CAPYBARA.get(), com.otterly76.ott.client.render.entity.CapybaraRenderer::new);
        event.registerEntityRenderer(ModEntities.HEDGEHOG.get(), com.otterly76.ott.client.render.entity.HedgehogRenderer::new);

        event.registerEntityRenderer(ModEntities.DRAGONFLY.get(), com.otterly76.ott.client.render.entity.DragonflyRenderer::new);
        event.registerEntityRenderer(ModEntities.DUMBO_OCTOPUS.get(), com.otterly76.ott.client.render.entity.DumboOctopusRenderer::new);
        event.registerEntityRenderer(ModEntities.FERRET.get(), com.otterly76.ott.client.render.entity.FerretRenderer::new);
        event.registerEntityRenderer(ModEntities.JUMPING_SPIDER.get(), com.otterly76.ott.client.render.entity.JumpingSpiderRenderer::new);
        event.registerEntityRenderer(ModEntities.KOI_FISH.get(), com.otterly76.ott.client.render.entity.KoiFishRenderer::new);
        event.registerEntityRenderer(ModEntities.OTTER.get(), com.otterly76.ott.client.render.entity.OtterRenderer::new);
        event.registerEntityRenderer(ModEntities.RED_PANDA.get(), com.otterly76.ott.client.render.entity.RedPandaRenderer::new);
        event.registerEntityRenderer(ModEntities.SEA_BUNNY.get(), com.otterly76.ott.client.render.entity.SeaBunnyRenderer::new);
        event.registerEntityRenderer(ModEntities.SMALL_FIREFLY.get(), com.otterly76.ott.client.render.entity.SmallFireflyRenderer::new);

        event.registerEntityRenderer(ModEntities.GHOST.get(), com.otterly76.ott.client.render.entity.GhostRenderer::new);
        event.registerEntityRenderer(ModEntities.SPECTRE.get(), com.otterly76.ott.client.render.entity.SpectreRenderer::new);
        event.registerEntityRenderer(ModEntities.HAUNT.get(), com.otterly76.ott.client.render.entity.HauntRenderer::new);
        event.registerEntityRenderer(ModEntities.GEIST.get(), com.otterly76.ott.client.render.entity.GeistRenderer::new);

        event.registerEntityRenderer(ModEntities.TREE_ENT.get(), com.otterly76.ott.client.render.entity.TreeEntRenderer::new);
        event.registerEntityRenderer(ModEntities.HERMIT_KING.get(), com.otterly76.ott.client.render.entity.HermitKingRenderer::new);
        event.registerEntityRenderer(ModEntities.SEA_VIPER.get(), com.otterly76.ott.client.render.entity.SeaViperRenderer::new);
        event.registerEntityRenderer(ModEntities.YETI.get(), com.otterly76.ott.client.render.entity.YetiRenderer::new);
        event.registerEntityRenderer(ModEntities.VILE_GATOR.get(), com.otterly76.ott.client.render.entity.VileGatorRenderer::new);
        event.registerEntityRenderer(ModEntities.PHOENIX.get(), com.otterly76.ott.client.render.entity.PhoenixRenderer::new);
        event.registerEntityRenderer(ModEntities.BABY_PHOENIX.get(), com.otterly76.ott.client.render.entity.BabyPhoenixRenderer::new);
        event.registerEntityRenderer(ModEntities.BONE_STALKER.get(), com.otterly76.ott.client.render.entity.BoneStalkerRenderer::new);
        event.registerEntityRenderer(ModEntities.SHADOW.get(), com.otterly76.ott.client.render.entity.ShadowRenderer::new);
        event.registerEntityRenderer(ModEntities.CHERRY_TREE_ENT.get(), com.otterly76.ott.client.render.entity.CherryTreeEntRenderer::new);
        event.registerEntityRenderer(ModEntities.GOLDEN_HERMIT_KING.get(), com.otterly76.ott.client.render.entity.GoldenHermitKingRenderer::new);
        event.registerEntityRenderer(ModEntities.CORAL_SEA_VIPER.get(), com.otterly76.ott.client.render.entity.CoralSeaViperRenderer::new);
        event.registerEntityRenderer(ModEntities.ARID_YETI.get(), com.otterly76.ott.client.render.entity.AridYetiRenderer::new);
        event.registerEntityRenderer(ModEntities.WIND_PHOENIX.get(), com.otterly76.ott.client.render.entity.WindPhoenixRenderer::new);
        event.registerEntityRenderer(ModEntities.BABY_WIND_PHOENIX.get(), com.otterly76.ott.client.render.entity.BabyWindPhoenixRenderer::new);
        event.registerEntityRenderer(ModEntities.BOGGED_BONE_STALKER.get(), com.otterly76.ott.client.render.entity.BoggedBoneStalkerRenderer::new);
        event.registerEntityRenderer(ModEntities.BOGGED_SHADOW.get(), com.otterly76.ott.client.render.entity.BoggedShadowRenderer::new);
        event.registerEntityRenderer(ModEntities.GILDED_TREE_ENT.get(), com.otterly76.ott.client.render.entity.GildedTreeEntRenderer::new);

        event.registerEntityRenderer(ModEntities.BEAVER.get(), com.otterly76.ott.client.render.entity.BeaverRenderer::new);
        event.registerEntityRenderer(ModEntities.CHUPACABRA.get(), com.otterly76.ott.client.render.entity.ChupacabraRenderer::new);
        event.registerEntityRenderer(ModEntities.CHUPACABRA_SPIT.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.COUGAR.get(), com.otterly76.ott.client.render.entity.CougarRenderer::new);
        event.registerEntityRenderer(ModEntities.COYOTE.get(), com.otterly76.ott.client.render.entity.CoyoteRenderer::new);
        event.registerEntityRenderer(ModEntities.HOWLER.get(), com.otterly76.ott.client.render.entity.HowlerRenderer::new);
        event.registerEntityRenderer(ModEntities.BEWITCHED_TIMBER_WOLF.get(), com.otterly76.ott.client.render.entity.BewitchedGreywolfRenderer::new);
        event.registerEntityRenderer(ModEntities.MARMOT.get(), com.otterly76.ott.client.render.entity.MarmotRenderer::new);
        event.registerEntityRenderer(ModEntities.MOUSE.get(), com.otterly76.ott.client.render.entity.MouseRenderer::new);
        event.registerEntityRenderer(ModEntities.PIT_VIPER.get(), com.otterly76.ott.client.render.entity.PitViperRenderer::new);
        event.registerEntityRenderer(ModEntities.RATTLESNAKE.get(), com.otterly76.ott.client.render.entity.RattlesnakeRenderer::new);
        event.registerEntityRenderer(ModEntities.RINGTAIL.get(), com.otterly76.ott.client.render.entity.RingtailRenderer::new);
        event.registerEntityRenderer(ModEntities.SASQUATCH.get(), com.otterly76.ott.client.render.entity.SasquatchRenderer::new);
        event.registerEntityRenderer(ModEntities.SKINWALKER.get(), com.otterly76.ott.client.render.entity.SkinwalkerRenderer::new);
        event.registerEntityRenderer(ModEntities.SNAKE.get(), com.otterly76.ott.client.render.entity.SnakeRenderer::new);
        event.registerEntityRenderer(ModEntities.SQUONK.get(), com.otterly76.ott.client.render.entity.SquonkRenderer::new);
        event.registerEntityRenderer(ModEntities.TURKEY.get(), com.otterly76.ott.client.render.entity.TurkeyRenderer::new);
        event.registerEntityRenderer(ModEntities.WECHUGE.get(), com.otterly76.ott.client.render.entity.WechugeRenderer::new);
        event.registerEntityRenderer(ModEntities.WENDIGO.get(), com.otterly76.ott.client.render.entity.WendigoRenderer::new);
        event.registerEntityRenderer(ModEntities.WOLVERINE.get(), com.otterly76.ott.client.render.entity.WolverineRenderer::new);

        event.registerEntityRenderer(ModEntities.CICHLID.get(), com.otterly76.ott.client.render.entity.CichlidRenderer::new);
        event.registerEntityRenderer(ModEntities.LEOPARD_CAT.get(), com.otterly76.ott.client.render.entity.LeopardCatRenderer::new);
        event.registerEntityRenderer(ModEntities.WATER_BUFFALO.get(), com.otterly76.ott.client.render.entity.WaterBuffaloGeoRenderer::new);
        event.registerEntityRenderer(ModEntities.ECHIDNA.get(), com.otterly76.ott.client.render.entity.EchidnaRenderer::new);
        event.registerEntityRenderer(ModEntities.GUITARFISH.get(), com.otterly76.ott.client.render.entity.GuitarfishRenderer::new);
        event.registerEntityRenderer(ModEntities.BONNETHEAD_SHARK.get(), com.otterly76.ott.client.render.entity.BonnetheadSharkRenderer::new);
        event.registerEntityRenderer(ModEntities.BURROWING_OWL.get(), com.otterly76.ott.client.render.entity.BurrowingOwlRenderer::new);
        event.registerEntityRenderer(ModEntities.BUSHDOG.get(), com.otterly76.ott.client.render.entity.BushdogRenderer::new);
        event.registerEntityRenderer(ModEntities.QUAIL.get(), com.otterly76.ott.client.render.entity.QuailRenderer::new);
        event.registerEntityRenderer(ModEntities.CANDYCANE_SNAIL.get(), com.otterly76.ott.client.render.entity.CandycaneSnailRenderer::new);
        event.registerEntityRenderer(ModEntities.FIRE_SALAMANDER.get(), com.otterly76.ott.client.render.entity.FireSalamanderRenderer::new);
        event.registerEntityRenderer(ModEntities.RIVER_TURTLE.get(), com.otterly76.ott.client.render.entity.RiverTurtleRenderer::new);
        event.registerEntityRenderer(ModEntities.GOBLIN_SHARK.get(), com.otterly76.ott.client.render.entity.GoblinSharkRenderer::new);
        event.registerEntityRenderer(ModEntities.GUINEA_FOWL.get(), com.otterly76.ott.client.render.entity.GuineaFowlRenderer::new);
        event.registerEntityRenderer(ModEntities.IMPALA.get(), com.otterly76.ott.client.render.entity.ImpalaRenderer::new);
        event.registerEntityRenderer(ModEntities.MANTA_RAY.get(), com.otterly76.ott.client.render.entity.MantaRayRenderer::new);
        event.registerEntityRenderer(ModEntities.STORK.get(), com.otterly76.ott.client.render.entity.StorkRenderer::new);
        event.registerEntityRenderer(ModEntities.MOLE.get(), com.otterly76.ott.client.render.entity.MoleRenderer::new);
        event.registerEntityRenderer(ModEntities.TREE_KANGAROO.get(), com.otterly76.ott.client.render.entity.TreeKangarooRenderer::new);
        event.registerEntityRenderer(ModEntities.PALLAS_CAT.get(), com.otterly76.ott.client.render.entity.PallasCatRenderer::new);
        event.registerEntityRenderer(ModEntities.PINK_LAND_IGUANA.get(), com.otterly76.ott.client.render.entity.PinkLandIguanaRenderer::new);
        event.registerEntityRenderer(ModEntities.PSYCHO_JELLY.get(), com.otterly76.ott.client.render.entity.PsychoJellyRenderer::new);
        event.registerEntityRenderer(ModEntities.SPOONBILL.get(), com.otterly76.ott.client.render.entity.SpoonbillRenderer::new);
        event.registerEntityRenderer(ModEntities.GIANT_SOFTSHELL_TURTLE.get(), com.otterly76.ott.client.render.entity.GiantSoftshellTurtleRenderer::new);

        // --- Ecologics ---
        event.registerEntityRenderer(ModEntities.COCONUT_CRAB.get(), com.otterly76.ott.client.render.entity.CoconutCrabRenderer::new);
        event.registerEntityRenderer(ModEntities.SAND_CRAB.get(), com.otterly76.ott.client.render.entity.SandCrabRenderer::new);
        // --- Friends and Foes ---
        event.registerEntityRenderer(ModEntities.FIDDLER_CRAB.get(), com.otterly76.ott.client.render.entity.FiddlerCrabRenderer::new);
        event.registerEntityRenderer(ModEntities.GLARE.get(), com.otterly76.ott.client.render.entity.GlareRenderer::new);
        event.registerEntityRenderer(ModEntities.ICEOLOGER.get(), com.otterly76.ott.client.render.entity.IceologerRenderer::new);
        event.registerEntityRenderer(ModEntities.ICE_CHUNK.get(), com.otterly76.ott.client.render.entity.IceChunkRenderer::new);
        event.registerEntityRenderer(ModEntities.MAULER.get(), com.otterly76.ott.client.render.entity.MaulerRenderer::new);
        event.registerEntityRenderer(ModEntities.RASCAL.get(), com.otterly76.ott.client.render.entity.RascalRenderer::new);
        event.registerEntityRenderer(ModEntities.TUFF_GOLEM.get(), com.otterly76.ott.client.render.entity.TuffGolemRenderer::new);
        event.registerEntityRenderer(ModEntities.WILDFIRE.get(), com.otterly76.ott.client.render.entity.WildfireRenderer::new);

        event.registerEntityRenderer(ModEntities.KIWI_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.PENGUIN_EGG.get(), ThrownItemRenderer::new);

        event.registerEntityRenderer(ModEntities.EMU_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.HOOPOE_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.PHEASANT_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.TOUCAN_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.THROWN_DUCK_EGG.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.SHEEP, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.SheepGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.animal.Sheep>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.COW, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.CowGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.animal.Cow>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.MOOSHROOM, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.MooshroomGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.animal.MushroomCow>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.CHICKEN, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.ChickenGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.animal.Chicken>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.PIG, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.PigGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.animal.Pig>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.SKELETON, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.SkeletonGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.monster.Skeleton>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.BOGGED, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.BoggedGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.monster.Bogged>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.STRAY, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.StrayGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.monster.Stray>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.WITHER_SKELETON, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.WitherSkeletonGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.monster.WitherSkeleton>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.ZOMBIE, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.ZombieGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.monster.Zombie>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.DROWNED, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.DrownedGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.monster.Drowned>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.HUSK, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.HuskGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.monster.Husk>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.ALLAY, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.AllayGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.animal.allay.Allay>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.VEX, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.VexGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.monster.Vex>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.RABBIT, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.RabbitGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.animal.Rabbit>) renderer;
        });
        event.registerEntityRenderer(net.minecraft.world.entity.EntityType.SNOW_GOLEM, (context) -> {
            net.minecraft.client.renderer.entity.EntityRenderer<?> renderer = new com.otterly76.ott.client.render.entity.SnowGolemGeoRenderer<>(context);
            return (net.minecraft.client.renderer.entity.EntityRenderer<net.minecraft.world.entity.animal.SnowGolem>) renderer;
        });

        event.registerBlockEntityRenderer(ModBlockEntities.ANVIL_BLOCK_ENTITY_TYPE.get(), com.otterly76.ott.client.render.AnvilRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.SHELF.get(), com.otterly76.ott.client.render.blockentity.ShelfRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COPPER_CHEST.get(), com.otterly76.ott.client.render.blockentity.CopperChestRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COPPER_GOLEM_STATUE.get(), com.otterly76.ott.client.render.blockentity.CopperGolemStatueRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.WEATHERING_STATION.get(), com.otterly76.ott.client.render.blockentity.WeatheringStationRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COLOR_SET_BED.get(), com.otterly76.ott.client.render.blockentity.ColorSetBedRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COLOR_SET_BANNER.get(), com.otterly76.ott.client.render.blockentity.ColorSetBannerRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.COLOR_SET_SHULKER_BOX.get(), com.otterly76.ott.client.render.blockentity.ColorSetShulkerBoxRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.FIREFLY_JAR.get(), com.otterly76.ott.client.render.blockentity.FireflyJarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.BUTTERFLY_JAR.get(), com.otterly76.ott.client.renderer.block.ButterflyJarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.CATERPILLAR_JAR.get(), com.otterly76.ott.client.renderer.block.CaterpillarJarRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.ELEVATOR.get(), com.otterly76.ott.client.render.blockentity.ElevatorArrowRenderer::new);
    }

    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.CREAKING, CreakingModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.PALE_OAK_BOAT, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.PALE_OAK_CHEST_BOAT, ChestBoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.OTT_WOOD_SET_BOAT, BoatModel::createBodyModel);
        event.registerLayerDefinition(ModModelLayers.OTT_WOOD_SET_CHEST_BOAT, ChestBoatModel::createBodyModel);

        event.registerLayerDefinition(ModModelLayers.COLD_PIG, ColdPigModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.COLD_CHICKEN, ColdChickenModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.COPPER_GOLEM, com.otterly76.ott.client.model.CopperGolemModel::createBodyLayer);

        event.registerLayerDefinition(ModModelLayers.HAPPY_GHAST, () -> HappyGhastModel.createBodyLayer(CubeDeformation.NONE));
        event.registerLayerDefinition(ModModelLayers.HAPPY_GHAST_HARNESS, HappyGhastHarnessModel::createHarnessLayer);
        event.registerLayerDefinition(ModModelLayers.HAPPY_GHAST_ROPES, () -> HappyGhastModel.createBodyLayer(new CubeDeformation(0.2F)));

        event.registerLayerDefinition(ModModelLayers.NAUTILUS, NautilusModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.NAUTILUS_BABY, NautilusModel::createBabyBodyLayer);
        event.registerLayerDefinition(ModModelLayers.NAUTILUS_ARMOR, NautilusArmorModel::createBodyLayer);
        event.registerLayerDefinition(ModModelLayers.NAUTILUS_SADDLE, NautilusSaddleModel::createSaddleLayer);
    }

    public static void onRegisterSpriteLoader(RegisterSpriteSourceTypesEvent event) {
        event.register(Ott.resource("fxsprite"), FXAtlasSpriteSource.TYPE);
    }

    public static void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tint) -> level != null && pos != null ? LeafColors.getAverageDryFoliageColor(pos) : -10732494, ModBlocks.LEAF_LITTER.get());
        event.register((state, level, pos, tint) -> level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.getDefaultColor(), ModBlocks.BUSH.get(), ModBlocks.BIG_LILY_PAD.get());
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

        ModBlocks.COPPER_WATER_CAULDRONS.values().forEach(blockSupplier -> {
            event.register((state, level, pos, tint) -> tint == 0 && level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1, blockSupplier.get());
        });

        event.register((state, level, pos, tint) -> tint == 0 && level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1, ModBlocks.WEATHERING_STATION.get());
        event.register((state, level, pos, tint) -> tint == 0 && level != null && pos != null ? BiomeColors.getAverageWaterColor(level, pos) : -1,
                ModBlocks.STONE_BRICKS_POOL.get(), ModBlocks.STONE_BRICKS_SMALL_POOL.get(),
                ModBlocks.STONE_BRICKS_FAUCET.get(), ModBlocks.STONE_BRICKS_WATER_JET.get(), ModBlocks.WATER_SOURCE_TRICKLE.get());

        for (var patternMap : java.util.List.of(
                ModBlocks.PATTERN_BLOCKS, ModBlocks.PATTERN_PLATES, ModBlocks.PATTERN_EDGES,
                ModBlocks.PATTERN_BEAMS, ModBlocks.PATTERN_PERGOLAS, ModBlocks.PATTERN_WINDOWS)) {
            patternMap.forEach((pattern, colorMap) -> colorMap.forEach((colorName, block) ->
                    com.otterly76.ott.color.ModPatterns.ALL_COLORS.stream()
                            .filter(c -> c.name().equals(colorName))
                            .findFirst()
                            .ifPresent(info -> event.register((state, level, pos, tint) -> info.color(), block.get()))));
        }

        ModBlocks.ELEVATORS.forEach((colorName, block) -> {
            com.otterly76.ott.color.ModPatterns.ALL_COLORS.stream()
                    .filter(c -> c.name().equals(colorName))
                    .findFirst()
                    .ifPresent(info -> event.register((state, level, pos, tint) -> {
                        if (level != null && pos != null) {
                            net.minecraft.world.level.block.entity.BlockEntity be = level.getBlockEntity(pos);
                            if (be instanceof com.otterly76.ott.block.entity.ElevatorBlockEntity elevator) {
                                net.minecraft.world.level.block.state.BlockState camo = elevator.getCamoState();
                                if (camo != null && !camo.isAir()) {
                                    return net.minecraft.client.Minecraft.getInstance().getBlockColors().getColor(camo, level, pos, tint);
                                }
                            }
                        }
                        return info.color();
                    }, block.get()));
        });
    }

    public static void registerItemColors(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> event.getBlockColors().getColor(((net.minecraft.world.item.BlockItem)stack.getItem()).getBlock().defaultBlockState(), null, null, tintIndex), ModBlocks.BUSH.get(), ModBlocks.WILDFLOWERS.get(), ModBlocks.WEATHERING_STATION.get(), ModBlocks.BIG_LILY_PAD.get());

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

        for (var patternMap : java.util.List.of(
                ModBlocks.PATTERN_BLOCKS, ModBlocks.PATTERN_PLATES, ModBlocks.PATTERN_EDGES,
                ModBlocks.PATTERN_BEAMS, ModBlocks.PATTERN_PERGOLAS, ModBlocks.PATTERN_WINDOWS)) {
            patternMap.forEach((pattern, colorMap) -> colorMap.forEach((colorName, block) ->
                    com.otterly76.ott.color.ModPatterns.ALL_COLORS.stream()
                            .filter(c -> c.name().equals(colorName))
                            .findFirst()
                            .ifPresent(info -> event.register((stack, tintIndex) -> tintIndex == 0 ? info.color() : -1, block.get()))));
        }

        ModBlocks.ELEVATORS.forEach((colorName, block) -> {
            com.otterly76.ott.color.ModPatterns.ALL_COLORS.stream()
                    .filter(c -> c.name().equals(colorName))
                    .findFirst()
                    .ifPresent(info -> event.register((stack, tintIndex) -> tintIndex == 0 ? info.color() : -1, block.get()));
        });

        event.register((stack, tintIndex) -> {
            if (tintIndex == 0) return 0xFFFFC400;
            return -1;
        }, ModItems.TORCH_ARROW.get());

        event.register((stack, tintIndex) -> -1,
                ModItems.DUCK_SPAWN_EGG.get(), ModItems.CATFISH_SPAWN_EGG.get(), ModItems.BASS_SPAWN_EGG.get(),
                ModItems.BLUEJAY_SPAWN_EGG.get(), ModItems.CANARY_SPAWN_EGG.get(), ModItems.CARDINAL_SPAWN_EGG.get(),
                ModItems.FINCH_SPAWN_EGG.get(), ModItems.ROBIN_SPAWN_EGG.get(), ModItems.SPARROW_SPAWN_EGG.get(),
                ModItems.BROWN_BEAR_SPAWN_EGG.get(), ModItems.BLACK_BEAR_SPAWN_EGG.get(), ModItems.DEER_SPAWN_EGG.get(),
                ModItems.REINDEER_SPAWN_EGG.get(), ModItems.WHITE_DEER_SPAWN_EGG.get(), ModItems.BUTTERFLY_SPAWN_EGG.get(),
                ModItems.CATERPILLAR_SPAWN_EGG.get(), ModItems.FIREFLY_SPAWN_EGG.get(), ModItems.SMALL_FIREFLY_SPAWN_EGG.get(), ModItems.DRAGONFLY_SPAWN_EGG.get(), ModItems.ALLIGATOR_SPAWN_EGG.get(),
                ModItems.ELEPHANT_SPAWN_EGG.get(), ModItems.GIRAFFE_SPAWN_EGG.get(), ModItems.HIPPO_SPAWN_EGG.get(),
                ModItems.LION_SPAWN_EGG.get(), ModItems.RHINO_SPAWN_EGG.get(),
                ModItems.LIZARD_SPAWN_EGG.get(), ModItems.SNAIL_SPAWN_EGG.get(), ModItems.TORTOISE_SPAWN_EGG.get(),
                ModItems.VULTURE_SPAWN_EGG.get(), ModItems.ZEBRA_SPAWN_EGG.get(), ModItems.MOOSE_SPAWN_EGG.get(),
                ModItems.MAMMOTH_SPAWN_EGG.get(), ModItems.MYCELIUM_MAMMOTH_SPAWN_EGG.get(), ModItems.FENNEC_FOX_SPAWN_EGG.get(),
                ModItems.CAPYBARA_SPAWN_EGG.get(), ModItems.HEDGEHOG_SPAWN_EGG.get(), ModItems.LARGE_JELLYFISH_SPAWN_EGG.get(),
                ModItems.KIWI_SPAWN_EGG.get(), ModItems.PENGUIN_SPAWN_EGG.get(), ModItems.SEAL_SPAWN_EGG.get(),
                ModItems.SEA_URCHIN_SPAWN_EGG.get(),
                ModItems.COPPER_GOLEM_SPAWN_EGG.get(),
                ModItems.CREAKING_SPAWN_EGG.get(), ModItems.HAPPY_GHAST_SPAWN_EGG.get(),
                net.minecraft.world.item.Items.ALLAY_SPAWN_EGG, net.minecraft.world.item.Items.ARMADILLO_SPAWN_EGG,
                net.minecraft.world.item.Items.AXOLOTL_SPAWN_EGG, net.minecraft.world.item.Items.BAT_SPAWN_EGG,
                net.minecraft.world.item.Items.BEE_SPAWN_EGG, net.minecraft.world.item.Items.BLAZE_SPAWN_EGG,
                net.minecraft.world.item.Items.BOGGED_SPAWN_EGG, net.minecraft.world.item.Items.BREEZE_SPAWN_EGG,
                net.minecraft.world.item.Items.CAT_SPAWN_EGG, net.minecraft.world.item.Items.CAVE_SPIDER_SPAWN_EGG,
                net.minecraft.world.item.Items.CHICKEN_SPAWN_EGG, net.minecraft.world.item.Items.COD_SPAWN_EGG,
                net.minecraft.world.item.Items.COW_SPAWN_EGG, net.minecraft.world.item.Items.CREEPER_SPAWN_EGG,
                net.minecraft.world.item.Items.DOLPHIN_SPAWN_EGG, net.minecraft.world.item.Items.DONKEY_SPAWN_EGG,
                net.minecraft.world.item.Items.DROWNED_SPAWN_EGG, net.minecraft.world.item.Items.ELDER_GUARDIAN_SPAWN_EGG,
                net.minecraft.world.item.Items.ENDERMAN_SPAWN_EGG, net.minecraft.world.item.Items.ENDERMITE_SPAWN_EGG,
                net.minecraft.world.item.Items.EVOKER_SPAWN_EGG, net.minecraft.world.item.Items.FOX_SPAWN_EGG,
                net.minecraft.world.item.Items.FROG_SPAWN_EGG, net.minecraft.world.item.Items.GHAST_SPAWN_EGG,
                net.minecraft.world.item.Items.GLOW_SQUID_SPAWN_EGG, net.minecraft.world.item.Items.GOAT_SPAWN_EGG,
                net.minecraft.world.item.Items.GUARDIAN_SPAWN_EGG, net.minecraft.world.item.Items.HOGLIN_SPAWN_EGG,
                net.minecraft.world.item.Items.HORSE_SPAWN_EGG, net.minecraft.world.item.Items.HUSK_SPAWN_EGG,
                net.minecraft.world.item.Items.IRON_GOLEM_SPAWN_EGG, net.minecraft.world.item.Items.LLAMA_SPAWN_EGG,
                net.minecraft.world.item.Items.MAGMA_CUBE_SPAWN_EGG, net.minecraft.world.item.Items.MOOSHROOM_SPAWN_EGG,
                net.minecraft.world.item.Items.MULE_SPAWN_EGG, net.minecraft.world.item.Items.OCELOT_SPAWN_EGG,
                net.minecraft.world.item.Items.PANDA_SPAWN_EGG, net.minecraft.world.item.Items.PARROT_SPAWN_EGG,
                net.minecraft.world.item.Items.PHANTOM_SPAWN_EGG, net.minecraft.world.item.Items.PIGLIN_BRUTE_SPAWN_EGG,
                net.minecraft.world.item.Items.PIGLIN_SPAWN_EGG, net.minecraft.world.item.Items.PIG_SPAWN_EGG,
                net.minecraft.world.item.Items.PILLAGER_SPAWN_EGG, net.minecraft.world.item.Items.POLAR_BEAR_SPAWN_EGG,
                net.minecraft.world.item.Items.PUFFERFISH_SPAWN_EGG, net.minecraft.world.item.Items.RABBIT_SPAWN_EGG,
                net.minecraft.world.item.Items.RAVAGER_SPAWN_EGG, net.minecraft.world.item.Items.SALMON_SPAWN_EGG,
                net.minecraft.world.item.Items.SHEEP_SPAWN_EGG, net.minecraft.world.item.Items.SHULKER_SPAWN_EGG,
                net.minecraft.world.item.Items.SILVERFISH_SPAWN_EGG, net.minecraft.world.item.Items.SKELETON_HORSE_SPAWN_EGG,
                net.minecraft.world.item.Items.SKELETON_SPAWN_EGG, net.minecraft.world.item.Items.SLIME_SPAWN_EGG,
                net.minecraft.world.item.Items.SNIFFER_SPAWN_EGG, net.minecraft.world.item.Items.SNOW_GOLEM_SPAWN_EGG,
                net.minecraft.world.item.Items.SPIDER_SPAWN_EGG, net.minecraft.world.item.Items.SQUID_SPAWN_EGG,
                net.minecraft.world.item.Items.STRAY_SPAWN_EGG, net.minecraft.world.item.Items.STRIDER_SPAWN_EGG,
                net.minecraft.world.item.Items.TADPOLE_SPAWN_EGG, net.minecraft.world.item.Items.TRADER_LLAMA_SPAWN_EGG,
                net.minecraft.world.item.Items.TROPICAL_FISH_SPAWN_EGG, net.minecraft.world.item.Items.TURTLE_SPAWN_EGG,
                net.minecraft.world.item.Items.VEX_SPAWN_EGG, net.minecraft.world.item.Items.VILLAGER_SPAWN_EGG,
                net.minecraft.world.item.Items.VINDICATOR_SPAWN_EGG, net.minecraft.world.item.Items.WANDERING_TRADER_SPAWN_EGG,
                net.minecraft.world.item.Items.WARDEN_SPAWN_EGG, net.minecraft.world.item.Items.WITCH_SPAWN_EGG,
                net.minecraft.world.item.Items.WITHER_SKELETON_SPAWN_EGG, net.minecraft.world.item.Items.WITHER_SPAWN_EGG,
                net.minecraft.world.item.Items.WOLF_SPAWN_EGG, net.minecraft.world.item.Items.ZOGLIN_SPAWN_EGG,
                net.minecraft.world.item.Items.ZOMBIE_HORSE_SPAWN_EGG, net.minecraft.world.item.Items.ZOMBIE_SPAWN_EGG,
                net.minecraft.world.item.Items.ZOMBIE_VILLAGER_SPAWN_EGG, net.minecraft.world.item.Items.ZOMBIFIED_PIGLIN_SPAWN_EGG,
                ModItems.FIDDLER_CRAB_SPAWN_EGG.get(), ModItems.GLARE_SPAWN_EGG.get(), ModItems.ICEOLOGER_SPAWN_EGG.get(), ModItems.ILLUSIONER_SPAWN_EGG.get(),
                ModItems.MAULER_SPAWN_EGG.get(), ModItems.RASCAL_SPAWN_EGG.get(), ModItems.TUFF_GOLEM_SPAWN_EGG.get(),
                ModItems.WILDFIRE_SPAWN_EGG.get(), ModItems.PARCHED_SPAWN_EGG.get(), ModItems.COCONUT_CRAB_SPAWN_EGG.get(), ModItems.DUMBO_OCTOPUS_SPAWN_EGG.get(),
                ModItems.FERRET_SPAWN_EGG.get(), ModItems.JUMPING_SPIDER_SPAWN_EGG.get(), ModItems.KOI_FISH_SPAWN_EGG.get(),
                ModItems.OTTER_SPAWN_EGG.get(), ModItems.RED_PANDA_SPAWN_EGG.get(), ModItems.SEA_BUNNY_SPAWN_EGG.get(),
                ModItems.WATER_BUFFALO_SPAWN_EGG.get());

        // Water tinting for water-feature item models
        event.register((stack, tintIndex) -> tintIndex == 0 ? 0x3F76E4 : -1,
                ModBlocks.STONE_BRICKS_FAUCET.get(), ModBlocks.STONE_BRICKS_WATER_JET.get(), ModBlocks.WATER_SOURCE_TRICKLE.get());
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        ItemPropertyRegistrar.bootstrap();
        Minecraft.getInstance().toast = new BetterToastComponent();
        BetterToastComponent.handleToastReloc();
        BetterToastComponent.handleBlockedClasses();
    }

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        com.otterly76.ott.client.handler.NameTagTooltipHandler.onItemTooltip(event.getItemStack(), event.getToolTip(), event.getContext(), event.getEntity(), event.getFlags());
    }

    public static void applyWaterTint(net.minecraft.client.particle.Particle particle, net.minecraft.client.multiplayer.ClientLevel clientLevel, net.minecraft.core.BlockPos blockPos) {
        Color waterColor = new Color(BiomeColors.getAverageWaterColor(clientLevel, blockPos));
        Color fogColor = new Color(clientLevel.getBiome(blockPos).value().getFogColor());
        float rCol = Mth.lerp((float)OttConfig.WEATHER.TINT_MIX.get() / 100.0F, (float)waterColor.getRed(), (float)fogColor.getRed()) / 255.0F;
        float gCol = Mth.lerp((float)OttConfig.WEATHER.TINT_MIX.get() / 100.0F, (float)waterColor.getGreen(), (float)fogColor.getGreen()) / 255.0F;
        float bCol = Mth.lerp((float)OttConfig.WEATHER.TINT_MIX.get() / 100.0F, (float)waterColor.getBlue(), (float)fogColor.getBlue()) / 255.0F;
        particle.setColor(rCol, gCol, bCol);
    }

    public static NativeImage loadTexture(ResourceLocation resourceLocation) throws java.io.IOException {
        Resource resource = Minecraft.getInstance().getResourceManager().getResourceOrThrow(resourceLocation);
        try (java.io.InputStream inputStream = resource.open()) {
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

    public static int getRippleResolution(java.util.List<SpriteContents> contents) {
        try {
            if (OttConfig.WEATHER.USE_RESOURCEPACK_RESOLUTION.get()) {
                int max = 0;
                for (SpriteContents content : contents) {
                    if (content.width() > max) {
                        max = content.width();
                    }
                }
                return max;
            }
            return OttConfig.WEATHER.RIPPLE_RESOLUTION.get();
        } catch (Exception ignored) {
            return 16;
        }
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
        drawCirclePixels(image, centerX, centerY, x, y, colorint);
        while(y >= x) {
            if (d > 0) {
                --y;
                d = d + 4 * (x - y) + 10;
            } else {
                d = d + 4 * x + 6;
            }

            ++x;
            drawCirclePixels(image, centerX, centerY, x, y, colorint);
        }
    }

    private static void drawCirclePixels(NativeImage image, int xc, int yc, int x, int y, int color) {
        image.setPixelRGBA(xc + x, yc + y, color);
        image.setPixelRGBA(xc - x, yc + y, color);
        image.setPixelRGBA(xc + x, yc - y, color);
        image.setPixelRGBA(xc - x, yc - y, color);
        image.setPixelRGBA(xc + y, yc + x, color);
        image.setPixelRGBA(xc - y, yc + x, color);
        image.setPixelRGBA(xc + y, yc - x, color);
        image.setPixelRGBA(xc - y, yc - x, color);
    }

    public static void registerRecipeBookCategories(net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent event) {
        // Map all woodcutting recipes to the STONECUTTER category so the recipe book doesn't warn about unknown categories
        event.registerRecipeCategoryFinder(com.otterly76.ott.registry.ModRecipeTypes.WOODCUTTING.get(),
                holder -> net.minecraft.client.RecipeBookCategories.STONECUTTER);
    }

    /** Wraps every registered elevator block model with ElevatorBakedModel for camo support. */
    public static void registerElevatorModelBaking(ModelEvent.ModifyBakingResult event) {
        for (ModelResourceLocation mrl : new java.util.ArrayList<>(event.getModels().keySet())) {
            ResourceLocation id = mrl.id();
            if (!id.getNamespace().equals(MOD_ID)) continue;
            String path = id.getPath();
            if (!path.endsWith("_elevator")) continue;

            BakedModel original = event.getModels().get(mrl);
            if (original != null) {
                event.getModels().put(mrl, new com.otterly76.ott.client.model.ElevatorBakedModel(original));
            }
        }
    }
}