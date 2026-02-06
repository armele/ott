package com.otterly76.ott.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.otterly76.ott.config.ConfigHandler.Harvest;
import com.otterly76.ott.platform.Services;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.DataMapHooks;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

public class HarvestUtils {
    public static boolean isVanilla(Block block) {
        return getBlockId(block).getNamespace().equals("minecraft");
    }

    public static boolean isBottomBlock(Block block) {
        return getBlockId(block).getPath().contains("_bottom");
    }

    public static ResourceLocation getBlockId(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public static String[] parseBlockString(String blockString) {
        boolean inBracket = false;

        for(int i = 0; i < blockString.length(); ++i) {
            char c = blockString.charAt(i);
            if (c == '[') {
                inBracket = true;
            } else if (c == ']') {
                inBracket = false;
            } else if (c == ',' && !inBracket) {
                return new String[]{blockString.substring(0, i), blockString.substring(i + 1)};
            }
        }

        return new String[]{blockString};
    }

    public static BlockState fromString(String key) {
        try {
            BlockStateParser.BlockResult result = BlockStateParser.parseForBlock(BuiltInRegistries.BLOCK.asLookup(), new StringReader(key), false);
            return result.blockState();
        } catch (CommandSyntaxException var2) {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Nullable
    public static BlockState getToolModifiedState(BlockState state, UseOnContext context, String toolActionType, boolean simulate) {
        BlockState var10000;
        switch (toolActionType) {
            case "axe_strip":
                var10000 = getAxeStrippingState(state);
                break;
            case "axe_scrape":
                var10000 = WeatheringCopper.getPrevious(state).orElse(null);
                break;
            case "axe_wax_off":
                var10000 = Optional.ofNullable(DataMapHooks.getBlockUnwaxed(state.getBlock())).map((blockx) -> blockx.withPropertiesOf(state)).orElse(null);
                break;
            case "shovel_flatten":
                var10000 = getShovelPathingState(state);
                break;
            case "hoe_till":
                Block block = state.getBlock();
                if (block == Blocks.ROOTED_DIRT) {
                    if (!simulate && !context.getLevel().isClientSide) {
                        Block.popResourceFromFace(context.getLevel(), context.getClickedPos(), context.getClickedFace(), new ItemStack(Items.HANGING_ROOTS));
                    }

                    var10000 = Blocks.DIRT.defaultBlockState();
                } else {
                    var10000 = (block == Blocks.GRASS_BLOCK || block == Blocks.DIRT_PATH || block == Blocks.DIRT || block == Blocks.COARSE_DIRT) && context.getLevel().getBlockState(context.getClickedPos().above()).isAir() ? (block == Blocks.COARSE_DIRT ? Blocks.DIRT.defaultBlockState() : Blocks.FARMLAND.defaultBlockState()) : null;
                }
                break;
            default:
                var10000 = null;
        }

        return var10000;
    }

    public static InteractionType getInteractionTypeForBlock(BlockState state, boolean canRightClick) {
        state = getModifiedState(state).getLeft();
        if (state.is(ModTags.Blocks.HARVEST_BLACKLIST)) {
            return HarvestUtils.InteractionType.NONE;
        } else if (canRightClick && Harvest.getRightClickBlocks().contains(state.getBlock())) {
            return HarvestUtils.InteractionType.CLICK;
        } else {
            return Harvest.getCrops().containsKey(state) ? HarvestUtils.InteractionType.HARVEST : HarvestUtils.InteractionType.NONE;
        }
    }

    public static Pair<BlockState, Boolean> getModifiedState(BlockState state) {
        AtomicBoolean useDefault = new AtomicBoolean(false);
        for (net.minecraft.world.level.block.state.properties.Property<?> property : state.getProperties()) {
            if (property.getName().equals("distance")) {
                useDefault.set(true);
                break;
            }
        }
        if (useDefault.get()) {
            Block block = BuiltInRegistries.BLOCK.get(BuiltInRegistries.BLOCK.getKey(state.getBlock()));
            if (block instanceof CropBlock cropBlock) {
                Integer age = state.getValue(cropBlock.getAgeProperty());
                state = cropBlock.defaultBlockState().setValue(cropBlock.getAgeProperty(), age);
            }
        }

        return Pair.of(state, useDefault.get());
    }

    public static boolean playerCanHarvest(Player player) {
        if (Harvest.allowFakePlayer()) {
            return true;
        } else {
            return !Services.PLATFORM.isFakePlayer(player);
        }
    }

    @Nullable
    private static BlockState getAxeStrippingState(BlockState state) {
        Block block = AxeItem.STRIPPABLES.get(state.getBlock());
        return block != null ? block.defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : null;
    }

    private static BlockState getShovelPathingState(BlockState state) {
        return ShovelItem.FLATTENABLES.get(state.getBlock());
    }

    public static boolean isBlockItem(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem() instanceof net.minecraft.world.item.BlockItem;
    }

    public static boolean isHoe(ItemStack stack) {
        return !stack.isEmpty() && (stack.getItem() instanceof HoeItem || stack.is(ItemTags.HOES));
    }

    public static int getHoeRange(ItemStack hoe) {
        int range = Harvest.getHoeTools().getOrDefault(hoe.getItem(), 1);
        int expandedRange = 0;
        if (Harvest.expandHoeRangeEnchanted()) {
            ItemEnchantments enchantments = hoe.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

            for (Holder<Enchantment> enchantmentHolder : enchantments.keySet()) {
                if (enchantmentHolder.is(Enchantments.EFFICIENCY)) {
                    expandedRange = enchantments.getLevel(enchantmentHolder);
                }
            }
        }

        return Math.min(range + expandedRange, Harvest.maxHoeExpansionRange());
    }

    public static int getBaseHoeRange(int level) {
        if (Harvest.expandHoeRange()) {
            return level <= 2 ? Harvest.smallTierExpansionRange() : Harvest.highTierExpansionRange();
        } else {
            return 1;
        }
    }

    public static ResourceLocation getItemId(ItemStack stack) {
        return Services.PLATFORM.getResourceLocation(stack.getItem());
    }

    // Dummy calls to satisfy IDE warnings about unused Access Transformers
    private static void unusedATSuppression() {
        //noinspection ConstantConditions
        if (false) {
            ((BeetrootBlock) null).getAgeProperty();
            ((TorchflowerCropBlock) null).getAgeProperty();
        }
    }

    public enum InteractionType {
        NONE,
        CLICK,
        HARVEST
    }
}