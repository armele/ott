package com.otterly76.ott.config;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import com.otterly76.ott.util.HarvestUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.Tool;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ConfigHandler {
    // --- JSON Config ---
    private static ConfigCodec LOADED_CONFIG;

    public static ConfigCodec getConfig() {
        return LOADED_CONFIG != null ? LOADED_CONFIG : ConfigCodec.DEFAULT;
    }

    public static void load(Path path) {
        if (!Files.isRegularFile(path)) {
            write(path);
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            JsonElement json = JsonParser.parseReader(reader);
            Optional<ConfigCodec> result = ConfigCodec.CODEC.parse(JsonOps.INSTANCE, json).result();
            if (result.isEmpty()) {
                throw new JsonParseException("Invalid codec");
            }

            LOADED_CONFIG = result.get();
        } catch (Exception ignored) {
        }

        write(path);
    }

    private static void write(Path path) {
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                JsonElement element = ConfigCodec.CODEC.encodeStart(JsonOps.INSTANCE, LOADED_CONFIG)
                        .result()
                        .orElseThrow(() -> new IllegalStateException("Failed to encode configuration to JSON"));

                StringWriter stringWriter = new StringWriter();
                JsonWriter jsonWriter = new JsonWriter(stringWriter);
                jsonWriter.setIndent("  ");
                GsonHelper.writeValue(jsonWriter, element, Comparator.naturalOrder());
                writer.write(commentHack(stringWriter.toString()));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String commentHack(String json) {
        return json.replaceAll("\"__.\": \"", "// ").replaceAll("\"...__\": \"", "// ").replace("\",", "");
    }

    // --- Harvest Config Data ---
    private static final Map<BlockState, BlockState> crops = Maps.newHashMap();
    private static final Set<Block> rightClickBlocks = Sets.newHashSet();
    private static final Map<Item, Integer> hoeTools = Maps.newHashMap();

    public static void initHarvest() {
        crops.clear();
        rightClickBlocks.clear();
        hoeTools.clear();
        if (Harvest.autoConfigMods()) {
            for(Block block : BuiltInRegistries.BLOCK) {
                if (Harvest.isNotBlacklistedMod(block) && Harvest.isNotBlacklistedCrop(block)) {
                    BlockState defaultState = block.defaultBlockState();
                    if (block instanceof CropBlock cropBlock) {
                        BlockState minAgeState = defaultState;
                        BlockState maxAgeState = cropBlock.getStateForAge(cropBlock.getMaxAge());

                        for (net.minecraft.world.level.block.state.properties.Property<?> prop : defaultState.getProperties()) {
                            if (prop.getName().equals("upper") || prop.getName().equals("top")) {
                                if (prop instanceof BooleanProperty boolProp) {
                                    minAgeState = minAgeState.setValue(boolProp, true);
                                    maxAgeState = maxAgeState.setValue(boolProp, true);
                                }
                            } else if (prop.equals(DoublePlantBlock.HALF)) {
                                minAgeState = minAgeState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
                                maxAgeState = maxAgeState.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
                            }
                        }

                        if (!HarvestUtils.isBottomBlock(block)) {
                            crops.put(maxAgeState, minAgeState);
                        }
                    } else if (block instanceof CocoaBlock cocoaBlock) {
                        BlockStateProperties.HORIZONTAL_FACING.getAllValues().forEach((direction) -> {
                            BlockState zeroState = cocoaBlock.defaultBlockState().setValue(CocoaBlock.AGE, 0).setValue(CocoaBlock.FACING, direction.value());
                            BlockState maxAgeState = cocoaBlock.defaultBlockState().setValue(CocoaBlock.AGE, 2).setValue(CocoaBlock.FACING, direction.value());
                            crops.put(maxAgeState, zeroState);
                        });
                    } else {
                        IntegerProperty ageProp = null;
                        for (net.minecraft.world.level.block.state.properties.Property<?> prop : defaultState.getProperties()) {
                            if (prop instanceof IntegerProperty intProp && (prop.getName().equals("age") || prop.getName().equals("growth"))) {
                                ageProp = intProp;
                                break;
                            }
                        }

                        if (ageProp != null) {
                            int maxAge = Collections.max(ageProp.getPossibleValues());
                            int minAge = Collections.min(ageProp.getPossibleValues());
                            BlockState maxAgeState = defaultState.setValue(ageProp, maxAge);
                            BlockState minAgeState = defaultState.setValue(ageProp, minAge);
                            crops.put(maxAgeState, minAgeState);
                        } else if ((block instanceof BushBlock || block instanceof GrowingPlantBlock) && block instanceof BonemealableBlock) {
                            rightClickBlocks.add(block);
                        }
                    }
                }
            }
        }

        for(String cropKey : Harvest.harvestableCrops()) {
            String[] parts = HarvestUtils.parseBlockString(cropKey);
            BlockState initial = HarvestUtils.fromString(cropKey);
            Block block = initial.getBlock();
            if (block != Blocks.AIR && Harvest.isNotBlacklistedCrop(block) && Harvest.isNotBlacklistedMod(block)) {
                BlockState result;
                if (parts.length > 1) {
                    result = HarvestUtils.fromString(parts[1]);
                } else {
                    result = block.defaultBlockState();
                }

                crops.put(initial, result);
            }
        }

        for(String blockKey : Harvest.harvestableBlocks()) {
            Block block = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(blockKey));
            if (block != Blocks.AIR && Harvest.isNotBlacklistedCrop(block) && Harvest.isNotBlacklistedMod(block)) {
                rightClickBlocks.add(block);
            }
        }

        BuiltInRegistries.ITEM.forEach((item) -> {
            if (item instanceof DiggerItem digger) {
                Tool tool = digger.components().get(DataComponents.TOOL);
                TagKey<Block> tagKey = null;
                if (tool != null) {
                    for(Tool.Rule rule : tool.rules()) {
                        if (rule.correctForDrops().isPresent()) {
                            Optional<TagKey<Block>> optionalBlockTagKey = rule.blocks().unwrapKey();
                            if (optionalBlockTagKey.isPresent()) {
                                tagKey = optionalBlockTagKey.get();
                            }
                        }
                    }
                }

                if (tagKey == BlockTags.MINEABLE_WITH_HOE) {
                    int tier = 0;

                    try {
                        tier = Tiers.valueOf(digger.getTier().toString()).ordinal();
                    } catch (Exception ignored) {
                    }

                    hoeTools.put(digger, HarvestUtils.getBaseHoeRange(tier));
                }
            }
        });

        for(String hoeItem : Harvest.hoeItems()) {
            String[] parts = hoeItem.split("-");
            int range = HarvestUtils.getBaseHoeRange(Integer.parseInt(parts[1]));
            ResourceLocation loc = ResourceLocation.parse(parts[0]);
            Item item = BuiltInRegistries.ITEM.get(loc);
            hoeTools.put(item, range);
        }
    }

    public static class Harvest {
        public static boolean allowEmptyHand() {
            return OttConfig.HARVEST.ALLOW_EMPTY_HAND.get();
        }

        public static boolean damageTool() {
            return OttConfig.HARVEST.DAMAGE_TOOL.get();
        }

        public static boolean autoConfigMods() {
            return OttConfig.HARVEST.AUTO_CONFIG_MODS.get();
        }

        public static int xpFromHarvestChance() {
            return OttConfig.HARVEST.XP_FROM_HARVEST_CHANCE.get();
        }

        public static int xpFromHarvestAmount() {
            return OttConfig.HARVEST.XP_FROM_HARVEST_AMOUNT.get();
        }

        public static boolean xpFromHarvestUseRange() {
            return OttConfig.HARVEST.XP_FROM_HARVEST_USE_RANGE.get();
        }

        public static Pair<Integer, Integer> xpFromHarvestRangeAmount() {
            String[] amounts = OttConfig.HARVEST.XP_FROM_HARVEST_RANGE_AMOUNT.get().split("-");
            try {
                int left = Integer.parseInt(amounts[0]);
                int right = Integer.parseInt(amounts[1]);
                return left > right ? Pair.of(0, 3) : Pair.of(left, right);
            } catch (Exception e) {
                return Pair.of(0, 3);
            }
        }

        public static List<? extends String> harvestableCrops() {
            return OttConfig.HARVEST.HARVESTABLE_CROPS.get();
        }

        public static List<? extends String> harvestableBlocks() {
            return OttConfig.HARVEST.HARVESTABLE_BLOCKS.get();
        }

        public static List<? extends String> hoeItems() {
            return OttConfig.HARVEST.HOE_ITEMS.get();
        }

        public static Set<Block> getRightClickBlocks() {
            return rightClickBlocks;
        }

        public static Map<BlockState, BlockState> getCrops() {
            return crops;
        }

        public static boolean expandHoeRange() {
            return OttConfig.HARVEST.EXPAND_HOE_RANGE.get();
        }

        public static int smallTierExpansionRange() {
            return OttConfig.HARVEST.SMALL_TIER_EXPANSION_RANGE.get();
        }

        public static int highTierExpansionRange() {
            return OttConfig.HARVEST.HIGH_TIER_EXPANSION_RANGE.get();
        }

        public static boolean expandHoeRangeEnchanted() {
            return OttConfig.HARVEST.EXPAND_HOE_RANGE_ENCHANTED.get();
        }

        public static int maxHoeExpansionRange() {
            return OttConfig.HARVEST.MAX_HOE_EXPANSION_RANGE.get();
        }

        public static Map<Item, Integer> getHoeTools() {
            return hoeTools;
        }

        public static boolean allowFakePlayer() {
            return OttConfig.HARVEST.ALLOW_FAKE_PLAYER.get();
        }

        public static boolean isNotBlacklistedCrop(Block block) {
            return !OttConfig.HARVEST.BLACKLIST_CROPS.get().contains(HarvestUtils.getBlockId(block).toString());
        }

        public static boolean isNotBlacklistedMod(Block block) {
            return !OttConfig.HARVEST.BLACKLIST_MODS.get().contains(HarvestUtils.getBlockId(block).getNamespace());
        }

        public static boolean isBlacklistHeldItem(ItemStack stack) {
            return OttConfig.HARVEST.BLACKLIST_HELD_ITEMS.get().contains(HarvestUtils.getItemId(stack).toString());
        }

        public static boolean replantCrops() {
            return OttConfig.HARVEST.REPLANT_CROPS.get();
        }
    }

    static {
        LOADED_CONFIG = ConfigCodec.DEFAULT;
    }
}
